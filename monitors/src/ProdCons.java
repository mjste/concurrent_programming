public class ProdCons {


    public void run() {
        int n = 2;
        int m = 2;
        MonitorBuffer monitorBuffer = new MonitorBuffer(5);

        for (int i = 0; i < n; i++) {
            Producer producer = new Producer(monitorBuffer);
            Thread thread = new Thread(producer);
            thread.start();
        }

        for (int i = 0; i < m; i++) {
            Consumer consumer = new Consumer(monitorBuffer);
            Thread thread = new Thread(consumer);
            thread.start();
        }
    }
}
