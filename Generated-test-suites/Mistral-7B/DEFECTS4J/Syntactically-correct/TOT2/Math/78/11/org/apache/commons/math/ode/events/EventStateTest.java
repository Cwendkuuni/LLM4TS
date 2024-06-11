import org.apache.commons.math3.exception.DerivativeException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.UninitializedPropertyException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class EventStateTest {

    private EventState state;
    private EventHandler handler;

    @Before
    public void setUp() {
        handler = new MockEventHandler();
        state = new EventState(handler, 0.1, 0.01, 50);
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        double tStart = 0.0;
        double[] yStart = new double[]{0.0, 1.0};
        state.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, state.getT0(), 0.0);
        assertEquals(yStart, state.getY0(), 0.0);
    }

    @Test
    public void testEvaluateStepWithEventDuringStep() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.evaluateStep(interpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(0.1, state.getPendingEventTime(), 0.001);
    }

    @Test
    public void testEvaluateStepWithoutEvent() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.evaluateStep(interpolator);
        assertFalse(state.isPendingEvent());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        interpolator.setFunction(new MockFunction() {
            @Override
            public double value(double t, double[] y) {
                throw new DerivativeException("Test DerivativeException");
            }
        });
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        interpolator.setFunction(new MockFunction() {
            @Override
            public double value(double t, double[] y) {
                throw new EventException("Test EventException");
            }
        });
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        interpolator.setFunction(new MockFunction() {
            @Override
            public double value(double t, double[] y) {
                return Math.sin(t) / Math.sqrt(t);
            }
        });
        interpolator.setSolver(new BrentSolver());
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.evaluateStep(interpolator);
    }

    @Test
    public void testStepAccepted() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.evaluateStep(interpolator);
        state.stepAccepted(0.1, new double[]{0.1, 0.1});
        assertEquals(0.1, state.getT0(), 0.0);
        assertEquals(new double[]{0.1, 0.1}, state.getY0(), 0.0);
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    @Test
    public void testStop() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());
    }

    @Test
    public void testReset() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.setNextAction(EventHandler.RESET_STATE);
        state.evaluateStep(interpolator);
        state.reset(0.1, new double[]{0.1, 0.1});
        assertFalse(state.isPendingEvent());
    }

    @Test
    public void testResetDerivatives() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        state.evaluateStep(interpolator);
        state.reset(0.1, new double[]{0.1, 0.1});
        assertTrue(state.isPendingEvent());
    }

    @Test(expected = UninitializedPropertyException.class)
    public void testResetWithoutPendingEvent() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.add(0.0, 0.0, 0.1);
        state.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        state.reset(0.1, new double[]{0.1, 0.1});
    }

}

class MockEventHandler implements EventHandler {

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) {
        setNextAction(EventHandler.RESET_STATE);
    }

    @Override
    public double g(double t, double[] y) {
        return 0.0;
    }

    @Override
    public void resetState(double t, double[] y) {

    }

}

class MockFunction implements UnivariateRealFunction {

    @Override
    public double value(double t) {
        return 0.0;
    }

    @Override
    public double value(double t, double[] y) {
        throw new UninitializedPropertyException("y not initialized");
    }

}