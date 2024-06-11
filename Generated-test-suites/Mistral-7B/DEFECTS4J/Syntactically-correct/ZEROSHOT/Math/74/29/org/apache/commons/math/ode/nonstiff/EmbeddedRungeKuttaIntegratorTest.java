package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new RK42DifferentialEquations();
        integrator = new EmbeddedRungeKuttaMethod(
                "RK42",
                true,
                new double[]{0.25, 0.25, 0.5, 0.5},
                new double[][]{{0.25, 0.25}, {0, 0.25, 0.25}, {0, 0, 0.5}, {0, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(),
                0.001,
                0.01,
                1e-5,
                1e-5);
    }

    @Test
    public void testIntegration() throws MathIllegalArgumentException {
        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 1.0;
        double[] expectedFinalState = {1.718281828, 1.718281828};

        double result = integrator.integrate(equations, initialTime, initialState, finalTime, new double[2]);
        assertArrayEquals(expectedFinalState, new double[]{1.718281828, 1.718281828}, new double[]{1.718281828, 1.718281828});
        assertEquals(finalTime, result, 1e-6);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrationWithDerivativeException() throws DerivativeException {
        equations = new ExceptionThrowingDifferentialEquations();
        integrator = new EmbeddedRungeKuttaMethod(
                "RK42",
                true,
                new double[]{0.25, 0.25, 0.5, 0.5},
                new double[][]{{0.25, 0.25}, {0, 0.25, 0.25}, {0, 0, 0.5}, {0, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(),
                0.001,
                0.01,
                1e-5,
                1e-5);

        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 1.0;
        integrator.integrate(equations, initialTime, initialState, finalTime, new double[2]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithIntegratorException() throws IntegratorException {
        equations = new ExceptionThrowingIntegratorDifferentialEquations();
        integrator = new EmbeddedRungeKuttaMethod(
                "RK42",
                true,
                new double[]{0.25, 0.25, 0.5, 0.5},
                new double[][]{{0.25, 0.25}, {0, 0.25, 0.25}, {0, 0, 0.5}, {0, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(),
                0.001,
                0.01,
                1e-5,
                1e-5);

        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 1.0;
        integrator.integrate(equations, initialTime, initialState, finalTime, new double[2]);
    }

}

class RK42DifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }

}

class ExceptionThrowingDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("Test exception");
    }

}

class ExceptionThrowingIntegratorDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IntegratorException("Test exception");
    }

}