package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolation;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 double[] c = new double[] { 0.0, 0.25, 0.25, 0.5 };
 double[][] a = new double[][] {
 new double[] { 0.0, 0.5, 0.5, 0.5 },
 new double[] { 0.5, 0.5, 0.0, 0.5 },
 new double[] { 0.5, 0.0, 0.5, 0.5 },
 new double[] { 0.5, 0.5, 0.5, 0.0 }
 };
 double[] b = new double[] { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0 };
 RungeKuttaStepInterpolator prototype = new RungeKuttaMethod(4, c, a, b);
 integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, prototype, 0.001, 0.01, 1.0E-9, 1.0E-6) {
 @Override
 protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
 return 0.0;
 }
 };
 equations = ODETestUtils.createTestEquations(2, 1.0);
 }

 @Test
 public void testGetOrder() throws Exception {
 assertEquals(4, integrator.getOrder());
 }

 @Test
 public void testGetSafety() throws Exception {
 assertEquals(0.9, integrator.getSafety(), 1.0E-6);
 }

 @Test
 public void testSetSafety() throws Exception {
 integrator.setSafety(0.5);
 assertEquals(0.5, integrator.getSafety(), 1.0E-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSafetyNegative() throws Exception {
 integrator.setSafety(-1.0);
 }

 @Test
 public void testIntegrate() throws Exception {
 double t = 1.0;
 double[] y0 = new double[] { 1.0, 1.0 };
 double[] y1 = new double[] { 1.1, 1.1 };
 double[] y2 = y1.clone();
 integrator.integrate(equations, t);
 assertEquals(y1[0], equations.getCompleteState()[0], Precision.SAFE_DOUBLE_EPSILON);
 assertEquals(y1[1], equations.getCompleteState()[1], Precision.SAFE_DOUBLE_EPSILON);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateTooSmallStep() throws Exception {
 integrator.setStepSize(0.01);
 integrator.integrate(equations, 1.0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateDimensionMismatch() throws Exception {
 equations = ODETestUtils.createTestEquations(1, 1.0);
 integrator.integrate(equations, 1.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateMaxCountExceeded() throws Exception {
 integrator.setMaxSteps(1);
 integrator.integrate(equations, 2.0);
 }

 @Test
 public void testIntegrateNoBracketing() throws Exception {
 equations = ODETestUtils.createTestEquations(2, 1.0);
 equations.setTime(1.5);
 integrator.integrate(equations, 1.5);
 }

 @Test
 public void testInitIntegration() throws Exception {
 integrator.initIntegration(1.0, y0, 2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateNullEquations() throws Exception {
 integrator.integrate(null, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateNullTime() throws Exception {
 integrator.integrate(equations, null);
 }

 @Test
 public void testIsLastStep() throws Exception {
 integrator.isLastStep = true;
 integrator.integrate(equations, 1.0);
 assertEquals(true, integrator.isLastStep);
 }

 @Test
 public void testResetInternalState() throws Exception {
 integrator.resetInternalState();
 integrator.isLastStep = false;
 integrator.integrate(equations, 1.0);
 }

 @Test
 public void testAcceptStep() throws Exception {
 StepInterpolation interpolator = new RungeKuttaStepInterpolator();
 interpolator.setOrder(4);
 double h = integrator.acceptStep(interpolator, y1, y2, 1.2);
 assertEquals(0.1, h, Precision.SAFE_DOUBLE_EPSILON);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAcceptStepNullInterpolator() throws Exception {
 integrator.acceptStep(null, y1, y2, 1.2);
 }

 @Test
 public void testAcceptStepNullY2() throws Exception {
 integrator.acceptStep(interpolator, null, y2, 1.2);
 }

 @Test
 public void testAcceptStepNullYTmp() throws Exception {
 integrator.acceptStep(interpolator, y1, null, 1.2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAcceptStepNullT() throws Exception {
 integrator.acceptStep(interpolator, y1, y2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterStepNullStepSize() throws Exception {
 integrator.filterStep(null, true, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterStepNegativeStepSize() throws Exception {
 integrator.filterStep(-1.0, true, false);
 }

 @Test
 public void testFilterStep() throws Exception {
 double h = integrator.filterStep(0.2, true, true);
 assertEquals(0.15, h, Precision.SAFE_DOUBLE_EPSILON);
 }

}