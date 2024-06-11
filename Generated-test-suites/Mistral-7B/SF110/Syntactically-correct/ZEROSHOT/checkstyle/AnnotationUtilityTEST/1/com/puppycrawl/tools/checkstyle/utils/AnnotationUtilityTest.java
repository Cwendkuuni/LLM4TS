package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AnnotationUtilityTest {

 private AnnotationUtility utility = new AnnotationUtility();

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = createValidAST();
 String annotation = "ValidAnnotation";
 assertTrue(utility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testContainsAnnotationWithValidAstAndEmptyAnnotation() {
 DetailAST ast = createValidAST();
 assertFalse(utility.containsAnnotation(ast, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 utility.containsAnnotation(null, "ValidAnnotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createValidAST();
 utility.containsAnnotation(ast, null);
 }

 @Test
 public void testContainsAnnotationWithValidAstAndInvalidAnnotationType() {
 DetailAST ast = createValidAST();
 String annotation = "InvalidTypeAnnotation";
 assertFalse(utility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
 DetailAST ast = createValidAST();
 DetailAST expected = createAnnotationHolder(ast, "ValidAnnotationType");
 DetailAST actual = utility.getAnnotationHolder(ast);
 assertEquals(expected.getText(), actual.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
 utility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotationWithValidAstAndValidAnnotationName() {
 DetailAST ast = createValidAST();
 String annotation = "ValidAnnotation";
 DetailAST expected = createAnnotation(ast, "ValidAnnotationType", annotation);
 DetailAST actual = utility.getAnnotation(ast, annotation);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 utility.getAnnotation(null, "ValidAnnotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotationName() {
 DetailAST ast = createValidAST();
 utility.getAnnotation(ast, null);
 }

 @Test
 public void testGetAnnotationWithInvalidAstTypeAndValidAnnotationName() {
 DetailAST ast = createInvalidASTType(1);
 String annotation = "ValidAnnotation";
 assertNull(utility.getAnnotation(ast, annotation));
 }

 @Test
 public void testGetAnnotationWithValidAstTypeAndInvalidAnnotationName() {
 DetailAST ast = createValidASTType();
 String annotation = "InvalidAnnotation";
 assertNull(utility.getAnnotation(ast, annotation));
 }

 private DetailAST createValidAST() {
 return createASTWithAnnotation("ValidAnnotationType", "ValidAnnotation");
 }

 private DetailAST createValidASTType() {
 return createASTWithAnnotationType("ValidAnnotationType");
 }

 private DetailAST createInvalidASTType(int type) {
 return createASTWithType(type);
 }

 private DetailAST createASTWithAnnotation(String annotationType, String annotationName) {
 DetailAST ast = createAST();
 DetailAST annotationHolder = createAnnotationHolder(ast, annotationType);
 DetailAST annotation = createAnnotation(ast, annotationType, annotationName);
 return ast;
 }

 private DetailAST createAnnotationHolder(DetailAST ast, String annotationType) {
 DetailAST holder = ast.findFirstToken(TokenTypes.ANNOTATION);
 if (holder.getType() != TokenTypes.ANNOTATION) {
 if (ast.getType() == TokenTypes.CLASS_DEF) {
 holder = ast.findFirstToken(TokenTypes.ANNOTATION);
 }
 else {
 holder = ast.findFirstToken(TokenTypes.ANNOTATION_TYPE_DEF);
 }
 }
 DetailAST expected = createAnnotationFragment(annotationType);
 DetailAST actual = holder;
 assertEquals(expected.getText(), actual.getText());
 return holder;
 }

 private DetailAST createAnnotation(DetailAST ast, String annotationType, String annotationName) {
 DetailAST annotation = ast.findFirstToken(TokenTypes.ANNOTATION);
 DetailAST element = createAnnotationElement(annotation, annotationType, annotationName);
 DetailAST expected = createAnnotationFragment(annotationType + " " + annotationName);
 DetailAST actual = element;
 assertEquals(expected.getText(), actual.getText());
 return element;
 }

 private DetailAST createAnnotationElement(DetailAST annotation, String elementType, String elementName) {
 DetailAST element = annotation.findFirstChild();
 while (element != null && !element.getText().equals(elementType)) {
 element = element.getNextSibling();
 }
 DetailAST name = element.getFirstChild();
 DetailAST expected = createAnnotationFragment(elementType + " " + elementName + " = ");
 DetailAST actual = name;
 assertEquals(expected.getText(), actual.getText());
 return name;
 }

 private DetailAST createAnnotationFragment(String text) {
 DetailAST ast = createAST();
 DetailAST fragment = ast.interpNOWS(text);
 return fragment;
 }

 private DetailAST createAST() {
 DetailAST ast = new DetailAST();
 return ast;
 }

 private DetailAST createASTWithType(int type) {
 DetailAST ast = createAST();
 ast.setType(type);
 return ast;
 }

}