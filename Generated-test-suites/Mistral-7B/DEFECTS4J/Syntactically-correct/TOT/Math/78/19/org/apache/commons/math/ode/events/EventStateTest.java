package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test for {@link EventState} class.
 */
public class EventStateTest {

    /** A mock event handler for testing purposes. */
    public static class MockEventHandler implements EventHandler {

        private double g;
        private double previousEventTime;
        private boolean pendingEvent;
        private boolean increasing;

        public MockEventHandler(double g) {
            this.g = g;
            this.previousEventTime = Double.NaN;
            this.pendingEvent = false;
            this.increasing = g > 0;
        }

        @Override
        public double g(double t, double[] y) throws EventException {
            return g;
        }

        @Override
        public void resetState(double t, double[] y) {
            // no-op
        }

        @Override
        public boolean eventOccurred(double t, double[] y, boolean crossing) {
            this.previousEventTime = t;
            this.pendingEvent = true;
            this.increasing = (g > 0) ^ crossing;
            return true;
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // no-op
        }

        @Override
        public int nextAction() {
            return EventHandler.CONTINUE;
        }

        public void setG(double g) {
            this.g = g;
        }

        public double getPreviousEventTime() {
            return previousEventTime;
        }

        public boolean isPendingEvent() {
            return pendingEvent;
        }

        public boolean isIncreasing() {
            return increasing;
        }

    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final double maxCheckInterval = 1.0;
        final double convergence = 0.001;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        assertEquals(handler, state.getEventHandler());
        assertEquals(maxCheckInterval, state.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, state.getConvergence(), 0.0);
        assertEquals(maxIterationCount, state.getMaxIterationCount());
        assertEquals(Double.NaN, state.getT0(), 0.0);
        assertEquals(Double.NaN, state.getG0(), 0.0);
        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test reinitialization of event state.
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        final double maxCheckInterval = 1.0;
        final double convergence = 0.001;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 2.0 };

        state.reinitializeBegin(t0, y0);

        assertEquals(t0, state.getT0(), 0.0);
        assertEquals(handler.g(t0, y0), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test evaluateStep method with event during step.
     */
    @Test
    public void testEvaluateStepWithEvent()
        throws DerivativeException, EventException, ConvergenceException {

        final double maxCheckInterval = 1.0;
        final double convergence = 0.01;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);

        final StepInterpolator interpolator = new StepInterpolator();

        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        handler.setG(0.0);

        interpolator.setInitialStep(0.1);
        interpolator.setInterpolatedState(new double[] { 1.0, 2.0 });

        state.reinitializeBegin(0.0, new double[] { 1.0, 2.0 });

        assertTrue(state.evaluateStep(interpolator));

        assertEquals(0.01, state.getPendingEventTime(), 0.0);
        assertTrue(state.isPendingEvent());
        assertTrue(state.hasPendingEvent());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        handler.setG(0.01);

        assertTrue(state.evaluateStep(interpolator));

        assertEquals(0.021, state.getPendingEventTime(), 0.0);
        assertTrue(state.isPendingEvent());
        assertTrue(state.hasPendingEvent());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        handler.setG(-0.01);

        assertFalse(state.evaluateStep(interpolator));

        assertEquals(Double.NaN, state.getPendingEventTime(), 0.0);
        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

    /**
     * Test evaluateStep method with no event during step.
     */
    @Test
    public void testEvaluateStepNoEvent()
        throws DerivativeException, EventException, ConvergenceException {

        final double maxCheckInterval = 1.0;
        final double convergence = 0.01;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);

        final StepInterpolator interpolator = new StepInterpolator();

        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        handler.setG(0.0);

        interpolator.setInitialStep(0.1);
        interpolator.setInterpolatedState(new double[] { 1.0, 2.0 });

        state.reinitializeBegin(0.0, new double[] { 1.0, 2.0 });

        assertFalse(state.evaluateStep(interpolator));

        assertEquals(Double.NaN, state.getPendingEventTime(), 0.0);
        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        handler.setG(0.01);

        interpolator.setInterpolatedTime(0.05);
        interpolator.setInterpolatedState(new double[] { 1.01, 2.01 });

        assertFalse(state.evaluateStep(interpolator));

        assertEquals(Double.NaN, state.getPendingEventTime(), 0.0);
        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

    /**
     * Test stepAccepted method.
     */
    @Test
    public void testStepAccepted() throws EventException {
        final double maxCheckInterval = 1.0;
        final double convergence = 0.01;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);

        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        final double t = 1.0;
        final double[] y = new double[] { 1.1, 2.1 };

        handler.setG(0.0);

        state.reinitializeBegin(0.0, new double[] { 1.0, 2.0 });

        state.evaluateStep(new StepInterpolator());

        handler.setG(1.0);

        state.stepAccepted(t, y);

        assertEquals(t, state.getT0(), 0.0);
        assertEquals(handler.g(t, y), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        assertTrue(handler.isPendingEvent());
        assertTrue(handler.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, handler.getNextAction());
    }

    /**
     * Test stop method.
     */
    @Test
    public void testStop() throws EventException {
        final double maxCheckInterval = 1.0;
        final double convergence = 0.01;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);

        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        handler.setG(0.0);

        state.setNextAction(EventHandler.STOP);

        assertTrue(state.stop());

        assertFalse(state.isPendingEvent());
        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.STOP, state.getNextAction());
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() throws EventException {
        final double maxCheckInterval = 1.0;
        final double convergence = 0.01;
        final int maxIterationCount = 10;

        final EventHandler handler = new MockEventHandler(0.0);

        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        handler.setG(0.0);

        state.reinitializeBegin(0.0, new double[] { 1.0, 2.0 });

        handler.setG(1.0);

        state.stepAccepted(1.0, new double[] { 1.1, 2.1 });

        assertTrue(state.hasPendingEvent());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

        handler.setNextAction(EventHandler.RESET_STATE);

        state.reset(1.0, new double[] { 1.1, 2.1 });

        assertFalse(state.hasPendingEvent());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        handler.setNextAction(EventHandler.RESET_DERIVATIVES);

        state.reset(1.0, new double[] { 1.1, 2.1 });

        assertFalse(state.isG0Positive());
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
    }

}