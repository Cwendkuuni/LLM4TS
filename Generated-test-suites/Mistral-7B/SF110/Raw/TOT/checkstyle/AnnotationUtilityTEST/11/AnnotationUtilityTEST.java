package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public final class AnnotationUtilityTest {
   // ContainsAnnotation(DetailAST, String) test cases
   @Test
   public void testContainsAnnotationWithValidASTAndAnnotation() {
      // Test case 1
   }

   @Test
   public void testContainsAnnotationWithValidASTButNoAnnotation() {
      // Test case 2
   }

   @Test(expected = IllegalArgumentException.class)
   public void testContainsAnnotationWithNullAST() {
      // Test case 3
   }

   // ContainsAnnotation(DetailAST) test cases
   @Test
   public void testContainsAnnotationWithValidAST() {
      // Test case 4
   }

   @Test
   public void testContainsAnnotationWithNullAST() {
      // Test case 5
   }

   @Test(expected = IllegalArgumentException.class)
   public void testContainsAnnotationWithInvalidAST() {
      // Test case 6
   }

   // getAnnotation(DetailAST, String) test cases
   @Test
   public void testGetAnnotationWithValidASTAndAnnotation() {
      // Test case 7
   }

   @Test
   public void testGetAnnotationWithNullAST() {
      // Test case 8
   }

   @Test
   public void testGetAnnotationWithNullAnnotation() {
      // Test case 9
   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetAnnotationWithInvalidAST() {
      // Test case 10
   }
}