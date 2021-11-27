package ru.tuanviet.javabox;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SuperBenchRunner.class)
public class SuperBenchRunnerTest {
    TestClass5 sutClass5 = new TestClass5();

    @Test
    @Benchmark
    public void shouldRepeatMethod100000Times() throws NoSuchMethodException {
        assertThat(sutClass5.multiply(3, 7)).isEqualTo(21);
    }

    @Test
    @Benchmark
    public void shouldCheckMultiplyResult() {
        //System.out.println("1");
        assertThat(sutClass5.multiply(5, 2)).isEqualTo(10);
    }

}
