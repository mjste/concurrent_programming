package engines;

import agents.sync.SyncConsumer;
import agents.sync.SyncProducer;
import other.ArgumentParser;
import structures.sync.SyncBufferMonitor;

import java.util.ArrayList;
import java.util.List;

import static other.Utils.meanDouble;
import static other.Utils.meanLong;

public class SyncEngine implements IEngine {
    public ArgumentParser argParser;
    public long time;
    public double prodMean;
    public double consMean;

    public SyncEngine(ArgumentParser argParser) throws InterruptedException {
        this.argParser = argParser;
        run();
    }

    public void run() throws InterruptedException {
        long startTime = System.nanoTime();

        SyncBufferMonitor bufferMonitor = new SyncBufferMonitor(2*argParser.bound, argParser.objectWork);

        List<SyncProducer> syncProducerList = new ArrayList<>();
        List<SyncConsumer> syncConsumerList = new ArrayList<>();


        for (int i = 0; i < argParser.producers; i++) {
            SyncProducer syncProducer = new SyncProducer(bufferMonitor, i, argParser.bound, false, argParser.agentWork);
            syncProducerList.add(syncProducer);
        }
        for (int i = 0; i < argParser.consumers; i++) {
            SyncConsumer syncConsumer = new SyncConsumer(bufferMonitor, i, argParser.bound, false, argParser.agentWork);
            syncConsumerList.add(syncConsumer);
        }

        while (bufferMonitor.totalGet < argParser.totalGet) {
            Thread.sleep(10);
        }

        bufferMonitor.stop();
        long stopTime = System.nanoTime();

        for (SyncProducer syncProducer : syncProducerList)
            syncProducer.stop();
        for (SyncConsumer syncConsumer : syncConsumerList)
            syncConsumer.stop();

        Thread.sleep(50);


        List<Double> prodMeans = new ArrayList<>();
        List<Double> consMeans = new ArrayList<>();

        for (SyncProducer syncProducer : syncProducerList) prodMeans.add(meanLong(syncProducer.tasksDone));
        for (SyncConsumer syncConsumer : syncConsumerList) consMeans.add(meanLong(syncConsumer.tasksDone));

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
