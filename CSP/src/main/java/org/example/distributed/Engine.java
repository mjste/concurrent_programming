package org.example.distributed;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

public final class Engine {
    private final int numberOfAgents = 10;
    private final int delay_ms = 0;
    private final boolean isRandomDelay = true;

    public Engine() {
        Producer[] producers = new Producer[numberOfAgents];
        Consumer[] consumers = new Consumer[numberOfAgents];
        BufferNode[] buffers = new BufferNode[numberOfAgents];
        One2OneChannelInt[] producerChannels = new One2OneChannelInt[numberOfAgents];
        One2OneChannelInt[] consumerChannels = new One2OneChannelInt[numberOfAgents];
        One2OneChannelInt[] requestChannels = new One2OneChannelInt[numberOfAgents];
        One2OneChannelInt[] bufferChannels = new One2OneChannelInt[2 * numberOfAgents];


        for (int i = 0; i < numberOfAgents; i++) {
            producerChannels[i] = org.jcsp.lang.Channel.one2oneInt();
            consumerChannels[i] = org.jcsp.lang.Channel.one2oneInt();
            requestChannels[i] = org.jcsp.lang.Channel.one2oneInt();
        }
        for (int i = 0; i < 2 * numberOfAgents; i++) {
            bufferChannels[i] = org.jcsp.lang.Channel.one2oneInt();
        }

        for (int i = 0; i < numberOfAgents; i++) {
            producers[i] = new Producer(producerChannels[i].out(), i, delay_ms, isRandomDelay, false);
            consumers[i] = new Consumer(consumerChannels[i].in(), requestChannels[i].out(), i, (int)(delay_ms), isRandomDelay, false);
            int successor_out = 2 * i;
            int successor_in = 2 * i + 1;

            int n = 2 * numberOfAgents;
            int predecessor_in = ((2 * i - 2) + n) % n;
            int predecessor_out = ((2 * i - 1) + n) % n;

            buffers[i] = new BufferNode(
                    producerChannels[i].in(),
                    requestChannels[i].in(),
                    bufferChannels[predecessor_in].in(),
                    bufferChannels[successor_in].in(),
                    consumerChannels[i].out(),
                    bufferChannels[predecessor_out].out(),
                    bufferChannels[successor_out].out(),
                    (long) i,
                    delay_ms,
                    isRandomDelay,
                    100,
                    false);
        }

        buffers[0].setHasToken(true);

        Parallel parallel = new Parallel(new CSProcess[][]{producers, consumers, buffers});

        Monitor monitor = new Monitor(parallel, buffers);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();

        parallel.run();
    }
}
