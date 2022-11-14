import java.util.Random;
import java.util.concurrent.BlockingQueue;

public abstract class Agent implements Runnable {
    protected final BlockingQueue<SchedulerTask> proxyQueue;
    protected final Random random = new Random();
    protected final int randomBound;
    protected boolean stopped = false;
    protected boolean paused = false;

    public Agent(BlockingQueue<SchedulerTask> proxyQueue, int randomBound) {
        this.proxyQueue = proxyQueue;
        this.randomBound = randomBound;
    }

    abstract public void run();

    void stop() {
        stopped = true;
    }

    void setPaused(boolean paused) {
        this.paused = paused;
    }
}
