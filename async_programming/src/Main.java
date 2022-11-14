
public class Main {
    public static void main(String[] args) throws InterruptedException{
        Scheduler scheduler = new Scheduler(50, 5);
        int randomBound  = 10;

        int producers = 5;
        int consumers = 5;

        for (int i = 0; i < consumers; i++) {
            Consumer consumer = new Consumer(scheduler, randomBound);
            new Thread(consumer).start();
        }
        for (int i = 0; i < producers; i++) {
            Producer producer = new Producer(scheduler, randomBound);
            new Thread(producer).start();
        }

        Thread.sleep(2*1000);

        scheduler.stop();
        Thread.sleep(50);

        System.out.println(scheduler.bufferMonitor.total_get);

    }
}