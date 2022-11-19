package structures.async;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler {
    private final BlockingQueue<AbstractMethodWrapper> proxyQueue; // Queue representing proxy
    private final Queue<AbstractMethodWrapper> internalQueue; // Queue representing tasks that can't be currently done
    private Thread thread;
    private Boolean stopped = false;

    public Scheduler(int proxyCapacity) {
        proxyQueue = new LinkedBlockingQueue<>(proxyCapacity);
        internalQueue = new LinkedList<>();
        run();
    }

    // Scheduler thread
    private void run() {
        thread = new Thread(() -> {
            AbstractMethodWrapper wrapper;
            while (!stopped) {
                while (!internalQueue.isEmpty()) {
                    wrapper = internalQueue.element();
                    if (wrapper.canBeExecuted()) {
                        wrapper.execute();
                        internalQueue.remove();
                    } else {
                        break;
                    }
                }
                try {
                    wrapper = proxyQueue.take();
                } catch (InterruptedException e) {
                    stopped = true;
                    break;
                }
                if (wrapper.canBeExecuted()) {
                    wrapper.execute();
                } else {
                    internalQueue.add(wrapper);
                }
            }
        });
        thread.start();
    }

    // This function acts as request proxy
    public Response request(AbstractMethodWrapper methodWrapper) throws InterruptedException {
        proxyQueue.put(methodWrapper);
        return methodWrapper.getResponse();
    }

    public void stop() {
        stopped = true;
        thread.interrupt();
    }
}
