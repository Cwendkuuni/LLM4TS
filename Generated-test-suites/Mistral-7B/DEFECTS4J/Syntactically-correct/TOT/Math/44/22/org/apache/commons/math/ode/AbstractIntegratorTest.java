package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.step.AbstractStepInterpolator;
import org.apache.commons.math3.ode.step.StepHandler;
import org.apache.commons.math3.util.Precision;

/**
 * Test suite for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrate() throws Exception {
        // create a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }

            /** {@inheritDoc} */
            public void setTime(double t) {
                // empty implementation
            }

            /** {@inheritDoc} */
            public void setPrimaryState(double[] y) {
                // empty implementation
            }

        };

        // create an integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException {
                // implementation under test
            }

        };

        // set up the equation and the integrator
        integrator.setEquations(equations);

        // perform integration within a reasonable time
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0 };
        final double t = 1.0;
        final double[] y = new double[y0.length];
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(2.0, y[0], 1e-12);

        // perform integration beyond the limit of the implementation
        integrator.setMaxEvaluations(0);
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }

        // test error cases
        integrator.setMaxEvaluations(1);
        try {
            integrator.integrate(equations, t0, y0, -1.0, y);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
        try {
            integrator.integrate(equations, t0, y0, t, null);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }

    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    public void testAcceptStep() throws Exception {
        // create a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            /** {@inheritDoc} */
            public void setTime(double t) {
                // empty implementation
            }

            /** {@inheritDoc} */
            public void setPrimaryState(double[] y) {
                // empty implementation
            }

        };

        // create an integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException {
                // implementation under test
            }

            /** {@inheritDoc} */
            protected double acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                    throws MathIllegalStateException {
                return tEnd;
            }

        };

        // create a step interpolator
        final AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {

            /** {@inheritDoc} */
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            /** {@inheritDoc} */
            public double getGlobalCurrentTime() {
                return 1.0;
            }

            /** {@inheritDoc} */
            public double[] getInterpolatedState() {
                return new double[] { 1.0 };
            }

            /** {@inheritDoc} */
            public void setInterpolatedTime(double t) {
                // empty implementation
            }

        };

        // set up the equation, the integrator, and the interpolator
        integrator.setEquations(equations);

        // initialize the events states
        integrator.setStateInitialized(false);

        // perform a single step within a reasonable time
        final double[] y = new double[y0.length];
        final double tEnd = 2.0;
        integrator.acceptStep(interpolator, y, null, tEnd);
        assertEquals(2.0, tEnd, 1e-12);
        assertEquals(1.0, y[0], 1e-12);

        // perform a single step beyond the limit of the implementation
        integrator.setMaxEvaluations(0);
        try {
            integrator.acceptStep(interpolator, y, null, tEnd);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }

        // test error cases
        integrator.setMaxEvaluations(1);
        try {
            integrator.acceptStep(interpolator, y, null, -1.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // test case where an event occurs during the step
        final EventHandler eventHandler = new EventHandler() {

            /** {@inheritDoc} */
            public void evaluateState(double t, double[] y, double[] yDot, double[] yPrime) {
                yPrime[0] = -y[0];
            }

            /** {@inheritDoc} */
            public double getEventTime() {
                return 1.5;
            }

            /** {@inheritDoc} */
            public int getEventIndex() {
                return 0;
            }

            /** {@inheritDoc} */
            public void reset() {
                // empty implementation
            }

        };
        integrator.addEventHandler(eventHandler, 0.5, 1e-8, 10);
        try {
            integrator.acceptStep(interpolator, y, null, tEnd);
            fail();
        } catch (MathIllegalStateException e) {
            // expected
        }
        assertEquals(1.5, eventHandler.getEventTime(), 1e-12);

        // test case where an event occurs at the end of the step
        integrator.resetOccurred = false;
        integrator.acceptStep(interpolator, y, null, tEnd);
        assertTrue(integrator.resetOccurred);
        assertEquals(2.0, integrator.getCurrentSignedStepsize(), 1e-12);

        // test case where an event occurs at the beginning of the step
        integrator.reset();
        integrator.setStateInitialized(true);
        integrator.acceptStep(interpolator, y, null, tEnd);
        assertTrue(integrator.isLastStep);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);

    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized(boolean)}.
     */
    public void testSetStateInitialized() throws Exception {
        // create a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            /** {@inheritDoc} */
            public void setTime(double t) {
                // empty implementation
            }

            /** {@inheritDoc} */
            public void setPrimaryState(double[] y) {
                // empty implementation
            }

        };

        // create an integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // empty implementation
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException {
                // implementation under test
            }

            /** {@inheritDoc} */
            protected double acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                    throws MathIllegalStateException {
                return tEnd;
            }

            /** {@inheritDoc} */
            public boolean getStateInitialized() {
                return stateInitialized;
            }

        };

        // set up the equation and the integrator
        integrator.setEquations(equations);

        // test initialization
        assertFalse(integrator.getStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.getStateInitialized());

        // test reinitialization
        integrator.setStateInitialized(false);
        integrator.acceptStep(interpolator, new double[0], null, 1.0);
        assertTrue(integrator.getStateInitialized());

    }

}