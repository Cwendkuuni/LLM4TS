package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

public class EmbeddedRungeKuttaIntegratorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] a = {{-0.33333333333333333, 0.33333333333333333, -0.2}, {0.33333333333333333, 0.33333333333333333, 0.6}},
               b = {-0.25, 0.6};
        double[] c = {0.0, 0.5};
        double exp = -1.0 / 4.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, 1e-6, 1e-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                double sum = 0.0;
                for (int i = 0; i < p0[0].length; ++i) {
                    sum += Math.pow(Math.abs(p2[i] - p1[i]), 2.0);
                }
                return Math.sqrt(sum);
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        try {
            integrator.integrate(equations, 1.0);
            fail("Expected NumberIsTooSmallException or MaxCountExceededException");
        } catch (NumberIsTooSmallException | MaxCountExceededException e) {
            // Expected
        }

        integrator.setMaxStep(2.0);
        integrator.integrate(equations, 1.0);
        double[] y = equations.getCompleteState();
        assertEquals(y[0], 0.70710678118654758, 1e-5);
        assertEquals(y[1], -0.70710678118654758, 1e-5);

        integrator.setMinStep(0.1);
        integrator.integrate(equations, 2.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 1.26538535865423416, 1e-5);
        assertEquals(y[1], -1.26538535865423416, 1e-5);

        integrator.setMaxGrowth(1.5);
        integrator.integrate(equations, 3.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 1.82173235881772324, 1e-5);
        assertEquals(y[1], -1.82173235881772324, 1e-5);

        integrator.setMinReduction(0.1);
        equations.setTime(1.5);
        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 1.9223585993203043, 1e-5);
        assertEquals(y[1], -1.9223585993203043, 1e-5);

        integrator.setSafety(0.5);
        equations.setTime(1.8);
        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 2.01556368917595443, 1e-5);
        assertEquals(y[1], -2.01556368917595443, 1e-5);

        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public double[] getVecAbsoluteTolerance() {
                return new double[]{1e-6, 1e-6};
            }

            @Override
            public double[] getVecRelativeTolerance() {
                return new double[]{1e-6, 1e-6};
            }
        };

        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 0.70710678118654758, 1e-6);
        assertEquals(y[1], -0.70710678118654758, 1e-6);

        integrator.setMaxStep(2.0);
        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 1.41421374121356235, 1e-6);
        assertEquals(y[1], -1.41421374121356235, 1e-6);

        integrator.setMinStep(0.1);
        integrator.integrate(equations, 2.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 2.82842712474619038, 1e-6);
        assertEquals(y[1], -2.82842712474619038, 1e-6);

        integrator.setMaxGrowth(1.5);
        integrator.integrate(equations, 3.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 4.0824829046386356, 1e-6);
        assertEquals(y[1], -4.0824829046386356, 1e-6);

        integrator.setMinReduction(0.1);
        equations.setTime(3.0);
        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 4.3675374279955336, 1e-6);
        assertEquals(y[1], -4.3675374279955336, 1e-6);

        integrator.setSafety(0.5);
        equations.setTime(4.0);
        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 4.7325366762571284, 1e-6);
        assertEquals(y[1], -4.7325366762571284, 1e-6);

        integrator = new EmbeddedRungeKuttaMethod("RKF45", new double[]{1.0, 0.5}, new double[][]{{-0.33333333333333333, 0.33333333333333333, -0.2}, {0.33333333333333333, 0.33333333333333333, 0.6}}, new double[]{-0.25, 0.6});
        integrator.setStepSize(0.01);
        integrator.setMinStep(0.001);
        integrator.setMaxStep(2.0);
        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        equations = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        integrator.integrate(equations, 1.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 0.70710678118654758, 1e-6);
        assertEquals(y[1], -0.70710678118654758, 1e-6);

        integrator.integrate(equations, 2.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 1.41421374121356235, 1e-6);
        assertEquals(y[1], -1.41421374121356235, 1e-6);

        integrator.integrate(equations, 3.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 2.16506351762688785, 1e-6);
        assertEquals(y[1], -2.16506351762688785, 1e-6);

        integrator.integrate(equations, 4.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 2.88602367193709139, 1e-6);
        assertEquals(y[1], -2.88602367193709139, 1e-6);

        integrator.integrate(equations, 5.0);
        y = equations.getCompleteState();
        assertEquals(y[0], 3.68025333333303714, 1e-6);
        assertEquals(y[1], -3.68025333333303714, 1e-6);
    }
}