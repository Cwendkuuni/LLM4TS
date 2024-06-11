package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

 private DetailAST createAST() {
 return new DetailAST();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationNullAST() {
 AnnotationUtility.containsAnnotation(null, "annotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationNullAnnotation() {
 AnnotationUtility.containsAnnotation(createAST(), null);
 }

 @Test
 public void testContainsAnnotationNoAnnotation() {
 DetailAST ast = createAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test
 public void testContainsAnnotationHasAnnotation() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 DetailAST annotation = createAST();
 annotationHolder.setType(155);
 annotation.setType(159);
 annotation.setText("annotation");
 ast.addChild(annotationHolder);
 ast.addChild(annotation);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAST() {
 AnnotationUtility.getAnnotation(null, "annotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationNullAnnotation() {
 AnnotationUtility.getAnnotation(createAST(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationEmptyAnnotation() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 DetailAST annotation = createAST();
 annotationHolder.setType(155);
 annotation.setType(158);
 String emptyAnnotation = "";
 annotation.setText(emptyAnnotation);
 ast.addChild(annotationHolder);
 ast.addChild(annotation);
 AnnotationUtility.getAnnotation(ast, emptyAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationAnnotationWithSpaces() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 DetailAST annotation = createAST();
 annotationHolder.setType(155);
 annotation.setType(158);
 String annotationWithSpaces = "annotation   ";
 annotation.setText(annotationWithSpaces);
 ast.addChild(annotationHolder);
 ast.addChild(annotation);
 AnnotationUtility.getAnnotation(ast, annotationWithSpaces);
 }

 @Test
 public void testGetAnnotationNoAnnotation() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 DetailAST annotation = createAST();
 annotationHolder.setType(155);
 annotation.setType(158);
 annotation.setText("annotation");
 ast.addChild(annotationHolder);
 DetailAST expectedResult = null;
 DetailAST result = AnnotationUtility.getAnnotation(ast, "notAnnotation");
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetAnnotationMatchingAnnotation() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 DetailAST annotation = createAST();
 annotationHolder.setType(155);
 annotation.setType(158);
 String annotationName = "annotation";
 annotation.setText(annotationName);
 ast.addChild(annotationHolder);
 ast.addChild(annotation);
 DetailAST expectedResult = annotation;
 DetailAST result = AnnotationUtility.getAnnotation(ast, annotationName);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testContainsAnnotationWithType156() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 DetailAST annotation = createAST();
 annotationHolder.setType(156);
 annotation.setType(158);
 annotation.setText("annotation");
 ast.addChild(annotationHolder);
 ast.addChild(annotation);
 assertFalse(AnnotationUtility.containsAnnotation(ast));
 }

 @Test
 public void testGetAnnotationHolderNullAST() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderNullASTType155() {
 DetailAST ast = createAST();
 DetailAST expectedResult = null;
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderNullASTType16() {
 DetailAST ast = createAST();
 DetailAST expectedResult = null;
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetAnnotationHolderType155() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 annotationHolder.setType(155);
 ast.addChild(annotationHolder);
 DetailAST expectedResult = annotationHolder;
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetAnnotationHolderType16() {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAST();
 annotationHolder.setType(16);
 ast.addChild(annotationHolder);
 DetailAST expectedResult = annotationHolder;
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedResult, result);
 }
}