package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.MockEventManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepHandler;
import org.junit.Test;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorConstruction() {
        final String methodName = "EmbeddedRungeKuttaIntegratorTest_testEmbeddedRungeKuttaIntegratorConstruction";
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                new double[] { 0.0, 0.3, 0.5, 0.8 },
                new double[][] { { 0.0, 0.3, 0.5, 0.8 }, { 0.3, 0.5, 0.8, 0.8 }, { 0.5, 0.8, 0.5, 0.3 }, { 0.8, 0.8, 0.5, 0.0 } },
                new double[] { 0.0, 0.5, 0.5, 0.5 },
                new RungeKuttaStepInterpolator(3, 5, 2), 0.001, 0.01,
                new double[] { 1e-10, 1e-12 },
                new double[] { 1e-14, 1e-16 }) {

            // Do nothing
        };

        // Test constructor arguments
        assertEquals("EmbeddedRungeKuttaIntegrator", integrator.getName());
        assertEquals(false, integrator.isFsal());
        assertArrayEquals(new double[] { 0.0, 0.3, 0.5, 0.8 }, integrator.getC(), 0);
        assertArrayEquals(new double[][] { { 0.0, 0.3, 0.5, 0.8 }, { 0.3, 0.5, 0.8, 0.8 }, { 0.5, 0.8, 0.5, 0.3 }, { 0.8, 0.8, 0.5, 0.0 } }, integrator.getA(), 0);
        assertArrayEquals(new double[] { 0.0, 0.5, 0.5, 0.5 }, integrator.getB(), 0);
        assertEquals(3, integrator.getOrder());
        assertSame(RungeKuttaStepInterpolator.class, integrator.getPrototype());
        assertEquals(0.001, integrator.getMinStep(), 0);
        assertEquals(0.01, integrator.getMaxStep(), 0);
        assertArrayEquals(new double[] { 1e-10, 1e-12 }, integrator.getScalAbsoluteTolerance(), 0);
        assertArrayEquals(new double[] { 1e-14, 1e-16 }, integrator.getScalRelativeTolerance(), 0);

    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorIntegration() {
        final String methodName = "EmbeddedRungeKuttaIntegratorTest_testEmbeddedRungeKuttaIntegratorIntegration";

        // Test simple integration
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                new double[] { 0.0, 0.3, 0.5, 0.8 },
                new double[][] { { 0.0, 0.3, 0.5, 0.8 }, { 0.3, 0.5, 0.8, 0.8 }, { 0.5, 0.8, 0.5, 0.3 }, { 0.8, 0.8, 0.5, 0.0 } },
                new double[] { 0.0, 0.5, 0.5, 0.5 },
                new RungeKuttaStepInterpolator(3, 5, 2), 0.001, 0.01,
                new double[] { 1e-10, 1e-12 },
                new double[] { 1e-14, 1e-16 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }

        };

        final double[] initialCondition = { 1.0, 1.0 };
        final double[] finalCondition    = { 2.0, 2.0 };
        final double   time              = 2.0;
        final double   tolerance          = 1e-9;

        final RungeKuttaStepHandler stepHandler = new RungeKuttaStepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                if (step > 1) {
                    throw new EventException("StepHandler should not be called more than once");
                }
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialCondition.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                for (int i = 0; i < derivatives.length; ++i) {
                    derivatives[i] = -y[i];
                }
            }
        };

        final MockEventManager eventManager = new MockEventManager();

        integrator.addStepHandler(stepHandler);
        integrator.setEquations(equations);
        integrator.setEventsHandlersManager(eventManager);

        try {
            integrator.integrate(equations, 0.0, initialCondition, time, finalCondition);
            fail("Integration should have failed due to events handler");
        } catch (IntegratorException e) {
            // Success
        }

        assertEquals(initialCondition[0], finalCondition[0], tolerance);
        assertEquals(initialCondition[1], finalCondition[1], tolerance);

    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with custom error estimation.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorCustomErrorEstimation() {
        final String methodName = "EmbeddedRungeKuttaIntegratorTest_testEmbeddedRungeKuttaIntegratorCustomErrorEstimation";

        // Test simple integration
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                new double[] { 0.0, 0.3, 0.5, 0.8 },
                new double[][] { { 0.0, 0.3, 0.5, 0.8 }, { 0.3, 0.5, 0.8, 0.8 }, { 0.5, 0.8, 0.5, 0.3 }, { 0.8, 0.8, 0.5, 0.0 } },
                new double[] { 0.0, 0.5, 0.5, 0.5 },
                new RungeKuttaStepInterpolator(3, 5, 2), 0.001, 0.01,
                new double[] { 1e-10, 1e-12 },
                new double[] { 1e-14, 1e-16 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error) * 2.0; // Custom error estimation factor
            }

        };

        final double[] initialCondition = { 1.0, 1.0 };
        final double[] finalCondition    = { 2.0, 2.0 };
        final double   time              = 2.0;
        final double   tolerance          = 1e-9;

        final RungeKuttaStepHandler stepHandler = new RungeKuttaStepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                if (step > 1) {
                    throw new EventException("StepHandler should not be called more than once");
                }
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialCondition.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                for (int i = 0; i < derivatives.length; ++i) {
                    derivatives[i] = -y[i];
                }
            }
        };

        final MockEventManager eventManager = new MockEventManager();

        integrator.addStepHandler(stepHandler);
        integrator.setEquations(equations);
        integrator.setEventsHandlersManager(eventManager);

        try {
            integrator.integrate(equations, 0.0, initialCondition, time, finalCondition);
            fail("Integration should have failed due to events handler");
        } catch (IntegratorException e) {
            // Success
        }

        assertEquals(initialCondition[0], finalCondition[0], tolerance);
        assertEquals(initialCondition[1], finalCondition[1], tolerance);

    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with wrong dimensionality of initial and final conditions
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorWrongInitialAndFinalConditions() {
        final String methodName = "EmbeddedRungeKuttaIntegratorTest_testEmbeddedRungeKuttaIntegratorWrongInitialAndFinalConditions";

        // Test simple integration
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false,
                new double[] { 0.0, 0.3, 0.5, 0.8 },
                new double[][] { { 0.0, 0.3, 0.5, 0.8 }, { 0.3, 0.5, 0.8, 0.8 }, { 0.5, 0.8, 0.5, 0.3 }, { 0.8, 0.8, 0.5, 0.0 } },
                new double[] { 0.0, 0.5, 0.5, 0.5 },
                new RungeKuttaStepInterpolator(3, 5, 2), 0.001, 0.01,
                new double[] { 1e-10, 1e-12 },
                new double[] { 1e-14, 1e-16 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        };

        final double[] initialCondition = new double[3]; // Wrong length
        final double[] finalCondition    = new double[4]; // Wrong length
        final double   time              = 2.0;

        assertThrows(MathIllegalArgumentException.class,
                () -> integrator.integrate(null, time, initialCondition, time, finalCondition));

    }

}