package ru.tuanviet.javabox;

import java.util.ArrayList;

class AnnotatedMethodCalledStatistics {
    private final String status;
    private final long methodRepeats;
    private final double averageTime;
    private final double maxTime;
    private final long maxMethodRepeats;
    private final ArrayList<Double> diffTimeCollection;

    AnnotatedMethodCalledStatistics(String status,
                                    long methodRepeats,
                                    double averageTime,
                                    double maxTime,
                                    long maxMethodRepeats,
                                    ArrayList<Double> diffTimeCollection) {
        this.status = status;
        this.methodRepeats = methodRepeats;
        this.averageTime = averageTime;
        this.maxTime = maxTime;
        this.maxMethodRepeats = maxMethodRepeats;
        this.diffTimeCollection = diffTimeCollection;
    }

    public String getStatus() {
        return status;
    }

    public long getMethodRepeats() {
        return methodRepeats;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public long getAnnotatedMethodRepeats() {
        return maxMethodRepeats;
    }

    public ArrayList<Double> getDiffTimeCollection() {
        return diffTimeCollection;
    }
}
