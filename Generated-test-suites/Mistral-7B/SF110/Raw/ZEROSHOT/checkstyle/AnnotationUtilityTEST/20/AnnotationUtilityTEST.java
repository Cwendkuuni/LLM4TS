package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createDetailAST() {
 return new DetailAST();
 }

 @Test
 public void testContainsAnnotationNullAst() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "annotation"));
 }

 @Test
 public void testContainsAnnotationNullAnnotation() {
 DetailAST ast = createDetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotationEmptyAnnotation() {
 DetailAST ast = createDetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
 }

 @Test
 public void testContainsAnnotationNullAnnotationHolder() {
 DetailAST ast = createDetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test
 public void testContainsAnnotationNoAnnotation() {
 DetailAST ast = createDetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test
 public void testContainsAnnotationHasAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test
 public void testContainsAnnotationAnnotationType() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 annotationHolder.addChild(annotationType);
 ast.addChild(annotationHolder);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "com.test.annotation"));
 }

 @Test
 public void testGetAnnotationNullAst() {
 assertNull(AnnotationUtility.getAnnotation(null, "annotation"));
 }

 @Test
 public void testGetAnnotationNullAnnotation() {
 DetailAST ast = createDetailAST();
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotationEmptyAnnotation() {
 DetailAST ast = createDetailAST();
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test
 public void testGetAnnotationNullAnnotationHolder() {
 DetailAST ast = createDetailAST();
 assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
 }

 @Test
 public void testGetAnnotationNoAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
 }

 @Test
 public void testGetAnnotationHasAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 annotationHolder.addChild(annotationType);
 DetailAST annotationValue = createDetailAST();
 annotationValue.setText("value");
 DetailAST value = createDetailAST();
 value.setText("testValue");
 annotation.addChild(value);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "com.test.annotation");
 assertEquals(annotation, result);
 }

 @Test
 public void testGetAnnotationHasMultipleAnnotations() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder1 = createDetailAST();
 annotationHolder1.setType(TokenTypes.ANNOTATION);
 DetailAST annotation1 = createDetailAST();
 annotation1.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType1 = createDetailAST();
 annotationType1.setText("com.test.annotation1");
 annotationHolder1.addChild(annotationType1);
 DetailAST annotationValue1 = createDetailAST();
 annotationValue1.setText("value1");
 DetailAST value1 = createDetailAST();
 value1.setText("testValue1");
 annotation1.addChild(value1);
 ast.addChild(annotationHolder1);
 DetailAST annotationHolder2 = createDetailAST();
 annotationHolder2.setType(TokenTypes.ANNOTATION);
 DetailAST annotation2 = createDetailAST();
 annotation2.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType2 = createDetailAST();
 annotationType2.setText("com.test.annotation2");
 annotationHolder2.addChild(annotationType2);
 DetailAST annotationValue2 = createDetailAST();
 annotationValue2.setText("value2");
 DetailAST value2 = createDetailAST();
 value2.setText("testValue2");
 annotation2.addChild(value2);
 ast.addChild(annotationHolder2);
 DetailAST result1 = AnnotationUtility.getAnnotation(ast, "com.test.annotation1");
 assertEquals(annotation1, result1);
 DetailAST result2 = AnnotationUtility.getAnnotation(ast, "com.test.annotation2");
 assertEquals(annotation2, result2);
 }

 @Test
 public void testContainsAnnotationWithSpacesAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType = createDetailAST();
 annotationType.setText(" com.test.annotation ");
 annotationHolder.addChild(annotationType);
 ast.addChild(annotationHolder);
 assertTrue(AnnotationUtility.containsAnnotation(ast, " com.test.annotation "));
 }

 @Test
 public void testGetAnnotationWithSpacesAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType = createDetailAST();
 annotationType.setText(" com.test.annotation ");
 annotationHolder.addChild(annotationType);
 DetailAST annotationValue = createDetailAST();
 annotationValue.setText("value");
 DetailAST value = createDetailAST();
 value.setText("testValue");
 annotation.addChild(value);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, " com.test.annotation ");
 assertEquals(annotation, result);
 }

 @Test
 public void testGetAnnotationHolderNullAst() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotationHolderNoAnnotationHolder() {
 DetailAST ast = createDetailAST();
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotationHolderAnnotationType() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 annotationHolder.addChild(annotationType);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 }

 @Test
 public void testGetAnnotationHolderAnnotationTypeWithSiblings() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 DetailAST sibling = createDetailAST();
 sibling.setType(TokenTypes.TOKEN);
 annotationHolder.addChild(annotationType);
 annotationHolder.addChild(sibling);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 }

 @Test
 public void testGetAnnotationHolderAnnotationToken() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotation = createDetailAST();
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 DetailAST token = createDetailAST();
 token.setType(TokenTypes.TOKEN);
 annotation.addChild(token);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 }

 @Test
 public void testContainsAnnotationAnnotationToken() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 DetailAST annotation = createDetailAST();
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 DetailAST token = createDetailAST();
 token.setType(TokenTypes.TOKEN);
 annotation.addChild(token);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertFalse(AnnotationUtility.containsAnnotation(ast, "com.test.annotation"));
 }

 @Test
 public void testGetAnnotationAnnotationToken() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 DetailAST annotation = createDetailAST();
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 DetailAST token = createDetailAST();
 token.setType(TokenTypes.TOKEN);
 annotation.addChild(token);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "com.test.annotation");
 assertNull(result);
 }

 @Test
 public void testContainsAnnotationAnnotationHolderWithSiblings() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotationType = createDetailAST();
 annotationType.setText("com.test.annotation");
 DetailAST sibling = createDetailAST();
 sibling.setType(TokenTypes.TOKEN);
 annotationHolder.addChild(annotationType);
 annotationHolder.addChild(sibling);
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 assertTrue(AnnotationUtility.containsAnnotation(result, "com.test.annotation"));
 }

 @Test
 public void testContainsAnnotationAnnotationHolderWithNoAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST sibling = createDetailAST();
 sibling.setType(TokenTypes.TOKEN);
 annotationHolder.addChild(sibling);
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 assertFalse(AnnotationUtility.containsAnnotation(result, "com.test.annotation"));
 }
}