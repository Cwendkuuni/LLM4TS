package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.junit.Test;

public class AbstractIntegratorTest {

 private static final double TOL = 1e-12;
 private static final double DELTA_T = 0.01;

 @Test
 public void testIntegration() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
   public int getDimension() {
    return 1;
   }

   public void computeDerivatives(double t, double[] y, double[] yDot) {
    yDot[0] = Math.sin(y[0]);
   }
  };

  AbstractIntegrator integrator = new AbstractIntegrator("RKF45") {
   @Override
   public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
    // Empty implementation, as we're only interested in the integration result
   }
  };

  integrator.addEventHandler(new StepEvent(0.5, 1.0), 0.01, 1e-12, 5);

  double[] initialValues = { 0.47124195 };
  double initialTime = 0.0;

  double result = integrator.integrate(equations, initialTime, initialValues, 1.0, new double[1]);

  assertEquals(1.0, result, TOL);
  assertArrayEquals(new double[] { 1.0 }, new double[] { result }, 0.0);

  assertEquals(1, integrator.getEvaluations());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatch() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
   public int getDimension() {
    return 2;
   }

   public void computeDerivatives(double t, double[] y, double[] yDot) {
    // not implemented
   }
  };

  AbstractIntegrator integrator = new AbstractIntegrator("RKF45") {
   // empty constructor and initialization
  };

  integrator.integrate(equations, 0.0, new double[1], 1.0, new double[2]);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
   public int getDimension() {
    return 1;
   }

   public void computeDerivatives(double t, double[] y, double[] yDot) {
    yDot[0] = Math.sin(y[0]);
   }
  };

  AbstractIntegrator integrator = new AbstractIntegrator("RKF45") {
   @Override
   public int getMaxEvaluations() {
    return 0;
   }
  };

  integrator.addEventHandler(new StepEvent(0.5, 1.0), 0.01, 1e-12, 5);

  double[] initialValues = { 0.47124195 };
  double initialTime = 0.0;

  try {
   integrator.integrate(equations, initialTime, initialValues, 1.0, new double[1]);
   fail("Expected MaxCountExceededException");
  } catch (MaxCountExceededException e) {
   // expected exception
  }
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
   public int getDimension() {
    return 1;
   }

   public void computeDerivatives(double t, double[] y, double[] yDot) {
    yDot[0] = 0.5 * Math.abs(y[0]);
   }
  };

  AbstractIntegrator integrator = new AbstractIntegrator("RKF45") {
   @Override
   public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
    // Empty implementation, as we're only interested in the exception
   }
  };

  integrator.setMaxEvaluations(0);

  double[] initialValues = { 1.0 };
  double initialTime = 0.0;

  try {
   integrator.integrate(equations, initialTime, initialValues, 1.0, new double[1]);
   fail("Expected NoBracketingException");
  } catch (NoBracketingException e) {
   // expected exception
  }
 }
}