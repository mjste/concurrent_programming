import java.util.concurrent.BlockingQueue;

public class Consumer extends Agent {
    public Consumer(BlockingQueue<SchedulerTask> proxy, int randomBound) {
        super(proxy, randomBound);
    }

    @Override
    public void run() {
        while (!stopped) {
            if (!paused) {
                int n = random.nextInt(randomBound);

                try {
                    System.out.printf("%s requests consumption of %d\n", Thread.currentThread().getName(), n);
                    proxyQueue.put(new SchedulerTask() {
                        @Override
                        public void run(BufferMonitor bufferMonitor) {
                            for (int i = 0; i < n; i++)
                                bufferMonitor.get();
                        }

                        @Override
                        public boolean canRun(BufferMonitor bufferMonitor) {
                            return bufferMonitor.stored >= n;
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
