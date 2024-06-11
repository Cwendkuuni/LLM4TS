package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test class for {@link EventState}.
 *
 */
public class EventStateTest {

    /**
     * Test of constructor.
     */
    @Test
    public void testConstructor() {
        MockEventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        assertNull(state.getEventHandler());
        assertEquals(0.01, state.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, state.getConvergence(), 0.0);
        assertEquals(10, state.getMaxIterationCount());
        assertEquals(Double.NaN, state.getT0());
        assertEquals(Double.NaN, state.getG0());
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNull(state.getPreviousEventTime());
        assertTrue(state.isForward());
        assertTrue(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test of reinitializeBegin method, with valid arguments.
     */
    @Test
    public void testReinitializeBegin() {
        MockEventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        double[] y = new double[] { 1.0, 1.0 };
        state.reinitializeBegin(1.0, y);
        assertEquals(1.0, state.getT0(), 0.0);
        assertEquals(handler.g(1.0, y), state.getG0(), 0.0);
        assertTrue(state.isG0Positive());
    }

    /**
     * Test of reinitializeBegin method, with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testReinitializeBeginNull() {
        MockEventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.reinitializeBegin(null, null);
    }

    /**
     * Test of reinitializeBegin method, with non-double arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReinitializeBeginNonDouble() {
        MockEventHandler handler = new MockEventHandler();
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.reinitializeBegin(1, new Object[]{});
    }

    /**
     * Test of evaluateStep method, with positive step.
     */
    @Test
    public void testEvaluateStepPositive() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                double[] values = new double[] { 1.0, 1.0 };
                if (y != null) {
                    System.arraycopy(values, 0, y, 0, values.length);
                }
                return t * 0.1;
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[] { 1.0, 1.0 });
        interpolator.setUpperBound(1.4);
        interpolator.setLowerBound(1.0);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(1.2, state.getPendingEventTime(), 0.0);
        assertTrue(state.isPendingEvent());
    }

    /**
     * Test of evaluateStep method, with negative step.
     */
    @Test
    public void testEvaluateStepNegative() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                double[] values = new double[] { 1.0, 1.0 };
                if (y != null) {
                    System.arraycopy(values, 0, y, 0, values.length);
                }
                return -t * 0.1;
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[] { 1.0, 1.0 });
        interpolator.setUpperBound(0.6);
        interpolator.setLowerBound(1.0);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(0.8, state.getPendingEventTime(), 0.0);
        assertTrue(state.isPendingEvent());
    }

    /**
     * Test of evaluateStep method, with no event.
     */
    @Test
    public void testEvaluateStepNoEvent() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                double[] values = new double[] { 1.0, 1.0 };
                if (y != null) {
                    System.arraycopy(values, 0, y, 0, values.length);
                }
                return t * 0.1;
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[] { 1.0, 1.0 });
        interpolator.setUpperBound(1.4);
        interpolator.setLowerBound(1.0);
        assertFalse(state.evaluateStep(interpolator));
        assertFalse(state.isPendingEvent());
    }

    /**
     * Test of stepAccepted method.
     */
    @Test
    public void testStepAccepted() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                double[] values = new double[] { 1.0, 1.0 };
                if (y != null) {
                    System.arraycopy(values, 0, y, 0, values.length);
                }
                return t * 0.1;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                fail("Should not be called");
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        double[] y = new double[] { 1.0, 1.0 };
        state.stepAccepted(1.2, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test of stepAccepted method, with event.
     */
    @Test
    public void testStepAcceptedEvent() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public double g(double t, double[] y) {
                double[] values = new double[] { 1.0, 1.0 };
                if (y != null) {
                    System.arraycopy(values, 0, y, 0, values.length);
                }
                return t * 0.1;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                assertEquals(t, state.getPendingEventTime(), 0.0);
                assertEquals(increasing, state.isIncreasing());
                assertEquals(EventHandler.RESET_STATE, state.getNextAction());
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        double[] y = new double[] { 1.0, 1.0 };
        state.stepAccepted(1.2, y);
        assertTrue(state.isPendingEvent());
        assertNotNull(state.getPendingEventTime());
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());
    }

    /**
     * Test of reset method.
     */
    @Test
    public void testReset() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public void resetState(double t, double[] y) throws EventException {
                fail("Should not be called");
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        double[] y = new double[] { 1.0, 1.0 };
        state.reset(1.3, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /**
     * Test of reset method, with state reset.
     */
    @Test
    public void testResetState() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public void resetState(double t, double[] y) throws EventException {
                assertEquals(t, state.getT0(), 0.0);
                assertEquals(EventHandler.RESET_STATE, state.getNextAction());
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        double[] y = new double[] { 1.0, 1.0 };
        state.reset(1.3, y);
        assertTrue(state.isPendingEvent());
        assertNotNull(state.getPendingEventTime());
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());
    }

    /**
     * Test of reset method, with derivatives reset.
     */
    @Test
    public void testResetDerivatives() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public void resetDerivatives(double t, double[] y) throws EventException {
                assertEquals(t, state.getT0(), 0.0);
                assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.setPendingEvent(true);
        state.setPendingEventTime(1.2);
        double[] y = new double[] { 1.0, 1.0 };
        state.reset(1.3, y);
        assertTrue(state.isPendingEvent());
        assertNotNull(state.getPendingEventTime());
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());
    }

    /**
     * Test of reset method, with no event.
     */
    @Test
    public void testResetNoEvent() throws Exception {
        MockEventHandler handler = new MockEventHandler() {
            @Override
            public void resetState(double t, double[] y) throws EventException {
                fail("Should not be called");
            }
        };
        EventState state = new EventState(handler, 0.01, 0.001, 10);
        state.reset(1.3, new double[] { 1.0, 1.0 });
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

}