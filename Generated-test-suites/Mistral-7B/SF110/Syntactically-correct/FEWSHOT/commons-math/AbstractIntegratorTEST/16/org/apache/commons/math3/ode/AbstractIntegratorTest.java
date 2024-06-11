package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertNotNull(integrator.getName());
        assertEquals("IntegratorName", integrator.getName());
        assertNull(integrator.getExpandable());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(0, integrator.getEventHandlers().size());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertEquals(0, integrator.getMaxEvaluations());
    }

    @Test
    public void testAddStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        StepHandler handler1 = mock(StepHandler.class);
        integrator.addStepHandler(handler1);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 0.1, 1e-6, 10);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertEquals(Double.NaN, integrator.getCurrentStepStart());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
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
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0, y0 = 0.0;
        double t = 1.0;
        integrator.initIntegration(t0, new double[]{y0}, t);
        verify(equations).init(t0, new double[]{y0}, t);
        assertTrue(integrator.isStateInitialized());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitIntegrationWithNullEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.initIntegration(0.0, null, 1.0);
    }

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t0 = 0.0, y0 = 0.0;
        double t = 1.0;
        double[] y = new double[1];
        integrator.integrate(equations, t0, new double[]{y0}, t, y);
        verify(equations).setTime(t0);
        verify(equations).setPrimaryState(new double[]{y0});
        integrator.integrate(equations, t);
        assertEquals(y[0], equations.getPrimaryState()[0]);
        assertEquals(t, equations.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        integrator.integrate(null, 0.0, new double[]{0.0}, 1.0, new double[1]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t0 = 0.0, y0 = 0.0;
        double t = 1.0;
        double[] y = new double[2];
        integrator.integrate(equations, t0, new double[]{y0}, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t0 = 0.0, y0 = 0.0;
        double t = 1.0;
        when(equations.getPrimaryState()).thenReturn(new double[]{0.0});
        integrator.integrate(equations, t0, new double[]{y0}, t, new double[1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t0 = 0.0, y0 = 0.0;
        double t = 1.0;
        when(equations.getPrimaryState()).thenReturn(new double[]{0.0});
        doThrow(new MaxCountExceededException()).doReturn(1.0).when(integrator).acceptStep(any(AbstractStepInterpolator.class), any(double[][].class), any(double[][].class), anyDouble());
        integrator.integrate(equations, t0, new double[]{y0}, t, new double[1]);
    }

    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t0 = 0.0, y0 = 0.0;
        double tEnd = 1.0;
        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{0.0});
        when(interpolator.getInterpolatedSecondaryState(anyInt())).thenReturn(new double[]{0.0});
        doAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgs();
                double t = ((Double) args[0]);
                double[] y = (double[]) args[1];
                double[] yDot = (double[]) args[2];
                StepHandler handler = (StepHandler) invocation.getCaller();
                handler.handleStep(interpolator, false);
                return t;
            }
        }).when(integrator).acceptStep(any(AbstractStepInterpolator.class), any(double[][].class), any(double[][].class), anyDouble());

        integrator.initIntegration(t0, new double[]{y0}, t);
        setEquations(integrator, equations);
        integrator.acceptStep(interpolator, new double[1][], new double[1][], tEnd);

        verify(equations).computeDerivatives(eq(Double.valueOf(t)), any(double[][].class), any(double[][].class));
        verify(equations, times(2)).getPrimaryState();
        verify(equations, times(1)).setPrimaryState(any(double[][].class));
        verify(equations, times(2)).getTime();
        verify(equations, times(1)).setTime(eq(tEnd));
        verify(interpolator, times(1)).setInterpolatedTime(eq(tEnd));

        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testAcceptStepWithReset() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t0 = 0.0, y0 = 0.0;
        double tEnd = 1.0;
        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{0.0});
        when(interpolator.getInterpolatedSecondaryState(anyInt())).thenReturn(new double[]{0.0});
        doAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgs();
                double t = ((Double) args[0]);
                double[] y = (double[]) args[1];
                double[] yDot = (double[]) args[2];
                StepHandler handler = (StepHandler) invocation.getCaller();
                handler.handleStep(interpolator, false);
                return t;
            }
        }).when(integrator).acceptStep(any(AbstractStepInterpolator.class), any(double[][].class), any(double[][].class), anyDouble());

        integrator.initIntegration(t0, new double[]{y0}, t);
        setEquations(integrator, equations);
        integrator.acceptStep(interpolator, new double[1][], new double[1][], tEnd);
        integrator.acceptStep(interpolator, new double[1][], new double[1][], tEnd);

        verify(equations, times(3)).computeDerivatives(eq(Double.valueOf(tEnd)), any(double[][].class), any(double[][].class));
        verify(equations, times(3)).getPrimaryState();
        verify(equations, times(1)).setPrimaryState(any(double[][].class));
        verify(equations, times(3)).getTime();
        verify(equations, times(1)).setTime(eq(tEnd));
        verify(interpolator, times(2)).setInterpolatedTime(eq(tEnd));
        verify(integrator, times(1)).resetOccurred();
    }

    @Test
    public void testSanityChecks() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t = 0.0;
        integrator.sanityChecks(equations, t);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksWithTooSmallInterval() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("IntegratorName");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        double t = 1e-10;
        integrator.sanityChecks(equations, t);
    }

    private void setEquations(AbstractIntegrator integrator, ExpandableStatefulODE equations) {
        integrator.setEquations(equations);
    }
}