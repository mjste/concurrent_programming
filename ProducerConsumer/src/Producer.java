public class Producer implements Runnable {
    private Monitor monitor;

    public Producer(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.produce();
            } catch (InterruptedException e) {

            }
        }
    }
}
