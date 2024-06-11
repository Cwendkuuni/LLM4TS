package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() throws Exception {
        double[] y0 = new double[]{0.0, 1.0};
        double[][] a = {{0.0, 0.0, 0.3, 0.6}, {0.3, 0.6, 0.4, 0.8}, {0.6, 0.8, 0.8, 1.0}};
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0};
        double c = 0.05;
        double[] cValues = {c};
        double[] scalAbsoluteTolerance = {1e-12};
        double[] scalRelativeTolerance = {1e-6};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b, cValues, 1, 0.0, 0.0);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, cValues, a, b, prototype, c, c, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        integrator.integrate(equations, 0.0);
        assertEquals(y0[0], equations.getCompleteState()[0], 1e-12);
        assertEquals(y0[1], equations.getCompleteState()[1], 1e-12);

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getCompleteState()[0], 1e-12);
        assertEquals(-1.0, equations.getCompleteState()[1], 1e-12);

        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, -1.0));
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[])}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator2() throws Exception {
        double[] y0 = new double[]{0.0, 1.0};
        double[][] a = {{0.0, 0.0, 0.3, 0.6}, {0.3, 0.6, 0.4, 0.8}, {0.6, 0.8, 0.8, 1.0}};
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0};
        double c = 0.05;
        double[] cValues = {c};
        double[] scalAbsoluteTolerance = {1e-12};
        double[] scalRelativeTolerance = {1e-6};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b, cValues, 1, 0.0, 0.0);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, cValues, a, b, prototype, c, c, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1] + Math.exp(-t);
                yDot[1] = y[0] * Math.sin(t);
            }
        };

        integrator.integrate(equations, 0.0);
        assertEquals(0.0, equations.getCompleteState()[0], 1e-12);
        assertEquals(0.0, equations.getCompleteState()[1], 1e-12);

        integrator.integrate(equations, 1.0);
        assertEquals(1.718281828, equations.getCompleteState()[0], 1e-11);
        assertEquals(0.628318531, equations.getCompleteState()[1], 1e-11);

        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, -1.0));
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#embeddedRungeKuttaStep(RungeKuttaStepInterpolator, double[], double[], double[], double[], double[], double[], boolean)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaStep() throws Exception {
        double[] y0 = new double[]{0.0, 1.0};
        double[][] a = {{0.0, 0.0, 0.3, 0.6}, {0.3, 0.6, 0.4, 0.8}, {0.6, 0.8, 0.8, 1.0}};
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0};
        double c = 0.05;
        double[] cValues = {c};
        double[] scalAbsoluteTolerance = {1e-12};
        double[] scalRelativeTolerance = {1e-6};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a, b, cValues, 1, 0.0, 0.0);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, cValues, a, b, prototype, c, c, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        double[] y = y0.clone();
        double[] yDot = new double[y.length];
        double[] yDotK = new double[6][y.length];
        double[] yTmp = new double[y.length];
        double[] yDotTmp = new double[y.length];
        double stepStart = 0.0;
        double hNew = 0.0;

        integrator.embeddedRungeKuttaStep(prototype, y, yDot, yDotK, yTmp, yDotTmp, stepStart, y0, hNew, false);
        assertEquals(y0[0], y[0], 1e-12);
        assertEquals(y0[1], y[1], 1e-12);
        assertEquals(y0[0], yTmp[0], 1e-12);
        assertEquals(y0[1], yTmp[1], 1e-12);

        integrator.embeddedRungeKuttaStep(prototype, y, yDot, yDotK, yTmp, yDotTmp, stepStart, y, hNew, true);
        assertEquals(y[0], yTmp[0], 1e-12);
        assertEquals(y[1], yTmp[1], 1e-12);

        integrator.embeddedRungeKuttaStep(prototype, y, yDot, yDotK, yTmp, yDotTmp, stepStart, y, hNew, false);
        assertEquals(y[0], y[0], 1e-12);
        assertEquals(y[1], y[1], 1e-12);

        integrator.resetInternalState();

        integrator.embeddedRungeKuttaStep(prototype, y, yDot, yDotK, yTmp, yDotTmp, stepStart, y, hNew, false);
        assertEquals(y[0], y[0], 1e-12);
        assertEquals(y[1], y[1], 1e-12);
    }

}