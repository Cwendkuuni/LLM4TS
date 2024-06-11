package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionException;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertNotNull(integrator);
        assertEquals("IntegratorName", integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertTrue(integrator.getEventHandlers().isEmpty());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEventHandlerWithNullEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.addEventHandler(null, 0.0, 1.0, 10);
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 0.0, 1.0, 10);
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        StepHandler handler1 = mock(StepHandler.class);
        StepHandler handler2 = mock(StepHandler.class);
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        EventHandler handler1 = mock(EventHandler.class);
        EventHandler handler2 = mock(EventHandler.class);
        integrator.addEventHandler(handler1, 0.0, 1.0, 10);
        integrator.addEventHandler(handler2, 0.0, 1.0, 10);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.initIntegration(0.0, new double[0], 1.0);
        verify(equations).init(0.0, new double[0], 1.0);
        verify(stepHandler).init(0.0, new double[0], 1.0);
        assertEquals(false, integrator.getStatesInitialized());
    }

    @Test(expected = NullPointerException.class)
    public void testSetEquationsWithNullEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setEquations(null);
    }

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws Exception {
                // Custom implementation for testing
            }
        };
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getPrimaryState()).thenReturn(new double[0]);
        when(equations.getDimension()).thenReturn(0);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        doReturn(new double[0]).when(interpolator).getInterpolatedState();
        doReturn(new double[0]).when(interpolator).getInterpolatedSecondaryState(anyInt());

        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 1.0);
        verify(equations, times(2)).setTime(1.0);
        verify(equations, times(2)).setPrimaryState(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimension() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getPrimaryState()).thenReturn(new double[]{1.0});
        when(equations.getDimension()).thenReturn(0);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        doReturn(new double[1]).when(interpolator).getInterpolatedState();

        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithSmallStepSize() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws Exception {
                integrator.sanityChecks(equations, t);
            }
        };
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getPrimaryState()).thenReturn(new double[0]);
        when(equations.getDimension()).thenReturn(0);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        doReturn(new double[0]).when(interpolator).getInterpolatedState();

        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 1.0e-10, new double[0]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluationsExceeded() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws Exception {
                integrator.getEvaluationsCounter().incrementCount();
            }
        };
        integrator.setMaxEvaluations(0);
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getPrimaryState()).thenReturn(new double[0]);
        when(equations.getDimension()).thenReturn(0);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        doReturn(new double[0]).when(interpolator).getInterpolatedState();

        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws Exception {
                integrator.sanityChecks(equations, t);
            }
        };
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getPrimaryState()).thenReturn(new double[0]);
        when(equations.getDimension()).thenReturn(0);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.0);
        doReturn(new double[0]).when(interpolator).getInterpolatedState();

        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[0], 0.0, new double[0]);
    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws Exception {
                // Custom implementation for testing
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[0];
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        });
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(0.0).when(interpolator).getGlobalPreviousTime();
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        doReturn(new double[]{1.0}).when(interpolator).getInterpolatedState();
        doReturn(new double[0]).when(interpolator).getInterpolatedSecondaryState(anyInt());

        integrator.setEquations(equations);
        expansible = equations;
        double tEnd = 2.0;
        integrator.acceptStep(interpolator, new double[]{1.0}, new double[]{1.0 * 2}, tEnd);
        assertEquals(1.0, equations.getTime(), 0.0);
        assertEquals(1.0 * 2, equations.getPrimaryState()[0], 0.0);
    }

    @Test
    public void testAcceptStepWithEventHandlers() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws Exception {
                // Custom implementation for testing
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[0];
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        });
        StepInterpolator interpolator = mock(StepInterpolator.class);
        doReturn(0.0).when(interpolator).getGlobalPreviousTime();
        doReturn(1.0).when(interpolator).getGlobalCurrentTime();
        doReturn(new double[]{1.0}).when(interpolator).getInterpolatedState();
        doReturn(new double[0]).when(interpolator).getInterpolatedSecondaryState(anyInt());

        integrator.setEquations(equations);
        ExpandableStatefulODE equations2 = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[0];
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 3;
            }
        });
        when(interpolator.getInterpolatedSecondaryState(eq(anyInt()))).thenReturn(equations2.getPrimaryState());

        integrator.addEventHandler(new AbstractEvent() {}, 0.0, 1.0, 1);
        integrator.addEventHandler(new AbstractEvent() {}, 1.5, 1.0, 1);
        integrator.setEvaluationsCounter(new Incrementor());

        integrator.acceptStep(interpolator, new double[]{1.0}, new double[]{1.0 * 2}, 2.0);
        verify(interpolator, times(3)).setInterpolatedTime(anyDouble());
        verify(interpolator, times(3)).setSoftCurrentTime(anyDouble());
        verify(interpolator, times(3)).setSoftPreviousTime(anyDouble());
        verify(interpolator, times(3)).setInterpolatedState(anyArrayOfDouble());
        verify(interpolator, times(3)).setInterpolatedSecondaryState(anyInt(), anyArrayOfDouble());
        verify(equations, times(3)).setTime(anyDouble());
        verify(equations, times(3)).setPrimaryState(anyArrayOfDouble());
        verifyNoMoreInteractions();
    }
}