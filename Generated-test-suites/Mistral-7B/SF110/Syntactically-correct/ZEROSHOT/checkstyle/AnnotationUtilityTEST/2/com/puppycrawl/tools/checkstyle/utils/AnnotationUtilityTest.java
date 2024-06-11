package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

 private DetailAST createTestAST() {
  // Create a test DetailAST instance here or use a real DetailAST instance from your project.
  DetailAST ast = new DetailAST();
  // Set the necessary fields for testing.
  return ast;
 }

 @Test
 public void testContainsAnnotationWithValidAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = "ValidAnnotation";
  assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAST() {
  DetailAST ast = null;
  String annotation = "ValidAnnotation";
  AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyAST() {
  DetailAST ast = new DetailAST();
  String annotation = "";
  AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test
 public void testContainsAnnotationWithAnnotationHolder() {
  DetailAST ast = createTestAST();
  String annotation = "AnnotationHolder";
  DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
  assertTrue(AnnotationUtility.containsAnnotation(ast, annotation)
      == (annotationHolder != null && annotationHolder.branchContains(159)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = null;
  AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test
 public void testGetAnnotationWithValidAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = "ValidAnnotation";
  DetailAST expectedAnnotation = new DetailAST();
  // Set the necessary fields for expectedAnnotation.
  DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
  for (DetailAST child = annotationHolder.getFirstChild(); child != null; child = child.getNextSibling()) {
   if (child.getType() == 159) {
    DetailAST firstChild = child.getFirstChild();
    String name = FullIdent.createFullIdent(firstChild.getNextSibling()).getText();
    if (name.equals(annotation)) {
     expectedAnnotation = child;
     break;
    }
   }
  }
  DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
  assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAST() {
  DetailAST ast = null;
  String annotation = "ValidAnnotation";
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAST() {
  DetailAST ast = new DetailAST();
  String annotation = "ValidAnnotation";
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = null;
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = "";
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNoMatchingAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = "NoMatchingAnnotation";
  AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test
 public void testGetAnnotationWithAnnotationHolderAndMatchingAnnotation() {
  DetailAST ast = createTestAST();
  String annotation = "AnnotationHolder";
  DetailAST expectedAnnotation = new DetailAST();
  // Set the necessary fields for expectedAnnotation.
  DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
  for (DetailAST child = annotationHolder.getFirstChild(); child != null; child = child.getNextSibling()) {
   if (child.getType() == 159) {
    DetailAST firstChild = child.getFirstChild();
    String name = FullIdent.createFullIdent(firstChild.getNextSibling()).getText();
    if (name.equals(annotation)) {
     expectedAnnotation = child;
     break;
    }
   }
  }
  DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
  assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testCharMatcherMatchesAllOfAnnotation() {
  String annotation = "   ValidAnnotation   ";
  assertFalse(CharMatcher.WHITESPACE.matchesAllOf(annotation));
 }

 @Test
 public void testCharMatcherMatchesAllOfEmptyAnnotation() {
  String annotation = "";
  assertFalse(CharMatcher.WHITESPACE.matchesAllOf(annotation));
 }
}