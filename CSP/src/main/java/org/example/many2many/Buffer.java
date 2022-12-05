package org.example.many2many;

import org.jcsp.lang.*;

public class Buffer implements CSProcess {
    private One2OneChannelInt[] channelsIn;
    private One2OneChannelInt[] channelsOut;
    private One2OneChannelInt[] channelsRequest;

    private int[] buffer = new int[10];
    int hd = -1;
    int tl = -1;


    public Buffer(One2OneChannelInt[] channelsIn,
                  One2OneChannelInt[] channelsOut,
                  One2OneChannelInt[] channelsRequest) {
        this.channelsIn = channelsIn;
        this.channelsOut = channelsOut;
        this.channelsRequest = channelsRequest;
    }


    @Override
    public void run() {
        final Guard[] guards = {
                channelsIn[0].in(),
                channelsIn[1].in(),
                channelsRequest[0].in(),
                channelsRequest[1].in()
        };


        final Alternative alternative = new Alternative(guards);
        int countdown = 4;
        while (countdown > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int index = alternative.select();
            System.out.printf("Buffer in index: %d\n", index);
            switch (index) {
                case 0:
                case 1:
                {
                    if (hd < tl + 11) {// Space available
                        int item = channelsIn[index].in().read();
                        if (item < 0) {
                            countdown--;
                        } else {
                            hd++;
                            buffer[hd%buffer.length] = item;
                        }
                    }
                    break;
                }
                case 2:
                case 3:
                {
                    if (tl < hd) {
                        channelsRequest[index-2].in().read();
                        tl++;
                        int item = buffer[tl%buffer.length];
                        channelsOut[index-2].out().write(item);

                    } else if (countdown <= 2) {
                        channelsRequest[index-2].in().read();
                        channelsOut[index-2].out().write(-1);
                        countdown--;
                    }
                    break;
                }

            }
        }
        System.out.println("Buffer ended.");
    }
}
