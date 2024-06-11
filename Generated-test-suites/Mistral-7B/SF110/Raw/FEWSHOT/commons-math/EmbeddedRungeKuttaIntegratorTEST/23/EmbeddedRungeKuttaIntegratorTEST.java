package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolationException;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        EmbeddedRungeKuttaMethod rk4 = new EmbeddedRungeKutta5_4();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "Embedded Runge-Kutta Integrator 5(4)",
                true,
                new double[]{0.3, 0.2, 0.2, 0.1, 0.1, 0.1, 0.1},
                new double[][]{{0.683, 0.36}, {0.3, 0.4736}, {0.36, 0.0488}, {0.238, 0.0}, {0.0, 0.236}},
                new double[]{0.25, 0.25, 0.25, 0.25, 0.0},
                rk4,
                0.001,
                0.01,
                1e-9,
                1e-9) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                double error = 0.0;
                for (int i = 0; i < p2.length; i++) {
                    double diff = Math.abs(p2[i] - p1[i]);
                    error += Math.pow(diff, 2);
                }
                return Math.sqrt(error);
            }
        };

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(double t, double[] y, double[] dy) {
                System.out.println("t = " + t + ", y = " + Arrays.toString(y) + ", dy = " + Arrays.toString(dy));
            }
        };

        final double[] initialValues = {0.0, 1.0};

        try {
            integrator.integrate(new ExpandableStatefulODE() {
                @Override
                public int getDimension() {
                    return initialValues.length;
                }

                @Override
                public String getIdentifier() {
                    return "Test ODE";
                }

                @Override
                public void calculateDerivatives(double t, double[] state, double[] derivatives) {
                    derivatives[0] = state[1];
                    derivatives[1] = -state[0];
                }

                @Override
                public void reset() {
                    // no-op
                }
            }, 0.0);
        } catch (Exception e) {
            fail("Exception while integrating ODE: " + e.getMessage());
        }

        try {
            integrator.integrate(new ExpandableStatefulODE() {
                @Override
                public int getDimension() {
                    return initialValues.length;
                }

                @Override
                public String getIdentifier() {
                    return "Test ODE";
                }

                @Override
                public void calculateDerivatives(double t, double[] state, double[] derivatives) {
                    derivatives[0] = state[1] * 2;
                    derivatives[1] = -state[0] + state[1];
                }

                @Override
                public void reset() {
                    // no-op
                }
            }, 2.0, handler);
        } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException | NoBracketingException | StepInterpolationException e) {
            fail("Exception while integrating ODE with step handler: " + e.getMessage());
        }
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        assertEquals(5, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        assertEquals(0.9, integrator.getSafety(), 0.01);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSafetyNegative() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        integrator.setSafety(-1.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        assertEquals(0.2, integrator.getMinReduction(), 0.01);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinReductionNegative() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        integrator.setMinReduction(-1.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.01);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxGrowthNegative() {
        EmbeddedRungeKutta5_4 integrator = new EmbeddedRungeKutta5_4();
        integrator.setMaxGrowth(-1.0);
    }
}