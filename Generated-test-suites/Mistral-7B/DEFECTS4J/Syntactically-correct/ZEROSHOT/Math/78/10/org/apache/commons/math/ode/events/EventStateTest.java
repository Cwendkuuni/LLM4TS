package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EventState}
 */
public class EventStateTest {

    /** Event handler mock. */
    private MockEventHandler eventHandler;

    /** Event state under test. */
    private EventState eventState;

    /** Step interpolator for testing. */
    private StepInterpolator stepInterpolator;

    /**
     * Sets up the test.
     * @throws Exception in case of errors
     */
    @Before
    public void setUp() throws Exception {
        // event handler
        eventHandler = new MockEventHandler();

        // event state
        eventState = new EventState(eventHandler, 0.01, 0.001, 10);

        // step interpolator
        stepInterpolator = new StepInterpolator();
    }

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(eventState);
        assertEquals(eventHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getT0());
        assertNull(eventState.getG0());
        assertFalse(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Tests {@link EventState#reinitializeBegin(double, double[])}.
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        double[] state = new double[] { 0.0, 1.0 };

        eventState.reinitializeBegin(0.0, state);
        assertEquals(0.0, eventState.getT0(), 0.0);
        assertEquals(eventHandler.g(0.0, state), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    /**
     * Tests {@link EventState#evaluateStep(StepInterpolator)}.
     */
    @Test
    public void testEvaluateStep() throws Exception {
        eventHandler.setG(new UnivariateFunction() {
            @Override
            public double value(double t) {
                return Math.sin(t);
            }
        });

        double[] state = new double[] { 0.0, 1.0 };

        stepInterpolator.setStepFunction(new StepHandler() {
            @Override
            public double getStep(double t, double[] y, double[] yPrime) {
                return 0.1;
            }
        });

        eventState.reinitializeBegin(0.0, state);

        eventState.evaluateStep(stepInterpolator);
        assertEquals(0.01, eventState.getT0(), 0.0);
        assertEquals(Math.sin(0.01), eventState.getG0(), 0.0);
        assertTrue(Math.sin(0.01) >= 0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        stepInterpolator.setCurrentTime(0.015);
        eventState.evaluateStep(stepInterpolator);
        assertTrue(Math.sin(0.015) < 0);
        assertTrue(eventState.isPendingEvent());
        assertEquals(0.01485, eventState.getPendingEventTime(), 0.001);
        assertEquals(EventHandler.TRIGGER, eventState.getNextAction());
    }

    /**
     * Tests {@link EventState#getEventTime()}.
     */
    @Test
    public void testGetEventTime() throws Exception {
        stepInterpolator.setCurrentTime(0.02);
        eventState.setPendingEventTime(0.015);
        assertEquals(0.015, eventState.getEventTime(), 0.001);
    }

    /**
     * Tests {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void testStepAccepted() throws Exception {
        double[] state = new double[] { 0.0, 1.0 };

        eventState.stepAccepted(0.02, state);
        assertEquals(0.02, eventState.getT0(), 0.0);
        assertEquals(eventHandler.g(0.02, state), eventState.getG0(), 0.0);
        assertTrue(eventHandler.g(0.02, state) >= 0);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /**
     * Tests {@link EventState#stop()}.
     */
    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    /**
     * Tests {@link EventState#reset(double, double[])}.
     */
    @Test
    public void testReset() throws Exception {
        eventState.setPendingEventTime(0.015);
        eventState.setNextAction(EventHandler.RESET_STATE);

        double[] state = new double[] { 0.0, 1.0 };
        eventState.reset(0.02, state);
        assertNull(eventState.getPendingEventTime());
        assertEquals(0.02, eventState.getT0(), 0.0);
        assertEquals(eventHandler.g(0.02, state), eventState.getG0(), 0.0);
        assertTrue(eventHandler.g(0.02, state) >= 0);
    }

}