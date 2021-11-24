package ru.tuanviet.javabox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.UUID;

public class SuperBenchmark {
    private String result;

    @Benchmark(repeats = 1, timeout = 1000)
    public void benchmark(Class<?>[] classes) {
        StringBuilder resultBuilder = new StringBuilder();

        if (classes == null || classes.length == 0) {
            throw new IllegalArgumentException("list should not be empty");
        }

        createHeaderWithLocalDateTime(resultBuilder);

        finalResultBuilder(resultBuilder, classes);

        System.out.println(resultBuilder);


    }
    private void createHeaderWithLocalDateTime(StringBuilder resultBuilder) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss.SSS");
        String date = dtf.format(LocalDateTime.now());
        resultBuilder.append("-- OUTPUT ------------------------------------------------")
                .append("\n")
                .append("Benchmark started at ")
                .append(date)
                .append("\n\n");
    }

    private void finalResultBuilder(StringBuilder resultBuilder, Class<?>[] classes) {
        for (Class<?> clazz: classes) {
            if(clazz == null){
                continue;
            }

            checkAndAppendBenchmarkMethods(resultBuilder,clazz);

        }
    }

    private void checkAndAppendBenchmarkMethods(StringBuilder resultBuilder, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Benchmark.class)) {
                Benchmark benchmark = method.getAnnotation(Benchmark.class);
                method.setAccessible(true);
                benchmarkMethodsResultBuilder(resultBuilder, clazz, method, benchmark);
            }
        }
    }

    private void benchmarkMethodsResultBuilder(StringBuilder resultBuilder, Class<?> clazz, Method method, Benchmark benchmark) {
        long methodRepeats = 0;
        long maxMethodRepeats = benchmark.repeats();
        String testStatus = "PASSED";
        ArrayList<Long> diffTimeCollection = new ArrayList<>();
        try {
            for (int i = 0; i < benchmark.repeats(); i ++) {
                LocalDateTime startTime = LocalDateTime.now();
                method.invoke(clazz.getDeclaredConstructor().newInstance());
                LocalDateTime endTime = LocalDateTime.now();
                long diffTimeMillis = ChronoUnit.MILLIS.between(startTime, endTime);
                diffTimeCollection.add(diffTimeMillis);

                methodRepeats++;

                if (diffTimeMillis > benchmark.timeout()) {
                    Collections.sort(diffTimeCollection);
                    testStatus = "FAILED";
                    break;
                }
            }
            Collections.sort(diffTimeCollection);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }

        long minTime = diffTimeCollection.get(0);
        long maxTime = diffTimeCollection.get(diffTimeCollection.size()-1);
        double averageTime = diffTimeCollection.stream()
                .mapToDouble(a -> a)
                .average().getAsDouble();


        String nameID = UUID.randomUUID().toString();

        resultBuilder.append("[Test ").append(nameID).append(" ").append(testStatus).append("]\n")
                .append("Repeats: ").append(methodRepeats).append("/").append(maxMethodRepeats).append("\n")
                .append("Timeout: ").append(benchmark.timeout()).append("ms").append("\n")
                .append("Min: ").append(minTime).append("ms\n")
                .append("Avg: ").append(averageTime).append("ms\n")
                .append("Max: ").append(maxTime).append("ms\n");

    }


    @Override
    public String toString() {
        return result;
    }
}
