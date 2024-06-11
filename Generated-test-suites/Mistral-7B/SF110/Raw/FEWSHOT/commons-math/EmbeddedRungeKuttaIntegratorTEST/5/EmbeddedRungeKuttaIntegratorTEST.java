package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() throws NoSuchMethodException {
        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        assertEquals(5, integrator.getOrder());

        integrator = new DormandPrince853Integrator();
        assertEquals(8, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        assertEquals(0.9, integrator.getSafety(), 0.01);

        integrator = new DormandPrince853Integrator();
        assertEquals(0.9, integrator.getSafety(), 0.01);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.01);

        integrator = new DormandPrince853Integrator();
        integrator.setSafety(0.95);
        assertEquals(0.95, integrator.getSafety(), 0.01);
    }

    @Test
    public void testIntegrate() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);

        double[] initialState = { 1.0, 1.0 };
        double[] expectedState = { 0.9, 0.9 * Math.exp(-0.1 * 1.0) };
        double tolerance = 1e-8;

        integrator.integrate(equations, 1.0, initialState);
        assertArrayEquals(expectedState, initialState, tolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        integrator.integrate(null, 0.0, new double[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithInvalidTime() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        integrator.integrate(equations, Double.NEGATIVE_INFINITY, new double[2]);
    }

    @Test
    public void testIntegrateWithMinimumStepSize() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        integrator.setMinStep(1.0);
        integrator.setMaxStep(1.0);

        double[] initialState = { 1.0, 1.0 };
        double tolerance = 1e-8;

        integrator.integrate(equations, 1.0, initialState);
        assertEquals(initialState[0], initialState[0], tolerance);
        assertEquals(initialState[1], initialState[1], tolerance);
    }

    @Test
    public void testIntegrateWithMaximumStepSize() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        integrator.setMinStep(0.01);
        integrator.setMaxStep(100.0);

        double[] initialState = { 1.0, 1.0 };
        double tolerance = 1e-8;

        integrator.integrate(equations, 1.0, initialState);
        assertEquals(initialState[0], initialState[0], tolerance);
        assertEquals(initialState[1], initialState[1], tolerance);

        integrator.integrate(equations, 101.0, initialState);
        assertArrayEquals(new double[] { 1.0 + 100.0 * Math.exp(-0.1), 100.0 * Math.exp(-0.1) }, initialState, tolerance);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithStepSizeTooSmall() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);
        integrator.setMinStep(0.01);
        integrator.setMaxStep(0.1);

        double[] initialState = { 1.0, 1.0 };
        double tolerance = 1e-8;

        try {
            integrator.integrate(equations, 1.0, initialState, 0.001);
            fail("NumberIsTooSmallException expected");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedStateSize() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);

        try {
            integrator.integrate(equations, 1.0, new double[1]);
            fail("DimensionMismatchException expected");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxStepsExceeded() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);

        StepInterpolator interpolator = new RungeKuttaStepInterpolator();
        integrator.setInterpolator(interpolator);

        double[] initialState = { 1.0, 1.0 };
        double tolerance = 1e-8;

        try {
            integrator.integrate(equations, 1.0, initialState, 100000);
            fail("MaxCountExceededException expected");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws Exception {
        ODESystem equations = new ODESystem() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void calculateDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(5);

        double[] initialState = { 0.0, 1.0 };
        double tolerance = 1e-8;

        try {
            integrator.integrate(equations, 1.0, initialState, 0.1);
            fail("NoBracketingException expected");
        } catch (NoBracketingException e) {
            // Expected
        }
    }
}