package Agents;

import LibraryMonitors.LibraryMonitor;

public class Writer implements Runnable{
    private final LibraryMonitor monitor;

    public Writer(LibraryMonitor monitor) {
        this.monitor = monitor;
    }
    @Override
    public void run() {
        while (true) {
            this.monitor.beginWriting();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.monitor.endWriting();
        }
    }
}
