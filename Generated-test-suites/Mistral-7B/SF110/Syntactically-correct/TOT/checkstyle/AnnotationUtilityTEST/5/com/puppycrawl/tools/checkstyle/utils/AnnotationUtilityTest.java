package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Strings;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

 private static final DetailAST createSampleAST() {
 // Create a sample DetailAST for testing purposes
 // Replace this with a real DetailAST instance in your testing
 DetailAST ast = new DetailAST();
 // Set type and position for the sample DetailAST
 ast.setType(155);
 ast.setLineNumber(1);
 ast.setColumnNumber(1);
 return ast;
 }

 @Test
 public void testContainsAnnotation_NullAST() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
 }

 @Test
 public void testContainsAnnotation_NullAnnotation() {
 DetailAST ast = new DetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotation_EmptyAnnotation() {
 DetailAST ast = createSampleAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
 }

 @Test
 public void testContainsAnnotation_ContainsAnnotation() {
 DetailAST ast = createSampleAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotation.setType(159);
 DetailAST name = new DetailAST();
 name.setText("TestAnnotation");
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
 }

 @Test
 public void testContainsAnnotation_NoAnnotation() {
 DetailAST ast = createSampleAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "UnknownAnnotation"));
 }

 @Test
 public void testGetAnnotationHolder_NullAST() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotationHolder_AnnotationHolderFound() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(158);
 ast.addChild(annotationHolder);
 DetailAST expectedAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, expectedAnnotationHolder);
 }

 @Test
 public void testGetAnnotationHolder_NoAnnotationHolder() {
 DetailAST ast = new DetailAST();
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotation_NullAST() {
 assertNull(AnnotationUtility.getAnnotation(null, "TestAnnotation"));
 }

 @Test
 public void testGetAnnotation_NullAnnotation() {
 DetailAST ast = new DetailAST();
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotation_NoAnnotationFound() {
 DetailAST ast = createSampleAST();
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(158);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
 }

 @Test
 public void testGetAnnotation_AnnotationFound() {
 DetailAST ast = createSampleAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotation.setType(159);
 DetailAST name = new DetailAST();
 name.setText("TestAnnotation");
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 DetailAST expectedAnnotation = new DetailAST();
 DetailAST expectedName = new DetailAST();
 expectedName.setText("TestAnnotation");
 expectedAnnotation.addChild(expectedName);
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testGetAnnotation_AnnotationWithMultipleAnnotations_FirstAnnotationFound() {
 DetailAST ast = createSampleAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation1 = new DetailAST();
 annotation1.setType(159);
 DetailAST name1 = new DetailAST();
 name1.setText("TestAnnotation1");
 annotation1.addChild(name1);
 DetailAST annotation2 = new DetailAST();
 annotation2.setType(159);
 DetailAST name2 = new DetailAST();
 name2.setText("TestAnnotation2");
 annotation2.addChild(name2);
 annotationHolder.addChild(annotation1);
 annotationHolder.addChild(annotation2);
 ast.addChild(annotationHolder);
 DetailAST expectedAnnotation = new DetailAST();
 DetailAST expectedName = new DetailAST();
 expectedName.setText("TestAnnotation1");
 expectedAnnotation.addChild(expectedName);
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation1");
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAnnotationName() {
 DetailAST ast = createSampleAST();
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_EmptyAnnotationName() {
 DetailAST ast = createSampleAST();
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_AnnotationWithEmptyAnnotationName() {
 DetailAST ast = createSampleAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotation.setType(159);
 DetailAST name = new DetailAST();
 name.setText("");
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_AnnotatationWithWhitespaceAnnotationName() {
 DetailAST ast = createSampleAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotation.setType(159);
 DetailAST name = new DetailAST();
 name.setText(" TestAnnotation ");
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, " TestAnnotation "));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_ContainAnnotation() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_NoAnnotation() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "UnknownAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolder_NullAST() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolder_NullAST_NoAnnotationHolder() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolder_NullAST_TypeMismatch() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(5);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_NoAnnotationFound() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotation.setType(158);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_TypeMismatch_AnnotationHolder() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotation.setType(159);
 DetailAST name = new DetailAST();
 name.setText("TestAnnotation");
 annotation.addChild(name);
 annotationHolder.setType(5);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_TypeMismatch_Annotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST name = new DetailAST();
 name.setText("TestAnnotation");
 annotation.setType(5);
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_AnnotationWithEmptyName() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST name = new DetailAST();
 name.setText("");
 annotation.setType(159);
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_AnnotationWithWhitespaceName() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST name = new DetailAST();
 name.setText(" TestAnnotation ");
 annotation.setType(159);
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, " TestAnnotation "));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_AnnotationWithWhitespaceNameAndEmptyName() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST name = new DetailAST();
 name.setText("");
 annotation.setType(159);
 annotation.addChild(name);
 DetailAST whitespaceName = new DetailAST();
 whitespaceName.setText(" ");
 name.addChild(whitespaceName);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_NullAST_AnnotationWithEmptyNameAndWhitespace() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST name = new DetailAST();
 name.setText(" ");
 annotation.setType(159);
 name.addChild(new DetailAST());
 annotation.addChild(name);
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }
}