package com.jvmprofilerlearning;

import java.util.*;
import java.util.concurrent.*;

/**
 * Demo program with intentional performance issues for profiler learning
 * Run with: java -Xmx512m ProfilerDemo
 *
 * Profiling tools to use:
 * 1. VisualVM (free, bundled with JDK)
 * 2. JProfiler (commercial)
 * 3. YourKit (commercial)
 * 4. Java Flight Recorder: java -XX:StartFlightRecording=duration=60s,filename=recording.jfr ProfilerDemo
 */
public class ProfilerDemo {
    // Memory leak: static collection that keeps growing
    private static List<byte[]> memoryLeakList = new ArrayList<>();

    // Cache that never cleans up
    private static Map<String, LargeObject> cache = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting ProfilerDemo - Watch this in your profiler!");
        System.out.println("The program will run for 2 minutes with various issues.\n");

        // Run different scenarios
        Thread leakThread = new Thread(() -> simulateMemoryLeak());
        Thread cpuThread = new Thread(() -> simulateCpuIntensiveTask());
        Thread inefficientThread = new Thread(() -> simulateInefficientCode());
        Thread blockingThread = new Thread(() -> simulateBlockingOperations());
        new Thread(() -> {
            while(true) {
                clearLeak();  // Clear first
                try {
                    Thread.sleep(10000);  // Then wait 15 seconds
                } catch (InterruptedException e) {
                    break;  // Use break, not throw
                }
            }
        }).start();

        leakThread.setName("Memory-Leak-Thread");
        cpuThread.setName("CPU-Intensive-Thread");
        inefficientThread.setName("Inefficient-Code-Thread");
        blockingThread.setName("Blocking-Thread");

        leakThread.start();
        cpuThread.start();
        inefficientThread.start();
        blockingThread.start();

        // Monitor and report
        monitorMemory();

        leakThread.join();
        cpuThread.join();
        inefficientThread.join();
        blockingThread.join();

        System.out.println("\nDemo completed!");
    }

    /**
     * PROFILER LESSON 1: Memory Leak Detection
     * Watch the heap grow continuously in the profiler
     * Look for: Growing number of byte[] objects, increasing heap usage
     */
    private static void simulateMemoryLeak() {
        System.out.println("[Memory Leak] Starting - watch heap growth in profiler");

        for (int i = 0; i < 1000; i++) {
            // Allocate 1MB arrays and keep references
            byte[] leak = new byte[1024 * 1024]; // 1MB
            memoryLeakList.add(leak);

            // Also leak through cache
            cache.put("key" + i, new LargeObject(i));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }

            if (i % 10 == 0) {
                System.out.println("[Memory Leak] Allocated " + i + " MB, cache size: " + cache.size());
            }
        }
    }

    /**
     * PROFILER LESSON 2: CPU Hotspots
     * Watch CPU time spent in this method
     * Look for: High CPU usage, time spent in inefficient algorithms
     */
    private static void simulateCpuIntensiveTask() {
        System.out.println("[CPU Intensive] Starting - watch CPU usage spike");

        for (int i = 0; i < 100; i++) {
            // Inefficient algorithm: repeated string concatenation
            String result = "";
            for (int j = 0; j < 10000; j++) {
                result += "x"; // Creates new string object each time!
            }

            // Nested loops creating O(n²) complexity
            List<Integer> numbers = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
                numbers.add(j);
            }

            // Inefficient contains check
            for (int j = 0; j < 1000; j++) {
                numbers.contains(j); // O(n) lookup in ArrayList
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * PROFILER LESSON 3: Object Allocation Rate
     * Watch for high allocation rate and GC pressure
     * Look for: Frequent garbage collection, high object creation rate
     */
    private static void simulateInefficientCode() {
        System.out.println("[Inefficient Code] Starting - watch object allocation rate");

        for (int i = 0; i < 1000; i++) {
            // Creating many temporary objects
            for (int j = 0; j < 1000; j++) {
                // Boxing/unboxing creates objects
                Integer temp = Integer.valueOf(j);
                String str = new String("temporary" + temp);
                List<String> tempList = new ArrayList<>();
                tempList.add(str);
                // All these objects become garbage immediately
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }

            if (i % 100 == 0) {
                System.out.println("[Inefficient Code] Iteration " + i);
            }
        }
    }

    /**
     * PROFILER LESSON 4: Thread Blocking
     * Watch thread states and blocking time
     * Look for: Threads in BLOCKED/WAITING state
     */
    private static void simulateBlockingOperations() {
        System.out.println("[Blocking] Starting - watch thread states");

        Object lock = new Object();

        for (int i = 0; i < 50; i++) {
            synchronized (lock) {
                try {
                    // Simulate slow I/O or database operation
                    Thread.sleep(200);

                    // Do some work while holding lock
                    processData();

                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private static void processData() {
        // Simulate work
        double result = 0;
        for (int i = 0; i < 100000; i++) {
            result += Math.sqrt(i);
        }
    }

    /**
     * Monitor memory and print stats
     */
    private static void monitorMemory() {
        Thread monitor = new Thread(() -> {
            Runtime runtime = Runtime.getRuntime();

            for (int i = 0; i < 120; i++) {
                long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                long maxMemory = runtime.maxMemory() / 1024 / 1024;

                System.out.println("\n[MONITOR] Memory: " + usedMemory + "MB / " + maxMemory + "MB");
                System.out.println("[MONITOR] Leak list size: " + memoryLeakList.size() + " items");
                System.out.println("[MONITOR] Cache size: " + cache.size() + " items");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        monitor.setName("Memory-Monitor-Thread");
        monitor.setDaemon(true);
        monitor.start();
    }

    public static void clearLeak() {
        memoryLeakList.clear();
        cache.clear();
    }

    /**
     * Large object for cache demonstration
     */
    static class LargeObject {
        private byte[] data;
        private String description;
        private int id;

        public LargeObject(int id) {
            this.id = id;
            this.data = new byte[100 * 1024]; // 100KB
            this.description = "Large object with id: " + id;
        }
    }
}