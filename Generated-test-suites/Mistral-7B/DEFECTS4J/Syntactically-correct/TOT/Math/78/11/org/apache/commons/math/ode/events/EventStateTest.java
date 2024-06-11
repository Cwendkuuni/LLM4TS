package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.SimpleEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test for {@link EventState}.
 */
public class EventStateTest {

    /** A simple event handler that returns a constant value. */
    static class ConstantEventHandler implements EventHandler {

        private final double constant;

        public ConstantEventHandler(final double constant) {
            this.constant = constant;
        }

        @Override
        public double g(final double t, final double[] y) throws EventException {
            return constant;
        }

        @Override
        public void resetState(final double t, final double[] y) throws EventException {
            // no-op
        }

        @Override
        public int eventOccurred(final double t, final double[] y, final boolean increasing) {
            return EventHandler.RESET_STATE;
        }

    }

    /** A step interpolator that returns a constant value. */
    static class ConstantStepInterpolator implements StepInterpolator {

        private final double constant;

        public ConstantStepInterpolator(final double constant) {
            this.constant = constant;
        }

        @Override
        public double integrate(final double t, final double[] y) throws DerivativeException {
            return t + constant;
        }

        @Override
        public double getCurrentTime() {
            return 0.0;
        }

        @Override
        public void setInterpolatedTime(final double t) {
            // no-op
        }

        @Override
        public double[] getInterpolatedState() {
            return new double[0];
        }

        @Override
        public boolean isForward() {
            return true;
        }

    }

    /** Test for basic functionality. */
    @org.junit.Test
    public void testBasic() {
        // create a test event state
        final double maxCheckInterval = 0.01;
        final double convergence = 1e-7;
        final int maxIterationCount = 10;
        final EventHandler handler = new SimpleEventHandler();
        final EventState state = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        // test initialization
        assertNull(state.getEventHandler());
        assertEquals(maxCheckInterval, state.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, state.getConvergence(), 0.0);
        assertEquals(maxIterationCount, state.getMaxIterationCount());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());
        assertNull(state.getEventHandler());

        // test reinitialization
        double[] y = new double[5];
        double tStart = 1.0;
        state.reinitializeBegin(tStart, y);
        assertEquals(tStart, state.getT0(), 0.0);
        assertEquals(handler.g(tStart, y), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNull(state.getPreviousEventTime());
        assertFalse(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // test evaluateStep
        StepInterpolator interpolator = new ConstantStepInterpolator(0.1);
        try {
            state.evaluateStep(interpolator);
            fail("expected a DerivativeException");
        } catch (DerivativeException e) {
            // expected
        }
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 0.0);
        assertEquals(Double.NaN, state.getPreviousEventTime(), 0.0);
        assertFalse(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // test stepAccepted
        double[] yAccepted = new double[5];
        state.stepAccepted(1.1, yAccepted);
        assertEquals(1.1, state.getT0(), 0.0);
        assertEquals(handler.g(1.1, yAccepted), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(1.1, state.getPreviousEventTime(), 0.0);
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // test reset
        double tReset = 1.1;
        double[] yReset = new double[5];
        state.reset(tReset, yReset);
        assertEquals(1.1, state.getT0(), 0.0);
        assertEquals(handler.g(1.1, yReset), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(1.1, state.getPreviousEventTime(), 0.0);
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // test stop
        state.setNextAction(EventHandler.STOP);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertTrue(state.stop());

        // test stepAccepted with an event
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(final double t) throws FunctionEvaluationException {
                return Math.sin(t);
            }
        };
        double[] yEvent = new double[5];
        double[] yEventDerivative = new double[5];
        FunctionUtils.valueAndDerivative(f, 1.5, yEvent, yEventDerivative);
        state.reinitializeBegin(1.5, yEvent);
        interpolator = new ConstantStepInterpolator(0.1);
        try {
            state.evaluateStep(interpolator);
            fail("expected an EventException");
        } catch (EventException e) {
            // expected
        }
        assertTrue(state.isPendingEvent());
        assertEquals(1.532, state.getPendingEventTime(), 0.001);
        assertEquals(1.5, state.getPreviousEventTime(), 0.001);
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.EVENT, state.getNextAction());
        state.stepAccepted(1.6, yEvent);
        assertEquals(1.6, state.getT0(), 0.0);
        assertEquals(-0.9659258262890683, state.getG0(), 1e-6);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(1.6, state.getPreviousEventTime(), 0.0);
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        // test reset with an event
        state.reset(1.6, yEvent);
        assertEquals(1.6, state.getT0(), 0.0);
        assertEquals(-0.9659258262890683, state.getG0(), 1e-6);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(1.6, state.getPreviousEventTime(), 0.0);
        assertTrue(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

    }

}