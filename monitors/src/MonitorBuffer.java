import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorBuffer {
    int buffer = 0;
    int maxBuffer;
    private Lock lock = new ReentrantLock();
    private Condition producerCond = lock.newCondition();
    private Condition consumerCond = lock.newCondition();

    public MonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    void produce() {
        lock.lock();
        try {
            while (buffer == maxBuffer) {
                producerCond.await();
            }
            buffer++;
            System.out.println(Thread.currentThread().getName()+" produced, buffer: "+buffer);
            consumerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }

    void consume() {
        lock.lock();
        try {
            while (buffer == 0) {
                consumerCond.await();
            }
            buffer--;
            System.out.println(Thread.currentThread().getName()+" consumed, buffer: "+buffer);
            producerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }
}
