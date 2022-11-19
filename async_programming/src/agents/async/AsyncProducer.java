package agents.async;

import structures.BufferMonitor;
import structures.async.PutMethodWrapper;
import structures.async.Response;
import structures.async.Scheduler;

import java.util.LinkedList;
import java.util.List;

public class AsyncProducer extends AbstractAsyncAgent {

    public AsyncProducer(Scheduler scheduler, BufferMonitor bufferMonitor, long seed, int bound, boolean verbose, long workToDo) {
        super(scheduler, bufferMonitor, seed, bound, verbose, workToDo);
        run();
    }

    @Override
    protected void run() {
        new Thread(() -> {
            while (!stopped) {
                int n = random.nextInt(bound) + 1;
                List<Integer> list = new LinkedList<>();
                for (int i = 0; i < n; i++) {
                    list.add(random.nextInt());
                }
                Response response;
                try {
                    response = scheduler.request(new PutMethodWrapper(bufferMonitor, list));
                    if (verbose) {
                        System.out.printf("[%s], requesting to produce %d, %s\n", Thread.currentThread().getName(), n, list);
                    }
                } catch (InterruptedException e) {
                    stopped = true;
                    break;
                }

                long counter = 0;
                while (!response.isDone() && !stopped) {
                    for (long i = 0; i < workToDo; i++) {
                        double b = Math.cos(3);
                    }
                    counter++;
                }
                if (stopped) {
                    break;
                }
                tasksDone.add(counter);
                if (verbose) {
                    System.out.printf("[%s], production successful\n", Thread.currentThread().getName());
                }
            }
        }).start();
    }
}
