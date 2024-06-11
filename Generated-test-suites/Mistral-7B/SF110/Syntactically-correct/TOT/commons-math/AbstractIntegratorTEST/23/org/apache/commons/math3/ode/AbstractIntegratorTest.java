package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.junit.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;

 @Before
 public void setUp() {
 integrator = new SteppedIntegrator("TestIntegrator");
 equations = new MyDifferentialEquations();
 }

 @Test
 public void testIntegration() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = {0.0, 0.1};
 double tEnd = 1.0;
 double[] y = new double[2];
 integrator.initIntegration(0.0, y0, tEnd);
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 assertEquals(tEnd, integrator.getCurrentStepStart(), 1e-10);
 Assert.assertArrayEquals(new double[] {1.061433462298096, 0.198335458531481}, y, 1e-8);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatch() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new SteppedIntegrator("TestIntegrator");
 FirstOrderDifferentialEquations equationsMismatch = new MyDifferentialEquationsWithMismatchingDimension();
 integrator.initIntegration(0.0, new double[3], 1.0);
 integrator.integrate(equationsMismatch, 0.0, new double[2], 1.0, new double[2]);
 fail("Should have thrown DimensionMismatchException");
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testThresholdReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new SteppedIntegrator("TestIntegrator");
 equations = new MyDifferentialEquationsWithSmallStepSize();
 integrator.setMaxEvaluations(1);
 integrator.initIntegration(0.0, new double[2], 0.001);
 integrator.integrate(equations, 0.0, new double[2], 0.001, new double[2]);
 assertTrue(integrator.getEvaluations() >= 1);
 }

 @Test
 public void testEventHandlers() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new DormandPrince54Integrator("TestIntegrator");
 integrator.addEventHandler(new MyStepEvent(), 0.1, 1e-6, 10);
 integrator.addEventHandler(new MyEvent(), 1.0, 1e-6, 10);
 equations = new MyDifferentialEquationsWithEvent();
 double[] y0 = {0.0, 0.1};
 double tEnd = 2.0;
 double[] y = new double[2];
 integrator.initIntegration(0.0, y0, tEnd);
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 assertEquals(2.0, integrator.getCurrentStepStart(), 1e-10);
 Assert.assertArrayEquals(new double[] {1.718281828459045, 2.718281828459045}, y, 1e-8);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new SteppedIntegrator("TestIntegrator");
 integrator.setMaxEvaluations(1);
 equations = new MyDifferentialEquationsWithMaxEvaluations();
 double[] y0 = {0.0, 0.1};
 double tEnd = 1.0;
 double[] y = new double[2];
 integrator.initIntegration(0.0, y0, tEnd);
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 fail("Should have thrown MaxCountExceededException");
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
 public int getDimension() {
 return 2;
 }

 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[1];
 yDot[1] = -y[0];
 }
}

class MyDifferentialEquationsWithMismatchingDimension implements FirstOrderDifferentialEquations {
 public int getDimension() {
 return 3;
 }

 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[1];
 yDot[1] = -y[0];
 yDot[2] = 0.0;
 }
}

class MyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {
 public int getDimension() {
 return 2;
 }

 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[1];
 yDot[1] = -y[0];
 }
}

class MyDifferentialEquationsWithSmallStepSize implements FirstOrderDifferentialEquations {
 public int getDimension() {
 return 2;
 }

 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = 1.0;
 yDot[1] = 1.0;
 }
}

class MyStepEvent extends StepDetector {
 public boolean test(IntervalStepInterpolator interpolator) {
 return interpolator.getGlobalCurrentTime() >= 0.1;
 }
}

class MyEvent extends AbstractEvent {
 public double evaluateStep(IntervalStepInterpolator interpolator) {
 return interpolator.getGlobalCurrentTime() - 1.0;
 }

 public void stepAccepted(double t, double[] y) {
 // Empty implementation
 }

 public void stepRejected(double t, double[] y) {
 // Empty implementation
 }

 public void reset(double t, double[] y) {
 // Empty implementation
 }
}