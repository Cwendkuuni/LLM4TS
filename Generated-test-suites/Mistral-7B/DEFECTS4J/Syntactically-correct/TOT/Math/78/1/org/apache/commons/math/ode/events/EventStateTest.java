package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class EventStateTest {

    private EventHandler mockHandler;
    private EventState eventState;

    @Before
    public void setUp() {
        mockHandler = mock(EventHandler.class);
        eventState = new EventState(mockHandler, 0.01, 1e-10, 10) {
            @Override
            public boolean evaluateStep(StepInterpolator interpolator)
                    throws DerivativeException, EventException, ConvergenceException {
                return false;
            }

            @Override
            public void reinitializeBegin(double tStart, double[] yStart)
                    throws EventException {
                // do nothing
            }

            @Override
            public void stepAccepted(double t, double[] y)
                    throws EventException {
                // do nothing
            }
        };
    }

    @Test
    public void testInitialization() {
        assertNotNull(eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(1e-10, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0, -1.0, 0.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(EventHandler.STOP);

        eventState.evaluateStep(interpolator);
        assertTrue(eventState.hasPendingEvent());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testEventHandlerExceptionDuringStep() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(mockHandler.g(anyDouble(), anyArray())).thenThrow(new EventException());

        eventState.evaluateStep(interpolator);
        assertTrue(eventState.hasPendingEvent());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testMaxIterationCountExceeded() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0, -1.0, 0.0);
        when(mockHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(EventHandler.STOP);

        for (int i = 0; i < eventState.getMaxIterationCount() + 1; i++) {
            when(mockHandler.g(anyDouble(), anyArray())).thenReturn(1.0, -1.0, 0.0);
            eventState.evaluateStep(interpolator);
        }

        assertTrue(eventState.hasPendingEvent());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() {
        final double[] y = new double[1];
        eventState.stepAccepted(1.0, y);
        assertNotNull(eventState.getT0());
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(-1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testResetState() {
        final double t = 2.0;
        final double[] y = new double[1];
        eventState.reset(t, y);
        assertNotNull(eventState.getT0());
        assertEquals(t, eventState.getT0(), 0.0);
        assertNotNull(eventState.getEventHandler().getState());
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
    }

    @Test
    public void testResetDerivatives() {
        final double t = 2.0;
        final double[] y = new double[1];
        eventState.reset(t, y);
        assertNotNull(eventState.getEventHandler().getDerivatives());
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

    @Test
    public void testStepAcceptedWithPendingEvent() {
        final double[] y = new double[1];
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(1.0);
        eventState.setPreviousEventTime(0.0);
        eventState.stepAccepted(1.0, y);
        assertNull(eventState.getT0());
        assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
        assertFalse(eventState.hasPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }
}