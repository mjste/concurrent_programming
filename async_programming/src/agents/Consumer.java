package agents;

import structures.BufferMonitor;
import structures.GetMethod;
import structures.Response;
import structures.Scheduler;

import java.util.List;

public class Consumer extends AbstractAgent {

    public Consumer(Scheduler scheduler, BufferMonitor bufferMonitor, long seed, int bound, boolean verbose) {
        super(scheduler, bufferMonitor, seed, bound, verbose);
        run();
    }

    @Override
    protected void run() {
        new Thread(() -> {
            while (!stopped) {
                int n = random.nextInt(bound) + 1;
                List<Integer> list;
                Response response;
                try {
                    response = scheduler.request(new GetMethod(bufferMonitor, n));
                    if (verbose) {
                        System.out.printf("[%s], requesting to consume %d\n", Thread.currentThread().getName(),n);
                    }
                } catch (InterruptedException e) {
                    stopped = true;
                    break;
                }

                // busy waiting
                while (!response.isDone()) {
                    double a = Math.sin(3);
                }
                list = response.getResult();
                if (verbose) {
                    System.out.printf("[%s], got %s\n", Thread.currentThread().getName(), list.toString());
                }
            }
        }).start();
    }
}
