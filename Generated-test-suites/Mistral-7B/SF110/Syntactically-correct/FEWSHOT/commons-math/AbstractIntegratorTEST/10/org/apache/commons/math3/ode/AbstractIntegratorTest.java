package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.*;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

@RunWith(Parameterized.class)
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double t0;
    private double[] y0;
    private double tEnd;

    public AbstractIntegratorTest(String name, double[] y0, double t0, double tEnd) {
        this.integrator = new AbstractDopRI55Integrator(name);
        this.equations = Mockito.mock(ExpandableStatefulODE.class);
        this.y0 = y0;
        this.t0 = t0;
        this.tEnd = tEnd;
    }

    @Parameterized.Parameters(name = "{index}: test integrate({0}, {1}, {2}, {3})")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new double[]{0.0}, 0.0, 1.0, 2.0},
                {new double[]{1.0, 1.0}, 0.0, 1.0, 2.0},
                {new double[]{0.0, 0.1}, 0.0, 1.0, 1.5},
                {new double[]{0.0, 1.0, 0.2}, 0.0, 1.0, 2.0},
                {new double[]{0.0, 0.1, 0.2, 0.3}, 0.0, 1.0, 3.0}
        });
    }

    @Test
    public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
        Mockito.verify(equations).setTime(eq -> eq.setTime(result));
        Mockito.verify(equations).setPrimaryState(eq -> Arrays.deepEquals(eq.getPrimaryState(), new double[y0.length]));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_wrong_y0_length() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        Mockito.when(equations.getDimension()).thenReturn(y0.length + 1);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length - 1]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_wrong_y_length() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length + 1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrate_max_evaluations_exceeded() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integratorSpy = Mockito.spy(integrator);
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        integratorSpy.integrate(equations, t0, y0, tEnd, new double[y0.length]);
        integrator.setMaxEvaluations(0);
        integratorSpy.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrate_no_bracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integratorSpy = Mockito.spy(integrator);
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        integratorSpy.setMaxEvaluations(1);
        integratorSpy.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

    @Test
    public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integratorSpy = Mockito.spy(integrator);
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.getGlobalPreviousTime()).thenReturn(t0);
        Mockito.when(interpolator.getGlobalCurrentTime()).thenReturn(tEnd);
        integratorSpy.acceptStep(interpolator, Mockito.mock(boolean[].class), Mockito.mock(double[][].class), tEnd);
        Mockito.verify(interpolator).setSoftPreviousTime(eq -> eq.setSoftPreviousTime(t0));
        Mockito.verify(interpolator).setSoftCurrentTime(eq -> eq.setSoftCurrentTime(tEnd));
        Mockito.verify(interpolator).setInterpolatedTime(eq -> eq.setInterpolatedTime(tEnd));
        Mockito.verify(integrator, times(y0.length)).setLastStep(Mockito.anyBoolean());
        Mockito.verify(integrator, times(y0.length)).handleStep(interpolator, Mockito.eq(true));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testAcceptStep_max_evaluations_exceeded() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integratorSpy = Mockito.spy(integrator);
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.getGlobalPreviousTime()).thenReturn(t0);
        Mockito.when(interpolator.getGlobalCurrentTime()).thenReturn(tEnd);
        integratorSpy.acceptStep(interpolator, Mockito.mock(boolean[].class), Mockito.mock(double[][].class), tEnd);
        integrator.setMaxEvaluations(0);
        integratorSpy.acceptStep(interpolator, Mockito.mock(boolean[].class), Mockito.mock(double[][].class), tEnd);
    }

    @Test(expected = NoBracketingException.class)
    public void testAcceptStep_no_bracketing() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractIntegrator integratorSpy = Mockito.spy(integrator);
        Mockito.when(equations.getDimension()).thenReturn(y0.length);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setEquations(equations);
        StepInterpolator interpolator = Mockito.mock(StepInterpolator.class);
        Mockito.when(interpolator.getGlobalPreviousTime()).thenReturn(t0);
        Mockito.when(interpolator.getGlobalCurrentTime()).thenReturn(tEnd);
        integratorSpy.setMaxEvaluations(1);
        integratorSpy.acceptStep(interpolator, Mockito.mock(boolean[].class), Mockito.mock(double[][].class), tEnd);
    }

    @Test
    public void testGetName() {
        assertEquals("AbstractIntegrator", integrator.getName());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        assertEquals(2, integrator.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(42);
        assertEquals(42, integrator.getMaxEvaluations());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(Mockito.mock(StepHandler.class));
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(Mockito.mock(StepHandler.class));
        integrator.addStepHandler(Mockito.mock(StepHandler.class));
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(2, stepHandlers.size());
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(Mockito.mock(StepHandler.class));
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(1, stepHandlers.size());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        integrator.addEventHandler(Mockito.mock(EventHandler.class), 0.0, 1.0e-12, 10);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetCurrentStepStart() {
        assertEquals(Double.NaN, integrator.getCurrentStepStart());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
    }

    @Test
    public void testGetExpandable() {
        ExpandableStatefulODE expandableODE = Mockito.mock(ExpandableStatefulODE.class);
        integrator.setEquations(expandableODE);
        assertEquals(expandableODE, integrator.getExpandable());
    }

    @Test
    public void testGetEvaluationsCounter() {
        Incrementor evaluationsCounter = Mockito.mock(Incrementor.class);
        integrator.setEvaluationsCounter(evaluationsCounter);
        assertEquals(evaluationsCounter, integrator.getEvaluationsCounter());
    }
}