package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketsForSquareRootException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math.ode.nonstiff.TestEquations;

/**
 * Tests the {@link RungeKuttaIntegrator} abstract class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     */
    @Test
    public void testConstructor() {
        final String name = "test";
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {{0.0, 0.0, 0.0, 0.0},
                              {0.5, 0.5, 0.5, 1.0},
                              {0.5, 1.0, 1.0, 1.5},
                              {1.0, 1.0, 1.5, 2.0}};
        final double[] b = {0.25, 0.6366, 0.3466, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);
        final double step = 0.1;
        new RungeKuttaIntegrator(name, c, a, b, prototype, step);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        final RungeKuttaIntegrator rk = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        final FirstOrderDifferentialEquations equations = new TestEquations();
        final double[] y0 = {1.0, 1.0};
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = {2.71828, 2.71828};
        rk.integrate(equations, t0, y0, t1, y1);
        Assert.assertArrayEquals(y1, y0, 0.001);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test(expected = DeriveException.class)
    public void testIntegrateWithDeriveException() throws Exception {
        final RungeKuttaIntegrator rk = new RungeKuttaIntegrator("test", new double[0], new double[0][], new double[0], null, 0.1) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Test Derivative Exception");
            }
        };
        final FirstOrderDifferentialEquations equations = new TestEquations();
        final double[] y0 = {1.0, 1.0};
        final double t0 = 0.0;
        final double t1 = 1.0;
        rk.integrate(equations, t0, y0, t1, null);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws Exception {
        final RungeKuttaIntegrator rk = new RungeKuttaIntegrator("test", new double[0], new double[0][], new double[0], null, 0.1) {
            @Override
            public void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) {
                throw new IntegratorException("Test Integrator Exception");
            }
        };
        final FirstOrderDifferentialEquations equations = new TestEquations();
        final double[] y0 = {1.0, 1.0};
        final double t0 = 0.0;
        final double t1 = 1.0;
        rk.integrate(equations, t0, y0, t1, null);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrateWithRootFunction() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = {1.0, 1.0};
        final double t1 = Math.PI;
        final double[] y1 = {Math.cos(Math.PI), Math.sin(Math.PI)};
        final RootFunction rootFunction = new RootFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double bracketRoot(double x1, double x2) {
                if (Math.abs(value(x1) - 0.5) < Math.abs(value(x2) - 0.5)) {
                    return x1;
                } else {
                    return x2;
                }
            }

            @Override
            public double bracketDerivative(double x1, double x2) {
                if (Math.abs(value(x1) - 0.5) < Math.abs(value(x2) - 0.5)) {
                    return Math.cos(x1) - 1.0;
                } else {
                    return Math.cos(x2);
                }
            }
        };
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, int stepNumber) {
                if (stepNumber % 100 == 0) {
                    final double stepSize = integrator.getStepSize();
                    if (stepSize > 1.0e-10) {
                        integrator.setStepSize(stepSize / 2.0);
                    }
                }
            }
        };
        final Event event = new Event() {
            @Override
            public double value(double t) {
                return Math.sin(t) - 0.5;
            }
        };
        final RungeKuttaIntegrator rk = new RungeKuttaIntegrator("test", new double[0], new double[0][], new double[0], new RungeKuttaStepInterpolator(4), 0.1) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws IntegratorException {
                if (y[0] < 0.0 || y[1] < 0.0) {
                    throw new IntegratorException("The initial conditions cannot be negative.");
                }
            }
        };
        rk.setEquations(new TestEquations());
        rk.setStepHandlers(stepHandler);
        rk.setEvents(event);
        rk.integrate(new TestEquations(), t0, y0, t1, null);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrateWithDiscontinuity() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = {1.0, 1.0};
        final double t1 = 1.0;
        final double[] y1 = {Math.exp(1.0), Math.exp(1.0)};
        final RootFunction rootFunction = new RootFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    throw new MathIllegalArgumentException("x cannot be negative: " + x);
                }
                return Math.exp(x) - 0.5;
            }

            @Override
            public double bracketRoot(double x1, double x2) {
                if (value(x1) < 0.0) {
                    return x1;
                } else {
                    return x2;
                }
            }

            @Override
            public double bracketDerivative(double x1, double x2) {
                if (value(x1) < 0.0) {
                    return Math.exp(x1) - 1.0;
                } else {
                    return Math.exp(x2);
                }
            }
        };
        final Event event = new Event() {
            @Override
            public double value(double t) {
                if (t < 0.0) {
                    throw new NoBracketsForSquareRootException("t cannot be negative: " + t);
                }
                return Math.exp(t) - 0.5;
            }
        };
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, int stepNumber) {
                if (stepNumber % 100 == 0) {
                    final double stepSize = integrator.getStepSize();
                    if (stepSize > 1.0e-10) {
                        integrator.setStepSize(stepSize / 2.0);
                    }
                }
            }
        };
        final RungeKuttaIntegrator rk = new RungeKuttaIntegrator("test", new double[0], new double[0][], new double[0], new RungeKuttaStepInterpolator(4), 0.1) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws IntegratorException {
                if (y[0] < 0.0 || y[1] < 0.0) {
                    throw new IntegratorException("The initial conditions cannot be negative.");
                }
            }
        };
        rk.setEquations(new TestEquations());
        rk.setStepHandlers(stepHandler);
        rk.setEvents(event);
        rk.integrate(new TestEquations(), t0, y0, t1, null);
    }

}