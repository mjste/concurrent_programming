import java.util.LinkedList;
import java.util.List;

public class IncDec {
    int val;
    public List<Thread> threadList = new LinkedList<>();

    void createThreads(int threads, int iterations, int change) {
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread() {
                public void run() {
                    for (int j = 0; j < iterations; j++) {
//                        val = val + change;
                        fun(change);
                    }
                }
            };
            threadList.add(thread);
            thread.start();
        }
    }

    synchronized void fun(int change) {
        val = val + change;
    }


    void joinAll() throws InterruptedException {
        for (Thread thread : threadList) {
            thread.join();
        }
    }
}
