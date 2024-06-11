package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethods;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test a simple ODE with a fixed step size.
     * @throws Exception if an error occurs
     */
    public void testFixedStep() throws Exception {
        final double[] initialCondition = {0.0, 1.0};
        final double startTime = 0.0;
        final double endTime = 2.0;
        final double stepSize = 0.1;

        final String methodName = "CashKarp";
        final EmbeddedRungeKuttaIntegrator integrator =
            new RungeKutta54Integrator(methodName, true,
                                      new double[] {0.23, 0.382, 0.5, 0.667, 0.846, 1.0},
                                      new double[][] {
                                          {0.0, 0.23, 0.676, 0.0, 0.23, 0.676},
                                          {0.0, 0.382, 0.618, 0.382, 0.554, 0.932},
                                          {0.0, 0.5, 0.46864, 0.46864, 0.568, 0.9846},
                                          {0.0, 0.667, 0.32212, 0.32212, 0.37413, 0.9962},
                                          {0.0, 0.846, 0.2, 0.2, 0.2865, 0.9994},
                                          {1.0, 0.0, 0.0, 0.0, 0.0, 1.0}
                                      },
                                      new double[] {1.0, 0.0},
                                      0.01, 0.01,
                                      RungeKuttaStepInterpolator.class.newInstance());

        integrator.setMinStep(0.01);
        integrator.setMaxStep(10.0);
        integrator.initialize(new MyDifferentialEquations(), initialCondition, startTime);

        // integrate the ODE
        double[] y = integrator.integrate(startTime, endTime, initialCondition);
        assertArrayEquals("Integration failed", new double[] {2.71828, 1.71828}, y);

        // test the interpolator
        StepInterpolator interpolator = integrator.getInterpolator();
        double t = startTime;
        double[] yInterp = new double[initialCondition.length];
        for (int i = 0; i < initialCondition.length; ++i) {
            interpolator.interpolate(t, yInterp);
            assertEquals("Interpolation failed at time " + t,
                         initialCondition[i], yInterp[i]);
            t += stepSize;
        }

        // test the last step
        interpolator.interpolate(endTime, yInterp);
        assertArrayEquals("Interpolation failed at time " + endTime, y, yInterp);
    }

    /**
     * Test a simple ODE with an adaptive step size.
     * @throws Exception if an error occurs
     */
    public void testAdaptiveStep() throws Exception {
        final double[] initialCondition = {0.0, 1.0};
        final double startTime = 0.0;
        final double endTime = 2.0;

        final String methodName = "CashKarp
 et al.";
        final EmbeddedRungeKuttaIntegrator integrator =
            new RungeKutta54Integrator(methodName, true,
                                      new double[] {0.23, 0.382, 0.5, 0.667, 0.846, 1.0},
                                      new double[][] {
                                          {0.0, 0.23, 0.676, 0.0, 0.23, 0.676},
                                          {0.0, 0.382, 0.618, 0.382, 0.554, 0.932},
                                          {0.0, 0.5, 0.46864, 0.46864, 0.568, 0.9846},
                                          {0.0, 0.667, 0.32212, 0.32212, 0.37413, 0.9962},
                                          {0.0, 0.846, 0.2, 0.2, 0.2865, 0.9994},
                                          {1.0, 0.0, 0.0, 0.0, 0.0, 1.0}
                                      },
                                      new double[] {1.0, 0.0},
                                      0.01, 0.01,
                                      RungeKuttaStepInterpolator.class.newInstance());

        integrator.setMinStep(0.01);
        integrator.setMaxStep(10.0);
        integrator.initialize(new MyDifferentialEquations(), initialCondition, startTime);

        // integrate the ODE
        double[] y = integrator.integrate(startTime, endTime, initialCondition);
        assertArrayEquals("Integration failed", new double[] {2.71828, 1.71828}, y);

        // test the interpolator
        StepInterpolator interpolator = integrator.getInterpolator();
        double t = startTime;
        double[] yInterp = new double[initialCondition.length];
        for (int i = 0; i < initialCondition.length; ++i) {
            interpolator.interpolate(t, yInterp);
            assertEquals("Interpolation failed at time " + t,
                         initialCondition[i], yInterp[i]);
            t += 0.1;
        }

        // test the last step
        interpolator.interpolate(endTime, yInterp);
        assertArrayEquals("Interpolation failed at time " + endTime, y, yInterp);

        // test the error handling when the step is too large
        integrator.setMaxStep(100.0);
        try {
            integrator.integrate(startTime, endTime, initialCondition);
            fail("Expected an IntegratorException when the step was too large");
        } catch (IntegratorException e) {
            // expected
        }

        // test the error handling when the step is too small
        integrator.setMinStep(0.0001);
        try {
            integrator.integrate(startTime, endTime, initialCondition);
            fail("Expected an IntegratorException when the step was too small");
        } catch (IntegratorException e) {
            // expected
        }

        // test the error handling when the step is invalid
        integrator.setMinStep(-0.01);
        try {
            integrator.integrate(startTime, endTime, initialCondition);
            fail("Expected an IntegratorException when the step was negative");
        } catch (IntegratorException e) {
            // expected
        }

        // test the event handling
        final double eventTime = 1.0;
        final double eventValue = 2.0;
        integrator.addEvent(new When(eventTime, new MyEvent()));
        integrator.initialize(new MyDifferentialEquations(), initialCondition, startTime);
        integrator.integrate(startTime, endTime, initialCondition);
        assertTrue("No event detected", integrator.getEventIndex() == 0);
        assertEquals("The event value is incorrect", eventValue,
                     integrator.getEventValue(0));
    }

    /**
     * A simple differential equation.
     */
    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }

    }

    /**
     * A simple event.
     */
    private static class MyEvent implements Event {

        public boolean isRegularEvent() {
            return false;
        }

        public boolean isBranchPoint() {
            return false;
        }

        public double getValue(double t) {
            return 2.0;
        }

        public double getEventTime() {
            return 1.0;
        }

    }

    /**
     * Checks that two arrays are equal.
     * @param expected the expected array
     * @param actual the actual array
     * @param message a message to display in case of failure
     */
    private void assertArrayEquals(double[] expected, double[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            fail(message + ": expected [" + Arrays.toString(expected) + "], actual [" +
                 Arrays.toString(actual) + "]");
        }
    }

}