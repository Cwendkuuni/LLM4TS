package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Simple test case for {@link RungeKuttaIntegrator}.
     */
    @Test
    public void testSimpleRungeKuttaIntegrator() {
        final double step = 0.1;
        double[][] coefficients = {
            {0.0, 0.0, 0.5, 0.5},
            {0.0, 0.5, 0.5, 0.0},
            {0.5, 0.0, 0.5, 0.5},
            {0.5, 0.5, 0.0, 0.5}
        };
        double[] weights = {0.25, 0.25, 0.25, 0.25};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", coefficients, weights, new DummyStepInterpolator()) {
            @Override
            protected int getNumberOfSteps() {
                return 4;
            }
        };
        integrator.setStep(step);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialCondition = {0.0, 1.0};
        double[] y = new double[initialCondition.length];
        System.arraycopy(initialCondition, 0, y, 0, initialCondition.length);
        double[] expected = {1.0, 1.0};
        double time = 1.0;
        for (int i = 0; i < 10; ++i) {
            double result = integrator.integrate(equations, 0.0, y, time, new double[y.length]);
            assertArrayEquals("Result is not correct for step " + i, expected, y);
            time += step;
            for (int j = 0; j < y.length; ++j) {
                equations.derive(time, y);
                expected[j] += step * equations.getYDerivative(time, y)[j];
            }
        }
    }

    /**
     * Test case for {@link RungeKuttaIntegrator} with step size too small.
     */
    @Test(expected = IntegratorException.class)
    public void testRungeKuttaIntegratorWithSmallStepSize() {
        final double step = 1.0e-10;
        double[][] coefficients = {
            {0.0, 0.0, 0.5, 0.5},
            {0.0, 0.5, 0.5, 0.0},
            {0.5, 0.0, 0.5, 0.5},
            {0.5, 0.5, 0.0, 0.5}
        };
        double[] weights = {0.25, 0.25, 0.25, 0.25};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", coefficients, weights, new DummyStepInterpolator()) {
            @Override
            protected int getNumberOfSteps() {
                return 4;
            }
        };
        integrator.setStep(step);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialCondition = {0.0, 1.0};
        double[] y = new double[initialCondition.length];
        System.arraycopy(initialCondition, 0, y, 0, initialCondition.length);
        double time = 1.0;
        integrator.integrate(equations, 0.0, y, time, new double[y.length]);
    }

    /**
     * Test case for {@link RungeKuttaIntegrator} with multiple events.
     */
    @Test
    public void testRungeKuttaIntegratorWithMultipleEvents() {
        final double step = 0.1;
        double[][] coefficients = {
            {0.0, 0.0, 0.5, 0.5},
            {0.0, 0.5, 0.5, 0.0},
            {0.5, 0.0, 0.5, 0.5},
            {0.5, 0.5, 0.0, 0.5}
        };
        double[] weights = {0.25, 0.25, 0.25, 0.25};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", coefficients, weights, new DummyStepInterpolator()) {
            @Override
            protected int getNumberOfSteps() {
                return 4;
            }
        };
        integrator.setStep(step);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithEvent();
        double[] initialCondition = {0.0, 1.0};
        double[] y = new double[initialCondition.length];
        System.arraycopy(initialCondition, 0, y, 0, initialCondition.length);
        double time = 2.0;
        StepHandler stepHandler = new StepHandler() {
            private int count = 0;

            @Override
            public void handleStep(AbstractIntegrator integrator, boolean lastStep) {
                count++;
            }
        };
        integrator.setStepHandler(stepHandler);
        double[] expected = {1.0, 1.0};
        for (int i = 0; i < 10; ++i) {
            integrator.integrate(equations, 0.0, y, time, new double[y.length]);
            assertArrayEquals("Result is not correct for step " + i, expected, y);
            time += step;
            for (int j = 0; j < y.length; ++j) {
                equations.derive(time, y);
                expected[j] += step * equations.getYDerivative(time, y)[j];
            }
            assertEquals("Number of steps is not correct for step " + i, 4, stepHandler.getCount());
        }
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    /**
     * {@inheritDoc}
     */
    @Override
    public void derive(double time, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }

}

class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

    /**
     * {@inheritDoc}
     */
    @Override
    public void derive(double time, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
        yPrime[0] += 0.1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEventTime() {
        return 1.5;
    }

}