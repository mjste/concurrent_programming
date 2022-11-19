package structures.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncBufferMonitor {
    public long totalGet = 0;
    private final int capacity;
    private final int[] buffer;
    private int stored = 0;

    private boolean stopped = false;

    private final Lock producerLock = new ReentrantLock();
    private final Lock consumerLock = new ReentrantLock();
    private final Lock internalLock = new ReentrantLock();
    private final Condition producerCondition = internalLock.newCondition();
    private final Condition consumerCondition = internalLock.newCondition();

    public SyncBufferMonitor(int capacity) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
    }

    public void stop() {
        internalLock.lock();
    }


    public void putList(List<Integer> list) throws InterruptedException {
        producerLock.lock();
        internalLock.lock();

        while (capacity - stored < list.size()) {
            producerCondition.await();
        }

        for (Integer value : list) {
            buffer[stored] = value;
            stored++;
        }

        consumerCondition.signal();
        internalLock.unlock();
        producerLock.unlock();
    }

    public List<Integer> getList(int n) throws InterruptedException {
        consumerLock.lock();
        internalLock.lock();

        while (stored < n) {
                consumerCondition.await();
        }

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            stored--;
            totalGet++;
            list.add(buffer[stored]);
        }

        producerCondition.signal();
        internalLock.unlock();
        consumerLock.unlock();

        return list;
    }
}
