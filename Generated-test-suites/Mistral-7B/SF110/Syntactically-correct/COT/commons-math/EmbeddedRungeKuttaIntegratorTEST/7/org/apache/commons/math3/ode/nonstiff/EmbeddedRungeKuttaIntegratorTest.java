package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(1, 2, 3, 4, 5, new RungeKuttaStepInterpolator());
        assertEquals(1, integrator.getOrder());
        integrator = new MyEmbeddedRungeKuttaIntegrator(2, 2, 2, 2, 2, new RungeKuttaStepInterpolator());
        assertEquals(2, integrator.getOrder());
        integrator = new MyEmbeddedRungeKuttaIntegrator(3, 3, 3, 3, 3, new RungeKuttaStepInterpolator());
        assertEquals(3, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(1, 2, 3, 4, 5, new RungeKuttaStepInterpolator(), 0.1, 0.2, 0.001, 0.0001);
        assertEquals(0.9, integrator.getSafety(), 0.001);
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinStep() {
        new MyEmbeddedRungeKuttaIntegrator("Name", true, null, null, null, new RungeKuttaStepInterpolator(), -1.0, 0.2, 0.001, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullC() {
        new MyEmbeddedRungeKuttaIntegrator("Name", true, null, null, null, new RungeKuttaStepInterpolator(), 0.1, 0.2, 0.001, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullA() {
        double[][] a = new double[1][];
        new MyEmbeddedRungeKuttaIntegrator("Name", true, null, a, null, new RungeKuttaStepInterpolator(), 0.1, 0.2, 0.001, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullB() {
        new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, null, null, new RungeKuttaStepInterpolator(), 0.1, 0.2, 0.001, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullPrototype() {
        new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, null, 0.1, 0.2, 0.001, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.1, 0.2, 0.001, 0.0001);
        try {
            integrator.integrate(null, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithInvalidT() {
        ExpandableStatefulODE equations = new DummyEquations();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.1, 0.2, 0.001, 0.0001);
        try {
            integrator.integrate(equations, -1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrateWithSimpleEquations() {
        ExpandableStatefulODE equations = new DummyEquations();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 0.001, 0.0001);
        double initialValue = 1.0;
        double[] expected = {1.1, 1.2};
        integrator.integrate(equations, initialValue);
        double[] actual = equations.getCompleteState();
        assertArrayEquals(expected, actual, 0.001);
    }

    @Test
    public void testIntegrateWithNonSimpleEquations() {
        ExpandableStatefulODE equations = new ComplexEquations();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0, 1.0}, new double[][]{{}}, new double[]{1.0, 1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 0.001, 0.0001);
        double initialValue[] = {1.0, 1.0};
        double[] expected = {1.2, 1.4};
        integrator.integrate(equations, initialValue);
        double[] actual = equations.getCompleteState();
        assertArrayEquals(expected, actual, 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithSmallStep() {
        ExpandableStatefulODE equations = new DummyEquations();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.0001, 0.1, 0.001, 0.0001);
        try {
            integrator.integrate(equations, 0.0);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() {
        ExpandableStatefulODE equations = new ComplexEquationsWithMismatchedDimensions();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0, 1.0}, new double[][]{{}}, new double[]{1.0, 1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 0.001, 0.0001);
        try {
            integrator.integrate(equations, new double[]{1.0});
            fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithTooManySteps() {
        ExpandableStatefulODE equations = new DummyEquations();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 0.001, 0.0001);
        try {
            integrator.integrate(equations, 1000000.0);
            fail();
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() {
        ExpandableStatefulODE equations = new DummyEquationsWithNoBracketing();
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Name", true, new double[]{1.0}, new double[][]{{}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 0.001, 0.0001);
        try {
            integrator.integrate(equations, 1.0);
            fail();
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    private static class DummyEquations implements ExpandableStatefulODE {
        private double time;
        private double[] state;

        @Override
        public void addVariables(ODEStateAndDerivative state) {
            state.variables = new double[]{state[0]};
        }

        @Override
        public void initialize(double t, double[] state) {
            this.time = t;
            this.state = state;
        }

        @Override
        public void computeDerivatives(double t, double[] state, double[] derivatives) {
            derivatives[0] = state[0];
        }

        @Override
        public double getTime() {
            return this.time;
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public double[] getCompleteState() {
            return this.state;
        }

        @Override
        public String getName() {
            return "DummyEquations";
        }
    }

    private static class ComplexEquations implements ExpandableStatefulODE {
        @Override
        public void addVariables(ODEStateAndDerivative state) {
            state.variables = new double[]{state[0], state[1]};
        }

        @Override
        public void initialize(double t, double[] state) {
        }

        @Override
        public void computeDerivatives(double t, double[] state, double[] derivatives) {
            derivatives[0] = state[0];
            derivatives[1] = state[1] * 2.0;
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{0.0, 0.0};
        }

        @Override
        public String getName() {
            return "ComplexEquations";
        }
    }

    private static class ComplexEquationsWithMismatchedDimensions implements ExpandableStatefulODE {
        @Override
        public void addVariables(ODEStateAndDerivative state) {
            state.variables = new double[]{state[0], state[1], state[2]};
        }

        @Override
        public void initialize(double t, double[] state) {
        }

        @Override
        public void computeDerivatives(double t, double[] state, double[] derivatives) {
            derivatives[0] = state[0];
            derivatives[1] = state[1] * 2.0;
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{0.0};
        }

        @Override
        public String getName() {
            return "ComplexEquationsWithMismatchedDimensions";
        }
    }

    private static class DummyEquationsWithNoBracketing implements ExpandableStatefulODE {
        @Override
        public void addVariables(ODEStateAndDerivative state) {
            state.variables = new double[]{state[0]};
        }

        @Override
        public void initialize(double t, double[] state) {
        }

        @Override
        public void computeDerivatives(double t, double[] state, double[] derivatives) {
            derivatives[0] = 0.0;
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{1.0};
        }

        @Override
        public String getName() {
            return "DummyEquationsWithNoBracketing";
        }
    }

    private static class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        }

        @Override
        protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
            // Implement your custom error estimation here
            return 0.0;
        }
    }
}