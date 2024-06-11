package org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator; // Make sure to use the correct package name

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.*;

public class EmbeddedRungeKuttaIntegratorTest {

    // Assuming EmbeddedRungeKuttaIntegrator is extended by a specific Runge-Kutta method, e.g., EmbeddedRK23
    @Test
    public void testIntegrate() throws Exception {
        double[][] yInit = {1.0, 1.0}; // Initial conditions
        double tStart = 0.0, tEnd = 1.0, stepSize = 0.01;
        ExpandableODE equations = new DummyODE() {
            @Override
            public int getDimension() {
                return yInit.length;
            }

            @Override
            public String getName() {
                return "Dummy ODE";
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK23Integrator(
                "Embedded RK23",
                true,
                new double[]{0.5, 0.5, 0.5},
                new double[][]{{0.5, 0.5}, {0.5, 0.5}, {1.0, 1.0}},
                new double[]{0.5, 0.5},
                new RungeKuttaStepInterpolator(new EmbeddedRungeKuttaMethod() {
                    @Override
                    public int getOrder() {
                        return 2;
                    }
                }), 0.01, 0.1, new double[]{1e-10, 1e-10}, new double[]{1e-10, 1e-10});

        integrator.integrate(equations, tStart);
        StepHandler sh = equations.createStepHandler(tEnd, 1000);
        integrator.addStepHandler(sh);
        integrator.integrate(equations, tEnd);

        assertEquals(tEnd, equations.getTime(), 1e-10);
        double[] expectedFinalValues = {Math.exp(1.0), Math.exp(2.0)};
        assertArrayEquals(expectedFinalValues, equations.getCompleteState(), 1e-10);

        // Reset to initial conditions
        equations.setCompleteState(yInit);
        integrator.reset();
        integrator.integrate(equations, tStart);
    }

    @Test
    public void testMinReductionAndMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK45Integrator(
                "Embedded RK45",
                true,
                new double[]{0.3, 0.3, 0.3, 0.3, 0.3, 0.3},
                new double[][]{{0.3333333333333333, 0.3333333333333333, 0.3333333333333333, 0.3, 0.3, 0.3},
                              {0.3, 0.3333333333333333, 0.3333333333333333, 0.3333333333333333, 0.3, 0.3},
                              {0.3, 0.3, 0.3333333333333333, 0.3333333333333333, 0.3, 0.3},
                              {0.3, 0.3, 0.3, 0.3333333333333333, 0.3333333333333333, 0.3333333333333333},
                              {0.3, 0.3, 0.3, 0.3, 0.3333333333333333, 0.3333333333333333},
                              {0.3, 0.3, 0.3, 0.3, 0.3, 0.34375}},
                new double[]{0.5, 0.5},
                new RungeKuttaStepInterpolator(new EmbeddedRungeKuttaMethod() {
                    @Override
                    public int getOrder() {
                        return 5;
                    }
                }), 0.01, 0.1, new double[]{1e-10, 1e-10}, new double[]{1e-10, 1e-10});

        assertEquals(0.9, integrator.getSafety(), 1e-6);
        assertEquals(0.2, integrator.getMinReduction(), 1e-6);
        assertEquals(10.0, integrator.getMaxGrowth(), 1e-6);

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(5.0);
        assertEquals(0.1, integrator.getMinReduction(), 1e-6);
        assertEquals(5.0, integrator.getMaxGrowth(), 1e-6);
    }
}