package org.example.distributed;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {
    private final static AtomicLong counter = new AtomicLong(0);

    public static void increment() {
        long val = counter.incrementAndGet();
        if (val % 100000 == 0) {
//            System.out.printf("Counter: %d\n", val);
        }
    }
}
