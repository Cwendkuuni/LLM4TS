import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double[] y = new double[3];
        double tStart = 0.0;
        eventState.reinitializeBegin(tStart, y);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws Exception {
        EventHandler handler = new DummyEventHandlerWithException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double[] y = new double[3];
        double tStart = 0.0;
        eventState.reinitializeBegin(tStart, y);
    }

    @Test
    public void testEvaluateStep() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedTime(0.0);

        assertEquals(false, eventState.evaluateStep(interpolator));

        double t1 = 0.1;
        interpolator.setInterpolatedTime(t1);

        handler.resetState(0.0, new double[3]);

        double g0 = handler.g(0.0, new double[3]);
        eventState.reinitializeBegin(0.0, new double[3]);

        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);

        // test with negative step
        interpolator.setForward(false);
        interpolator.setInterpolatedTime(t1);

        handler.resetState(0.0, new double[3]);

        g0 = handler.g(0.0, new double[3]);
        eventState.reinitializeBegin(0.0, new double[3]);

        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException()
            throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandlerWithDerivativeException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedTime(0.0);

        assertEquals(false, eventState.evaluateStep(interpolator));
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException()
            throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandlerWithEventException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedTime(0.0);

        assertEquals(false, eventState.evaluateStep(interpolator));
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException()
            throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandlerWithConvergenceException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedTime(0.0);

        assertEquals(false, eventState.evaluateStep(interpolator));
    }

    @Test
    public void testStepAccepted() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double[] y = new double[3];
        double t = 0.1;
        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
        assertEquals(eventState.isG0Positive(), handler.isGPositive(t, y));

        if (eventState.isPendingEvent()) {
            assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
            assertEquals(eventState.getPendingEventTime(), handler.getEventTime(y), 0.0);
        } else {
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        }
    }

    @Test
    public void testStop() {
        EventHandler handler = new DummyEventHandlerWithStop();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double t = 0.1;
        double[] y = new double[3];
        eventState.stepAccepted(t, y);

        assertTrue(eventState.reset(t, y));

        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertFalse(eventState.isPendingEvent());

        if (eventState.isG0Positive()) {
            assertTrue(handler.isGPositive(t, y));
        } else {
            assertFalse(handler.isGPositive(t, y));
        }
    }

}

class DummyEventHandler implements EventHandler {
    @Override
    public double g(double t, double[] y) {
        return Math.sin(t) * Math.sin(Math.PI * y[0]);
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }
}

class DummyEventHandlerWithException implements EventHandler {
    @Override
    public double g(double t, double[] y) throws DerivativeException {
        throw new DerivativeException("Test");
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }
}

class DummyEventHandlerWithDerivativeException implements EventHandler {
    @Override
    public double g(double t, double[] y) {
        throw new DerivativeException("Test");
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }
}

class DummyEventHandlerWithEventException implements EventHandler {
    @Override
    public double g(double t, double[] y) throws EventException {
        throw new EventException("Test");
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }
}

class DummyEventHandlerWithConvergenceException implements EventHandler {
    @Override
    public double g(double t, double[] y) {
        return Math.sin(t) * Math.sin(Math.PI * y[0]);
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }

    @Override
    public double getEventTime(double[] y) {
        throw new ConvergenceException("Test");
    }
}

class DummyEventHandlerWithStop implements EventHandler {
    @Override
    public double g(double t, double[] y) {
        return Math.sin(t) * Math.sin(Math.PI * y[0]);
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }

    @Override
    public int getNextAction(double[] y) {
        return EventHandler.STOP;
    }
}