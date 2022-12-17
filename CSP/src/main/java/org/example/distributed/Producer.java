package org.example.distributed;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutputInt;

public class Producer implements CSProcess {
    private final int id;
    private final int delay;
    private final boolean randomDelay;
    private final boolean verbose = false;

    private final ChannelOutputInt channelOut;

    public Producer(ChannelOutputInt channelOutputInt, int id, int delay, boolean randomDelay) {
        this.id = id;
        this.channelOut = channelOutputInt;
        this.delay = delay;
        this.randomDelay = randomDelay;
    }


    @Override
    public void run() {
        while (true) {
            int item = 100 * id + (int) (Math.random() * 100);
            if (verbose) System.out.printf("Producer %d wants to send %d\n", id, item);
            channelOut.write(item);
            if (verbose) System.out.printf("Producer %d sent %d\n", id, item);
            if (randomDelay) {
                Sleeper.sleepRandom(delay);
            } else {
                Sleeper.sleep(delay);
            }
        }
    }
}
