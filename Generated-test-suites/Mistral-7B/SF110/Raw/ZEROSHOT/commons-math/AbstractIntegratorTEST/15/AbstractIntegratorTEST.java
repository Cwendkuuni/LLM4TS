package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.AbstractEvent;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.MockEquations;
import org.apache.commons.math3.ode.test.MockStepInterpolator;
import org.apache.commons.math3.ode.test.MockStepHandler;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private StepInterpolator interpolator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialState;
    private double[] finalState;
    private Event event;
    private List<StepHandler> stepHandlers;
    private List<EventHandler> eventHandlers;

    @Before
    public void setUp() {
        integrator = new MockIntegrator("MockIntegrator");
        interpolator = new MockStepInterpolator(true);
        equations = new MockEquations();
        initialState = new double[] { 0.0, 0.0 };
        finalState = new double[] { 1.0, 1.0 };
        event = new MockEvent();
        stepHandlers = new ArrayList<StepHandler>();
        stepHandlers.add(new MockStepHandler());
        eventHandlers = new ArrayList<EventHandler>();
        integrator.addEventHandler(event, 0.0, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.addStepHandler(new MockStepHandler());
    }

    @Test
    public void testConstructor() {
        assertNotNull(integrator);
        assertEquals("MockIntegrator", integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(0, integrator.getEventHandlers().size());
        assertNull(integrator.getExpandable());
        assertEquals(0, integrator.getEvaluations().getCount());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertFalse(integrator.getStatesInitialized());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        new MockIntegrator(null);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandlers.get(0));
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(stepHandlers.get(0));
        assertEquals(1, integrator.getStepHandlers().size());
        assertEquals(stepHandlers.get(0), integrator.getStepHandlers().get(0));
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandlers.get(0));
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(event, 0.0, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        assertEquals(event, integrator.getEventHandlers().get(0).getEventHandler());
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(event, 0.0, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        assertEquals(1, integrator.getEventHandlers().size());
        assertEquals(event, integrator.getEventHandlers().get(0).getEventHandler());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(event, 0.0, 0.001, 1, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentStepStart() {
        integrator.initIntegration(0.0, initialState, 1.0);
        assertNull(integrator.getCurrentStepStart());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.initIntegration(0.0, initialState, 1.0);
        assertNull(integrator.getCurrentSignedStepsize());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        assertEquals(3, integrator.getEvaluations().getCount());
    }

    @Test
    public void testIntegrate() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException, NumberIsTooSmallException {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEquations(equations);
        double result = integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
        assertEquals(1.0, result, Precision.SAFE_EPSILON);
        assertEquals(1.0, finalState[0], Precision.SAFE_EPSILON);
        assertEquals(1.0, finalState[1], Precision.SAFE_EPSILON);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateTooSmallStep() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException, NumberIsTooSmallException {
        integrator.initIntegration(0.0, initialState, 0.1);
        integrator.setEvaluationsCounter(interpolator);
        integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxCount() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException, NumberIsTooSmallException {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEvaluationsCounter(interpolator);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException, NumberIsTooSmallException {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEvaluationsCounter(new MockStepInterpolator(false));
        integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
    }

    @Test
    public void testIntegrateWithEvent() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException, NumberIsTooSmallException {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
        assertEquals(1.0, finalState[0], Precision.SAFE_EPSILON);
        assertEquals(1.0, finalState[1], Precision.SAFE_EPSILON);
        assertEquals(1, integrator.getEvaluations().getCount());
        assertTrue(event.isTriggered());
    }

    @Test
    public void testIntegrateWithStepHandlers() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException, NumberIsTooSmallException {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
        assertEquals(1.0, finalState[0], Precision.SAFE_EPSILON);
        assertEquals(1.0, finalState[1], Precision.SAFE_EPSILON);
        assertEquals(2, integrator.getEvaluations().getCount());
        assertEquals(stepHandlers.get(0).getCallCount(), integrator.getEvaluations().getCount());
    }

    @Test
    public void testAcceptStep() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
        double result = integrator.acceptStep(interpolator, finalState, null, 1.0);
        assertEquals(1.0, result, Precision.SAFE_EPSILON);
        assertEquals(2, integrator.getEvaluations().getCount());
    }

    @Test
    public void testSanityChecks() {
        integrator.initIntegration(0.0, initialState, 1.0);
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 1.0);
    }

    private static class MockIntegrator extends AbstractIntegrator {

        public MockIntegrator(String name) {
            super(name);
        }

        MockIntegrator() {
            this(null);
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Do nothing
        }
    }

    private static class MockStepInterpolator extends AbstractStepInterpolator {

        public MockStepInterpolator(boolean forward) {
            super(forward);
        }

        @Override
        public double getGlobalPreviousTime() {
            return 0.0;
        }

        @Override
        public double getGlobalCurrentTime() {
            return 0.0;
        }

        @Override
        public double getInterpolatedTime() {
            return 0.0;
        }

        @Override
        public double[] getPrimaryState() {
            return new double[0];
        }

        @Override
        public double[] getInterpolatedState() {
            return new double[0];
        }

        @Override
        public double[] getInterpolatedSecondaryState(int index) {
            return new double[0];
        }

        @Override
        public void setInterpolatedTime(double time) {
            // Do nothing
        }

        @Override
        public void setSoftPreviousTime(double time) {
            // Do nothing
        }

        @Override
        public void setSoftCurrentTime(double time) {
            // Do nothing
        }

        @Override
        public StepHandlerAdapter getStepHandlerAdapter() {
            return null;
        }

        @Override
        public boolean isForward() {
            return true;
        }
    }

    private static class MockStepHandler implements StepHandler {

        private int callCount = 0;

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
            callCount++;
        }

        public int getCallCount() {
            return callCount;
        }
    }

    private static class MockEvent extends AbstractEvent {

        private boolean triggered = false;

        @Override
        public void init(double t0, double[] y0, double t) {
            // Do nothing
        }

        @Override
        public boolean evaluateStep(StepInterpolator interpolator) {
            triggered = true;
            return false;
        }

        public boolean isTriggered() {
            return triggered;
        }
    }

    private static class MockEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }
}