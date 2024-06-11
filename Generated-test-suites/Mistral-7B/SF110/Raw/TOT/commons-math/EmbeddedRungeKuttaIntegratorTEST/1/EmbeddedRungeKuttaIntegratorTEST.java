package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABS_TOL = 1e-10;
 private static final double REL_TOL = 1e-6;

 @Test
 public void testOrder1Integrator() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod(
 "RK23",
 true,
 new double[] { 0.5 },
 new double[][] { { 0.25, 0.25 }, { 0.0 } },
 new double[] { 0.25, 0.0 },
 new RungeKuttaStepInterpolator() {
 @Override
 public void interpolate(double[] y, double[] dy, double[] yDot, double[] yDotDot, double t, int order) {
 // No-op, since RK23 doesn't require interpolation
 }
 },
 0.001,
 0.01,
 new double[] { ABS_TOL, REL_TOL },
 new double[] { ABS_TOL, REL_TOL }
 );

 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[0] * t;
 }
 };

 double[] y0 = { 1.0 };
 double tStart = 0.0;
 double tEnd = 2.0;

 integrator.integrate(equations, tStart);
 double y1[] = equations.getCompleteState();
 assertEquals("y1[0] should be close to y0[0] * tStart", y0[0] * tStart, y1[0], 1e-6);

 integrator.integrate(equations, tStart + tStart);
 double y2[] = equations.getCompleteState();
 assertEquals("y2[0] should be close to y1[0] * (1 + tStart)", y1[0] * (1 + tStart), y2[0], 1e-6);

 integrator.integrate(equations, tEnd);
 double y3[] = equations.getCompleteState();
 assertEquals("y3[0] should be close to y2[0] * exp(tEnd - tStart)", y2[0] * Math.exp(tEnd - tStart), y3[0], 1e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = 1.0;
 }
 };

 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod(
 "RK23",
 true,
 new double[] { 0.5 },
 new double[][] { { 0.25, 0.25 }, { 0.0 } },
 new double[] { 0.25, 0.0 },
 new RungeKuttaStepInterpolator() {
 @Override
 public void interpolate(double[] y, double[] dy, double[] yDot, double[] yDotDot, double t, int order) {
 // No-op, since RK23 doesn't require interpolation
 }
 },
 -0.001,
 0.01,
 new double[] { ABS_TOL, REL_TOL },
 new double[] { ABS_TOL, REL_TOL }
 );

 integrator.integrate(equations, 0.0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[0] * t;
 dy[1] = y[1];
 }
 };

 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod(
 "RK23",
 true,
 new double[] { 0.5 },
 new double[][] { { 0.25, 0.25 }, { 0.0 } },
 new double[] { 0.25, 0.0 },
 new RungeKuttaStepInterpolator() {
 @Override
 public void interpolate(double[] y, double[] dy, double[] yDot, double[] yDotDot, double t, int order) {
 // No-op, since RK23 doesn't require interpolation
 }
 },
 0.001,
 0.01,
 new double[] { ABS_TOL, REL_TOL },
 new double[] { ABS_TOL, REL_TOL }
 );

 integrator.integrate(equations, 0.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = 1.0;
 }
 };

 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod(
 "RK23",
 true,
 new double[] { 0.5 },
 new double[][] { { 0.25, 0.25 }, { 0.0 } },
 new double[] { 0.25, 0.0 },
 new RungeKuttaStepInterpolator() {
 @Override
 public void interpolate(double[] y, double[] dy, double[] yDot, double[] yDotDot, double t, int order) {
 // No-op, since RK23 doesn't require interpolation
 }
 },
 0.001,
 0.001,
 new double[] { ABS_TOL, REL_TOL },
 new double[] { ABS_TOL, REL_TOL }
 );

 StepHandler stepHandler = new StepHandler() {
 @Override
 public void handleStep(int step, double[] y, double[] dy, double t, double[][] yDot, double[][] yDotDot, double[][] yDotDotDot) {
 fail("Should not reach this point");
 }

 @Override
 public void handleStepFailure(int step, Throwable exc) {
 fail("Integration should not throw an exception: " + exc.getMessage());
 }
 };

 integrator.integrate(equations, 0.0, 1.0, stepHandler);
 }

 @Test
 public void testNoBracketingException() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[0] * t;
 }
 };

 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaMethod(
 "RK23",
 true,
 new double[] { 0.5 },
 new double[][] { { 0.25, 0.25 }, { 0.0 } },
 new double[] { 0.25, 0.0 },
 new RungeKuttaStepInterpolator() {
 @Override
 public void interpolate(double[] y, double[] dy, double[] yDot, double[] yDotDot, double t, int order) {
 // No-op, since RK23 doesn't require interpolation
 }
 },
 0.001,
 0.01,
 new double[] { ABS_TOL, REL_TOL },
 new double[] { ABS_TOL, REL_TOL }
 );

 double[] y0 = { 1.0 };
 double tStart = 0.0;
 double tEnd = 2.0;
 double[] y1 = integrator.initialize(equations, tStart, tEnd, y0);
 assertTrue("y1 should not be null", y1 != null);
 try {
 integrator.integrate(equations, tStart, tEnd, y1);
 fail("Integration should throw a NoBracketingException");
 } catch (NoBracketingException e) {
 // Expected
 }
 }
}