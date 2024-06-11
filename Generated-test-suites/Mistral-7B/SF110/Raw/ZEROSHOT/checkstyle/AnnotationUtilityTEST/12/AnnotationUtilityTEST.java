package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
 // Replace this with the actual DetailAST that you want to test with
 DetailAST ast = new DetailAST();
 return ast;
 }

 @Test
 public void testContainsAnnotationWithValidAnnotationAndAst() {
 DetailAST ast = createTestAst();
 String annotation = "ValidAnnotation";
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "ValidAnnotation";
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
 public void testContainsAnnotationWithValidAnnotationAndAnnotationHolder() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = new DetailAST();
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotationHolder, "ValidAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotationHolder() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = null;
 AnnotationUtility.containsAnnotation(ast, annotationHolder, "ValidAnnotation");
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
 DetailAST ast = createTestAst();
 DetailAST expectedAnnotationHolder = new DetailAST();
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
 String annotation = "ValidAnnotation";
 DetailAST expectedAnnotation = new DetailAST();
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "ValidAnnotation";
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
 public void testGetAnnotationWithValidAstAndAnnotationName() {
 DetailAST ast = createTestAst();
 String annotation = "ValidAnnotation";
 DetailAST expectedAnnotation = new DetailAST();
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
 assertEquals(expectedAnnotation, actualAnnotation);
 }
}