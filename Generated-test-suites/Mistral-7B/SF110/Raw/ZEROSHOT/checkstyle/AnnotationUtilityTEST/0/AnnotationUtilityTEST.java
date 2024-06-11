package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
  // Create a test DetailAST here if needed
  // for example using a mocking library or a factory method
  return null;
 }

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = "MyAnnotation";
  assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
  DetailAST ast = null;
  String annotation = "MyAnnotation";
  AnnotationUtility.containsAnnotation(ast, annotation);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = " ";
  AnnotationUtility.containsAnnotation(ast, annotation);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test
 public void testContainsAnnotationWithValidAstOnly() {
  DetailAST ast = createTestAst();
  assertTrue(AnnotationUtility.containsAnnotation(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAstOnly() {
  DetailAST ast = null;
  AnnotationUtility.containsAnnotation(ast);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
  DetailAST ast = createTestAst();
  DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
  assertNotNull(holder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
  DetailAST ast = null;
  AnnotationUtility.getAnnotationHolder(ast);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = "MyAnnotation";
  DetailAST expected = createTestDetailAstWithName(annotation);
  DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
  assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
  DetailAST ast = null;
  String annotation = "MyAnnotation";
  AnnotationUtility.getAnnotation(ast, annotation);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = null;
  AnnotationUtility.getAnnotation(ast, annotation);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = " ";
  AnnotationUtility.getAnnotation(ast, annotation);
  fail("Expected IllegalArgumentException but got none.");
 }

 @Test
 public void testGetAnnotationWithInvalidAstAndAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = "InvalidAnnotation";
  assertNull(AnnotationUtility.getAnnotation(ast, annotation));
 }

 private DetailAST createTestDetailAstWithName(String name) {
  // Create a test DetailAST here if needed
  // for example using a mocking library or a factory method
  return null;
 }

}