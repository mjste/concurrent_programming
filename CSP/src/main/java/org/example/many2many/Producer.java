package org.example.many2many;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Producer implements CSProcess {
    private int id;
    private final One2OneChannelInt channelOut;
    private int delay=100;

    public Producer(final One2OneChannelInt channelOut, int id) {
        this.channelOut = channelOut;
        this.id = id;
    } // constructor

    public Producer(final One2OneChannelInt channelOut, int id, int delay) {
        this.channelOut = channelOut;
        this.id = id;
        this.delay = delay;
    } // constructor

    public void run() {
        int item;
        for (int k = 0; k < 5; k++) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            item = (int) (Math.random()*100)+1+ id;
            channelOut.out().write(item);
            System.out.printf("Producer %d sent %d\n", id, item);
        }
        channelOut.out().write(-1);
        System.out.printf("Producer %d ended.", id);
    }
}
