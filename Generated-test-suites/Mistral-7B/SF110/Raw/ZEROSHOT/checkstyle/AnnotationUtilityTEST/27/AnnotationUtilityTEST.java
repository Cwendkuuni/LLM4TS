package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
 final DetailAST ast = new DetailAST();
 // Add nodes to the test AST here for testing purposes
 return ast;
 }

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = "annotationName";
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "annotationName";
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = null;
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = " ";
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test
 public void testContainsAnnotationWithValidAst() {
 DetailAST ast = createTestAst();
 assertTrue(AnnotationUtility.containsAnnotation(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAstForMethodTwo() {
 DetailAST ast = null;
 AnnotationUtility.containsAnnotation(ast);
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
 DetailAST ast = createTestAst();
 DetailAST expectedAnnotationHolder = new DetailAST();
 // Set up the expected annotation holder for testing purposes
 DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
 DetailAST ast = null;
 AnnotationUtility.getAnnotationHolder(ast);
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = "annotationName";
 DetailAST expectedAnnotation = new DetailAST();
 // Set up the expected annotation for testing purposes
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "annotationName";
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = null;
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = " ";
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotationNotPresent() {
 DetailAST ast = createTestAst();
 String annotation = "nonExistentAnnotation";
 DetailAST expectedAnnotation = null;
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
 assertEquals(expectedAnnotation, actualAnnotation);
 }
}