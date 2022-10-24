import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// This version might suffer to starvation
public class StandardMonitorBuffer implements IMonitorBuffer{
    int buffer = 0;
    int maxBuffer;
    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    public StandardMonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" want to produce "+n);
        int tries = 1;
        try {
            while (maxBuffer-buffer < n) {
                tries++;
                producerCond.await();
            }
            buffer += n;
            System.out.printf("%s produced %d after %d try/tries, buffer: %d\n", Thread.currentThread().getName(), n, tries, buffer);
            consumerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }

    public void consume(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" want to consume "+n);
        int tries = 1;
        try {
            while (buffer < n) {
                consumerCond.await();
                tries++;
            }
            buffer -= n;
            System.out.printf("%s produced %d after %d try/tries, buffer: %d\n", Thread.currentThread().getName(), n, tries, buffer);
            producerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }
}
