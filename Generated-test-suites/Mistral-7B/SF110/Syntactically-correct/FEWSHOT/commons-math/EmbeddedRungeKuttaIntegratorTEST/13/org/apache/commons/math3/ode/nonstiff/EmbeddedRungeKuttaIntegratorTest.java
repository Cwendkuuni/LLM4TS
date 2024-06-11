package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.6}, new double[][]{{0.683, -0.316, 1.0}, {0.633, 0.347, 0.678}, {0.0, 0.623, 0.623}, {0.373, -0.467, 0.437}}, new double[]{0.075, 0.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethodImpl(new DormandPrince853Method())), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 8;
            }

            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Implement the error estimation method here
                // For simplicity, we will just return 1.0 in this test
                return 1.0;
            }
        };

        GoalType goalType = GoalType.RELATIVE;
        double[] initialState = new double[]{1.0, 1.0};
        double[][] expectedStates = new double[][]{{1.01, 1.01}, {1.02, 1.02}, {1.03, 1.03}, {1.04, 1.04}, {1.05, 1.05}};
        double t = 5.0;

        Equation[] equations = new Equation[]{new DummyEquation()};
        ExpandableStatefulODE ode = new ExpandableStatefulODE(equations, initialState, null, null, null);

        integrator.integrate(ode, t);

        assertEquals(5, ode.getNumberOfStepsTaken());
        for (int i = 0; i < expectedStates.length; i++) {
            assertArrayEquals(expectedStates[i], ode.getCompleteState(), 0.001);
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.6}, new double[][]{{0.683, -0.316, 1.0}, {0.633, 0.347, 0.678}, {0.0, 0.623, 0.623}, {0.373, -0.467, 0.437}}, new double[]{0.075, 0.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethodImpl(new DormandPrince853Method())), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 8;
            }

            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Implement the error estimation method here
                // For simplicity, we will just return 1.0 in this test
                return 1.0;
            }
        };

        GoalType goalType = GoalType.ABSOLUTE;
        double[] initialState = new double[]{1.0, 1.0};
        double t = 0.0;

        Equation[] equations = new Equation[]{new DummyEquation()};
        ExpandableStatefulODE ode = new ExpandableStatefulODE(equations, initialState, null, null, null);

        integrator.integrate(ode, t);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.6}, new double[][]{{0.683, -0.316, 1.0}, {0.633, 0.347, 0.678}, {0.0, 0.623, 0.623}, {0.373, -0.467, 0.437}}, new double[]{0.075, 0.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethodImpl(new DormandPrince853Method())), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 8;
            }

            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Implement the error estimation method here
                // For simplicity, we will just return 1.0 in this test
                return 1.0;
            }
        };

        GoalType goalType = GoalType.RELATIVE;
        double[] initialState = new double[]{1.0};
        double t = 5.0;

        Equation[] equations = new Equation[]{new DummyEquation()};
        ExpandableStatefulODE ode = new ExpandableStatefulODE(equations, initialState, null, null, null);

        integrator.integrate(ode, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.6}, new double[][]{{0.683, -0.316, 1.0}, {0.633, 0.347, 0.678}, {0.0, 0.623, 0.623}, {0.373, -0.467, 0.437}}, new double[]{0.075, 0.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethodImpl(new DormandPrince853Method())), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 8;
            }

            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Implement the error estimation method here
                // For simplicity, we will just return 1.0 in this test
                return 1.0;
            }
        };

        GoalType goalType = GoalType.RELATIVE;
        double[] initialState = new double[]{1.0, 1.0};
        double t = 100.0;

        Equation[] equations = new Equation[]{new DummyEquation()};
        ExpandableStatefulODE ode = new ExpandableStatefulODE(equations, initialState, null, null, null);

        integrator.integrate(ode, t);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.6}, new double[][]{{0.683, -0.316, 1.0}, {0.633, 0.347, 0.678}, {0.0, 0.623, 0.623}, {0.373, -0.467, 0.437}}, new double[]{0.075, 0.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethodImpl(new DormandPrince853Method())), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 8;
            }

            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Implement the error estimation method here
                // For simplicity, we will just return 1.0 in this test
                return 1.0;
            }
        };

        GoalType goalType = GoalType.RELATIVE;
        double[] initialState = new double[]{1.0, 1.0};
        double t = 2.0;

        Equation[] equations = new Equation[]{new DummyEquation()};
        ExpandableStatefulODE ode = new ExpandableStatefulODE(equations, initialState, null, null, null);

        integrator.setMinStep(Double.MAX_VALUE);
        integrator.integrate(ode, t);
    }

    @Test
    public void testSettersAndGetters() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.6}, new double[][]{{0.683, -0.316, 1.0}, {0.633, 0.347, 0.678}, {0.0, 0.623, 0.623}, {0.373, -0.467, 0.437}}, new double[]{0.075, 0.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethodImpl(new DormandPrince853Method())), 0.001, 0.01, null, null) {
            @Override
            protected int getOrder() {
                return 8;
            }

            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Implement the error estimation method here
                // For simplicity, we will just return 1.0 in this test
                return 1.0;
            }
        };

        assertEquals(true, integrator.isFSAL());
        assertEquals(0.3, integrator.getC()[0], 0.0);
        assertEquals(0.3, integrator.getC()[1], 0.0);
        assertEquals(0.3, integrator.getC()[2], 0.0);
        assertEquals(0.6, integrator.getC()[3], 0.0);

        double[][] a = integrator.getA();
        assertEquals(4, a.length);
        assertEquals(3, a[0].length);
        assertEquals(3, a[1].length);
        assertEquals(3, a[2].length);
        assertEquals(3, a[3].length);

        assertEquals(0.075, integrator.getB()[0], 0.0);
        assertEquals(0.0, integrator.getB()[1], 0.0);

        RungeKuttaStepInterpolator prototype = integrator.getPrototype();
        assertEquals(8, prototype.getOrder());

        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);

        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);

        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.0);

        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }

    private static class DummyEquation implements Equation {
        public double[] evaluateDerivative(double time, double[] state) {
            double[] derivatives = new double[state.length];
            for (int i = 0; i < derivatives.length; i++) {
                derivatives[i] = state[i];
            }
            return derivatives;
        }

        public double value(double time, double[] state) {
            double value = 0.0;
            for (double s : state) {
                value += s;
            }
            return value;
        }
    }
}