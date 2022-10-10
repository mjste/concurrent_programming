public class Consumer implements Runnable {
    private Monitor monitor;

    public Consumer(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitor.consume();
            } catch (InterruptedException e) {

            }
        }
    }
}
