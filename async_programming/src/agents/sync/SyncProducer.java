package agents.sync;


import structures.async.Scheduler;
import structures.sync.SyncBufferMonitor;

import java.util.ArrayList;
import java.util.List;

public class SyncProducer extends AbstractSyncAgent{

    public SyncProducer(SyncBufferMonitor bufferMonitor, long seed, int bound, boolean verbose, long workToDo) {
        super(bufferMonitor, seed, bound, verbose, workToDo);
    }

    @Override
    protected void run() {
        while (!stopped) {
            int n = random.nextInt(bound) + 1;
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                list.add(random.nextInt());
            }
            try {
                bufferMonitor.putList(list);
            } catch (InterruptedException e) {
                break;
            }

            for (long i = 0; i < workToDo; i++) {
                double a = Math.sin(3);
            }

            tasksDone.add(1L);

            if (verbose) {
                System.out.printf("[%s], produced %s\n", Thread.currentThread().getName(), list.toString());
            }
        }
    }
}
