package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;

 @Before
 public void setUp() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(final ExpandableStatefulODE p0, final double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Empty implementation for testing
 }
 };
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean lastStep) {
 assertEquals("Test", integrator.getName());
 }
 });
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean lastStep) {
 fail("Unexpected step handler added");
 }
 });
 assertEquals(2, integrator.getStepHandlers().size());
 }

 @Test
 public void testClearStepHandlers() {
 integrator.clearStepHandlers();
 assertEquals(0, integrator.getStepHandlers().size());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(new AbstractStatefulODEEvent() {
 @Override
 public double getEventTime() {
 return 1.0;
 }
 }, 0.0, 1e-6, 10);
 integrator.addEventHandler(new AbstractStatefulODEEvent() {
 @Override
 public double getEventTime() {
 return 2.0;
 }
 }, 0.0, 1e-6, 10);
 assertEquals(2, integrator.getEventHandlers().size());
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testInitIntegration() {
 integrator.initIntegration(0.0, new double[0], 1.0);
 assertTrue(integrator.getEvaluationsCounter().getCount() == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitIntegrationWithInvalidInitialStateSize() {
 integrator.initIntegration(0.0, new double[3], 1.0);
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }
 };
 double[] initialState = { 0.0 };
 double tEnd = 2.0;
 double[] resultState = new double[1];
 integrator.integrate(equations, 0.0, initialState, tEnd, resultState);
 assertEquals(tEnd, resultState[0], 1e-6);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithInvalidInitialStateSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 3;
 }
 };
 double[] initialState = { 0.0, 0.0, 0.0 };
 double tEnd = 2.0;
 integrator.integrate(equations, 0.0, initialState, tEnd, new double[1]);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithTooSmallStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }
 };
 double[] initialState = { 0.0 };
 double tEnd = 2.0;
 integrator.setMaxEvaluations(2);
 integrator.integrate(equations, 0.0, initialState, tEnd, new double[1]);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }
 };
 double[] initialState = { 0.0 };
 double tEnd = 2.0;
 integrator.setMaxEvaluations(3);
 integrator.integrate(equations, 0.0, initialState, tEnd, new double[1]);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double[] computeDerivatives(double t, double[] y) {
 return new double[] { -y[0] };
 }
 };
 double[] initialState = { 0.0 };
 double tEnd = 1.0;
 integrator.integrate(equations, 0.0, initialState, tEnd, new double[1]);
 }

}