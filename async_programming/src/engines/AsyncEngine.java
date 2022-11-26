package engines;

import agents.async.AsyncConsumer;
import agents.async.AsyncProducer;
import other.ArgumentParser;
import structures.async.BufferMonitor;
import structures.async.Scheduler;

import java.util.ArrayList;
import java.util.List;

import static other.Utils.meanDouble;
import static other.Utils.meanLong;

public class AsyncEngine implements IEngine{
    public ArgumentParser argParser;
    public long time;
    public double prodMean;
    public double consMean;

    public AsyncEngine(ArgumentParser argParser) throws InterruptedException {
        this.argParser = argParser;
        run();
    }

    public void run() throws InterruptedException {
        long startTime = System.nanoTime();
        Scheduler scheduler = new Scheduler(100);
        BufferMonitor bufferMonitor = new BufferMonitor(2 * argParser.bound, false, argParser.objectWork);
        List<AsyncProducer> asyncProducerList = new ArrayList<>();
        List<AsyncConsumer> asyncConsumerList = new ArrayList<>();


        for (int i = 0; i < argParser.producers; i++) {
            AsyncProducer asyncProducer = new AsyncProducer(scheduler, bufferMonitor, i, argParser.bound, false, argParser.agentWork);
            asyncProducerList.add(asyncProducer);
        }
        for (int i = 0; i < argParser.consumers; i++) {
            AsyncConsumer asyncConsumer = new AsyncConsumer(scheduler, bufferMonitor, i, argParser.bound, false, argParser.agentWork);
            asyncConsumerList.add(asyncConsumer);
        }

        while (bufferMonitor.totalGet < argParser.totalGet) {
            Thread.sleep(10);
        }

        scheduler.stop();
        long stopTime = System.nanoTime();

        for (AsyncProducer asyncProducer : asyncProducerList)
            asyncProducer.stop();
        for (AsyncConsumer asyncConsumer : asyncConsumerList)
            asyncConsumer.stop();

        Thread.sleep(50);


        List<Double> prodMeans = new ArrayList<>();
        List<Double> consMeans = new ArrayList<>();

        for (AsyncProducer asyncProducer : asyncProducerList) prodMeans.add(meanLong(asyncProducer.tasksDone));
        for (AsyncConsumer asyncConsumer : asyncConsumerList) consMeans.add(meanLong(asyncConsumer.tasksDone));

        prodMean = meanDouble(prodMeans);
        consMean = meanDouble(consMeans);
        time = stopTime-startTime;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public double getProdMean() {
        return prodMean;
    }

    @Override
    public double getConsMean() {
        return consMean;
    }
}
