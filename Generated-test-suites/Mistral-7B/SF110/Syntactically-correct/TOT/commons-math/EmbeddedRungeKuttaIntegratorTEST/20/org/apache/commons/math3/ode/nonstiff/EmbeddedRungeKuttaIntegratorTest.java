package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.noisy.NoisyFunction;
import org.apache.commons.math3.ode.noisy.NoisyRK4Integrator;
import org.apache.commons.math3.ode.noisy.NoisyState;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Arrays;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK2Integrator() {
        EmbeddedRungeKuttaIntegrator rk2 = new EmbeddedRungeKuttaIntegrator("RK2", true, new double[] { 0.5, 0.5 }, new double[][] { { 0, 2.0 }, { 0.25, 0.75 } }, new double[] { 0.0, 1.0 }, new RungeKuttaStepInterpolator(new RK2StepInterpolator()));
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }
        };
        StepInterpolator interpolator = new RungeKuttaStepInterpolator(rk2.prototype);
        double[] initialState = new double[] { 1.0 };
        double time = 1.0;

        rk2.integrate(equations, time);
        double[] finalState = equations.getCompleteState();
        double expectedFinalState = Precision.round(Math.exp(1.0), 15);
        assertEquals(expectedFinalState, finalState[0], 1e-14);

        // Test error handling
        try {
            rk2.integrate(equations, -1.0);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        // Test step size control
        rk2.setMinStep(0.001);
        rk2.setMaxStep(0.01);
        rk2.integrate(equations, time);
        finalState = equations.getCompleteState();
        double expectedStepSize = 0.001;
        assertEquals(expectedStepSize, rk2.getStepSize(), 1e-14);

        // Test with a noisy function
        NoisyFunction noisyFunction = new NoisyFunction() {
            @Override
            public double value(double t, double[] state) {
                return state[0] * Math.exp(-t);
            }

            @Override
            public double noise(double t, double[] state) {
                return Math.random() * 0.1;
            }
        };
        NoisyState noisyState = new NoisyState(initialState, noisyFunction);
        NoisyRK4Integrator noisyRk4 = new NoisyRK4Integrator(noisyState);
        double[] noisyInitialState = noisyState.getState();
        time = 1.0;
        noisyRk4.integrate(equations, time);
        double[] noisyFinalState = noisyState.getState();
        expectedFinalState = Precision.round(Math.exp(1.0), 15);
        assertEquals(expectedFinalState, noisyFinalState[0], 1e-14);

        EmbeddedRungeKuttaIntegrator rk2Noisy = new EmbeddedRungeKuttaIntegrator("RK2Noisy", true, new double[] { 0.5, 0.5 }, new double[][] { { 0, 2.0 }, { 0.25, 0.75 } }, new double[] { 0.0, 1.0 }, new RungeKuttaStepInterpolator(new RK2StepInterpolator()), 0.001, 0.01, null, null) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + noisyFunction.noise(t, y);
            }
        };
        interpolator = new RungeKuttaStepInterpolator(rk2Noisy.prototype);
        rk2Noisy.integrate(equations, time);
        finalState = equations.getCompleteState();
        assertEquals(expectedFinalState, finalState[0], 1e-14);
    }

    private static class RK2StepInterpolator extends RK2StepInterpolatorAdapter {

        @Override
        public double[] interpolate(double t, double[] y, double[] yDot1, double[] yDot2, double[] yDot3, double[] yDot4, double[] y) {
            return new double[] { y[0] + this.coef1 * yDot1[0] + this.coef2 * yDot2[0] };
        }
    }
}