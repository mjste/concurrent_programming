import agents.Consumer;
import agents.Producer;
import structures.BufferMonitor;
import structures.Scheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArgumentParser argParser = new ArgumentParser(args);

        Scheduler scheduler = new Scheduler(100);
        BufferMonitor bufferMonitor = new BufferMonitor(2 * argParser.bound, false);
        List<Producer> producerList = new ArrayList<>();
        List<Consumer> consumerList = new ArrayList<>();


        for (int i = 0; i < argParser.producers; i++) {
            Producer producer = new Producer(scheduler, bufferMonitor, i, argParser.bound, false, argParser.workToDo);
            producerList.add(producer);
        }
        for (int i = 0; i < argParser.consumers; i++) {
            Consumer consumer = new Consumer(scheduler, bufferMonitor, i, argParser.bound, false, argParser.workToDo);
            consumerList.add(consumer);
        }

        Thread.sleep(argParser.time);

        scheduler.stop();
        for (Producer producer : producerList)
            producer.stop();
        for (Consumer consumer : consumerList)
            consumer.stop();

        Thread.sleep(50);

        // Metric 1: total consumed
        System.out.println(bufferMonitor.totalGet);


    }
}