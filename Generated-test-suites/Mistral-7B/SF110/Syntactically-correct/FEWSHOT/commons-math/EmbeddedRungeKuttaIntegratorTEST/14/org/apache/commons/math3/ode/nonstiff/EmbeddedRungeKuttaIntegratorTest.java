package org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator; // make sure to use the correct package name

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;
 private double initialTime;
 private double[] initialState;
 private double time;

 @Parameters
 public static Object[][] data() {
 return new Object[][] {
 // test case 1
 new Object[] { new RK2Method(), new double[] { 0.0, 0.0 }, new double[][] { { 0.0, 0.0 }, { 1.0, 1.0 } }, new double[] { 0.1, 0.1 }, 0.01, 0.01, 1.0, 0.9, 10.0, null, null },
 // test case 2 (with vecAbsoluteTolerance and vecRelativeTolerance)
 new Object[] { new RK4Method(), new double[] { 1.0, 1.0 }, new double[][] { { 0.25, 0.0, 0.25, 0.0 }, { 0.25, 0.25, 0.25, 0.25 }, { 0.5, 0.25, 0.25, 0.5 }, { 0.5, 0.5, 0.5, 0.5 } }, new double[] { 1.2, 1.2 }, 0.01, 0.01, 1.0, 0.9, 10.0, new double[] { 0.01, 0.01 }, new double[] { 0.001, 0.001 } }
 };
 }

 public EmbeddedRungeKuttaIntegratorTest(RungeKuttaMethod integratorType, double[] initialValues, double[][] coefficients, double initialAbsoluteTolerance, double initialRelativeTolerance, double minStep, double maxStep, double safety, double minReduction, double maxGrowth) {
 this.integrator = new EmbeddedRungeKuttaIntegrator(integratorType.getName(), false, initialValues, coefficients, null, new RungeKuttaStepInterpolator(integratorType), minStep, maxStep, initialAbsoluteTolerance, initialRelativeTolerance) {
 @Override
 protected double estimateError(double[][] y, double t, double[] ydot, double step) {
 return 0.0;
 }
 };
 this.equations = mock(ExpandableStatefulODE.class);
 when(equations.getCompleteState()).thenReturn(initialValues);
 when(equations.getTime()).thenReturn(initialTime);
 this.initialTime = initialTime;
 this.initialState = initialValues;
 this.time = time;
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.integrate(equations, time);
 assertEquals(initialState, equations.getCompleteState());
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setMinStep(0.0);
 integrator.integrate(equations, time);
 fail("NoBracketingException expected");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setMaxStep(0.0);
 integrator.integrate(equations, time);
 fail("MaxCountExceededException expected");
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setSafety(0.0);
 integrator.integrate(equations, time);
 fail("NumberIsTooSmallException expected");
 }

 @Test
 public void testGetOrder() {
 assertEquals(2, new RK2Method().getOrder());
 assertEquals(4, new RK4Method().getOrder());
 }

 @Test
 public void testGetSafety() {
 assertEquals(0.9, integrator.getSafety(), 0.001);
 }

 @Test
 public void testSetSafety() {
 integrator.setSafety(0.5);
 assertEquals(0.5, integrator.getSafety(), 0.001);
 }

 @Test
 public void testGetMinReduction() {
 assertEquals(0.2, integrator.getMinReduction());
 }

 @Test
 public void testSetMinReduction() {
 integrator.setMinReduction(0.3);
 assertEquals(0.3, integrator.getMinReduction());
 }

 @Test
 public void testGetMaxGrowth() {
 assertEquals(10.0, integrator.getMaxGrowth());
 }

 @Test
 public void testSetMaxGrowth() {
 integrator.setMaxGrowth(5.0);
 assertEquals(5.0, integrator.getMaxGrowth());
 }
}