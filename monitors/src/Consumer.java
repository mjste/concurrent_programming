public class Consumer implements Runnable {
    private MonitorBuffer monitorBuffer;

    public Consumer(MonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
    }

    @Override
    public void run() {
        while (true) {
                monitorBuffer.consume();
        }
    }
}
