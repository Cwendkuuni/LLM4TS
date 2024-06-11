package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                       MaxCountExceededException, NoBracketingException {
                System.out.println("Integrating from " + equations.getTime() + " to " + t);
                // no-op
            }
        };
        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }
        };
    }

    /**
     * Test that integrator initialize correctly.
     */
    @Test
    public void testIntegratorInitialization() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertNotNull(integrator.getStepHandlers());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.expandable);
    }

    /**
     * Test adding and removing step handlers.
     */
    @Test
    public void testStepHandlers() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals(0.5, interpolator.getGlobalCurrentTime(), 0.0);
                assertEquals(1.0, interpolator.getGlobalPreviousTime(), 0.0);
                assertTrue(isLastStep);
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals(0.25, interpolator.getGlobalCurrentTime(), 0.0);
                assertEquals(0.0, interpolator.getGlobalPreviousTime(), 0.0);
                assertFalse(isLastStep);
            }
        });
        integrator.clearStepHandlers();
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    /**
     * Test adding and removing event handlers.
     */
    @Test
    public void testEventHandlers() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.addEventHandler(
                new EventHandler() {
                    @Override
                    public void init(double t, double[] y, double tEnd) {
                        System.out.println("EventHandler initialized at " + t);
                    }

                    @Override
                    public int getOrder() {
                        return 1;
                    }

                    @Override
                    public void eventOccurred(double t, double[] y) {
                        assertArrayEquals(new double[] { 1.0 }, y);
                        System.out.println("EventHandler triggered at " + t);
                    }
                },
                0.5, 1.0, 5);
        integrator.addEventHandler(
                new EventHandler() {
                    @Override
                    public void init(double t, double[] y, double tEnd) {
                        System.out.println("EventHandler2 initialized at " + t);
                    }

                    @Override
                    public int getOrder() {
                        return 1;
                    }

                    @Override
                    public void eventOccurred(double t, double[] y) {
                        assertArrayEquals(new double[] { 0.5 }, y);
                        System.out.println("EventHandler2 triggered at " + t);
                    }
                },
                0.75, 1.0, 5);
        integrator.clearEventHandlers();
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test setting equations.
     */
    @Test
    public void testSettingEquations() {
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.setEquations(equations);
        assertEquals(equations, integrator.expandable);
    }

    /**
     * Test integrating equations.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationDimensionMismatch() {
        integrator.initIntegration(0.0, new double[2], 1.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0);
    }

    /**
     * Test integrating equations with correct dimensions.
     */
    @Test
    public void testIntegration() {
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertArrayEquals(new double[] { 1.0 }, equations.getPrimaryState(), 0.0);
        assertEquals(1.0, equations.getTime(), 0.0);
    }

    /**
     * Test integration with events.
     */
    @Test
    public void testIntegrationWithEvents() {
        integrator.initIntegration(0.0, new double[1], 3.0);
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void init(double t, double[] y, double tEnd) {
                System.out.println("Event handler initialized at " + t);
            }

            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public void eventOccurred(double t, double[] y) {
                System.out.println("Event handler triggered at " + t);
                assertEquals(1.0, y[0]);
            }
        }, 1.0, 1.0, 3);
        integrator.setEquations(equations);
        integrator.integrate(equations, 3.0);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
        assertEquals(2.718281828, equations.getPrimaryState()[0], 0.01);
        assertEquals(3.0, equations.getTime(), 0.0);
    }

    /**
     * Test integration with events and step handlers.
     */
    @Test
    public void testIntegrationWithEventsAndStepHandlers() {
        integrator.initIntegration(0.0, new double[1], 3.0);
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void init(double t, double[] y, double tEnd) {
                System.out.println("Event handler initialized at " + t);
            }

            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public void eventOccurred(double t, double[] y) {
                System.out.println("Event handler triggered at " + t);
                assertEquals(1.0, y[0]);
            }
        }, 1.0, 1.0, 3);
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void init(double t, double[] y, double tEnd) {
                System.out.println("Event handler2 initialized at " + t);
            }

            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public void eventOccurred(double t, double[] y) {
                System.out.println("Event handler2 triggered at " + t);
                assertEquals(1.5, y[0]);
            }
        }, 1.5, 1.0, 3);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals(1.0, interpolator.getGlobalCurrentTime(), 0.0);
                assertFalse(isLastStep);
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals(2.0, interpolator.getGlobalCurrentTime(), 0.0);
                assertFalse(isLastStep);
            }
        });
        integrator.setEquations(equations);
        integrator.integrate(equations, 3.0);
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertArrayEquals(new double[] { 2.718281828 }, equations.getPrimaryState(), 0.01);
        assertEquals(2.718281828, equations.getTime(), 0.01);
    }

    /**
     * Test integrating equations with max evaluation limit.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithMaxEvaluationLimit() {
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.setMaxEvaluations(1);
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0);
    }

    /**
     * Test integrating equations with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testIntegrationWithNoBracketing() {
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.setEquations(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * Math.sin(1 / y[0]);
            }
        });
        integrator.integrate(equations, 1.0);
    }

}