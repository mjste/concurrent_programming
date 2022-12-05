package org.example.many2many;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Consumer class: reads one int from input channel, displays it, then
 * terminates.
 */
public class Consumer implements CSProcess {
    private final int id;
    private int delay = 100;
    private final One2OneChannelInt channelIn;
    private final One2OneChannelInt channelRequest;

    public Consumer(One2OneChannelInt channelIn, One2OneChannelInt channelRequest, int id) {
        this.channelIn = channelIn;
        this.channelRequest = channelRequest;
        this.id = id;
    } // constructor

    public Consumer(One2OneChannelInt channelIn, One2OneChannelInt channelRequest, int id, int delay) {
        this.channelIn = channelIn;
        this.channelRequest = channelRequest;
        this.id = id;
        this.delay = delay;
    } // constructor

    public void run() {
        System.out.printf("Consumer has run\n");
        int item;
        while(true) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            channelRequest.out().write(0);
            System.out.printf("Consumer requested for item\n");
            item = channelIn.in().read();
            if (item < 0) {
                System.out.printf("Consumer %d got %d, stopped\n", id, item);
                break;
            }
            System.out.printf("Consumer got %d\n", item);
        }

    }
}
