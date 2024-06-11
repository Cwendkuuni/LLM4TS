package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleVector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.ForglassLagrange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private ObjectiveFunction function;
 private double[] xLower;
 private double[] xUpper;

 @Before
 public void setUp() {
 function = new ForglassLagrange();
 optimizer = new MultiDirectional();
 xLower = new double[]{-10.0, -10.0};
 xUpper = new double[]{10.0, 10.0};
 }

 @After
 public void tearDown() {
 optimizer = null;
 function = null;
 }

 @Test
 public void testMultiDirectionalOptimizerDefaultConstructor() {
 optimizer = new MultiDirectional();
 }

 @Test
 public void testMultiDirectionalOptimizerWithCustomKhiAndGamma() {
 optimizer = new MultiDirectional(3.0, 0.6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiDirectionalOptimizerWithKhiLessThanOne() {
 optimizer = new MultiDirectional(0.5, 0.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiDirectionalOptimizerWithGammaLessThanZero() {
 optimizer = new MultiDirectional(2.0, -0.5);
 }

 @Test
 public void testIterateSimplexWithSimpleBounds() throws Exception {
 final SimpleBounds bounds = new SimpleBounds(xLower, xUpper);
 optimizer.setMaxEvaluations(10);
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.optimize(function, bounds);
 PointValuePair solution = optimizer.getSolution();
 SimpleVector x = solution.getPoint();
 assertEquals(Precision.safeEpsilon(0.0), x.getEntry(0), 0.01);
 assertEquals(Precision.safeEpsilon(0.0), x.getEntry(1), 0.01);
 }

 @Test
 public void testIterateSimplexWithCustomInitialGuess() throws Exception {
 final double xInitialGuess1 = -5.0;
 final double xInitialGuess2 = 5.0;
 optimizer.setMaxEvaluations(10);
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.setInitialGuess(new InitialGuess() {
 @Override
 public PointValuePair getInitialPoint() {
 return new RealPointValuePair(new double[]{xInitialGuess1, xInitialGuess2}, Double.NaN, false);
 }
 });
 optimizer.optimize(function);
 PointValuePair solution = optimizer.getSolution();
 SimpleVector x = solution.getPoint();
 assertEquals(Precision.safeEpsilon(0.0), x.getEntry(0), 0.01);
 assertEquals(Precision.safeEpsilon(0.0), x.getEntry(1), 0.01);
 }

 @Test
 public void testIterateSimplexWithCustomMaxEval() throws Exception {
 optimizer.setMaxEvaluations(5);
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.optimize(function);
 PointValuePair solution = optimizer.getSolution();
 SimpleVector x = solution.getPoint();
 assertEquals(Precision.safeEpsilon(0.0), x.getEntry(0), 0.01);
 assertEquals(Precision.safeEpsilon(0.0), x.getEntry(1), 0.01);
 }
}