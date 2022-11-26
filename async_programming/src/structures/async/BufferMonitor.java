package structures.async;

import other.Utils;

import java.util.LinkedList;
import java.util.List;

public class BufferMonitor {
    public long totalGet = 0;
    public final long work;
    private final int capacity;
    private final int[] buffer;
    private int stored = 0;
    private final boolean verbose;

    public BufferMonitor(int capacity, boolean verbose, long work) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
        this.verbose = verbose;
        this.work = work;
    }


    public void putList(List<Integer> values) {
        for (Integer value : values) {
            buffer[stored] = value;
            stored++;
        }
        Utils.work(work);
    }

    public List<Integer> getList(int n) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            stored--;
            totalGet++;
            list.add(buffer[stored]);
        }
        Utils.work(work);
        return list;
    }

    public int getStored() {
        return stored;
    }

    public int getCapacity() {
        return capacity;
    }
}
