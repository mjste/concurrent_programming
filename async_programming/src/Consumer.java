import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Consumer extends Agent {
    public Consumer(Scheduler scheduler, int randomBound) {
        super(scheduler, randomBound);
    }

    @Override
    public void run() {
        while (!stopped) {
            if (!paused) {
                int n = random.nextInt(randomBound);

                System.out.printf("%s requests consumption of %d\n", Thread.currentThread().getName(), n);

                SchedulerTask task = new SchedulerTask() {
                    @Override
                    public void run(BufferMonitor bufferMonitor, Response response) {
                        List<Integer> list = new ArrayList<>();
                        for (int i = 0; i < n; i++)
                            list.add(bufferMonitor.get());
                        response.result = list;
                        response.done = true;
                    }

                    @Override
                    public boolean canRun(BufferMonitor bufferMonitor) {
                        return bufferMonitor.stored >= n;
                    }
                };

                Response response = scheduler.request(task);

                int counter = 0;
                double a = 0;
                while (!response.isDone()) {
                    a += Math.sin(3);
                    counter++;
                }
                System.out.printf("Getting %d items done, counter: %d\nlist: %s\n", n, counter, response.result.toString());

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
