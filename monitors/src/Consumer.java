public class Consumer implements Runnable {
    private MonitorBuffer monitorBuffer;

    public Consumer(MonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitorBuffer.consume();
            } catch (InterruptedException e) {

            }
        }
    }
}
