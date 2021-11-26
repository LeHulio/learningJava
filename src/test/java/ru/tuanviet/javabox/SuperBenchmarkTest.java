package ru.tuanviet.javabox;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


public class SuperBenchmarkTest {

    SuperBenchmark sutSB = new SuperBenchmark();


    @Test
    public void shouldRepeatMethodOneOrMoreTimesInTestClass1() throws NoSuchMethodException {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        Benchmark annotation = TestClass1.class
                .getDeclaredMethod("shouldMultiply100TimesIn50Millis")
                .getAnnotation(Benchmark.class);

        assertThat(annotation.repeats()).isEqualTo(100);

    }

    @Test
    public void shouldRepeatMethodInAnnotationDefault10TimesInTestClass2() throws NoSuchMethodException {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        assertThat(sutSB.getCalledStatisticsArrayList().get(1).getAnnotatedMethodRepeats()).isEqualTo(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException() {
        sutSB.benchmark(null);
    }

    @Test
    public void shouldCheckTestStatusInStatisticsWithStatusPassed() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        assertThat(sutSB.getCalledStatisticsArrayList().get(1).getStatus()).isEqualTo("PASSED");

    }

    @Test
    public void shouldCheckTestStatusInStatisticsWithStatusFailed() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        assertThat(sutSB.getCalledStatisticsArrayList().get(0).getStatus()).isEqualTo("FAILED");

    }

    @Test
    public void shouldCheckTestRepeatsInStatisticsAtLeastOneTime() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        assertThat(sutSB.getCalledStatisticsArrayList().get(0).getMethodRepeats()).isGreaterThan(0);

    }

    @Test
    public void shouldCheckTestAllRepeatsInStatistics() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        assertThat(sutSB.getCalledStatisticsArrayList().get(1).getMethodRepeats()).isEqualTo(10);
    }

    @Test
    public void shouldCheckTestAverageTimeInStatistics() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);
        Double sum = 0d;
        ArrayList<Double> testCollection = sutSB.getCalledStatisticsArrayList().get(1).getDiffTimeCollection();
        for (Double diffTime : testCollection) {
            sum += diffTime;
        }

        long methodRepeats = sutSB.getCalledStatisticsArrayList().get(1).getMethodRepeats();
        Double testAverage = sum/methodRepeats;

        assertThat(sutSB.getCalledStatisticsArrayList().get(1).getAverageTime()).isEqualTo(testAverage);
    }

    @Test
    public void shouldCheckTestMaxTimeInStatisticsEqualsAverageAndMinInTestClass1() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};

        sutSB.benchmark(testList);

        assertThat(sutSB.getCalledStatisticsArrayList().get(0).getAverageTime()).isEqualTo(sutSB.getCalledStatisticsArrayList().get(0).getMaxTime());

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfRepeats0InTestClass3() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class, TestClass3.class};

        sutSB.benchmark(testList);

    }

}