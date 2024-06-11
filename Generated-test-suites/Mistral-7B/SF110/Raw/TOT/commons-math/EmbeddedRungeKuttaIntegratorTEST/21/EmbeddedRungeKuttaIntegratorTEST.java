package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRK2Method;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRK4Method;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK2Integrator() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5}, new RungeKuttaStepInterpolator(new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5})));
        double[] initialState = {1.0, 1.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public double[] getCompleteState() {
                return initialState;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
            }

            @Override
            public void setCompleteState(double[] state) {
                System.arraycopy(state, 0, initialState, 0, state.length);
            }

            @Override
            public double[] getDerivatives(double time, double[] state) {
                double[] derivatives = new double[state.length];
                for (int i = 0; i < derivatives.length; i++) {
                    derivatives[i] = state[i] * 0.5;
                }
                return derivatives;
            }
        };

        integrator.integrate(equations, 2.0);
        double[] expectedFinalState = {2.01, 2.005};
        assertArrayEquals(expectedFinalState, equations.getCompleteState());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5}, new RungeKuttaStepInterpolator(new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5})));
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{0.0};
            }

            @Override
            public double getTime() {
                return -1.0;
            }

            @Override
            public void setTime(double time) {
            }

            @Override
            public void setCompleteState(double[] state) {
            }

            @Override
            public double[] getDerivatives(double time, double[] state) {
                return new double[]{1.0};
            }
        };

        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testRK4Integrator() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK4Method("RK4", false, new double[]{0.5, 0.5, 0.5, 0.5, 0}, new double[][]{{0.5, 0}, {0.25, 0.25}, {0.25, 0.25}, {0.5, 0}, {0, 1.0}}, new double[]{0, 0.5, 0, 0.5, 0}, new RungeKuttaStepInterpolator(new EmbeddedRK4Method("RK4", false, new double[]{0.5, 0.5, 0.5, 0.5, 0}, new double[][]{{0.5, 0}, {0.25, 0.25}, {0.25, 0.25}, {0.5, 0}, {0, 1.0}}, new double[]{0, 0.5, 0, 0.5, 0})));
        double[] initialState = {1.0, 1.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public double[] getCompleteState() {
                return initialState;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
            }

            @Override
            public void setCompleteState(double[] state) {
                System.arraycopy(state, 0, initialState, 0, state.length);
            }

            @Override
            public double[] getDerivatives(double time, double[] state) {
                double[] derivatives = new double[state.length];
                for (int i = 0; i < derivatives.length; i++) {
                    derivatives[i] = state[i] * 0.5;
                }
                return derivatives;
            }
        };

        integrator.integrate(equations, 4.0);
        double[] expectedFinalState = {2.1640625, 2.1294117647058825};
        assertArrayEquals(expectedFinalState, equations.getCompleteState());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5}, new RungeKuttaStepInterpolator(new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5})));
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{1.0, 1.0};
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
            }

            @Override
            public void setCompleteState(double[] state) {
            }

            @Override
            public double[] getDerivatives(double time, double[] state) {
                throw new DimensionMismatchException("ODE has dimension 2, but provided derivatives have dimension 1.");
            }
        };

        integrator.integrate(equations, 2.0);
    }

    @Test
    public void testMaxCountExceededException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5}, new RungeKuttaStepInterpolator(new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5})));
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{1.0};
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
            }

            @Override
            public void setCompleteState(double[] state) {
            }

            @Override
            public double[] getDerivatives(double time, double[] state) {
                throw new RuntimeException("Test exception");
            }

            @Override
            public int getStepCountLimit() {
                return 1;
            }
        };

        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(equations, 1.0));
    }

    @Test
    public void testNoBracketingException() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5}, new RungeKuttaStepInterpolator(new EmbeddedRK2Method("RK2", false, new double[]{0.5, 0}, new double[][]{{0.5, 0}, {0, 0.5}}, new double[]{0, 0.5})));
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{1.0};
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public void setTime(double time) {
            }

            @Override
            public void setCompleteState(double[] state) {
            }

            @Override
            public double[] getDerivatives(double time, double[] state) {
                return new double[]{0.0};
            }

            @Override
            public double getMinStepSize() {
                return 1.0;
            }

            @Override
            public double getMaxStepSize() {
                return 0.0;
            }
        };

        assertThrows(NoBracketingException.class, () -> integrator.integrate(equations, 1.0));
    }
}