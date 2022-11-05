import Agents.AbstractProdCons;
import Agents.Consumer;
import Agents.Producer;
import BufferMonitors.IMonitorBuffer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ProdConsEngine {
    private final List<AbstractProdCons> agentList = new ArrayList<>();
    private final List<Thread> threadList = new ArrayList<>();
    private final IMonitorBuffer monitorBuffer;
    private final int producers;
    private final int consumers;
    private final int maxPackage;
    private final int seed;
    private final ThreadMXBean threadManager = ManagementFactory.getThreadMXBean();

    public ProdConsEngine(int producers, int consumers, int maxPackage, IMonitorBuffer monitorBuffer) {
        this.monitorBuffer = monitorBuffer;
        this.producers = producers;
        this.consumers = consumers;
        this.maxPackage = maxPackage;
        this.seed = 42;
    }

    public void start() {
        for (int i = 0; i < producers; i++) {
            Producer producer = new Producer(monitorBuffer, maxPackage, seed + i);
            agentList.add(producer);
            Thread thread = new Thread(producer);
            thread.start();
            threadList.add(thread);
        }

        for (int i = 0; i < consumers; i++) {
            Consumer consumer = new Consumer(monitorBuffer, maxPackage, seed + i + 154);
            agentList.add(consumer);
            Thread thread = new Thread(consumer);
            thread.start();
            threadList.add(thread);
        }
    }

    public void stop() {
        List<Long> cpuTimes = new ArrayList<>();

        ListIterator<AbstractProdCons> agentIterator = agentList.listIterator();
        ListIterator<Thread> threadIterator = threadList.listIterator();

        while (agentIterator.hasNext())
        {
            AbstractProdCons agent = agentIterator.next();
            Thread thread = threadIterator.next();
            cpuTimes.add(threadManager.getThreadCpuTime(thread.getId()));
            agent.stop();
            thread.stop();
        }

        System.out.println("Cpu times: ");
        for (Long time : cpuTimes) {
            System.out.printf("%.3fs\n", (double)time/1000000000);
        }
        System.out.print("\n");
        System.out.printf("Total consumed: %d\nTotal times consumed: %d\n", monitorBuffer.get_total_consumed(), monitorBuffer.get_total_operations());
    }
}
