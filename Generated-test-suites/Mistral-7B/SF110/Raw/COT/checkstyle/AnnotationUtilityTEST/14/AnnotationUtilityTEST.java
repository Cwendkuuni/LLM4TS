package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.junit.Test;

public class AnnotationUtilityTest {

 private static DetailAST createTestAst() {
 return new DetailAST();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 AnnotationUtility.containsAnnotation(null, "AnnotationName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast, null);
 }

 @Test
 public void testContainsAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast, " ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyAst() {
 AnnotationUtility.containsAnnotation(null, "AnnotationName");
 }

 @Test
 public void testContainsAnnotationWithNoAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.containsAnnotation(ast);
 assertFalse(AnnotationUtility.containsAnnotation(ast));
 }

 @Test
 public void testContainsAnnotationWithAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST annotation = createTestAst();
 ast.setType(TokenTypes.ANNOTATION);
 ast.setProperty("name", "AnnotationName");
 annotation.setType(TokenTypes.ANNOTATION);
 annotation.setProperty("name", "AnnotationName");
 ast.addChild(annotation);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
 }

 @Test
 public void testGetAnnotationHolderWithNullAst() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNonAnnotationAst() {
 DetailAST ast = createTestAst();
 ast.setType(TokenTypes.CLASS_DEF);
 AnnotationUtility.getAnnotationHolder(ast);
 }

 @Test
 public void testGetAnnotationHolderWithAnnotationAst() {
 DetailAST ast = createTestAst();
 DetailAST annotation = createTestAst();
 ast.setType(TokenTypes.ANNOTATION);
 annotation.setProperty("name", "AnnotationName");
 ast.addChild(annotation);
 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotation, annotationHolder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 AnnotationUtility.getAnnotation(null, "AnnotationName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, " ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNonAnnotationAst() {
 DetailAST ast = createTestAst();
 ast.setType(TokenTypes.CLASS_DEF);
 AnnotationUtility.getAnnotation(ast, "AnnotationName");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetAnnotationWithNoSuchAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createTestAst();
 ast.setType(TokenTypes.ANNOTATION);
 ast.addChild(annotationHolder);
 AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation");
 }

 @Test
 public void testGetAnnotationWithExistingAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createTestAst();
 DetailAST annotation = createTestAst();
 ast.setType(TokenTypes.ANNOTATION);
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setProperty("name", "AnnotationName");
 annotation.setType(TokenTypes.ANNOTATION);
 annotation.setProperty("name", "AnnotationName");
 ast.addChild(annotationHolder);
 ast.addChild(annotation);
 DetailAST retrievedAnnotation = AnnotationUtility.getAnnotation(ast, "AnnotationName");
 assertEquals(annotation, retrievedAnnotation);
 }
}