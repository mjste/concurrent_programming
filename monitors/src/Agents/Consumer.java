package Agents;

import BufferMonitors.IMonitorBuffer;

import java.util.Random;

public class Consumer extends AbstractProdCons {

    public Consumer(IMonitorBuffer monitorBuffer, int bound, long seed) {
        super(monitorBuffer, bound, seed);
    }

    @Override
    public void run() {
        Random random = new Random(seed);
        while (!stopped) {
            monitorBuffer.consume(random.nextInt(bound) + 1);
        }
    }
}
