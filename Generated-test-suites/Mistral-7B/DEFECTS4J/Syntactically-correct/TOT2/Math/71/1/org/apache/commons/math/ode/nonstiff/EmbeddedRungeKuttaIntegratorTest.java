package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test object.
     */
    private EmbeddedRungeKuttaIntegrator integrator;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaMethodImpl(
                "RK23", false, new double[] { 0.25, 0.5 },
                new double[][] { { 0.25, 0.5 }, { 0.5, 0.25 } },
                new double[] { 0.25, 0.5 }, new DummyStepInterpolator(),
                0.01, 0.04, new double[] { 1.0e-8, 1.0e-8 },
                new double[] { 1.0e-8, 1.0e-8 });
    }

    /**
     * Test method getOrder().
     */
    @Test
    public void testGetOrder() {
        int order = integrator.getOrder();
        assertEquals(2, order);
    }

    /**
     * Test method integrate() with simple differential equation.
     */
    @Test
    public void testIntegrateSimple() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0 = 0.0;
        double  t1 = 2.0;

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertEquals(t1, result, 1.0e-12);

        assertArrayEquals(new double[] { 2.718281828 }, y0, 1.0e-12);
    }

    /**
     * Test method integrate() with differential equation with events.
     */
    @Test
    public void testIntegrateWithEvent() throws Exception {
        double[] y0 = new double[] { 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations = new SinTestDifferentialEquations();

        integrator.addStepHandler(new MyStepHandler());

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        assertEquals(1.0, integrator.getNumberOfStepsTaken(), 1.0);
        assertEquals(1.0, integrator.getStepSize(), 1.0e-12);
    }

    /**
     * Test method integrate() with differential equation that throws an exception.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateException() throws Exception {
        double[] y0 = new double[] { 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations = new ExceptionThrowingDifferentialEquations();

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
    }

    /**
     * Test method integrate() with differential equation that returns null derivatives.
     */
    @Test(expected = MathUnsupportedOperationException.class)
    public void testIntegrateNullDerivatives() throws Exception {
        double[] y0 = new double[] { 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations = new NullDerivativesDifferentialEquations();

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
    }

    /**
     * Test method integrate() with differential equation that returns invalid
     * derivatives.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInvalidDerivatives() throws Exception {
        double[] y0 = new double[] { 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations = new InvalidDerivativesDifferentialEquations();

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
    }

    /**
     * Test method integrate() with differential equation that fails to converge.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateFailureToConverge() throws Exception {
        double[] y0 = new double[] { 0.0 };
        double  t0 = 0.0;
        double  t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FailureToConvergeDifferentialEquations();

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
    }

}

class MyStepHandler implements StepHandler {

    private int stepCount;

    @Override
    public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
        stepCount++;
    }

}

class DummyStepInterpolator extends AbstractStepInterpolator {

    @Override
    public void storeTime(double time) {
        super.storeTime(time);
    }

    @Override
    public double interpolate(double time) {
        throw new UnsupportedOperationException();
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = Math.exp(t);
    }

}

class SinTestDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = Math.sin(t);
    }

}

class ExceptionThrowingDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IllegalArgumentException();
    }

}

class NullDerivativesDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime = null;
    }

}

class InvalidDerivativesDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = t;
    }

}

class FailureToConvergeDifferentialEquations implements FirstOrderDifferentialEquations {

    private double lastError = 1.0;

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        double error = 0.1;
        lastError += error;
        yPrime[0] = Math.exp(-t) / lastError;
    }

}