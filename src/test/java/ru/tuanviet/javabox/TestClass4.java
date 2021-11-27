package ru.tuanviet.javabox;

public class TestClass4 {
    @Benchmark(repeats = 100000, timeout = 20L)
    public void should_multiply_100000_times_in_1000_millis() {
        for (int i = 540000; i > 1; i--) {
            App.multiply(i, i + 1);

        }
    }
}
