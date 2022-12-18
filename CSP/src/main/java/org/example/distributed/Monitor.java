package org.example.distributed;

import org.jcsp.lang.Parallel;

import java.util.ArrayList;
import java.util.List;

public class Monitor implements Runnable {
    private BufferNode[] bufferNodes;

    private Parallel parallel;

    int[] histogram;
    public Monitor(Parallel parallel, BufferNode[] bufferNodes) {
        this.parallel = parallel;
        this.bufferNodes = bufferNodes;

        this.histogram = new int[101];
        for (int i = 0; i < 101; i++) {
            histogram[i] = 0;
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            List<Integer> filled = new ArrayList<>();
            int max = 0;
            int min = 100;
            for (BufferNode bufferNode : bufferNodes) {
                int itemsInBuffer = bufferNode.getItemsInBuffer();
                filled.add(itemsInBuffer);
                if (itemsInBuffer > max) {
                    max = itemsInBuffer;
                }
                if (itemsInBuffer < min) {
                    min = itemsInBuffer;
                }
                histogram[itemsInBuffer]++;
            }
//            System.out.println(filled);

//            System.out.println("Max: " + max);
//            System.out.println("Min: " + min);
//            System.out.println("Diff: " + (max - min));

            Sleeper.sleep(1, false);
        }

        parallel.destroy();



        long sum = 0;
        long sumSq = 0;
        long len = 0;
        for (int i = 0; i < 101; i++) {
            sum += histogram[i]*i;
            sumSq += histogram[i]*i*i;
            len += histogram[i];
        }
        double mean = sum/len;
        double stdDev = Math.sqrt(sumSq/len - mean*mean);
        System.out.println(mean + " " + stdDev);
    }
}
