package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test class for {@link EventState} class.
 */
public class EventStateTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        assertNotNull(state);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.01, state.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, state.getConvergence(), 0.0);
        assertEquals(10, state.getMaxIterationCount());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
        assertNull(state.getPreviousEventTime());
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test reinitializeBegin method.
     * @throws EventException if an error occurs during the test
     */
    @org.junit.Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);

        double[] y = new double[] { 1.0, 1.0 };
        state.reinitializeBegin(0.0, y);
        assertEquals(0.0, state.getT0(), 0.0);
        assertEquals(-1.0, state.getG0(), 0.0);
        assertTrue(state.getG0Positive());
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
        assertNull(state.getPreviousEventTime());
    }

    /**
     * Test evaluateStep method with no event.
     * @throws DerivativeException if an error occurs during the test
     * @throws EventException if an error occurs during the test
     * @throws ConvergenceException if an error occurs during the test
     */
    @org.junit.Test
    public void testEvaluateStepNoEvent()
        throws DerivativeException, EventException, ConvergenceException {

        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();

        interpolator.addInterpolatedValue(0.0, 0.0, new double[] { 1.0, 1.0 });
        interpolator.addInterpolatedValue(0.01, 0.1, new double[] { 1.1, 1.1 });

        assertTrue(state.evaluateStep(interpolator));
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
        assertEquals(0.1, state.getT0(), 0.0);
        assertEquals(0.1, state.getG0(), 0.0);
        assertTrue(state.getG0Positive());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

    /**
     * Test evaluateStep method with an event.
     * @throws DerivativeException if an error occurs during the test
     * @throws EventException if an error occurs during the test
     * @throws ConvergenceException if an error occurs during the test
     */
    @org.junit.Test
    public void testEvaluateStepEvent()
        throws DerivativeException, EventException, ConvergenceException {

        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();

        interpolator.addInterpolatedValue(0.0, 0.0, new double[] { 1.0, 1.0 });
        interpolator.addInterpolatedValue(0.005, 1.1, new double[] { 1.05, 1.05 });
        interpolator.addInterpolatedValue(0.01, 0.1, new double[] { 1.1, 1.1 });

        assertTrue(state.evaluateStep(interpolator));
        assertTrue(state.pendingEvent());
        assertEquals(0.005, state.getEventTime(), 0.0);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

    }

    /**
     * Test stepAccepted method with no event.
     * @throws EventException if an error occurs during the test
     */
    @org.junit.Test
    public void testStepAcceptedNoEvent() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);

        double[] y = new double[] { 1.0, 1.0 };
        state.stepAccepted(0.011, y);
        assertEquals(0.011, state.getT0(), 0.0);
        assertEquals(-1.1, state.getG0(), 0.0);
        assertTrue(state.getG0Positive());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test stepAccepted method with an event.
     * @throws EventException if an error occurs during the test
     */
    @org.junit.Test
    public void testStepAcceptedEvent() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);

        state.setPendingEvent(true);
        state.setPendingEventTime(0.005);

        double[] y = new double[] { 1.0, 1.0 };
        state.stepAccepted(0.0051, y);
        assertEquals(0.0051, state.getT0(), 0.0);
        assertEquals(-1.1, state.getG0(), 0.0);
        assertTrue(state.getG0Positive());
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());
        assertEquals(0.005, state.getEventTime(), 0.0);

    }

    /**
     * Test reset method.
     * @throws EventException if an error occurs during the test
     */
    @org.junit.Test
    public void testReset() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(0.005);
        state.setNextAction(EventHandler.RESET_STATE);

        double[] y = new double[] { 1.0, 1.0 };
        state.reset(0.01, y);
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

    /**
     * Test reset method with event and derivatives.
     * @throws EventException if an error occurs during the test
     */
    @org.junit.Test
    public void testResetWithDerivatives() throws EventException {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(0.005);
        state.setNextAction(EventHandler.RESET_STATE_AND_DERIVATIVES);

        double[] y = new double[] { 1.0, 1.0 };
        state.reset(0.01, y);
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());

    }

    /**
     * Test stop method.
     */
    @org.junit.Test
    public void testStop() {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());
    }

    /**
     * Test getters and setters.
     */
    @org.junit.Test
    public void testGettersAndSetters() {
        EventHandler handler = new TestEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);

        state.setEventHandler(new TestEventHandler());
        assertEquals(new TestEventHandler(), state.getEventHandler());

        state.setMaxCheckInterval(0.02);
        assertEquals(0.02, state.getMaxCheckInterval(), 0.0);

        state.setConvergence(0.002);
        assertEquals(0.002, state.getConvergence(), 0.0);

        state.setMaxIterationCount(20);
        assertEquals(20, state.getMaxIterationCount());

        state.setT0(1.0);
        assertEquals(1.0, state.getT0(), 0.0);

        state.setG0(1.1);
        assertEquals(1.1, state.getG0(), 0.0);

        state.setG0Positive(false);
        assertFalse(state.getG0Positive());

        state.setPendingEvent(true);
        assertTrue(state.pendingEvent());

        state.setPendingEventTime(1.2);
        assertEquals(1.2, state.getPendingEventTime(), 0.0);

        state.setPreviousEventTime(1.3);
        assertEquals(1.3, state.getPreviousEventTime(), 0.0);

        state.setForward(false);
        assertFalse(state.isForward());

        state.setIncreasing(true);
        assertTrue(state.isIncreasing());

        state.setNextAction(EventHandler.EVENT_OCCURRED);
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

        state.setNextAction(EventHandler.RESET_STATE);
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());

        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());

        state.setNextAction(EventHandler.STOP);
        assertEquals(EventHandler.STOP, state.getNextAction());
    }

    /**
     * Test custom event handler class.
     */
    public static class TestEventHandler implements EventHandler {

        private boolean triggered;

        @Override
        public void resetState(double t, double[] y) {
            // empty method
        }

        @Override
        public double g(double t, double[] y) throws EventException {
            triggered = true;
            return Math.sin(t) + y[0];
        }

        @Override
        public EventAction eventOccurred(double t, double[] y, boolean increaseEvent)
                throws EventException {
            return EventAction.RESET_STATE;
        }

        @Override
        public void eventReset() {
            triggered = false;
        }

        @Override
        public boolean isTriggered() {
            return triggered;
        }

    }

    /**
     * Test custom event handler class with polynomial event.
     */
    public static class TestPolynomialEventHandler implements EventHandler {

        @Override
        public void resetState(double t, double[] y) {
            // empty method
        }

        @Override
        public double g(double t, double[] y) throws EventException {
            return new PolynomialFunction(1.0, -1.0, 0.0).value(t);
        }

        @Override
        public EventAction eventOccurred(double t, double[] y, boolean increaseEvent)
                throws EventException {
            return EventAction.RESET_STATE;
        }

        @Override
        public void eventReset() {
            // empty method
        }

        @Override
        public boolean isTriggered() {
            return false;
        }

    }

}