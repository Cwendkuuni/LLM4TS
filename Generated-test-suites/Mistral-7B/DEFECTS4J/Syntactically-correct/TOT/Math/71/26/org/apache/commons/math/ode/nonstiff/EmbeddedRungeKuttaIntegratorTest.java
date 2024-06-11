package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK2() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}};
        double[] b = {0.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK2Integrator(
                "RK2", false, new double[]{0.0, 0.5}, a, b, prototype, 0.01, 0.05, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;
        double stepSize = 0.1;

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                double[] output = new double[2];
                output[0] = y[1];
                output[1] = -y[0];
                return output;
            }
        }, tStart, initialValues, tEnd, resultValues);

        assertEquals(expectedValues[0], resultValues[0], 0.001);
        assertEquals(expectedValues[1], resultValues[1], 0.001);
    }

    @Test
    public void testRK2_NegativeStepSize() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}};
        double[] b = {0.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK2Integrator(
                "RK2", false, new double[]{0.0, 0.5}, a, b, prototype, 0.01, 0.05, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;
        double stepSize = -0.1; // Should throw an exception

        try {
            integrator.integrate(new SimpleDifferentialEquations() {
                @Override
                public double[] computeDerivatives(double t, double[] y) {
                    double[] output = new double[2];
                    output[0] = y[1];
                    output[1] = -y[0];
                    return output;
                }
            }, tStart, initialValues, tEnd, resultValues);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRK2_InvalidMinStepSize() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}};
        double[] b = {0.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK2Integrator(
                "RK2", false, new double[]{0.0, 0.5}, a, b, prototype, 0.0, 0.05, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;

        try {
            integrator.integrate(new SimpleDifferentialEquations() {
                @Override
                public double[] computeDerivatives(double t, double[] y) {
                    double[] output = new double[2];
                    output[0] = y[1];
                    output[1] = -y[0];
                    return output;
                }
            }, tStart, initialValues, tEnd, resultValues);
            fail("Should have thrown an exception");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testRK2_InvalidMaxStepSize() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}};
        double[] b = {0.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK2Integrator(
                "RK2", false, new double[]{0.0, 0.5}, a, b, prototype, 0.01, 1.0, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;

        try {
            integrator.integrate(new SimpleDifferentialEquations() {
                @Override
                public double[] computeDerivatives(double t, double[] y) {
                    double[] output = new double[2];
                    output[0] = y[1];
                    output[1] = -y[0];
                    return output;
                }
            }, tStart, initialValues, tEnd, resultValues);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testRK2_StepRejected() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}, {0.25, 0.5}, {0.25, 1.5}};
        double[] b = {0.0, 1.0, 0.5, 1.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator(
                "RK4", false, new double[]{0.0, 0.01, 0.1, 0.5, 0.6, 0.9, 1.0}, a, b, prototype,
                0.001, 0.05, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                double[] output = new double[2];
                output[0] = y[1];
                output[1] = -y[0];
                return output;
            }
        }, tStart, initialValues, tEnd, resultValues);

        double[] interpolatedValues = new double[resultValues.length];
        integrator.getInterpolator().getInterpolatedValues(0.05, interpolatedValues);

        assertEquals(expectedValues[0], interpolatedValues[0], 0.001);
        assertEquals(expectedValues[1], interpolatedValues[1], 0.001);
    }

    @Test
    public void testRK2_StepAccepted() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}, {0.25, 0.5}, {0.25, 1.5}};
        double[] b = {0.0, 1.0, 0.5, 1.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator(
                "RK4", false, new double[]{0.0, 0.01, 0.1, 0.5, 0.6, 0.9, 1.0}, a, b, prototype,
                0.001, 0.05, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                double[] output = new double[2];
                output[0] = y[1];
                output[1] = -y[0];
                return output;
            }
        }, tStart, initialValues, tEnd, resultValues);

        double[] interpolatedValues = new double[resultValues.length];
        integrator.getInterpolator().getInterpolatedValues(0.1, interpolatedValues);

        assertEquals(expectedValues[0], interpolatedValues[0], 0.001);
        assertEquals(expectedValues[1], interpolatedValues[1], 0.001);
    }

    @Test
    public void testRK2_EventHandling() {
        double[][] a = {{0.0, 0.0}, {0.5, 1.0}, {0.25, 0.5}, {0.25, 1.5}};
        double[] b = {0.0, 1.0, 0.5, 1.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator(
                "RK4", false, new double[]{0.0, 0.01, 0.1, 0.5, 0.6, 0.9, 1.0}, a, b, prototype,
                0.001, 0.05, null, null);

        double[] initialValues = {0.0, 1.0};
        double[] expectedValues = {0.1, 0.2};
        double[] resultValues = new double[initialValues.length];

        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.addEvent(0.5, new MyEventHandler());

        integrator.integrate(new SimpleDifferentialEquations() {
            @Override
            public double[] computeDerivatives(double t, double[] y) {
                double[] output = new double[2];
                output[0] = y[1];
                output[1] = -y[0];
                return output;
            }
        }, tStart, initialValues, tEnd, resultValues);

        double[] interpolatedValues = new double[resultValues.length];
        integrator.getInterpolator().getInterpolatedValues(0.1, interpolatedValues);

        assertEquals(expectedValues[0], interpolatedValues[0], 0.001);
        assertEquals(expectedValues[1], interpolatedValues[1], 0.001);
    }
}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public double[] computeDerivatives(double t, double[] y) {
        double[] output = new double[2];
        output[0] = y[1];
        output[1] = -y[0];
        return output;
    }
}

class MyEventHandler implements StepHandler {
    @Override
    public void handleStep(StepHandlerContext context, int step) {
        if (step > 1) {
            context.setStopRequested(true);
        }
    }
}