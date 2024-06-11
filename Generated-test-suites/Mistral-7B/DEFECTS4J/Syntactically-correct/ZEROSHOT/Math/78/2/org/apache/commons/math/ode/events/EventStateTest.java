import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.EventException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handlerMock = mock(EventHandler.class);
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);

        assertEquals(handlerMock, eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, eventState.getConvergence(), 0.0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertFalse(eventState.hasPreviousEventTime());
        assertFalse(eventState.isForward());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        double tStart = 0.0;
        double[] yStart = new double[]{1.0};

        try {
            eventState.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, eventState.getT0(), 0.0);
            assertEquals(yStart[0], eventState.getG0(), 0.0);
            assertTrue(eventState.isG0Positive());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        when(interpolatorMock.isForward()).thenReturn(true);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.0);

        try {
            eventState.evaluateStep(interpolatorMock);
            assertFalse(eventState.isPendingEvent());
            assertFalse(eventState.hasPendingEventTime());
            assertTrue(eventState.isForward());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (DerivativeException | EventException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        when(interpolatorMock.getCurrentTime()).thenReturn(1.5);

        try {
            eventState.evaluateStep(interpolatorMock);
            assertTrue(eventState.isPendingEvent());
            assertEquals(1.5, eventState.getPendingEventTime(), 0.0);
            assertTrue(eventState.isForward());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (DerivativeException | EventException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        when(handlerMock.g(anyDouble(), anyArray())).thenThrow(new EventException());

        try {
            eventState.stepAccepted(1.7, new double[]{1.1});
            fail("EventException expected");
        } catch (EventException e) {
            // Expected
        }

        when(handlerMock.g(anyDouble(), anyArray())).thenReturn(Double.NaN);

        try {
            eventState.stepAccepted(1.9, new double[]{1.2});
            fail("EventException expected");
        } catch (EventException e) {
            // Expected
        }

        when(handlerMock.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(EventHandler.RESET_STATE);

        try {
            eventState.reset(2.1, new double[]{1.3});
            assertFalse(eventState.isPendingEvent());
            assertFalse(eventState.hasPendingEventTime());
            assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(handlerMock.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenReturn(EventHandler.RESET_DERIVATIVES);

        try {
            eventState.reset(2.3, new double[]{1.4});
            assertTrue(eventState.isPendingEvent());
            assertFalse(eventState.hasPendingEventTime());
            assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(handlerMock.resetState(anyDouble(), anyArray())).thenThrow(new EventException());

        try {
            eventState.reset(2.5, new double[]{1.5});
            fail("EventException expected");
        } catch (EventException e) {
            // Expected
        }
    }

}