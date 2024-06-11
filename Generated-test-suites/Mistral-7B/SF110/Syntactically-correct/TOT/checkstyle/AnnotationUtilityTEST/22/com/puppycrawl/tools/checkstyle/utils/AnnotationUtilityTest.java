package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

 private DetailAST createSampleAst() {
 // Create a sample DetailAST for testing purposes
 return new DetailAST();
 }

 @Test
 public void testContainsAnnotationWithValidAnnotationAndAst() {
 DetailAST ast = createSampleAst();
 String annotation = "ValidAnnotation";
 DetailAST annotationHolder = ast.findFirstToken(158);
 DetailAST annotationNode = new DetailAST();
 annotationNode.setType(159);
 annotationHolder.addFirstChild(annotationNode);
 DetailAST annotationName = new DetailAST();
 annotationName.setText(annotation);
 annotationNode.addFirstChild(annotationName);
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testContainsAnnotationWithValidAnnotationAndNullAst() {
 String annotation = "ValidAnnotation";
 assertFalse(AnnotationUtility.containsAnnotation(null, annotation));
 }

 @Test
 public void testContainsAnnotationWithNullAnnotationAndValidAst() {
 DetailAST ast = createSampleAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotationWithEmptyOrSpacesAnnotationAndValidAst() {
 DetailAST ast = createSampleAst();
 String annotation = " ";
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testContainsAnnotationWithValidAnnotationAndEmptyAst() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "ValidAnnotation"));
 }

 @Test
 public void testContainsAnnotationWithValidAnnotationAndInvalidAstType() {
 DetailAST ast = createSampleAst();
 ast.setType(1); // Invalid ast type
 String annotation = "ValidAnnotation";
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotationWithValidAnnotationNameAndAst() {
 DetailAST ast = createSampleAst();
 DetailAST annotationHolder = ast.findFirstToken(158);
 DetailAST annotationNode = new DetailAST();
 annotationNode.setType(159);
 String annotation = "ValidAnnotation";
 DetailAST annotationName = new DetailAST();
 annotationName.setText(annotation);
 annotationNode.addFirstChild(annotationName);
 annotationHolder.addFirstChild(annotationNode);
 DetailAST expectedAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
 assertEquals(expectedAnnotation, annotationNode);
 }

 @Test
 public void testGetAnnotationWithInvalidAnnotationNameAndValidAst() {
 DetailAST ast = createSampleAst();
 String annotation = "InvalidAnnotation";
 assertNull(AnnotationUtility.getAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotationWithEmptyOrSpacesAnnotationNameAndValidAst() {
 DetailAST ast = createSampleAst();
 String annotation = " ";
 assertNull(AnnotationUtility.getAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotationWithNullAnnotationNameAndValidAst() {
 DetailAST ast = createSampleAst();
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotationWithValidAnnotationNameAndInvalidAst() {
 assertNull(AnnotationUtility.getAnnotation(null, "ValidAnnotation"));
 }

 @Test
 public void testGetAnnotationWithValidAnnotationNameAndEmptyAst() {
 assertNull(AnnotationUtility.getAnnotation(null, "ValidAnnotation"));
 }

 @Test
 public void testGetAnnotationWithValidAnnotationNameAndInvalidAstType() {
 DetailAST ast = createSampleAst();
 ast.setType(1); // Invalid ast type
 String annotation = "ValidAnnotation";
 assertNull(AnnotationUtility.getAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
 DetailAST ast = createSampleAst();
 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(158, annotationHolder.getType());
 }

 @Test
 public void testGetAnnotationHolderWithNullAst() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotationHolderWithInvalidAstType() {
 DetailAST ast = new DetailAST();
 ast.setType(1); // Invalid ast type
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }
}