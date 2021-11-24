package ru.tuanviet.javabox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;


public class SuperBenchmarkTest {

    SuperBenchmark sutSB = new SuperBenchmark();


    @Test
    public void shouldCreateSuperBenchmark() {
        Class<?>[] testList = new Class[]{TestClass1.class, TestClass2.class};
        sutSB.benchmark(testList);
        assertThat(sutSB.toString()).isEqualTo("1, 2");

    }

    @Test
    public void shouldPrintAllNamesOfClassesInArray(){
        Class<?>[] testList = new Class[]{TestClass1.class, null, TestClass2.class};
        sutSB.benchmark(testList);
        assertThat(sutSB.toString()).isEqualTo("ru.tuanviet.javabox.TestClass1, ru.tuanviet.javabox.TestClass2");
    }

}