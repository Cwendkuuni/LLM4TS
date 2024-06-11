package org.apache.commons.math3.ode;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 AbstractIntegrator integrator = null;
 FirstOrderDifferentialEquations equations = null;
 double[] y0 = null;
 double t0 = 0.0;
 double t1 = 1.0;
 double[] y1 = new double[1];
 double[] yDot1 = new double[1];

 @Before
 public void setUp() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Your implementation here
 }
 };
 equations = new MyFirstOrderDifferentialEquations();
 y0 = new double[]{0.0};
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(InterpolatedState interpolator, boolean isLastStep) {
 // Your implementation here
 }
 });
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(Interpolator interpolator, boolean isLastStep) {
 // Your implementation here
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
 integrator.addEventHandler(new EventHandler() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Your implementation here
 }

 @Override
 public void stepAccepted(double eventTime, double[] eventYPrimary) {
 // Your implementation here
 }

 @Override
 public boolean evaluateStep(Interpolator interpolator) {
 // Your implementation here
 return true;
 }

 @Override
 public boolean stop() {
 // Your implementation here
 return false;
 }

 @Override
 public double getEventTime() {
 // Your implementation here
 return 0.0;
 }
 }, 0.1, 1e-5, 10);
 integrator.addEventHandler(new EventHandler() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Your implementation here
 }

 @Override
 public void stepAccepted(double eventTime, double[] eventYPrimary) {
 // Your implementation here
 }

 @Override
 public boolean evaluateStep(Interpolator interpolator) {
 // Your implementation here
 return true;
 }

 @Override
 public boolean stop() {
 // Your implementation here
 return false;
 }

 @Override
 public double getEventTime() {
 // Your implementation here
 return 1.0;
 }
 }, 0.1, 1e-5, 10, new BracketingNthOrderBrentSolver(1e-5, 5));
 assertEquals(2, integrator.getEventHandlers().size());
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setEquations(equations);
 integrator.initIntegration(t0, y0, t1);
 integrator.integrate(equations, t0, y0, t1, y1);
 assertEquals(t1, y1[0], 1e-5);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithIncorrectDimensions() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setEquations(new MyFirstOrderDifferentialEquations(2));
 integrator.initIntegration(t0, y0, t1);
 integrator.integrate(equations, t0, y0, t1, y1);
 fail("Should have thrown DimensionMismatchException");
 }

 @Test
 public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
 integrator.computeDerivatives(t0, y0, yDot1);
 assertEquals(yDot1[0], equations.getDerivative(y0[0], t0), 1e-5);
 }

 @Test
 public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 Interpolator interpolator = new StepInterpolator(y0, 0.0, 1.0, 0.1);
 double stepT = integrator.acceptStep(interpolator, y0, yDot1, t1);
 assertEquals(t1, stepT, 1e-5);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testAcceptStepWithMaxCountExceeded() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 integrator.setMaxEvaluations(1);
 Interpolator interpolator2 = new StepInterpolator(y0, 0.0, 1.0, 0.1);
 integrator.acceptStep(interpolator2, y0, yDot1, t1);
 fail("Should have thrown MaxCountExceededException");
 }

 @Test(expected = NoBracketingException.class)
 public void testAcceptStepWithNoBracketing() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 integrator.setMaxEvaluations(Integer.MAX_VALUE);
 integrator.setMinStepSize(Double.POSITIVE_INFINITY);
 integrator.setMaxStepSize(Double.NEGATIVE_INFINITY);
 Interpolator interpolator3 = new StepInterpolator(y0, 0.0, 1.0, 2.0);
 integrator.acceptStep(interpolator3, y0, yDot1, t1);
 fail("Should have thrown NoBracketingException");
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testAcceptStepWithNumberIsTooSmall() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 integrator.setMaxEvaluations(Integer.MAX_VALUE);
 integrator.setMinStepSize(Double.POSITIVE_INFINITY);
 integrator.setMaxStepSize(Double.POSITIVE_INFINITY);
 integrator.setStepSize(0.0);
 Interpolator interpolator4 = new StepInterpolator(y0, 0.0, 1.0, 0.1);
 integrator.acceptStep(interpolator4, y0, yDot1, t1);
 fail("Should have thrown NumberIsTooSmallException");
 }

 @Test
 public void testSanityChecks() throws NumberIsTooSmallException, DimensionMismatchException {
 integrator.sanityChecks(equations, t1);
 }
}

class MyFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double getDerivative(double y, double t) {
 return 0.0;
 }
}