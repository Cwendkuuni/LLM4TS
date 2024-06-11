package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 // Test for containsAnnotation method with valid DetailAST and annotation
 @Test
 public void testContainsAnnotationWithValidDetailASTAndAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotation.setText("@Test");
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);

 DetailAST annotationToFind = new DetailAST();
 annotationToFind.setType(TokenTypes.ANNOTATION);
 annotationToFind.setText("@Test");

 assertTrue(AnnotationUtility.containsAnnotation(ast, "Test"));
 }

 // Test for containsAnnotation method with null DetailAST
 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullDetailAST() {
 AnnotationUtility.containsAnnotation(null, "Test");
 }

 // Test for containsAnnotation method with null annotation
 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = new DetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 // Test for containsAnnotation method with empty or whitespace annotation
 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
 DetailAST ast = new DetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
 assertFalse(AnnotationUtility.containsAnnotation(ast, " "));
 }

 // Test for containsAnnotation method with DetailAST that doesn't contain annotation
 @Test
 public void testContainsAnnotationWithDetailASTThatDoesntContainAnnotation() {
 DetailAST ast = new DetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "Test"));
 }

 // Test for getAnnotationHolder method with valid DetailAST
 @Test
 public void testGetAnnotationHolderWithValidDetailAST() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotation.setText("@Test");
 annotationHolder.addChild(annotation);
 DetailAST annotationType = new DetailAST();
 annotationType.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotationType.setText("Test");
 ast.addChild(annotationType);
 ast.addChild(annotationHolder);

 DetailAST expectedAnnotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
 DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
 }

 // Test for getAnnotationHolder method with null DetailAST
 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullDetailAST() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 // Test for getAnnotation method with valid DetailAST and annotation
 @Test
 public void testGetAnnotationWithValidDetailASTAndAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setText("@Test");
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotation.setText("Test");
 annotationHolder.addChild(annotation);
 DetailAST firstChild = new DetailAST();
 DetailAST name = new DetailAST();
 name.setText("Test");
 firstChild.setType(TokenTypes.IDENTIFIER);
 firstChild.addChild(name);
 annotationHolder.addChild(firstChild);
 ast.addChild(annotationHolder);

 DetailAST expectedAnnotation = annotationHolder.getFirstChild();
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "Test");
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 // Test for getAnnotation method with null DetailAST
 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullDetailAST() {
 AnnotationUtility.getAnnotation(null, "Test");
 }

 // Test for getAnnotation method with null annotation
 @Test
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setText("@Test");
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotation.setText("Test");
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);

 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 // Test for getAnnotation method with empty or whitespace annotation
 @Test
 public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setText("@Test");
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotation.setText("");
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);

 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 assertNull(AnnotationUtility.getAnnotation(ast, " "));
 }

 // Test for getAnnotation method with DetailAST that doesn't contain annotation
 @Test
 public void testGetAnnotationWithDetailASTThatDoesntContainAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setText("@Test");
 annotation.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 annotation.setText("Test");
 annotationHolder.addChild(annotation);
 ast.addChild(annotationHolder);

 DetailAST invalidAnnotation = new DetailAST();
 invalidAnnotation.setText("Invalid");
 assertNull(AnnotationUtility.getAnnotation(ast, "Invalid"));
 }
}