package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.NullEventHandler;
import org.apache.commons.math3.ode.events.StepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaFehlbergIntegrator;

import java.util.Arrays;

public class EventStateTest {

    private StepInterpolator interpolator;
    private RungeKuttaFehlbergIntegrator integrator;
    private double[] initialState;

    @org.junit.Before
    public void setUp() {
        EventHandler handler = new NullEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);
        this.interpolator = new StepInterpolator();
        this.integrator = new RungeKuttaFehlbergIntegrator(5, 6, 1.0e-8, 1.0e-8, true);
        this.initialState = new double[]{2.0, 3.0};
        eventState.reinitializeBegin(0.0, initialState);
    }

    @org.junit.Test
    public void testInitialState() {
        assertEquals(Double.NaN, eventState.getT0(), 0.0);
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.hasPreviousEvent());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @org.junit.Test
    public void testEvaluateStep_NoEvent() throws DerivativeException, EventException {
        double time = 0.1;
        interpolator.setCurrentTime(time);
        integrator.addStep(time, initialState, eventState);
        interpolator.interpolate();
        boolean eventOccurred = eventState.evaluateStep(interpolator);
        assertFalse(eventOccurred);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertEquals(0.0, eventState.getEventTime(), 0.0);
    }

    @org.junit.Test
    public void testEvaluateStep_EventOccurs() throws DerivativeException, EventException, ConvergenceException {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x) - 0.5;
            }
        };

        EventHandler handler = new MyEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);
        eventState.reinitializeBegin(0.0, initialState);
        integrator.addStep(0.1, initialState, eventState);

        interpolator.setCurrentTime(0.1);
        interpolator.interpolate();
        boolean eventOccurred = eventState.evaluateStep(interpolator);
        assertTrue(eventOccurred);
        assertEquals(EventHandler.EVENT_ENCOUNTERED, eventState.getNextAction());
        assertEquals(0.5493061422641507, eventState.getEventTime(), 0.001);
    }

    @org.junit.Test(expected = DerivativeException.class)
    public void testEvaluateStep_DerivativeException() throws EventException {
        EventHandler handler = new NullEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);
        eventState.reinitializeBegin(0.0, initialState);

        double time = 0.1;
        interpolator.setCurrentTime(time);
        integrator.addStep(time, initialState, eventState);
        interpolator.interpolate();

        // This should throw a DerivativeException as the handler's g() method is not mocked
        eventState.evaluateStep(interpolator);
    }

    @org.junit.Test(expected = EventException.class)
    public void testEvaluateStep_EventException() throws DerivativeException {
        EventHandler handler = new MyBrokenEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);
        eventState.reinitializeBegin(0.0, initialState);

        double time = 0.1;
        interpolator.setCurrentTime(time);
        integrator.addStep(time, initialState, eventState);
        interpolator.interpolate();

        // This should throw an EventException as the handler's g() method throws an EventException
        eventState.evaluateStep(interpolator);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateStep_ConvergenceException() throws DerivativeException, EventException {
        EventHandler handler = new MyBrokenEventHandlerWithConvergenceException();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);
        eventState.reinitializeBegin(0.0, initialState);

        double time = 0.1;
        interpolator.setCurrentTime(time);
        integrator.addStep(time, initialState, eventState);
        interpolator.interpolate();

        // This should throw a ConvergenceException as the handler's g() method throws a ConvergenceException
        eventState.evaluateStep(interpolator);
    }

    @org.junit.Test
    public void testStepAccepted() throws EventException {
        double time = 0.15;
        interpolator.setCurrentTime(time);
        integrator.addStep(time, initialState, eventState);
        interpolator.interpolate();

        boolean eventOccurred = eventState.evaluateStep(interpolator);
        assertTrue(eventOccurred);

        eventState.stepAccepted(time, initialState);

        assertEquals(Double.NaN, eventState.getT0(), 0.0);
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertFalse(eventState.isG0Positive());
        assertTrue(eventState.hasPendingEvent());
        assertEquals(time, eventState.getPreviousEventTime(), 0.0);
        assertEquals(EventHandler.EVENT_ENCOUNTERED, eventState.getNextAction());
        assertEquals(time, eventState.getEventTime(), 0.0);
    }

    @org.junit.Test
    public void testReset() throws EventException {
        double time = 0.15;
        interpolator.setCurrentTime(time);
        integrator.addStep(time, initialState, eventState);
        interpolator.interpolate();

        boolean eventOccurred = eventState.evaluateStep(interpolator);
        assertTrue(eventOccurred);

        eventState.stepAccepted(time, initialState);

        eventState.reset(time + 0.01, new double[]{4.0, 5.0});

        assertEquals(Double.NaN, eventState.getT0(), 0.0);
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertFalse(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.hasPreviousEvent());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    private static class MyEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) throws EventException {
            return Math.sin(y[0]) - 0.5;
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean increasingEvent) throws EventException {
            // do nothing
        }

        @Override
        public void resetState(double t, double[] y) throws EventException {
            // do nothing
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // do nothing
        }

    }

    private static class MyBrokenEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) throws EventException {
            throw new EventException("Broken event handler");
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean increasingEvent) throws EventException {
            // do nothing
        }

        @Override
        public void resetState(double t, double[] y) throws EventException {
            // do nothing
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // do nothing
        }

    }

    private static class MyBrokenEventHandlerWithConvergenceException implements EventHandler {

        @Override
        public double g(double t, double[] y) throws EventException, ConvergenceException {
            throw new ConvergenceException("Broken event handler with convergence exception");
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean increasingEvent) throws EventException {
            // do nothing
        }

        @Override
        public void resetState(double t, double[] y) throws EventException {
            // do nothing
        }

        @Override
        public void resetDerivatives(double t, double[] y) {
            // do nothing
        }

    }

}