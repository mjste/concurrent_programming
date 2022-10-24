import java.util.Random;

public class Consumer implements Runnable {
    private final IMonitorBuffer monitorBuffer;
    private final int bound;

    public Consumer(IMonitorBuffer monitorBuffer, int bound) {
        this.monitorBuffer = monitorBuffer;
        this.bound = bound;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
                monitorBuffer.consume(random.nextInt(bound)+1);
        }
    }
}
