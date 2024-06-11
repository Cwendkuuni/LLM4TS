import org.apache.commons.math3.exception.DerivativeException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFindFailureException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventStateTest {

    @Test
    public void testEventState() {
        // given
        EventHandler mockEventHandler = mock(EventHandler.class);
        double maxCheckInterval = 0.1;
        double convergence = 1e-5;
        int maxIterationCount = 10;
        EventState eventState = new EventState(mockEventHandler, maxCheckInterval, convergence, maxIterationCount);

        // when

        // default values
        assertEquals(Double.NaN, eventState.getEventHandler(), null);
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, eventState.getConvergence(), 0.0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount(), 0);
        assertEquals(Double.NaN, eventState.getT0(), 0.0);
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertFalse(eventState.hasPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // test reinitializeBegin
        double tStart = 0.0;
        double[] yStart = new double[1];
        try {
            eventState.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, eventState.getT0(), 0.0);
            assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.0);
            assertTrue(eventState.isG0Positive());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        // test evaluateStep
        StepInterpolator stepInterpolator = mock(StepInterpolator.class);
        when(stepInterpolator.isForward()).thenReturn(true);
        when(stepInterpolator.getCurrentTime()).thenReturn(1.0);
        try {
            when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(-1.0);
            boolean result = eventState.evaluateStep(stepInterpolator);
            assertTrue(result);
            assertEquals(1.0, eventState.getPendingEventTime(), 0.0);
        } catch (DerivativeException | EventException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(0.0);
        try {
            result = eventState.evaluateStep(stepInterpolator);
            assertFalse(result);
        } catch (DerivativeException | EventException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new EventException());
        try {
            result = eventState.evaluateStep(stepInterpolator);
            throw new AssertionError("Should have thrown an EventException");
        } catch (DerivativeException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new FunctionEvaluationException(new DerivativeException()));
        try {
            result = eventState.evaluateStep(stepInterpolator);
            throw new AssertionError("Should have thrown a DerivativeException");
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new MathRuntimeException(""));
        try {
            result = eventState.evaluateStep(stepInterpolator);
            throw new AssertionError("Should have thrown a MathRuntimeException");
        } catch (DerivativeException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        try {
            when(mockEventHandler.g(anyDouble(), anyArray())).thenAnswer(invocation -> {
                throw new NoBracketingException();
            });
            result = eventState.evaluateStep(stepInterpolator);
            throw new AssertionError("Should have thrown a NoBracketingException");
        } catch (DerivativeException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        try {
            when(mockEventHandler.g(anyDouble(), anyArray())).thenAnswer(invocation -> {
                throw new MaxIterationsExceededException();
            });
            result = eventState.evaluateStep(stepInterpolator);
            throw new AssertionError("Should have thrown a MaxIterationsExceededException");
        } catch (DerivativeException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        try {
            when(mockEventHandler.g(anyDouble(), anyArray())).thenAnswer(invocation -> {
                throw new RootFindFailureException();
            });
            result = eventState.evaluateStep(stepInterpolator);
            throw new AssertionError("Should have thrown a RootFindFailureException");
        } catch (DerivativeException | ConvergenceException e) {
            throw new RuntimeException(e);
        }

        // test getEventTime
       assertEquals(Double.NaN, eventState.getEventTime(), 0.0);

        // test stepAccepted
        double t = 1.0;
        double[] y = new double[1];
        try {
            eventState.stepAccepted(t, y);
            assertEquals(t, eventState.getT0(), 0.0);
            assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
            assertEquals(eventState.getG0Positive(), mockEventHandler.eventOccurred(t, y, false) == EventHandler.TRIGGER, false);
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        // test stop
        assertEquals(EventHandler.CONTINUE, eventState.stop());
        when(mockEventHandler.eventOccurred(t, y, false)).thenReturn(EventHandler.STOP);
        assertEquals(EventHandler.STOP, eventState.stop());

        // test reset
        t = 2.0;
        y = new double[1];
        try {
            eventState.reset(t, y);
            assertEquals(t, eventState.getT0(), 0.0);
            assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
            assertFalse(eventState.hasPendingEvent());
            assertFalse(eventState.hasPendingEventTime());
            assertFalse(eventState.hasPreviousEventTime());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(mockEventHandler.eventOccurred(t, y, false)).thenReturn(EventHandler.RESET_STATE);
        try {
            eventState.reset(t, y);
            assertTrue(mockEventHandler.resetState(t, y));
            assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(mockEventHandler.eventOccurred(t, y, false)).thenReturn(EventHandler.RESET_DERIVATIVES);
        try {
            eventState.reset(t, y);
            assertTrue(mockEventHandler.resetDerivatives(t, y));
            assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }
    }

}