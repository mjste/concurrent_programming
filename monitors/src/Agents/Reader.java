package Agents;

import LibraryMonitors.LibraryMonitor;

public class Reader implements Runnable {
    private final LibraryMonitor monitor;

    public Reader(LibraryMonitor monitor) {
        this.monitor = monitor;
    }
    @Override
    public void run() {
        while (true) {
            this.monitor.beginReading();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.monitor.endReading();
        }
    }
}
