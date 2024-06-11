package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.verifiers.InitialStepSizeVerifier;
import org.apache.commons.math3.ode.verifiers.StepSizeVerifier;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test with a simple ODE function
        double[] y0 = new double[]{1.0, 1.0};
        ODEFunction simpleODE = new ODEFunction() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                y[0] = y[1];
                y[1] = 2.0 * y[1];
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(double t, double[] y) {
                System.out.println("t = " + t + ", y = " + FastUtil.arrayToString(y));
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta54Integrator(1.0, 1.0, simpleODE, new RungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12, stepHandler);
        integrator.integrate(0.0, 10.0, y0);

        // Test initial step size verification
        InitialStepSizeVerifier isv = new InitialStepSizeVerifier(integrator, new double[][]{0.0, 1.0}, new double[]{1.0, 1.0}, 0.0);
        assertTrue(isv.isInitialStepSizeValid());

        // Test step size verification
        StepSizeVerifier sv = new StepSizeVerifier(integrator, 1.0e-12, 1.0e-12);
        double tolerance = 1.0e-12;
        double maxError = 0.0;
        double step = 0.1;
        double t = 0.0;
        double[] y = y0.clone();
        do {
            integrator.integrate(t, t + step, y);
            double[] yExpected = new double[]{y[0] + step * simpleODE.computeDerivatives(t, y)[0], y[1] + step * simpleODE.computeDerivatives(t, y)[1]};
            double error = Precision.relativeltol(yExpected[0], y[0]);
            if (error > maxError) {
                maxError = error;
            }
            t += step;
            step *= 0.5;
        } while (Math.abs(t - 10.0) > 1.0e-12);
        sv.assertErrorAtMost(maxError);

        // Test error scenarios
        try {
            integrator.integrate(0.0, -1.0, y0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            integrator.integrate(0.0, 10.0, null);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            integrator.integrate(0.0, 10.0, y0.clone());
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        try {
            integrator.integrate(0.0, 10.0, y0);
            integrator.integrate(1.0, 10.0, y0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }
}