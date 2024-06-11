package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABS_TOL = 1e-9;
 private static final double REL_TOL = 1e-6;

 @Test
 public void testRk4Integrator() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  double[] y0 = { 1.0, 2.0 };
  double tEnd = 5.0;
  ExpandableStatefulODE equations = new AbstractODEFunction() {
   public int getDimension() {
    return y0.length;
   }

   public int getOrder() {
    return 4;
   }

   public void computeDerivatives(double time, double[] state, double[] derivatives) {
    derivatives[0] = state[1];
    derivatives[1] = state[0];
   }
  }.createIntegrator();

  EmbeddedRungeKuttaIntegrator rk4 = new RK4Integrator();
  rk4.setStepSize(0.1);
  rk4.integrate(equations, y0, tEnd);

  double[] expected = { 3.989376953125, 3.988340692383 };
  assertArrayEquals("RK4 integrator did not produce expected result", expected, equations.getCompleteState(), ABS_TOL, REL_TOL);
 }

 @Test
 public void testRk4IntegratorWithAbsoluteTolerance() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  double[] y0 = { 1.0, 2.0 };
  double tEnd = 5.0;
  ExpandableStatefulODE equations = new AbstractODEFunction() {
   public int getDimension() {
    return y0.length;
   }

   public int getOrder() {
    return 4;
   }

   public void computeDerivatives(double time, double[] state, double[] derivatives) {
    derivatives[0] = state[1];
    derivatives[1] = state[0];
   }
  }.createIntegrator();

  EmbeddedRungeKuttaIntegrator rk4 = new RK4Integrator(null, ABS_TOL);
  rk4.setStepSize(0.1);
  rk4.integrate(equations, y0, tEnd);

  double[] expected = { 3.989376953125, 3.988340692383 };
  assertArrayEquals("RK4 integrator with absolute tolerance did not produce expected result", expected, equations.getCompleteState(), ABS_TOL, REL_TOL);
 }

 @Test
 public void testRk4IntegratorWithRelativeTolerance() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  double[] y0 = { 1.0, 2.0 };
  double tEnd = 5.0;
  ExpandableStatefulODE equations = new AbstractODEFunction() {
   public int getDimension() {
    return y0.length;
   }

   public int getOrder() {
    return 4;
   }

   public void computeDerivatives(double time, double[] state, double[] derivatives) {
    derivatives[0] = state[1];
    derivatives[1] = state[0];
   }
  }.createIntegrator();

  EmbeddedRungeKuttaIntegrator rk4 = new RK4Integrator(null, null, REL_TOL);
  rk4.setStepSize(0.1);
  rk4.integrate(equations, y0, tEnd);

  double[] expected = { 3.989376953125, 3.988340692383 };
  assertArrayEquals("RK4 integrator with relative tolerance did not produce expected result", expected, equations.getCompleteState(), ABS_TOL, REL_TOL);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testRk4IntegratorStepSizeTooSmall() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  double[] y0 = { 1.0, 2.0 };
  double tEnd = 5.0;
  ExpandableStatefulODE equations = new AbstractODEFunction() {
   public int getDimension() {
    return y0.length;
   }

   public int getOrder() {
    return 4;
   }

   public void computeDerivatives(double time, double[] state, double[] derivatives) {
    derivatives[0] = state[1];
    derivatives[1] = state[0];
   }
  }.createIntegrator();

  EmbeddedRungeKuttaIntegrator rk4 = new RK4Integrator();
  rk4.setStepSize(0.001);
  try {
   rk4.integrate(equations, y0, tEnd);
   fail("Expected NumberIsTooSmallException when step size is too small.");
  } catch (NumberIsTooSmallException e) {
   // Expected
  }
 }
}