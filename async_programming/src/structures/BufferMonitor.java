package structures;

public class BufferMonitor {
    public long totalGet = 0;
    private final int capacity;
    private final int[] buffer;
    private int stored = 0;
    private final boolean verbose;

    public BufferMonitor(int capacity, boolean verbose) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
        this.verbose = verbose;
    }

    public void put(int value) {
        buffer[stored] = value;
        stored++;
        if (verbose) {
            System.out.printf("[Buffer] Put (%d), stored: %d\n", value, stored);
        }
    }

    public int get() {
        stored--;
        totalGet++;
        if (verbose) {
            System.out.printf("[Buffer] Take (%d), stored: %d\n", buffer[stored], stored);
        }
        return buffer[stored];
    }

    public int getStored() {
        return stored;
    }

    public int getCapacity() {
        return capacity;
    }
}
