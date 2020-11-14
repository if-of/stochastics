package ua.karazina.service;

import static java.lang.Math.random;

public class GeometricMonteCarloIntegrator {

    private final MyFunction myFunction;

    private double fMin;
    private double fMax;

    private double sumFx;
    private double sumSquareFx;
    private double N;

    public GeometricMonteCarloIntegrator(MyFunction myFunction) {
        this.myFunction = myFunction;
    }

    public double integrate(int N0, int N) {
        this.N = N;
        sumFx = 0;
        sumSquareFx = 0;

        calculateMinAndMaxX(N0);
        return integrate(N);
    }

    public double calculateSigma() {
        double coeff = 1.0 / (N);
        return Math.sqrt(Math.abs(
                coeff * (coeff * coeff * sumFx * sumFx - coeff * sumSquareFx)
        ));
    }

    private double integrate(int N) {
        var N1 = 0;
        for (int i = 0; i < N; i++) {
            double x = random();
            double y = random();
            double z = random();
            double fRand = fMin + (fMax - fMin) * random();
            double f = myFunction.function(x, y, z);
            if (f > fRand) N1++;

            sumFx += f;
            sumSquareFx += f * f;
        }
        return ((fMax - fMin) * N1 / N + fMin);
    }

    private void calculateMinAndMaxX(int N0) {
        double S;
        fMin = myFunction.xFunction(0);
        fMax = fMin;
        for (int i = 1; i <= N0; i++) {
            for (int j = 1; j <= N0; j++) {
                for (int k = 1; k <= N0; k++) {

                    S = myFunction.function(1.0 / N0 * i, 1.0 / N0 * j, 1.0 / N0 * k);
                    if (S < fMin) fMin = S;
                    if (S > fMax) fMax = S;
                }
            }
        }
    }
}
