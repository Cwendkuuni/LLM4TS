package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNotNullAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        assertNull(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNotNullAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithNotNullAstAndAnnotationName() {
        DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.ANNOTATION);
        ast.setText("@TestAnnotation");
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotationHolder.setProperty("name", "TestAnnotation");
        ast.setParent(annotationHolder);
        DetailAST child = new DetailAST();
        child.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        child.setText("TestAnnotation");
        child.setProperty("typeid", "TestAnnotationTypeID");
        child.setParent(ast);
        DetailAST firstChild = new DetailAST();
        firstChild.setText("TestAnnotationName");
        firstChild.setType(TokenTypes.IDENT);
        firstChild.setParent(child);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotationName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithNotNullAstButNoAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithNotNullAstAndIsAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.ANNOTATION);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderWithNotNullAstButNotAnAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.CLASS_DEF);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndAnnotationName() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotationHolder.setProperty("name", "TestAnnotation");
        ast.setParent(annotationHolder);
        DetailAST child = new DetailAST();
        child.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        child.setText("TestAnnotation");
        child.setProperty("typeid", "TestAnnotationTypeID");
        child.setParent(ast);
        DetailAST firstChild = new DetailAST();
        firstChild.setText("TestAnnotationName");
        firstChild.setType(TokenTypes.IDENT);
        firstChild.setParent(child);
        DetailAST returnValue = AnnotationUtility.getAnnotation(ast, "TestAnnotationName");
        assertNotNull(returnValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, "TestAnnotationName"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotationName() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotationHolder.setProperty("name", "TestAnnotation");
        ast.setParent(annotationHolder);
        DetailAST child = new DetailAST();
        child.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        child.setText("TestAnnotation");
        child.setProperty("typeid", "TestAnnotationTypeID");
        child.setParent(ast);
        DetailAST firstChild = new DetailAST();
        firstChild.setText("TestAnnotationName");
        firstChild.setType(TokenTypes.IDENT);
        firstChild.setParent(child);
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithNullAstAndAnnotationName() {
        assertNull(AnnotationUtility.getAnnotation(null, "TestAnnotationName"));
    }

    @Test
    public void testGetAnnotationWithNotNullAstButNoMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotationHolder.setProperty("name", "TestAnnotation");
        ast.setParent(annotationHolder);
        DetailAST child = new DetailAST();
        child.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        child.setText("TestAnnotation");
        child.setProperty("typeid", "TestAnnotationTypeID");
        child.setParent(ast);
        DetailAST firstChild = new DetailAST();
        firstChild.setText("TestAnnotationDifferentName");
        firstChild.setType(TokenTypes.IDENT);
        firstChild.setParent(child);
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotationDifferentName"));
    }
}