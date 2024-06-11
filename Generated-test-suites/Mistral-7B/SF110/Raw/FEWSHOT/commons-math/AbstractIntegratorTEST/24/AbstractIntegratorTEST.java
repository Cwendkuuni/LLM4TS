package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.interpolators.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolatorFactory;
import org.apache.commons.math3.ode.solvers.IDAEventAdapter;
import org.apache.commons.math3.ode.solvers.IDAStepHandler;
import org.apache.commons.math3.ode.solvers.NoBracketingException;
import org.apache.commons.math3.ode.solvers.StepControlledODEIntegrator;
import org.apache.commons.math3.ode.solvers.StepSizeControl;
import org.apache.commons.math3.ode.solvers.UnivariateSolver;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private StepInterpolator interpolator;
    private StepHandler stepHandler;
    private EventHandler eventHandler;
    private FirstOrderDifferentialEquations equations;
    private double[] y;
    private double tStart;

    @Before
    public void setUp() {
        integrator = new StepControlledODEIntegrator(new IDAStepHandler() {
            @Override
            public void handleStep(IDAEventAdapter event, double[] oldY, double time, double[] newY) {
                stepHandler.handleStep(event, true);
            }
        }, new IDAStepHandler() {
            @Override
            public void handleStep(IDAEventAdapter event, double[] oldY, double time, double[] newY) {
                eventHandler.handleStep(event);
            }
        }, new StepControlledODEIntegrator.StepSizeControl() {
            @Override
            public double computeStepSize(IDAEventAdapter event, double[] oldY, double time, double[] newY) {
                return 0.1;
            }
        });
        integrator.setMaxEvaluations(100);
        interpolator = new StepInterpolatorFactory().createStepInterpolator();
        stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean last) {
                //Add your test assertions here
            }
        };
        eventHandler = new IDAEventAdapter() {
            @Override
            public void handleStep(double time, double[] y) {
                //Add your test assertions here
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };
        y = new double[1];
        tStart = 0.0;
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(eventHandler, 0.1, 1.0E-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testIntegrate() throws NoBracketingException, DimensionMismatchException, MaxCountExceededException, NumberIsTooSmallException {
        integrator.initIntegration(tStart, y, 1.0);
        integrator.setEquations(equations);
        double tEnd = 1.1;
        integrator.integrate(equations, tStart, y, tEnd, y);
        assertEquals(tEnd, integrator.getCurrentStepStart(), 1.0E-6);
        assertEquals(tEnd, equations.getTime(), 1.0E-6);
        assertEquals(1 + y.length, integrator.getEvaluations(), 1);
    }

    @Test
    public void testAcceptStep() throws NoBracketingException, DimensionMismatchException, MaxCountExceededException {
        integrator.initIntegration(tStart, y, 0.5);
        integrator.setEquations(equations);
        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 0.6;
            }
        }, 0.01);
        integrator.integrate(equations, tStart, y, 1.0, y);
        assertFalse(integrator.isLastStep());
        assertEquals(1.0, integrator.getCurrentStepStart(), 1.0E-6);
        integrator.acceptStep(interpolator, y, null, 0.6);
        assertTrue(integrator.isLastStep());
        assertEquals(0.6, integrator.getCurrentStepStart(), 1.0E-6);
        reset(integrator);
        integrator.integrate(equations, tStart, y, 1.1, y);
        assertFalse(integrator.isLastStep());
        assertEquals(1.1, integrator.getCurrentStepStart(), 1.0E-6);
        integrator.acceptStep(interpolator, y, null, 1.1);
        assertTrue(integrator.isLastStep());
        assertEquals(1.1, integrator.getCurrentStepStart(), 1.0E-6);
        assertEquals(2 + y.length, integrator.getEvaluations(), 1);
    }

    @Test
    public void testReset() {
        integrator.initIntegration(tStart, y, 0.5);
        integrator.setEquations(equations);
        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 0.6;
            }
        }, 0.01);
        integrator.integrate(equations, tStart, y, 1.0, y);
        integrator.acceptStep(interpolator, y, null, 0.6);
        integrator.reset();
        assertFalse(integrator.isLastStep());
        assertEquals(1.0, integrator.getCurrentStepStart(), 1.0E-6);
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator = new StepControlledODEIntegrator(new IDAStepHandler() {
            @Override
            public void handleStep(IDAEventAdapter event, double[] oldY, double time, double[] newY) {
                stepHandler.handleStep(event, true);
            }
        }, new IDAStepHandler() {
            @Override
            public void handleStep(IDAEventAdapter event, double[] oldY, double time, double[] newY) {
                eventHandler.handleStep(event);
            }
        }, new StepControlledODEIntegrator.StepSizeControl() {
            @Override
            public double computeStepSize(IDAEventAdapter event, double[] oldY, double time, double[] newY) {
                return 0.1;
            }
        });
        integrator.setMaxEvaluations(5);
        integrator.initIntegration(tStart, y, 0.5);
        integrator.setEquations(equations);
        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 0.6;
            }
        }, 0.01);
        integrator.integrate(equations, tStart, y, 1.0, y);
        integrator.acceptStep(interpolator, y, null, 0.6);
        integrator.acceptStep(interpolator, y, null, 1.0);
        integrator.acceptStep(interpolator, y, null, 1.0);
        integrator.acceptStep(interpolator, y, null, 1.0);
        integrator.acceptStep(interpolator, y, null, 1.0);
        assertTrue(integrator.isLastStep());
        assertEquals(6, integrator.getEvaluations(), 1);
    }
}