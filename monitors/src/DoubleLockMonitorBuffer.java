import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DoubleLockMonitorBuffer implements IMonitorBuffer {
    int buffer = 0;
    int maxBuffer;
    ReentrantLock producerLock = new ReentrantLock();
    ReentrantLock consumerLock = new ReentrantLock();
    ReentrantLock commonLock = new ReentrantLock();
    Condition consumerCondition = commonLock.newCondition();
    Condition producerCondition = commonLock.newCondition();

    public DoubleLockMonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int n) {
        producerLock.lock();
        System.out.println(Thread.currentThread().getName() + " want to produce " + n);
        commonLock.lock();
        while (2 * maxBuffer - buffer < n) {
            try {
                producerCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        buffer += n;
        System.out.println(Thread.currentThread().getName() + " produced " + n);
        consumerCondition.signal();
        commonLock.unlock();
        producerLock.unlock();
    }

    public void consume(int n) {
        consumerLock.lock();
        System.out.println(Thread.currentThread().getName() + " want to consume " + n);
        commonLock.lock();
        while (buffer < n) {
            try {
                consumerCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        buffer -= n;
        System.out.println(Thread.currentThread().getName() + " consumed " + n);
        producerCondition.signal();
        commonLock.unlock();
        consumerLock.unlock();
    }
}

