import java.util.concurrent.BlockingQueue;

public class Producer extends Agent {
    public Producer(Scheduler scheduler, int randomBound) {
        super(scheduler, randomBound);
    }

    @Override
    public void run() {
        while (!stopped) {
            if (!paused) {
                int n = random.nextInt(randomBound);

                System.out.printf("%s requests production of %d\n", Thread.currentThread().getName(), n);

                SchedulerTask task = new SchedulerTask() {
                    @Override
                    public void run(BufferMonitor bufferMonitor, Response response) {
                        for (int i = 0; i < n; i++)
                            bufferMonitor.put(random.nextInt());
                        response.done = true;
                    }

                    @Override
                    public boolean canRun(BufferMonitor bufferMonitor) {
                        return bufferMonitor.capacity - bufferMonitor.stored >= n;
                    }
                };

                Response response = scheduler.request(task);
                int counter = 0;
                double a = 0;
                while (!response.isDone()) {
                    a += Math.sin(3);
                    counter++;
                }
                System.out.printf("Putting done, counter: %d\n", counter);
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
