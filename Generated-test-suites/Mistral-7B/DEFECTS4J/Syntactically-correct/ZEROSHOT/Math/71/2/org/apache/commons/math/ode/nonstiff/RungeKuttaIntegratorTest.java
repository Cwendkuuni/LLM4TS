package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {1.0, 1.0}};
        double[] b = {0.3, 0.6, 0.1, 0.0};
        RungeKuttaIntegrator rk = new RungeKuttaIntegrator("RK4", c, a, b, new RK4Method(), 0.01) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                super.sanityChecks(equations, t0, y0, t, y);
                assertArrayEquals(y0, y, 0.0001); // sanity check initial conditions
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = E.E(0.5) * y[0];
            }
        };

        double tStart = 0.0;
        double yStart = 0.1;
        double tEnd = 1.0;
        double[] yEnd = {1.718281828459045};

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                assertEquals(yEnd[0], context.getY(), 0.0001);
            }
        };

        RungeKuttaIntegrator.RungeKuttaStepInterpolator interpolator = rk.getStepInterpolator();
        interpolator.setStepHandler(stepHandler);

        try {
            rk.integrate(equations, tStart, new double[]{yStart}, tEnd, null);
            fail("Expected IntegratorException due to yEnd is not null");
        } catch (IntegratorException e) {
            // expected
        }

        rk.integrate(equations, tStart, new double[]{yStart}, tEnd, yEnd);
    }

}

class RK4Method extends RungeKuttaStepInterpolator {
    @Override
    public int getOrder() {
        return 4;
    }
}