package org.apache.commons.cli;

import static org.junit.Assert.*;
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
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddStepHandlerToConstantSizeList() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public Collection<StepHandler> getStepHandlers() {
                return Collections.unmodifiableList(Arrays.asList(new StepHandler[0]));
            }
        };
        integrator.addStepHandler(mock(StepHandler.class));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.addStepHandler(mock(StepHandler.class));
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.initIntegration(0.0, new double[0], 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitIntegrationWithNullEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.initIntegration(0.0, null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitIntegrationWithNullY0() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.initIntegration(0.0, null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitIntegrationWithNullT() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.initIntegration(0.0, new double[0], null);
    }

    @Test
    public void testSetEvaluationsCounter() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        Incrementor evaluations = mock(Incrementor.class);
        integrator.setEvaluationsCounter(evaluations);
        assertEquals(evaluations, integrator.getEvaluationsCounter());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public Collection<EventHandler> getEventHandlers() {
                return Collections.unmodifiableList(Arrays.asList(new EventHandler[0]));
            }
        };
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 1.0, 0.001, 10);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEventHandlerToConstantSizeList() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public Collection<EventHandler> getEventHandlers() {
                return Collections.unmodifiableList(Arrays.asList(new EventHandler[0]));
            }
        };
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 0.001, 10);
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.addEventHandler(mock(EventHandler.class), 1.0, 0.001, 10);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
        verify(equations).setTime(t);
        verify(equations).setPrimaryState(y);

        integrator.verifyNoMoreInteractions();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        FirstOrderDifferentialEquations equations = null;
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullY0() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = null;
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullY() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = null;

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullT() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = null;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[2];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithSmallStepSize() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.setMaxEvaluations(Integer.MIN_VALUE);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluationsReached() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.setMaxEvaluations(1);
        integrator.setMaxStepSize(Double.MIN_VALUE);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t = 0.0;
        double[] y = {1.0};
        double[] yDot = new double[y.length];

        integrator.computeDerivatives(t, y, yDot);
        verify(equations).computeDerivatives(t, y, yDot);

        integrator.verifyNoMoreInteractions();
    }
}