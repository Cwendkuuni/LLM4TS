package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;
import java.util.function.Supplier;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.ConvergenceCheckerFactory;
import org.apache.commons.math3.optimization.optimizers.AbstractMultivariateOptimizer;
import org.apache.commons.math3.optimization.optimizers.SimplexOptimizer;
import org.apache.commons.math3.optimization.objects.ObjectValue;
import org.junit.Test;

public class BaseOptimizerTest {
 private static final Supplier<BaseOptimizer<Object>> optimizerSupplier = () -> new SimplexOptimizer<>();

 @Test
 public void testGetMaxEvaluations() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 int maxEvaluations = optimizer.getMaxEvaluations();
 int expectedMaxEvaluations = 100;
 assertEquals(expectedMaxEvaluations, maxEvaluations);
 }

 @Test
 public void testGetEvaluations() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 optimizer.optimize(new ObjectiveFunction() {
 @Override
 public ObjectValue evaluate(Object point) {
 return new ObjectValue(1.0, null); // Placeholder for a valid ObjectValue
 }
 });
 int evaluations = optimizer.getEvaluations();
 int expectedEvaluations = 5;
 assertEquals(expectedEvaluations, evaluations);
 }

 @Test
 public void testGetConvergenceChecker() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
 assertNotNull(convergenceChecker);
 ConvergenceCheckerFactory<?> factory = (ConvergenceCheckerFactory<?>) convergenceChecker;
 ConvergenceChecker<Object> newChecker = factory.createChecker();
 assertNotNull(newChecker);
 }

 @Test
 public void testGetMaxEvaluationsWithNegativeValue() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 int maxEvaluations = optimizer.getMaxEvaluations();
 int expectedMaxEvaluations = -1;
 assertNotEquals(expectedMaxEvaluations, maxEvaluations);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMaxEvaluationsWithNullValue() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 int maxEvaluations = optimizer.getMaxEvaluations();
 int expectedMaxEvaluations = null;
 }

 @Test
 public void testGetMaxEvaluationsWithLargeValue() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 int maxEvaluations = optimizer.getMaxEvaluations();
 long expectedMaxEvaluations = Long.MAX_VALUE;
 assertNotEquals(expectedMaxEvaluations, maxEvaluations);
 }

 @Test
 public void testGetEvaluationsWithNegativeValue() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 optimizer.optimize(new ObjectiveFunction() {
 @Override
 public ObjectValue evaluate(Object point) {
 return new ObjectValue(1.0, null); // Placeholder for a valid ObjectValue
 }
 });
 int evaluations = optimizer.getEvaluations();
 int expectedEvaluations = -1;
 assertNotEquals(expectedEvaluations, evaluations);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetEvaluationsWithNullValue() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 int evaluations = optimizer.getEvaluations();
 int expectedEvaluations = null;
 }

 @Test
 public void testGetConvergenceCheckerWithNullObject() {
 BaseOptimizer<Object> optimizer = optimizerSupplier.get();
 ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
 assertNotNull(convergenceChecker);
 ConvergenceChecker<String> newChecker = (ConvergenceChecker<String>) convergenceChecker; // Compile error
 }
}