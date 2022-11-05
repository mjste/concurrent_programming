package BufferMonitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
This version is starvation and deadlock free, utilizes nested locks
*/
public class NestedLocksMonitorBuffer implements IMonitorBuffer {
    private int buffer = 0;
    private final int maxBuffer;
    private long total_items_consumed = 0;
    private long total_times_consumed = 0;
    private final ReentrantLock producerLock = new ReentrantLock();
    private final ReentrantLock consumerLock = new ReentrantLock();
    private final ReentrantLock commonLock = new ReentrantLock();
    private final Condition consumerCondition = commonLock.newCondition();
    private final Condition producerCondition = commonLock.newCondition();

    public NestedLocksMonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    @Override
    public void produce(int n) {
        producerLock.lock();
//        System.out.println(Thread.currentThread().getName() + " want to produce " + n);
        commonLock.lock();
        while (2 * maxBuffer - buffer < n) {
            try {
                producerCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        buffer += n;
//        System.out.println(Thread.currentThread().getName() + " produced " + n);
        consumerCondition.signal();
        commonLock.unlock();
        producerLock.unlock();
    }

    @Override
    public void consume(int n) {
        consumerLock.lock();
//        System.out.println(Thread.currentThread().getName() + " want to consume " + n);
        commonLock.lock();
        while (buffer < n) {
            try {
                consumerCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        buffer -= n;
        total_items_consumed += n;
        total_times_consumed++;
//        System.out.println(Thread.currentThread().getName() + " consumed " + n);
        producerCondition.signal();
        commonLock.unlock();
        consumerLock.unlock();
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
