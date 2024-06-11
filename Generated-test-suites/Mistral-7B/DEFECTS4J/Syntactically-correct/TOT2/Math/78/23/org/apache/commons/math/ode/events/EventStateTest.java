import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class EventStateTest {

    private EventState eventState;
    private EventHandler mockEventHandler;
    private StepInterpolator mockInterpolator;

    @Before
    public void setUp() {
        mockEventHandler = mock(EventHandler.class);
        mockInterpolator = mock(StepInterpolator.class);
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler());
        assertNull(eventState.getEventHandler());
        assertNull(eventState.getEventHandler());
        assertNull(eventState.getEventHandler());
        assertNull(eventState.getEventHandler());
        assertNull(eventState.getEventHandler());
        assertNull(eventState.getEventHandler());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        verify(mockEventHandler, times(1)).g(eq(tStart), eq(yStart));
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(yStart, eventState.getY(), assertArrayEquals(true));
        assertEquals(1.0, eventState.isG0Positive(), 0.0);
    }

    @Test
    public void testEvaluateStep_EventOccurs_StepRejected() throws DerivativeException, EventException, ConvergenceException {
        when(mockEventHandler.g(eq(anyDouble()), eq(anyDoubleArray()))).thenReturn(1.0).thenReturn(-1.0);

        eventState.evaluateStep(mockInterpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(-1.0, eventState.getPendingEventTime(), 0.0);
        assertEquals(-1.0, eventState.getPreviousEventTime(), 0.0);
        assertEquals(StepInterpolator.Direction.FORWARD, eventState.isForward());
        assertEquals(true, eventState.isIncreasing());

        eventState.stepAccepted(1.1, new double[]{1.1, 1.1});
        verify(mockEventHandler, times(2)).g(anyDouble(), anyDoubleArray());
        verify(mockEventHandler, times(1)).eventOccurred(eq(1.1), anyDoubleArray(), anyBoolean());
        verifyNoMoreInteractions(mockEventHandler);
        verifyNoMoreInteractions(mockInterpolator);

        assertEquals(1.1, eventState.getT0(), 0.0);
        assertEquals(new double[]{1.1, 1.1}, eventState.getY(), assertArrayEquals(true));
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(StepInterpolator.Direction.FORWARD, eventState.isForward());
        assertEquals(false, eventState.isIncreasing());
    }

    @Test
    public void testEvaluateStep_EventDoesntOccur_StepAccepted() throws DerivativeException, EventException, ConvergenceException {
        when(mockEventHandler.g(eq(anyDouble()), eq(anyDoubleArray()))).thenReturn(1.0).thenReturn(1.0);

        eventState.evaluateStep(mockInterpolator);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(StepInterpolator.Direction.FORWARD, eventState.isForward());
        assertEquals(true, eventState.isIncreasing());
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStep_ConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        when(mockEventHandler.g(eq(anyDouble()), eq(anyDoubleArray()))).thenThrow(new ConvergenceException("ConvergenceException"));

        eventState.evaluateStep(mockInterpolator);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStep_DerivativeException() throws DerivativeException, EventException, ConvergenceException {
        when(mockInterpolator.getInterpolatedState()).thenThrow(new DerivativeException("DerivativeException"));

        eventState.evaluateStep(mockInterpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStep_EventException() throws DerivativeException, EventException, ConvergenceException {
        when(mockEventHandler.g(eq(anyDouble()), eq(anyDoubleArray()))).thenThrow(new EventException("EventException"));

        eventState.evaluateStep(mockInterpolator);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 1.1;
        double[] y = new double[]{1.1, 1.1};
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(y, eventState.getY(), assertArrayEquals(true));
        assertEquals(1.1, eventState.isG0Positive(), 0.0);
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 1.1;
        double[] y = new double[]{1.1, 1.1};
        eventState.setNextAction(EventHandler.RESET_STATE);
        assertTrue(eventState.reset(t, y));
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(y, eventState.getY(), assertArrayEquals(true));
        assertEquals(1.1, eventState.isG0Positive(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(StepInterpolator.Direction.FORWARD, eventState.isForward());
        assertEquals(false, eventState.isIncreasing());
    }

    @Test
    public void testReset_ResetDerivatives() throws EventException {
        double t = 1.1;
        double[] y = new double[]{1.1, 1.1};
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertTrue(eventState.reset(t, y));
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(y, eventState.getY(), assertArrayEquals(true));
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(StepInterpolator.Direction.FORWARD, eventState.isForward());
        assertEquals(false, eventState.isIncreasing());
        // Derivatives should be reseted, not tested here
    }

    @Test
    public void testGetEventTime() {
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(1.1);
        assertEquals(1.1, eventState.getEventTime(), 0.0);
    }
}