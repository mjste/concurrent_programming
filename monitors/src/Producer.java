public class Producer implements Runnable {
    private MonitorBuffer monitorBuffer;

    public Producer(MonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
    }

    @Override
    public void run() {
        while (true) {
                monitorBuffer.produce();
        }
    }
}
