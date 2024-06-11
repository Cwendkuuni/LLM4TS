package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        assertNull(state.getEventHandler());
        assertEquals(0.01, state.getMaxCheckInterval(), 1e-9);
        assertEquals(1e-9, state.getConvergence(), 1e-9);
        assertEquals(10, state.getMaxIterationCount());
        assertNaN(state.getT0());
        assertNaN(state.getG0());
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNaN(state.getPendingEventTime());
        assertNaN(state.getPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        double[] y = new double[2];
        double tStart = 1.0;
        state.reinitializeBegin(tStart, y);
        assertEquals(tStart, state.getT0(), 1e-9);
        assertEquals(handler.g(tStart, y), state.getG0(), 1e-9);
        assertTrue(state.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                throw new EventException("test");
            }
        };
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        double[] y = new double[2];
        double tStart = 1.0;
        state.reinitializeBegin(tStart, y);
        fail("Expected EventException");
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[]{1.0, 1.0});
        interpolator.setInterpolatedTime(1.1);

        // No event
        assertFalse(state.evaluateStep(interpolator));
        assertEquals(1.0, state.getT0(), 1e-9);
        assertEquals(1.0, state.getG0(), 1e-9);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNaN(state.getPendingEventTime());

        // Event at t = 1.2
        interpolator.setInterpolatedTime(1.2);
        state.evaluateStep(interpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(1.2, state.getPendingEventTime(), 1e-9);

        // Event at t = 1.3
        interpolator.setInterpolatedTime(1.3);
        state.evaluateStep(interpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(1.3, state.getPendingEventTime(), 1e-9);

        // Event at t = 1.4
        interpolator.setInterpolatedTime(1.4);
        state.evaluateStep(interpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(1.4, state.getPendingEventTime(), 1e-9);

        // Event at t = 1.5
        interpolator.setInterpolatedTime(1.5);
        state.evaluateStep(interpolator);
        assertFalse(state.isPendingEvent());
        assertEquals(1.5, state.getT0(), 1e-9);
        assertEquals(handler.g(1.5, new double[]{1.5, 1.5}), state.getG0(), 1e-9);
        assertTrue(state.isG0Positive());
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);

        // No event
        double[] y = new double[2];
        state.stepAccepted(1.0, y);
        assertEquals(1.0, state.getT0(), 1e-9);
        assertEquals(handler.g(1.0, y), state.getG0(), 1e-9);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());

        // Event
        y[0] = 1.2;
        state.stepAccepted(1.1, y);
        assertTrue(state.isPendingEvent());
        assertEquals(1.1, state.getT0(), 1e-9);
        assertEquals(handler.g(1.1, y), state.getG0(), 1e-9);
        assertTrue(state.isG0Positive());
        assertEquals(1.1, state.getPendingEventTime(), 1e-9);

        // Event reset
        handler.resetEvent();
        state.stepAccepted(1.1, y);
        assertFalse(state.isPendingEvent());
        assertEquals(1.1, state.getT0(), 1e-9);
        assertEquals(handler.g(1.1, y), state.getG0(), 1e-9);
        assertTrue(state.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithException() throws EventException {
        EventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                throw new EventException("test");
            }
        };
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        double[] y = new double[2];
        state.stepAccepted(1.0, y);
        fail("Expected EventException");
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        double[] y = new double[2];
        double t = 1.0;

        // No event
        state.reset(t, y);
        assertFalse(state.isPendingEvent());

        // Event
        state.setPendingEvent(true);
        state.setPendingEventTime(1.1);
        state.reset(t, y);
        assertFalse(state.isPendingEvent());

        // Event reset
        state.setPendingEvent(true);
        state.setPendingEventTime(1.1);
        state.setNextAction(EventHandler.RESET_STATE);
        state.reset(t, y);
        assertEquals(1.1, state.getPreviousEventTime(), 1e-9);
    }

    @Test(expected = EventException.class)
    public void testResetWithException() throws EventException {
        EventHandler handler = new MockEventHandler() {
            @Override
            public void resetState(double t, double[] y) throws EventException {
                throw new EventException("test");
            }
        };
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        double[] y = new double[2];
        double tReset = 1.0;

        // No event
        state.reset(tReset, y);
        assertFalse(state.isPendingEvent());

        // Event
        state.setPendingEvent(true);
        state.setPendingEventTime(1.1);
        state.setNextAction(EventHandler.RESET_STATE);
        state.reset(tReset, y);
        fail("Expected EventException");
    }

    @Test
    public void testStop() {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());
    }

    @Test
    public void testIsStopped() {
        EventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 1e-9, 10);
        state.setNextAction(EventHandler.STOP);
        assertFalse(state.isStopped());
        state.setStopped(true);
        assertTrue(state.isStopped());
    }

    private static class MockEventHandler implements EventHandler {

        private double previousEventTime;

        @Override
        public void resetEvent() {
            previousEventTime = Double.NaN;
        }

        @Override
        public boolean eventOccurred(double t, double[] y, boolean increasingFlag) throws EventException {
            if (previousEventTime == Double.NaN) {
                previousEventTime = t;
            }
            return false;
        }

        @Override
        public void resetState(double t, double[] y) throws EventException {
            // empty
        }

        @Override
        public void setStopped(boolean stopped) {
            // empty
        }

        @Override
        public double g(double t, double[] y) throws EventException {
            return BesselFunctions.sin(t);
        }

        @Override
        public void setPendingEvent(boolean pendingEvent) {
            // empty
        }

        @Override
        public double getPreviousEventTime() {
            return previousEventTime;
        }

        public void setNextAction(int nextAction) {
            this.nextAction = nextAction;
        }

    }
}