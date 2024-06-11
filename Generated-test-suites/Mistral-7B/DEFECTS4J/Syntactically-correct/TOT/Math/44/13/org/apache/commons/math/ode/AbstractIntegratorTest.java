package org.apache.commons.math.ode.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.SimpleEventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;

/**
 * Tests for the {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest extends TestCase {

    /**
     * Test the {@link AbstractIntegrator} class with a simple set of differential equations.
     */
    public void testSimpleIntegration() {
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t0 = 0.0;
        final double t1 = 5.0;
        final double[] y1 = new double[] { 3.0, 4.0 };

        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        AbstractIntegrator integrator = new EulerIntegrator(); // or any other integrator
        integrator.setEquations(equations);

        try {
            double t = integrator.integrate(equations, t0, y0, t1, y1);
            assertEquals(t1, t, 1e-5);
            assertArrayEquals(y1, y1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test the {@link AbstractIntegrator} class with a simple set of differential equations and an event handler.
     */
    public void testSimpleIntegrationWithEventHandler() {
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t0 = 0.0;
        final double t1 = 5.0;
        final double[] y1 = new double[] { 3.0, 4.0 };

        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        AbstractIntegrator integrator = new EulerIntegrator(); // or any other integrator

        integrator.addEventHandler(new SimpleEventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                assertEquals(1.0, state[0], 1e-5);
                assertEquals(2.0, state[1], 1e-5);
            }
        }, 0.5, 1e-5, 5);

        integrator.setEquations(equations);

        try {
            double t = integrator.integrate(equations, t0, y0, t1, y1);
            assertEquals(t1, t, 1e-5);
            assertArrayEquals(y1, y1);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    /**
     * Test the {@link AbstractIntegrator} class with a simple set of differential equations, an event handler, and a step interpolator.
     */
    public void testSimpleIntegrationWithEventHandlerAndInterpolator() {
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t0 = 0.0;
        final double t1 = 5.0;
        final double[] y1 = new double[] { 3.0, 4.0 };

        final FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        AbstractIntegrator integrator = new EulerIntegrator(); // or any other integrator

        integrator.addEventHandler(new SimpleEventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                assertEquals(1.0, state[0], 1e-5);
                assertEquals(2.0, state[1], 1e-5);
            }
        }, 0.5, 1e-5, 5);

        integrator.setEquations(equations);

        // setup a StepInterpolator for testing
        AbstractStepInterpolator interpolator = new StepInterpolator();
        integrator.setStepInterpolator(interpolator);

        try {
            double t = integrator.integrate(equations, t0, y0, t1, y1);
            assertEquals(t1, t, 1e-5);
            assertArrayEquals(y1, y1);

            int numSteps = interpolator.getNumberOfSteps();
            for (int i = 0; i < numSteps; i++) {
                double tStep = interpolator.getStep(i);
                double[] state = interpolator.getState(i);
                assertEquals(1.0 + i / (numSteps + 1.0), state[0], 1e-5);
                assertEquals(2.0 + i / (numSteps + 1.0), state[1], 1e-5);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    /**
     * Test the {@link AbstractIntegrator} class with a set of differential equations that causes an exception.
     */
    public void testExceptionThrowingIntegration() {
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t0 = 0.0;
        final double[] y1 = new double[] { 3.0, 4.0 };

        final FirstOrderDifferentialEquations equations = new ExceptionThrowingDifferentialEquations();
        AbstractIntegrator integrator = new EulerIntegrator(); // or any other integrator
        integrator.setEquations(equations);

        try {
            integrator.integrate(equations, t0, y0, 1.0, y1);
            fail("An exception should have been thrown.");
        } catch (Exception e) {
            // expected
        }

    }

    /**
     * Test the {@link AbstractIntegrator} class with a set of differential equations that causes an exception and an event handler.
     */
    public void testExceptionThrowingIntegrationWithEventHandler() {
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = new double[] { 3.0, 4.0 };

        final FirstOrderDifferentialEquations equations = new ExceptionThrowingDifferentialEquations();
        AbstractIntegrator integrator = new EulerIntegrator(); // or any other integrator

        integrator.addEventHandler(new SimpleEventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                fail("An event handler should not be called.");
            }
        }, 0.0, 1e-5, 1);

        integrator.setEquations(equations);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            fail("An exception should have been thrown.");
        } catch (Exception e) {
            // expected
        }

    }

    /**
     * Test the {@link AbstractIntegrator} class with a set of differential equations that causes an exception, an event handler, and a step interpolator.
     */
    public void testExceptionThrowingIntegrationWithEventHandlerAndInterpolator() {
        final double[] y0 = new double[] { 1.0, 2.0 };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = new double[] { 3.0, 4.0 };

        final FirstOrderDifferentialEquations equations = new ExceptionThrowingDifferentialEquations();
        AbstractIntegrator integrator = new EulerIntegrator(); // or any other integrator

        integrator.addEventHandler(new SimpleEventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                fail("An event handler should not be called.");
            }
        }, 0.0, 1e-5, 1);

        integrator.setEquations(equations);

        // setup a StepInterpolator for testing
        AbstractStepInterpolator interpolator = new StepInterpolator();
        integrator.setStepInterpolator(interpolator);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            fail("An exception should have been thrown.");
        } catch (Exception e) {
            // expected
        }

    }

}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -0.1 * y[1];
    }

}

class ExceptionThrowingDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        throw new RuntimeException("An exception.");
    }

}

class StepInterpolator extends AbstractStepInterpolator {

    @Override
    public int getNumberOfSteps() {
        return 10;
    }

}