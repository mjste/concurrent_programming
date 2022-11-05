package Agents;

import BufferMonitors.IMonitorBuffer;

import java.util.Random;

public class Producer extends AbstractProdCons {

    public Producer(IMonitorBuffer monitorBuffer, int bound, long seed) {
        super(monitorBuffer, bound, seed);
    }

    @Override
    public void run() {
        Random random = new Random(seed);
        while (!stopped) {
            monitorBuffer.produce(random.nextInt(bound) + 1);
        }
    }
}
