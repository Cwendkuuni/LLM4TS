package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.VectorValueFunction;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test integration of a simple differential equation.
     */
    @org.junit.Test
    public void testSimpleIntegration() {
        double[] y0 = new double[] { 1.0, 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;
        double  h = 0.1;

        FirstOrderDifferentialEquations equations =
            new VectorValueFunction(2) {

                @Override
                public int getDimension() {
                    return 2;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] g) {
                    g[0] = y[1];
                    g[1] = -y[0];
                }

            };

        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("Fehlberg78", false, 7, 1.0e-9, 1.0e-6);

        double actual = integrator.integrate(equations, t0, y0, t1, new double[2]);
        double expected = ODETestUtils.testDoubleArrayEquals(y0, 2, 1.0);
        assertEquals(expected, actual, 1.0e-6);
    }

    /**
     * Test integration with an event.
     */
    @org.junit.Test
    public void testIntegrationWithEvent() {
        double[] y0 = new double[] { 0.0, 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations =
            new VectorValueFunction(2) {

                @Override
                public int getDimension() {
                    return 2;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] g) {
                    g[0] = y[1];
                    g[1] = -y[0];
                }

            };

        StepHandler handler = new StepHandler() {

            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double t = interpolator.getTime();
                if (t >= 0.5) {
                    assertTrue(interpolator.hasReachedStepLimit());
                }
            }

        };

        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("Fehlberg78", false, 7, 1.0e-9, 1.0e-6);

        integrator.addStepHandler(handler);
        integrator.setEventsHandlersManager(
            new CombinedEventsManager() {

                @Override
                public boolean evaluateStep(StepInterpolator interpolator) {
                    double t = interpolator.getTime();
                    double[] y = interpolator.getState();
                    return new When(t, new double[] { 0.5 }) {}.isSatisfied();
                }

            });

        double actual = integrator.integrate(equations, t0, y0, t1, new double[2]);
        double expected = ODETestUtils.testDoubleArrayEquals(y0, 2, 0.5);
        assertEquals(expected, actual, 1.0e-6);
        assertFalse(handler.isEventReached());
    }

    /**
     * Test integration with a step limit.
     */
    @org.junit.Test
    public void testIntegrationWithStepLimit() {
        double[] y0 = new double[] { 0.0, 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations =
            new VectorValueFunction(2) {

                @Override
                public int getDimension() {
                    return 2;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] g) {
                    g[0] = y[1];
                    g[1] = -y[0];
                }

            };

        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("Fehlberg78", false, 7, 1.0e-9, 1.0e-6);

        integrator.setMaxStep(0.1);

        double actual = integrator.integrate(equations, t0, y0, t1, new double[2]);
        double expected = ODETestUtils.testDoubleArrayEquals(y0, 2, 0.1);
        assertEquals(expected, actual, 1.0e-6);
    }

    /**
     * Test integration with a small step size.
     */
    @org.junit.Test
    public void testIntegrationWithSmallStepSize() {
        double[] y0 = new double[] { 0.0, 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations =
            new VectorValueFunction(2) {

                @Override
                public int getDimension() {
                    return 2;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] g) {
                    g[0] = y[1];
                    g[1] = -y[0];
                }

            };

        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegratorFactory()
                .createIntegrator("Fehlberg78", false, 7, 1.0e-9, 1.0e-6);

        integrator.setMinStep(1.0e-6);

        double actual = integrator.integrate(equations, t0, y0, t1, new double[2]);
        double expected = ODETestUtils.testDoubleArrayEquals(y0, 2, 1.0);
        assertEquals(expected, actual, 1.0e-6);
    }

}