package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.EventException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /** Event handler mock. */
    private static final EventHandler handler = new EventHandler() {
        @Override
        public double g(double t, double[] y) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public void resetState(double t, double[] y) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean forwardCrossed) {
            throw new UnsupportedOperationException("not implemented");
        }
    };

    /** Event state instance. */
    private EventState eventState;

    /** Step interpolator mock. */
    private StepInterpolator interpolator;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        eventState = new EventState(handler, 0.01, 1e-7, 32);
        interpolator = new StepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public void setInterpolatedTime(double time) {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public double integrate(UnivariateRealFunction f, double t0, double y0, double h)
                throws DerivativeException, PrecisionException, NoBracketingException,
                       MaxEvalException, UnsupportedOperationException {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public double[] getInterpolatedState() {
                throw new UnsupportedOperationException("not implemented");
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(eventState);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.01, eventState.getMaxCheckInterval(), 1e-7);
        Assert.assertEquals(1e-7, eventState.getConvergence(), 1e-7);
        Assert.assertEquals(32, eventState.getMaxIterationCount());
        Assert.assertTrue(Double.isNaN(eventState.getT0()));
        Assert.assertTrue(Double.isNaN(eventState.getG0()));
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertTrue(Double.isNaN(eventState.getPreviousEventTime()));
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test reinitializeBegin.
     * @throws EventException on error
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(0.0, new double[] { 0.0 });
        Assert.assertEquals(0.0, eventState.getT0(), 1e-7);
        Assert.assertEquals(Double.NaN, eventState.getG0(), 1e-7);
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertTrue(Double.isNaN(eventState.getPreviousEventTime()));
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test evaluateStep.
     * @throws EventException on error
     */
    @Test
    public void testEvaluateStep() throws EventException {
        interpolator.setInterpolatedTime(0.1);
        eventState.setInterpolator(interpolator);
        Assert.assertFalse(eventState.evaluateStep(interpolator));
        interpolator.setInterpolatedTime(0.2);
        eventState.setInterpolator(interpolator);
        Assert.assertTrue(eventState.evaluateStep(interpolator));
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(0.2, eventState.getPendingEventTime(), 1e-7);
        Assert.assertFalse(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    /**
     * Test getEventTime.
     */
    @Test
    public void testGetEventTime() {
        eventState.setPendingEventTime(0.2);
        Assert.assertEquals(0.2, eventState.getEventTime(), 1e-7);
    }

    /**
     * Test stepAccepted.
     * @throws EventException on error
     */
    @Test
    public void testStepAccepted() throws EventException {
        interpolator.setInterpolatedTime(0.2);
        eventState.setInterpolator(interpolator);
        eventState.evaluateStep(interpolator);
        Assert.assertEquals(0.2, eventState.getT0(), 1e-7);
        Assert.assertEquals(0.0, eventState.getG0(), 1e-7);
        Assert.assertFalse(eventState.isG0Positive());
        Assert.assertTrue(eventState.isPendingEvent());
        eventState.stepAccepted(0.2, new double[] { 0.0 });
        Assert.assertEquals(0.2, eventState.getT0(), 1e-7);
        Assert.assertEquals(0.0, eventState.getG0(), 1e-7);
        Assert.assertFalse(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Test stop.
     */
    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        Assert.assertTrue(eventState.stop());
    }

    /**
     * Test reset.
     * @throws EventException on error
     */
    @Test
    public void testReset() throws EventException {
        eventState.setPendingEvent(true);
        eventState.setPendingEventTime(0.2);
        Assert.assertTrue(eventState.pendingEvent);
        Assert.assertEquals(0.2, eventState.getPendingEventTime(), 1e-7);
        Assert.assertTrue(eventState.reset(0.3, new double[] { 0.0 }));
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
    }

}