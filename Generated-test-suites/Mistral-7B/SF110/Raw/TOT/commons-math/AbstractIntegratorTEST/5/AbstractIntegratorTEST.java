package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.AbstractStateTransitionEvent;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() throws Exception {
        final String integratorName = "MyIntegrator";
        final AbstractIntegrator integrator = new MyIntegrator(integratorName);
        assertNotNull(integrator);
        assertEquals(integratorName, integrator.getName());

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void calculateDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[0] * 2;
            }
        };

        final double[] y0 = new double[]{1.0};
        final double tEnd = 10.0;

        try {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
            fail("Expected NumberIsTooSmallException or MaxCountExceededException");
        } catch (NoBracketingException e) {
            // Expected
        } catch (DimensionMismatchException e) {
            fail("Unexpected DimensionMismatchException");
        } catch (MaxCountExceededException e) {
            // Expected
        } catch (NumberIsTooSmallException e) {
            fail("Unexpected NumberIsTooSmallException");
        }

        integrator.clearEventHandlers();
        integrator.clearStepHandlers();

        final StepInterpolator interpolator = new StepInterpolator(new RectangularStepInterpolator(new RectangularStep(0.1), 10));
        integrator.setMaxEvaluations(100);

        final MyEvent event = new MyEvent();
        integrator.addEventHandler(event, 2.0, 0.001, 10);

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setEquations(new ExpandableStatefulODE(equations));

        final double[] y = new double[y0.length];
        integrator.integrate(interpolator, y);

        assertEquals(11, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertArrayEquals(new double[]{3.6960089145824366d}, y, 0.00001);

        integrator.clearEventHandlers();
        integrator.clearStepHandlers();

        final MyStepHandler stepHandler = new MyStepHandler();
        integrator.addStepHandler(stepHandler);

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setEquations(new ExpandableStatefulODE(equations));

        integrator.integrate(interpolator, y);

        assertEquals(11, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertArrayEquals(new double[]{3.6960089145824366d}, y, 0.00001);
        assertTrue(stepHandler.isCalled);

        integrator.clearStepHandlers();

        final MyErrorEvent errorEvent = new MyErrorEvent();
        integrator.addEventHandler(errorEvent, 5.0, 0.001, 10, new BracketingNthOrderBrentSolver(0.001, 5));

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setEquations(new ExpandableStatefulODE(equations));

        integrator.integrate(interpolator, y);

        assertEquals(11, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertArrayEquals(new double[]{3.6960089145824366d}, y, 0.00001);
        assertFalse(errorEvent.isTriggered);

        integrator.clearEventHandlers();

        final MyEventWithState eventWithState = new MyEventWithState();
        integrator.addEventHandler(eventWithState, 3.0, 0.001, 10, null);

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setEquations(new ExpandableStatefulODE(equations));

        final double[] yWithState = new double[y0.length + 1];
        System.arraycopy(y0, 0, yWithState, 0, y0.length);
        integrator.integrate(interpolator, yWithState);

        assertEquals(11, integrator.getEvaluations());
        assertTrue(integrator.isLastStep());
        assertArrayEquals(new double[]{3.0006341897158465d}, yWithState, 0.00001);
        assertTrue(eventWithState.isTriggered);
        assertEquals(3.0, eventWithState.getEventTime(), 0.00001);
        assertArrayEquals(new double[]{3.0006341897158465d}, eventWithState.getEventState(), 0.00001);

        integrator.clearEventHandlers();

        try {
            integrator.integrate(interpolator, yWithState);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        integrator.clearStepHandlers();
        integrator.clearEventHandlers();

        assertFalse(integrator.isLastStep());
        assertFalse(integrator.isStepInitialized());
        assertNull(integrator.getExpandable());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.resetOccurred);
    }

    static class MyIntegrator extends AbstractIntegrator {
        MyIntegrator() {
            super();
        }

        MyIntegrator(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Custom implementation
        }
    }

    static class MyEvent extends AbstractStateTransitionEvent {
        boolean isTriggered = false;

        public void stepAccepted(double time, double[] state) {
            isTriggered = true;
        }

        public double getEventTime() {
            return 3.0;
        }
    }

    static class MyEventWithState extends AbstractStateTransitionEvent {
        boolean isTriggered = false;

        double[] eventState;

        public void stepAccepted(double time, double[] state) {
            isTriggered = true;
            eventState = state;
        }

        public double getEventTime() {
            return 3.0;
        }

        public double[] getEventState() {
            return eventState;
        }
    }

    static class MyStepHandler implements StepHandler {
        boolean isCalled = false;

        public void handleStep(StepInterpolator interpolator, boolean lastStep) {
            isCalled = true;
        }
    }

}