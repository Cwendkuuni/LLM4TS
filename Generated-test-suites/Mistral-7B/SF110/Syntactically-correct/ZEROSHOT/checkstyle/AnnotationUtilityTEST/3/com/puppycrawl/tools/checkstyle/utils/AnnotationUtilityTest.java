package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAST() {
 return new DetailAST() {
 // A placeholder for AST structure in tests
 };
 }

 @Test
 public void testContainsAnnotation_ValidAnnotationPresent() {
 DetailAST ast = new DetailAST();
 String annotation = "AnnotationTest";
 DetailAST annotationHolder = createTestAST();
 DetailAST annotationAnnotation = createTestAST();
 annotationAnnotation.setType(TokenTypes.ANNOTATION);
 annotationAnnotation.setText("@" + annotation);
 annotationHolder.addChild(annotationAnnotation);
 ast.addChild(annotationHolder);

 DetailAST annotationHolderFromAst = AnnotationUtility.getAnnotationHolder(ast);
 DetailAST annotationFromAst = AnnotationUtility.getAnnotation(ast, annotation);

 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotationHolderFromAst));
 assertEquals(annotationFromAst, AnnotationUtility.getAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotation_NullAst() {
 AnnotationUtility.containsAnnotation(null, "AnnotationTest");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotation_NullAnnotationName() {
 AnnotationUtility.containsAnnotation(createTestAST(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotation_EmptyOrWhitespaceAnnotationName() {
 String emptyAnnotation = " ";
 AnnotationUtility.containsAnnotation(createTestAST(), emptyAnnotation);
 }

 @Test
 public void testContainsAnnotation_AnnotationNotPresent() {
 DetailAST ast = new DetailAST();
 String annotation = "AnnotationTest";
 AnnotationUtility.assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testContainsAnnotation_DifferentAnnotationNames() {
 DetailAST ast = new DetailAST();
 String annotation1 = "AnnotationTest1";
 String annotation2 = "AnnotationTest2";

 AnnotationUtility.assertFalse(AnnotationUtility.containsAnnotation(ast, annotation1));
 AnnotationUtility.assertFalse(AnnotationUtility.containsAnnotation(ast, annotation2));
 }

 @Test
 public void testGetAnnotationHolder_AnnotationAnnotationPresent() {
 DetailAST ast = new DetailAST();
 String annotation = "AnnotationTest";
 DetailAST annotationHolder = createTestAST();
 DetailAST annotationAnnotation = createTestAST();
 annotationAnnotation.setType(TokenTypes.ANNOTATION);
 annotationAnnotation.setText("@" + annotation);
 annotationHolder.addChild(annotationAnnotation);
 ast.addChild(annotationHolder);

 DetailAST annotationHolderFromAst = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, annotationHolderFromAst);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolder_NullAst() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotationHolder_NoAnnotationAnnotationPresent() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = createTestAST();
 ast.addChild(annotationHolder);

 DetailAST annotationHolderFromAst = AnnotationUtility.getAnnotationHolder(ast);
 assertNull(annotationHolderFromAst);
 }

 @Test
 public void testGetAnnotation_NullAst() {
 AnnotationUtility.getAnnotation(null, "AnnotationTest");
 }

 @Test
 public void testGetAnnotation_NullAnnotationName() {
 AnnotationUtility.getAnnotation(createTestAST(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_EmptyOrWhitespaceAnnotationName() {
 DetailAST ast = new DetailAST();
 String emptyAnnotation = " ";
 AnnotationUtility.getAnnotation(ast, emptyAnnotation);
 }

 @Test
 public void testGetAnnotation_NoAnnotationAnnotationPresent() {
 DetailAST ast = new DetailAST();
 String annotation = "AnnotationTest";
 AnnotationUtility.assertNull(AnnotationUtility.getAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotation_AnnotationAnnotationPresent() {
 DetailAST ast = new DetailAST();
 String annotation = "AnnotationTest";
 DetailAST annotationHolder = createTestAST();
 DetailAST annotationAnnotation = createTestAST();
 annotationAnnotation.setType(TokenTypes.ANNOTATION);
 annotationAnnotation.setText("@" + annotation);
 annotationHolder.addChild(annotationAnnotation);
 DetailAST annotationFromAst = AnnotationUtility.getAnnotation(ast, annotation);

 assertEquals(annotationAnnotation, annotationFromAst);
 }

 @Test
 public void testGetAnnotation_DifferentAnnotationNames() {
 DetailAST ast = new DetailAST();
 String annotation1 = "AnnotationTest1";
 String annotation2 = "AnnotationTest2";
 DetailAST annotationHolder = createTestAST();
 DetailAST annotationAnnotation1 = createTestAST();
 DetailAST annotationAnnotation2 = createTestAST();
 annotationAnnotation1.setType(TokenTypes.ANNOTATION);
 annotationAnnotation1.setText("@" + annotation1);
 annotationAnnotation2.setType(TokenTypes.ANNOTATION);
 annotationAnnotation2.setText("@" + annotation2);
 annotationHolder.addChild(annotationAnnotation1);
 annotationHolder.addChild(annotationAnnotation2);
 ast.addChild(annotationHolder);

 DetailAST annotationFromAst1 = AnnotationUtility.getAnnotation(ast, annotation1);
 DetailAST annotationFromAst2 = AnnotationUtility.getAnnotation(ast, annotation2);
 assertNull(annotationFromAst1);
 assertNull(annotationFromAst2);
 }
}