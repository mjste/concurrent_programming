package BufferMonitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 Possibly error-prone due to using lock.hasWaiters() instead of boolean
 On first look it's good (not suffer to starvation and deadlock)
 However due to java intricacies it may suffer from starvation and eventually deadlock.
*/
public class DegradedMonitorBuffer implements IMonitorBuffer {
    int buffer = 0;
    int maxBuffer;
    private long total_items_consumed = 0;
    private long total_times_consumed = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition otherProducersCond = lock.newCondition();
    private final Condition firstProducerCond = lock.newCondition();
    private final Condition otherConsumersCond = lock.newCondition();
    private final Condition firstConsumerCond = lock.newCondition();

    public DegradedMonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " want to produce " + n);
        int tries = 1;
        try {
            while (lock.hasWaiters(firstProducerCond)) {
                otherProducersCond.await();
                tries++;
            }
            while (2 * maxBuffer - buffer < n) {
                firstProducerCond.await();
                tries++;
            }
            buffer += n;
            otherProducersCond.signal();
            firstConsumerCond.signal();
//            System.out.printf("%s produced %d after %d try/tries, buffer: %d\n", Thread.currentThread().getName(), n, tries, buffer);
            System.out.printf("%d\n", tries);
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }

    public void consume(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " want to consume " + n);
        int tries = 1;
        try {
            while (lock.hasWaiters(firstConsumerCond)) {
                otherConsumersCond.await();
                tries++;
            }
            while (buffer < n) {
                firstConsumerCond.await();
                tries++;
            }
            buffer -= n;
            total_items_consumed += n;
            total_times_consumed++;
            otherConsumersCond.signal();
            firstProducerCond.signal();
//            System.out.printf("%s produced %d after %d try/tries, buffer: %d\n", Thread.currentThread().getName(), n, tries, buffer);
            System.out.printf("%d\n", tries);
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
