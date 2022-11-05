import Agents.Consumer;
import Agents.Producer;
import BufferMonitors.IMonitorBuffer;
import BufferMonitors.UpgradedMonitorBuffer;

public class ProdConsEngine {


    public void run() {
        int producers = 2;
        int consumers = 2;
        int maxPackage = 2;
        int seed = 42;

        IMonitorBuffer monitorBuffer = new UpgradedMonitorBuffer(4);

        for (int i = 0; i < producers; i++) {
            Producer producer = new Producer(monitorBuffer, maxPackage, seed + i);
            new Thread(producer).start();
        }

        for (int i = 0; i < consumers; i++) {
            Consumer consumer = new Consumer(monitorBuffer, maxPackage, seed + i + 154);
            new Thread(consumer).start();
        }
    }
}
