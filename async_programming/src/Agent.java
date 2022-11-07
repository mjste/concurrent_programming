import java.util.concurrent.SynchronousQueue;

public abstract class Agent implements Runnable{
    private final SynchronousQueue<Package> proxy;

    public Agent(SynchronousQueue<Package> proxy) {
        this.proxy = proxy;
    }

    abstract public void run();
}
