package org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator; // package for testing

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegratorInitialization() {
        double[] c = {0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = {1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, 1.0E-8, 1.0E-8);
        assertEquals(1.0, integrator.getOrder());
        assertEquals(true, integrator.isFullyImplicit());

        EmbeddedRungeKuttaIntegrator integrator2 = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, new double[]{1.0E-8, 1.0E-8}, new double[]{1.0E-8, 1.0E-8});
        assertEquals(1.0, integrator2.getOrder());
        assertEquals(true, integrator2.isFullyImplicit());
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorIntegration() throws Exception {
        double[] c = {0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = {1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, 1.0E-8, 1.0E-8);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                y[0] = y[1];
                y[1] = 0.1 * y[1] + 2.0 * Math.sin(y[0]);
            }
        };

        double[] initialState = new double[]{2.0, 0.1};

        integrator.integrate(equations, 10.0);

        double[] finalState = equations.getCompleteState();
        double expectedY = 3.7360139574692831;
        double expectedX = 10.0;

        assertEquals(expectedY, finalState[0], Precision.SAFE_DOUBLE);
        assertEquals(expectedX, equations.getTime(), Precision.SAFE_DOUBLE);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorIntegrationException() throws Exception {
        double[] c = {0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = {1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, 1.0E-8, 1.0E-8);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                throw new IllegalStateException("Test exception");
            }
        };

        double[] initialState = new double[]{2.0};

        assertThrows(IllegalStateException.class, () -> integrator.integrate(equations, 10.0));
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorNullParameters() throws Exception {
        double[] c = null;
        double[][] a = null;
        double[] b = null;
        RungeKuttaStepInterpolator prototype = null;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, 1.0E-8, 1.0E-8);

        assertNull(integrator);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorNegativeParameters() throws Exception {
        double[] c = new double[]{-0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = new double[]{-1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, 1.0E-8, 1.0E-8);

        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(new ExpandableStatefulODE() {}, 10.0));
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorZeroStepSize() throws Exception {
        double[] c = new double[]{0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = new double[]{1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.0, 0.05, 1.0E-8, 1.0E-8);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                y[0] = y[1];
                y[1] = 0.1 * y[1] + 2.0 * Math.sin(y[0]);
            }
        };

        double[] initialState = new double[]{2.0, 0.1};

        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, 10.0));
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorNonStiffExample() throws Exception {
        double[] c = new double[]{0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = new double[]{1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.05, 1.0E-8, 1.0E-8);

        double[] initialState = new double[]{0.0, 1.0};

        integrator.integrate(new TestNonStiffODE(), 10.0);

        double[] finalState = integrator.getState();
        double expectedX = 10.0;
        double expectedY = 2.718281828;

        assertEquals(expectedY, finalState[0], Precision.SAFE_DOUBLE);
        assertEquals(expectedX, integrator.getTime(), Precision.SAFE_DOUBLE);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorAdaptiveStepsize() throws Exception {
        double[] c = new double[]{0.3, 0.4, 0.5, 0.6};
        double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0}, {0.3, 0.0, 0.0, 0.0}, {0.27, 0.3, 0.0, 0.0}, {0.168000000000000003, 0.48, 0.3, 0.0}};
        double[] b = new double[]{1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, 0.01, 1.0E-8);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.1, 1.0, null, null);

        double[] initialState = new double[]{0.0, 1.0};

        integrator.integrate(new TestNonStiffODE(), 10.0);

        double[] finalState = integrator.getState();
        double expectedX = 10.0;
        double expectedY = 2.718281828;

        assertEquals(expectedY, finalState[0], Precision.SAFE_DOUBLE);
        assertEquals(expectedX, integrator.getTime(), Precision.SAFE_DOUBLE);

        StepInterpolator interpolator = integrator.getInterpolator();
        double[] predictedState = new double[2];
        double predictedTime = integrator.predictState(11.0, predictedState);

        assertEquals(expectedX, predictedTime, Precision.SAFE_DOUBLE);
        assertEquals(expectedY * Math.exp(1.0), predictedState[0], Precision.SAFE_DOUBLE);
    }

    private static class TestNonStiffODE implements ExpandableStatefulODE {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y) {
            double x = y[0];
            double y1 = y[1];

            y[0] = x;
            y[1] = y1 + Math.exp(-1.0) * y1;
        }
    }
}