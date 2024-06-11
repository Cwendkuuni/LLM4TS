package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.interpolators.CubicSplineInterpolator;
import org.apache.commons.math3.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.Integrator;
import org.apache.commons.math.ode.StepInterpolator;

/**
 * Test cases for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /** Integrator under test. */
    private Integrator integrator;

    /** Test equations. */
    private final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

        /** {@inheritDoc} */
        @Override
        public int getDimension() {
            return 2;
        }

        /** {@inheritDoc} */
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "Test equations.";
        }
    };

    /** Output interpolator. */
    private StepInterpolator interpolator;

    /** {@inheritDoc} */
    @Before
    public void setUp() {
        integrator = new EulerIntegrator(1.0E-10);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step " + interpolator.getGlobalStepNumber());
                System.out.println("Time: " + interpolator.getGlobalCurrentTime());
                System.out.println("State: " + FunctionUtils.arrayToString(interpolator.getInterpolatedState(), 3));
            }
        });
        interpolator = new AbstractStepInterpolator() {
            @Override
            public double interpolate(double t, String interpolationMethod)
                    throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Not supported.");
            }

            @Override
            public double getGlobalCurrentTime() {
                return integrator.getCurrentStepStart();
            }

            @Override
            public double getGlobalPreviousTime() {
                return integrator.getPreviousStepStart();
            }

            @Override
            public double getInterpolatedState(int index) {
                return integrator.getState()[index];
            }

            @Override
            public int getGlobalStepNumber() {
                return integrator.getStepNumber();
            }
        };
    }

    /**
     * Test basic functionality.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBasicFunctionality() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 0.1, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 2.0);
        double[] result = integrator.getState();
        assertEquals(2.0, integrator.getTime(), 1.0E-12);
        assertArrayEquals(new double[] { 1.73205080757, 0.73205080757 }, result, 1.0E-12);
    }

    /**
     * Test exception handling.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testExceptionHandling() throws Exception {
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 2.0);
    }

    /**
     * Test integration with event.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testEventHandling() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.5, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

    /**
     * Test integration with multiple events.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testMultipleEvents() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.2, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.8, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

    /**
     * Test integration with multiple events and step handlers.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testMultipleEventsAndStepHandlers() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.2, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.8, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    throw new RuntimeException("Last step failed.");
                }
            }
        });
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

    /**
     * Test integration with multiple events and different solvers.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testMultipleEventsAndDifferentSolvers() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.2, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.8, 1.0E-10, 10, new SecantSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

    /**
     * Test integration with multiple events and different interpolators.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testMultipleEventsAndDifferentInterpolators() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.2, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.8, 1.0E-10, 10, new LaguerreSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.setStepInterpolator(new LinearInterpolator());
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

    /**
     * Test integration with multiple events and different interpolators and solvers.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testMultipleEventsAndDifferentInterpolatorsAndSolvers() throws Exception {
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.2, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.addEventHandler(new MyEventHandlerWithReset(), 0.8, 1.0E-10, 10, new SecantSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.setStepInterpolator(new CubicSplineInterpolator());
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

    /**
     * Test dimension mismatch exception.
     * @throws Exception if an error occurs
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws Exception {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        });
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0, 0.0 }, 2.0);
    }

    /**
     * Test out of range exception.
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOutOfRangeException() throws Exception {
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 0.0);
    }

    /**
     * Test max evaluations exception.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluationsException() throws Exception {
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 2.0);
    }

    /**
     * Test reset exception.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalStateException.class)
    public void testResetException() throws Exception {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, EventState state) {
                state.setNext(new EventState() {
                    @Override
                    public void reset(double t, double[] y) {
                        throw new UnsupportedOperationException("Not supported.");
                    }
                });
            }
        }, 0.5, 1.0E-10, 10, new BrentSolver(1.0E-10));
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.5);
    }

}

class MyEventHandler implements EventHandler {

    /** {@inheritDoc} */
    @Override
    public void evaluateState(double t, double[] y, EventState state) {
        // no-op
    }

}

class MyEventHandlerWithReset implements EventHandler {

    /** {@inheritDoc} */
    @Override
    public void evaluateState(double t, double[] y, EventState state) {
        state.setNext(new EventState() {
            @Override
            public void reset(double t, double[] y) {
                y[0] = 0.0;
                y[1] = 0.0;
            }
        });
    }

}