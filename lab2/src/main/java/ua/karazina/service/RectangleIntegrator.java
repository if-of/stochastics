package ua.karazina.service;

public class RectangleIntegrator {

    private final MyFunction myFunction;

    private int N;

    public RectangleIntegrator(MyFunction myFunction) {
        this.myFunction = myFunction;
    }

    public double integrate(int N) {
        this.N = N;
        double step = 1.0 / N;
        double current = step;
        int stepCount = 0;

        double fx = 0;
        double fy = 0;
        double fz = 0;

        while (current <= 1.0) {
            fx += myFunction.xFunction(current);
            fy += myFunction.yFunction(current);
            fz += myFunction.zFunction(current);
            stepCount++;
            current += step;
        }

        fx /= stepCount;
        fy /= stepCount;
        fz /= stepCount;

        return fx * fy * fz;
    }

    public double calculateSigma() {
        return Math.abs(integrate(N) - integrate(N * 2));
    }
}