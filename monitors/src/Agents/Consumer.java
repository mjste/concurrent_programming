package Agents;

import BufferMonitors.IMonitorBuffer;

import java.util.Random;

public class Consumer implements Runnable {
    private final IMonitorBuffer monitorBuffer;
    private final int bound;
    private final long seed;

    public Consumer(IMonitorBuffer monitorBuffer, int bound, long seed) {
        this.monitorBuffer = monitorBuffer;
        this.bound = bound;
        this.seed = seed;
    }

    @Override
    public void run() {
        Random random = new Random(seed);
        while (true) {
            monitorBuffer.consume(random.nextInt(bound) + 1);
        }
    }
}
