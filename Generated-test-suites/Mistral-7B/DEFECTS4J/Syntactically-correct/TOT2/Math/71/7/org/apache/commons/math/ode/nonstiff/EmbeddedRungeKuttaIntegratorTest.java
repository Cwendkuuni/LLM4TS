import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicFunctionality() {
        double[] initialValues = new double[]{1.0, 0.0};
        double time = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0, 0.25, 0.25, 0.25, 0.25},
                                {0, 0.25, 0.25, 0.25, 0.25, 0.25}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
            @Override
            protected int getOrder() {
                return 4;
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * t;
                }
            }
        };

        double result = integrator.integrate(equations, 0.0, initialValues, time, new double[initialValues.length]);
        double[] expectedValues = new double[]{3.0, 1.5};
        Assert.assertArrayEquals(expectedValues, result, 1.0e-9);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        double[] initialValues = new double[]{1.0, 0.0};
        double time = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0, 0.25, 0.25, 0.25, 0.25},
                                {0, 0.25, 0.25, 0.25, 0.25, 0.25}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
            @Override
            protected int getOrder() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                // Intentionally left empty
            }
        };

        integrator.integrate(equations, 0.0, initialValues, time, new double[initialValues.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        double[] initialValues = new double[]{1.0, 0.0};
        double time = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0, 0.25, 0.25, 0.25, 0.25},
                                {0, 0.25, 0.25, 0.25, 0.25, 0.25}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new IntegratorException("An error occurred.");
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                // Intentionally left empty
            }
        };

        integrator.integrate(equations, 0.0, initialValues, time, new double[initialValues.length]);
    }

    @Test
    public void testEdgeCases() {
        double[] initialValues = new double[]{1.0, 0.0};
        double time = 1.0; // Minimal step

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0, 0.25, 0.25, 0.25, 0.25},
                                {0, 0.25, 0.25, 0.25, 0.25, 0.25}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator()) {
            @Override
            protected int getOrder() {
                return 4;
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = Math.pow(t, 2.0) * y[i];
                }
            }
        };

        double result = integrator.integrate(equations, 0.0, initialValues, time, new double[initialValues.length]);
        double[] expectedValues = new double[]{1.0, 1.0};
        Assert.assertArrayEquals(expectedValues, result, 1.0e-9);

        time = 0.0; // Zero step
        result = integrator.integrate(equations, 0.0, initialValues, time, new double[initialValues.length]);
        Assert.assertArrayEquals(initialValues, result, 1.0e-9);

        time = Double.POSITIVE_INFINITY; // Maximal step
        result = integrator.integrate(equations, 0.0, initialValues, time, new double[initialValues.length]);
        expectedValues = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        Assert.assertArrayEquals(expectedValues, result, 1.0e-9);
    }
}