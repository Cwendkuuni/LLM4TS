package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ReligiousHoliday;
import org.apache.commons.math3.ode.events.StepEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepState;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static EmbeddedRungeKuttaIntegrator integrator;
    private static double[] y0;
    private static double[] y1;
    private static double t0 = 0.0;
    private static double t1 = 2.0 * Math.PI;

    /**
     * Sets up the test.
     */
    @BeforeClass
    public static void setUp() {
        // create integrator
        integrator = new EmbeddedRungeKuttaIntegrator("RK23", false, new double[] { 0.25, 0.5 },
                new double[][] { { 0.25, 0.45 }, { 0.5, 0.5 } }, new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(new double[] { 0.5 }));
        integrator.setMinStep(1.0e-12);
        integrator.setMaxStep(1.0);

        y0 = new double[] { 1.0, 1.0 };
        y1 = new double[] { 1.0, 1.0 };
    }

    /**
     * Test integration of simple ODE.
     * @throws DerivativeException if failed
     * @throws IntegratorException if failed
     */
    @Test
    public void testSimpleODE() throws DerivativeException, IntegratorException {
        final double exp = Math.E;
        final double t = integrator.integrate(new SimpleODE(), t0, y0, t1, y1);
        Assert.assertEquals(t1, t, 1.0e-12);
        Assert.assertEquals(exp, y1[0], 1.0e-6);
        Assert.assertEquals(exp * Math.sin(Math.PI), y1[1], 1.0e-6);
    }

    /**
     * Test error handling.
     * @throws DerivativeException if failed
     * @throws IntegratorException if failed
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testErrorHandling() throws DerivativeException, IntegratorException {
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] evaluateDerivative(double t, double[] y) {
                throw new UnsupportedOperationException();
            }
        };
        integrator.integrate(equations, t0, y0, t1, y1);
    }

    /**
     * Test handling of events.
     * @throws DerivativeException if failed
     * @throws IntegratorException if failed
     */
    @Test
    public void testHandlingOfEvents() throws DerivativeException, IntegratorException {
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] evaluateDerivative(double t, double[] y) {
                return new double[] { Math.sin(y[0]) };
            }

            @Override
            public double[] getInitialValue() {
                return new double[] { Math.PI / 2.0 };
            }
        };
        final StepHandler handler = new StepHandler() {
            private int count = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                final StepState state = interpolator.getStepState();
                if (state.isEventOccured()) {
                    final StepEvent event = state.getEvent();
                    if (event instanceof ReligiousHoliday) {
                        ++count;
                    }
                }
            }
        };
        integrator.addStepHandler(handler);
        integrator.setEventsManager(new CombinedEventsManager());
        integrator.addEndTimeChecker(t1, new ReligiousHoliday("Easter", t1 - 10.0));
        integrator.integrate(equations, t0, y0, t1, y1);
        Assert.assertEquals(1, count);
    }

    /**
     * Test endless loop.
     * @throws DerivativeException if failed
     * @throws IntegratorException if failed
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEndlessLoop() throws DerivativeException, IntegratorException {
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] evaluateDerivative(double t, double[] y) {
                return new double[] { y[0] - 1.0 };
            }

            @Override
            public double[] getInitialValue() {
                return new double[] { 1.0 };
            }
        };
        integrator.integrate(equations, t0, y0, t1, y1);
    }

}

class SimpleODE implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public double[] evaluateDerivative(double t, double[] y) {
        final double x = y[0];
        final double yp = y[1];
        return new double[] { yp, x * yp - 1.0 };
    }

}