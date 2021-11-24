package ru.tuanviet.javabox;

public class TestClass2 {

    @Benchmark
    public void should_substract_10_times_in_1000_millis() {
        for (int i = 540000; i > 1; i--) {
            App.multiply(i, i+1);

        }
    }

}
