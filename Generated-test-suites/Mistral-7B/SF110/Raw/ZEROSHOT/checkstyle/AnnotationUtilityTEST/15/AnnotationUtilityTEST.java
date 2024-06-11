package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private static final String ANNOTATION_NAME = "com.test.annotation";

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
     DetailAST validAst = createValidAST();
     assertTrue(AnnotationUtility.containsAnnotation(validAst, ANNOTATION_NAME));
 }

 @Test
 public void testContainsAnnotationWithValidAstAndNoAnnotation() {
     DetailAST validAst = createValidAST();
     assertFalse(AnnotationUtility.containsAnnotation(validAst, "com.other.annotation"));
 }

 @Test
 public void testContainsAnnotationWithNullAst() {
     try {
         AnnotationUtility.containsAnnotation(null, ANNOTATION_NAME);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals(THE_AST_IS_NULL, e.getMessage());
     }
 }

 @Test
 public void testContainsAnnotationWithNullAnnotation() {
     DetailAST validAst = createValidAST();
     try {
         AnnotationUtility.containsAnnotation(validAst, null);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals("the annotation is null", e.getMessage());
     }
 }

 @Test
 public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
     DetailAST validAst = createValidAST();
     try {
         AnnotationUtility.containsAnnotation(validAst, "");
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals("the annotation is empty or spaces", e.getMessage());
     }
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
     DetailAST validAst = createValidAST();
     DetailAST expectedAnnotationHolder = validAst.findFirstToken(TokenTypes.ANNOTATION);
     DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(validAst);
     assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
 }

 @Test
 public void testGetAnnotationHolderWithNullAst() {
     try {
         AnnotationUtility.getAnnotationHolder(null);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals(THE_AST_IS_NULL, e.getMessage());
     }
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
     DetailAST validAst = createValidAST();
     DetailAST expectedAnnotation = createAnnotationToken(ANNOTATION_NAME);
     DetailAST actualAnnotation = AnnotationUtility.getAnnotation(validAst, ANNOTATION_NAME);
     assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testGetAnnotationWithValidAstAndNoAnnotation() {
     DetailAST validAst = createValidAST();
     assertNull(AnnotationUtility.getAnnotation(validAst, "com.other.annotation"));
 }

 @Test
 public void testGetAnnotationWithNullAst() {
     try {
         AnnotationUtility.getAnnotation(null, ANNOTATION_NAME);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         assertEquals(THE_AST_IS_NULL, e.getMessage());
     }
 }

 @Test
 @SuppressWarnings("ResultOfMethodCallIgnored")
 public void testGetAnnotationWithNullAnnotation() {
     DetailAST validAst = createValidAST();
     try {
         AnnotationUtility.getAnnotation(validAst, null);
         fail("NullPointerException expected");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test
 @SuppressWarnings("ResultOfMethodCallIgnored")
 public void testGetAnnotationWithEmptyOrSpacesAnnotationName() {
     DetailAST validAst = createValidAST();
     try {
         AnnotationUtility.getAnnotation(validAst, "");
         fail("NullPointerException expected");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 private DetailAST createAnnotationToken(String name) {
     DetailAST annotationToken = new DetailAST();
     annotationToken.setType(TokenTypes.ANNOTATION);
     DetailAST nameToken = new DetailAST();
     nameToken.setText(name);
     DetailAST firstChild = new DetailAST();
     firstChild.setType(TokenTypes.IDENT);
     firstChild.setNextSibling(nameToken);
     annotationToken.setFirstChild(firstChild);
     return annotationToken;
 }

 private DetailAST createValidAST() {
     DetailAST ast = new DetailAST();
     ast.setType(TokenTypes.ANNOTATION);
     DetailAST annotationToken = createAnnotationToken(ANNOTATION_NAME);
     ast.setFirstChild(annotationToken);
     return ast;
 }
}