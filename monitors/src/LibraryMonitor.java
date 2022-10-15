import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class LibraryMonitor {
    int reading = 0;
    int writing = 0;
    int wantsToWrite = 0;

    Lock lock = new ReentrantLock();

    public synchronized void beginReading() {
        while (writing + wantsToWrite > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        reading++;
        System.out.println(Thread.currentThread().getId()+" R1");
    }

    public synchronized void endReading() {
        reading--;
        System.out.println(Thread.currentThread().getId()+" R2");
        notifyAll();
    }

    public synchronized void beginWriting() {
        wantsToWrite++;
        while (reading + writing > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        wantsToWrite--;
        writing = 1;
        System.out.println(Thread.currentThread().getId()+" W1");
        notifyAll();
    }

    public synchronized void endWriting() {
        writing = 0;
        System.out.println(Thread.currentThread().getId()+" W2");
        notifyAll();
    }
}
