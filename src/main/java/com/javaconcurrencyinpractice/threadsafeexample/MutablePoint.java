package com.javaconcurrencyinpractice.threadsafeexample;

public class MutablePoint {
    public int x;
    public int y;

    public MutablePoint() {
        this.x = 0;
        this.y = 0;
    }

    public MutablePoint(MutablePoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
