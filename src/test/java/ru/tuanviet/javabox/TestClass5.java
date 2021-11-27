package ru.tuanviet.javabox;

import org.junit.runner.RunWith;

@RunWith(SuperBenchRunner.class)
public class TestClass5 {
    @Benchmark(repeats = 100000, timeout = 20L)
    public long multiply(int a, int b) {
        return (long) a * b;
    }
}
