package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Test;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        assertNotNull(state);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.1, state.getMaxCheckInterval(), 0.0);
        assertEquals(0.01, state.getConvergence(), 0.0);
        assertEquals(10, state.getMaxIterationCount());
        assertNull(state.getEventHandler().getState());
        assertNull(state.getEventHandler().getStateDerivatives());
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
        assertTrue(state.stop());
    }

    /**
     * Test {@link EventState#reinitializeBegin(double, double[])}.
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        final double tStart = 2.0;
        final double[] yStart = new double[] { 3.0 };
        state.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, state.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), state.getG0(), 0.0);
    }

    /**
     * Test {@link EventState#evaluateStep(StepInterpolator)}.
     */
    @Test
    public void testEvaluateStep() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        final StepInterpolator interpolator = new StepInterpolator();
        interpolator.setIntegratorStep(1.0);
        interpolator.setForward(true);
        interpolator.setCurrentTime(1.5);
        final double t1 = interpolator.getCurrentTime();

        // no event
        assertFalse(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getT0(), 0.0);
        assertEquals(handler.g(1.5, interpolator.getInterpolatedState()), state.getG0(), 0.0);

        // event at t0
        interpolator.setIntegratorStep(0.5);
        interpolator.setForward(true);
        interpolator.setCurrentTime(1.0);
        final double t0 = interpolator.getCurrentTime();
        state.reinitializeBegin(t0, new double[] { 0.0 });
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.0, state.getEventTime(), 0.0);

        // event at t1
        interpolator.setCurrentTime(1.5);
        state.reinitializeBegin(t1, new double[] { 0.0 });
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.5, state.getEventTime(), 0.0);

        // event at t1 + convergence
        interpolator.setCurrentTime(1.6);
        state.reinitializeBegin(t1, new double[] { 0.0 });
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.6, state.getEventTime(), 0.0);
    }

    /**
     * Test {@link EventState#getEventTime()}.
     */
    @Test
    public void testGetEventTime() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.pendingEvent = true;
        state.pendingEventTime = 1.0;
        assertEquals(1.0, state.getEventTime(), 0.0);
    }

    /**
     * Test {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void testStepAccepted() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.pendingEvent = true;
        state.pendingEventTime = 1.0;
        final double t = 2.0;
        final double[] y = new double[] { 3.0 };
        state.stepAccepted(t, y);
        assertEquals(t, state.getT0(), 0.0);
        assertEquals(handler.g(t, y), state.getG0(), 0.0);
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
    }

    /**
     * Test {@link EventState#stop()}.
     */
    @Test
    public void testStop() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        assertEquals(true, state.stop());
    }

    /**
     * Test {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testReset() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.pendingEvent = true;
        state.pendingEventTime = 1.0;
        final double t = 2.0;
        final double[] y = new double[] { 3.0 };
        state.reset(t, y);
        assertFalse(state.pendingEvent());
        assertNull(state.getEventTime());
    }

    /**
     * Test {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testResetResetEvent() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.pendingEvent = true;
        state.pendingEventTime = 1.0;
        state.nextAction = EventHandler.RESET_EVENT;
        final double t = 2.0;
        final double[] y = new double[] { 3.0 };
        state.reset(t, y);
        assertTrue(state.pendingEvent());
        assertEquals(1.0, state.getEventTime(), 0.0);
    }

    /**
     * Test {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testResetState() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.pendingEvent = true;
        state.pendingEventTime = 1.0;
        state.nextAction = EventHandler.RESET_STATE;
        final double t = 2.0;
        final double[] y = new double[] { 3.0 };
        state.reset(t, y);
        assertNull(state.getEventHandler().getState());
        assertNull(state.getEventHandler().getStateDerivatives());
    }

    /**
     * Test {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testResetStateDerivatives() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        state.pendingEvent = true;
        state.pendingEventTime = 1.0;
        state.nextAction = EventHandler.RESET_STATE_DERIVATIVES;
        final double t = 2.0;
        final double[] y = new double[] { 3.0 };
        state.reset(t, y);
        assertEquals(y[0], state.getEventHandler().getState()[0], 0.0);
        assertEquals(0.0, state.getEventHandler().getStateDerivatives()[0], 0.0);
    }

    /**
     * Test {@link EventState#pendingEvent()}.
     */
    @Test
    public void testPendingEvent() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.1, 0.01, 10);
        assertFalse(state.pendingEvent());
        state.pendingEvent = true;
        assertTrue(state.pendingEvent());
    }

    /**
     * Dummy event handler.
     */
    static class DummyEventHandler implements EventHandler {
        private double state;
        private double[] stateDerivatives;

        /**
         * Get the state.
         * @return the state
         */
        public double getState() {
            return state;
        }

        /**
         * Set the state.
         * @param state the state to set
         */
        public void setState(double state) {
            this.state = state;
        }

        /**
         * Get the state derivatives.
         * @return the state derivatives
         */
        public double[] getStateDerivatives() {
            return stateDerivatives;
        }

        /**
         * Set the state derivatives.
         * @param stateDerivatives the state derivatives to set
         */
        public void setStateDerivatives(double[] stateDerivatives) {
            this.stateDerivatives = stateDerivatives;
        }

        /**
         * Get the value of the event handler.
         * @param t the independent variable value
         * @param y the state vector
         * @return the value of the event handler
         * @throws DerivativeException if the derivatives cannot be
         * computed
         * @throws EventException if the event cannot be triggered
         */
        public double g(double t, double[] y) throws DerivativeException, EventException {
            throw new UnsupportedOperationException();
        }

        /**
         * Trigger an event.
         * @param t the independent variable value
         * @param y the state vector
         * @param increasing the variation direction
         * @throws EventException if the event fails to trigger
         */
        public void eventOccurred(double t, double[] y, boolean increasing) throws EventException {
            // empty
        }

        /**
         * Reset the state.
         * @param t the independent variable value
         * @param y the state vector
         */
        public void resetState(double t, double[] y) {
            // empty
        }

        /**
         * Reset the derivatives.
         */
        public void resetDerivatives() {
            // empty
        }
    }
}