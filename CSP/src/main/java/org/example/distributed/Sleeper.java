package org.example.distributed;

public class Sleeper {
    public static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepRandom(int delay) {
        try {
            Thread.sleep((int) (Math.random() * delay));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
