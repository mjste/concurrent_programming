import java.util.Random;

public class Consumer implements Runnable {
    private MonitorBuffer monitorBuffer;

    public Consumer(MonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
                monitorBuffer.consume(random.nextInt(5)+1);
        }
    }
}
