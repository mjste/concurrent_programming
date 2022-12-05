package org.example.one2one;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Producer implements CSProcess {
    private final One2OneChannelInt channel;

    public Producer(final One2OneChannelInt out) {
        channel = out;
    } // constructor

    public void run() {
        int item = (int) (Math.random() * 100) + 1;
        channel.out().write(item);
    } // run
} // class Producer
