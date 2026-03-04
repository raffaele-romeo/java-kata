package com.javaconcurrencyinpractice;

public class Holder {
    private static final ThreadLocal<Integer> counter =
            new ThreadLocal<Integer>() {
                @Override protected Integer initialValue() {
                    return 0;
                }
            };

    public void increment() {
        counter.set(counter.get() + 1);
    }

    public int get() {
        return counter.get();
    }
}
