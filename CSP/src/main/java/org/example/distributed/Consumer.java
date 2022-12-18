package org.example.distributed;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelInputInt;
import org.jcsp.lang.ChannelOutputInt;

public class Consumer implements CSProcess {
    private final int id;
    private final int delay;
    private final boolean randomDelay;
    private final boolean verbose;
    private boolean running = true;
    private final ChannelOutputInt channelRequestOut;
    private final ChannelInputInt channelIn;

    public Consumer(ChannelInputInt channelInputInt, ChannelOutputInt channelOutputInt, int id, int delay, boolean randomDelay, boolean verbose) {
        this.id = id;
        this.delay = delay;
        this.randomDelay = randomDelay;
        this.channelRequestOut = channelOutputInt;
        this.channelIn = channelInputInt;
        this.verbose = verbose;
    }


    @Override
    public void run() {
        while (running) {
            if (verbose) System.out.printf("Consumer %d wants to send request\n", id);
            channelRequestOut.write(0);
            if (verbose) System.out.printf("Consumer %d sent request successfully, waiting to consume\n", id);
            int item = channelIn.read();
            if (verbose) System.out.printf("Consumer %d received %d\n", id, item);
            Counter.increment();
            Sleeper.sleep(delay, randomDelay);
        }
    }

    public void stop() {
        this.running = false;
    }
}

