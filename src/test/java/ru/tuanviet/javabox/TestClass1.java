package ru.tuanviet.javabox;

public class TestClass1 {


    @Benchmark(repeats = 10000, timeout = 5)
    public void should_substract_100_times_in_10000_millis() {
        for (int i = 230000; i > 1; i--) {
            App.multiply(i, i+1);

        }
    }
}
