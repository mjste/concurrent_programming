package agents.sync;

import other.Utils;
import structures.async.Scheduler;
import structures.sync.SyncBufferMonitor;

import java.util.List;

public class SyncConsumer extends AbstractSyncAgent{

    public SyncConsumer(SyncBufferMonitor bufferMonitor, long seed, int bound, boolean verbose, long workToDo) {
        super(bufferMonitor, seed, bound, verbose, workToDo);
    }

    @Override
    protected void run() {
        while (!stopped) {
            int n = random.nextInt(bound) + 1;
            List<Integer> list;
            try {
                list = bufferMonitor. getList(n);
            } catch (InterruptedException e) {
                break;
            }

            Utils.work(workToDo);

            tasksDone.add(1L);

            if (verbose) {
                System.out.printf("[%s], got %s\n", Thread.currentThread().getName(), list.toString());
            }
        }
    }
}
