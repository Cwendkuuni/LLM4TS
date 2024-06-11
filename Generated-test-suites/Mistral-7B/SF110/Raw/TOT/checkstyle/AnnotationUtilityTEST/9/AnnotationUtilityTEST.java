package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

 @Test
 public void testContainsAnnotation_ValidAnnotation() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 assertTrue(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
 }

 @Test
 public void testContainsAnnotation_NullAst() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "AnnotationName"));
 }

 @Test
 public void testContainsAnnotation_NullAnnotationName() {
 assertFalse(AnnotationUtility.containsAnnotation(createValidAST(), null));
 }

 @Test
 public void testContainsAnnotation_EmptyAnnotationName() {
 assertFalse(AnnotationUtility.containsAnnotation(createValidAST(), ""));
 }

 @Test
 public void testContainsAnnotation_AnnotationHolderNull() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
 }

 @Test
 public void testContainsAnnotation_AnnotationNotFound() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
 }

 @Test
 public void testGetAnnotationHolder_ValidType() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 DetailAST expectedAnnotationHolder = Preconditions.checkNotNull(ast.findFirstToken(158));
 DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals("Unexpected annotation holder", expectedAnnotationHolder, actualAnnotationHolder);
 }

 @Test
 public void testGetAnnotationHolder_InvalidType() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 DetailAST expectedAnnotationHolder = Preconditions.checkNotNull(ast.findFirstToken(5));
 DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals("Unexpected annotation holder", expectedAnnotationHolder, actualAnnotationHolder);
 }

 @Test
 public void testGetAnnotationHolder_NullAst() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotation_ValidAnnotation() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 DetailAST expectedAnnotation = Preconditions.checkNotNull(ast.findFirstToken(2)); // Assuming "ValidAnnotation" is a simple type
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "ValidAnnotation");
 assertEquals("Unexpected annotation", expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testGetAnnotation_NullAst() {
 assertNull(AnnotationUtility.getAnnotation(null, "AnnotationName"));
 }

 @Test
 public void testGetAnnotation_NullAnnotationName() {
 assertNull(AnnotationUtility.getAnnotation(createValidAST(), null));
 }

 @Test
 public void testGetAnnotation_EmptyAnnotationName() {
 assertNull(AnnotationUtility.getAnnotation(createValidAST(), ""));
 }

 @Test
 public void testGetAnnotation_NonExistentAnnotation() {
 DetailAST ast = Preconditions.checkNotNull(createValidAST());
 assertNull(AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation"));
 }

 private DetailAST createValidAST() {
 // Create a DetailAST instance representing a valid AST for testing
 // You can use a library like CheckstyleTestUtils or create a mock AST using JUnit's Mockito or PowerMockito
 return new DetailAST();
 }

}