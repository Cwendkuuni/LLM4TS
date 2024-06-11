package org.apache.commons.math.ode.nonstiff;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousResponseEvent;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.RungeKuttaStepInterpolator;

/**
 * Tests for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test the Embedded Runge-Kutta method for the simple equation: dy/dt = y.
     */
    public void testSimpleEquation() {
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        double y0 = 1.0;
        final double[] yValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        final double tStart = 0.0;
        final double tEnd = 1.0;
        double t;
        double[] y;

        // integrate the equation
        y = new double[yValues.length];
        t = rk4.integrate(new SimpleEquation(), tStart, new double[] { y0 }, tEnd, y);
        assertEquals("The final time is not correct.", tEnd, t);

        // check the results
        for (int i = 0; i < yValues.length; ++i) {
            assertEquals("The result for time [" + tStart + ", " + (tStart + i * (tEnd - tStart) / (yValues.length - 1)) + "] is not correct.", yValues[i], y[i]);
        }
    }

    /**
     * Test the Embedded Runge-Kutta method for the equation with a continuous response event.
     */
    public void testEvent() {
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        double y0 = 0.0;
        final double tStart = 0.0;
        final double tEnd = 2.0;
        double t;
        double[] y;

        final ContinuousResponseEvent event = new ContinuousResponseEvent() {
            @Override
            public double getResponse(double t, double y, double[] yDot) {
                return Math.sin(y);
            }

            @Override
            public double getEventTime() {
                return Math.PI;
            }
        };

        final RungeKuttaStepInterpolator interpolator = new RungeKuttaStepInterpolator(rk4);
        final double[] yDotInitialConditions = new double[1];
        interpolator.reinitialize(rk4, yDotInitialConditions, new double[1][], false);

        // integrate the equation
        y = new double[2];
        t = rk4.integrate(new SinEventEquation(event), tStart, new double[] { y0 }, tEnd, y, interpolator);

        // check the results
        assertEquals("The final time is not correct.", tEnd, t);
        assertEquals("The result for time [" + tStart + "] is not correct.", y0, y[0]);
        assertEquals("The result for time [" + t + "] is not correct.", Math.sin(Math.PI), y[1]);
    }

    /**
     * Test the Embedded Runge-Kutta method for the equation with an event at the initial time.
     */
    public void testEventAtInitialTime() {
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        double y0 = 0.0;
        final double tStart = Math.PI;
        final double tEnd = 2.0;
        double t;
        double[] y;

        final ContinuousResponseEvent event = new ContinuousResponseEvent() {
            @Override
            public double getResponse(double t, double y, double[] yDot) {
                return Math.sin(y);
            }

            @Override
            public double getEventTime() {
                return Math.PI;
            }
        };

        final RungeKuttaStepInterpolator interpolator = new RungeKuttaStepInterpolator(rk4);
        final double[] yDotInitialConditions = new double[1];
        interpolator.reinitialize(rk4, yDotInitialConditions, new double[1][], false);

        // integrate the equation
        y = new double[2];
        t = rk4.integrate(new SinEventEquation(event), tStart, new double[] { y0 }, tEnd, y, interpolator);

        // check the results
        assertEquals("The final time is not correct.", tEnd, t);
        assertEquals("The result for time [" + tStart + "] is not correct.", Math.sin(Math.PI), y[0]);
        assertEquals("The result for time [" + t + "] is not correct.", Math.cos(Math.PI), y[1]);
    }

    /**
     * Test the Embedded Runge-Kutta method for the equation with a step handler.
     */
    public void testStepHandler() {
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        double y0 = 0.0;
        final double tStart = 0.0;
        final double tEnd = 2.0;
        double t;
        double[] y;

        final StepHandler stepHandler = new StepHandler() {
            private int counter = 0;

            @Override
            public void handleStep(StepHandler handler, int step) {
                if (step > 10) {
                    // stop the integration after 11 steps
                    handler.stopIntegration();
                } else {
                    // print the step number
                    System.out.println("Step " + (step + 1));
                }
            }
        };

        // integrate the equation
        y = new double[1];
        t = rk4.integrate(new SimpleEquation(), tStart, new double[] { y0 }, tEnd, y, null, stepHandler);

        // check the results
        assertEquals("The final time is not correct.", tEnd, t);
        assertEquals("The result for time [" + tStart + "] is not correct.", y0, y[0]);
        // the integration should have been stopped after 11 steps
        // so there should be no result for t
        assertNull("The result for time [" + t + "] is not null.", y[0]);
    }

    /**
     * Test the Embedded Runge-Kutta method for the equation with a step handler that changes the step size.
     */
    public void testStepHandlerThatChangesStepSize() {
        final EmbeddedRungeKuttaIntegrator rk4 = new RK4Method();
        double y0 = 0.0;
        final double tStart = 0.0;
        final double tEnd = 2.0;
        double t;
        double[] y;

        final StepHandler stepHandler = new StepHandler() {
            private double stepSize = 0.2;

            @Override
            public void handleStep(StepHandler handler, int step) {
                if (step > 5) {
                    // decrease the step size after 6 steps
                    handler.setStepSize(stepSize / 2.0);
                }
                // print the step number and the current step size
                System.out.println("Step " + (step + 1) + " with step size " + stepSize);
            }
        };

        // integrate the equation
        y = new double[1];
        t = rk4.integrate(new SimpleEquation(), tStart, new double[] { y0 }, tEnd, y, null, stepHandler);

        // check the results
        assertEquals("The final time is not correct.", tEnd, t);
        assertEquals("The result for time [" + tStart + "] is not correct.", y0, y[0]);
        // the integration should have been stopped after 12 steps
        // so there should be no result for t
        assertNull("The result for time [" + t + "] is not null.", y[0]);
        // check that the step sizes have been correctly decreased
        for (int i = 1; i < y.length; ++i) {
            double dt = tEnd - tStart;
            double expectedStepSize = dt / Math.max((tEnd - tStart) / (y.length - 1), 1.0);
            assertEquals("The step size for time [" + (tStart + i * (tEnd - tStart) / (y.length - 1)) + "] is not correct.", expectedStepSize, y[i] / (i + 1));
        }
    }

    /**
     * A simple differential equation: dy/dt = y.
     */
    static class SimpleEquation implements FirstOrderDifferentialEquations {
        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0];
        }
    }

    /**
     * A simple differential equation: dy/dt = y, with a continuous response event at PI.
     */
    static class SinEventEquation extends SimpleEquation {
        private final ContinuousResponseEvent event;

        SinEventEquation(ContinuousResponseEvent event) {
            this.event = event;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            super.computeDerivatives(t, y, yPrime);
            final double response = event.getResponse(t, y[0], yPrime);
            yPrime[0] += response;
        }
    }

}