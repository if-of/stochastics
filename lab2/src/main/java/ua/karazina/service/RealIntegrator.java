package ua.karazina.service;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.exp;

public class RealIntegrator {

    private final double a;
    private final double u;
    private final double k;

    public RealIntegrator(double a, double u, double k) {
        this.a = a;
        this.u = u;
        this.k = k;
    }

    public double integrate() {
        double x = (a * (1.0 / 6));

        double y;
        if (u != 0) {
            y = (-1.0 / u) * (exp(-1.0 * u) - 1);
        } else {
            y = exp(0);
        }

        double z = (-1.0 / (PI * k)) * (cos(PI * k) - 1);

        return x * y * z;
    }

    /**
     * Use it like test
     */
    public static void main(String[] args) {
        RealIntegrator realIntegrator = new RealIntegrator(3, 5, 7);
        System.out.println(realIntegrator.integrate());

        MyFunction myFunction = new MyFunction(3, 5, 7);

        RectangleIntegrator rectangleIntegrator = new RectangleIntegrator(myFunction);
        System.out.println(rectangleIntegrator.integrate(100000));
        System.out.println("sigma: " + rectangleIntegrator.calculateSigma());

        SimpleMonteCarloIntegrator simpleMonteCarloIntegrator = new SimpleMonteCarloIntegrator(myFunction);
        System.out.println(simpleMonteCarloIntegrator.integrate(10000000));
        System.out.println("sigma: " + simpleMonteCarloIntegrator.calculateSigma());

        GeometricMonteCarloIntegrator geometricMonteCarloIntegrator = new GeometricMonteCarloIntegrator(myFunction);
        System.out.println(geometricMonteCarloIntegrator.integrate(100, 10000000));
        System.out.println("sigma: " + geometricMonteCarloIntegrator.calculateSigma());
    }
}
