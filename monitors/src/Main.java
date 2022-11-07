import BufferMonitors.NestedLocksMonitorBuffer;
import BufferMonitors.UpgradedMonitorBuffer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        long time = 5*1000;
        ProdConsEngine engine = new ProdConsEngine(10, 5, 10, new UpgradedMonitorBuffer(20));
        engine.start();

        Thread.sleep(time);

        engine.stop();
        System.out.printf("Run %d seconds", time/1000);
    }
}