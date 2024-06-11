package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbedgedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinStep() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), -0.001, 0.01, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMaxStep() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, -0.01, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidScalAbsoluteTolerance() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, new double[]{-0.001}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidScalRelativeTolerance() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, new double[]{-0.2});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithInvalidEquationsDimension() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // empty
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getCompleteState() {
                return new double[2];
            }

            @Override
            public void setTime(double t) {
                // empty
            }

            @Override
            public void setCompleteState(double[] y) {
                // empty
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);

        integrator.integrate(equations, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStepSize() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(1, () -> new double[]{0.1, 0.1});

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);

        integrator.integrate(equations, 1.0, 0.0001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(1, () -> new double[]{0.1, 0.1});

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);

        integrator.integrate(equations, 1.0, 1000000);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws Exception {
        ExpandableStatefulODE equations = new DummyODE(1, () -> new double[]{0.1, 0.1});

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Integrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0}, {0.25, 0.25}, {0.25, 0.5}, {0.25, 0.75}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(new RK4Integrator(), 0.0, 0.01, 0.0), 0.001, 0.01, null, null);

        integrator.integrate(equations, 0.0, 1.0);
    }

    private static class DummyODE implements ExpandableStatefulODE {
        private final int dimension;
        private final Function<Double, Double[]> yFunction;

        public DummyODE(int dimension, Function<Double, Double[]> yFunction) {
            this.dimension = dimension;
            this.yFunction = yFunction;
        }

        @Override
        public int getDimension() {
            return dimension;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot = yFunction.apply(t);
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public double[] getCompleteState() {
            return new double[dimension];
        }

        @Override
        public void setTime(double t) {
            // empty
        }

        @Override
        public void setCompleteState(double[] y) {
            // empty
        }
    }

    private interface Function<T, R> {
        R apply(T t);
    }
}