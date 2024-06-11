package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final double[][] a = {{0.0, 0.5, 0.5, 0.5}, {0.5, 0.5, 1.0, 0.5}, {0.5, 1.0, 0.5, 1.0}, {0.5, 1.0, 1.0, 2.0}};
        final double[] b = {0.25, 0.25, 0.5, 0.5, 0.5, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, new double[] {0.0, 0.5, 1.0, 1.0}, a, b, prototype, 1e-12, 1e-12) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                // custom error estimation logic here
                return 1.0;
            }
        };
        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return integrator.getOrder();
            }

            @Override
            public void initialize(double time, double[] state) {
                state[0] = 0.0;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = Math.exp(state[0]);
            }

            @Override
            public double getTime() {
                return state[0];
            }

            @Override
            public void setTime(double time) {
                state[0] = time;
            }

            @Override
            public void setCompleteState(double[] state) {
                this.state = state;
            }
        };

        final double startTime = 0.0;
        final double endTime = 1.0;

        integrator.integrate(equations, startTime);
        assertTrue(Math.abs(equations.getCompleteState()[0] - 1.0) < 1e-6);

        integrator.integrate(equations, endTime);
        assertTrue(Math.abs(equations.getCompleteState()[0] - Math.E) < 1e-6);

        try {
            integrator.integrate(equations, Double.NaN);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            integrator.integrate(equations, stepSize + 1.0);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }

        try {
            integrator.integrate(equations, startTime - 1.0);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }

        try {
            integrator.integrate(equations, endTime + 1.0);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }
    }

}