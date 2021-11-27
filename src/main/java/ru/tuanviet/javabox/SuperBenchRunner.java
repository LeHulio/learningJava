package ru.tuanviet.javabox;


import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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

        for (Method method : testClass.getMethods()) {
            Annotation annotation =
                    method.getAnnotation(Test.class);
            if ( annotation != null ) {
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
            Object instance = testClass.newInstance();

            methodDescriptions.forEach((method, description) ->
            {
                try {
                    notifier.fireTestStarted(description);

                    Benchmark benchmark = method.getAnnotation(Benchmark.class);
                    for (int i = 0; i < benchmark.repeats(); i++) {
                        LocalDateTime startTime = LocalDateTime.now();
                        method.invoke(instance);
                        LocalDateTime endTime = LocalDateTime.now();
                        double diffTimeMillis = ChronoUnit.MILLIS.between(startTime, endTime);

                        if ( diffTimeMillis > benchmark.timeout() ) {
                            break;
                        }
                    }

                    notifier.fireTestFinished(description);
                }
                catch(AssumptionViolatedException e) {
                    Failure failure = new Failure(description, e.getCause());
                    notifier.fireTestAssumptionFailed(failure);
                }
                catch(Throwable e) {
                    Failure failure = new Failure(description, e.getCause());
                    notifier.fireTestFailure(failure);
                }
                finally {
                    notifier.fireTestFinished(description);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
