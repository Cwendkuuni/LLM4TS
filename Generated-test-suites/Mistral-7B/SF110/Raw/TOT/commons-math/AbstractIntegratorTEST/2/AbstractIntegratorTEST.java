package org.apache.commons.math3.ode.tests;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;

 @Before
 public void setUp() {
 integrator = new MyAbstractIntegrator("MyIntegrator");
 equations = new MyDifferentialEquations();
 }

 @Test
 public void testIntegration() throws Exception {
 final double[] initialCondition = {1.0, 2.0};
 final double timeStart = 0.0;
 final double timeEnd = 10.0;
 final double[] solution = {3.0, 4.0};

 integrator.initIntegration(timeStart, initialCondition, timeEnd);
 integrator.setExpandable(equations);
 integrator.integrate(equations, timeStart, initialCondition, timeEnd, solution);

 assertArrayEquals("Solution is not correct", solution, new double[] {3.0, 4.0});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatch() throws Exception {
 final double[] initialCondition = {1.0};
 final FirstOrderDifferentialEquations equations = new MyDifferentialEquations();
 integrator.initIntegration(0.0, initialCondition, 1.0);
 integrator.setExpandable(equations);
 integrator.integrate(equations, 0.0, initialCondition, 1.0, new double[2]);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmall() throws Exception {
 integrator = new MyAbstractIntegrator("MyIntegrator");
 integrator.setStepSize(1.0e-10); // too small step size
 final double[] initialCondition = {1.0, 2.0};
 final double timeStart = 0.0;
 final double timeEnd = 1.0;
 try {
 integrator.initIntegration(timeStart, initialCondition, timeEnd);
 integrator.setExpandable(new MyDifferentialEquations());
 integrator.integrate(new MyDifferentialEquations(), timeStart, initialCondition, timeEnd, new double[2]);
 fail("Should have thrown NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxEvaluations() throws Exception {
 integrator = new MyAbstractIntegrator("MyIntegrator") {
 @Override
 public int getMaxEvaluations() {
 return 1; // Maximum evaluations limit reached
 }
 };
 final double[] initialCondition = {1.0, 2.0};
 final double timeStart = 0.0;
 final double timeEnd = 1.0;
 try {
 integrator.initIntegration(timeStart, initialCondition, timeEnd);
 integrator.setExpandable(new MyDifferentialEquations());
 integrator.integrate(new MyDifferentialEquations(), timeStart, initialCondition, timeEnd, new double[2]);
 fail("Should have thrown MaxCountExceededException");
 } catch (MaxCountExceededException e) {
 // Expected
 }
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketing() throws Exception {
 integrator = new MyAbstractIntegrator("MyIntegrator") {
 @Override
 protected void sanityChecks(final ExpandableStatefulODE equations, final double t) throws NumberIsTooSmallException, DimensionMismatchException {
 // Disable sanity checks
 }
 };
 final double[] initialCondition = {1.0, 2.0};
 final double timeStart = 0.0;
 final double timeEnd = 1.0;
 try {
 integrator.initIntegration(timeStart, initialCondition, timeEnd);
 integrator.setExpandable(new MyDifferentialEquations());
 integrator.integrate(new MyDifferentialEquations(), timeStart, initialCondition, timeEnd, new double[2]);
 fail("Should have thrown NoBracketingException");
 } catch (NoBracketingException e) {
 // Expected
 }
 }
}

class MyAbstractIntegrator extends AbstractIntegrator {
 // Empty implementation
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
 // Implementation of the differential equations
}