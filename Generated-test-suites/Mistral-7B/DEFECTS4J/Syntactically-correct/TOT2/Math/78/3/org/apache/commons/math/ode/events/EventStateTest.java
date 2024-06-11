package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.space.VectorValue;
import org.junit.Test;

/**
 * Test class for {@link EventState} class.
 */
public class EventStateTest {

    /** Test method for {@link EventState#EventState(EventHandler, double, double, int)} . */
    @Test
    public void testConstructor() {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, eventState.getConvergence(), 0.0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertFalse(Double.isFinite(eventState.getEventTime()));
        assertFalse(eventState.stop());
        assertFalse(eventState.reset(0.0, new VectorValue(new double[0])));
    }

    /** Test method for {@link EventState#reinitializeBegin(double, double[])}. */
    @Test
    public void testReinitializeBegin() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        final double tStart = 0.0;
        final double[] yStart = new double[] { 1.0 };
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(yStart[0], eventState.getG0(), 0.0);
        assertTrue(eventState.getG0Positive());
        assertFalse(eventState.pendingEvent());
        assertFalse(eventState.stop());
        assertFalse(eventState.reset(0.0, new VectorValue(new double[0])));
    }

    /** Test method for {@link EventState#evaluateStep(StepInterpolator)}. */
    @Test
    public void testEvaluateStep() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        final StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(new StepInterpolator.Step(0.0, 0.0));
        interpolator.add(new StepInterpolator.Step(0.1, 1.0));
        interpolator.add(new StepInterpolator.Step(0.2, 2.0));
        interpolator.setForward(true);

        // Test no event
        final boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
        assertEquals(EventState.EventAction.CONTINUE, eventState.getNextAction());
        assertEquals(Double.NaN, eventState.getEventTime());

        // Test event
        interpolator.setForward(false);
        interpolator.setInterpolatedTime(0.15);
        final double gb = 0.0;
        interpolator.setValueFunction(new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                if (Double.isNaN(t) || t < 0.0) {
                    throw new FunctionEvaluationException("t must be finite and positive");
                }
                return gb;
            }
        });
        handler.setG(new double[] { gb }, new double[] { 0.0 });
        final boolean expectedResult = true;
        final boolean result2 = eventState.evaluateStep(interpolator);
        assertTrue(result2);
        assertEquals(EventState.EventAction.EVENT_OCCURRED, eventState.getNextAction());
        assertEquals(0.15, eventState.getEventTime(), 0.0);
        assertTrue(eventState.pendingEvent());
        assertEquals(0.15, eventState.getPendingEventTime(), 0.0);

    }

    /** Test method for {@link EventState#stepAccepted(double, double[])}. */
    @Test
    public void testStepAccepted() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        final double t = 0.15;
        final double[] y = new double[] { 1.0 };
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(y[0], eventState.getG0(), 0.0);
        assertEquals(eventState.getG0Positive(), handler.g(t, y) >= 0);
        assertFalse(eventState.pendingEvent());
        assertFalse(eventState.stop());
        assertFalse(eventState.reset(0.0, new VectorValue(new double[0])));
    }

    /** Test method for {@link EventState#stop()}. */
    @Test
    public void testStop() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    /** Test method for {@link EventState#reset(double, double[])}. */
    @Test
    public void testReset() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        eventState.setNextAction(EventHandler.RESET_STATE);
        final double t = 0.15;
        final double[] y = new double[] { 1.0 };
        final boolean result = eventState.reset(t, y);
        assertTrue(result);
        assertFalse(eventState.pendingEvent());
        assertFalse(eventState.stop());
    }

    /** Test method for {@link EventState#pendingEvent()}. */
    @Test
    public void testPendingEvent() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        assertFalse(eventState.pendingEvent());
        eventState.setPendingEvent(true);
        assertTrue(eventState.pendingEvent());
    }

    /** Test method for {@link EventState#setPendingEventTime(double)}. */
    @Test
    public void testSetPendingEventTime() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        assertFalse(eventState.pendingEvent());
        double pendingEventTime = 0.15;
        eventState.setPendingEventTime(pendingEventTime);
        assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.pendingEvent());
    }

    /** Test method for {@link EventState#getPendingEventTime()}. */
    @Test
    public void testGetPendingEventTime() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        assertFalse(eventState.pendingEvent());
        double pendingEventTime = 0.15;
        eventState.setPendingEventTime(pendingEventTime);
        assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);
    }

    /** Test method for {@link EventState#getT0()}. */
    @Test
    public void testGetT0() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double t0 = 0.15;
        eventState.setT0(t0);
        assertEquals(t0, eventState.getT0(), 0.0);
    }

    /** Test method for {@link EventState#getG0()}. */
    @Test
    public void testGetG0() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double g0 = 0.15;
        eventState.setG0(g0);
        assertEquals(g0, eventState.getG0(), 0.0);
    }

    /** Test method for {@link EventState#getG0Positive()}. */
    @Test
    public void testGetG0Positive() throws Exception {
        final double maxCheckInterval = 0.1;
        final double convergence = 0.0001;
        final int maxIterationCount = 100;
        final EventHandler handler = new MockEventHandler();
        final EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        boolean g0Positive = true;
        eventState.setG0Positive(g0Positive);
        assertEquals(g0Positive, eventState.getG0Positive());
    }

}