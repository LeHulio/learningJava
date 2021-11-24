package ru.tuanviet.javabox;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Benchmark {
    public long repeats() default 10L;
    public long timeout() default 1000L;

}
