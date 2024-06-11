package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStateTest {

    private static final double EPSILON = 1.0E-6;
    private static final double MAX_CHECK_INTERVAL = 0.01;
    private static final double CONVERGENCE = 1.0E-6;
    private static final int MAX_ITERATION_COUNT = 10;

    private EventState eventState;
    private UnivariateFunction testFunction;
    private double[] testValues;
    private double testTime;

    @Before
    public void setUp() {
        testFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.exp(x) - 1;
            }
        };
        testValues = new double[]{1.0, 0.5, 2.0};
        eventState = new EventState(new MyEventHandler(), MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT);
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        eventState.reinitializeBegin(0.0, testValues);
        assertEquals(0.0, eventState.gett0(), EPSILON);
        assertEquals(Double.NaN, eventState.getg0(), EPSILON);
        assertEquals(true, eventState.isg0Positive());
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testEvaluateStep() throws Exception {
        StepInterpolator interpolator = new StepInterpolator(testFunction, 0.0, 0.1, 0.05);
        double t1 = interpolator.getCurrentTime();
        eventState.evaluateStep(interpolator);
        assertEquals(false, eventState.isPendingEvent());
        interpolator.nextStep();
        eventState.evaluateStep(interpolator);
        assertEquals(true, eventState.isPendingEvent());
        assertEquals(1.718281828459045, eventState.getPendingEventTime(), EPSILON);
    }

    @Test
    public void testStepAccepted() throws Exception {
        StepInterpolator interpolator = new StepInterpolator(testFunction, 0.0, 0.1, 0.05);
        eventState.reinitializeBegin(0.0, testValues);
        interpolator.setInterpolatedTime(0.3);
        eventState.stepAccepted(0.3, testValues);
        assertEquals(0.3, eventState.gett0(), EPSILON);
        assertEquals(-0.495394923928381, eventState.getg0(), EPSILON);
        assertEquals(false, eventState.isg0Positive());
        assertFalse(eventState.isPendingEvent());
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithException() throws Exception {
        StepInterpolator interpolator = new StepInterpolator(new MyExceptionFunction(), 0.0, 0.1, 0.05);
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testReset() throws Exception {
        StepInterpolator interpolator = new StepInterpolator(testFunction, 0.0, 0.1, 0.05);
        eventState.reinitializeBegin(0.0, testValues);
        interpolator.setInterpolatedTime(0.5);
        eventState.stepAccepted(0.5, testValues);
        eventState.reset(0.6, testValues);
        assertEquals(0.6, eventState.gett0(), EPSILON);
        assertEquals(6.127706564797821E-31, eventState.getg0(), EPSILON);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testStop() throws Exception {
        StepInterpolator interpolator = new StepInterpolator(testFunction, 0.0, 0.1, 0.05);
        eventState.reinitializeBegin(0.0, testValues);
        interpolator.setInterpolatedTime(0.1);
        eventState.evaluateStep(interpolator);
        eventState.setNextAction(EventHandler.STOP);
        assertFalse(eventState.isPendingEvent());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    private static class MyEventHandler implements EventHandler {

        private double eventTime = 1.0;

        public double g(double time, double[] state) throws EventException {
            return testFunction.value(time);
        }

        public void resetState(double time, double[] state) throws EventException {
            eventTime = time;
        }

        public int eventOccurred(double time, double[] state, boolean forward)
                throws EventException {
            if (Math.abs(time - eventTime) < CONVERGENCE) {
                setNextAction(EventHandler.RESET_STATE);
                return EventHandler.RESET_STATE;
            } else {
                setNextAction(EventHandler.STOP);
                return EventHandler.STOP;
            }
        }
    }

    private static class MyExceptionFunction implements UnivariateFunction {

        public double value(double x) throws FunctionEvaluationException {
            throw new MathRuntimeException("Exception function");
        }
    }

}