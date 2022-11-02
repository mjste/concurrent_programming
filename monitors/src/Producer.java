import java.util.Random;

public class Producer implements Runnable {
    private final IMonitorBuffer monitorBuffer;
    private final int bound;
    private final long seed;

    public Producer(IMonitorBuffer monitorBuffer, int bound, long seed) {
        this.monitorBuffer = monitorBuffer;
        this.bound = bound;
        this.seed = seed;
    }

    @Override
    public void run() {
        Random random = new Random(seed);
        while (true) {
                monitorBuffer.produce(random.nextInt(bound)+1);
        }
    }
}
