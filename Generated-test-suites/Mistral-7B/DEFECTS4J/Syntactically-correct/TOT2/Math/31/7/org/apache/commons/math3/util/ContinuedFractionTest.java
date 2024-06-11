package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.IllegalArgumentException;
import org.apache.commons.math3.special.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class ContinuedFractionTest {
 private ContinuedFraction continuedFraction;

 @Before
 public void setUp() {
 continuedFraction = new MyContinuedFraction();
 }

 // Test cases for method getA(int n, double x)
 @Test
 public void testGetA() {
 double x = 0.1;
 int n = 1;
 double expected = 1.0;
 double actual = continuedFraction.getA(n, x);
 assertEquals(expected, actual, DEFAULT_EPSILON);
 }

 // Test case for method getB(int n, double x)
 @Test
 public void testGetB() {
 double x = 0.1;
 int n = 1;
 double expected = 1.0;
 double actual = continuedFraction.getB(n, x);
 assertEquals(expected, actual, DEFAULT_EPSILON);
 }

 // Test case for method evaluate(double x) with normal input
 @Test
 public void testEvaluateNormalInput() {
 double x = 0.1;
 double expected = 1.7615025861; // Approximate value of sqrt(2)
 double actual = continuedFraction.evaluate(x);
 assertEquals(expected, actual, DEFAULT_EPSILON);
 }

 // Test case for method evaluate(double x, double epsilon) with normal input
 @Test
 public void testEvaluateEpsilon() {
 double x = 0.1;
 double epsilon = 1e-6;
 double expected = 1.7615025861; // Approximate value of sqrt(2)
 double actual = continuedFraction.evaluate(x, epsilon);
 assertEquals(expected, actual, epsilon);
 }

 // Test case for method evaluate(double x, int maxIterations) with normal input
 @Test
 public void testEvaluateMaxIterations() {
 double x = 0.1;
 int maxIterations = 10;
 double expected = 1.7615025861; // Approximate value of sqrt(2)
 double actual = continuedFraction.evaluate(x, maxIterations);
 assertEquals(expected, actual, DEFAULT_EPSILON);
 }

 // Test case for method evaluate(double x, double epsilon, int maxIterations) with normal input
 @Test
 public void testEvaluateEpsilonAndMaxIterations() {
 double x = 0.1;
 double epsilon = 1e-6;
 int maxIterations = 10;
 double expected = 1.7615025861; // Approximate value of sqrt(2)
 double actual = continuedFraction.evaluate(x, epsilon, maxIterations);
 assertEquals(expected, actual, epsilon);
 }

 // Test case for edge case: evaluate(double x) with negative input
 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateNegativeInput() {
 double x = -1.0;
 continuedFraction.evaluate(x);
 }

 // Test case for edge case: evaluate(double x) with negative epsilon
 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateNegativeEpsilon() {
 double x = 0.1;
 double epsilon = -1e-6;
 continuedFraction.evaluate(x, epsilon);
 }

 // Test case for edge case: evaluate(double x) with negative maxIterations
 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateNegativeMaxIterations() {
 double x = 0.1;
 int maxIterations = -1;
 continuedFraction.evaluate(x, maxIterations);
 }

 // Test case for edge case: evaluate(double x, double epsilon, int maxIterations) with negative epsilon and maxIterations
 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateNegativeEpsilonAndMaxIterations() {
 double x = 0.1;
 double epsilon = -1e-6;
 int maxIterations = -1;
 continuedFraction.evaluate(x, epsilon, maxIterations);
 }

 // Test case for edge case: evaluate(double x) with x = 1
 @Test
 public void testEvaluateEdgeCaseXEqualsOne() {
 double x = 1.0;
 double expected = Double.POSITIVE_INFINITY; // sqrt(2) diverges for x = 1
 double actual = continuedFraction.evaluate(x);
 assertEquals(expected, actual);
 }

 // Test case for edge case: evaluate(double x, int maxIterations) with maxIterations = 0
 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateMaxIterationsEdgeCaseZero() {
 double x = 0.1;
 int maxIterations = 0;
 continuedFraction.evaluate(x, maxIterations);
 }
}