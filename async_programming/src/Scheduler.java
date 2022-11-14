import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler {
    private Thread thread;
    public final BlockingQueue<Package> proxyQueue;
    private final Queue<Package> waitingQueue = new LinkedList<>();
    public final BufferMonitor bufferMonitor;
    boolean paused = false;
    boolean stopped = false;

    public Scheduler(int bufferCapacity, int proxyQueueCapacity) {
        this.bufferMonitor = new BufferMonitor(bufferCapacity);
        this.proxyQueue = new LinkedBlockingQueue<>(proxyQueueCapacity);
        run();
    }

    public void stop() {
        stopped = true;
        thread.interrupt();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void run() {
        thread = new Thread(() -> {
            while (!stopped) {
                if (!paused) {
                    Package pack;
                    SchedulerTask task;
                    while (waitingQueue.size() > 0) {
                        pack = waitingQueue.peek();
                        task = pack.task;
                        if (task.canRun(bufferMonitor)) {
                            waitingQueue.remove();
                            task.run(bufferMonitor, pack.response);
                        } else {
                            break;
                        }
                    }

                    try {
                        pack = proxyQueue.take();
                        task = pack.task;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (task.canRun(bufferMonitor)) {
                        task.run(bufferMonitor, pack.response);
                    } else {
                        waitingQueue.add(pack);
                    }
                }
            }
        });
        thread.start();
    }

    public Response request(SchedulerTask task) {
        Package pack = new Package(task);
        try {
            proxyQueue.put(pack);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return pack.response;
    }
}
