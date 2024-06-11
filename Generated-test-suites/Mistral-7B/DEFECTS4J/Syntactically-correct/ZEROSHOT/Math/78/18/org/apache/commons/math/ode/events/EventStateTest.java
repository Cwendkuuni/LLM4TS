import org.apache.commons.math3.exception.DerivativeException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.ode.EventException;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class EventStateTest {

    @Mock
    private EventHandler mockEventHandler;

    private EventState eventState;

    @Before
    public void setUp() {
        eventState = new EventState(mockEventHandler, 0.01, 0.01, 10);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertArrayEquals(yStart, eventState.getY0(), 0.0);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};

        when(mockEventHandler.g(anyDouble(), anyArray())).thenThrow(new EventException("Test error"));

        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator stepInterpolator = new StepInterpolator();
        double t1 = 2.0;
        double h = 0.1;

        when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(1.0, -1.0, 0.0);

        eventState.evaluateStep(stepInterpolator);

        assertTrue(eventState.isPendingEvent());
    }

    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator stepInterpolator = new StepInterpolator();
        double t1 = 2.0;
        double h = 0.1;

        when(mockEventHandler.g(anyDouble(), anyArray())).thenReturn(1.0, 1.0, 0.0);

        eventState.evaluateStep(stepInterpolator);

        assertFalse(eventState.isPendingEvent());
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator stepInterpolator = new StepInterpolator();
        double t1 = 2.0;
        double h = 0.1;

        when(mockEventHandler.g(anyDouble(), anyArray()))
            .thenThrow(new DerivativeException("Test error"));

        eventState.evaluateStep(stepInterpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepEventException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator stepInterpolator = new StepInterpolator();
        double t1 = 2.0;
        double h = 0.1;

        when(mockEventHandler.g(anyDouble(), anyArray()))
            .thenThrow(new EventException("Test error"));

        eventState.evaluateStep(stepInterpolator);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(mockEventHandler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(0.01, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(10, eventState.getMaxIterationCount());
    }

    @Test
    public void testGetT0() {
        double tStart = 1.0;

        eventState.reinitializeBegin(tStart, new double[]{1.0, 2.0});

        assertEquals(tStart, eventState.getT0(), 0.0);
    }

    @Test
    public void testGetG0() {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test
    public void testIsG0Positive() {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testIsPendingEvent() {
        eventState.setPendingEvent(true);

        assertTrue(eventState.isPendingEvent());
    }

    @Test
    public void testIsNotPendingEvent() {
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testGetPendingEventTime() {
        double pendingEventTime = 2.0;

        eventState.setPendingEventTime(pendingEventTime);

        assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testGetPreviousEventTime() {
        double previousEventTime = 1.0;

        eventState.setPreviousEventTime(previousEventTime);

        assertEquals(previousEventTime, eventState.getPreviousEventTime(), 0.0);
    }

    @Test
    public void testIsForward() {
        eventState.setForward(true);

        assertTrue(eventState.isForward());
    }

    @Test
    public void testIsNotForward() {
        eventState.setForward(false);

        assertFalse(eventState.isForward());
    }

    @Test
    public void testIsIncreasing() {
        eventState.setIncreasing(true);

        assertTrue(eventState.isIncreasing());
    }

    @Test
    public void testIsNotIncreasing() {
        eventState.setIncreasing(false);

        assertFalse(eventState.isIncreasing());
    }

    @Test
    public void testGetNextAction() {
        eventState.setNextAction(EventHandler.CONTINUE);

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testGetNextActionResetState() {
        eventState.setNextAction(EventHandler.RESET_STATE);

        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
    }

    @Test
    public void testGetNextActionResetDerivatives() {
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);

        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 2.0;
        double[] y = new double[]{1.0, 2.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getY0(), 0.0);
        assertTrue(eventState.isPendingEvent());
    }

    @Test
    public void testStepAcceptedNoPendingEvent() throws EventException {
        double t = 2.0;
        double[] y = new double[]{1.0, 2.0};

        eventState.stepAccepted(t, y);

        assertFalse(eventState.isPendingEvent());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithEventException() throws EventException {
        double t = 2.0;
        double[] y = new double[]{1.0, 2.0};

        when(mockEventHandler.eventOccurred(anyDouble(), anyArray(), anyBoolean())).thenThrow(new EventException("Test error"));

        eventState.stepAccepted(t, y);
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 2.0;
        double[] y = new double[]{1.0, 2.0};

        eventState.reset(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getY0(), 0.0);
        assertFalse(eventState.isPendingEvent());
    }

    @Test(expected = EventException.class)
    public void testResetWithException() throws EventException {
        double t = 2.0;
        double[] y = new double[]{1.0, 2.0};

        when(mockEventHandler.resetState(anyDouble(), anyArray())).thenThrow(new EventException("Test error"));

        eventState.reset(t, y);
    }

}