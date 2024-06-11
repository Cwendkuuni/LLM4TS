package org.apache.commons.math3.optim.univariate.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.optim.initializers.*;
import org.apache.commons.math3.optim.point.*;
import org.apache.commons.math3.optim.maximizer.*;
import org.apache.commons.math3.optim.minimizer.*;

public class BrentOptimizerTest {

 private static final double EPS = 1e-9;
 private static final double ACC = 1e-6;

 @Test
 public void testConstructor1() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructor1WithSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructor1WithNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0, -1.0);
 }

 @Test
 public void testConstructor2() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0);
 assertNotNull(optimizer);
 }

 @Test
 public void testDoOptimize_minimize() throws OptimizeException {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 UnivariatePointValuePair solution = optimizer.optimize(f, 0.0, 3.0, null);
 assertEquals(1.0, solution.getPoint(), ACC);
 assertEquals(0.0, solution.getValue(), EPS);
 }

 @Test
 public void testDoOptimize_minimize_convergenceChecker() throws OptimizeException {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 ConvergenceChecker<UnivariatePointValuePair> checker = new FailedConvergenceChecker(10);
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, checker);
 UnivariatePointValuePair solution = optimizer.optimize(f, 0.0, 3.0, null);
 assertEquals(1.0, solution.getPoint(), ACC);
 assertEquals(0.0, solution.getValue(), EPS);
 assertTrue(checker.isConverged());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_nullFunction() throws OptimizeException {
 Function f = null;
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 optimizer.optimize(f, 0.0, 3.0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_nullLowerBound() {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 optimizer.optimize(f, null, 3.0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_nullUpperBound() {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 optimizer.optimize(f, 0.0, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_nullInitialGuess() {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 optimizer.optimize(f, null, 3.0, null);
 }

 @Test(expected = OptimizeException.class)
 public void testDoOptimize_nonConvergence() throws OptimizeException {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 try {
 optimizer.optimize(f, 0.0, 1.0, null);
 fail("Exception not thrown");
 } catch (OptimizeException ex) {
 // expected
 }
 }

 @Test(expected = OptimizeException.class)
 public void testDoOptimize_minimize_wrongBrackets() throws OptimizeException {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0) * Math.sin(x);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 try {
 optimizer.optimize(f, 0.0, 3.0, null);
 fail("Exception not thrown");
 } catch (OptimizeException ex) {
 // expected
 }
 }

 @Test
 public void testDoOptimize_maximize() throws OptimizeException {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 UnivariatePointValuePair solution = optimizer.optimize(new NegativeFunction(f), 0.0, 3.0, null);
 assertEquals(3.0, solution.getPoint(), ACC);
 assertEquals(0.0, solution.getValue(), EPS);
 }

 @Test
 public void testDoOptimizer_minimize_wrongGoalType() throws OptimizeException {
 Function f = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
 try {
 optimizer.optimize(f, 0.0, 3.0, GoalType.MAXIMIZE);
 fail("Exception not thrown");
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }
}