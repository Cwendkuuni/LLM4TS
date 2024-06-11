package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventHandler.EventAction;
import org.apache.commons.math3.ode.events.EventHandler.EventType;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /**
     * Test method for {@link EventState#EventState(EventHandler, double, double, int)}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testConstructor() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        assertNotNull(state);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.01, state.getMaxCheckInterval(), DELTA);
        assertEquals(0.001, state.getConvergence(), DELTA);
        assertEquals(10, state.getMaxIterationCount());
        assertEquals(Double.NaN, state.getT0());
        assertEquals(Double.NaN, state.getG0());
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime());
        assertEquals(Double.NaN, state.getPreviousEventTime());
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test method for {@link EventState#reinitializeBegin(double, double[])}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testReinitializeBegin() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        double[] y = new double[] { 1.0, 2.0 };
        state.reinitializeBegin(0.0, y);
        assertEquals(0.0, state.getT0(), DELTA);
        assertEquals(handler.g(0.0, y), state.getG0(), DELTA);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime());
        assertEquals(Double.NaN, state.getPreviousEventTime());
    }

    /**
     * Test method for {@link EventState#evaluateStep(StepInterpolator)}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testEvaluateStep() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(5);
        interpolator.addStep(0.0, new double[] { 1.0, 2.0 });
        interpolator.addStep(0.01, new double[] { 2.0, 3.0 });
        interpolator.setCurrentStep(1);
        assertTrue(state.evaluateStep(interpolator));
        assertTrue(state.isPendingEvent());
        assertEquals(0.01, state.getPendingEventTime(), DELTA);
        assertEquals(Double.NaN, state.getPreviousEventTime());
    }

    /**
     * Test method for {@link EventState#getEventTime()}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testGetEventTime() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEventTime(0.02);
        assertEquals(0.02, state.getEventTime(), DELTA);
    }

    /**
     * Test method for {@link EventState#stepAccepted(double, double[])}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testStepAccepted() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        double[] y = new double[] { 1.0, 2.0 };
        state.reinitializeBegin(0.0, y);
        state.evaluateStep(new RungeKuttaStepInterpolator(5));
        state.stepAccepted(0.01, y);
        assertEquals(0.01, state.getT0(), DELTA);
        assertEquals(handler.g(0.01, y), state.getG0(), DELTA);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime());
        assertEquals(0.01, state.getPreviousEventTime(), DELTA);
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test method for {@link EventState#stop()}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testStop() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());
    }

    /**
     * Test method for {@link EventState#reset(double, double[])}.
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testReset() throws Exception {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        double[] y = new double[] { 1.0, 2.0 };
        state.setPendingEvent(true);
        state.setPendingEventTime(0.02);
        state.setNextAction(EventHandler.RESET_STATE);
        state.reset(0.03, y);
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime());
        assertEquals(0.03, state.getPreviousEventTime(), DELTA);
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test class for a dummy event handler.
     */
    public static class TestEventHandler implements EventHandler {

        /**
         * {@inheritDoc}
         */
        public double g(double t, double[] y) throws EventException {
            return Math.sin(t) - y[0];
        }

        /**
         * {@inheritDoc}
         */
        public void resetState(double t, double[] y) throws EventException {
            y[0] = 0.0;
        }

        /**
         * {@inheritDoc}
         */
        public void eventOccurred(double t, double[] y, boolean crossesEvent)
                throws EventException {
            if (crossesEvent) {
                nextAction = EventAction.STOP;
            } else {
                nextAction = EventAction.RESET_STATE;
            }
        }

        /**
         * {@inheritDoc}
         */
        public EventType getEventType() {
            return EventType.TRANSCENDENTAL;
        }

    }

}