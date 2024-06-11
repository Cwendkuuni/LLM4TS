package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RK23Integrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        integrator = new RK23Integrator("RK23", true, new double[]{0.25, 0.25, 0.5, 0.25}, new double[][]{
                {0.6, 0.275, 0.84, 0.013},
                {0.2, 0.436, 0.389, 0.172},
                {0.65, 0.039, 0.9, 0.174},
                {0.13, 0.67, 0.055, 0.87},
                {0.65, 0.045, 0.87, 0.017}}, new double[]{0.2, 0.0, 0.389, 0.5, 0.389, 0.0},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] yDot, boolean forward, Mapper mapper, Mapper... secondaryMappers) {
                        this.integrator = integrator;
                        this.y = y;
                        this.yDot = yDot;
                        this.forward = forward;
                        this.mapper = mapper;
                        this.secondaryMappers = secondaryMappers;
                    }
                });
        equations = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        interpolator = (RungeKuttaStepInterpolator) integrator.prototype.copy();
    }

    @Test
    public void testGetOrder() {
        assertEquals(5, integrator.getOrder());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOrderThrowsExceptionIfIntegratorIsAbstract() {
        EmbeddedRungeKuttaIntegrator abstractIntegrator = new EmbeddedRungeKuttaIntegrator("Abstract", true, null, null, null, null, 0.0, 0.0, 0.0, 0.0) {
            @Override
            public int getOrder() {
                throw new IllegalStateException("Abstract integrator cannot be instantiated");
            }
        };
        abstractIntegrator.getOrder();
    }

    @Test
    public void testIntegrateGivenInitialConditionsAndFinalTime() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double finalTime = 10.0;
        integrator.integrate(equations, initialState, finalTime);
        double expectedFinalState = Precision.round(Math.exp(10.0 * 2.0), 16);
        assertEquals(expectedFinalState, equations.getCompleteState()[0]);
    }

    @Test
    public void testIntegrateGivenInitialConditionsAndSameTime() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double currentTime = 10.0;
        integrator.integrate(equations, initialState, currentTime);
        assertEquals(initialState[0], equations.getCompleteState()[0]);
    }

    @Test
    public void testIntegrateGivenInitialConditionsAndTimeBeforeStart() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double currentTime = 5.0;
        integrator.integrate(equations, initialState, currentTime);
        double expectedStateAtTimeFive = Precision.round(Math.exp(5.0 * 2.0), 16);
        assertEquals(expectedStateAtTimeFive, equations.getCompleteState()[0]);
    }

    @Test
    public void testIntegrateGivenInitialConditionsAndTimeAfterEnd() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double currentTime = 15.0;
        integrator.integrate(equations, initialState, currentTime);
        assertFalse(Double.isFinite(equations.getCompleteState()[0]));
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateGivenInitialConditionsAndTimeBelowMinimumStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double currentTime = 0.1;
        integrator.integrate(equations, initialState, currentTime);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateGivenInitialConditionsWithInvalidStateLength() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0, 2.0};
        double currentTime = 10.0;
        integrator.integrate(equations, initialState, currentTime);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateGivenInitialConditionsWithMaxCountExceeded() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double currentTime = 1000.0;
        integrator.setMaxStep(0.001);
        integrator.integrate(equations, initialState, currentTime);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateGivenInitialConditionsWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] initialState = {1.0};
        double currentTime = 10.0;
        integrator.setMinStep(10.0);
        integrator.integrate(equations, initialState, currentTime);
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 0.001);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testIsLastStep() {
        integrator.isLastStep = true;
        assertTrue(integrator.isLastStep);
    }

    @Test
    public void testIsLastStepFalse() {
        assertFalse(integrator.isLastStep);
    }

    @Test
    public void testSanityChecks() {
        ExpandableStatefulODE equationsWithInvalidMapper = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        equationsWithInvalidMapper.getPrimaryMapper().setMapperInfo(0, 0, 0);
        try {
            integrator.sanityChecks(equationsWithInvalidMapper, 10.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ExpandableStatefulODE equationsWithInvalidSecondaryMapper = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        Mapper invalidSecondaryMapper = new Mapper() {
            @Override
            public void map(double[] source, double[] target, int sourceIndex, int targetIndex, int length) {
                throw new UnsupportedOperationException();
            }
        };
        equationsWithInvalidSecondaryMapper.setSecondaryMapper(0, invalidSecondaryMapper);
        try {
            integrator.sanityChecks(equationsWithInvalidSecondaryMapper, 10.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ExpandableStatefulODE equationsWithInvalidTime = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        try {
            integrator.sanityChecks(equationsWithInvalidTime, -1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ExpandableStatefulODE equationsWithNaN = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Double.NaN;
            }
        };
        try {
            integrator.sanityChecks(equationsWithNaN, 10.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ExpandableStatefulODE equationsWithNull = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        try {
            integrator.sanityChecks(equationsWithNull, 10.0);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        ExpandableStatefulODE equationsWithInvalidVectorLength = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        double[] wrongLengthState = new double[2];
        try {
            integrator.sanityChecks(equationsWithInvalidVectorLength, 10.0, wrongLengthState);
            fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }

        ExpandableStatefulODE equationsWithEmptyArray = new ExpandableODE("Test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 2 * y[0];
            }
        };
        double[] emptyInitialState = {};
        try {
            integrator.sanityChecks(equationsWithEmptyArray, 10.0, emptyInitialState);
            fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testInitIntegration() {
        integrator.initIntegration(10.0, new double[]{1.0}, 10.0);
        assertEquals(1.0, integrator.y[0]);
        assertEquals(1.0, integrator.yDot[0]);
    }

    @Test
    public void testAcceptStep() {
        double[] y = new double[]{1.0};
        double[] yDot = new double[]{1.0};
        double finalTime = 20.0;
        double acceptedStep = integrator.acceptStep(interpolator, y, yDot, finalTime);
        assertNotNull(acceptedStep);
        assertEquals(10.0, acceptedStep, 0.001);
    }

    @Test
    public void testAcceptStepReturnsZeroWhenLastStep() {
        double[] y = new double[]{1.0};
        double[] yDot = new double[]{1.0};
        double finalTime = 10.0;
        integrator.isLastStep = true;
        double acceptedStep = integrator.acceptStep(interpolator, y, yDot, finalTime);
        assertEquals(0.0, acceptedStep, 0.001);
    }
}