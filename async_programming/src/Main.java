import agents.Consumer;
import agents.Producer;
import structures.BufferMonitor;
import structures.Scheduler;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        /*
        structures.Scheduler scheduler = new structures.Scheduler(50, 5);
        int randomBound  = 10;

        int producers = 5;
        int consumers = 5;

        for (int i = 0; i < consumers; i++) {
            agents.Consumer consumer = new agents.Consumer(scheduler, randomBound);
            new Thread(consumer).start();
        }
        for (int i = 0; i < producers; i++) {
            agents.Producer producer = new agents.Producer(scheduler, randomBound);
            new Thread(producer).start();
        }

        Thread.sleep(2*1000);

        scheduler.stop();
        Thread.sleep(50);

        System.out.println(scheduler.bufferMonitor.total_get);


        */
        Scheduler scheduler = new Scheduler(100);
        BufferMonitor bufferMonitor = new BufferMonitor(20);

        int producers = 2;
        int consumers = 2;
        int bound = 10;

        for (int i = 0; i < producers; i++) {
            Producer producer = new Producer(scheduler, bufferMonitor, i, bound, true);
        }
        for (int i = 0; i < consumers; i++) {
            Consumer consumer = new Consumer(scheduler, bufferMonitor, i, bound, true);
        }

        Thread.sleep(1000);

        scheduler.stop();
        Thread.sleep(50);

        System.out.println(bufferMonitor.total_get);


    }
}