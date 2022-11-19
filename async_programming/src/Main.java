import engines.AsyncEngine;
import engines.IEngine;
import engines.SyncEngine;
import other.ArgumentParser;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArgumentParser argParser = new ArgumentParser(args);

        IEngine engine;
        switch (argParser.type) {
            case "sync" -> engine = new SyncEngine(argParser);
            case "async" -> engine = new AsyncEngine(argParser);
            default -> throw new RuntimeException();
        }

        // Metrics:
        // 1: time to handle n requests
        // 2: mean times of done tasks by producers
        // 3: mean times of done tasks by consumers
        System.out.printf("%.3f %.3f %.3f\n", (double) (engine.getTime()) / 1000000000, engine.getProdMean(), engine.getConsMean());
    }
}

