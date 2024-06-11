package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.space.VectorValue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    private EventState eventState;
    private MockEventHandler mockEventHandler;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        double convergence = 1e-9;
        double maxCheckInterval = 1e-5;
        int maxIterationCount = 10;
        mockEventHandler = new MockEventHandler();
        eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);
        interpolator = new StepInterpolator();
    }

    /**
     * Test method for {@link EventState#EventState(EventHandler, double, double, int)}.
     */
    @Test
    public void testEventState() {
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0);
        assertEquals(convergence, eventState.getConvergence(), 0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        Double.isNaN(eventState.getT0());
        Double.isNaN(eventState.getG0());
        assertTrue(eventState.isG0Positive());
    }

    /**
     * Test method for {@link EventState#reinitializeBegin(double, double[])}.
     * @throws EventException
     */
    @Test(expected = EventException.class)
    public void testReinitializeBeginThrowsEventException() throws EventException {
        double tStart = 0.0;
        double[] yStart = { 1.0, 2.0 };
        eventState.reinitializeBegin(tStart, yStart);
        assertFalse(eventState.isPendingEvent());
        assertEquals(tStart, eventState.getT0(), 0);
        assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0);
        assertTrue(eventState.isG0Positive());
    }

    /**
     * Test method for {@link EventState#evaluateStep(StepInterpolator)}.
     * @throws DerivativeException
     * @throws EventException
     * @throws ConvergenceException
     */
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 1.0;
        interpolator.setCurrentTime(t1);
        double gb = mockEventHandler.g(t1, new VectorValue(new double[] { 1.0, 2.0 }));
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(t1, eventState.getPendingEventTime(), 0);
        assertEquals(gb, eventState.getG0(), 0);
        assertTrue(eventState.isG0Positive() ^ gb >= 0);
        assertTrue(eventState.isIncreasing() == gb > g0);
    }

    /**
     * Test method for {@link EventState#getEventTime()}.
     */
    @Test
    public void testGetEventTime() {
        double t1 = 1.0;
        interpolator.setCurrentTime(t1);
        eventState.evaluateStep(interpolator);
        assertEquals(eventState.getPendingEventTime(), t1, 0);
    }

    /**
     * Test method for {@link EventState#stepAccepted(double, double[])}.
     * @throws EventException
     */
    @Test(expected = EventException.class)
    public void testStepAcceptedThrowsEventException() throws EventException {
        double t = 1.0;
        double[] y = { 1.0, 2.0 };
        eventState.stepAccepted(t, y);
        assertFalse(eventState.isPendingEvent());
        assertEquals(t, eventState.getT0(), 0);
        assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0);
        assertTrue(eventState.isG0Positive());
    }

    /**
     * Test method for {@link EventState#stop()}.
     */
    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    /**
     * Test method for {@link EventState#reset(double, double[])}.
     * @throws EventException
     */
    @Test(expected = EventException.class)
    public void testResetThrowsEventException() throws EventException {
        double t = 1.0;
        double[] y = { 1.0, 2.0 };
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

}

class MockEventHandler implements EventHandler {

    private boolean resetDerivatives = false;

    @Override
    public double g(double t, double[] y) {
        return Math.sin(t) + y[0];
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean increaseEvent) {
        resetDerivatives = increaseEvent;
    }

    @Override
    public void resetState(double t, double[] y) {
        y[0] = 0.0;
    }

    @Override
    public void resetDerivatives(double t, double[] y) {
        resetDerivatives = true;
    }

}