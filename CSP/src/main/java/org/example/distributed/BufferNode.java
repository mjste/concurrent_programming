package org.example.distributed;

import org.jcsp.lang.*;

public class BufferNode implements CSProcess {
    private final Long id;
    private final int delay;
    private final int[] buffer;
    private int itemsInBuffer = 0;
    private final boolean randomDelay;
    private boolean consumerWaiting = false;
    private boolean hasToken = false;
    private boolean running = true;
    private final boolean verbose;
    private final AltingChannelInputInt producerChannel;
    private final AltingChannelInputInt requestChannel;
    private final AltingChannelInputInt predecessorInChannel;
    private final AltingChannelInputInt successorInChannel;
    private final ChannelOutputInt consumerChannel;
    private final ChannelOutputInt predecessorOutChannel;
    private final ChannelOutputInt successorOutChannel;
    public BufferNode(AltingChannelInputInt producerChannel,
                       AltingChannelInputInt requestChannel,
                       AltingChannelInputInt predecessorInChannel,
                       AltingChannelInputInt successorInChannel,
                       ChannelOutputInt consumerChannel,
                       ChannelOutputInt predecessorOutChannel,
                       ChannelOutputInt successorOutChannel,
                       Long id, int delay, boolean randomDelay, int bufferSize, boolean verbose) {
        this.id = id;
        this.producerChannel = producerChannel;
        this.requestChannel = requestChannel;
        this.predecessorInChannel = predecessorInChannel;
        this.successorInChannel = successorInChannel;
        this.consumerChannel = consumerChannel;
        this.predecessorOutChannel = predecessorOutChannel;
        this.successorOutChannel = successorOutChannel;
        this.delay = delay;
        this.randomDelay = randomDelay;
        this.buffer = new int[bufferSize];
        this.verbose = verbose;
    }

    public int getItemsInBuffer() {
        return itemsInBuffer;
    }

    public void setHasToken(boolean hasToken) {
        this.hasToken = hasToken;
    }

    @Override
    public void run() {
        Guard[] guards = {
                producerChannel,
                requestChannel,
                predecessorInChannel,
        };
        Alternative alternative = new Alternative(guards);

        while (running) {
            switch (InputType.values()[alternative.select()]) {
                case PRODUCER -> {
                    if (itemsInBuffer < buffer.length) {
                        int value = producerChannel.read();
                        buffer[itemsInBuffer] = value;
                        itemsInBuffer++;
                        if (verbose) System.out.printf("Buffer %d received %d from producer\n", id, value);
                    }
                }
                case REQUEST -> {
                    if (itemsInBuffer > 0) {
                        requestChannel.read();
                        if (verbose) System.out.printf("Buffer %d received request from consumer\n", id);
                        int value = buffer[itemsInBuffer-1];
                        itemsInBuffer--;
                        consumerChannel.write(value);
                        if (verbose) System.out.printf("Buffer %d sent %d to consumer\n", id, value);
                    } else {
                        consumerWaiting = true;
                    }
                }
                case PREDECESSOR_TOKEN -> {
                    int token;
                    hasToken = true;
                    TokenType tokenType;

                    // Take care of predecessor
                    token = predecessorInChannel.read();
                    tokenType = TokenType.values()[token];
                    if (tokenType == TokenType.FULL_TOKEN) {
                        if (itemsInBuffer > 0.45 * buffer.length) {
                            if (verbose) System.out.printf("Buffer %d got token FULL, but does not take\n", id);
                            predecessorOutChannel.write(TokenType.SKIP_TOKEN.ordinal());
                        } else {
                            predecessorOutChannel.write(TokenType.TAKE_TOKEN.ordinal());
                            int value = predecessorInChannel.read();
                            buffer[itemsInBuffer] = value;
                            itemsInBuffer++;
                            if (verbose) System.out.printf("Buffer %d got token FULL, and took %d\n", id, value);
                        }
                    } else {
                        if (verbose) System.out.printf("Buffer %d got token EMPTY, and ignored it\n", id);
                    }
                }
            }
            int token;
            TokenType tokenType;
            if (itemsInBuffer > 0) {
                if (consumerWaiting) {
                    if (verbose) {
                        System.out.printf("Buffer %d has last value %d, and sends it to consumer\n", id, buffer[itemsInBuffer-1]);
                    }
                    requestChannel.read();
                    int value = buffer[itemsInBuffer-1];
                    itemsInBuffer--;
                    consumerChannel.write(value);
                    consumerWaiting = false;
                } else if (hasToken && itemsInBuffer > 0.55 * buffer.length) {
                    if (verbose) {
                        System.out.printf("Buffer %d has last value %d, and sends FULL to successor\n", id, buffer[itemsInBuffer-1]);
                    }
                    successorOutChannel.write(TokenType.FULL_TOKEN.ordinal());
                    token = successorInChannel.read();
                    tokenType = TokenType.values()[token];
                    if (tokenType == TokenType.TAKE_TOKEN) {
                        int value = buffer[itemsInBuffer-1];
                        itemsInBuffer--;
                        successorOutChannel.write(value);
                    }
                    hasToken = false;
                } else if (hasToken) {
                    if (verbose) System.out.printf("Buffer %d has low occupancy, and sends EMPTY to successor\n", id);
                    successorOutChannel.write(TokenType.EMPTY_TOKEN.ordinal());
                    hasToken = false;
                }
                // not consumer waiting and no token but has value
            } else {
                // has empty buffer
                if (hasToken) {
                    if (verbose) System.out.printf("Buffer %d has empty buffer, and sends EMPTY to successor\n", id);
                    hasToken = false;
                    successorOutChannel.write(TokenType.EMPTY_TOKEN.ordinal());
                }
                // no value and no token
            }
            Sleeper.sleep(delay, randomDelay);
        }
    }

    public void stop() {
        running = false;
    }
}
