import java.util.concurrent.SynchronousQueue;

public class Consumer extends Agent{
    public Consumer(SynchronousQueue<Package> proxy) {
        super(proxy);
    }

    @Override
    public void run() {

    }
}
