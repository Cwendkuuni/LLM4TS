package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test case for the Euler integrator.
     */
    @Test
    public void testEulerIntegrator() {
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.01, new EulerStepInterpolator());
        double[] y = { 1.0, 0.0 };
        double[] yPrime = { 0.1, 0.01 };
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(yPrime);

        double t = 0.0;
        try {
            t = integrator.integrate(equations, 0.0, y, 1.0, new double[y.length]);
            assertEquals("Incorrect integration result.", 1.0, t, 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 2.0;
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected IntegratorException with the message \"step size is too large\". Actual result: " + t);
        } catch (IntegratorException e) {
            assertEquals("Correct exception message.", "step size is too large", e.getMessage());
        }

        t = 1.0;
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            assertEquals("Incorrect integration result.", 1.5, t, 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 0.0;
        StepHandler stepHandler = new StepCounter();
        integrator.addStepHandler(stepHandler);
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            assertEquals("Incorrect number of steps.", 1, stepHandler.getNumberOfSteps());
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 1.0;
        equations = new RelativetimeEventDifferentialEquations(1.0, new RelativetimeEvent(1.0));
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected IntegratorException with the message \"event encountered\". Actual result: " + t);
        } catch (IntegratorException e) {
            assertEquals("Correct exception message.", "event encountered", e.getMessage());
        }

        t = 0.0;
        try {
            integrator.resetEvaluations();
            integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected MaxCountExceededException.");
        } catch (MaxCountExceededException e) {
            // Expected result
        } catch (IntegratorException | DerivativeException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }
    }

    /**
     * Test case for the midpoint integrator.
     */
    @Test
    public void testMidpointIntegrator() {
        RungeKuttaIntegrator integrator = new MidpointIntegrator(0.01, new MidpointStepInterpolator());
        double[] y = { 1.0, 0.0 };
        double[] yPrime = { 0.1, 0.01 };
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(yPrime);

        double t = 0.0;
        try {
            t = integrator.integrate(equations, 0.0, y, 1.0, new double[y.length]);
            assertEquals("Incorrect integration result.", 1.0, t, 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 2.0;
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected IntegratorException with the message \"step size is too large\". Actual result: " + t);
        } catch (IntegratorException e) {
            assertEquals("Correct exception message.", "step size is too large", e.getMessage());
        }

        t = 1.0;
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            assertEquals("Incorrect integration result.", 1.5, t, 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 0.0;
        StepHandler stepHandler = new StepCounter();
        integrator.addStepHandler(stepHandler);
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            assertEquals("Incorrect number of steps.", 2, stepHandler.getNumberOfSteps());
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 1.0;
        equations = new RelativetimeEventDifferentialEquations(1.0, new RelativetimeEvent(1.0));
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected IntegratorException with the message \"event encountered\". Actual result: " + t);
        } catch (IntegratorException e) {
            assertEquals("Correct exception message.", "event encountered", e.getMessage());
        }

        t = 0.0;
        try {
            integrator.resetEvaluations();
            integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected MaxCountExceededException.");
        } catch (MaxCountExceededException e) {
            // Expected result
        } catch (IntegratorException | DerivativeException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }
    }

    /**
     * Test case for the Runge-Kutta-Fehlberg 7-8 integrator.
     */
    @Test
    public void testRkf78Integrator() {
        RungeKuttaIntegrator integrator = new RkF78Integrator(0.01, new RkF78StepInterpolator());
        double[] y = { 1.0, 0.0 };
        double[] yPrime = { 0.1, 0.01 };
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(yPrime);

        double t = 0.0;
        try {
            t = integrator.integrate(equations, 0.0, y, 1.0, new double[y.length]);
            assertEquals("Incorrect integration result.", 1.0, t, 1e-13);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 2.0;
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected IntegratorException with the message \"step size is too large\". Actual result: " + t);
        } catch (IntegratorException e) {
            assertEquals("Correct exception message.", "step size is too large", e.getMessage());
        }

        t = 1.0;
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            assertEquals("Incorrect integration result.", 1.5, t, 1e-13);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 0.0;
        StepHandler stepHandler = new StepCounter();
        integrator.addStepHandler(stepHandler);
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            assertEquals("Incorrect number of steps.", 3, stepHandler.getNumberOfSteps());
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }

        t = 1.0;
        equations = new RelativetimeEventDifferentialEquations(1.0, new RelativetimeEvent(1.0));
        try {
            t = integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected IntegratorException with the message \"event encountered\". Actual result: " + t);
        } catch (IntegratorException e) {
            assertEquals("Correct exception message.", "event encountered", e.getMessage());
        }

        t = 0.0;
        try {
            integrator.resetEvaluations();
            integrator.integrate(equations, 0.0, y, t, new double[y.length]);
            fail("Expected MaxCountExceededException.");
        } catch (MaxCountExceededException e) {
            // Expected result
        } catch (IntegratorException | DerivativeException e) {
            fail("Unexpected exception during integration: " + e.getMessage());
        }
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    private final double[] yPrime;

    public DummyDifferentialEquations(double[] yPrime) {
        this.yPrime = yPrime;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        System.arraycopy(this.yPrime, 0, yPrime, 0, yPrime.length);
    }

}

class RelativetimeEventDifferentialEquations implements FirstOrderDifferentialEquations {

    private final double eventTime;
    private final Event event;

    public RelativetimeEventDifferentialEquations(double eventTime, Event event) {
        this.eventTime = eventTime;
        this.event = event;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (t >= eventTime) {
            yPrime[0] = Double.NaN;
            event.handle(t, y);
        } else {
            yPrime[0] = 1.0;
        }
    }

}

class StepCounter implements StepHandler {
    private int steps = 0;

    @Override
    public void handleStep(double t, double[] y) {
        ++steps;
    }

    public int getNumberOfSteps() {
        return steps;
    }

}