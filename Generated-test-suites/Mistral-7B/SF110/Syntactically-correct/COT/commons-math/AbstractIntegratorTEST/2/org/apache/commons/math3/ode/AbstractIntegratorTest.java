package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.utils.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private ExpandableStatefulODE ode;
 private double[] y0 = {1.0, 2.0};

 @Before
 public void setUp() throws Exception {
 integrator = new MyAbstractIntegrator("MyIntegrator");
 integrator.setMaxEvaluations(10);
 ode = new ExpandableStatefulODE(new MyDifferentialEquations()) {
 @Override
 public int getDimension() {
 return y0.length;
 }
 };
 ode.setTime(0.0);
 ode.setPrimaryState(y0);
 }

 @Test
 public void testConstructor() {
 assertEquals("MyIntegrator", integrator.getName());
 assertNotNull(integrator.getStepHandlers());
 assertFalse(integrator.isLastStep());
 assertFalse(integrator.isStatesInitialized());
 assertNull(integrator.getExpandable());
 }

 @Test
 public void testAddStepHandler() {
 StepHandler handler = mock(StepHandler.class);
 integrator.addStepHandler(handler);
 assertTrue(integrator.getStepHandlers().contains(handler));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEventHandlerWithNullEventHandler() {
 integrator.addEventHandler(null, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithNegativeMaxCheckInterval() {
 integrator.addEventHandler(mock(EventHandler.class), -1.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
 }

 @Test
 public void testAddEventHandler() {
 EventHandler handler = mock(EventHandler.class);
 integrator.addEventHandler(handler, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
 assertTrue(integrator.getEventHandlers().contains(handler));
 }

 @Test
 public void testClearEventHandlers() {
 integrator.addEventHandler(mock(EventHandler.class), 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
 integrator.clearEventHandlers();
 assertFalse(integrator.getEventHandlers().isEmpty());
 integrator.clearEventHandlers();
 assertTrue(integrator.getEventHandlers().isEmpty());
 }

 @Test
 public void testGetCurrentStepStart() {
 integrator.initIntegration(0.0, y0, 1.0);
 assertEquals(0.0, integrator.getCurrentStepStart(), 1e-14);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 integrator.initIntegration(0.0, y0, 1.0);
 assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator.setMaxEvaluations(5);
 assertEquals(5, integrator.getMaxEvaluations());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxEvaluationsNegativeNumber() {
 integrator.setMaxEvaluations(-1);
 }

 @Test
 public void testGetEvaluations() {
 integrator.setMaxEvaluations(10);
 integrator.integrate(ode, 0.0, y0, 1.0);
 assertEquals(10, integrator.getEvaluations());
 }

 @Test
 public void testInitIntegration() {
 integrator.initIntegration(0.0, y0, 1.0);
 assertFalse(integrator.isStatesInitialized());
 }

 @Test
 public void testIntegrate() throws Exception {
 integrator.integrate(ode, 0.0, y0, 1.0);
 assertFalse(integrator.isLastStep());
 assertFalse(integrator.isStatesInitialized());
 assertFalse(integrator.resetOccurred);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithIncorrectDimensions() throws Exception {
 integrator.setMaxEvaluations(10);
 double[] y0_mismatched = {1.0};
 integrator.integrate(ode, 0.0, y0_mismatched, 1.0);
 fail("Did not throw DimensionMismatchException");
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithSmallStep() throws Exception {
 integrator.setMaxEvaluations(10);
 double tEnd = 1.0e-11;
 double[] y = new double[ode.getDimension()];
 integrator.integrate(ode, 0.0, y0, tEnd, y);
 fail("Did not throw NumberIsTooSmallException");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(3);
 integrator.integrate(ode, 0.0, y0, 1.0);
 fail("Did not throw MaxCountExceededException");
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 integrator.setMaxEvaluations(10);
 integrator.setExpandable(ode);
 ode.setPrimaryState(y0);
 integrator.integrate(ode, 0.0, y0, 1.0);
 fail("Did not throw NoBracketingException");
 }

 @Test
 public void testComputeDerivatives() throws Exception {
 double t = 1.0;
 integrator.integrate(ode, 0.0, y0, t);
 integrator.computeDerivatives(t, y0, new double[ode.getDimension()]);
 }

 @Test
 public void testAcceptStep() throws Exception {
 StepInterpolator interpolator = new StepInterpolator(ode.getPrimaryMapper(), StepInterpolator.Type.RK4);
 integrator.integrate(ode, 0.0, y0, 1.0);
 double tEnd = 3.0;
 double[] y = new double[ode.getDimension()];
 double step = integrator.acceptStep(interpolator, y, new double[ode.getDimension()], tEnd);
 assertEquals(tEnd, step, 1e-14);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testAcceptStepWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(1);
 StepInterpolator interpolator = new StepInterpolator(ode.getPrimaryMapper(), StepInterpolator.Type.RK4);
 integrator.integrate(ode, 0.0, y0, 1.0);
 double tEnd = 3.0;
 double[] y = new double[ode.getDimension()];
 integrator.acceptStep(interpolator, y, new double[ode.getDimension()], tEnd);
 fail("Did not throw MaxCountExceededException");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAcceptStepWithIncorrectDimensions() throws Exception {
 integrator.setMaxEvaluations(10);
 StepInterpolator interpolator = new StepInterpolator(ode.getPrimaryMapper(), StepInterpolator.Type.RK4);
 integrator.integrate(ode, 0.0, y0, 1.0);
 double[] y_mismatched = new double[ode.getDimension() + 1];
 integrator.acceptStep(interpolator, y_mismatched, new double[ode.getDimension()], 1.0);
 fail("Did not throw DimensionMismatchException");
 }

 @Test(expected = NoBracketingException.class)
 public void testAcceptStepWithNoBracketing() throws Exception {
 integrator.setMaxEvaluations(10);
 integrator.setExpandable(ode);
 StepInterpolator interpolator = new StepInterpolator(ode.getPrimaryMapper(), StepInterpolator.Type.RK4);
 integrator.integrate(ode, 0.0, y0, 1.0);
 double[] y = new double[ode.getDimension()];
 integrator.acceptStep(interpolator, y, new double[ode.getDimension()], 1.0);
 fail("Did not throw NoBracketingException");
 }
}