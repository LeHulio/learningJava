package ru.tuanviet.javabox;

public class TestClass3 {
    @Benchmark(repeats = 0)
    public void should_multiply_10_times_in_1000_millis() {
        for (int i = 540000; i > 1; i--) {
            App.multiply(i, i + 1);

        }
    }

}
