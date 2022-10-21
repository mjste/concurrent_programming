import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UpgradedMonitorBuffer implements IMonitorBuffer{
    int buffer = 0;
    int maxBuffer;
    boolean waitingProducer = false;
    boolean waitingConsumer = false;
    private final Lock lock = new ReentrantLock();
    private final Condition otherProducersCond = lock.newCondition();
    private final Condition firstProducerCond = lock.newCondition();
    private final Condition otherConsumersCond = lock.newCondition();
    private final Condition firstConsumerCond = lock.newCondition();

    public UpgradedMonitorBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void produce(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" want to produce "+n);
        try {
            while (waitingProducer) {
                otherProducersCond.await();
            }
            while (2*maxBuffer - buffer < n) {
                waitingProducer = true;
                firstProducerCond.await();
            }
            buffer += n;
            waitingProducer = false;
            otherProducersCond.signal();
            firstConsumerCond.signal();
            System.out.println(Thread.currentThread().getName()+" produced "+n+", buffer: "+buffer);
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }

    public void consume(int n) {
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" want to consume "+n);
        try {
            while (waitingConsumer) {
                otherConsumersCond.await();
            }
            while (buffer < n) {
                firstConsumerCond.await();
            }
            buffer -= n;
            waitingConsumer = false;
            otherConsumersCond.signal();
            firstProducerCond.signal();
            System.out.println(Thread.currentThread().getName()+" consumed "+n+", buffer: "+buffer);
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }
}
