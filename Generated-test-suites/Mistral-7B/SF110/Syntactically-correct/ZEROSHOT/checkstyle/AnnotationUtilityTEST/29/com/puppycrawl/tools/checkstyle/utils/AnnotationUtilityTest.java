package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
 // Replace this with the creation of a valid DetailAST object for testing.
 DetailAST ast = new DetailAST();
 return ast;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationNullAst() {
 AnnotationUtility.containsAnnotation(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationEmptyAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast, "");
 }

 @Test
 public void testContainsAnnotationPresent() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST annotation = createAnnotation(annotationHolder);
 AnnotationUtility.containsAnnotation(ast);
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation.getText()));
 }

 @Test
 public void testContainsAnnotationAbsent() {
 DetailAST ast = createTestAst();
 DetailAST annotation = createAnnotation("NotPresentAnnotation");
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation.getText()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAst() {
 AnnotationUtility.getAnnotation(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationEmptyAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAnnotationHolder() {
 DetailAST ast = createTestAst();
 DetailAST annotation = createAnnotation("SomeAnnotation");
 AnnotationUtility.getAnnotation(ast, annotation);
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotationPresent() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST annotation = createAnnotation(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, annotation.getText());
 assertEquals(annotation, result);
 }

 @Test
 public void testGetAnnotationAbsent() {
 DetailAST ast = createTestAst();
 DetailAST annotation = createAnnotation("NotPresentAnnotation");
 DetailAST result = AnnotationUtility.getAnnotation(ast, annotation.getText());
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderNullAst() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderNotAnnotationHolder() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotationHolder(ast.cloneTree());
 }

 @Test
 public void testGetAnnotationHolderPresent() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 }

 @Test
 public void testGetAnnotationHolderAbsent() {
 DetailAST ast = createTestAst();
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertNull(result);
 }

 private DetailAST createAnnotation(DetailAST annotationHolder) {
 DetailAST annotation = new DetailAST();
 annotation.setType(TokenTypes.ANNOTATION);
 annotation.setText("SomeAnnotation");
 annotationHolder.addChild(annotation);
 return annotation;
 }

 private DetailAST createAnnotationHolder(DetailAST ast) {
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 DetailAST holder = new DetailAST();
 holder.setType(TokenTypes.TYPE_IDENT);
 holder.setText("AnnotationType");
 annotationHolder.addChild(holder);
 ast.addChild(annotationHolder);
 return annotationHolder;
 }
}