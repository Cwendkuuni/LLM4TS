package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKutta54;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.stephandler.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** The integrator to test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** The differential equations to integrate. */
    private static final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = Math.sin(y[0]);
        }

    };

    /** The step handler to test. */
    private StepHandler stepHandler;

    /** The event to trigger. */
    private Event event;

    /** The interpolator to use. */
    private StepInterpolator interpolator;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        integrator = new RungeKutta54();
        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);
        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);
        integrator.setEquations(equations);

        interpolator = integrator.getPrototype().copy();
        stepHandler = new TestStepHandler();
        integrator.setStepHandler(stepHandler);
        event = new When(0.5);
    }

    /**
     * Test a simple integration without events.
     */
    @Test
    public void testSimpleIntegration() {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double t = 1.0;
        double[] y = integrator.integrate(equations, t0, y0, t);
        assertEquals(Math.sin(0.5), y[0], 1e-10);
    }

    /**
     * Test an integration with an event.
     */
    @Test
    public void testIntegrationWithEvent() {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double t = 2.0;
        integrator.addEvent(event);
        double[] y = integrator.integrate(equations, t0, y0, t);
        assertTrue(stepHandler.isEventTriggered());
        assertEquals(1, y.length);
        assertEquals(1, y[0]);
    }

    /**
     * Test an integration with an event that is not reached.
     */
    @Test
    public void testIntegrationWithUnreachableEvent() {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double t = 0.5;
        integrator.addEvent(event);
        double[] y = integrator.integrate(equations, t0, y0, t);
        assertFalse(stepHandler.isEventTriggered());
        assertEquals(1, y.length);
        assertEquals(0.0, y[0], 1e-10);
    }

    /**
     * A test step handler.
     */
    private static class TestStepHandler implements StepHandler {

        /** Whether an event has been triggered. */
        private boolean eventTriggered = false;

        /**
         * Reset the step handler.
         */
        public void reset() {
            eventTriggered = false;
        }

        /**
         * Handle a step.
         *
         * @param interpolator the interpolator
         * @param lastStep the last step
         */
        public void handleStep(StepInterpolator interpolator, boolean lastStep) {
            eventTriggered = interpolator.isAtEvent();
        }

        /**
         * Get whether an event has been triggered.
         *
         * @return true if an event has been triggered, false otherwise
         */
        public boolean isEventTriggered() {
            return eventTriggered;
        }

    }

}