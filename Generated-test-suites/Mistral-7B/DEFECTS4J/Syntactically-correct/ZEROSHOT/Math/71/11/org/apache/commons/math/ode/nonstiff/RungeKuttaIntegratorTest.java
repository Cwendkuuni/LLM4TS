package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.ode.noalias.ODETestUtils.createMockStepHandler;
import static org.apache.commons.math3.ode.noalias.ODETestUtils.createMockDifferentialEquations;
import static org.apache.commons.math3.ode.noalias.ODETestUtils.assertArrayClose;

import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegratorTest;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Test for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest extends RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() throws Exception {
        RungeKuttaIntegrator rk1 = new EulerIntegrator("Euler", new double[] { 0.0 }, new double[][] { { 1.0 } }, new double[] { 1.0 }, new RungeKuttaStepInterpolator(1), 0.01);
        RungeKuttaIntegrator rk2 = new ClassicalRungeKuttaIntegrator("RK2", new double[] { 0.0, 0.5 }, new double[][] { { 1.0/2.0, 1.0/2.0 }, { 1.0/2.0, 1.0 } }, new double[] { 1.0/2.0, 1.0 }, new RungeKuttaStepInterpolator(2), 0.01);
        RungeKuttaIntegrator rk3 = new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/2.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 } }, new double[] { 1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0/6.0 }, new RungeKuttaStepInterpolator(4), 0.01) {
            @Override
            protected boolean isAutomaticStepSizeAccepted(double step) {
                return step <= 0.1;
            }
        };

        testIntegrator(rk1, createMockDifferentialEquations(0.0, 1.0, 1.0, x -> new double[] { x, 1.0 }), 0.01, 0.1, 0.5, 0.5001);
        testIntegrator(rk2, createMockDifferentialEquations(0.0, 1.0, 1.0, x -> new double[] { x, Math.exp(x) }), 0.01, 1.0, 1.0, 1.3678794411714423);
        testIntegrator(rk3, createMockDifferentialEquations(0.0, 1.0, 1.0, x -> new double[] { x, Math.exp(x) }), 0.01, 1.0, 1.0, 1.4986984841385956);

        try {
            new RungeKuttaIntegrator("RK3", null, null, null, null, 0.01);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, null, null, null, 0.01);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, null }, null, null, 0.01);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 } }, null, null, 0.01);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0 } }, new double[] { 1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0/6.0 }, new RungeKuttaStepInterpolator(5), 0.01);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0 } }, new double[] { 1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0/6.0 }, new RungeKuttaStepInterpolator(5), -0.01);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0 } }, new double[] { 1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0/6.0 }, new RungeKuttaStepInterpolator(5), 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRungeKuttaIntegratorStepHandler() {
        RungeKuttaIntegrator rk3 = new RungeKuttaIntegrator("RK3", new double[] { 0.0, 1.0/3.0, 1.0/2.0, 1.0 }, new double[][] { { 1.0/2.0, 1.0/2.0, 1.0/2.0, 1.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0 }, { 1.0 } }, new double[] { 1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0/6.0 }, new RungeKuttaStepInterpolator(5), 0.01);
        StepHandler stepHandler = createMockStepHandler(1, 0.1, 0.11, 0.12, 0.13, 0.14);
        rk3.addStepHandler(stepHandler);

        double[] y0 = { 0.0, 0.0 };
        double tStart = 0.0;
        double tEnd = 1.0;
        double[] yEnd = { 1.0, 1.1 };
        double step = 0.01;

        rungeKuttaIntegratorTest(rk3, createMockDifferentialEquations(0.0, 1.0, 1.0, x -> new double[] { x, x }), stepHandler, tStart, y0, tEnd, yEnd, step);

        // check that step handler has been called
        double[][] expectedYDot = new double[][] { { 1.0, 1.0 }, { 1.2, 1.3 }, { 1.32, 1.4728 }, { 1.4608, 1.6330272 } };
        for (int i = 0; i < stepHandler.getCallCount(); ++i) {
            double[] y = stepHandler.getY(i);
            double[] yDot = stepHandler.getYDot(i);
            assertArrayClose(expectedYDot[i % 4], y, 1e-12);
            assertArrayClose(yDot, yDotK[i % 4], 1e-12);
        }
    }

}