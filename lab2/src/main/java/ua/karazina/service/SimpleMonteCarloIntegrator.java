package ua.karazina.service;

import java.util.Random;

public class SimpleMonteCarloIntegrator {

    private final MyFunction myFunction;
    private final Random random = new Random();

    private double sumFx;
    private double sumSquareFx;
    private double N;

    public SimpleMonteCarloIntegrator(MyFunction myFunction) {
        this.myFunction = myFunction;
    }

    public double integrate(int N) {
        this.N = N;
        sumFx = 0;
        sumSquareFx = 0;

        double fTotal = 0;
        for (int i = 0; i < N; i++) {
            double f = myFunction.function(random.nextDouble(), random.nextDouble(), random.nextDouble());
            fTotal += f;

            sumFx += f;
            sumSquareFx += f * f;
        }
        fTotal /= N;

        return fTotal;
    }

    public double calculateSigma() {
        double coeff = 1.0 / (N);
        return Math.sqrt(Math.abs(
                coeff * (coeff * coeff * sumFx * sumFx - coeff * sumSquareFx)
        ));
    }
}
