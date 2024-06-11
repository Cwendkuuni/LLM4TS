package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.utils.ODETestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Test equation. */
    private static final FirstOrderDifferentialEquations equation = new FirstOrderDifferentialEquations() {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double time, double[] y, double[] yPrime) {
            yPrime[0] = Math.exp(y[0]);
        }
    };

    /** Equations manager. */
    private FirstOrderDifferentialEquations equations;

    /** Integrator. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** Equations manager for test with absolute and relative tolerance. */
    private FirstOrderDifferentialEquations toleranceEquations;

    /** Step interpolator. */
    private StepInterpolator interpolator;

    /** Step handler. */
    private StepHandler stepHandler;

    /** Event handler. */
    private Event event;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        equations = equation;
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.5, 0.5 },
                                                      new double[][] { { 0.25, 0 }, { 0, 0.25 }, { 0.25, 0.25 },
                                                                          { 0, 0.5 } }, new double[] { 0.25, 0.25, 0.25, 0.25 },
                                                      new RungeKuttaStepInterpolator(), 1e-12, 1e-8) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / Math.max(Math.abs(y1[0]), 1.0);
            }
        };
        toleranceEquations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]);
            }
        };

        interpolator = integrator.getStepInterpolator();
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean last) {
                assertTrue(interpolator instanceof RungeKuttaStepInterpolator);
                assertEquals(integrator.getStep(), interpolator.getStep(), 1e-13);
                assertTrue(last);
            }
        };
        event = new When(1.0);
    }

    /**
     * Test integration.
     */
    @Test
    public void testIntegration() throws Exception {
        double[] y0 = { 1.0 };
        double[] y = new double[y0.length];
        double t = 2.0;

        // test integration
        double result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1e-11);
        assertArrayEquals(y, new double[] { 2.718281828459045 }, 6);

        // test integration with absolute and relative tolerance
        toleranceEquations.setAbsoluteTolerance(1e-15);
        toleranceEquations.setRelativeTolerance(1e-13);
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.5, 0.5 },
                                                      new double[][] { { 0.25, 0 }, { 0, 0.25 }, { 0.25, 0.25 },
                                                                          { 0, 0.5 } }, new double[] { 0.25, 0.25, 0.25, 0.25 },
                                                      new RungeKuttaStepInterpolator(), 1e-15, 1e-13,
                                                      toleranceEquations.getAbsoluteTolerance(),
                                                      toleranceEquations.getRelativeTolerance()) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }
        };
        result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1e-15);
        assertArrayEquals(y, new double[] { 2.718281828459045 }, 6);
    }

    /**
     * Test step size control.
     */
    @Test
    public void testStepSizeControl() throws Exception {

        // test step size control with no events
        double[] y0 = { 1.0 };
        double t = 2.0;
        double step = 0.1;
        double[] y = new double[y0.length];

        integrator.setStep(step);
        integrator.setMinStep(step / 10);
        integrator.setMaxStep(step * 10);

        // test integration
        double result = integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(t, result, 1e-11);
        assertArrayEquals(y, new double[] { 2.718281828459045 }, 6);

        // test that stepsize is accepted
        assertEquals(step, integrator.getStep(), 1e-13);

        // test that stepsize is not reduced
        integrator.resetStep();
        integrator.setStep(3.0 * step);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step, integrator.getStep(), 1e-13);

        // test that stepsize is increased
        integrator.resetStep();
        integrator.setStep(step / 5);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step, integrator.getStep() * 4.0, 1e-13);

        // test that stepsize is reduced
        integrator.resetStep();
        double oldStep = integrator.getStep();
        integrator.setStep(step / 2.0);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(oldStep / 2.0, integrator.getStep(), 1e-13);

        // test that stepsize is increased again
        integrator.resetStep();
        integrator.setStep(oldStep);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(oldStep, integrator.getStep(), 1e-13);

        // test that stepsize is reduced again
        integrator.resetStep();
        integrator.setStep(oldStep / 4.0);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(oldStep / 8.0, integrator.getStep(), 1e-13);

        // test that stepsize reaches the minimum step
        integrator.resetStep();
        integrator.setStep(step / 100);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step / 100, integrator.getStep(), 1e-13);

        // test that stepsize reaches the maximum step
        integrator.resetStep();
        integrator.setStep(step * 10);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step * 10, integrator.getStep(), 1e-13);

        // test that stepsize is reduced when a step is rejected
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        integrator.setSafety(0.9);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step, integrator.getStep(), 1e-13);
        integrator.resetStep();
        integrator.setStep(step / 2.0);
        integrator.integrate(equations, 0.0, y0, t, y);
        step *= 2.0;
        assertEquals(step / 2.0, integrator.getStep(), 1e-13);

        // test that stepsize is increased when a step is accepted
        integrator.resetStep();
        integrator.setStep(step / 4.0);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step / 2.0, integrator.getStep(), 1e-13);

        // test that stepsize is increased when a step is accepted with a large error
        integrator.resetStep();
        integrator.setStep(step / 100);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step / 10.0, integrator.getStep(), 1e-13);

        // test that maximum growth factor is applied
        integrator.resetStep();
        integrator.setStep(step / 100);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step * 10.0, integrator.getStep(), 1e-13);

        // test that minimum reduction factor is applied
        integrator.resetStep();
        integrator.setStep(step);
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(step * 0.1, integrator.getStep(), 1e-13);

    }

    /**
     * Test events.
     */
    @Test
    public void testEvents() throws Exception {

        // test an event at the start
        double[] y0 = { 1.0 };
        double t = 1.5;
        double[] y = new double[y0.length];
        integrator.addStepHandler(stepHandler);
        integrator.addEventsManager(new CombinedEventsManager());
        integrator.setEvents(event);

        integrator.integrate(equations, 0.0, y0, t, y);
        assertTrue(interpolator.getStepCount() > 1);
        assertFalse(event.isAccepted());

        // test an event at the end
        integrator.reset();
        integrator.addStepHandler(stepHandler);
        integrator.addEventsManager(new CombinedEventsManager());
        integrator.setEvents(new When(t));

        integrator.integrate(equations, 0.0, y0, t, y);
        assertTrue(interpolator.getStepCount() > 1);
        assertTrue(event.isAccepted());

        // test an event in the middle
        integrator.reset();
        integrator.addStepHandler(stepHandler);
        integrator.addEventsManager(new CombinedEventsManager());
        integrator.setEvents(new When(0.5));

        integrator.integrate(equations, 0.0, y0, t, y);
        assertTrue(interpolator.getStepCount() > 1);
        assertTrue(interpolator.getStepIndex(1) > 0);
        assertTrue(event.isAccepted());
    }

}