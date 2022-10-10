public class Monitor {
    int buffer = 0;
    int maxBuffer;

    public Monitor(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    synchronized void produce() throws InterruptedException {
        while (buffer == maxBuffer) wait();
        buffer += 1;
        System.out.println(buffer);
        notifyAll();
    }

    synchronized void consume() throws InterruptedException {
        while (buffer == 0) wait();
        buffer -= 1;
        System.out.println(buffer);
        notifyAll();
    }
}
