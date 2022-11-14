import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler {
    private Thread thread;
    public final BlockingQueue<SchedulerTask> proxyQueue;
    private final Queue<SchedulerTask> waitingQueue = new LinkedList<>();
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
                    SchedulerTask task;
                    while (waitingQueue.size() > 0) {
                        task = waitingQueue.peek();
                        if (task.canRun(bufferMonitor)) {
                            task = waitingQueue.remove();
                            task.run(bufferMonitor);
                        } else {
                            break;
                        }
                    }
                    try {
                        task = proxyQueue.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (task.canRun(bufferMonitor)) {
                        task.run(bufferMonitor);
                    } else {
                        waitingQueue.add(task);
                    }
                }
            }
        });
        thread.start();
    }

    public Response request(SchedulerTask task) {
        //TODO
        return new Response();
    }
}
