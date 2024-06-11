package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventHandler.EventAction;
import org.apache.commons.math3.ode.events.EventHandler.EventType;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link EventState}.
 */
public class EventStateTest {

    /** Test event handler. */
    private static class TestEventHandler implements EventHandler {
        private double prevValue = Double.NaN;

        /**
         * {@inheritDoc}
         */
        @Override
        public double g(double t, double[] y) {
            prevValue = y[0];
            return prevValue * (t - 1) + 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EventAction eventOccurred(double t, double[] y, boolean crossing) {
            assertEquals("t", 1.0, t, 1e-9);
            assertEquals("y[0]", prevValue + 1, y[0]);
            assertEquals("crossing", true, crossing);
            prevValue = y[0];
            return EventAction.RESET_DERIVATIVES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resetState(double t, double[] y) {
            prevValue = y[0];
        }
    }

    /** Test event state. */
    private EventState eventState;

    /** Test event handler. */
    private TestEventHandler eventHandler;

    /** Test step interpolator. */
    private StepInterpolator stepInterpolator;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        eventState = new EventState(eventHandler, 0.01, 1e-9, 10);
        eventHandler = new TestEventHandler();
        stepInterpolator = new StepInterpolator();
    }

    /**
     * Test method {@link EventState#reinitializeBegin(double, double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        double[] y = new double[1];
        eventState.reinitializeBegin(0.0, y);
        assertNull("g0", eventState.getEventTime());
        assertNull("pendingEvent", eventState.isPendingEvent());
        assertEquals("g0", 1.0, eventState.getEventHandler().g(eventState.getT0(), y), 1e-9);
    }

    /**
     * Test method {@link EventState#evaluateStep(StepInterpolator)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEvaluateStep() throws Exception {
        stepInterpolator.addStep(0.5, 0.5);
        double[] y = new double[1];
        eventState.reinitializeBegin(0.0, y);
        stepInterpolator.setCurrentTime(0.5);
        assertTrue("evaluateStep", eventState.evaluateStep(stepInterpolator));
        assertEquals("pendingEventTime", 0.5, eventState.getEventTime(), 1e-9);
        assertFalse("pendingEvent", eventState.isPendingEvent());
        assertEquals("g0", 0.5, eventState.getEventHandler().g(eventState.getT0(), y), 1e-9);
    }

    /**
     * Test method {@link EventState#getEventTime()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetEventTime() throws Exception {
        stepInterpolator.addStep(0.5, 0.5);
        double[] y = new double[1];
        eventState.reinitializeBegin(0.0, y);
        stepInterpolator.setCurrentTime(0.5);
        assertTrue("evaluateStep", eventState.evaluateStep(stepInterpolator));
        assertEquals("pendingEventTime", 0.5, eventState.getEventTime(), 1e-9);
        stepInterpolator.setCurrentTime(0.6);
        assertNull("pendingEventTime", eventState.getEventTime());
    }

    /**
     * Test method {@link EventState#stepAccepted(double, double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testStepAccepted() throws Exception {
        stepInterpolator.addStep(0.5, 0.5);
        double[] y = new double[1];
        eventState.reinitializeBegin(0.0, y);
        stepInterpolator.setCurrentTime(0.5);
        assertTrue("evaluateStep", eventState.evaluateStep(stepInterpolator));
        eventState.stepAccepted(0.5, y);
        assertEquals("t0", 0.5, eventState.getT0(), 1e-9);
        assertEquals("g0", 1.0, eventState.getEventHandler().g(eventState.getT0(), y), 1e-9);
        assertEquals("pendingEvent", false, eventState.isPendingEvent());
    }

    /**
     * Test method {@link EventState#reset(double, double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testReset() throws Exception {
        stepInterpolator.addStep(0.5, 0.5);
        double[] y = new double[1];
        eventState.reinitializeBegin(0.0, y);
        stepInterpolator.setCurrentTime(0.5);
        assertTrue("evaluateStep", eventState.evaluateStep(stepInterpolator));
        eventState.stepAccepted(0.5, y);
        eventState.reset(0.6, y);
        assertEquals("t0", 0.6, eventState.getT0(), 1e-9);
        assertEquals("g0", 1.1, eventState.getEventHandler().g(eventState.getT0(), y), 1e-9);
        assertFalse("pendingEvent", eventState.isPendingEvent());
    }

    /**
     * Test method {@link EventState#stop()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testStop() throws Exception {
        stepInterpolator.addStep(0.5, 0.5);
        double[] y = new double[1];
        eventState.reinitializeBegin(0.0, y);
        stepInterpolator.setCurrentTime(0.5);
        eventState.setEventHandler(
            new TestEventHandler() {
                @Override
                public EventAction eventOccurred(double t, double[] y, boolean crossing) {
                    return EventAction.STOP;
                }
            });
        assertTrue("evaluateStep", eventState.evaluateStep(stepInterpolator));
        assertTrue("stop", eventState.stop());
    }

}