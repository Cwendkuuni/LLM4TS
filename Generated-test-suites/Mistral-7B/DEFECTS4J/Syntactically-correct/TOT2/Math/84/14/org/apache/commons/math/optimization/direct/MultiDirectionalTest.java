package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.*;
import org.junit.Test;

public class MultiDirectionalTest {

 @Test
 public void testMultiDirectionalOptimizer() throws Exception {
 // Basic test
 MultiDirectional optimizer = new MultiDirectional();
 RealPointValuePair bestPoint = optimizer.optimize(new AckleyFunction(), new AmplitudeInitializer(5), GoalType.MINIMIZE);
 assertEquals(0.0, bestPoint.getValue(), 0.001);
 }

 // Edge cases and error scenarios
 @Test(expected = FunctionEvaluationException.class)
 public void testFunctionEvaluationException() throws Exception {
 MultiDirectional optimizer = new MultiDirectional();
 ObjectiveFunction function = new FunctionWithException();
 RealPointValuePair bestPoint = optimizer.optimize(function, new DummyInitializer(), GoalType.MINIMIZE);
 fail("Should have thrown FunctionEvaluationException");
 }

 @Test(expected = OptimizationException.class)
 public void testOptimizationException() throws Exception {
 MultiDirectional optimizer = new MultiDirectional();
 ObjectiveFunction function = new FunctionWithMaxEvaluations();
 RealPointValuePair bestPoint = optimizer.optimize(function, new DummyInitializer(), GoalType.MINIMIZE);
 fail("Should have thrown OptimizationException");
 }

 // Testing the reflection step
 @Test
 public void testReflectionStep() throws Exception {
 MultiDirectional optimizer = new MultiDirectional();
 ObjectiveFunction function = new SphereFunction(3);
 RealPointValuePair initialPoint = new PointValuePair(new DenseVector(3, 5.0));
 RealPointValuePair expectedBestPoint = new PointValuePair(new DenseVector(3, 1.0));
 RealPointValuePair[] simplex = {initialPoint};
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return o1.getValue().compareTo(o2.getValue());
 }
 };
 PointValuePair bestPoint = optimizer.iterateSimplex(comparator, simplex);
 assertEquals(expectedBestPoint.getValue(), bestPoint.getValue(), 0.001);
 }

 // Testing the reflection and expansion steps
 @Test
 public void testReflectionAndExpansionSteps() throws Exception {
 MultiDirectional optimizer = new MultiDirectional();
 ObjectiveFunction function = new SphereFunction(3);
 RealPointValuePair initialPoint = new PointValuePair(new DenseVector(3, 10.0));
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return o1.getValue().compareTo(o2.getValue());
 }
 };
 PointValuePair bestPoint = optimizer.iterateSimplex(comparator, new RealPointValuePair[] {initialPoint});

 RealPointValuePair[] simplexBeforeReflection = simplex;
 PointValuePair reflectedPoint = optimizer.evaluateNewSimplex(simplexBeforeReflection, 1.0, comparator);
 assertEquals(bestPoint.getValue(), reflectedPoint.getValue(), 0.001);

 RealPointValuePair[] simplexBeforeExpansion = simplexBeforeReflection;
 PointValuePair expandedPoint = optimizer.evaluateNewSimplex(simplexBeforeExpansion, 2.0, comparator);
 assertTrue(reflectedPoint.getValue() < expandedPoint.getValue());
 }

 // Testing the contraction step
 @Test
 public void testContractionStep() throws Exception {
 MultiDirectional optimizer = new MultiDirectional();
 ObjectiveFunction function = new SphereFunction(3);
 RealPointValuePair initialPoint = new PointValuePair(new DenseVector(3, 8.0));
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return o1.getValue().compareTo(o2.getValue());
 }
 };
 PointValuePair bestPoint = optimizer.iterateSimplex(comparator, new RealPointValuePair[] {initialPoint});

 RealPointValuePair[] simplexBeforeContraction = simplex;
 PointValuePair contractedPoint = optimizer.evaluateNewSimplex(simplexBeforeContraction, 0.5, comparator);
 assertTrue(bestPoint.getValue() < contractedPoint.getValue());
 }

}