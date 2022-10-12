public class Producer implements Runnable {
    private MonitorBuffer monitorBuffer;

    public Producer(MonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                monitorBuffer.produce();
            } catch (InterruptedException e) {

            }
        }
    }
}
