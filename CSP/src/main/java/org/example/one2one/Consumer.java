package org.example.one2one;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Consumer class: reads one int from input channel, displays it, then
 * terminates.
 */
public class Consumer implements CSProcess {
    private final One2OneChannelInt channel;

    public Consumer(final One2OneChannelInt in) {
        channel = in;
    } // constructor

    public void run() {
        int item = channel.in().read();
        System.out.println(item);
    } // run
} // class Consumer
