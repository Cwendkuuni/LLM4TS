package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.DerivativeException;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Tests for {@link EventState} class.
 */
public class EventStateTest {

    /** Event handler mock. */
    private EventHandler handlerMock;

    /** Event state under test. */
    private EventState eventState;

    /**
     * Sets up the mocks and initializes the event state under test.
     */
    @Before
    public void setUp() {
        handlerMock = Mockito.mock(EventHandler.class);
        eventState = new EventState(handlerMock, 0.001, 0.001, 10);
    }

    /**
     * Tests initialization of event state.
     */
    @Test
    public void testInitialization() {
        assertNull(eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Tests reinitialization of event state.
     * @throws DerivativeException if the interpolator fails to compute the switching function somewhere within the step
     * @throws EventException if the switching function cannot be evaluated
     */
    @Test
    public void testReinitialization() throws DerivativeException, EventException {
        final double tStart = 0.0;
        final double[] yStart = new double[5];

        Mockito.when(handlerMock.g(Double.NaN, yStart)).thenReturn(Double.NaN);
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.g(Double.anyInt(), yStart)).thenThrow(new EventException());
        try {
            eventState.reinitializeBegin(tStart, yStart);
            fail("Expected EventException");
        } catch (EventException e) {
            // expected
        }

        Mockito.when(handlerMock.g(tStart, yStart)).thenReturn(1.0);
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0());
        assertEquals(1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.g(tStart, yStart)).thenReturn(-1.0);
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0());
        assertEquals(-1.0, eventState.getG0(), 0.0);
        assertFalse(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Tests evaluation of step.
     * @throws DerivativeException if the interpolator fails to
     * compute the switching function somewhere within the step
     * @throws EventException if the switching function cannot be evaluated
     * @throws ConvergenceException if an event cannot be located
     */
    @Test
    public void testEvaluateStep()
        throws DerivativeException, EventException, ConvergenceException {

        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.isForward()).thenReturn(true);
        Mockito.when(interpolator.getCurrentTime()).thenReturn(0.1);

        Mockito.when(handlerMock.g(Double.NaN, Mockito.anyDoubleArray())).thenReturn(Double.NaN);

        eventState.evaluateStep(interpolator);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.g(0.0, Mockito.anyDoubleArray())).thenReturn(1.0);
        Mockito.when(handlerMock.g(0.1, Mockito.anyDoubleArray())).thenReturn(-1.0);
        Mockito.when(handlerMock.g(0.15, Mockito.anyDoubleArray())).thenReturn(1.0);

        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(0.125, eventState.getPendingEventTime(), 0.001);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.EVENT, eventState.getNextAction());

        eventState.stepAccepted(0.125, new double[5]);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.eventOccurred(0.125, Mockito.anyDoubleArray(), Mockito.anyBoolean()))
            .thenReturn(EventHandler.RESET_DERIVATIVES);
        eventState.stepAccepted(0.125, new double[5]);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());

        Mockito.when(handlerMock.eventOccurred(0.125, Mockito.anyDoubleArray(), Mockito.anyBoolean()))
            .thenReturn(EventHandler.STOP);
        eventState.stepAccepted(0.125, new double[5]);
        assertTrue(eventState.isStop());

    }

    /**
     * Tests getting event time.
     */
    @Test
    public void testGetEventTime() {
        eventState.pendingEventTime = 0.125;
        assertEquals(0.125, eventState.getEventTime(), 0.001);
        assertNull(eventState.getEventTime());
    }

    /**
     * Tests acknowledging step acceptance.
     * @throws EventException if the value of the event
     * handler cannot be evaluated
     */
    @Test
    public void testStepAccepted() throws EventException {
        final double t = 0.125;
        final double[] y = new double[5];

        Mockito.when(handlerMock.g(t, y)).thenReturn(1.0);
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0());
        assertEquals(1.0, eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.g(t, y)).thenThrow(new EventException());
        try {
            eventState.stepAccepted(t, y);
            fail("Expected EventException");
        } catch (EventException e) {
            // expected
        }

        Mockito.when(handlerMock.g(t, y)).thenReturn(-1.0);
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0());
        assertEquals(-1.0, eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.eventOccurred(t, y, Mockito.anyBoolean()))
            .thenReturn(EventHandler.RESET_STATE);
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0());
        assertEquals(-1.0, eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());

        Mockito.when(handlerMock.eventOccurred(t, y, Mockito.anyBoolean()))
            .thenReturn(EventHandler.STOP);
        eventState.stepAccepted(t, y);
        assertTrue(eventState.isStop());
    }

    /**
     * Tests resetting state.
     * @throws EventException if the state cannot be reseted by the event
     * handler
     */
    @Test
    public void testReset() throws EventException {
        final double t = 0.125;
        final double[] y = new double[5];

        Mockito.when(handlerMock.resetState(t, y)).thenThrow(new EventException());
        try {
            eventState.reset(t, y);
            fail("Expected EventException");
        } catch (EventException e) {
            // expected
        }

        Mockito.when(handlerMock.resetState(t, y)).thenReturn();
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        Mockito.when(handlerMock.resetState(t, y)).thenReturn(EventHandler.RESET_DERIVATIVES);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());

        Mockito.when(handlerMock.resetState(t, y)).thenReturn(EventHandler.RESET_STATE);
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());

        Mockito.when(handlerMock.resetState(t, y)).thenReturn(EventHandler.STOP);
        eventState.reset(t, y);
        assertTrue(eventState.isStop());

    }

}