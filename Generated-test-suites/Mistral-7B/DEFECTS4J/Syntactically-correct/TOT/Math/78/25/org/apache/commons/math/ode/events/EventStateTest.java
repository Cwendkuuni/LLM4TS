package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.step.StepInterpolator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the {@link EventState} class.
 */
public class EventStateTest {

    /** The event handler used to create the {@link EventState} instances. */
    private static final EventHandler DUMMY_EVENT_HANDLER = new EventHandler() {
        @Override
        public double g(double t, double[] y) {
            return Math.sin(t);
        }

        @Override
        public void resetState(double t, double[] y) {
            // no-op
        }

        @Override
        public int eventOccurred(double t, double[] y, boolean crossing) {
            return EventHandler.CONTINUE;
        }
    };

    /** The step interpolator used to perform the integration steps. */
    private StepInterpolator interpolator;

    /** The event state used for testing. */
    private EventState state;

    /** The expected occurrence time of the first event. */
    private static final double FIRST_EVENT_TIME = Math.PI;

    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() {
        interpolator = new StepInterpolator(new RKF45Integrator());
        state = new EventState(DUMMY_EVENT_HANDLER, 0.01, 1e-9, 20);
    }

    /**
     * Tear down the test environment.
     */
    @After
    public void tearDown() {
        state = null;
        interpolator = null;
    }

    /**
     * Test a simple integration step with no event.
     */
    @Test
    public void testNoEvent() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(0.0, y);
        state.evaluateStep(interpolator);
        assertEquals(0.0, state.getEventTime(), 1e-9);
        assertFalse(state.stop());
        assertFalse(state.reset(0.1, y));
        assertEquals(1.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
    }

    /**
     * Test an integration step with an event at the beginning of the step.
     */
    @Test
    public void testEventAtBeginning() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME, y);
        state.evaluateStep(interpolator);
        assertTrue(state.stop());
        assertEquals(FIRST_EVENT_TIME, state.getEventTime(), 1e-9);
        assertFalse(state.reset(FIRST_EVENT_TIME + 1e-9, y));
        assertEquals(0.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
    }

    /**
     * Test an integration step with an event in the middle of the step.
     */
    @Test
    public void testEventInMiddle() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME + 1e-9, y);
        state.evaluateStep(interpolator);
        assertTrue(state.stop());
        assertEquals(FIRST_EVENT_TIME + 1e-9, state.getEventTime(), 1e-9);
        assertFalse(state.reset(FIRST_EVENT_TIME, y));
        assertEquals(0.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
    }

    /**
     * Test an integration step with an event at the end of the step.
     */
    @Test
    public void testEventAtEnd() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME + 1e-9, y);
        state.evaluateStep(interpolator);
        state.evaluateStep(interpolator);
        assertFalse(state.stop());
        assertEquals(Double.NaN, state.getEventTime(), 1e-9);
        assertFalse(state.reset(FIRST_EVENT_TIME + 2e-9, y));
        assertEquals(1.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
    }

    /**
     * Test an integration step with an event that overlaps the step.
     */
    @Test
    public void testEventOverlap() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME - 1e-9, y);
        state.evaluateStep(interpolator);
        assertTrue(state.stop());
        assertEquals(FIRST_EVENT_TIME - 1e-9, state.getEventTime(), 1e-9);
        assertFalse(state.reset(FIRST_EVENT_TIME, y));
        assertEquals(0.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
    }

    /**
     * Test an integration step with an event that occurs before the step.
     */
    @Test(expected = ConvergenceException.class)
    public void testEventBefore() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME - 2e-9, y);
        state.evaluateStep(interpolator);
        assertTrue(state.stop());
        assertEquals(FIRST_EVENT_TIME - 2e-9, state.getEventTime(), 1e-9);
        assertFalse(state.reset(FIRST_EVENT_TIME, y));
        assertEquals(0.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
    }

    /**
     * Test an integration step with a reset state.
     */
    @Test
    public void testResetState() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME, y);
        state.evaluateStep(interpolator);
        state.reset(FIRST_EVENT_TIME, y);
        assertEquals(0.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
        assertFalse(state.stop());
        assertFalse(state.reset(FIRST_EVENT_TIME + 1e-9, y));
    }

    /**
     * Test an integration step with a reset derivatives.
     */
    @Test
    public void testResetDerivatives() {
        double[] y = new double[] { 0.0, 1.0 };

        state.reinitializeBegin(FIRST_EVENT_TIME, y);
        state.evaluateStep(interpolator);
        state.reset(FIRST_EVENT_TIME, y);
        state.reset(FIRST_EVENT_TIME, new double[2]);
        assertEquals(0.0, state.getEventHandler().g(state.getT(), state.getY()), 1e-9);
        assertFalse(state.stop());
        assertFalse(state.reset(FIRST_EVENT_TIME + 1e-9, y));
    }

}