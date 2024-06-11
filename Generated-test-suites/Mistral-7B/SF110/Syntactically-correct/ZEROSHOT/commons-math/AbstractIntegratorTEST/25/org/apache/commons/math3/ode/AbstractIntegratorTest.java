package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        assertNotNull(integrator.getName());
        assertEquals(name, integrator.getName());
        assertFalse(integrator.isLastStep());
        assertTrue(integrator.statesInitialized);
        assertNull(integrator.expandable);
        assertEquals(0, integrator.evaluations.getCount());
        assertFalse(integrator.resetOccurred);

        integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        integrator.setName("TestIntegrator2");
        assertEquals("TestIntegrator2", integrator.getName());

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public void handleStep(AbstractODEIntegrator integrator, boolean isLastStep) {}
        });
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(1, stepHandlers.size());
        StepHandler stepHandler = stepHandlers.get(0);
        assertEquals(stepHandler, stepHandlers.get(0));

        integrator.clearStepHandlers();
        stepHandlers = integrator.getStepHandlers();
        assertTrue(stepHandlers.isEmpty());

        integrator.addEventHandler(new AbstractEventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public void handleEvent(double t, double[] y) {}

            @Override
            public double getEventTime() {
                return Double.NaN;
            }
        }, 0.0, 0.0, 0);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(1, eventHandlers.size());
        EventHandler eventHandler = eventHandlers.get(0);
        assertEquals(eventHandler, eventHandlers.get(0));

        integrator.clearEventHandlers();
        eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.isEmpty());

        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());

        integrator.initIntegration(0.0, new double[0], 1.0);
        assertFalse(integrator.statesInitialized);
        assertEquals(Double.NaN, integrator.getCurrentStepStart());
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());

        integrator.setEquations(new DummyODE());
        assertEquals(integrator.getExpandable(), integrator.getExpandable());

        integrator.getEvaluationsCounter();
        assertEquals(integrator.evaluations, integrator.getEvaluationsCounter());

        try {
            integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, new double[0]);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }

        try {
            integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, new double[1]);

        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        assertEquals(0, integrator.evaluations.getCount());
        assertTrue(integrator.resetOccurred);
        integrator.resetOccurred = false;

        integrator = new AbstractIntegrator();
        integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, new double[1]);
        assertEquals(1, integrator.evaluations.getCount());
        assertFalse(integrator.resetOccurred);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluationsReached() throws MaxCountExceededException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(1);
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDifferentDimensions() throws DimensionMismatchException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(1);
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.integrate(new DummyODE(), 0.0, new double[0], 1.0, new double[1]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallStepSize() throws NumberIsTooSmallException, NoBracketingException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(1);
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.setEquations(new DummyODE());
        integrator.sanityChecks(integrator.getExpandable(), 1.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws NoBracketingException, MaxCountExceededException {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(1);
        integrator.initIntegration(0.0, new double[0], 1.0);
        integrator.setEquations(new DummyODEWithBracketingException());
        integrator.integrate(integrator.getExpandable(), 1.0);
    }

    private static class DummyODE implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0];
        }
    }

    private static class DummyODEWithBracketingException implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0];
            if (y[0] > 0.5) {
                throw new NoBracketingException("Test exception");
            }
        }
    }
}