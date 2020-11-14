package ua.karazina.service;

import lombok.RequiredArgsConstructor;

import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.sin;

@RequiredArgsConstructor
public class MyFunction {

    private final double a;
    private final double u;
    private final double k;

    public double function(double x, double y, double z) {
        return xFunction(x)
                * yFunction(y)
                * zFunction(z);
    }

    public double xFunction(double x) {
        return (a * (1 - x) * x);
    }

    public double yFunction(double y) {
        return exp(-1 * u * y);
    }

    public double zFunction(double z) {
        return sin(PI * k * z);
    }
}
