public class Writer implements Runnable{
    private LibraryMonitor monitor;

    public Writer(LibraryMonitor monitor) {
        this.monitor = monitor;
    }
    @Override
    public void run() {
        while (true) {
            this.monitor.beginWriting();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.monitor.endWriting();
        }
    }
}
