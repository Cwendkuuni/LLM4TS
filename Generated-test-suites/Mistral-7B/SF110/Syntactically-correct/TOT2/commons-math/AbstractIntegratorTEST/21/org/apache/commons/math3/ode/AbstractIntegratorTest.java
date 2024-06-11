package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    @Test
    public void testAbstractIntegrator_getName() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testAbstractIntegrator_addStepHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testAbstractIntegrator_clearStepHandlers() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        StepHandler handler1 = mock(StepHandler.class);
        StepHandler handler2 = mock(StepHandler.class);
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegrator_addEventHandler_invalidHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        Object handler = new Object();
        integrator.addEventHandler(handler, 0.0, 0.0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegrator_addEventHandler_invalidConvergence() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 0.0, -0.01, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegrator_addEventHandler_invalidMaxIterationCount() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 0.0, 0.0, -1);
    }

    @Test
    public void testAbstractIntegrator_integrate_success() throws Exception {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] initialState = {0.0, 0.0};
        double tEnd = 1.0;
        double[] result = integrator.integrate(equations, 0.0, initialState, tEnd, new double[equations.getDimension()]);
        assertEquals(tEnd, result[0], 1e-9);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractIntegrator_integrate_dimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] initialState = {0.0};
        double tEnd = 1.0;
        integrator.integrate(equations, 0.0, initialState, tEnd, new double[equations.getDimension() + 1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testAbstractIntegrator_integrate_maxCountExceeded() throws Exception {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(0);
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double[] initialState = {0.0};
        double tEnd = 1.0;
        integrator.integrate(equations, 0.0, initialState, tEnd, new double[equations.getDimension()]);
    }

    @Test(expected = NoBracketingException.class)
    public void testAbstractIntegrator_integrate_noBracketing() throws Exception {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE expandable = mock(ExpandableStatefulODE.class);
        integrator.integrate(expandable, 0.0);
    }

    @Test
    public void testAbstractIntegrator_computeDerivatives() throws Exception {
        AbstractIntegrator integrator = new MyAbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = new ExpandableStatefulODE(mock(FirstOrderDifferentialEquations.class)) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Do nothing
            }
        };
        integrator.computeDerivatives(0.0, new double[equations.getDimension()], new double[equations.getDimension()]);
    }
}