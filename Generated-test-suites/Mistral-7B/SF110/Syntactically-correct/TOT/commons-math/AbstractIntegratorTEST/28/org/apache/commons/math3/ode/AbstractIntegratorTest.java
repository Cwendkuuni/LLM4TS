package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.nonstiff.RK4Integrator;
import org.apache.commons.math3.ode.nonstiff.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new RK4Integrator("RK4IntegratorTest");
    }

    @Test
    public void testInitialize() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(3));
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 10.0;
        integrator.initIntegration(t0, y0, t);
        assertEquals(10.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(-1, integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInitializeWithWrongDimensions() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(3));
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        integrator.initIntegration(t0, y0, t);
    }

    @Test
    public void testIntegration() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(3));
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double tEnd = 10.0;
        double[] y = new double[3];
        integrator.integrate(equations, t0, y0, tEnd, y);
        assertEquals(10.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(equations.getDimension(), y.length);
        assertEquals(1, integrator.getEvaluations());
        assertArrayEquals(new double[]{2.718281828, 2.718281828, 2.718281828}, y, 1e-12);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithWrongDimensions() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(3));
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double tEnd = 10.0;
        double[] y = new double[2];
        integrator.integrate(equations, t0, y0, tEnd, y);
    }

    @Test
    public void testIntegrationWithEventHandlers() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(3));
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double tEnd = 10.0;
        double[] y = new double[3];

        // Add an event handler that stops integration when y[0] reaches 2.5
        MyEventHandler eventHandler = new MyEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 5);

        integrator.integrate(equations, t0, y0, tEnd, y);
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1, integrator.getEvaluations());
        assertArrayEquals(new double[]{2.5, 2.5, 2.5}, y, 1e-12);
        assertTrue(eventHandler.isCalled());

        // Add a step handler that prints a message when integration starts
        MyStepHandler stepHandler = new MyStepHandler();
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(t0, y0, tEnd);
        assertEquals(10.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(1, integrator.getEvaluations());
        assertTrue(stepHandler.isCalled());
    }

    @Test
    public void testStepAcceptance() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(2));
        StepInterpolator interpolator = new StepInterpolator();
        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double tEnd = 5.0;
        double[] y = new double[2];

        integrator.initIntegration(t0, y0, tEnd);
        integrator.setExpandable(equations);

        // Test accepting a step
        double t = 1.0;
        double[] yDot = new double[2];
        double stepSize = 0.1;
        interpolator.setGlobalPreviousTime(t0);
        interpolator.setGlobalCurrentTime(t);
        double stepAccepted = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(t, stepAccepted, 1e-12);
        assertEquals(1, integrator.getEvaluations());

        // Test reinitializing events and step handlers
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setExpandable(equations);

        integrator.addEventHandler(new MyEventHandler(), 0.1, 0.01, 5);
        integrator.addStepHandler(new MyStepHandler());
        interpolator.setGlobalPreviousTime(t0);
        interpolator.setGlobalCurrentTime(t);
        stepAccepted = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(t, stepAccepted, 1e-12);
        assertEquals(2, integrator.getEvaluations());
        assertTrue(eventHandler.isCalled());
        assertTrue(stepHandler.isCalled());

        // Test rejecting a step
        t = 2.0;
        stepAccepted = integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertEquals(t, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(3, integrator.getEvaluations());
        assertFalse(eventHandler.isCalled());
        assertTrue(stepHandler.isCalled());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testStepRejection() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(2));
        StepInterpolator interpolator = new StepInterpolator();
        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double tEnd = 5.0;
        double[] y = new double[2];

        integrator.initIntegration(t0, y0, tEnd);
        integrator.setExpandable(equations);

        integrator.addEventHandler(new MyEventHandler(), 0.1, 0.01, 5);
        integrator.addStepHandler(new MyStepHandler());
        integrator.setMaxEvaluations(3);

        interpolator.setGlobalPreviousTime(t0);
        interpolator.setGlobalCurrentTime(t0);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
    }

    @Test
    public void testReset() throws NoBracketingException, DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new SimpleDifferentialEquations(2));
        StepInterpolator interpolator = new StepInterpolator();
        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double tEnd = 5.0;
        double[] y = new double[2];

        integrator.initIntegration(t0, y0, tEnd);
        integrator.setExpandable(equations);

        integrator.addEventHandler(new MyEventHandler(), 0.1, 0.01, 5);
        integrator.addStepHandler(new MyStepHandler());

        interpolator.setGlobalPreviousTime(t0);
        interpolator.setGlobalCurrentTime(t0);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(integrator.resetOccurred);
        assertEquals(1, integrator.getEvaluations());
        assertArrayEquals(y0, y, 1e-12);

        integrator.resetOccurred = false;
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        integrator.acceptStep(interpolator, y, yDot, tEnd);
        assertTrue(integrator.resetOccurred);
        assertEquals(4, integrator.getEvaluations());
    }
}

class MyEventHandler implements EventHandler {
    private boolean called;

    public void init(double t, double[] y, double tEnd) {
        // Initialize event handler
    }

    public boolean evaluateStep(StepInterpolator interpolator) {
        double y0 = interpolator.getInterpolatedState()[0];
        return y0 >= 2.5;
    }

    public void stepAccepted(double t, double[] y) {
        called = true;
    }

    public void stop() {
        // Do nothing
    }

    public boolean isCalled() {
        return called;
    }
}

class MyStepHandler implements StepHandler {
    private boolean called;

    public void init(double t, double[] y, double tEnd) {
        // Initialize step handler
    }

    public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
        called = true;
    }

    public boolean isCalled() {
        return called;
    }
}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {
    private int dimension;

    public SimpleDifferentialEquations(int dimension) {
        this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        for (int i = 0; i < dimension; i++) {
            yDot[i] = y[i] * 0.1;
        }
    }
}