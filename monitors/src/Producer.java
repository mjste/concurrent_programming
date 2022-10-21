import java.util.Random;

public class Producer implements Runnable {
    private final IMonitorBuffer monitorBuffer;

    public Producer(IMonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
                monitorBuffer.produce(random.nextInt(5)+1);
        }
    }
}
