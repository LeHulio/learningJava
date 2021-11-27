package ru.tuanviet.javabox;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperBenchRunnerTest {
    TestClass5 sutClass5 = new TestClass5();

    @Test
    public void shouldRepeatMethod100000Times() throws NoSuchMethodException {

        Benchmark benchmark = TestClass5.class.getDeclaredMethod("multiply", Integer.TYPE, Integer.TYPE)
                .getAnnotation(Benchmark.class);

        ArrayList<Long> collection = new ArrayList<>();
        for (int i = 0; i < benchmark.repeats(); i++) {
            collection.add(sutClass5.multiply(1, 1));
        }
        assertThat(collection.size()).isEqualTo(100000);
    }

    @Test
    public void shouldCheckMultiplyResult() {
        assertThat(sutClass5.multiply(5, 2)).isEqualTo(10);

    }

}
