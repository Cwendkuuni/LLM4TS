package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Strings;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 @Test
 public void testContainsAnnotationNullAst() {
  assertFalse(AnnotationUtility.containsAnnotation(null, "SomeAnnotation"));
 }

 @Test
 public void testContainsAnnotationNullAnnotation() {
  DetailAST ast = new DetailAST();
  assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotationEmptyStringAnnotation() {
  DetailAST ast = new DetailAST();
  assertFalse(AnnotationUtility.containsAnnotation(ast, Strings.EMPTY));
 }

 @Test
 public void testContainsAnnotationAnnotationNotPresent() {
  DetailAST ast = new DetailAST();
  assertFalse(AnnotationUtility.containsAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testContainsAnnotationSimpleAnnotation() {
  DetailAST ast = new DetailAST();
  ast.setType(TokenTypes.ANNOTATION);
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setProperty(new SimpleProperty("annotationName", "SomeAnnotation"));
  ast.addChild(annotation);
  DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
  assertTrue(AnnotationUtility.containsAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testGetAnnotationNullAst() {
  assertNull(AnnotationUtility.getAnnotation(null, "SomeAnnotation"));
 }

 @Test
 public void testGetAnnotationNullAnnotation() {
  DetailAST ast = new DetailAST();
  assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotationEmptyStringAnnotation() {
  DetailAST ast = new DetailAST();
  assertNull(AnnotationUtility.getAnnotation(ast, Strings.EMPTY));
 }

 @Test
 public void testGetAnnotationAnnotationNotFound() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(annotationHolder);
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  assertNull(AnnotationUtility.getAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testGetAnnotationAnnotationFound() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setProperty(new SimpleProperty("annotationName", "SomeAnnotation"));
  DetailAST annotationName = new DetailAST();
  annotationName.setText("SomeAnnotation");
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationName);
  annotation.addChild(annotationValue);
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotationHolder.addChild(annotationTypeName);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(annotationHolder);
  DetailAST foundAnnotation = AnnotationUtility.getAnnotation(ast, "SomeAnnotation");
  assertEquals(annotation, foundAnnotation);
 }

 @Test
 public void testGetAnnotationAnnotationNameMismatch() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setProperty(new SimpleProperty("annotationName", "SomeAnnotation"));
  DetailAST annotationName = new DetailAST();
  annotationName.setText("SomeOtherAnnotation");
  annotation.addChild(annotationName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationValue);
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotationHolder.addChild(annotationTypeName);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(annotationHolder);
  assertNull(AnnotationUtility.getAnnotation(ast, "SomeOtherAnnotation"));
 }

 @Test
 public void testContainsAnnotationWithAnnotationHolder() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotation.addChild(annotationTypeName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationValue);
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationHolderName = new DetailAST();
  annotationHolderName.setText("SomeAnnotationHolder");
  annotationHolder.addChild(annotationHolderName);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(annotationHolder);
  assertTrue(AnnotationUtility.containsAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testContainsAnnotationWithWrongAnnotationHolder() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setProperty(new SimpleProperty("annotationName", "SomeAnnotation"));
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotation.addChild(annotationTypeName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationValue);
  DetailAST wrongAnnotationHolder = new DetailAST();
  wrongAnnotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST wrongAnnotationHolderName = new DetailAST();
  wrongAnnotationHolderName.setText("SomeOtherAnnotationHolder");
  wrongAnnotationHolder.addChild(wrongAnnotationHolderName);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(wrongAnnotationHolder);
  assertFalse(AnnotationUtility.containsAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testGetAnnotationWithWrongAnnotationHolder() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  annotation.setProperty(new SimpleProperty("annotationName", "SomeAnnotation"));
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotation.addChild(annotationTypeName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationValue);
  DetailAST wrongAnnotationHolder = new DetailAST();
  wrongAnnotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST wrongAnnotationHolderName = new DetailAST();
  wrongAnnotationHolderName.setText("SomeOtherAnnotationHolder");
  wrongAnnotationHolder.addChild(wrongAnnotationHolderName);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(wrongAnnotationHolder);
  assertNull(AnnotationUtility.getAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testGetAnnotationWithAnnotationHolderAtDifferentDepth() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotation.addChild(annotationTypeName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationValue);
  DetailAST parent = new DetailAST();
  parent.setType(TokenTypes.ANNOTATION);
  DetailAST parentAnnotationHolder = new DetailAST();
  parentAnnotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  parentAnnotationHolder.addChild(new DetailAST("SomeParentAnnotationName"));
  parent.addChild(parentAnnotationHolder);
  parent.addChild(annotation);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(parent);
  DetailAST foundAnnotation = AnnotationUtility.getAnnotation(ast, "SomeAnnotation");
  assertEquals(annotation, foundAnnotation);
 }

 @Test
 public void testGetAnnotationWithWrongAnnotationType() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotation = new DetailAST();
  annotation.setType(TokenTypes.ANNOTATION);
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeOtherAnnotation");
  annotation.addChild(annotationTypeName);
  DetailAST annotationValue = new DetailAST();
  annotationValue.setText("SomeValue");
  annotation.addChild(annotationValue);
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationHolderName = new DetailAST();
  annotationHolderName.setText("SomeAnnotationHolder");
  annotationHolder.addChild(annotationHolderName);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(annotationHolder);
  assertNull(AnnotationUtility.getAnnotation(ast, "SomeAnnotation"));
 }

 @Test
 public void testGetAnnotationHolderNullAst() {
  assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotationHolderEmptyAst() {
  DetailAST ast = new DetailAST();
  assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotationHolderNoAnnotationTypeDef() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST parent = new DetailAST();
  parent.setType(TokenTypes.ANNOTATION);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(parent);
  assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotationHolderWithAnnotationTypeDef() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationHolderName = new DetailAST();
  annotationHolderName.setText("SomeAnnotationHolder");
  annotationHolder.addChild(annotationHolderName);
  DetailAST parent = new DetailAST();
  parent.setType(TokenTypes.ANNOTATION);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(parent);
  parent.addChild(annotationHolder);
  DetailAST foundAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
  assertEquals(annotationHolder, foundAnnotationHolder);
 }

 @Test
 public void testGetAnnotationHolderWithWrongAnnotationTypeDef() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeOtherAnnotationTypeDef");
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationHolderName = new DetailAST();
  annotationHolderName.setText("SomeAnnotationHolder");
  annotationHolder.addChild(annotationHolderName);
  DetailAST parent = new DetailAST();
  parent.setType(TokenTypes.ANNOTATION);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(parent);
  parent.addChild(annotationHolder);
  assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotationHolderWithAnnotationTypeDefAtDifferentDepth() {
  DetailAST ast = new DetailAST();
  DetailAST annotationHolder = new DetailAST();
  DetailAST annotationTypeName = new DetailAST();
  annotationTypeName.setText("com.example.SomeAnnotation");
  annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST annotationHolderName = new DetailAST();
  annotationHolderName.setText("SomeAnnotationHolder");
  annotationHolder.addChild(annotationHolderName);
  DetailAST parent = new DetailAST();
  DetailAST parentAnnotationHolder = new DetailAST();
  parentAnnotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
  DetailAST parentAnnotationHolderName = new DetailAST();
  parentAnnotationHolderName.setText("SomeParentAnnotationHolder");
  parentAnnotationHolder.addChild(parentAnnotationHolderName);
  DetailAST grandparent = new DetailAST();
  grandparent.setType(TokenTypes.ANNOTATION);
  ast.setType(TokenTypes.ANNOTATION);
  ast.addChild(grandparent);
  grandparent.addChild(parent);
  parent.addChild(annotationHolder);
  DetailAST foundAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
  assertEquals(annotationHolder, foundAnnotationHolder);
 }
}