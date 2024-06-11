package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // To be implemented in subclasses
            }
        };

        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // To be implemented in subclasses
            }
        };

        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 0.0, 0.0, 0);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // To be implemented in subclasses
            }
        };

        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // To be implemented in subclasses
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = new double[1];
        double t = 1.0;
        double[] y = new double[1];

        integrator.initIntegration(t0, y0, t);
        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(y0[0], y[0]);
        assertEquals(t0, integrator.getCurrentStepStart(), 0.0);
        assertEquals(t, integrator.getCurrentStepStart() + integrator.getCurrentSignedStepsize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatch() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // To be implemented in subclasses
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = new double[2];
        double t = 1.0;
        double[] y = new double[1];

        integrator.initIntegration(t0, y0, t);
        integrator.integrate(equations, t0, y0, t, y);
    }
}