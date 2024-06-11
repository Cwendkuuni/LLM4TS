import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventStateTest {

    private EventHandler mockEventHandler;
    private EventState eventState;

    @Before
    public void setUp() {
        mockEventHandler = mock(EventHandler.class);
        eventState = new EventState(mockEventHandler, 0.001, 1e-6, 10);
    }

    @Test
    public void testEventStateInitialization() {
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.001, eventState.getMaxCheckInterval(), 0);
        Assert.assertEquals(1e-6, eventState.getConvergence(), 0);
        Assert.assertEquals(10, eventState.getMaxIterationCount());
        Assert.assertNull(eventState.gett0());
        Assert.assertNull(eventState.getg0());
        Assert.assertTrue(eventState.isg0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertNull(eventState.getPendingEventTime());
        Assert.assertNull(eventState.getPreviousEventTime());
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 5.0;
        double[] yStart = {1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        verify(mockEventHandler, times(1)).g(eq(tStart), isA(double[].class));
        Assert.assertEquals(tStart, eventState.gett0(), 0);
        Assert.assertEquals(eventState.getg0(), handler.g(tStart, yStart), 0);
        Assert.assertTrue(eventState.isg0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        double tStart = 5.0;
        double[] yStart = {1.0, 2.0};
        when(mockEventHandler.g(eq(tStart), isA(double[].class))).thenThrow(new EventException());
        eventState.reinitializeBegin(tStart, yStart);
        verify(mockEventHandler, times(1)).g(eq(tStart), isA(double[].class));
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(6.0);

        double t1 = 6.0;
        double gb = 2.0;

        when(mockEventHandler.g(eq(anyDouble()), isA(double[].class))).thenReturn(gb);

        boolean result = eventState.evaluateStep(interpolator);
        Assert.assertTrue(result);
        Assert.assertEquals(t1, eventState.gett0(), 0);
        Assert.assertEquals(gb, eventState.getg0(), 0);
        Assert.assertTrue(eventState.isg0Positive());
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(6.0, eventState.getPendingEventTime(), 0);
        Assert.assertEquals(5.0, eventState.getPreviousEventTime(), 0);
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 6.1;
        double[] y = {1.1, 2.1};

        eventState.stepAccepted(t, y);
        verify(mockEventHandler, times(1)).g(eq(t), isA(double[].class));
        Assert.assertEquals(t, eventState.gett0(), 0);
        Assert.assertEquals(eventState.getg0(), handler.g(t, y), 0);
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithException() throws EventException {
        double t = 6.1;
        double[] y = {1.1, 2.1};
        when(mockEventHandler.g(eq(t), isA(double[].class))).thenThrow(new EventException());
        eventState.stepAccepted(t, y);
        verify(mockEventHandler, times(1)).g(eq(t), isA(double[].class));
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 6.1;
        double[] y = {1.1, 2.1};

        eventState.setNextAction(EventHandler.RESET_STATE);
        eventState.reset(t, y);
        verify(mockEventHandler, times(1)).resetState(eq(t), isA(double[].class));
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertNull(eventState.getPendingEventTime());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        eventState.reset(t, y);
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertNull(eventState.getPendingEventTime());
        Assert.assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());

    }

    @Test(expected = EventException.class)
    public void testResetWithException() throws EventException {
        double t = 6.1;
        double[] y = {1.1, 2.1};
        when(mockEventHandler.resetState(eq(t), isA(double[].class))).thenThrow(new EventException());
        eventState.reset(t, y);
        verify(mockEventHandler, times(1)).resetState(eq(t), isA(double[].class));
    }

}