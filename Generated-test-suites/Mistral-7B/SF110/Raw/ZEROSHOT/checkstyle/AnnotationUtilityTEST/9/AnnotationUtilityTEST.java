package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class AnnotationUtilityTest {

 private Function<DetailAST, DetailAST> getAnnotationHolderFunction = new Function<DetailAST, DetailAST>() {
 @Override
 public DetailAST apply(DetailAST input) {
 return AnnotationUtility.getAnnotationHolder(input);
 }
 };

 @Test
 public void testContainsAnnotationWithAnnotation() {
 DetailAST ast = createMockAst();
 DetailAST annotation = createMockAnnotation();
 ast.setChild(AnnotationUtility.createAnnotationHolder(ast), annotation);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testContainsAnnotationWithoutAnnotation() {
 DetailAST ast = createMockAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testContainsAnnotationWithNullAst() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "testAnnotation"));
 }

 @Test
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createMockAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotationWithEmptyStringAnnotation() {
 DetailAST ast = createMockAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
 }

 @Test
 public void testContainsAnnotationWithAnnotationHolder() {
 DetailAST ast = createMockAst();
 DetailAST annotationHolder = createMockAnnotationHolder(ast);
 ast.setChild(annotationHolder, null);
 assertTrue(AnnotationUtility.containsAnnotation(ast));
 }

 @Test
 public void testContainsAnnotationWithNoAnnotationHolder() {
 DetailAST ast = createMockAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast));
 }

 @Test
 public void testContainsAnnotationWithInvalidAstType() {
 DetailAST ast = createMockAst(1);
 assertFalse(AnnotationUtility.containsAnnotation(ast));
 }

 @Test
 public void testGetAnnotationWithAnnotation() {
 DetailAST ast = createMockAst();
 DetailAST annotation = createMockAnnotation();
 ast.setChild(AnnotationUtility.createAnnotationHolder(ast), annotation);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "testAnnotation");
 assertEquals(annotation, result);
 }

 @Test
 public void testGetAnnotationWithNullAst() {
 assertNull(AnnotationUtility.getAnnotation(null, "testAnnotation"));
 }

 @Test
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = createMockAst();
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotationWithEmptyStringAnnotation() {
 DetailAST ast = createMockAst();
 assertNull(AnnotationUtility.getAnnotation(ast, ""));
 }

 @Test
 public void testGetAnnotationWithNoAnnotation() {
 DetailAST ast = createMockAst();
 assertNull(AnnotationUtility.getAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testGetAnnotationWithAnnotationHolderButNoAnnotation() {
 DetailAST ast = createMockAst();
 DetailAST annotationHolder = createMockAnnotationHolder(ast);
 ast.setChild(annotationHolder, null);
 assertNull(AnnotationUtility.getAnnotation(ast, "testAnnotation"));
 }

 @Test
 public void testGetAnnotationWithAnnotationHolderAndAnnotation() {
 DetailAST ast = createMockAst();
 DetailAST annotationHolder = createMockAnnotationHolder(ast);
 DetailAST annotation = createMockAnnotation();
 ast.setChild(annotationHolder, annotation);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "testAnnotation");
 assertEquals(annotation, result);
 }

 @Test
 public void testGetAnnotationWithInvalidAstType() {
 DetailAST ast = createMockAst(1);
 assertNull(AnnotationUtility.getAnnotation(ast, "testAnnotation"));
 }

 private DetailAST createMockAst() {
 return createMockAst(0);
 }

 private DetailAST createMockAst(int type) {
 DetailAST ast = MockastFactory.createNonLeaf(type);
 return ast;
 }

 private DetailAST createMockAnnotation() {
 DetailAST annotation = MockastFactory.createToken(TokenTypes.ANNOTATION, "testAnnotation");
 return annotation;
 }

 private DetailAST createMockAnnotationHolder(DetailAST ast) {
 DetailAST annotationHolder = MockastFactory.createToken(TokenTypes.ANNOTATION, "AnnotationHolder");
 ast.setChild(getAnnotationHolderFunction.apply(ast), annotationHolder);
 return annotationHolder;
 }
}