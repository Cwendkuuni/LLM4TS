package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
 // Replace this with the actual DetailAST creation code
 DetailAST ast = new DetailAST();
 return ast;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationNullAst() {
 AnnotationUtility.containsAnnotation(null, "testAnnotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationEmptyOrWhitespaceAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast, "");
 AnnotationUtility.containsAnnotation(ast, " \t\n\r ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAst() {
 AnnotationUtility.getAnnotation(null, "testAnnotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationEmptyOrWhitespaceAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, "");
 AnnotationUtility.getAnnotation(ast, " \t\n\r ");
 }

 @Test
 public void testContainsAnnotationNoAnnotation() {
 DetailAST ast = createTestAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testContainsAnnotationHasAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST annotation = createAnnotation(annotationHolder);
 ast.addChild(annotation);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testGetAnnotationNoAnnotationFound() {
 DetailAST ast = createTestAst();
 assertNull(AnnotationUtility.getAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testGetAnnotationAnnotationFound() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST annotation = createAnnotation(annotationHolder);
 ast.addChild(annotation);
 DetailAST foundAnnotation = AnnotationUtility.getAnnotation(ast, "testAnnotation");
 assertEquals(annotation, foundAnnotation);
 }

 @Test
 public void testGetAnnotationHolderNullAst() {
 DetailAST ast = null;
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderNullAstType() {
 DetailAST ast = createTestAst();
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotationHolderAnnotationType() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST annotation = createAnnotation(annotationHolder);
 ast.addChild(annotationHolder);
 DetailAST foundAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, foundAnnotationHolder);
 }

 @Test
 public void testGetAnnotationHolderNotAnnotationType() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createAnnotationHolder(ast);
 DetailAST notAnnotationHolder = createNonAnnotationHolder(ast);
 ast.addChild(annotationHolder);
 ast.addChild(notAnnotationHolder);
 DetailAST foundAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, foundAnnotationHolder);
 }

 private DetailAST createAnnotationHolder(DetailAST ast) {
 // Replace this with the actual DetailAST creation code
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 return annotationHolder;
 }

 private DetailAST createAnnotation(DetailAST annotationHolder) {
 // Replace this with the actual DetailAST creation code
 DetailAST annotation = new DetailAST();
 annotation.setType(TokenTypes.ANNOTATION);
 DetailAST name = new DetailAST();
 name.setType(TokenTypes.IDENT);
 name.setText("testAnnotation");
 annotation.addFirstChild(name);
 return annotation;
 }

 private DetailAST createNonAnnotationHolder(DetailAST ast) {
 // Replace this with the actual DetailAST creation code
 DetailAST nonAnnotationHolder = new DetailAST();
 nonAnnotationHolder.setType(TokenTypes.OTHER);
 return nonAnnotationHolder;
 }

}