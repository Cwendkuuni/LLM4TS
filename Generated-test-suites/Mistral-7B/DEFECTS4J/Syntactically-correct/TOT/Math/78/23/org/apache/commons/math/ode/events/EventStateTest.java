package org.apache.commons.math.ode.events;

import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link EventState}.
 */
public class EventStateTest {

    /** Event handler that always returns a constant value. */
    private static class ConstEventHandler implements EventHandler {

        private final double constant;

        public ConstEventHandler(final double constant) {
            this.constant = constant;
        }

        @Override
        public double g(final double t, final double[] y) throws EventException {
            return constant;
        }

        @Override
        public void eventOccurred(final double t, final double[] y, final boolean increasingFlag)
                throws EventException {
            // do nothing
        }

        @Override
        public void resetState(final double t, final double[] y) {
            // do nothing
        }

    }

    /** Event handler with a simple event. */
    private static class SimpleEventHandler implements EventHandler {

        @Override
        public double g(final double t, final double[] y) throws EventException {
            return Math.sin(t);
        }

        @Override
        public void eventOccurred(final double t, final double[] y, final boolean increasingFlag)
                throws EventException {
            if (increasingFlag) {
                throw new EventException("Event occurred at " + t);
            }
        }

        @Override
        public void resetState(final double t, final double[] y) {
            // do nothing
        }

    }

    /** Event state under test. */
    private EventState state;

    /** Event handler. */
    private EventHandler handler;

    /** Initialization. */
    @Before
    public void setUp() {
        handler = new SimpleEventHandler();
        state = new EventState(handler, 0.001, 1.0e-6, 50);
    }

    /**
     * Test a step with no event.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNoEvent() throws Exception {

        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[1];

        state.reinitializeBegin(0.0, y);

        interpolator.setInterpolatedTime(0.001);
        state.evaluateStep(interpolator);
        Assert.assertFalse(state.stop());

        interpolator.setInterpolatedTime(0.01);
        state.evaluateStep(interpolator);
        Assert.assertFalse(state.stop());

    }

    /**
     * Test a step with an event.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEvent() throws Exception {

        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[1];

        state.reinitializeBegin(0.0, y);

        interpolator.setInterpolatedTime(0.01);
        state.evaluateStep(interpolator);

        try {
            state.evaluateStep(interpolator);
            Assert.fail("Expected an EventException");
        } catch (EventException e) {
            // expected
        }

    }

    /**
     * Test a step with a reset event.
     * @throws Exception if an error occurs
     */
    @Test
    public void testResetEvent() throws Exception {

        StepInterpolator interpolator = new StepInterpolator();

        double[] y = new double[1];

        state.reinitializeBegin(0.0, y);

        interpolator.setInterpolatedTime(0.01);
        state.evaluateStep(interpolator);

        try {
            state.evaluateStep(interpolator);
            Assert.fail("Expected an EventException");
        } catch (EventException e) {
            // expected
        }

        handler = new ConstEventHandler(0.0);
        state.reset(interpolator.getCurrentTime(), y);
        Assert.assertFalse(state.stop());

        interpolator.setInterpolatedTime(0.02);
        state.evaluateStep(interpolator);
        Assert.assertFalse(state.stop());

    }

}