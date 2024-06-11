package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.events.StepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaFehlberg78StepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaFehlberg85StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for the {@code EventState} class.
 */
public class EventStateTest {

    /** The event handler to be used for testing. */
    private EventHandler handler;

    /** The event state to be tested. */
    private EventState state;

    /**
     * Sets up the test environment by creating a mock event handler
     * and an event state based on this handler.
     * @throws Exception if the event state cannot be created
     */
    @Before
    public void setUp() throws Exception {
        handler = new MockEventHandler();
        state = new EventState(handler, 0.001, 1e-9, 10);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(state);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.001, state.getMaxCheckInterval(), 1e-9);
        assertEquals(1e-9, state.getConvergence(), 1e-9);
        assertEquals(10, state.getMaxIterationCount());
        assertNull(state.getEventHandler().getState());
        assertNull(state.getEventHandler().getStateDerivatives());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
        assertNull(state.getEventTime());
    }

    /**
     * Test the {@code reinitializeBegin} method.
     * @throws Exception if the event handler value cannot be evaluated
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        double tStart = 1.0;
        double[] yStart = new double[] { 1.0, 2.0 };
        state.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, state.getT0(), 1e-9);
        assertEquals(handler.g(tStart, yStart), state.getG0(), 1e-9);
    }

    /**
     * Test the {@code evaluateStep} method.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testEvaluateStep() throws Exception {
        StepInterpolator interpolator = null;

        // Test with a step interpolator that cannot compute the
        // switching function anywhere within the step.
        try {
            interpolator = new RungeKuttaFehlberg78StepInterpolator(0.001);
            state.evaluateStep(interpolator);
            assertFalse("An exception should have been thrown", true);
        } catch (DerivativeException e) {
            // expected
        }

        // Test with a step interpolator that can compute the
        // switching function but cannot evaluate the event
        // handler value at the beginning of the step.
        try {
            interpolator = new RungeKuttaFehlberg85StepInterpolator(0.001);
            state.reinitializeBegin(0.0, new double[] { 0.0 });
            state.evaluateStep(interpolator);
            assertFalse("An exception should have been thrown", true);
        } catch (EventException e) {
            // expected
        }

        // Test with a step interpolator that can compute the
        // switching function and evaluate the event handler
        // value at the beginning of the step but there is no
        // event during the step.
        try {
            interpolator = new RungeKuttaFehlberg85StepInterpolator(0.001);
            state.reinitializeBegin(0.0, new double[] { 0.0 });
            interpolator.setInterpolatedState(new double[] { 0.0, 0.0 });
            boolean result = state.evaluateStep(interpolator);
            assertFalse("An event was expected but none occurred", result);
            assertEquals(EventHandler.CONTINUE, state.getNextAction());
            assertEquals(Double.NaN, state.getEventTime());
        } catch (DerivativeException e) {
            throw new RuntimeException(e);
        }

        // Test with a step interpolator that can compute the
        // switching function and evaluate the event handler
        // value at the beginning and end of the step, an
        // event occurs at the end of the step but before the
        // proposed step, the step should be rejected.
        try {
            state.reinitializeBegin(1.0, new double[] { 1.0, 1.0 });
            interpolator.setInterpolatedTime(1.1);
            interpolator.setInterpolatedState(new double[] { 1.1, 1.1 });
            boolean result = state.evaluateStep(interpolator);
            assertTrue("The step should have been rejected", result);
            assertEquals(EventHandler.STOP, state.getNextAction());
            assertEquals(1.1, state.getEventTime(), 1e-9);
        } catch (DerivativeException e) {
            throw new RuntimeException(e);
        }

        // Test with a step interpolator that can compute the
        // switching function and evaluate the event handler
        // value at the beginning and end of the step, an
        // event occurs at the end of the step but inside the
        // proposed step, the step should be rejected with an
        // error indicating that the event cannot be located.
        try {
            state.reinitializeBegin(1.0, new double[] { 1.0, 1.0 });
            interpolator.setInterpolatedTime(1.15);
            interpolator.setInterpolatedState(new double[] { 1.15, 1.15 });
            boolean result = state.evaluateStep(interpolator);
            assertTrue("The step should have been rejected", result);
            assertEquals(EventHandler.ERROR, state.getNextAction());
            assertNull(state.getEventTime());
        } catch (DerivativeException e) {
            throw new RuntimeException(e);
        } catch (ConvergenceException e) {
            throw new RuntimeException(e);
        }

        // Test with a step interpolator that can compute the
        // switching function and evaluate the event handler
        // value at the beginning and end of the step, an
        // event occurs at the end of the step but exactly at the
        // proposed step, the step should be accepted and end at
        // the event.
        try {
            state.reinitializeBegin(1.0, new double[] { 1.0, 1.0 });
            interpolator.setInterpolatedTime(1.1);
            interpolator.setInterpolatedState(new double[] { 1.1, 1.1 });
            boolean result = state.evaluateStep(interpolator);
            assertFalse("The step should have been accepted", result);
            assertEquals(EventHandler.CONTINUE, state.getNextAction());
            assertEquals(Double.NaN, state.getEventTime());
            state.stepAccepted(1.1, new double[] { 1.1, 1.1 });
            assertEquals(1.1, state.getT0(), 1e-9);
            assertEquals(handler.g(1.1, new double[] { 1.1, 1.1 }), state.getG0(), 1e-9);
            assertEquals(EventHandler.EVENT_REACHED, state.getNextAction());
            assertEquals(1.1, state.getEventTime(), 1e-9);
        } catch (DerivativeException e) {
            throw new RuntimeException(e);
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the {@code stepAccepted} method.
     * @throws Exception if the event handler value cannot be evaluated
     */
    @Test
    public void testStepAccepted() throws Exception {
        double t = 1.0;
        double[] y = new double[] { 1.0, 1.0 };
        state.stepAccepted(t, y);
        assertEquals(t, state.getT0(), 1e-9);
        assertEquals(handler.g(t, y), state.getG0(), 1e-9);
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test the {@code reset} method.
     * @throws Exception if the event handler state cannot be reseted
     */
    @Test
    public void testReset() throws Exception {
        double t = 1.0;
        double[] y = new double[] { 1.0, 1.0 };
        state.reset(t, y);
        assertNull(state.getPreviousEventTime());
        assertFalse(state.pendingEvent());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

}

class MockEventHandler extends EventHandler {

    private double[] state;

    @Override
    public void resetState(double t, double[] y) {
        this.state = y;
    }

    @Override
    public int eventOccurred(double t, double[] y, boolean crossing) {
        return EventHandler.RESET_STATE;
    }

    public double g(double t, double[] y) {
        return y[0] * y[1];
    }

    public boolean pendingEvent() {
        return (state != null);
    }

}