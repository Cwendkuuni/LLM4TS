package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        assertEquals("Name should be set correctly", name, integrator.getName());
        assertTrue("Step Handlers should be empty", integrator.getStepHandlers().isEmpty());
        assertFalse("Max evaluations should be unlimited", integrator.getMaxEvaluations() < 0);
        assertFalse("Evaluations counter should be empty", integrator.getEvaluations() > 0);

        // Test adding and getting step handlers
        StepHandler stepHandler = new MyStepHandler();
        integrator.addStepHandler(stepHandler);
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals("Number of step handlers should be one", 1, stepHandlers.size());
        assertEquals("Step handler should be the same as added", stepHandler, stepHandlers.get(0));

        // Test clearing step handlers
        integrator.clearStepHandlers();
        assertTrue("Step handlers should be empty after clearing", integrator.getStepHandlers().isEmpty());

        // Test adding and getting event handlers
        EventHandler eventHandler = new MyEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-6, 10);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals("Number of event handlers should be one", 1, eventHandlers.size());
        assertEquals("EventHandler should be the same as added", eventHandler, eventHandlers.get(0));

        // Test clearing event handlers
        integrator.clearEventHandlers();
        assertTrue("Event handlers should be empty after clearing", integrator.getEventHandlers().isEmpty());

        // Test getting current step start and signed step size
        integrator.initIntegration(0.0, new double[1], 1.0);
        assertNaN("Current step start should be NaN initially", integrator.getCurrentStepStart());
        assertNaN("Current signed step size should be NaN initially", integrator.getCurrentSignedStepsize());

        // Test setting and getting max evaluations
        integrator.setMaxEvaluations(5);
        assertEquals("Max evaluations should be five", 5, integrator.getMaxEvaluations());

        try {
            integrator.integrate(new MyDifferentialEquations(), 0.0, new double[1], 1.0);
            fail("Integration should have thrown an exception with max evaluations set");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        integrator.setMaxEvaluations(-1);
        integrator.integrate(new MyDifferentialEquations(), 0.0, new double[1], 1.0);

        // Test initialize methods
        integrator = new AbstractIntegrator();
        assertNull("Name should be null when constructor has no argument", integrator.getName());

        integrator = new AbstractIntegrator("TestIntegrator2");
        assertEquals("Name should be set correctly", "TestIntegrator2", integrator.getName());

        // Test integration
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.setExpandable(new MyExpandableStatefulODE());
        integrator.integrate(integrator.getExpandable(), 1.1);

        // Test computeDerivatives
        integrator.computeDerivatives(1.0, new double[1], new double[1]);

        // Test setStateInitialized
        integrator.setStateInitialized(true);
        assertTrue("State initialized should be true after setting", integrator.isStateInitialized());

        integrator.setStateInitialized(false);
        assertFalse("State initialized should be false after resetting", integrator.isStateInitialized());
    }
}

class MyEventHandler implements EventHandler {
    @Override
    public void evaluateStep(final StepContext context) throws EventOccurredException {
        return EventOccurred.CONTINUE;
    }

    @Override
    public void handleEvent(double t, double[] state) {
        // Empty
    }

    @Override
    public void init(double t, double[] y, double tEnd) {
        // Empty
    }

    @Override
    public void reinitializeBegin(StepContext context) {
        // Empty
    }

    @Override
    public void reset(double t, double[] state) {
        // Empty
        return false;
    }

    @Override
    public void stepAccepted(double t, double[] state) {
        // Empty
    }
}

class MyStepHandler implements StepHandler {
    @Override
    public void handleStep(StepContext context, boolean last) {
        // Empty
    }

    @Override
    public void init(double t, double[] y, double tEnd) {
        // Empty
    }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computePrimaryDerivative(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * 2;
    }
}

class MyExpandableStatefulODE implements ExpandableStatefulODE {
    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void setTime(double time) {
        // Empty
    }

    @Override
    public void setPrimaryState(double[] state) {
        // Empty
    }

    @Override
    public void setPrimaryMapper(StateMapper mapper) {
        // Empty
    }

    @Override
    public void setSecondaryMappers(StateMapper[] mappers) {
        // Empty
    }

    @Override
    public void setExpandableState(ExpandableState expandableState) {
        // Empty
    }

    @Override
    public double getTime() {
        return 0.0;
    }

    @Override
    public double[] getPrimaryState() {
        return new double[1];
    }

    @Override
    public double[] getSecondaryState(int index) {
        return new double[0];
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * 2;
    }

    @Override
    public StateMapper getPrimaryMapper() {
        return new StateMapper() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getIndex(int index) {
                return 0;
            }

            @Override
            public void insertEquationData(StateState state, double[] data) {
                data[0] = state.getY()[0];
            }

            @Override
            public void insertEquationData(SecondaryStateState state, double[] data) {
                // Empty
            }
        };
    }
}