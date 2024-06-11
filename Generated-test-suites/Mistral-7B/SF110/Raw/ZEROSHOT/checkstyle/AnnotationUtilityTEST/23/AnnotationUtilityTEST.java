package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
  // Create a sample DetailAST for testing
  DetailAST ast = new DetailAST();
  ast.setType(155); // Type Annotation
  DetailAST annotationHolder = new DetailAST();
  annotationHolder.setType(TokenTypes.ANNOTATION);
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  annotation.setText("TestAnnotation");
  DetailAST annotationName = new DetailAST();
  annotationName.setText("TestAnnotation");
  annotation.addChild(annotationName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("test value");
  annotation.addChild(annotationValue);
  annotationHolder.addChild(annotation);
  ast.addChild(annotationHolder);
  return ast;
 }

 @Test
 public void testContainsAnnotationWithValidAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = "TestAnnotation";
  assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
  DetailAST ast = null;
  String annotation = "TestAnnotation";
  AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyAnnotation() {
  DetailAST ast = createTestAst();
  String annotation = "";
  AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test
 public void testContainsAnnotationWithValidAnnotationType() {
  DetailAST ast = createTestAst();
  assertTrue(AnnotationUtility.containsAnnotation(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAstForType() {
  DetailAST ast = null;
  AnnotationUtility.containsAnnotation(ast);
 }

 @Test
 public void testGetAnnotationHolderWithValidAnnotation() {
  DetailAST ast = createTestAst();
  DetailAST expected = ast.findFirstToken(TokenTypes.ANNOTATION);
  DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
  assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
  DetailAST ast = null;
  AnnotationUtility.getAnnotationHolder(ast);
 }

 @Test
 public void testGetAnnotationWithValidAnnotationAndName() {
  DetailAST ast = createTestAst();
  DetailAST expected = ast.findFirstToken(TokenTypes.ANNOTATION);
  DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
  assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
  DetailAST ast = null;
  String annotation = "TestAnnotation";
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAnnotationName() {
  DetailAST ast = createTestAst();
  String annotation = "";
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test
 public void testGetAnnotationWithValidAnnotationName() {
  DetailAST ast = createTestAst();
  String annotation = "TestAnnotation";
  DetailAST expected = ast.findFirstToken(TokenTypes.ANNOTATION);
  DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
  assertEquals(expected, result);
 }

 @Test
 public void testGetAnnotationWithInvalidAnnotationName() {
  DetailAST ast = createTestAst();
  String annotation = "InvalidAnnotation";
  assertNull(AnnotationUtility.getAnnotation(ast, annotation));
 }
}