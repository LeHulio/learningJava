package ru.tuanviet.javabox;

import java.util.ArrayList;

public class TestClass1 {


    @Benchmark(repeats = 100, timeout = 5)
    public void shouldMultiply100TimesIn50Millis() {

        ArrayList<Long> testList = new ArrayList<Long>();
        for (long i = 10000; i <= 50000; i++) {
            testList.add(i*i);
        }

        for (int i = 1; i < testList.size(); i++) {
            App.multiply(testList.get(i), testList.get(i-1));

        }
    }
}
