package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.ObjectiveFunctionValuesPair;
import org.junit.Test;

public class BaseOptimizerTest {

   @Test
   public void testGetMaxEvaluations() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      when(optimizer.getMaxEvaluations()).thenReturn(100);
      int maxEvaluations = optimizer.getMaxEvaluations();
      assertEquals(100, maxEvaluations);
   }

   // edge case: method returns the default value (0) if not initialized
   @Test
   public void testGetMaxEvaluationsDefaultValue() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      int maxEvaluations = optimizer.getMaxEvaluations();
      assertEquals(0, maxEvaluations);
   }

   @Test
   public void testGetEvaluations() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      when(optimizer.getEvaluations()).thenReturn(5);
      int evaluations = optimizer.getEvaluations();
      assertEquals(5, evaluations);
   }

   // edge case: method returns 0 if not called optimize method
   @Test
   public void testGetEvaluationsNotCalled() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      int evaluations = optimizer.getEvaluations();
      assertEquals(0, evaluations);
   }

   // exception handling: method throws an IllegalStateException if optimize method was not called
   @Test(expected = IllegalStateException.class)
   public void testGetEvaluationsUninitialized() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      optimizer.getEvaluations();
   }

   @Test
   public void testGetConvergenceChecker() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      ConvergenceChecker<ObjectiveFunctionValuesPair> convergenceChecker = mock(ConvergenceChecker.class);
      when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
      assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
   }

   // edge case: method returns null if not initialized
   @Test
   public void testGetConvergenceCheckerNull() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      assertNull(optimizer.getConvergenceChecker());
   }

   // edge case: method returns null if convergenceChecker was not initialized
   @Test
   public void testGetConvergenceCheckerNullNotInitialized() {
      BaseOptimizer<ObjectiveFunctionValuesPair> optimizer = mock(BaseOptimizer.class);
      assertNull(optimizer.getConvergenceChecker());
   }

}