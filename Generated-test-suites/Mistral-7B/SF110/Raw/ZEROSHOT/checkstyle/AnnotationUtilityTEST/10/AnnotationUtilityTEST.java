package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAST() {
  // Create a mock DetailAST for testing
  DetailAST ast = new DetailAST();
  ast.setType(TokenTypes.ANNOTATION);
  DetailAST annotationHolder = new DetainAST();
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setText("MyAnnotation");
  DetailAST name = new DetailAST();
  name.setType(TokenTypes.IDENTIFIER);
  name.setText("MyAnnotation");
  DetailAST value = new DetailAST();
  value.setType(TokenTypes.STRING_LITERAL);
  value.setText("value");
  DetailAST expression = new DetailAST();
  expression.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
  expression.setFirstChild(value);
  annotation.setFirstChild(name);
  annotation.setNextSibling(expression);
  annotationHolder.setFirstChild(annotation);
  ast.setFirstChild(annotationHolder);
  return ast;
 }

 @Test
 public void testContainsAnnotation_Valid() {
  DetailAST ast = createTestAST();
  assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotation_NullAST() {
  assertNull(AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotation_NullAnnotation() {
  DetailAST ast = createTestAST();
  assertNull(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotation_EmptyAnnotation() {
  DetailAST ast = createTestAST();
  assertNull(AnnotationUtility.containsAnnotation(ast, ""));
 }

 @Test
 public void testContainsAnnotation_NotAnnotation() {
  DetailAST ast = new DetailAST();
  ast.setType(TokenTypes.CLASS_DEF);
  assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
 }

 @Test
 public void testContainsAnnotation_AnnotationHolderNotFound() {
  DetailAST ast = createTestAST();
  DetailAST child = ast.getFirstChild();
  child.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
 }

 @Test
 public void testContainsAnnotation_AnnotationNameNotFound() {
  DetailAST ast = createTestAST();
  DetailAST annotationHolder = ast.findFirstToken(158);
  annotationHolder.setNextSibling(new DetailAST());
  assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
 }

 @Test
 public void testContainsAnnotation_MultipleAnnotations() {
  DetailAST ast = createTestAST();
  DetailAST annotationHolder = ast.findFirstToken(158);
  DetailAST annotation1 = new DetailAST();
  annotation1.setType(TokenTypes.ANNOTATION);
  annotation1.setText("MyAnnotation1");
  DetailAST name1 = new DetailAST();
  name1.setType(TokenTypes.IDENTIFIER);
  name1.setText("MyAnnotation1");
  DetailAST value1 = new DetailAST();
  value1.setType(TokenTypes.STRING_LITERAL);
  value1.setText("value1");
  DetailAST expression1 = new DetailAST();
  expression1.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
  expression1.setFirstChild(value1);
  annotation1.setFirstChild(name1);
  annotation1.setNextSibling(expression1);
  DetailAST annotation2 = new DetailAST();
  annotation2.setType(TokenTypes.ANNOTATION);
  annotation2.setText("MyAnnotation2");
  DetailAST name2 = new DetailAST();
  name2.setType(TokenTypes.IDENTIFIER);
  name2.setText("MyAnnotation2");
  DetailAST value2 = new DetailAST();
  value2.setType(TokenTypes.STRING_LITERAL);
  value2.setText("value2");
  DetailAST expression2 = new DetailAST();
  expression2.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
  expression2.setFirstChild(value2);
  annotation2.setFirstChild(name2);
  annotation2.setNextSibling(expression2);
  DetailAST annotationHolderCopy = new DetailAST();
  annotationHolderCopy.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  annotationHolderCopy.setFirstChild(annotation1);
  annotationHolderCopy.setNextSibling(annotation2);
  annotationHolder.setFirstChild(annotationHolderCopy);
  assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation1"));
  assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation2"));
 }

 @Test
 public void testGetAnnotationHolder_Valid() {
  DetailAST ast = createTestAST();
  DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
  assertEquals(TokenTypes.ANNOTATION_TYPE_DEF, result.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolder_NullAST() {
  assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotationHolder_NoAnnotationTypeDef() {
  DetailAST ast = new DetailAST();
  ast.setType(TokenTypes.CLASS_DEF);
  assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotationHolder_MixedAnnotations() {
  DetailAST ast = createTestAST();
  DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
  assertTrue(result.getNextSibling() != null);
 }

 @Test
 public void testGetAnnotation_Valid() {
  DetailAST ast = createTestAST();
  DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
  assertEquals(TokenTypes.ANNOTATION, result.getType());
  assertEquals("MyAnnotation", result.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST() {
  assertNull(AnnotationUtility.getAnnotation(null, "MyAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAnnotation() {
  DetailAST ast = createTestAST();
  assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_EmptyAnnotation() {
  DetailAST ast = createTestAST();
  assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_AnnotationNotFound() {
  DetailAST ast = createTestAST();
  assertNull(AnnotationUtility.getAnnotation(ast, "MyOtherAnnotation"));
 }

 @Test
 public void testGetAnnotation_MultipleAnnotations() {
  DetailAST ast = createTestAST();
  DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation1");
  assertEquals(TokenTypes.ANNOTATION, result.getType());
  assertEquals("MyAnnotation1", result.getText());
 }

 @Test
 public void testGetAnnotation_MixedTypes() {
  DetailAST ast = new DetailAST();
  ast.setType(TokenTypes.ANNOTATION);
  DetailAST annotationHolder = new DetailAST();
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setText("MyAnnotation");
  DetailAST name = new DetailAST();
  name.setType(TokenTypes.IDENTIFIER);
  name.setText("MyAnnotation");
  DetailAST value = new DetailAST();
  value.setType(TokenTypes.STRING_LITERAL);
  value.setText("value");
  DetailAST expression = new DetailAST();
  expression.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
  expression.setFirstChild(value);
  annotation.setFirstChild(name);
  annotation.setNextSibling(expression);
  DetailAST annotationHolderCopy = new DetailAST();
  annotationHolderCopy.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  annotationHolderCopy.setFirstChild(annotation);
  DetailAST nestedAST = new DetailAST();
  nestedAST.setType(TokenTypes.CLASS_DEF);
  DetailAST nestedAnnotationHolder = new DetailAST();
  nestedAnnotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST nestedAnnotation = new DetailAST();
  nestedAnnotation.setType(TokenTypes.ANNOTATION);
  nestedAnnotation.setText("MyOtherAnnotation");
  DetailAST nestedName = new DetailAST();
  nestedName.setType(TokenTypes.IDENTIFIER);
  nestedName.setText("MyOtherAnnotation");
  DetailAST nestedValue = new DetailAST();
  nestedValue.setType(TokenTypes.STRING_LITERAL);
  nestedValue.setText("value2");
  DetailAST nestedExpression = new DetailAST();
  nestedExpression.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
  nestedExpression.setFirstChild(nestedValue);
  nestedAnnotation.setFirstChild(nestedName);
  nestedAnnotation.setNextSibling(nestedExpression);
  nestedAnnotationHolder.setFirstChild(nestedAnnotation);
  annotationHolderCopy.setNextSibling(nestedAnnotationHolder);
  annotationHolder.setFirstChild(annotationHolderCopy);
  ast.setFirstChild(annotationHolder);
  nestedAST.setFirstChild(annotationHolder);
  DetailAST result = AnnotationUtility.getAnnotation(ast, "MyOtherAnnotation");
  assertEquals(TokenTypes.ANNOTATION, result.getType());
  assertEquals("MyOtherAnnotation", result.getText());
 }

}