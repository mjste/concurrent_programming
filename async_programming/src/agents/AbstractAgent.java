package agents;

import structures.BufferMonitor;
import structures.Scheduler;

import java.util.Random;

public abstract class AbstractAgent {
    protected final Scheduler scheduler;
    protected final BufferMonitor bufferMonitor;
    protected boolean stopped = false;
    protected boolean verbose;
    protected final Random random;
    protected final int bound;

    public AbstractAgent(Scheduler scheduler, BufferMonitor bufferMonitor, long seed, int bound, boolean verbose) {
        this.scheduler = scheduler;
        this.bufferMonitor = bufferMonitor;
        this.random = new Random(seed);
        this.bound = bound;
        this.verbose = verbose;
    }

    abstract protected void run();

    public void stop() {
        stopped = true;
    }
}
