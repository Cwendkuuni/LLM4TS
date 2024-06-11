package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ACCURACY = 1e-12;

 @SuppressWarnings("rawtypes")
 @org.junit.Test
 public void testIntegrator() throws Exception {
  double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
  double[] b = {0.5, 0.5, 0.5, 0.5};
  double[] c = {0.0, 0.5, 0.5, 1.0};
  RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();

  EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKutta78Method", true, c, a, b, prototype, 1.0e-12, 1.0e12, null, null) {
   @Override
   protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
    double error = 0.0;
    for (int i = 0; i < p0[0].length; i++) {
     error += Precision.pow(Precision.abs(p1[i] - p2[i]), 2);
    }
    return Math.sqrt(error);
   }
  };

  StepInterpolator interpolator = integrator.prototype;

  // Test integration with a simple ODE
  ExpandableStatefulODE equations = new DummyODEEquations();
  double[] initialState = new double[] {1.0, 1.0};
  double startTime = 0.0;
  double endTime = 10.0;

  integrator.integrate(equations, startTime);
  assertEquals(10.0, equations.getTime(), ACCURACY);

  // Check final state
  double[] finalState = equations.getCompleteState();
  assertArrayEquals(new double[] {3.331536332136623E-01, 1.105319368421052E+00}, finalState, ACCURACY);

  // Test integration with a step size change
  integrator.setMinStep(0.1);
  integrator.integrate(equations, startTime);
  assertEquals(1.0, equations.getTime(), ACCURACY);
  integrator.integrate(equations, endTime);

  // Check final state after step size change
  finalState = equations.getCompleteState();
  assertArrayEquals(new double[] {3.3335522447923363E-01, 1.1055525109041885E+00}, finalState, ACCURACY);

  // Test error estimation
  integrator.setMaxStep(1.0);
  integrator.integrate(equations, startTime);
  integrator.integrate(equations, endTime);
  double estimatedError = integrator.getLastError();
  assertTrue(estimatedError < 1.0e-9);
 }

 @SuppressWarnings("rawtypes")
 @org.junit.Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() throws Exception {
  double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
  double[] b = {0.5, 0.5, 0.5, 0.5};
  double[] c = {0.0, 0.5, 0.5, 1.0};
  RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();

  EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKutta78Method", true, c, a, b, prototype, 1.0e-35, 1.0e12, null, null);

  ExpandableStatefulODE equations = new DummyODEEquations();
  double[] initialState = new double[] {1.0, 1.0};
  double startTime = 0.0;
  double endTime = 10.0;

  integrator.integrate(equations, startTime);
 }

 @SuppressWarnings("rawtypes")
 @org.junit.Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() throws Exception {
  double[][] a = {{0.0, 0.0}, {0.0, 0.5}};
  double[] b = {0.5};
  double[] c = {0.0, 0.5};
  RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();

  EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKutta78Method", true, c, a, b, prototype, 1.0e-12, 1.0e12, null, null);

  ExpandableStatefulODE equations = new ExpandableStatefulODE() {
   @Override
   public int getPrimaryDimension() {
    return 2;
   }

   @Override
   public int getSecondaryDimension() {
    return 1;
   }

   @Override
   public void computeDerivatives(double t, double[] y, double[] yPrime) {
    yPrime[0] = y[1];
   }
  };
  double[] initialState = new double[] {1.0, 1.0};
  double startTime = 0.0;
  double endTime = 10.0;

  integrator.integrate(equations, startTime);
 }

 @SuppressWarnings("rawtypes")
 @org.junit.Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() throws Exception {
  double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
  double[] b = {0.5, 0.5, 0.5, 0.5};
  double[] c = {0.0, 0.5, 0.5, 1.0};
  RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();

  EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKutta78Method", true, c, a, b, prototype, 1.0e-12, 1.0e12, null, null);

  ExpandableStatefulODE equations = new DummyODEEquations();
  double[] initialState = new double[] {1.0, 1.0};
  double startTime = 0.0;
  double endTime = 1.0e10;

  integrator.integrate(equations, startTime);
 }

 @SuppressWarnings("rawtypes")
 @org.junit.Test(expected = NoBracketingException.class)
 public void testNoBracketingException() throws Exception {
  double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 0.0}, {0.5, 0.5, 0.0, 0.5}, {0.5, 0.0, 0.5, 0.5}};
  double[] b = {0.5, 0.5, 0.5, 0.5};
  double[] c = {0.0, 0.5, 0.5, 1.0};
  RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();

  EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKutta78Method", true, c, a, b, prototype, 1.0e-12, 1.0e12, null, null);

  ExpandableStatefulODE equations = new DummyODEEquations();
  double[] initialState = new double[] {1.0, 1.0};
  double startTime = 1.0;
  double endTime = 0.0;

  integrator.integrate(equations, startTime);
 }
}

class DummyODEEquations implements ExpandableStatefulODE {
 @Override
 public int getPrimaryDimension() {
  return 2;
 }

 @Override
 public int getSecondaryDimension() {
  return 0;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
  yPrime[0] = y[1];
  yPrime[1] = -y[0] + 0.5 * y[1];
 }

 @Override
 public void setTime(double t) {
 }

 @Override
 public void setCompleteState(double[] y) {
 }
}