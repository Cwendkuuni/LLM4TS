package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepContext;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.SortedSet;
import java.util.concurrent.TreeSet;

public class AbstractIntegratorTest extends AbstractODETest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        assertEquals("IntegratorTest", integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertFalse(integrator.getEventHandlers().isEmpty());
        assertNull(integrator.getExpandable());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepContext context, boolean lastStep) {}
        };
        integrator.addStepHandler(stepHandler);
        assertTrue(integrator.getStepHandlers().contains(stepHandler));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEventHandlerWithoutInitialization() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        integrator.addEventHandler(new AbstractEvent() {}, 0.0, 1.0, 1);
    }

    @Test
    public void testAddEventHandlerWithInitialization() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        Event event = new AbstractEvent() {};
        integrator.addEventHandler(event, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        assertTrue(integrator.getEventHandlers().contains(event));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        Event event = new AbstractEvent() {};
        integrator.addEventHandler(event, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        integrator.clearEventHandlers();
        assertFalse(integrator.getEventHandlers().contains(event));
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        assertNaN(integrator.getCurrentStepStart());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        assertNaN(integrator.getCurrentSignedStepsize());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {
                // empty implementation
            }
        };
        FirstOrderDifferentialEquations equations = ODETestUtils.createTestEquations(1);
        double[] y0 = new double[] {0.1};
        double t = 1.0;
        double[] y = new double[1];
        integrator.integrate(equations, 0.0, y0, t, y);
        assertEquals(y0[0], y[0], 1E-12);
        assertEquals(t, integrator.getCurrentStepStart());
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithWrongY0Length() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {
                // empty implementation
            }
        };
        FirstOrderDifferentialEquations equations = ODETestUtils.createTestEquations(1);
        double[] y0 = new double[] {0.1, 0.2};
        double t = 1.0;
        integrator.integrate(equations, 0.0, y0, t, new double[1]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithWrongYLength() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {
                // empty implementation
            }
        };
        FirstOrderDifferentialEquations equations = ODETestUtils.createTestEquations(1);
        double[] y0 = new double[] {0.1};
        double t = 1.0;
        integrator.integrate(equations, 0.0, y0, t, new double[2]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluations() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {
                // empty implementation
            }
        };
        integrator.setMaxEvaluations(1);
        FirstOrderDifferentialEquations equations = ODETestUtils.createTestEquations(1);
        double[] y0 = new double[] {0.1};
        double t = 1.0;
        integrator.integrate(equations, 0.0, y0, t, new double[1]);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {
                // empty implementation
            }
        };
        integrator.setMaxEvaluations(1);
        FirstOrderDifferentialEquations equations = ODETestUtils.createTestEquationsWithNoBracketing(1);
        double[] y0 = new double[] {0.1};
        double t = 1.0;
        integrator.integrate(equations, 0.0, y0, t, new double[1]);
    }

    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(ODETestUtils.createTestEquations(1));
        double[] y = new double[1];
        integrator.computeDerivatives(0.0, y, new double[1]);
        assertArrayEquals(new double[] {0.0}, y, 1E-12);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        assertFalse(integrator.isStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    @Test
    public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(ODETestUtils.createTestEquations(1));
        StepInterpolator interpolator = new StepInterpolator(new StepContext(0.0, new double[] {0.1}));
        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepContext context, boolean lastStep) {
                assertEquals(context.getPreviousTime(), 0.0, 1E-12);
                assertEquals(context.getCurrentTime(), context.getInterpolatedTime(), 1E-12);
                assertEquals(context.getInterpolatedState(), new double[] {0.1}, 1E-12);
                assertEquals(context.getInterpolatedSecondaryState(0), 0.0, 1E-12);
                assertEquals(context.getInterpolatedSecondaryState(1), 0.0, 1E-12);
                assertTrue(lastStep);
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0.0, new double[] {0.1}, 1.0, new double[1]);
        integrator.acceptStep(interpolator, new double[] {0.1}, new double[] {0.2}, 1.0);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
    }

    @Test
    public void testAcceptStepWithReset() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorTest") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t) {}
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(ODETestUtils.createTestEquations(1));
        StepInterpolator interpolator = new StepInterpolator(new StepContext(0.0, new double[] {0.1}));
        integrator.addEventHandler(new AbstractEvent() {}, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        integrator.integrate(equations, 0.0, new double[] {0.1}, 1.0, new double[1]);
        integrator.acceptStep(interpolator, new double[] {0.1}, new double[] {0.2}, 1.0);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred);
    }
}