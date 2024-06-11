package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertNull(integrator.getName());
        assertNotNull(integrator.getStepHandlers());
        assertEquals(0, integrator.getStepHandlers().size());
        assertNull(integrator.getEventHandlers());
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.getExpandable());
        assertNull(integrator.getEvaluationsCounter());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testGetStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        StepHandler handler1 = mock(StepHandler.class);
        StepHandler handler2 = mock(StepHandler.class);
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        assertEquals(2, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        StepHandler handler1 = mock(StepHandler.class);
        StepHandler handler2 = mock(StepHandler.class);
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 1.0, 1.0e-6, 10);
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        EventHandler handler1 = mock(EventHandler.class);
        EventHandler handler2 = mock(EventHandler.class);
        integrator.addEventHandler(handler1, 1.0, 1.0e-6, 10);
        integrator.addEventHandler(handler2, 2.0, 1.0e-6, 10);
        assertEquals(2, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        EventHandler handler1 = mock(EventHandler.class);
        EventHandler handler2 = mock(EventHandler.class);
        integrator.addEventHandler(handler1, 1.0, 1.0e-6, 10);
        integrator.addEventHandler(handler2, 2.0, 1.0e-6, 10);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertNaN(integrator.getCurrentStepStart());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setMaxEvaluations(5);
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        assertEquals(5, integrator.getEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0, y0 = 0.0;
        double t = 1.0;
        integrator.initIntegration(t0, new double[]{y0}, t);
        verify(equations).init(t0, new double[]{y0}, t);
    }

    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getExpandable());
    }

    @Test
    public void testGetExpandable() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getExpandable());
    }

    @Test
    public void testGetEvaluationsCounter() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        Incrementor evaluationsCounter = integrator.getEvaluationsCounter();
        assertEquals(evaluationsCounter, integrator.getEvaluationsCounter());
    }

    @Test
    public void testIntegrate() {
        // TODO: Implement this test
    }

    @Test
    public void testComputeDerivatives() {
        // TODO: Implement this test
    }

    @Test
    public void testSetLastStep() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setLastStep(true);
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testResetOccurred() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.resetOccurred = true;
        assertTrue(integrator.resetOccurred);
    }
}