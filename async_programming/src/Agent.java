import java.util.Random;
import java.util.concurrent.BlockingQueue;

public abstract class Agent implements Runnable {
    protected final Scheduler scheduler;
    protected final Random random = new Random();
    protected final int randomBound;
    protected boolean stopped = false;
    protected boolean paused = false;

    public Agent(Scheduler scheduler, int randomBound) {
        this.scheduler = scheduler;
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
