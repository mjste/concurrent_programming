import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorBuffer {
    int buffer = 0;
    int maxBuffer;
    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    public MonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    void produce(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" want to produce "+n);
        try {
            while (maxBuffer-buffer < n) {
                producerCond.await();
            }
            buffer += n;
            System.out.println(Thread.currentThread().getName()+" produced "+n+", buffer: "+buffer);
            consumerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }

    void consume(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" want to consume "+n);
        try {
            while (buffer < n) {
                consumerCond.await();
            }
            buffer -= n;
            System.out.println(Thread.currentThread().getName()+" consumed "+n+", buffer: "+buffer);
            producerCond.signal();
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }
}
