import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class EventStateTest {

    private EventHandler mockEventHandler;
    private EventState eventState;

    @BeforeMethod
    public void setup() {
        mockEventHandler = Mockito.mock(EventHandler.class);
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 10);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(yStart, eventState.getY0(), 0.0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.isForward()).thenReturn(true);
        Mockito.when(interpolator.getCurrentTime()).thenReturn(0.5);

        eventState.evaluateStep(interpolator);
        assertFalse(eventState.isPendingEvent());

        Mockito.when(interpolator.isForward()).thenReturn(false);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 0.5;
        double[] y = new double[]{3.0, 4.0};
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(y, eventState.getY0(), 0.0);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 1.0;
        double[] y = new double[]{5.0, 6.0};
        eventState.setNextAction(EventHandler.RESET_STATE);
        assertFalse(eventState.reset(t, y));

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertTrue(eventState.reset(t, y));
    }

}