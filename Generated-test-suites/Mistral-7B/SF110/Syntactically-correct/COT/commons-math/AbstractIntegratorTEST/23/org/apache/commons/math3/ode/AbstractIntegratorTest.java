package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;

 @Test
 public void testConstructor() {
 integrator = new AbstractIntegrator("TestIntegrator");
 assertEquals("TestIntegrator", integrator.getName());
 assertEquals(0, integrator.getStepHandlers().size());
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testAddStepHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, double[] y, double[] yDot, int index) {
 // Handle step event
 }
 });
 assertEquals(1, integrator.getStepHandlers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerNullHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addEventHandler(null, 0, 0, 0, new BracketingNthOrderBrentSolver(0, 5));
 }

 @Test
 public void testAddEventHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 EventHandler handler = new AbstractEventHandler() {
 @Override
 public void handleEvent(double t, double[] y) {
 // Handle event
 }
 };
 integrator.addEventHandler(handler, 0, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test
 public void testClearStepHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, double[] y, double[] yDot, int index) {
 // Handle step event
 }
 });
 integrator.clearStepHandlers();
 assertEquals(0, integrator.getStepHandlers().size());
 }

 @Test
 public void testClearEventHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator");
 EventHandler handler = new AbstractEventHandler() {
 @Override
 public void handleEvent(double t, double[] y) {
 // Handle event
 }
 };
 integrator.addEventHandler(handler, 0, 0.01, 10, new BracketingNthOrderBrentSolver(0.01, 5));
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetCurrentStepStart() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.initIntegration(0, new double[0], 1);
 assertEquals(Double.NaN, integrator.getCurrentStepStart());
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.initIntegration(0, new double[0], 1);
 assertEquals(Double.NaN, integrator.getCurrentSignedStepsize());
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxEvaluationsNegative() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(-1);
 }

 @Test
 public void testGetEvaluations() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(10);
 integrator.evaluate(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 0;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 }, 0, new double[0], 1, new double[0]);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test(expected = AssertionError.class)
 public void testIntegrateDimensionMismatch() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(10);
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 };
 integrator.integrate(equations, 0, new double[0], 1, new double[1]);
 }

 @Test(expected = AssertionError.class)
 public void testIntegrateNumberIsTooSmall() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(1);
 integrator.integrate(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 }, 0, new double[0], 1e-12, new double[1]);
 }

 @Test(expected = AssertionError.class)
 public void testIntegrateMaxCountExceeded() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(1);
 integrator.integrate(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 }, 0, new double[0], 1, new double[1]);
 integrator.integrate(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 }, 0, new double[0], 1, new double[1]);
 }

 @Test(expected = AssertionError.class)
 public void testIntegrateNoBracketing() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(1);
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 };
 integrator.initIntegration(0, new double[0], 1);
 integrator.integrate(equations, 1, new double[0], 1, new double[1]);
 integrator.integrate(equations, 1, new double[0], 1, new double[1]);
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE ode, double time) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Integrate
 }
 };
 integrator.setMaxEvaluations(10);
 FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Compute derivatives
 }
 };
 integrator.initIntegrate(equations, 0, new double[0]);
 double[] y = new double[1];
 double result = integrator.integrate(equations, 1, y);
 assertEquals(1, result, 0.01);
 }
}