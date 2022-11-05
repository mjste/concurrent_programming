package LibraryMonitors;

import java.util.concurrent.locks.*;

public class LibraryMonitor {
    int reading = 0;
    int writing = 0;
    int wantsToWrite = 0;
    int wantsToRead = 0;

    Lock lock = new ReentrantLock();
    Condition readerCond = lock.newCondition();
    Condition writerCond = lock.newCondition();

    public void beginReading() {
        lock.lock();
        wantsToRead++;
        System.out.println(Thread.currentThread().getName() + " wants to read, wtr: " + wantsToRead);
        try {
            if (wantsToWrite > 0)
                readerCond.await();
            while (writing > 0) {
                readerCond.await();
            }
            reading++;
            System.out.println(Thread.currentThread().getName() + " begins reading");
            wantsToRead--;
        } catch (InterruptedException ignore) {
        } finally {
            lock.unlock();
        }
    }

    public void endReading() {
        lock.lock();

        reading--;
        if (reading == 0) {
            writerCond.signalAll();
        }
        System.out.println(Thread.currentThread().getName() + " ends reading");
        lock.unlock();
    }

    public void beginWriting() {
        lock.lock();
        wantsToWrite++;
        System.out.println(Thread.currentThread().getName() + " wants to write, wtw: " + wantsToWrite);
        try {
            while (reading + writing > 0) {
                writerCond.await();
            }
            writing = 1;
            wantsToWrite--;
            System.out.println(Thread.currentThread().getName() + " starts writing");
        } catch (InterruptedException ignore) {
        } finally {
            lock.unlock();
        }
    }

    public void endWriting() {
        lock.lock();
        writing = 0;
        if (wantsToRead == 0) {
            System.out.println(Thread.currentThread().getName() + " ends writing, signal writer");
            writerCond.signal();
        } else {
            System.out.println(Thread.currentThread().getName() + " ends writing, signal reader");
            readerCond.signalAll();
        }
        lock.unlock();
    }
}
