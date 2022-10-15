public class ReadWrite {
    public void run() {
        int n = 1;
        int m = 1;

        LibraryMonitor monitor = new LibraryMonitor();

        for (int i = 0; i < n; i++) {
            Reader reader = new Reader(monitor);
            Thread thread = new Thread(reader);
            thread.start();
        }

        for (int i = 0; i < m; i++) {
            Writer writer = new Writer(monitor);
            Thread thread = new Thread(writer);
            thread.start();
        }
    }
}