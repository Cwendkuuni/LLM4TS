package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

import java.util.*;
import java.lang.reflect.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        assertEquals("Name", integrator.getName());
        integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, double t, double[] y, double[] yDot) {
                // no-op
            }

            @Override
            public void init(double t, double[] y, double tEnd) {
                // no-op
            }
        });
        assertFalse(integrator.getStepHandlers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addEventHandler(null, 0, 0, 0, new BracketingNthOrderBrentSolver(0, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullSolver() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(StepContext context) {
                // no-op
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no-op
            }
        }, 0, 0, 0, null);
    }

    @Test
    public void testGetStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, double t, double[] y, double[] yDot) {
                // no-op
            }

            @Override
            public void init(double t, double[] y, double tEnd) {
                // no-op
            }
        });
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertFalse(stepHandlers.isEmpty());
        assertEquals(1, stepHandlers.size());
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, double t, double[] y, double[] yDot) {
                // no-op
            }

            @Override
            public void init(double t, double[] y, double tEnd) {
                // no-op
            }
        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        stepHandlers.add(new StepHandler() {
            @Override
            public void handleStep(StepContext context, double t, double[] y, double[] yDot) {
                // no-op
            }

            @Override
            public void init(double t, double[] y, double tEnd) {
                // no-op
            }
        });
        integrator.setStepHandlers(new ArrayList<>());
    }

    @Test
    public void testGetEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(StepContext context) {
                // no-op
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no-op
            }
        }, 0, 0, 0, new BracketingNthOrderBrentSolver(0, 5));
        EventHandler eventHandler = integrator.getEventHandlers().iterator().next();
        assertSame(eventHandler.getClass(), AbstractIntegratorTest$DummyEventHandler.class);
    }

    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(StepContext context) {
                // no-op
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no-op
            }
        }, 0, 0, 0, new BracketingNthOrderBrentSolver(0, 5));
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertFalse(eventHandlers.isEmpty());
        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateStep(StepContext context) {
                // no-op
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no-op
            }
        }, 0, 0, 0, new BracketingNthOrderBrentSolver(0, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        eventHandlers.add(new EventHandler() {
            @Override
            public void evaluateStep(StepContext context) {
                // no-op
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // no-op
            }
        });
        integrator.setEventHandlers(new ArrayList<>());
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        double currentStepStart = integrator.getCurrentStepStart();
        assertEquals(Double.NaN, currentStepStart, 0.0);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        integrator.evaluations.setCount(10);
        assertEquals(10, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[1];
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0, y[0], 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
                yDot[1] = y[1] * 2.0;
            }
        };
        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double t = 1.0;
        double[] y = new double[1];
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateNumberIsTooSmall() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0E-308;
        double[] y = new double[1];
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxCountExceeded() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[1];
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Name");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[1];
        integrator.integrate(equations, t0, y0, t, y);
        integrator.addEventHandler(new StepEventHandler() {
            @Override
            public void handleStep(StepContext context, double t, double[] y, double[] yDot) throws MaxCountExceededException {
                fail("Should not be called");
            }

            @Override
            public void init(double t, double[] y, double tEnd) {
                // no-op
            }
        }, 0, 0, 0, new BracketingNthOrderBrentSolver(0, 5));
        integrator.integrate(equations, t0, y0, t, y);
    }

    private static class DummyEventHandler implements EventHandler {
        @Override
        public void evaluateStep(StepContext context) {
            // no-op
        }

        @Override
        public void handleEvent(double t, double[] y) {
            // no-op
        }
    }
}