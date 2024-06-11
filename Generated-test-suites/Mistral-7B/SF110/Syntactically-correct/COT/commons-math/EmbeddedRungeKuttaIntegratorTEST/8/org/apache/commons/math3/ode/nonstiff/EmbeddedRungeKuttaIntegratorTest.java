package org.junit.jupitertesting.embeddedrungekuttaintegrator;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private ExpandableStatefulODE equations;
 private EmbeddedRungeKuttaIntegrator integrator;
 private RungeKuttaStepInterpolator interpolator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 equations = mock(ExpandableStatefulODE.class);
 integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 interpolator = mock(RungeKuttaStepInterpolator.class);
 }

 @Test
 public void testGetOrder() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 assertEquals(2, integrator.getOrder());
 }

 @Test
 public void testGetSafety() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 assertEquals(0.9, integrator.getSafety(), 0.001);
 }

 @Test
 public void testIntegrate_givenInitialStateAndTime_shouldIntegrateCorrectly() {
 double[] y0 = new double[]{1.0, 2.0};
 double time = 3.0;
 integrator.integrate(equations, time);
 double[] yExpected = new double[]{3.7119294696784266, 5.692253979185123};
 assertEquals(yExpected, equations.getCompleteState(), 0.001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrate_givenInitialStateAndTimeBelowZero_shouldThrowNumberIsTooSmallException() {
 double[] y0 = new double[]{1.0, 2.0};
 double time = -1.0;
 integrator.integrate(equations, time);
 fail("NumberIsTooSmallException should be thrown");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrate_givenInitialStateOfDifferentSize_shouldThrowDimensionMismatchException() {
 double[] y0 = new double[]{1.0, 2.0, 3.0};
 integrator.integrate(equations, 1.0);
 fail("DimensionMismatchException should be thrown");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrate_givenMaxStepsExceeded_shouldThrowMaxCountExceededException() {
 doReturn(100).when(equations).getMaxStepCount();
 integrator.setMaxStep(1);
 integrator.integrate(equations, 1.0);
 fail("MaxCountExceededException should be thrown");
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrate_givenInitialStateAndTimeOutOfRange_shouldThrowNoBracketingException() {
 double[] y0 = new double[]{1.0, 2.0};
 double time = Double.POSITIVE_INFINITY;
 integrator.integrate(equations, time);
 fail("NoBracketingException should be thrown");
 }

 @Test
 public void testGetMinReduction() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 assertEquals(0.2, integrator.getMinReduction(), 0.001);
 }

 @Test
 public void testSetMinReduction() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 integrator.setMinReduction(0.5);
 assertEquals(0.5, integrator.getMinReduction(), 0.001);
 }

 @Test
 public void testGetMaxGrowth() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
 }

 @Test
 public void testSetMaxGrowth() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
 "MyEmbeddedRungeKuttaIntegrator",
 true,
 new double[]{1.0, 2.0},
 new double[][]{{0.33267048642787746, 0.8321856458578875},
 {0.6751216227120489, 1.266516135033621}},
 new double[]{1.0, 2.0},
 (RungeKuttaStepInterpolator)mock(RungeKuttaStepInterpolator.class),
 0.001,
 0.01,
 null,
 null);
 integrator.setMaxGrowth(5.0);
 assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
 }
}

 class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
 @Override
 protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
 // Implement your custom error estimation logic here
 double error = 0.01;
 return error;
 }
 }