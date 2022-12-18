package org.example.distributed;

public class Sleeper {
    public static void sleepFixed(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }

    public static void sleepRandom(int delay) {
        try {
            Thread.sleep((int) (Math.random() * delay));
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }

    public static void sleep(int delay, boolean randomDelay) {
        if (randomDelay) {
            sleepRandom(delay);
        } else {
            sleepFixed(delay);
        }
    }
}
