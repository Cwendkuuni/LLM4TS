package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.Event;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.MultipleEventsException;
import org.apache.commons.math3.ode.events.StateChangeEvent;
import org.apache.commons.math3.ode.events.StateTransitionEvent;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractIntegratorTest.TestDifferentialEquations;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialConditions;

    @Before
    public void setUp() {
        integrator = new AbstractStepIntegrator();
        equations = new TestDifferentialEquations();
        initialConditions = new double[] { 1.0, 1.0 };
    }

    @Test
    public void testConstructor() {
        AbstractIntegrator instance = new AbstractIntegrator("");
        assertNotNull(instance.getName());
        assertNotNull(instance.stepHandlers);
        assertNotNull(instance.eventsStates);
        assertFalse(instance.isLastStep());
        assertFalse(instance.resetOccurred());
        assertEquals(-1, instance.getMaxEvaluations());
        assertEquals(0, instance.getEvaluations());
        assertNull(instance.expandable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new AbstractIntegrator(null);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new TestStepHandler());
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(new TestStepHandler());
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        assertEquals(integrator.getStepHandlers().get(0), handlers.get(0));
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new TestStepHandler());
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithNullEventHandler() {
        integrator.addEventHandler(null, 0.0, 0.0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerWithNegativeMaxIterationCount() {
        integrator.addEventHandler(new TestEventHandler(), 0.0, 0.0, -1);
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new TestEventHandler(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(new TestEventHandler(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        List<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(1, handlers.size());
        assertEquals(integrator.getEventHandlers().get(0), handlers.get(0));
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new TestEventHandler(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() {
        integrator.integrate(null, 0.0, initialConditions, 1.0, new double[initialConditions.length]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedInitialConditions() {
        FirstOrderDifferentialEquations equationsWithDifferentDimension = new TestDifferentialEquationsWithDifferentDimension();
        integrator.integrate(equationsWithDifferentDimension, 0.0, new double[] { 1.0, 1.0, 1.0 }, 1.0, new double[initialConditions.length]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedOutputCondition() {
        FirstOrderDifferentialEquations equationsWithDifferentDimensionOutput = new TestDifferentialEquationsWithDifferentDimensionOutput();
        integrator.integrate(equationsWithDifferentDimensionOutput, 0.0, initialConditions, 1.0, new double[initialConditions.length + 1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluationsReached() throws Exception {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[initialConditions.length]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStepSize() throws Exception {
        integrator.setStepSize(1e-15);
        try {
            integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[initialConditions.length]);
        } catch (NoBracketingException e) {
            throw new NumberIsTooSmallException("Expected NumberIsTooSmallException, but received NoBracketingException", e);
        }
    }

    @Test(expected = MultipleEventsException.class)
    public void testIntegrateWithMultipleEvents() throws Exception {
        integrator.addEventHandler(new TestEventHandlerWithMultipleEvents(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        try {
            integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[initialConditions.length]);
        } catch (MultipleEventsException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrate() throws Exception {
        integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[initialConditions.length]);
    }

    @Test
    public void testAcceptStep() throws Exception {
        integrator.integrate(equations, 0.0, initialConditions, 1.0, new double[initialConditions.length]);

        StepInterpolator interpolator = new AbstractStepInterpolator();
        interpolator.setGlobalCurrentTime(1.0);
        interpolator.setGlobalPreviousTime(0.0);

        double[] y = new double[initialConditions.length];
        System.arraycopy(initialConditions, 0, y, 0, y.length);

        double tEnd = 1.1;

        double result = integrator.acceptStep(interpolator, y, null, tEnd);

        assertEquals(1.0, result, 1e-12);
    }

    @Test
    public void testSanityChecks() throws Exception {
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        integrator.sanityChecks(expandableODE, 1.0);
    }

    private static class TestDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return initialConditions.length;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    private static class TestDifferentialEquationsWithDifferentDimension implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return initialConditions.length + 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
            yDot[2] = 0;
        }
    }

    private static class TestDifferentialEquationsWithDifferentDimensionOutput implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return initialConditions.length;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
            yDot[2] = 1;
        }
    }

    private static class TestEventHandler implements EventHandler {
        @Override
        public void handleEvent(double t, double[] y) {
            // Empty
        }

        @Override
        public void init(double t, double[] y, double tEnd) {
            // Empty
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // Empty
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return true;
        }

        @Override
        public boolean stop() {
            return false;
        }

        @Override
        public void stepAccepted(double t, double[] y) {
            // Empty
        }

        @Override
        public void reset(double t, double[] y) {
            // Empty
        }
    }

    private static class TestEventHandlerWithMultipleEvents extends AbstractEventHandler {
        private int eventCount = 0;

        @Override
        public EventRate getEventRate(double t, double[] y) {
            return EventRate.CONTINUOUS;
        }

        @Override
        public Event[] getEvents(double t, double[] y) {
            if (eventCount == 0) {
                return new Event[] {new StateChangeEvent(t, y)};
            } else if (eventCount == 1) {
                return new Event[] {new StateTransitionEvent(t, y)};
            } else {
                throw new IllegalStateException("Multiple events should not be reached");
            }
        }

        @Override
        public void init(double t, double[] y, double tEnd) {
            // Empty
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // Empty
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            eventCount++;
            return true;
        }

        @Override
        public boolean stop() {
            return eventCount >= 2;
        }

        @Override
        public void stepAccepted(double t, double[] y) {
            // Empty
        }

        @Override
        public void reset(double t, double[] y) {
            // Empty
        }
    }

    private static class TestStepHandler implements StepHandler {
        @Override
        public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
            // Empty
        }

        @Override
        public void init(double t, double[] y, double tEnd) {
            // Empty
        }
    }
}