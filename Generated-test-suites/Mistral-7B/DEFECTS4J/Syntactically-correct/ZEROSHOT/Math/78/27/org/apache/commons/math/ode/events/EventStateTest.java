package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventStateTestUtils;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link EventState} class.
 */
public class EventStateTest {

    private EventState eventState;
    private MockEventHandler mockEventHandler;
    private StepInterpolator stepInterpolator;

    @Before
    public void setUp() {
        mockEventHandler = new MockEventHandler();
        stepInterpolator = new StepInterpolator();
        eventState = new EventState(mockEventHandler, 0.01, 1e-5, 10);
    }

    @Test
    public void testBasicConstruction() {
        assertNotNull(eventState);
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 1e-7);
        assertEquals(1e-5, eventState.getConvergence(), 1e-7);
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

    @Test
    public void testReset() {
        eventState.reinitializeBegin(0.0, new double[]{1.0});
        eventState.stepAccepted(0.01, new double[]{1.1});
        eventState.reset(0.02, new double[]{1.2});
        assertTrue(eventState.isForward());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(1.2, eventState.getT0(), 1e-7);
        assertEquals(1.2, eventState.getG0(), 1e-7);
    }

    @Test
    public void testEvaluateStep() throws Exception {
        eventState.reinitializeBegin(0.0, new double[]{0.0});
        stepInterpolator.setInterpolatedFunction(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * 0.1;
            }
        });
        assertTrue(eventState.evaluateStep(stepInterpolator));
        assertTrue(eventState.isPendingEvent());
        assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws Exception {
        eventState.reinitializeBegin(0.0, new double[]{0.0});
        stepInterpolator.setInterpolatedFunction(new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new EventException("Test exception");
            }
        });
        assertTrue(eventState.evaluateStep(stepInterpolator));
        fail("Expected EventException to be thrown");
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws Exception {
        eventState.reinitializeBegin(0.0, new double[]{0.0});
        stepInterpolator.setInterpolatedFunction(new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new DerivativeException("Test exception");
            }
        });
        assertTrue(eventState.evaluateStep(stepInterpolator));
        fail("Expected DerivativeException to be thrown");
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws Exception {
        eventState.reinitializeBegin(0.0, new double[]{0.0});
        stepInterpolator.setInterpolatedFunction(new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new ConvergenceException("Test exception");
            }
        });
        assertTrue(eventState.evaluateStep(stepInterpolator));
        fail("Expected ConvergenceException to be thrown");
    }

}

class MockEventHandler implements EventHandler {

    private boolean resetState;
    private boolean resetDerivatives;
    private double eventTime;
    private int nextAction;

    @Override
    public void eventOccurred(double t, double[] y, boolean crossesEvent) {
        eventTime = t;
        if (crossesEvent) {
            resetState = true;
            nextAction = EventHandler.RESET_STATE;
        } else {
            resetDerivatives = true;
            nextAction = EventHandler.RESET_DERIVATIVES;
        }
    }

    @Override
    public void resetState(double t, double[] y) {
        // empty
    }

    @Override
    public double g(double t, double[] y) {
        return EventStateTestUtils.mockEventHandlerG(t, y);
    }

    public void setResetState(boolean resetState) {
        this.resetState = resetState;
    }

    public void setResetDerivatives(boolean resetDerivatives) {
        this.resetDerivatives = resetDerivatives;
    }

    public boolean isResetState() {
        return resetState;
    }

    public boolean isResetDerivatives() {
        return resetDerivatives;
    }

}