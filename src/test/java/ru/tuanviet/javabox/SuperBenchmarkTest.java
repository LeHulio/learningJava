package ru.tuanviet.javabox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;


public class SuperBenchmarkTest {

    SuperBenchmark sutSB = new SuperBenchmark();


    @Test
    public void shouldRepeatMethodOneOrMoreTimesInTestClass1() throws NoSuchMethodException {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        Annotation annotation = TestClass1.class
                .getDeclaredMethod("shouldMultiply100TimesIn50Millis")
                .getAnnotation(Benchmark.class);

            Benchmark benchmark = (Benchmark) annotation;
            assertThat(benchmark.repeats()).isGreaterThan(0);

    }

    @Test
    public void shouldRepeatMethodInAnnotationDefault10TimesInTestClass2() throws NoSuchMethodException {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        Annotation annotation = TestClass2.class
                .getDeclaredMethod("should_multiply_10_times_in_1000_millis")
                .getAnnotation(Benchmark.class);

        Benchmark benchmark = (Benchmark) annotation;
        assertThat(benchmark.repeats()).isGreaterThan(0);
    }

}