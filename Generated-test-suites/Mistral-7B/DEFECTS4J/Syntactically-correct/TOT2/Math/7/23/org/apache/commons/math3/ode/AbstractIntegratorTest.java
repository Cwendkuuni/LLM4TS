package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.RungeKuttaFehlberg78Stepper;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the AbstractIntegrator class
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    /**
     * Initialize the integrator and equations
     */
    @Before
    public void setup() {
        integrator = new RungeKuttaFehlberg78Integrator("test");
        equations = new MockDifferentialEquations();
        integrator.setEquations(equations);
    }

    /**
     * Test basic functionality of the integrator
     */
    @Test
    public void testBasicIntegration() {
        double[] y = new double[] { 1.0, 2.0 };
        double tEnd = 5.0;
        double[] yResult = integrator.integrate(equations, 0.0, y, tEnd);
        assertEquals(5.0, yResult[0], 1e-12);
        assertEquals(9.0, yResult[1], 1e-12);
    }

    /**
     * Test exception handling when number of evaluations is exceeded
     */
    @Test(expected = MaxCountExceededException.class)
    public void testExceptionHandlingMaxEvaluations() {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[] { 0.0 }, 1.0);
    }

    /**
     * Test exception handling when dimensions mismatch
     */
    @Test(expected = DimensionMismatchException.class)
    public void testExceptionHandlingDimensionMismatch() {
        equations.setDimension(3);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 5.0);
    }

    /**
     * Test exception handling when no bracketing occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testExceptionHandlingNoBracketing() {
        equations.setEventHandler(new MockEventHandler());
        integrator.integrate(equations, 0.0, new double[] { 0.0 }, 1.0);
    }

    /**
     * Test handling of events
     */
    @Test
    public void testEventHandling() {
        equations.setEventHandler(new MockEventHandler());
        integrator.integrate(equations, 0.0, new double[] { 0.0 }, 3.0);
        assertEquals(1.0, integrator.getEvaluations(), 1);
    }

    /**
     * Test handling of step handlers
     */
    @Test
    public void testStepHandlers() {
        integrator.addStepHandler(new MockStepHandler());
        integrator.integrate(equations, 0.0, new double[] { 0.0 }, 3.0);
        assertEquals(2.0, integrator.getEvaluations(), 1);
    }

    /**
     * Test integration with custom stepper
     */
    @Test
    public void testCustomStepper() {
        integrator = new RungeKuttaCashKarpIntegrator("test");
        integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 5.0);
        double[] yResult = integrator.getLastState();
        assertEquals(5.0, yResult[0], 1e-12);
        assertEquals(13.0, yResult[1], 1e-12);
    }

}

/**
 * Mock differential equations for testing
 */
class MockDifferentialEquations implements FirstOrderDifferentialEquations {

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

/**
 * Mock event handler for testing
 */
class MockEventHandler implements EventHandler {

    private int count = 0;

    @Override
    public boolean isImmediate() {
        return false;
    }

    @Override
    public void evaluateState(double t, double[] y, double[] yDot, double[] events) {
        events[count++] = 2.0;
    }

    @Override
    public double getEventTime() {
        return 2.0;
    }

    @Override
    public void init(double t, double[] y, double tStep) {
        // empty
    }

    @Override
    public boolean evaluateStep(AbstractStepInterpolator interpolator) {
        return count < 2;
    }

}

/**
 * Mock step handler for testing
 */
class MockStepHandler implements StepHandler {

    @Override
    public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
        if (last) {
            integrator.setLastStep(true);
        }
    }

}