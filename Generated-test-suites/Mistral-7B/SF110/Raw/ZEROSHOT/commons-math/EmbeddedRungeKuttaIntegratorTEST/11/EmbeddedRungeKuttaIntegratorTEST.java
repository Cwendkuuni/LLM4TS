package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;

 @Before
 public void setUp() {
 // Create a dummy implementation of EmbeddedRungeKuttaIntegrator
 integrator = new TestEmbeddedRungeKuttaIntegrator(1, new boolean[0], new double[3][], new double[3][], new RungeKuttaStepInterpolator(1), 0.001, 0.01, 1.0e-10, 1.0e-10);
 // Create a dummy set of equations for testing
 equations = new DummyODE();
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 }

 @Test
 public void testGetOrder() {
 assertEquals(1, integrator.getOrder());
 }

 @Test
 public void testGetSafety() {
 assertEquals(0.9, integrator.getSafety(), 0.01);
 }

 @Test
 public void testSetSafety() {
 integrator.setSafety(0.8);
 assertEquals(0.8, integrator.getSafety(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSafetyNegative() {
 integrator.setSafety(-0.1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = new double[] {0.0, 0.0, 0.0};
 double t = 1.0;
 integrator.integrate(equations, t, y0);
 // Check the result
 double[] y1 = equations.getCompleteState();
 assertArrayEquals(y1, y0, 1.0e-10);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateSmallStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = new double[] {0.0, 0.0, 0.0};
 double t = 0.001;
 integrator.integrate(equations, t, y0);
 fail("Should throw NumberIsTooSmallException");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateMismatchedStateSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = new double[] {0.0, 0.0};
 integrator.integrate(equations, 1.0, y0);
 fail("Should throw DimensionMismatchException");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateMaxCountExceeded() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 equations.setMaxSteps(0);
 integrator.integrate(equations, 1.0, new double[] {0.0, 0.0, 0.0});
 fail("Should throw MaxCountExceededException");
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = new double[] {0.0, 0.0, 0.0};
 integrator.setMinStep(1.0);
 integrator.setMaxStep(0.0);
 integrator.integrate(equations, 1.0, y0);
 fail("Should throw NoBracketingException");
 }

 @Test
 public void testIntegrateStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = new double[] {0.0, 0.0, 0.0};
 double t = 1.0;
 integrator.integrate(equations, t, y0, 0.1);
 // Check the result
 double[] y1 = equations.getCompleteState();
 assertArrayEquals(y1, y0, 1.0e-10);
 }

 @Test
 public void testIntegrateWithScaledTolerance() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double[] y0 = new double[] {0.0, 0.0, 0.0};
 double t = 1.0;
 double[] vecAbsoluteTolerance = {1.0e-12, 1.0e-12, 1.0e-12};
 double[] vecRelativeTolerance = {1.0e-6, 1.0e-6, 1.0e-6};
 integrator = new TestEmbeddedRungeKuttaIntegrator(1, new boolean[0], new double[3][], new double[3][], new RungeKuttaStepInterpolator(1), 0.001, 0.01, vecAbsoluteTolerance, vecRelativeTolerance);
 integrator.integrate(equations, t, y0);
 // Check the result
 double[] y1 = equations.getCompleteState();
 assertArrayEquals(y1, y0, 1.0e-12);
 }
}

public class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

 public TestEmbeddedRungeKuttaIntegrator(int order, boolean[] fsal, double[][] c, double[][] a, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double[] vecRelativeTolerance) {
 super("TestEmbeddedRungeKuttaIntegrator", fsal, c, a, prototype, minStep, maxStep, scalAbsoluteTolerance, vecRelativeTolerance);
 }

 @Override
 public int getOrder() {
 return order;
 }

 @Override
 protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
 return 1.0;
 }
}

class DummyODE implements ExpandableStatefulODE {

 @Override
 public String getName() {
 return "DummyODE";
 }

 @Override
 public int getDimension() {
 return 3;
 }

 @Override
 public int getStateSize() {
 return 3;
 }

 @Override
 public int getOrder() {
 return 1;
 }

 @Override
 public void setTime(double time) {
 // Do nothing
 }

 @Override
 public double getTime() {
 return 0.0;
 }

 @Override
 public void setCompleteState(double[] state) {
 // Do nothing
 }

 @Override
 public double[] getCompleteState() {
 return new double[3];
 }

 @Override
 public void setPrimaryMapper(ODEStateAndDerivativeMapper mapper) {
 // Do nothing
 }

 @Override
 public void setSecondaryMappers(ODEStateAndDerivativeMapper[] secondaryMappers) {
 // Do nothing
 }

 @Override
 public void initialize(double t, double[] y, double[] yDot) {
 // Set initial state and derivatives
 y[0] = 0.0;
 y[1] = 0.0;
 y[2] = 0.0;
 yDot[0] = 1.0;
 yDot[1] = 2.0;
 yDot[2] = 3.0;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 // Set derivatives based on state
 yDot[0] = y[1];
 yDot[1] = y[2];
 yDot[2] = -y[0] - y[1] - y[2];
 }
}