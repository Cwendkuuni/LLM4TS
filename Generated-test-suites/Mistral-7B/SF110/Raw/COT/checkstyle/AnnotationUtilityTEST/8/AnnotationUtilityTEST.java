package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
 return new DetailAST();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 AnnotationUtility.containsAnnotation(null, "annotation");
 }

 @Test
 public void testContainsAnnotationWithNotNullAstAndAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createTestAst();
 DetailAST annotation = createTestAst();
 DetailAST annotationNode = createTestAst();
 annotationNode.setType(159);
 annotationHolder.addChild(annotationNode);
 annotationNode.addFirstChild(annotation);
 ast.addChild(annotationHolder);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAstAndNoAnnotation() {
 DetailAST ast = createTestAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 assertTrue(AnnotationUtility.containsAnnotation(ast, ""));
 }

 @Test
 public void testContainsAnnotationWithNullAnnotationButNotEmpty() {
 DetailAST ast = createTestAst();
 String annotation = " ";
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
 DetailAST ast = createTestAst();
 DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
 assertNotNull(holder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 AnnotationUtility.getAnnotation(null, "annotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, "");
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST annotationHolder = createTestAst();
 DetailAST annotationNode = createTestAst();
 DetailAST annotation = createTestAst();
 annotationNode.setType(159);
 annotation.setText("annotation");
 annotationHolder.addChild(annotationNode);
 annotationNode.addFirstChild(annotation);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
 assertNotNull(result);
 }

 @Test
 public void testGetAnnotationWithValidAstButNoAnnotation() {
 DetailAST ast = createTestAst();
 DetailAST holder = createTestAst();
 DetailAST annotationNode = createTestAst();
 holder.addChild(annotationNode);
 ast.addChild(holder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithInvalidAstType() {
 DetailAST ast = createTestAst();
 ast.setType(1);
 DetailAST holder = createTestAst();
 DetailAST annotationNode = createTestAst();
 DetailAST annotation = createTestAst();
 annotationNode.setType(159);
 annotation.setText("annotation");
 holder.addChild(annotationNode);
 ast.addChild(holder);
 AnnotationUtility.getAnnotation(ast, "annotation");
 }

}