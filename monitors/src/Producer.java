import java.util.Random;

public class Producer implements Runnable {
    private final IMonitorBuffer monitorBuffer;
    private final int bound;

    public Producer(IMonitorBuffer monitorBuffer, int bound) {
        this.monitorBuffer = monitorBuffer;
        this.bound = bound;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
                monitorBuffer.produce(random.nextInt(bound)+1);
        }
    }
}
