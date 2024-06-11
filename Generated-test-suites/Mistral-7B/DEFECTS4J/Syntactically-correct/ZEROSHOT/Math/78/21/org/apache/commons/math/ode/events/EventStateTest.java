import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.EventException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.special.BrentSolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventStateTest {

    private EventState eventState;
    private EventHandler mockEventHandler;

    @BeforeEach
    public void setup() {
        mockEventHandler = new MockEventHandler();
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 50);
    }

    @Test
    public void testEventStateConstruction() {
        Assertions.assertNotNull(eventState);
        Assertions.assertNotNull(eventState.getEventHandler());
        Assertions.assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        Assertions.assertEquals(0.001, eventState.getConvergence(), 0.0);
        Assertions.assertEquals(50, eventState.getMaxIterationCount());
        Assertions.assertNull(eventState.getT0());
        Assertions.assertNull(eventState.getG0());
        Assertions.assertTrue(eventState.isG0Positive());
        Assertions.assertFalse(eventState.isPendingEvent());
        Assertions.assertNull(eventState.getPendingEventTime());
        Assertions.assertNull(eventState.getPreviousEventTime());
        Assertions.assertTrue(eventState.isForward());
        Assertions.assertTrue(eventState.isIncreasing());
        Assertions.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double[] testValues = new double[]{1.0, 2.0, 3.0};
        eventState.reinitializeBegin(0.0, testValues);
        Assertions.assertEquals(0.0, eventState.getT0(), 0.0);
        Assertions.assertEquals(mockEventHandler.g(0.0, testValues), eventState.getG0(), 0.0);
        Assertions.assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testEvaluateStep() throws EventException, DerivativeException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.0);
        interpolator.add(0.01, 1.0, 1.1);

        eventState.t0 = 0.0;
        eventState.g0 = mockEventHandler.g(0.0, new double[]{0.0, 0.0, 0.0});
        eventState.g0Positive = true;

        boolean result = eventState.evaluateStep(interpolator);
        Assertions.assertFalse(result);
        Assertions.assertNull(eventState.getPendingEventTime());
        Assertions.assertFalse(eventState.isPendingEvent());

        interpolator.setForward(false);
        result = eventState.evaluateStep(interpolator);
        Assertions.assertFalse(result);
        Assertions.assertNull(eventState.getPendingEventTime());
        Assertions.assertFalse(eventState.isPendingEvent());

        interpolator.setForward(true);
        interpolator.setCurrentTime(0.005);
        eventState.t0 = 0.0;
        eventState.g0 = mockEventHandler.g(0.0, new double[]{0.0, 0.0, 0.0});
        eventState.g0Positive = true;

        result = eventState.evaluateStep(interpolator);
        Assertions.assertFalse(result);
        Assertions.assertNull(eventState.getPendingEventTime());
        Assertions.assertFalse(eventState.isPendingEvent());

        interpolator.setCurrentTime(0.01);
        eventState.t0 = 0.005;
        eventState.g0 = mockEventHandler.g(0.005, new double[]{0.0, 0.0, 0.0});
        eventState.g0Positive = true;

        result = eventState.evaluateStep(interpolator);
        Assertions.assertTrue(result);
        Assertions.assertEquals(0.0055, eventState.getPendingEventTime(), 0.0001);
        Assertions.assertTrue(eventState.isPendingEvent());

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                try {
                    interpolator.setInterpolatedTime(t);
                    return mockEventHandler.g(t, interpolator.getInterpolatedState());
                } catch (DerivativeException e) {
                    throw new FunctionEvaluationException(e, t);
                } catch (EventException e) {
                    throw new FunctionEvaluationException(e, t);
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(0.001);
        solver.setMaximalIterationCount(50);

        double root = solver.solve(f, 0.005, 0.006);
        Assertions.assertEquals(eventState.getPendingEventTime(), root, 0.0001);

    }

    @Test
    public void testStepAccepted() throws EventException {
        double[] testValues = new double[]{1.0, 2.0, 3.0};
        eventState.stepAccepted(0.01, testValues);
        Assertions.assertEquals(0.01, eventState.getT0(), 0.0);
        Assertions.assertEquals(mockEventHandler.g(0.01, testValues), eventState.getG0(), 0.0);
        Assertions.assertEqual(eventState.isG0Positive(), mockEventHandler.isG0Positive(0.01, testValues));
        Assertions.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        if (eventState.isPendingEvent()) {
            Assertions.assertEquals(eventState.getPendingEventTime(), eventState.getPreviousEventTime(), 0.0);
            Assertions.assertFalse(eventState.isG0Positive());
            Assertions.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

            double[] eventValues = new double[]{1.1, 2.1, 3.1};
            mockEventHandler.eventOccurred(0.01, eventValues, false);

            Assertions.assertEquals(eventState.getPendingEventTime(), eventState.getPreviousEventTime(), 0.0);
            Assertions.assertTrue(eventState.isG0Positive());
            Assertions.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

            eventState.stepAccepted(0.011, eventValues);
            Assertions.assertEquals(0.011, eventState.getT0(), 0.0);
            Assertions.assertEquals(mockEventHandler.g(0.011, eventValues), eventState.getG0(), 0.0);
            Assertions.assertTrue(eventState.isG0Positive());
            Assertions.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        }
    }

    @Test
    public void testReset() throws EventException {
        double[] testValues = new double[]{1.0, 2.0, 3.0};
        eventState.stepAccepted(0.01, testValues);
        eventState.reset(0.012, testValues);
        Assertions.assertEquals(0.01, eventState.getT0(), 0.0);
        Assertions.assertEquals(mockEventHandler.g(0.01, testValues), eventState.getG0(), 0.0);
        Assertions.assertTrue(eventState.isG0Positive());
        Assertions.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        Assertions.assertFalse(eventState.isPendingEvent());
    }

}

class MockEventHandler implements EventHandler {

    private double g;

    public double g(double t, double[] y) {
        g = t;
        return g;
    }

    public void eventOccurred(double t, double[] y, boolean backward) {
        // empty implementation
    }

    public boolean isG0Positive(double t, double[] y) {
        return g > 0;
    }

}