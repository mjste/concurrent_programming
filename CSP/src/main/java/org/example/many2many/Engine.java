package org.example.many2many;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

public final class Engine {
    public Engine() { // Create channel object
        One2OneChannelInt[] producerChannels = {Channel.one2oneInt(), Channel.one2oneInt()};
        One2OneChannelInt[] consumerChannels = {Channel.one2oneInt(), Channel.one2oneInt()};
        One2OneChannelInt[] requestChannels = {Channel.one2oneInt(), Channel.one2oneInt()};

        int delay = 1;

        Buffer buffer = new Buffer(producerChannels, consumerChannels, requestChannels);
        Producer producer1 = new Producer(producerChannels[0], 1, delay);
        Producer producer2 = new Producer(producerChannels[1], 2, delay);
        Consumer consumer1 = new Consumer(consumerChannels[0], requestChannels[0], 1, delay);
        Consumer consumer2 = new Consumer(consumerChannels[1], requestChannels[1], 2, delay);

        Parallel par = new Parallel(new CSProcess[] {buffer, consumer1, consumer2, producer1, producer2});
        par.run();

    } // PCMain constructor
} // class PCMain