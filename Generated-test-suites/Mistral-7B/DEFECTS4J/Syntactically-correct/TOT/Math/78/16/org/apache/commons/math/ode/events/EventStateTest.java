package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EventState}.
 */
public class EventStateTest {

    /** Event handler mock. */
    private static class MockEventHandler implements EventHandler {

        private boolean triggered;

        /** {@inheritDoc} */
        @Override
        public double g(double t, double[] y) {
            return 0;
        }

        /** {@inheritDoc} */
        @Override
        public void resetState(double t, double[] y) {
            // empty
        }

        /** {@inheritDoc} */
        @Override
        public void eventOccurred(double t, double[] y, boolean isCrossing) {
            triggered = true;
        }

        /** {@inheritDoc} */
        @Override
        public int nextAction() {
            return EventHandler.CONTINUE;
        }

        /** {@inheritDoc} */
        @Override
        public boolean acceptState(double t, double[] y) {
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isResetStateOnAccept() {
            return false;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isResetDerivativesOnAccept() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isResetStateOnEvent() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isResetDerivativesOnEvent() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isCrossingEvent() {
            return false;
        }

        /** {@inheritDoc} */
        public double getConvergenceThreshold() {
            return 0;
        }

        /** {@inheritDoc} */
        public int getMaxIterationCount() {
            return 0;
        }

        /** {@inheritDoc} */
        public boolean isEventActive() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isContinuous() {
            return true;
        }

        /** {@inheritDoc} */
        public boolean isLocal() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isStateDependent() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isBracketing() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isLocalMinMax() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isTransversal() {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isSymmetric() {
            return false;
        }

        /** {@inheritDoc} */
        public double getValueAt(double t, double[] y) {
            return 0;
        }

        /** {@inheritDoc} */
        public UnivariateRealFunction getDerivative(double t, double[] y) {
            return null;
        }

        /** {@inheritDoc} */
        public double getIntervalEnd() {
            return Double.POSITIVE_INFINITY;
        }

        /** {@inheritDoc} */
        public double getIntervalStart() {
            return Double.NEGATIVE_INFINITY;
        }

        /** {@inheritDoc} */
        public boolean isActive(double t, double[] y) {
            return false;
        }

        /** {@inheritDoc} */
        public boolean isActiveValue(double t, double[] y) {
            return false;
        }

        /** Tests if the event handler has been triggered. */
        public boolean wasTriggered() {
            return triggered;
        }

        /** Resets the trigger state. */
        public void resetTriggered() {
            triggered = false;
        }

    }

    /** Event handler under test. */
    private EventHandler handler;

    /** Event state under test. */
    private EventState eventState;

    /** Step interpolator under test. */
    private StepInterpolator interpolator;

    /** Event handler mock. */
    private MockEventHandler mockEventHandler;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        handler = new MockEventHandler();
        mockEventHandler = new MockEventHandler();
        eventState = new EventState(handler, 0.001, 0.0001, 10);
        interpolator = new StepInterpolator();
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        // nothing to test
    }

    /**
     * Tests the {@link EventState#getEventHandler()} method.
     */
    @Test
    public void testGetEventHandler() {
        Assert.assertSame(handler, eventState.getEventHandler());
    }

    /**
     * Tests the {@link EventState#getMaxCheckInterval()} method.
     */
    @Test
    public void testGetMaxCheckInterval() {
        Assert.assertEquals(0.001, eventState.getMaxCheckInterval(), 0);
    }

    /**
     * Tests the {@link EventState#getConvergence()} method.
     */
    @Test
    public void testGetConvergence() {
        Assert.assertEquals(0.0001, eventState.getConvergence(), 0);
    }

    /**
     * Tests the {@link EventState#getMaxIterationCount()} method.
     */
    @Test
    public void testGetMaxIterationCount() {
        Assert.assertEquals(10, eventState.getMaxIterationCount());
    }

    /**
     * Tests the {@link EventState#reinitializeBegin(double, double[])} method.
     * @throws EventException if an error occurs
     */
    @Test
    public void testReinitializeBegin()
        throws EventException {
        double tStart = 0.1;
        double[] yStart = new double[] { 1.0, 2.0 };
        eventState.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, eventState.getT0(), 0);
        Assert.assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0);
    }

    /**
     * Tests the {@link EventState#evaluateStep(StepInterpolator)} method.
     * @throws DerivativeException if an error occurs
     * @throws ConvergenceException if an error occurs
     * @throws EventException if an error occurs
     */
    @Test
    public void testEvaluateStep()
        throws DerivativeException, ConvergenceException, EventException {
        double t1 = 0.2;
        interpolator.setInitialStep(0.1);
        interpolator.setInterpolatedTime(t1);
        handler.resetTriggered();

        eventState.evaluateStep(interpolator);

        Assert.assertFalse(handler.wasTriggered());
    }

    /**
     * Tests the {@link EventState#getEventTime()} method.
     */
    @Test
    public void testGetEventTime() {
        Assert.assertEquals(Double.NaN, eventState.getEventTime(), 0);
    }

    /**
     * Tests the {@link EventState#stepAccepted(double, double[])} method.
     * @throws EventException if an error occurs
     */
    @Test
    public void testStepAccepted()
        throws EventException {
        double t = 0.3;
        double[] y = new double[] { 1.0, 2.0 };
        eventState.stepAccepted(t, y);
        Assert.assertEquals(t, eventState.getT0(), 0);
        Assert.assertEquals(handler.g(t, y), eventState.getG0(), 0);
    }

    /**
     * Tests the {@link EventState#stop()} method.
     */
    @Test
    public void testStop() {
        Assert.assertFalse(eventState.stop());
    }

    /**
     * Tests the {@link EventState#reset(double, double[])} method.
     * @throws EventException if an error occurs
     */
    @Test
    public void testReset()
        throws EventException {
        double t = 0.4;
        double[] y = new double[] { 1.0, 2.0 };
        eventState.reset(t, y);
        Assert.assertFalse(eventState.pendingEvent);
        Assert.assertEquals(Double.NaN, eventState.getPendingEventTime(), 0);
    }

}