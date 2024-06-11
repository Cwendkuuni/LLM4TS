import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);

        // test initial state
        assertNull(eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.01, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNaN(eventState.getT0());
        assertNaN(eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertNaN(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // test reinitializeBegin
        double tStart = 0.0;
        double[] yStart = {0.0};
        try {
            eventState.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, eventState.getT0(), 0.0);
            assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
            assertTrue(handler.isG0Positive(tStart, yStart));
            assertEquals(tStart, eventState.getPreviousEventTime(), 0.0);
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // test evaluateStep
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.1;
            }

            @Override
            public void setInterpolatedTime(double time) {
                // no-op
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{0.1};
            }
        };

        try {
            eventState.evaluateStep(interpolator);
            assertFalse(eventState.isPendingEvent());
            assertNaN(eventState.getPendingEventTime());
        } catch (DerivativeException e1) {
            // expected because the interpolator does not provide the derivatives
        } catch (EventException e2) {
            fail("Unexpected exception: " + e2.getMessage());
        } catch (ConvergenceException e3) {
            fail("Unexpected exception: " + e3.getMessage());
        }

        // test stepAccepted
        double tAccepted = 0.2;
        double[] yAccepted = {0.2};
        try {
            eventState.stepAccepted(tAccepted, yAccepted);
            assertEquals(tAccepted, eventState.getT0(), 0.0);
            assertEquals(handler.g(tAccepted, yAccepted), eventState.getG0(), 0.0);
            assertTrue(handler.isG0Positive(tAccepted, yAccepted));
            assertFalse(eventState.isPendingEvent());
            assertNaN(eventState.getPendingEventTime());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // test stop
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());

        // test reset
        double tReset = 0.3;
        double[] yReset = {0.3};
        try {
            eventState.reset(tReset, yReset);
            assertEquals(tReset, eventState.getT0(), 0.0);
            // the state should not be reset since nextAction is not RESET_STATE
            assertArrayNotEquals(yReset, eventState.getInterpolatedState());
            assertFalse(eventState.isPendingEvent());
            assertNaN(eventState.getPendingEventTime());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // test reset with RESET_STATE
        eventState.setNextAction(EventHandler.RESET_STATE);
        try {
            eventState.reset(tReset, yReset);
            assertArrayEquals(yReset, eventState.getInterpolatedState());
            assertFalse(eventState.isPendingEvent());
            assertNaN(eventState.getPendingEventTime());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // test reset with RESET_DERIVATIVES
        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);
        try {
            eventState.reset(tReset, yReset);
            assertArrayEquals(yReset, eventState.getInterpolatedState());
            assertFalse(eventState.isPendingEvent());
            assertNaN(eventState.getPendingEventTime());
            assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private static class DummyEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) {
            return Math.sin(t) * y[0];
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean crossing) throws EventException {
            // no-op
        }

        @Override
        public boolean isG0Positive(double t, double[] y) {
            return Math.sin(t) > 0;
        }

        @Override
        public void resetState(double t, double[] y) throws EventException {
            // no-op
        }

    }

}