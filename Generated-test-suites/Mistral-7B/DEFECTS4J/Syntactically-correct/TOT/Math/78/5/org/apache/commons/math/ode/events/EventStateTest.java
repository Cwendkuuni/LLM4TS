package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.DerivativeException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventState.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /**
     * Test method for {@link EventState#EventState(EventHandler, double, double, int)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        final MockEventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, 1.0e-9, 1.0e-9, 10);
        assertNotNull(eventState);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(1.0e-9, eventState.getMaxCheckInterval(), 1e-12);
        assertEquals(1.0e-9, eventState.getConvergence(), 1e-12);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertNull(eventState.getEventHandler().getPreviousState());
    }

    /**
     * Test method for {@link EventState#reinitializeBegin(double, double[])}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testReinitializeBegin() throws Exception {
        final MockEventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, 1.0e-9, 1.0e-9, 10);
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };
        eventState.reinitializeBegin(t0, y0);
        assertEquals(t0, eventState.getT0(), 1e-12);
        assertEquals(y0, eventState.getY0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method for {@link EventState#evaluateStep(StepInterpolator)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEvaluateStep() throws Exception {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 1.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] { 1.0, 2.0 };
            }

            @Override
            public void setInterpolatedTime(double time) {
                // dummy method
            }
        };
        final MockEventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, 1.0e-9, 1.0e-9, 10);
        interpolator.setForward(true);
        assertTrue(eventState.evaluateStep(interpolator));
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.0, eventState.getPendingEventTime(), 1e-12);
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        interpolator.setForward(false);
        assertFalse(eventState.evaluateStep(interpolator));
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                throw new DerivativeException(new EventException("Test exception"));
            }
        };
        interpolator.setInterpolator(new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 1.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] { 1.0, 2.0 };
            }

            @Override
            public void setInterpolatedTime(double time) {
                // dummy method
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                throw new EventException("Test exception");
            }

            @Override
            public void setInterpolatedDerivatives(double[] values) {
                // dummy method
            }

            @Override
            public double interpolate(double t) throws DerivativeException {
                throw new EventException("Test exception");
            }

            @Override
            public void setInterpolator(UnivariateRealFunction function) {
                this.function = function;
            }
        });
        interpolator.setFunction(f);
        assertFalse(eventState.evaluateStep(interpolator));
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test method for {@link EventState#stepAccepted(double, double[])}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testStepAccepted() throws Exception {
        final MockEventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, 1.0e-9, 1.0e-9, 10);
        final double t = 1.0;
        final double[] y = new double[] { 1.0, 2.0 };
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 1e-12);
        assertEquals(y, eventState.getY0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        final double tEvent = 2.0;
        final double[] yEvent = new double[] { 1.0, 3.0 };
        handler.setEventOccurred(tEvent, yEvent, true);
        final double t1 = 1.5;
        final double[] y1 = new double[] { 1.0, 2.1 };
        eventState.stepAccepted(t1, y1);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());

        handler.setEventOccurred(tEvent, yEvent, false);
        final double t2 = 2.5;
        final double[] y2 = new double[] { 3.0, 2.1 };
        eventState.stepAccepted(t2, y2);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

    /**
     * Test method for {@link EventState#reset(double, double[])}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testReset() throws Exception {
        final MockEventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, 1.0e-9, 1.0e-9, 10);
        final double t = 1.0;
        final double[] y = new double[] { 1.0, 2.0 };
        eventState.stepAccepted(t, y);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        handler.setEventOccurred(t, y, true);
        final double tReset = 1.5;
        final double[] yReset = new double[] { 2.0, 3.0 };
        assertTrue(eventState.reset(tReset, yReset));
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(tReset, eventState.getT0(), 1e-12);
        assertEquals(yReset, eventState.getY0(), 0.0);

        handler.setEventOccurred(t, y, false);
        final double tReset2 = 2.0;
        final double[] yReset2 = new double[] { 3.0, 4.0 };
        assertTrue(eventState.reset(tReset2, yReset2));
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(tReset2, eventState.getT0(), 1e-12);
        assertEquals(yReset2, eventState.getY0(), 0.0);
    }

    /**
     * Test method for {@link EventState#stop()}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testStop() throws Exception {
        final MockEventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, 1.0e-9, 1.0e-9, 10);
        final double t = 1.0;
        final double[] y = new double[] { 1.0, 2.0 };
        eventState.stepAccepted(t, y);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        handler.setEventOccurred(t, y, true);
        assertTrue(eventState.stop());
        assertTrue(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.STOP, eventState.getNextAction());

        handler.setEventOccurred(t, y, false);
        assertTrue(eventState.stop());
        assertTrue(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-12);
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    /**
     * Test class for {@link MockEventHandler}.
     */
    public static class MockEventHandler implements EventHandler {
        private double previousEventTime = Double.NaN;
        private double[] previousState = null;
        private boolean stateReset = false;
        private boolean eventOccurred = false;
        private double eventTime = Double.NaN;

        /**
         * {@inheritDoc}
         */
        @Override
        public double g(double time, double[] state) throws EventException {
            return 0.0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resetState(double time, double[] state) {
            previousState = state;
            stateReset = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void eventOccurred(double time, double[] state, boolean isLocalEvent) {
            if (isLocalEvent) {
                previousState = state;
                eventOccurred = true;
                eventTime = time;
            } else {
                previousEventTime = time;
            }
        }

        /**
         * Getter for the previous state.
         * @return the previous state
         */
        public double[] getPreviousState() {
            return previousState;
        }

        /**
         * Getter for the previous event time.
         * @return the previous event time
         */
        public double getPreviousEventTime() {
            return previousEventTime;
        }

        /**
         * Getter for the previous event.
         * @return true if an event occurred during the previous step
         */
        public boolean hasEventOccurred() {
            return eventOccurred;
        }

        /**
         * Getter for the previous event time.
         * @return the previous event time
         */
        public double getEventTime() {
            return eventTime;
        }

        /**
         * Reset the state of the mock event handler.
         */
        public void reset() {
            previousEventTime = Double.NaN;
            previousState = null;
            eventOccurred = false;
            stateReset = false;
        }
    }

}