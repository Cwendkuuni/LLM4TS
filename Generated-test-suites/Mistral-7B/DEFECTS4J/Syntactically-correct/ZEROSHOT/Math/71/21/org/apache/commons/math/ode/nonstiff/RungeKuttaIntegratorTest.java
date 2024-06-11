package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoIntegratorFoundException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        integrator = new RungeKutta4Integrator(
                0.01,
                new double[] {0.25, 0.45, 0.5, 0.55},
                new double[][] {{0.25, 0.25, 0.25, 0.25},
                                {0,       0.45, 0.5,   0.55},
                                {0.5,     0.5,   0.5,   0.5},
                                {0.25,    0.25, 0.25,  0.25}},
                new double[] {0.25, 0.5, 0.25, 0.25},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public double[] interpolate(double t, double[] ys, double[] ysPrimes) {
                        return ys;
                    }
                },
                0.01);
    }

    @Test
    public void testIntegrate() throws Exception {
        double[] initialValues = {0.1};
        double initialTime = 0.0;
        double finalTime = 0.5;

        integrator.integrate(equations, initialTime, initialValues, finalTime, new double[initialValues.length]);

        double[] expectedValues = {0.3679731};
        Assert.assertArrayEquals(expectedValues, initialValues, 0.00001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithNegativeStepSize() {
        integrator.integrate(equations, 0.0, new double[] {0.1}, -0.01, new double[1]);
    }

    @Test(expected = NoIntegratorFoundException.class)
    public void testIntegrateWithEmptyStepper() {
        RungeKuttaIntegrator emptyIntegrator = new RungeKuttaIntegrator("EmptyIntegrator",
                new double[] {0}, new double[][] {}, new double[] {},
                new RungeKuttaStepInterpolator() {}, 0.01) {
            @Override
            public boolean integratesDenseOutput() {
                return false;
            }
        };
        emptyIntegrator.integrate(equations, 0.0, new double[] {0.1}, 0.1, new double[1]);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        double[] initialValues = {0.1};
        double initialTime = 0.0;
        double finalTime = 1.0;

        integrator.addStepHandler(step -> {
            if (step > 0.5) {
                throw new IllegalStateException("Unexpected step at " + step);
            }
        });

        integrator.addEvent(new When(0.25, event -> {
            equations.setEquations(new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = 3 * y[0];
                }
            });
            return null;
        }));

        integrator.integrate(equations, initialTime, initialValues, finalTime, new double[initialValues.length]);

        double[] expectedValues = {0.1184243, 0.3559281, 1.0};
        Assert.assertArrayEquals(expectedValues, integrator.getStepHandler().getLastState(), 0.00001);
    }

}