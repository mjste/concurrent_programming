package BufferMonitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// This version might will not suffer deadlock, however starvation is possible
public class StandardMonitorBuffer implements IMonitorBuffer {
    int buffer = 0;
    int maxBuffer;
    private long total_items_consumed = 0;
    private long total_times_consumed = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    public StandardMonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " want to produce " + n);
        int tries = 1;
        try {
            while (maxBuffer - buffer < n) {
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

    @Override
    public void consume(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " want to consume " + n);
        int tries = 1;
        try {
            while (buffer < n) {
                consumerCond.await();
                tries++;
            }
            buffer -= n;
            total_items_consumed += n;
            total_times_consumed++;
            System.out.printf("%s produced %d after %d try/tries, buffer: %d\n", Thread.currentThread().getName(), n, tries, buffer);
            producerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }

    @Override
    public long get_total_consumed() {
        return total_items_consumed;
    }

    @Override
    public long get_total_operations() {
        return total_times_consumed;
    }
}
