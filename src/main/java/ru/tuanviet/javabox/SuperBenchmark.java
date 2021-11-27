package ru.tuanviet.javabox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SuperBenchmark {
    private final double percentile75 = 75d;
    private final double percentile95 = 95d;
    private final double percentile99 = 99d;
    private String result;

    private ArrayList<AnnotatedMethodCalledStatistics> calledStatisticsArrayList = new ArrayList<>();

    @Benchmark(repeats = 1, timeout = 1000)
    public void benchmark(Class<?>[] classes) {
        StringBuilder resultBuilder = new StringBuilder();

        if ( classes == null || classes.length == 0 ) {
            throw new IllegalArgumentException("list should not be empty or null");
        }

        createHeaderWithLocalDateTime(resultBuilder);

        finalResultBuilder(resultBuilder, classes);

        System.out.println(resultBuilder);
        result = resultBuilder.toString();
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
        for (Class<?> clazz : classes) {
            if ( clazz == null ) {
                continue;
            }
            checkAndAppendBenchmarkMethods(resultBuilder, clazz);
        }
    }

    private void checkAndAppendBenchmarkMethods(StringBuilder resultBuilder, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if ( method.isAnnotationPresent(Benchmark.class) ) {
                Benchmark benchmark = method.getAnnotation(Benchmark.class);
                method.setAccessible(true);
                benchmarkMethodsResultBuilder(resultBuilder, clazz, method, benchmark);
            }
        }
    }

    private void benchmarkMethodsResultBuilder(StringBuilder resultBuilder, Class<?> clazz, Method method, Benchmark benchmark) {
        if ( benchmark.repeats() == 0 ) {
            System.out.println(resultBuilder);
            throw new IllegalArgumentException("Benchmark repeats should be greater than 0");
        }

        long methodRepeats = 0;
        long maxMethodRepeats = benchmark.repeats();
        String testStatus = "PASSED";
        ArrayList<Double> diffTimeCollection = new ArrayList<>();
        try {
            for (int i = 0; i < benchmark.repeats(); i++) {
                LocalDateTime startTime = LocalDateTime.now();
                method.invoke(clazz.getDeclaredConstructor().newInstance());
                LocalDateTime endTime = LocalDateTime.now();
                double diffTimeMillis = ChronoUnit.MILLIS.between(startTime, endTime);
                diffTimeCollection.add(diffTimeMillis);

                methodRepeats++;

                if ( diffTimeMillis > benchmark.timeout() ) {
                    Collections.sort(diffTimeCollection);
                    testStatus = "FAILED";
                    break;
                }
            }
            Collections.sort(diffTimeCollection);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }

        double minTime = diffTimeCollection.get(0);
        double maxTime = diffTimeCollection.get(diffTimeCollection.size() - 1);
        double averageTime = diffTimeCollection.stream()
                .mapToDouble(a -> a)
                .average().getAsDouble();


        double resultFor75 = percentile(diffTimeCollection, percentile75);
        double resultFor95 = percentile(diffTimeCollection, percentile95);
        double resultFor99 = percentile(diffTimeCollection, percentile99);

        String nameID = UUID.randomUUID().toString();
        String methodName = getFormattedStringFromCamelAndSnakeCase(method);

        AnnotatedMethodCalledStatistics statistics = new AnnotatedMethodCalledStatistics(testStatus,
                methodRepeats, averageTime, maxTime, maxMethodRepeats, diffTimeCollection);
        calledStatisticsArrayList.add(statistics);

        resultBuilder.append("[Test ").append(nameID).append(" ").append(testStatus).append("]\n")
                .append(methodName)
                .append("\n")
                .append("Repeats: ").append(methodRepeats).append("/").append(maxMethodRepeats).append("\n")
                .append("Timeout: ").append(benchmark.timeout()).append("ms").append("\n")
                .append("Min: ").append(minTime).append("ms\n")
                .append("Avg: ").append(averageTime).append("ms\n")
                .append("Max: ").append(maxTime).append("ms\n")
                .append("Percentile 75: ").append(resultFor75).append("\n")
                .append("Percentile 95: ").append(resultFor95).append("\n")
                .append("Percentile 99: ").append(resultFor99).append("\n").append("\n");

    }

    private String getFormattedStringFromCamelAndSnakeCase(Method method) {
        String methodName = method.getName().replaceFirst("should", ">");

        if ( Character.isUpperCase(methodName.charAt(1)) ) {
            methodName = methodName.replaceAll("([A-Z])", " $1").toLowerCase(Locale.ROOT)
                    .replaceAll("(?<=\\D)(?=\\d)", " ");
        } else {
            methodName = methodName.replaceAll("_", " ");
        }
        methodName = methodName.substring(0, 2)
                + methodName.substring(2, 3).toUpperCase(Locale.ROOT)
                + methodName.substring(3);
        return methodName;
    }

    private double percentile(List<Double> latencies, double percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * latencies.size());
        return latencies.get(index - 1);
    }

    @Override
    public String toString() {
        return result;
    }

    public ArrayList<AnnotatedMethodCalledStatistics> getCalledStatisticsArrayList() {
        return calledStatisticsArrayList;
    }
}
