package agents.sync;

import structures.BufferMonitor;
import structures.async.Scheduler;
import structures.sync.SyncBufferMonitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public abstract class AbstractSyncAgent {
    public List<Long> tasksDone = new LinkedList<>();
    protected final long workToDo;
    protected final SyncBufferMonitor bufferMonitor;
    private Thread thread;
    protected boolean stopped = false;
    protected boolean verbose;
    protected final Random random;
    protected final int bound;

    public AbstractSyncAgent(SyncBufferMonitor bufferMonitor, long seed, int bound, boolean verbose, long workToDo) {
        this.bufferMonitor = bufferMonitor;
        this.random = new Random(seed);
        this.bound = bound;
        this.verbose = verbose;
        this.workToDo = workToDo;
        thread = new Thread(this::run);
        thread.start();
    }

    abstract protected void run();

    public void stop() {
        stopped = true;
        thread.stop();
    }
}
