package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] a = { new double[] { 0.0, 0.4, 0.0 },
                         new double[] { 0.0, 0, 0.5, 0.5 },
                         new double[] { 0.0, 1 / 3.0, 1 / 3.0, 1 / 3.0, 2 / 3.0 } };
        double[] c = { 1.0 / 2.0, 1 / 3.0, 1 / 2.0 };
        double[] b = { 1 / 2.0, 0.25, 0.6 };

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                "RK23", false, c, a, b, prototype, 1e-8, 1e-8);

        assertEquals(2, integrator.getOrder());
        assertEquals(false, integrator.isFsal());

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getPrimaryDimension() {
                return 1;
            }

            @Override
            public int getSecondaryDimension() {
                return 0;
            }

            @Override
            public void initialize(double time) {
                state = 1.0;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] yDot) {
                yDot[0] = Math.exp(-time) * state[0];
            }

            @Override
            public double getTime() {
                return stateTime;
            }

            @Override
            public void setTime(double time) {
                stateTime = time;
            }

            @Override
            public double[] getCompleteState() {
                return state;
            }

            double[] state = new double[]{1.0};
            double stateTime = 0.0;
        };

        integrator.integrate(equations, 2.0);
        assertEquals(2.0, equations.getTime(), Precision.TOLERANCE);
        assertEquals(0.6366197723675814, equations.getCompleteState()[0], Precision.TOLERANCE);

        // Test with invalid arguments
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    null, false, c, a, b, prototype, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK23", true, null, a, b, prototype, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK23", false, c, null, b, prototype, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK23", false, c, a, null, prototype, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK23", false, c, a, b, null, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalStateException.class, () -> {
            integrator.integrate(equations, Double.POSITIVE_INFINITY);
        });
        assertThrows(MathIllegalStateException.class, () -> {
            integrator.integrate(equations, Double.NEGATIVE_INFINITY);
        });
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator2() {
        double[][] a = { new double[] { 0.0, 0.3, 0.0 },
                         new double[] { 0.0, 0, 0.9, 0.3 },
                         new double[] { 0.0, 1 / 3.0, 1 / 3.0, 1 / 3.0, 2 / 3.0 } };
        double[] c = { 1.0 / 2.0, 1 / 3.0, 1 / 2.0 };
        double[] b = { 1 / 2.0, 0.4, 0.6 };

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                "RK45", true, c, a, b, prototype, 1e-8, new double[] {1e-8, 1e-8}, new double[]{1e-7, 1e-8});

        assertEquals(5, integrator.getOrder());
        assertEquals(true, integrator.isFsal());

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getPrimaryDimension() {
                return 1;
            }

            @Override
            public int getSecondaryDimension() {
                return 0;
            }

            @Override
            public void initialize(double time) {
                state = 0.0;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] yDot) {
                yDot[0] = Math.sin(time) * state[0];
            }

            @Override
            public double getTime() {
                return stateTime;
            }

            @Override
            public void setTime(double time) {
                stateTime = time;
            }

            @Override
            public double[] getCompleteState() {
                return state;
            }

            double[] state = new double[]{1.0};
            double stateTime = 0.0;
        };

        integrator.integrate(equations, Math.PI);
        assertEquals(Math.PI, equations.getTime(), Precision.TOLERANCE);
        assertEquals(Math.sin(Math.PI), equations.getCompleteState()[0], Precision.TOLERANCE);

        // Test with invalid arguments
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    null, true, c, a, b, prototype, 1e-8, new double[] {1e-8, 1e-8}, new double[]{1e-7, 1e-8});
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK45", false, null, a, b, prototype, 1e-8, new double[] {1e-8, 1e-8}, new double[]{1e-7, 1e-8});
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK45", true, c, null, b, prototype, 1e-8, new double[] {1e-8, 1e-8}, new double[]{1e-7, 1e-8});
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK45", true, c, a, null, prototype, 1e-8, new double[] {1e-8, 1e-8}, new double[]{1e-7, 1e-8});
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegratorFactory().createInstance(
                    "RK45", true, c, a, b, null, 1e-8, new double[] {1e-8, 1e-8}, new double[]{1e-7, 1e-8});
        });
        assertThrows(MathIllegalStateException.class, () -> {
            integrator.integrate(equations, Double.POSITIVE_INFINITY);
        });
        assertThrows(MathIllegalStateException.class, () -> {
            integrator.integrate(equations, Double.NEGATIVE_INFINITY);
        });
    }

}