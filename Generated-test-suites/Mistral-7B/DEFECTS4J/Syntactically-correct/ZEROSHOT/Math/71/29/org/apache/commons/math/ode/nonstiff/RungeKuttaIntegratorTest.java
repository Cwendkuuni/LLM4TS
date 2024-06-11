package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractStepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     */
    @Test
    public void testRungeKuttaIntegratorConstruction() {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Euler", new double[] {1.0}, new double[][] {{}}, new double[] {1.0},
                new RungeKuttaStepInterpolator(), 0.1);
        Assert.assertNotNull(integrator);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        double[] a = {0.0, 1.0, 0.5};
        double[] b = {1.0 / 6.0, 2. / 3.0, 1. / 2.};
        double[] c = {1.0 / 6.0, 1.2 / 3.0, 1. / 2.};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta method of order 2", c, a, b, new RungeKuttaStepInterpolator(), 0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2.0 * y[0];
            }
        };

        double[] initialValues = {1.0};
        double start = 0.0;
        double end = 2.0;

        double result = integrator.integrate(equations, start, initialValues, end, new double[initialValues.length]);

        double expected = ODETestUtils.tolerableIntegral(equations, start, end, 1.0E-12, new AbstractStepHandler() {
            @Override
            public void handleStep(StepHandler stepHandler, int step) {
                if (step == 1) {
                    throw new MathIllegalArgumentException("Test step should not be accepted");
                }
            }
        });

        Assert.assertEquals(expected, result, 1.0E-12);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with events handling.
     */
    @Test
    public void testIntegrateWithEvents() throws Exception {
        double[] a = {0.0, 1.0, 0.5};
        double[] b = {1.0 / 6.0, 2. / 3.0, 1. / 2.};
        double[] c = {1.0 / 6.0, 1.2 / 3.0, 1. / 2.};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta method of order 2", c, a, b, new RungeKuttaStepInterpolator(), 0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2.0 * y[0];
            }
        };

        double[] initialValues = {1.0};
        double start = 0.0;
        double end = 2.0;

        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new When(1.5, new Event() {
            @Override
            public void handleEvent(double t, double[] y) {
                throw new NumberIsTooSmallException("Test event should not be reached");
            }
        }));

        double result = integrator.integrate(equations, start, initialValues, end, new double[initialValues.length], manager);

        double expected = ODETestUtils.tolerableIntegral(equations, start, end, 1.0E-12, new AbstractStepHandler() {
            @Override
            public void handleStep(StepHandler stepHandler, int step) {
                if (step == 1) {
                    throw new MathIllegalArgumentException("Test step should not be accepted");
                }
            }
        });

        Assert.assertEquals(expected, result, 1.0E-12);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with small step size.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithSmallStepSize() throws Exception {
        double[] a = {0.0, 1.0, 0.5};
        double[] b = {1.0 / 6.0, 2. / 3.0, 1. / 2.};
        double[] c = {1.0 / 6.0, 1.2 / 3.0, 1. / 2.};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta method of order 2", c, a, b, new RungeKuttaStepInterpolator(), 1.0E-14);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2.0 * y[0];
            }
        };

        double[] initialValues = {1.0};
        double start = 0.0;
        double end = 2.0;

        integrator.integrate(equations, start, initialValues, end, new double[initialValues.length]);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with only one stage.
     */
    @Test
    public void testIntegrateWithOneStage() throws Exception {
        double[] a = {1.0};
        double[] b = {1.0};
        double[] c = {1.0};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta method of order 1", c, a, b, new RungeKuttaStepInterpolator(), 0.1);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2.0 * y[0];
            }
        };

        double[] initialValues = {1.0};
        double start = 0.0;
        double end = 2.0;

        double result = integrator.integrate(equations, start, initialValues, end, new double[initialValues.length]);

        double expected = ODETestUtils.tolerableIntegral(equations, start, end, 1.0E-12, new AbstractStepHandler() {
            @Override
            public void handleStep(StepHandler stepHandler, int step) {
                if (step > 1) {
                    throw new AssertionError("More than one step should not be taken");
                }
            }
        });

        Assert.assertEquals(expected, result, 1.0E-12);
    }

}