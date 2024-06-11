package org.apache.commons.math3.ode.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;

public class AbstractIntegratorTest {

 private AbstractIntegrator abstractIntegrator;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 abstractIntegrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Empty implementation for testing purposes
 }
 };
 }

 @Test
 public void testConstructor() {
 assertEquals("TestIntegrator", abstractIntegrator.getName());
 assertTrue(abstractIntegrator.getStepHandlers().isEmpty());
 assertTrue(abstractIntegrator.getEventHandlers().isEmpty());
 assertEquals(-1, abstractIntegrator.getMaxEvaluations());
 assertEquals(0, abstractIntegrator.getEvaluations());
 }

 @Test
 public void testAddStepHandler() {
 abstractIntegrator.addStepHandler(new AbstractStepHandler() {
 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean lastStep) {
 fail("Step handler should not be called during testing");
 }
 });
 assertEquals(1, abstractIntegrator.getStepHandlers().size());
 }

 @Test
 public void testClearStepHandlers() {
 abstractIntegrator.clearStepHandlers();
 assertTrue(abstractIntegrator.getStepHandlers().isEmpty());
 }

 @Test
 public void testAddEventHandler() throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 abstractIntegrator.addEventHandler(new AbstractEventHandler() {
 @Override
 public void handleEvent(double t, double[] y) {
 fail("EventHandler should not be called during testing");
 }
 }, 0.0, 1.0E-6, 10);
 assertEquals(1, abstractIntegrator.getEventHandlers().size());
 }

 @Test
 public void testClearEventHandler() {
 abstractIntegrator.clearEventHandlers();
 assertTrue(abstractIntegrator.getEventHandlers().isEmpty());
 }

 @Test
 public void testGetCurrentStepStart() {
 assertEquals(Double.NaN, abstractIntegrator.getCurrentStepStart(), 0.0);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 assertEquals(Double.NaN, abstractIntegrator.getCurrentSignedStepsize(), 0.0);
 }

 @Test
 public void testSetMaxEvaluations() {
 abstractIntegrator.setMaxEvaluations(5);
 assertEquals(5, abstractIntegrator.getMaxEvaluations());
 }

 @Test
 public void testGetMaxEvaluations() {
 assertEquals(Integer.MAX_VALUE, abstractIntegrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 assertEquals(0, abstractIntegrator.getEvaluations());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithDimensionMismatch() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(new double[] { 1.0, 2.0 }, 2);
 abstractIntegrator.integrate(equations, 0.0, new double[] { 1.0, 1.0 }, 1.0, new double[3]);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithTooSmallStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(new double[] { 1.0 }, 1);
 abstractIntegrator.sanityChecks(equations, 0.0);
 abstractIntegrator.integrate(equations, 0.0, new double[] { 1.0 }, 1.0E-10, new double[1]);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(new double[] { 1.0 }, 1);
 abstractIntegrator.setMaxEvaluations(1);
 abstractIntegrator.integrate(equations, 0.0, new double[] { 1.0 }, 1.0, new double[1]);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(new double[] { 1.0, 1.0 }, 2);
 abstractIntegrator.integrate(equations, 0.0, new double[] { 1.0, 1.0 }, 1.0, new double[2]);
 }
}