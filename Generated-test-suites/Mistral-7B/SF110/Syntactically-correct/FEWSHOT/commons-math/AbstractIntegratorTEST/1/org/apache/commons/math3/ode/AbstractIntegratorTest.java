package org.apache.commons.math3.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.event.AbstractEvent;
import org.apache.commons.math3.ode.event.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.EquationsMapper;
import org.apache.commons.math3.ode.util.ExpandableStatefulODETest;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.ode.util.StatefulODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double[] initialState;
    private double initialTime;
    private double finalTime;
    private double[] solution;
    private EquationsMapper equationsMapper;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // Do nothing
            }
        };
        equations = new ExpandableStatefulODE(new MyDifferentialEquations(1));
        initialState = new double[]{1.0};
        initialTime = 0.0;
        finalTime = 1.0;
        solution = new double[]{2.0};
        equationsMapper = StatefulODETestUtils.createIdentityMapper();
        interpolator = new StepInterpolator(2, 0.1);
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals("Test", interpolator.getInterpolatedState()[0], 0.0);
                assertFalse(isLastStep);
            }
        });
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals("Test2", interpolator.getInterpolatedState()[0], 0.5);
                assertTrue(isLastStep);
            }
        });
    }

    @Test
    public void testAddEventHandler() {
        AbstractEvent event = new MyEvent() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                assertEquals("Test event", integrator.getName(), interpolator.getInterpolatedState()[0], 0.0, 1e-10);
                return true;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // Do nothing
            }
        };
        integrator.addEventHandler(event, 0.5, 1e-10, 5);
        integrator.addEventHandler(event, 0.6, 1e-10, 5);
        assertEquals(2, integrator.getEventHandlers().size());
    }

    @Test
    public void testIntegrationWithStepHandlers() throws DimensionMismatchException, NoBracketingException, MaxCountExceededException, NumberIsTooSmallException {
        integrator.initIntegration(initialTime, initialState, finalTime);
        integrator.setExpandable(equations);
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals("Test1", integrator.getName(), interpolator.getInterpolatedState()[0], 0.0, 1e-10);
                assertFalse(isLastStep);
                integrator.setStateInitialized(true);
            }
        });
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals("Test2", integrator.getName(), interpolator.getInterpolatedState()[0], 0.5, 1e-10);
                assertTrue(isLastStep);
            }
        });
        integrator.integrate(equations, initialTime, initialState, finalTime, solution);
        assertArrayEquals(solution, initialState);
    }

    @Test
    public void testIntegrationWithEvents() throws DimensionMismatchException, NoBracketingException, MaxCountExceededException, NumberIsTooSmallException {
        integrator.initIntegration(initialTime, initialState, finalTime);
        integrator.setExpandable(equations);
        integrator.addEventHandler(new MyEvent() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                assertEquals("Test event1", integrator.getName(), interpolator.getInterpolatedState()[0], 0.1, 1e-10);
                return true;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                assertEquals("Test event1 accepted", integrator.getName(), t, finalTime, 1e-10);
            }
        }, 0.1, 1e-10, 5);
        integrator.addEventHandler(new MyEvent() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                assertEquals("Test event2", integrator.getName(), interpolator.getInterpolatedState()[0], 0.5, 1e-10);
                return true;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                assertEquals("Test event2 accepted", integrator.getName(), t, finalTime, 1e-10);
            }
        }, 0.5, 1e-10, 5);
        integrator.integrate(equations, initialTime, initialState, finalTime, solution);
        assertArrayEquals(solution, initialState);
    }

    @Test
    public void testIntegrationWithResetOccurred() throws DimensionMismatchException, NoBracketingException, MaxCountExceededException, NumberIsTooSmallException {
        integrator.initIntegration(initialTime, initialState, finalTime);
        integrator.setExpandable(equations);
        integrator.addEventHandler(new MyEvent() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                assertEquals("Test event1", integrator.getName(), interpolator.getInterpolatedState()[0], 0.1, 1e-10);
                return true;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                assertEquals("Test event1 accepted", integrator.getName(), t, finalTime, 1e-10);
                integrator.reset();
            }
        }, 0.1, 1e-10, 5);
        integrator.addEventHandler(new MyEvent() {
            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                assertEquals("Test event2", integrator.getName(), interpolator.getInterpolatedState()[0], 0.5, 1e-10);
                return true;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                assertEquals("Test event2 accepted", integrator.getName(), t, finalTime, 1e-10);
            }
        }, 0.5, 1e-10, 5);
        integrator.integrate(equations, initialTime, initialState, finalTime, solution);
        assertArrayEquals(solution, initialState);
        assertTrue(integrator.getResetOccurred());
    }

    @Test
    public void testMaxEvaluations() throws DimensionMismatchException, NoBracketingException, MaxCountExceededException, NumberIsTooSmallException {
        integrator.initIntegration(initialTime, initialState, finalTime);
        integrator.setExpandable(equations);
        integrator.setMaxEvaluations(10);
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                integrator.getEvaluationsCounter().increment();
            }
        });
        try {
            integrator.integrate(equations, initialTime, initialState, finalTime, solution);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
        assertEquals(10, integrator.getEvaluations());
    }

    @Test
    public void testSanityChecks() throws DimensionMismatchException, NumberIsTooSmallException {
        integrator.initIntegration(initialTime, initialState, finalTime);
        integrator.setExpandable(equations);
        assertEquals(initialTime, integrator.getCurrentStepStart(), 1e-10);
        assertEquals(-1, integrator.getMaxEvaluations());
        integrator.sanityChecks(equations, initialTime);
        integrator.setMaxEvaluations(1);
        assertThrows(MaxCountExceededException.class, () -> integrator.sanityChecks(equations, initialTime));
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {
        private int dimension;

        public MyDifferentialEquations(int dimension) {
            this.dimension = dimension;
        }

        @Override
        public int getDimension() {
            return dimension;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] * 0.1;
        }
    }

    private static class MyEvent extends AbstractEvent {
        @Override
        public boolean isRegularArrest() {
            return false;
        }
    }
}