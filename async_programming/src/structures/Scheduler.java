package structures;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler {
    private final BlockingQueue<MethodWrapper> proxyQueue;
    private final Queue<MethodWrapper> internalQueue;
    private Thread thread;
    private Boolean stopped = false;

    public Scheduler(int proxyCapacity) {
        proxyQueue = new LinkedBlockingQueue<>(proxyCapacity);
        internalQueue = new LinkedList<>();
        run();
    }

    private void run() {
        thread = new Thread(() -> {
            MethodWrapper wrapper;
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

    public Response request(MethodWrapper methodWrapper) throws InterruptedException {
        proxyQueue.put(methodWrapper);
        return methodWrapper.getResponse();
    }

    public void stop() {
        stopped = true;
        thread.interrupt();
    }
}
