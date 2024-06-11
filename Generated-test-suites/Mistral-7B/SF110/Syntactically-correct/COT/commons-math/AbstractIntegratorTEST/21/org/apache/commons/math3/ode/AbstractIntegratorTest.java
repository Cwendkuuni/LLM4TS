package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepRequest;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class AbstractIntegratorTest {

 private static final double TOLERANCE = 1e-12;
 private static final double DELTA_T = 0.1;

 @Test
 public void testConstructor() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 assertEquals("TestIntegrator", integrator.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullName() {
 new AbstractIntegrator(null);
 }

 @Test
 public void testAddStepHandler() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 StepHandler handler = mock(StepHandler.class);
 integrator.addStepHandler(handler);
 assertEquals(1, integrator.getStepHandlers().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEventHandlerNullEventHandler() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addEventHandler(null, 0.0, 0.0, 0);
 }

 @Test
 public void testAddEventHandler() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 EventHandler handler = mock(EventHandler.class);
 integrator.addEventHandler(handler, 0.0, 0.0, 0, new BracketingNthOrderBrentSolver(0.0, 5));
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerNegativeMaxCheckInterval() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 EventHandler handler = mock(EventHandler.class);
 integrator.addEventHandler(handler, -0.01, 0.0, 0, new BracketingNthOrderBrentSolver(0.0, 5));
 }

 @Test
 public void testAddEventHandlerMaxCheckIntervalZero() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 EventHandler handler = mock(EventHandler.class);
 integrator.addEventHandler(handler, 0.0, 0.0, 0, new BracketingNthOrderBrentSolver(0.0, 5));
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test
 public void testClearStepHandlers() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 StepHandler handler1 = mock(StepHandler.class);
 integrator.addStepHandler(handler1);
 StepHandler handler2 = mock(StepHandler.class);
 integrator.addStepHandler(handler2);
 integrator.clearStepHandlers();
 assertEquals(0, integrator.getStepHandlers().size());
 }

 @Test
 public void testClearEventHandlers() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 EventHandler handler1 = mock(EventHandler.class);
 integrator.addEventHandler(handler1, 0.0, 0.0, 0, new BracketingNthOrderBrentSolver(0.0, 5));
 EventHandler handler2 = mock(EventHandler.class);
 integrator.addEventHandler(handler2, 0.0, 0.0, 0, new BracketingNthOrderBrentSolver(0.0, 5));
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetCurrentStepStart() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 double expected = Double.NaN;
 assertEquals(expected, integrator.getCurrentStepStart(), TOLERANCE);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 double expected = Double.NaN;
 assertEquals(expected, integrator.getCurrentSignedStepsize(), TOLERANCE);
 }

 @Test
 public void testSetMaxEvaluations() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testSetMaxEvaluationsNegative() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 integrator.setMaxEvaluations(-1);
 assertEquals(-1, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 integrator.setMaxEvaluations(10);
 integrator.computeDerivatives(0.0, new double[0], new double[0]);
 integrator.getEvaluationsCounter().incrementCount();
 integrator.getEvaluationsCounter().incrementCount();
 integrator.getEvaluationsCounter().incrementCount();
 assertEquals(3, integrator.getEvaluations());
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateNullEquations() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 integrator.integrate(null, 0.0, new double[0], 1.0, new double[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateNegativeStepSize() {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 integrator.integrate(equations, 0.0, new double[0], -0.1, new double[0]);
 }

 @Test
 public void testIntegrate() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 when(equations.getDimension()).thenReturn(2);
 when(equations.getTime()).thenReturn(0.0);
 when(equations.getPrimaryState()).thenReturn(new double[] { 1.0, 2.0 });
 when(equations.getPrimaryDerivatives()).thenReturn(new double[] { 3.0, 4.0 });
 integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 1.0, new double[] { 3.0, 4.0 });
 verify(equations).setTime(1.0);
 verify(equations).setPrimaryState(new double[] { 3.0, 4.0 });
 }

 @Test
 public void testIntegrateWithEventHandler() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 EventHandler handler = mock(EventHandler.class);
 integrator.addEventHandler(handler, 0.1, 0.01, 1, new BracketingNthOrderBrentSolver(0.0, 5));
 when(equations.getDimension()).thenReturn(2);
 when(equations.getTime()).thenReturn(0.0);
 when(equations.getPrimaryState()).thenReturn(new double[] { 1.0, 2.0 });
 when(equations.getPrimaryDerivatives()).thenReturn(new double[] { 3.0, 4.0 });
 integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 1.0, new double[] { 3.0, 4.0 });
 verify(handler).handleEvent(eq(isA(StepRequest.class)), any());
 }

 @Test
 public void testAcceptStepForward() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 ExpandableStatefulODE ode = mock(ExpandableStatefulODE.class);
 when(ode.getTime()).thenReturn(0.0);
 when(ode.getPrimaryState()).thenReturn(new double[] { 1.0, 2.0 });
 when(ode.getPrimaryDerivatives()).thenReturn(new double[] { 3.0, 4.0 });
 when(ode.setTime(anyDouble())).thenAnswer(i -> ode.getTime() + DELTA_T);
 StepHandler handler = mock(StepHandler.class);
 integrator.addStepHandler(handler);
 integrator.setExpandable(ode);
 integrator.setMaxEvaluations(10);
 integrator.setMaxStepSize(DELTA_T);
 integrator.initIntegration(0.0, new double[] { 1.0, 2.0 }, 1.0);
 double expected = 0.0 + DELTA_T;
 double result = integrator.acceptStep(new AbstractStepInterpolator() {}, new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 }, 1.0 + DELTA_T);
 verify(handler).handleStep(any(AbstractStepInterpolator.class), eq(false));
 assertEquals(expected, result, TOLERANCE);
 }

 @Test
 public void testAcceptStepBackward() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 ExpandableStatefulODE ode = mock(ExpandableStatefulODE.class);
 when(ode.getTime()).thenReturn(1.0);
 when(ode.getPrimaryState()).thenReturn(new double[] { 3.0, 4.0 });
 when(ode.getPrimaryDerivatives()).thenReturn(new double[] { 5.0, 6.0 });
 when(ode.setTime(anyDouble())).thenAnswer(i -> ode.getTime() - DELTA_T);
 StepHandler handler = mock(StepHandler.class);
 integrator.addStepHandler(handler);
 integrator.setExpandable(ode);
 integrator.setMaxEvaluations(10);
 integrator.setMaxStepSize(DELTA_T);
 integrator.initIntegration(0.0, new double[] { 1.0, 2.0 }, 1.0);
 double expected = 1.0 - DELTA_T;
 double result = integrator.acceptStep(new AbstractStepInterpolator() {
 @Override
 public boolean isForward() {
 return false;
 }
 }, new double[] { 3.0, 4.0 }, new double[] { 5.0, 6.0 }, 1.0 - DELTA_T);
 verify(handler).handleStep(any(AbstractStepInterpolator.class), eq(false));
 assertEquals(expected, result, TOLERANCE);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testAcceptStepMaxEvaluationsReached() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 ExpandableStatefulODE ode = mock(ExpandableStatefulODE.class);
 when(ode.getTime()).thenReturn(0.0);
 when(ode.getPrimaryState()).thenReturn(new double[] { 1.0, 2.0 });
 when(ode.getPrimaryDerivatives()).thenReturn(new double[] { 3.0, 4.0 });
 when(ode.setTime(anyDouble())).thenAnswer(i -> ode.getTime() + DELTA_T);
 StepHandler handler = mock(StepHandler.class);
 integrator.addStepHandler(handler);
 integrator.setExpandable(ode);
 integrator.setMaxEvaluations(2);
 integrator.setMaxStepSize(DELTA_T);
 integrator.initIntegration(0.0, new double[] { 1.0, 2.0 }, 1.0);
 integrator.acceptStep(new AbstractStepInterpolator() {}, new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 }, 1.0 + DELTA_T);
 integrator.acceptStep(new AbstractStepInterpolator() {}, new double[] { 3.0, 4.0 }, new double[] { 7.0, 8.0 }, 2.0 + DELTA_T);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateDimensionMismatch() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 when(equations.getDimension()).thenReturn(3);
 integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 1.0, new double[] { 3.0, 4.0 });
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateNumberIsTooSmall() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 when(equations.getDimension()).thenReturn(2);
 integrator.sanityChecks(equations, 0.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateNoBracketing() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 when(equations.getDimension()).thenReturn(2);
 integrator.sanityChecks(equations, 0.0);
 when(equations.getPrimaryState()).thenReturn(new double[] { 1.0, 2.0 });
 when(equations.getPrimaryDerivatives()).thenReturn(new double[] { 3.0, 4.0 });
 when(equations.getRoots(anyDouble(), anyDouble(), any(UnivariateSolver.class))).thenThrow(new NoBracketingException());
 integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 1.0, new double[0]);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateMaxCountExceeded() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
 when(equations.getDimension()).thenReturn(2);
 integrator.sanityChecks(equations, 0.0);
 when(equations.getPrimaryState()).thenReturn(new double[] { 1.0, 2.0 });
 when(equations.getPrimaryDerivatives()).thenReturn(new double[] { 3.0, 4.0 });
 when(equations.getRoots(anyDouble(), anyDouble(), any(UnivariateSolver.class))).thenThrow(new MaxCountExceededException());
 integrator.integrate(equations, 0.0, new double[] { 1.0, 2.0 }, 1.0, new double[0]);
 }

}