package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ForcedEvent;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * This class tests {@link RungeKuttaIntegrator} with various test cases.
 */
public class RungeKuttaIntegratorTest extends TestCase {

    /**
     * Test the integration of a simple system with known solution.
     * @throws Exception if an error occurs
     */
    public void testSimpleSystem() throws Exception {
        final double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.0, 0.5}, {0.0, 0.5, 0.0}};
        final double[] b = {0.5, 0.0, 0.5};
        final double[] c = {0.0, 0.5, 1.0};
        final double step = 0.1;

        final RungeKuttaIntegrator rk = new MidpointIntegrator(
                "Midpoint Integrator", c, a, b, new MidpointStepInterpolator(), step);
        final FirstOrderDifferentialEquations equations = new DummyEquations();

        final double[] y0 = {0.0, 1.0};
        final double tStart = 0.0;
        final double tEnd = 2.0;
        final double[] yEnd = {1.0, 2.0};

        assertArrayEquals(yEnd, rk.integrate(equations, tStart, y0, tEnd, new double[yEnd.length]));
    }

    /**
     * Test the integration with an event.
     * @throws Exception if an error occurs
     */
    public void testEvent() throws Exception {
        final double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.0, 0.5}, {0.0, 0.5, 0.0}};
        final double[] b = {0.5, 0.0, 0.5};
        final double[] c = {0.0, 0.5, 1.0};
        final double step = 0.1;

        final RungeKuttaIntegrator rk = new MidpointIntegrator(
                "Midpoint Integrator", c, a, b, new MidpointStepInterpolator(), step);
        final FirstOrderDifferentialEquations equations = new SimpleDampedOscillator();

        final double[] y0 = {1.0, 1.0};
        final double tStart = 0.0;
        final double tEnd = 2.0;

        // Add an event at t = 1.5
        rk.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                if (step > 0) {
                    final double t = event.getCurrentTime();
                    if (Math.abs(t - 1.5) < 1e-12) {
                        addEvent(t, new ForcedEvent());
                    }
                }
            }
        });

        assertArrayEquals(new double[] {1.0, 0.0}, rk.integrate(equations, tStart, y0, tEnd, new double[2]));
    }

    /**
     * Test the integration with an event and a relative tolerance.
     * @throws Exception if an error occurs
     */
    public void testEventWithRelativeTolerance() throws Exception {
        final double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.0, 0.5}, {0.0, 0.5, 0.0}};
        final double[] b = {0.5, 0.0, 0.5};
        final double[] c = {0.0, 0.5, 1.0};
        final double step = 0.1;

        final RungeKuttaIntegrator rk = new MidpointIntegrator(
                "Midpoint Integrator", c, a, b, new MidpointStepInterpolator(), step);
        final FirstOrderDifferentialEquations equations = new SimpleDampedOscillator();

        final double[] y0 = {1.0, 1.0};
        final double tStart = 0.0;
        final double tEnd = 2.0;
        final double relTolerance = 1e-6;

        // Add an event at t = 1.5
        rk.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                if (step > 0) {
                    final double t = event.getCurrentTime();
                    if (Math.abs(t - 1.5) < 1e-12) {
                        addEvent(t, new RelativisticEvent(1.5, 1.0));
                    }
                }
            }
        });

        assertArrayEquals(new double[] {1.0, 0.1}, rk.integrate(equations, tStart, y0, tEnd, new double[2], relTolerance));
    }

    /**
     * Test the integration with a singular Jacobian.
     * @throws Exception if an error occurs
     */
    public void testSingularJacobian() throws Exception {
        final double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.0, 0.5}, {0.0, 0.5, 0.0}};
        final double[] b = {0.5, 0.0, 0.5};
        final double[] c = {0.0, 0.5, 1.0};
        final double step = 0.1;

        final RungeKuttaIntegrator rk = new MidpointIntegrator(
                "Midpoint Integrator", c, a, b, new MidpointStepInterpolator(), step);
        final FirstOrderDifferentialEquations equations = new SingularJacobian();

        final double[] y0 = {1.0, 1.0};
        final double tStart = 0.0;
        final double tEnd = 2.0;

        try {
            rk.integrate(equations, tStart, y0, tEnd, new double[2]);
            fail("An IntegratorException should have been thrown.");
        } catch (IntegratorException e) {
            // Expected
        }
    }

    /**
     * Test the integration with an invalid step size.
     * @throws Exception if an error occurs
     */
    public void testInvalidStepSize() throws Exception {
        final double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.0, 0.5}, {0.0, 0.5, 0.0}};
        final double[] b = {0.5, 0.0, 0.5};
        final double[] c = {0.0, 0.5, 1.0};
        final double step = 0.0;

        final RungeKuttaIntegrator rk = new MidpointIntegrator(
                "Midpoint Integrator", c, a, b, new MidpointStepInterpolator(), step);
        final FirstOrderDifferentialEquations equations = new DummyEquations();

        try {
            rk.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Helper class for a simple differential equation.
     */
    private static class DummyEquations implements FirstOrderDifferentialEquations {

        @Override
        public void derive(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }
    }

    /**
     * Helper class for a simple damped oscillator.
     */
    private static class SimpleDampedOscillator implements FirstOrderDifferentialEquations {

        @Override
        public void derive(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0] - 0.2 * y[1];
        }
    }

    /**
     * Helper class for a differential equation with a singular Jacobian.
     */
    private static class SingularJacobian implements FirstOrderDifferentialEquations {

        @Override
        public void derive(double t, double[] y, double[] yPrime) {
            yPrime[0] = 1.0 / (y[0] * y[0]);
            yPrime[1] = y[1];
        }
    }

    /**
     * Adds an event to the {@link RungeKuttaIntegrator}.
     * @param time the time where the event occurs
     * @param event the event to add
     */
    private void addEvent(double time, Event event) {
        ((CombinedEventsManager) rk.getEventsManager()).addRelativisticEvent(time, event);
    }

}