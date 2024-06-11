package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        double[][] a = {{0.346733235294118, 0.0, 0.678166470588237, 0.0},
                         {0.0, 0.833464522501618, 0.0, 1.20122564462439},
                         {0.678166470588237, 1.20122564462439, 0.346733235294118, 0.0},
                         {0.0, 1.20122564462439, 0.346733235294118, 0.0}};
        double[] b = {0.0, 0.0, 1.0 / 6.0, 1.0 / 2.0};
        double[] c = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 2.0};

        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, interpolator, 0.01, 0.01, 1.0e-6, 1.0e-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                double sum = 0.0;
                for (int i = 0; i < p0.length; ++i) {
                    sum += Math.pow(p1[i] - p2[i], 2);
                }
                return Math.sqrt(sum);
            }
        };

        ExpandableStatefulODE equations = new ODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        try {
            integrator.integrate(equations, 2.0);
            double[] expected = {1.0, 1.0};
            assertEquals(expected[0], equations.getCompleteState()[0], 1.0e-6);
            assertEquals(expected[1], equations.getCompleteState()[1], 1.0e-6);
        } catch (Exception e) {
            fail("Integration failed: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMismatchedDimensions() {
        double[][] a = {{0.346733235294118, 0.0}, {0.0, 0.833464522501618}};
        double[] b = {0.0, 0.0};
        double[] c = {0.0, 1.0 / 4.0};

        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, interpolator, 0.01, 0.01, 1.0e-6, 1.0e-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                return 0.0;
            }
        };

        ExpandableStatefulODE equations = new ODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
                dy[2] = 0.0;
            }
        };

        integrator.integrate(equations, 2.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        double[][] a = {{0.346733235294118, 0.0, 0.678166470588237, 0.0},
                         {0.0, 0.833464522501618, 0.0, 1.20122564462439},
                         {0.678166470588237, 1.20122564462439, 0.346733235294118, 0.0},
                         {0.0, 1.20122564462439, 0.346733235294118, 0.0}};
        double[] b = {0.0, 0.0, 1.0 / 6.0, 1.0 / 2.0};
        double[] c = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 2.0};

        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, interpolator, 0.01, 0.01, 1.0e-6, 1.0e-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                return 0.0;
            }
        };

        ExpandableStatefulODE equations = new ODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        integrator.setMaxSteps(1);
        integrator.integrate(equations, 10.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmall() {
        double[][] a = {{0.346733235294118, 0.0, 0.678166470588237, 0.0},
                         {0.0, 0.833464522501618, 0.0, 1.20122564462439},
                         {0.678166470588237, 1.20122564462439, 0.346733235294118, 0.0},
                         {0.0, 1.20122564462439, 0.346733235294118, 0.0}};
        double[] b = {0.0, 0.0, 1.0 / 6.0, 1.0 / 2.0};
        double[] c = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 2.0};

        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, interpolator, 0.01, 0.01, 1.0e-6, 1.0e-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                return 0.0;
            }
        };

        ExpandableStatefulODE equations = new ODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        integrator.setAbsoluteTolerance(1.0e-15);
        integrator.integrate(equations, 2.0);
    }

    @Test
    public void testErrorReduction() {
        double[][] a = {{0.346733235294118, 0.0, 0.678166470588237, 0.0},
                         {0.0, 0.833464522501618, 0.0, 1.20122564462439},
                         {0.678166470588237, 1.20122564462439, 0.346733235294118, 0.0},
                         {0.0, 1.20122564462439, 0.346733235294118, 0.0}};
        double[] b = {0.0, 0.0, 1.0 / 6.0, 1.0 / 2.0};
        double[] c = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 2.0};

        StepInterpolator interpolator = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, interpolator, 0.01, 0.01, 1.0e-6, 1.0e-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                double sum = 0.0;
                for (int i = 0; i < p0.length; ++i) {
                    sum += Math.pow(p1[i] - p2[i], 2);
                }
                return Math.sqrt(sum);
            }
        };

        ExpandableStatefulODE equations = new ODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        try {
            integrator.integrate(equations, 2.0);
            double[] expected = {1.0, 1.0};
            assertEquals(expected[0], equations.getCompleteState()[0], 1.0e-6);
            assertEquals(expected[1], equations.getCompleteState()[1], 1.0e-6);

            double oldError = integrator.getErrorEstimate();
            integrator.setRelativeTolerance(0.1);
            integrator.integrate(equations, 2.0);

            double newError = integrator.getErrorEstimate();
            assertTrue(newError < oldError);
        } catch (Exception e) {
            fail("Integration failed: " + e.getMessage());
        }
    }
}