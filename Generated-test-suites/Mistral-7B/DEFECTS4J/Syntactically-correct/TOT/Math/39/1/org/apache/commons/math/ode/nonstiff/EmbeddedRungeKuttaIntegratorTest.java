package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.samples.LotkaVolterraSampleFunction;
import org.apache.commons.math3.special.BesselFunctions;

import java.util.Arrays;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the RK4 integrator with a simple function.
     */
    @org.junit.Test
    public void testRK4() {
        final double[][] coefficients = new double[][] {
            { 0.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 },
            { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0, 1.0 / 12.0, 1.0 / 12.0, 1.0 / 12.0, 1.0 / 24.0 }
        };
        final double minStep = 0.001;
        final double maxStep = 1e10;
        final double absTol = 1e-13;
        final double relTol = 1e-8;

        final EmbeddedRungeKuttaIntegrator rk4 = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients[1],
                                                                                 RungeKuttaStepInterpolator.RK4, minStep, maxStep, absTol, relTol);

        final ExpandableStatefulODE function = new LotkaVolterraSampleFunction();
        final double initialTime = 0.0;
        final double[] initialState = new double[] { 1.0, 1.0 };
        final double finalTime = 5.0;

        rk4.integrate(function, initialTime + 0.1);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.21373, 1.21373 }),
                     rk4.getEquations().getCompleteState());

        rk4.integrate(function, initialTime + 0.5);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.49983, 1.5 }),
                     rk4.getEquations().getCompleteState());

        rk4.integrate(function, initialTime + 1.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 2.04653, 2.1 }),
                     rk4.getEquations().getCompleteState());

        rk4.integrate(function, initialTime + 2.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 3.17443, 3.45332 }),
                     rk4.getEquations().getCompleteState());

        rk4.integrate(function, finalTime - 1.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 10.68388, 13.38143 }),
                     rk4.getEquations().getCompleteState());

        assertThrows(IllegalArgumentException.class, () -> rk4.integrate(function, -1.0));
        assertThrows(IllegalArgumentException.class, () -> rk4.integrate(function, 1e100));

        rk4.setMaxGrowth(1.1);
        rk4.integrate(function, initialTime + 1.1);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 2.21753, 2.39142 }),
                     rk4.getEquations().getCompleteState());

        rk4.setMinReduction(0.1);
        rk4.integrate(function, initialTime + 0.01);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.0014, 1.0005 }),
                     rk4.getEquations().getCompleteState());

    }

    /**
     * Test the RKF45 integrator with a simple function.
     */
    @org.junit.Test
    public void testRKF45() {
        final double[][] coefficients = new double[][] {
            { 0.0, 0.3, 0.382, 0.556, 0.833, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 },
            { 0.236927, 0.049012, 0.045591, 0.171315, 0.232174, 0.24563, 0.04227, 0.0, 0.0, 0.0, 0.0 },
            { 0.0, 0.14123, 0.46022, 0.53447, 0.27762, 0.07132, 0.0, 0.0, 0.0, 0.0, 0.0 }
        };
        final double minStep = 0.001;
        final double maxStep = 1e10;
        final double absTol = 1e-13;
        final double relTol = 1e-8;

        final EmbeddedRungeKuttaIntegrator rkf45 = new EmbeddedRungeKuttaIntegrator("RKF45", true, coefficients[0], coefficients[1],
                                                                                  RungeKuttaStepInterpolator.RKF45, minStep, maxStep, absTol, relTol);

        final ExpandableStatefulODE function = new LotkaVolterraSampleFunction();
        final double initialTime = 0.0;
        final double[] initialState = new double[] { 1.0, 1.0 };
        final double finalTime = 5.0;

        rkf45.integrate(function, initialTime + 0.1);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.21367, 1.21405 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, initialTime + 0.5);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.50001, 1.50001 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, initialTime + 1.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.99993, 2.00003 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, initialTime + 2.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 3.00004, 3.00005 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, finalTime - 1.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 10.68391, 13.38151 }),
                     rkf45.getEquations().getCompleteState());

        assertThrows(IllegalArgumentException.class, () -> rkf45.integrate(function, -1.0));
        assertThrows(IllegalArgumentException.class, () -> rkf45.integrate(function, 1e100));

        rkf45.setMaxGrowth(1.1);
        rkf45.integrate(function, initialTime + 1.1);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 2.21764, 2.39152 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.setMinReduction(0.1);
        rkf45.integrate(function, initialTime + 0.01);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.0014, 1.0005 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.setSafety(0.8);
        rkf45.integrate(function, initialTime + 0.1);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.21414, 1.21369 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, initialTime + 0.5);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.49999, 1.50001 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, initialTime + 1.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 1.99996, 2.00005 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, initialTime + 2.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 3.00008, 3.00007 }),
                     rkf45.getEquations().getCompleteState());

        rkf45.integrate(function, finalTime - 1.0);
        assertEquals(Arrays.toString(function.getCompleteState()), Arrays.toString(new double[] { 10.68412, 13.38166 }),
                     rkf45.getEquations().getCompleteState());

        final double[] yp = new double[] { 1.0, 1.0 };
        assertThrows(MathIllegalArgumentException.class, () -> rkf45.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] state, double[] yp) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getCompleteState() {
                return state;
            }
        }, initialTime + 0.1));

        final double[] y2p = new double[] { 1.0, 1.0 };
        assertThrows(MathIllegalStateException.class, () -> rkf45.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] state, double[] yp) {
                yp[0] = FastMath.sin(BesselFunctions.I0(t));
                yp[1] = FastMath.cos(BesselFunctions.I1(t));
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getCompleteState() {
                return y2p;
            }
        }, initialTime + 0.1, y2p));

    }

}