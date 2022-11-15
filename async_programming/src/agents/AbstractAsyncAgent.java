package agents;

import structures.BufferMonitor;
import structures.Scheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AbstractAsyncAgent {
    public List<Long> tasksDone = new LinkedList<>();
    protected final long workToDo;
    protected final Scheduler scheduler;
    protected final BufferMonitor bufferMonitor;
    protected boolean stopped = false;
    protected boolean verbose;
    protected final Random random;
    protected final int bound;

    public AbstractAsyncAgent(Scheduler scheduler, BufferMonitor bufferMonitor, long seed, int bound, boolean verbose, long workToDo) {
        this.scheduler = scheduler;
        this.bufferMonitor = bufferMonitor;
        this.random = new Random(seed);
        this.bound = bound;
        this.verbose = verbose;
        this.workToDo = workToDo;
    }

    abstract protected void run();

    public void stop() {
        stopped = true;
    }
}
