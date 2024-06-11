package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.EventsState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.test.AbstractIntegratorTestBase;
import org.apache.commons.math3.ode.util.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test suite for AbstractIntegrator class.
 */
public class AbstractIntegratorTest extends AbstractIntegratorTestBase {

    /**
     * Constructor.
     */
    public AbstractIntegratorTest() {
        super();
    }

    /**
     * Test method for {@link AbstractIntegrator# AbstractIntegrator(String)}.
     */
    @Test
    public void testConstructor() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        assertNotNull(integrator);
        assertEquals("testIntegrator", integrator.getName());
    }

    /**
     * Test method for {@link AbstractIntegrator# addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        final StepHandler handler = new StepHandler() {
            /** {@inheritDoc} */
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    /**
     * Test method for {@link AbstractIntegrator# clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        final StepHandler handler1 = new StepHandler() {
            /** {@inheritDoc} */
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // do nothing
            }
        };
        final StepHandler handler2 = new StepHandler() {
            /** {@inheritDoc} */
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                // do nothing
            }
        };
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        assertEquals(2, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator# integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws MathIllegalStateException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public double evaluateDerivative(double t, double[] state) {
                // do nothing
                return 0.0;
            }

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

        };
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {

                super.integrate(equations, t);
                assertEquals(equations.getPrimaryState()[0], getLastState()[0], 1e-12);
                assertEquals(equations.getTime(), getLastTime(), 1e-12);

            }

        };
        integrator.setEquations(equations);
        double[] initialState = { 1.0 };
        double[] finalState = {};
        try {
            integrator.integrate(equations, 0.0, initialState, 1.0, finalState);
            assertEquals(1, finalState.length);
            assertEquals(1.0, finalState[0], 1e-12);
        } catch (RuntimeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link AbstractIntegrator# integrate(ExpandableStatefulODE, double)}.
     * @throws MathIllegalStateException
     * @throws MathIllegalArgumentException
     * @throws MaxCountExceededException
     */
    @Test
    public void testIntegrateException() throws MathIllegalStateException, MathIllegalArgumentException, MaxCountExceededException {
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {

            /** {@inheritDoc} */
            public double evaluateDerivative(double t, double[] state) {
                // do nothing
                return 0.0;
            }

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

            /** {@inheritDoc} */
            public double[] getPrimaryState() {
                return new double[0];
            }

            /** {@inheritDoc} */
            public void setPrimaryState(double[] state) {
                // do nothing
            }

            /** {@inheritDoc} */
            public double getTime() {
                return 0.0;
            }

            /** {@inheritDoc} */
            public void setTime(double time) {
                // do nothing
            }

            /** {@inheritDoc} */
            public double[] getState() {
                return new double[0];
            }

            /** {@inheritDoc} */
            public void setState(double[] state) {
                // do nothing
            }

            /** {@inheritDoc} */
            public double[] computeState(double t, double[] y) {
                throw new MathIllegalArgumentException("testComputeState: state is null");
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }

        };
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {

                super.integrate(equations, t);
                fail("Unexpected call to integrate method");

            }

        };
        integrator.setEquations(equations);
        try {
            integrator.integrate(equations, 0.0);
            fail("Unexpected call to integrate method, no exception thrown");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link AbstractIntegrator# setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.setMaxEvaluations(1);
        assertEquals(1, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator# getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator");
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
        integrator.evaluateDerivative(0.0, new double[0]);
        assertEquals(1, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator# getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {

                super.integrate(equations, t);

            }

        };
        integrator.setMaxEvaluations(1);
        assertEquals(1, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator# acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     * @throws MathIllegalStateException
     */
    @Test
    public void testAcceptStep() throws MathIllegalStateException {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected double acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
                throws MathIllegalStateException {

                super.acceptStep(interpolator, y, yDot, tEnd);
                return tEnd;

            }

        };
        final AbstractStepInterpolator interpolator = new StepInterpolator();
        double[] initialState = { 1.0 };
        double[] finalState = {};
        integrator.setEquations(new DummyDifferentialEquations());
        integrator.integrate(new DummyExpandableODE(), 0.0, initialState, 1.0, finalState);
        assertEquals(1.0, finalState[0], 1e-12);
        double tEnd = integrator.acceptStep(interpolator, initialState, new double[0], 1.1);
        assertEquals(1.1, tEnd, 1e-12);
    }

    /**
     * Test method for {@link AbstractIntegrator# setStateInitialized(boolean)}.
     */
    @Test
    public void testStateInitialized() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected boolean getStateInitialized() {
                return super.getStateInitialized();
            }

            /** {@inheritDoc} */
            protected void setStateInitialized(boolean stateInitialized) {
                super.setStateInitialized(stateInitialized);
            }

        };
        integrator.setStateInitialized(false);
        assertFalse(integrator.getStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.getStateInitialized());
    }

    /**
     * Test method for {@link AbstractIntegrator# sanityChecks(ExpandableStatefulODE, double)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecks() throws NumberIsTooSmallException {
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {

            /** {@inheritDoc} */
            public double evaluateDerivative(double t, double[] state) {
                // do nothing
                return 0.0;
            }

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

        };
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {

                super.integrate(equations, t);

            }

        };
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 0.0);
        integrate(equations, 0.0, new double[0], 1.0, new double[0]);

    }

    /**
     * Test method for {@link AbstractIntegrator# sanityChecks(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testSanityChecksNoThrow() throws NumberIsTooSmallException {
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {

            /** {@inheritDoc} */
            public double evaluateDerivative(double t, double[] state) {
                // do nothing
                return 0.0;
            }

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }

        };
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegrator") {

            /** {@inheritDoc} */
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {

                super.integrate(equations, t);

            }

        };
        integrator.setEquations(equations);
        integrator.sanityChecks(equations, 1.0);
        integrate(equations, 1.0, new double[0], 2.0, new double[0]);

    }

}