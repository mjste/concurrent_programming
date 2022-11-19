package agents.async;

import structures.BufferMonitor;
import structures.async.GetMethodWrapper;
import structures.async.Response;
import structures.async.Scheduler;

import java.util.List;

public class AsyncConsumer extends AbstractAsyncAgent {

    public AsyncConsumer(Scheduler scheduler, BufferMonitor bufferMonitor, long seed, int bound, boolean verbose, long workToDo) {
        super(scheduler, bufferMonitor, seed, bound, verbose, workToDo);
        run();
    }

    @Override
    protected void run() {
        new Thread(() -> {
            while (!stopped) {
                int n = random.nextInt(bound) + 1;
                Response response;
                try {
                    response = scheduler.request(new GetMethodWrapper(bufferMonitor, n));
                    if (verbose) {
                        System.out.printf("[%s], requesting to consume %d\n", Thread.currentThread().getName(), n);
                    }
                } catch (InterruptedException e) {
                    stopped = true;
                    break;
                }

                long counter = 0;
                while (!response.isDone() && !stopped) {
                    for (long i = 0; i < workToDo; i++) {
                        double a = Math.sin(3);
                    }
                    counter++;
                }
                if (stopped) {
                    break;
                }
                tasksDone.add(counter);
                List<Integer> list = response.getResult();
                if (verbose) {
                    System.out.printf("[%s], got %s\n", Thread.currentThread().getName(), list.toString());
                }
            }
        }).start();
    }
}
