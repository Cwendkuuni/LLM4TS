import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    private EventHandler mockEventHandler;
    private EventState eventState;

    @Before
    public void setUp() {
        mockEventHandler = Mockito.mock(EventHandler.class);
        eventState = new EventState(mockEventHandler, 0.1, 0.001, 10);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(eventState);
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0);
        assertEquals(0.001, eventState.getConvergence(), 0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertFalse(eventState.pendingEvent());
        assertNull(eventState.pendingEventTime());
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction());
    }

    /**
     * Test reinitializeBegin.
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        double[] state = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(0.0, state);

        assertEquals(0.0, eventState.getT0(), 0);
        assertEquals(mockEventHandler.g(0.0, state), eventState.getG0(), 0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.pendingEvent());
        assertNull(eventState.pendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction());

        Mockito.verify(mockEventHandler).g(Mockito.eq(0.0), Mockito.eq(state));
    }

    /**
     * Test evaluateStep.
     */
    @Test(expected = DerivativeException.class)
    public void testEvaluateStepDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.isForward()).thenReturn(true);
        Mockito.when(interpolator.getCurrentTime()).thenReturn(1.0);
        Mockito.when(interpolator.getInterpolatedState()).thenThrow(new DerivativeException());

        eventState.evaluateStep(interpolator);
    }

    /**
     * Test evaluateStep EventException.
     */
    @Test(expected = EventException.class)
    public void testEvaluateStepEventException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.isForward()).thenReturn(true);
        Mockito.when(interpolator.getCurrentTime()).thenReturn(1.0);
        Mockito.when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.0, 2.0});
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenThrow(new EventException());

        eventState.evaluateStep(interpolator);
    }

    /**
     * Test evaluateStep ConvergenceException.
     */
    @Test
    public void testEvaluateStepConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.isForward()).thenReturn(true);
        Mockito.when(interpolator.getCurrentTime()).thenReturn(1.0);
        Mockito.when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.0, 2.0});
        Mockito.when(mockEventHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(Math.random());
        Mockito.when(mockEventHandler.eventOccurred(Mockito.anyDouble(), Mockito.any(double[][].class), Mockito.anyBoolean()))
            .thenReturn(EventHandler.RESET_DERIVIVATES);

        eventState.evaluateStep(interpolator);

        ArgumentCaptor<Double> tArgCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<double[]> yArgCaptor = ArgumentCaptor.forClass(double[].class);
        Mockito.verify(mockEventHandler).eventOccurred(tArgCaptor.capture(), yArgCaptor.capture(), Mockito.eq(false));
        assertEquals(1.0, tArgCaptor.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 2.0}, yArgCaptor.getValue(), 0.001);

        assertEquals(EventHandler.RESET_DERIVIVATES, eventState.nextAction());
    }

    /**
     * Test getEventTime.
     */
    @Test
    public void testGetEventTime() {
        double[] state = new double[]{1.0, 2.0};
        eventState.pendingEventTime = 1.0;
        eventState.previousEventTime = 0.5;

        assertEquals(1.0, eventState.getEventTime(), 0.001);
    }

    /**
     * Test stepAccepted.
     */
    @Test
    public void testStepAccepted() throws EventException {
        double[] state = new double[]{1.0, 2.0};

        eventState.stepAccepted(1.0, state);

        assertEquals(1.0, eventState.getT0(), 0.001);
        assertEquals(mockEventHandler.g(1.0, state), eventState.getG0(), 0.001);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.pendingEvent());
        assertNull(eventState.pendingEventTime());
        assertEquals(1.0, eventState.getPreviousEventTime(), 0.001);
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction());

        Mockito.verify(mockEventHandler).g(Mockito.eq(1.0), Mockito.eq(state));
    }

    /**
     * Test stop.
     */
    @Test
    public void testStop() {
        eventState.nextAction = EventHandler.STOP;

        assertTrue(eventState.stop());
    }

    /**
     * Test reset.
     */
    @Test
    public void testReset() throws EventException {
        double[] state = new double[]{1.0, 2.0};
        double t = 1.0;

        eventState.reset(t, state);

        assertNull(eventState.pendingEvent());
        assertNull(eventState.pendingEventTime());
        assertEquals(t, eventState.getT0());
        assertEquals(state, eventState.getEventHandler().getState());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction());

        Mockito.verify(mockEventHandler).resetState(t, state);
    }

}