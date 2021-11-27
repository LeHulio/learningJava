package ru.tuanviet.javabox;


import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class SuperBenchRunner extends Runner {

    private final Class<?> testClass;
    private final HashMap<Method, Description> methodDescriptions;

    public SuperBenchRunner(Class<?> testClass) {
        super();
        this.testClass = testClass;
        methodDescriptions = new HashMap<>();
    }

    @Override
    public Description getDescription() {
        Description description =
                Description.createSuiteDescription(
                        testClass.getName(),
                        testClass.getAnnotations());

        for(Method method : testClass.getMethods()) {
            Annotation annotation =
                    method.getAnnotation(Test.class);
            if(annotation != null) {
                Description methodDescription =
                        Description.createTestDescription(
                                testClass,
                                method.getName(),
                                annotation);
                description.addChild(methodDescription);

                methodDescriptions.put(method, methodDescription);
            }
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        System.out.println("running the tests from SuperBenchRunner: " + testClass);
        try {
            Object testObject = testClass.getDeclaredConstructor().newInstance();
            for (Method method : testClass.getMethods()) {
                if ( method.isAnnotationPresent(Benchmark.class) ) {
                    notifier.fireTestStarted(Description
                            .createTestDescription(testClass, method.getName()));

                    Benchmark benchmark = method.getAnnotation(Benchmark.class);
                    for (int i = 0; i < benchmark.repeats(); i++) {
                        LocalDateTime startTime = LocalDateTime.now();
                        method.invoke(testObject);
                        LocalDateTime endTime = LocalDateTime.now();
                        double diffTimeMillis = ChronoUnit.MILLIS.between(startTime, endTime);


                        if ( diffTimeMillis > benchmark.timeout() ) {

                            break;
                        }
                    }

                    notifier.fireTestFinished(Description
                            .createTestDescription(testClass, method.getName()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
