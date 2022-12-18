package org.example.distributed;

import org.jcsp.lang.*;

public class ElementNode implements CSProcess {
    private final Long id;
    private boolean hasValue = false;
    private final boolean randomDelay;
    private final int delay;
    private int storedValue;
    private boolean consumerWaiting = false;
    private boolean hasToken = false;
    private final boolean verbose = false;
    private final AltingChannelInputInt producerChannel;
    private final AltingChannelInputInt requestChannel;
    private final AltingChannelInputInt predecessorInChannel;
    private final AltingChannelInputInt successorInChannel;
    private final ChannelOutputInt consumerChannel;
    private final ChannelOutputInt predecessorOutChannel;
    private final ChannelOutputInt successorOutChannel;

    public ElementNode(AltingChannelInputInt producerChannel,
                       AltingChannelInputInt requestChannel,
                       AltingChannelInputInt predecessorInChannel,
                       AltingChannelInputInt successorInChannel,
                       ChannelOutputInt consumerChannel,
                       ChannelOutputInt predecessorOutChannel,
                       ChannelOutputInt successorOutChannel,
                       Long id, int delay, boolean randomDelay) {
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


        while (true) {
            switch (InputType.values()[alternative.select()]) {
                case PRODUCER -> {
                    if (!hasValue) {
                        hasValue = true;
                        storedValue = producerChannel.read();
                        if (verbose) System.out.printf("Buffer %d received %d from producer\n", id, storedValue);

                    }
                }
                case REQUEST -> {
                    if (hasValue) {
                        requestChannel.read();
                        if (verbose) System.out.printf("Buffer %d received request from consumer\n", id);

                        hasValue = false;
                        consumerChannel.write(storedValue);
                        if (verbose) System.out.printf("Buffer %d sent %d to consumer\n", id, storedValue);
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
                        if (hasValue) {
                            if (verbose) System.out.printf("Buffer %d got token FULL, but does not take\n", id);
                            predecessorOutChannel.write(TokenType.SKIP_TOKEN.ordinal());
                        } else {
                            predecessorOutChannel.write(TokenType.TAKE_TOKEN.ordinal());
                            storedValue = predecessorInChannel.read();
                            hasValue = true;
                            if (verbose) System.out.printf("Buffer %d got token FULL, and took %d\n", id, storedValue);
                        }
                    } else {
                        if (verbose) System.out.printf("Buffer %d got token %s, and ignored it\n", id, tokenType);
                    }
                }
            }

            int token;
            TokenType tokenType;
            if (hasValue) {
                if (consumerWaiting) {
                    if (verbose) {
                        System.out.printf("Buffer %d has value %d, and sends it to consumer\n", id, storedValue);
                    }
                    requestChannel.read();
                    hasValue = false;
                    consumerChannel.write(storedValue);
                    consumerWaiting = false;
                } else if (hasToken) {
                    if (verbose) {
                        System.out.printf("Buffer %d has value %d, and sends FULL to successor\n", id, storedValue);
                    }
                    successorOutChannel.write(TokenType.FULL_TOKEN.ordinal());
                    token = successorInChannel.read();
                    tokenType = TokenType.values()[token];
                    if (tokenType == TokenType.TAKE_TOKEN) {
                        successorOutChannel.write(storedValue);
                        hasValue = false;
                    }
                    hasToken = false;
                }
                // not consumer waiting and no token but has value
            } else {
                // no value
                if (hasToken) {
                    if (verbose) {
                        System.out.printf("Buffer %d has no value, and sends EMPTY to successor\n", id);
                    }
                    hasToken = false;
                    successorOutChannel.write(TokenType.EMPTY_TOKEN.ordinal());
                }
                // no value and no token
            }
            Sleeper.sleep(delay, randomDelay);
        }
    }
}
