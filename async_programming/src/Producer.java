import java.util.concurrent.SynchronousQueue;

public class Producer extends Agent {
    public Producer(SynchronousQueue<Package> proxy) {
        super(proxy);
    }

    @Override
    public void run() {

    }
}
