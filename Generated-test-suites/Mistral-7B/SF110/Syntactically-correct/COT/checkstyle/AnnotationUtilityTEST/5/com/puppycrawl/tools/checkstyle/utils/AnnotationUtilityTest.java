package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createDetailAST() {
 return new DetailAST();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 AnnotationUtility.containsAnnotation(null, "annotationName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 AnnotationUtility.containsAnnotation(createDetailAST(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
 AnnotationUtility.containsAnnotation(createDetailAST(), " ");
 }

 @Test
 public void testContainsAnnotation() {
 DetailAST ast = createDetailAST();
 assertTrue(AnnotationUtility.containsAnnotation(ast, "AnnotationType"));
 }

 @Test
 public void testContainsAnnotationWithAnnotationHolder() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 ast.setType(AnnotationUtility.ANNOTATION_TYPE);
 annotationHolder.setType(AnnotationUtility.ANNOTATION_HOLDER_TYPE);
 ast.addChild(annotationHolder);
 ast.addChild(createAnnotationNode("AnnotationType"));
 assertTrue(AnnotationUtility.containsAnnotation(ast, "AnnotationType"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotationHolder() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertNull(annotationHolder);
 }

 @Test
 public void testGetAnnotationHolderWithAnnotationType155() {
 DetailAST ast = createDetailAST();
 ast.setType(AnnotationUtility.ANNOTATION_TYPE_155);
 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(AnnotationUtility.ANNOTATION_HOLDER_TYPE, annotationHolder.getType());
 }

 @Test
 public void testGetAnnotationHolderWithAnnotationType16() {
 DetailAST ast = createDetailAST();
 ast.setType(AnnotationUtility.ANNOTATION_TYPE_16);
 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(AnnotationUtility.ANNOTATION_HOLDER_TYPE, annotationHolder.getType());
 }

 @Test
 public void testGetAnnotationHolderWithOtherType() {
 DetailAST ast = createDetailAST();
 ast.setType(10);
 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertNull(annotationHolder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 AnnotationUtility.getAnnotation(null, "AnnotationType");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotationName() {
 AnnotationUtility.getAnnotation(createDetailAST(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyOrWhitespaceAnnotationName() {
 AnnotationUtility.getAnnotation(createDetailAST(), " ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotationHolder() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 ast.setType(AnnotationUtility.ANNOTATION_TYPE);
 annotationHolder.setType(AnnotationUtility.ANNOTATION_HOLDER_TYPE);
 ast.addChild(annotationHolder);
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 AnnotationUtility.getAnnotation(ast, "AnnotationType");
 }

 @Test
 public void testGetAnnotation() {
 DetailAST ast = createDetailAST();
 DetailAST annotationHolder = createDetailAST();
 DetailAST annotation = createAnnotationNode("AnnotationType");
 DetailAST annotationName = createAnnotationNameNode("AnnotationType");
 annotationHolder.addChild(annotationName);
 annotationHolder.addChild(annotation);
 ast.setType(AnnotationUtility.ANNOTATION_TYPE);
 ast.addChild(annotationHolder);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "AnnotationType");
 assertSame(annotation, result);
 }

 private DetailAST createAnnotationNode(String name) {
 DetailAST node = new DetailAST();
 node.setType(AnnotationUtility.ANNOTATION_TYPE);
 node.setText(name);
 return node;
 }

 private DetailAST createAnnotationNameNode(String name) {
 DetailAST node = new DetailAST();
 node.setType(AnnotationUtility.ANNOTATION_NAME);
 node.setText(name);
 return node;
 }
}