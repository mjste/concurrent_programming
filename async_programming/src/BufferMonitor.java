public class BufferMonitor {
    public long total_get = 0;
    public final int capacity;
    public final int[] buffer;
    public int stored = 0;

    public BufferMonitor(int capacity) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
    }

    public void put(int value) {
        buffer[stored] = value;
        stored++;
//        System.out.printf("[Buffer] Put (%d), stored: %d\n", value, stored);
    }

    public int get() {
        stored--;
        total_get++;
//        System.out.printf("[Buffer] Take (%d), stored: %d\n", buffer[stored], stored);
        return buffer[stored];
    }
}
