package com.javaconcurrencyinpractice;

import java.util.concurrent.locks.ReentrantLock;

public class PrivateLock {
    private final Object lock = new Object();

    public void someMethod() {
        synchronized (lock) {
            //
        }

        var lock2 = new ReentrantLock();
    }
}
