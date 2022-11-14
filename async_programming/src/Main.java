
public class Main {
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler(50, 5);
        int randomBound  = 10;

        int producers = 5;
        int consumers = 5;
        for (int i = 0; i < consumers; i++) {
            Consumer consumer = new Consumer(scheduler.proxyQueue, randomBound);
            new Thread(consumer).start();
        }
        for (int i = 0; i < producers; i++) {
            Producer producer = new Producer(scheduler.proxyQueue, randomBound);
            new Thread(producer).start();
        }
//        scheduler.stop();
    }
}