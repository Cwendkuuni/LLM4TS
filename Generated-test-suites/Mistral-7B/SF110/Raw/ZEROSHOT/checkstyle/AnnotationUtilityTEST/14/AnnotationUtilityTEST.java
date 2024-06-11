package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAST() {
 // Create a sample DetailAST for testing
 DetailAST ast = new DetailAST();
 DetailAST type = new DetailAST();
 type.setType(TokenTypes.ANNOTATION);
 ast.setParent(ast);
 ast.setType(TokenTypes.ANNOTATION_TYPE_DEF);
 ast.setName("TestAnnotation");
 ast.addChild(type);
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setParent(ast);
 DetailAST annotation = new DetailAST();
 annotation.setType(TokenTypes.ANNOTATION);
 annotation.setParent(annotationHolder);
 DetailAST name = new DetailAST();
 name.setText("test");
 name.setType(TokenTypes.IDENT);
 annotation.addChild(name);
 DetailAST value = new DetailAST();
 value.setText("TestValue");
 value.setType(TokenTypes.ASSIGN);
 annotation.addChild(value);
 annotationHolder.addChild(annotation);
 DetailAST element = new DetailAST();
 element.setType(TokenTypes.ANNOTATION_ELEMENT);
 element.setParent(annotation);
 DetailAST elementName = new DetailAST();
 elementName.setText("elementName");
 elementName.setType(TokenTypes.IDENT);
 element.addChild(elementName);
 DetailAST elementValue = new DetailAST();
 elementValue.setText("TestElementValue");
 elementValue.setType(TokenTypes.ASSIGN);
 element.addChild(elementValue);
 annotationHolder.addChild(element);
 ast.addChild(annotationHolder);
 return ast;
 }

 @Test
 public void testContainsAnnotation_nullAst() {
 assertFalse(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
 }

 @Test
 public void testContainsAnnotation_nullAnnotation() {
 DetailAST ast = createTestAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotation_emptyAnnotation() {
 DetailAST ast = createTestAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, " "));
 }

 @Test
 public void testContainsAnnotation_noAnnotation() {
 DetailAST ast = new DetailAST();
 assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
 }

 @Test
 public void testContainsAnnotation_singleAnnotation() {
 DetailAST ast = createTestAST();
 assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
 }

 @Test
 public void testContainsAnnotation_multipleAnnotations() {
 DetailAST ast = createTestAST();
 DetailAST anotherAnnotation = new DetailAST();
 anotherAnnotation.setType(TokenTypes.ANNOTATION);
 anotherAnnotation.setParent(ast);
 DetailAST anotherAnnotationName = new DetailAST();
 anotherAnnotationName.setText("AnotherAnnotation");
 anotherAnnotationName.setType(TokenTypes.IDENT);
 anotherAnnotation.addChild(anotherAnnotationName);
 ast.addChild(anotherAnnotation);
 assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
 }

 @Test
 public void testGetAnnotationHolder_nullAst() {
 assertNull(AnnotationUtility.getAnnotationHolder(null));
 }

 @Test
 public void testGetAnnotationHolder_annotationTypeDef() {
 DetailAST ast = createTestAST();
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals("annotationHolder", result.getText());
 }

 @Test
 public void testGetAnnotationHolder_classDeclaration() {
 DetailAST ast = createTestAST();
 DetailAST result = AnnotationUtility.getAnnotationHolder(ast.getFirstChild());
 assertEquals("holder", result.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolder_nullChild() {
 DetailAST ast = createTestAST();
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotation_nullAst() {
 assertNull(AnnotationUtility.getAnnotation(null, "TestAnnotation"));
 }

 @Test
 public void testGetAnnotation_nullAnnotation() {
 DetailAST ast = createTestAST();
 assertNull(AnnotationUtility.getAnnotation(ast, null));
 }

 @Test
 public void testGetAnnotation_noAnnotation() {
 DetailAST ast = new DetailAST();
 assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
 }

 @Test
 public void testGetAnnotation_singleAnnotation() {
 DetailAST ast = createTestAST();
 DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
 assertEquals("annotation", result.getText());
 }

 @Test
 public void testGetAnnotation_multipleAnnotations() {
 DetailAST ast = createTestAST();
 DetailAST anotherAnnotation = new DetailAST();
 anotherAnnotation.setType(TokenTypes.ANNOTATION);
 anotherAnnotation.setParent(ast);
 anotherAnnotation.addChild(new DetailAST("AnotherAnnotationName", TokenTypes.IDENT));
 ast.addChild(anotherAnnotation);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
 assertNull(result);
 }

 @Test
 public void testGetAnnotation_annotationNameCaseInsensitive() {
 DetailAST ast = createTestAST();
 DetailAST result = AnnotationUtility.getAnnotation(ast, "TESTANNOTATION");
 assertEquals("annotation", result.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_nullAnnotationName() {
 DetailAST ast = createTestAST();
 AnnotationUtility.getAnnotation(ast, null);
 }

 @Test
 public void testGetAnnotation_annotationElement() {
 DetailAST ast = createTestAST();
 DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation").getFirstChild();
 assertEquals("element", result.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotation_annotationElementNull() {
 DetailAST ast = createTestAST();
 DetailAST annotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
 DetailAST element = annotation.getFirstChild();
 element.setParent(null);
 AnnotationUtility.getAnnotationElement(annotation);
 }
}