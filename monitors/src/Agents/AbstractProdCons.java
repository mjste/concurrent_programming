package Agents;

import BufferMonitors.IMonitorBuffer;

public abstract class AbstractProdCons implements Runnable {

    protected final IMonitorBuffer monitorBuffer;
    protected final int bound;
    protected final long seed;
    protected boolean stopped;

    public AbstractProdCons(IMonitorBuffer monitorBuffer, int bound, long seed) {
        this.monitorBuffer = monitorBuffer;
        this.bound = bound;
        this.seed = seed;
    }

    @Override
    public abstract void run();

    public void stop() {
        stopped = true;
    }
}
