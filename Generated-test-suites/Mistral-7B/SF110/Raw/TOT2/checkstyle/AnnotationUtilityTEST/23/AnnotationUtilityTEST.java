package com.example.junit;

import static org.junit.Assert.*;
import static com.google.common.base.Strings.*;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public final class AnnotationUtilityTest {

  @Test
  public void testContainsAnnotationWithValidAnnotationAndAst() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created
    String annotation = "ValidAnnotation";
    assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
  }

  @Test
  public void testContainsAnnotationWithNullAst() {
    DetailAST ast = null;
    String annotation = "ValidAnnotation";
    AnnotationUtility.containsAnnotation(ast, annotation);
  }

  @Test
  public void testContainsAnnotationWithNullAnnotation() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created
    String annotation = null;
    AnnotationUtility.containsAnnotation(ast, annotation);
  }

  @Test
  public void testContainsAnnotationWithEmptyAnnotation() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created
    String annotation = "";
    AnnotationUtility.containsAnnotation(ast, annotation);
  }

  @Test
  public void testContainsAnnotationWithValidAst() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created and it has the appropriate structure
    assertTrue(AnnotationUtility.containsAnnotation(ast));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotationHolderWithNullAst() {
    DetailAST ast = null;
    AnnotationUtility.getAnnotationHolder(ast);
  }

  @Test
  public void testGetAnnotationHolderWithValidAst() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created and it has the appropriate structure
    DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming an instance can be created
    DetailAST resultAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
    assertEquals(expectedAnnotationHolder, resultAnnotationHolder);
  }

  @Test
  public void testGetAnnotationWithValidAnnotationAndAst() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created and it has the appropriate structure
    String annotation = "ValidAnnotation";
    DetailAST expectedAnnotation = new DetailAST(); // Assuming an instance can be created
    DetailAST resultAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
    assertEquals(expectedAnnotation, resultAnnotation);
  }

  @Test
  public void testGetAnnotationWithValidAnnotationAndAst_MultipleAnnotations() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created and it has multiple annotations with the same name
    String annotation = "ValidAnnotation";
    DetailAST expectedAnnotation1 = new DetailAST(); // Assuming an instance can be created for the first annotation
    DetailAST expectedAnnotation2 = new DetailAST(); // Assuming an instance can be created for the second annotation
    DetailAST resultAnnotation1 = AnnotationUtility.getAnnotation(ast, annotation);
    DetailAST resultAnnotation2 = AnnotationUtility.getAnnotation(ast, annotation);
    assertEquals(expectedAnnotation1, resultAnnotation1);
    assertEquals(expectedAnnotation2, resultAnnotation2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotationWithNullAst() {
    DetailAST ast = null;
    String annotation = "ValidAnnotation";
    AnnotationUtility.getAnnotation(ast, annotation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotationWithNullAnnotation() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created
    String annotation = null;
    AnnotationUtility.getAnnotation(ast, annotation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotationWithEmptyAnnotation() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created
    String annotation = "";
    AnnotationUtility.getAnnotation(ast, annotation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAnnotationWithAstWithoutAnnotation() {
    DetailAST ast = new DetailAST(); // Assuming an instance can be created without an annotation
    String annotation = "ValidAnnotation";
    AnnotationUtility.getAnnotation(ast, annotation);
  }
}