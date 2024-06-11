package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;

/**
 * Test cases for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends TestCase {

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrate() {
        final double step = 0.01;
        final double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47},
            {0.4666666666666666, 0.0},
            {0.3333333333333333, 0.4}
        };
        final double[] weights = {0.25, 0.6, 0.3, 1.0};
        final RungeKuttaIntegrator rk4 = new RungeKutta4Integrator("RK4", coefficients, weights, (n, y) -> new double[] {2.0 * n, 2.0 * y[0]});

        final double initialTime = 0.0;
        final double[] initialState = {0.0};
        final double finalTime = 1.0;
        final double[] expectedFinalState = {1.0};

        final RelativeEvent event = new RelativeEvent(1.0, 0.9999);
        final StepHandler stepHandler = (interpolator, lastStep) -> {
            if (lastStep) {
                throw new AssertionError("Unexpected last step");
            }
            if (event != null && interpolator.getTime() >= event.getTime()) {
                event.handle(interpolator);
            }
        };

        try {
            final double result = rk4.integrate(new MyDifferentialEquations(), initialTime, initialState, finalTime, new double[initialState.length]);
            assertArrayEquals("Unexpected final state", expectedFinalState, new double[] {result[0], result[1]});
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        rk4.addStepHandler(stepHandler);
        rk4.setEventsHandlerManager(new CombinedEventsManager());
        rk4.integrate(new MyDifferentialEquations(), initialTime, initialState, finalTime, new double[initialState.length]);
        assertTrue("Expected at least one event to be triggered", stepHandler.isTriggered());
    }

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * When step size is too large, an exception should be thrown.
     */
    public void testIntegrateTooLargeStep() {
        final double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47},
            {0.4666666666666666, 0.0},
            {0.3333333333333333, 0.4}
        };
        final double[] weights = {0.25, 0.6, 0.3, 1.0};
        final RungeKuttaIntegrator rk4 = new RungeKutta4Integrator("RK4", coefficients, weights, (n, y) -> new double[] {2.0 * n, 2.0 * y[0]});

        final double initialTime = 0.0;
        final double[] initialState = {0.0};
        final double finalTime = 1.0;
        final double[] expectedFinalState = {1.0};
        final double tooLargeStep = 1.1;

        try {
            rk4.integrate(new MyDifferentialEquations(), initialTime, initialState, finalTime, new double[initialState.length], tooLargeStep);
            fail("Expected IntegratorException with message 'Step size is too large'");
        } catch (IntegratorException e) {
            assertEquals("Step size is too large", e.getMessage());
        }
    }

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], double[])}.
     * When derivative function throws exception, an exception should be thrown with the same message.
     */
    public void testIntegrateDerivativeException() {
        final double[][] coefficients = {
            {0.0, 0.0},
            {0.3, 0.47},
            {0.4666666666666666, 0.0},
            {0.3333333333333333, 0.4}
        };
        final double[] weights = {0.25, 0.6, 0.3, 1.0};
        final RungeKuttaIntegrator rk4 = new RungeKutta4Integrator("RK4", coefficients, weights, (n, y) -> {
            throw new MathIllegalArgumentException("Test exception");
        });

        final double initialTime = 0.0;
        final double[] initialState = {0.0};
        final double finalTime = 1.0;
        final double[] expectedFinalState = {1.0};

        try {
            rk4.integrate(new MyDifferentialEquations(), initialTime, initialState, finalTime, new double[initialState.length]);
            fail("Expected MathIllegalArgumentException with message 'Test exception'");
        } catch (IntegratorException e) {
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

    /**
     * Test differential equations.
     */
    static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = 2.0 * y[0];
            yPrime[1] = 2.0 * y[1];
        }
    }

}