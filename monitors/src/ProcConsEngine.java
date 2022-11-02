public class ProcConsEngine {


    public void run() {
        int n = 2;
        int m = 2;
        int bound = 2;
        IMonitorBuffer monitorBuffer = new DoubleLockMonitorBuffer(4);

        for (int i = 0; i < n; i++) {
            Producer producer = new Producer(monitorBuffer, bound);
            Thread thread = new Thread(producer);
            thread.start();
        }

        for (int i = 0; i < m; i++) {
            Consumer consumer = new Consumer(monitorBuffer, bound);
            Thread thread = new Thread(consumer);
            thread.start();
        }
    }
}
