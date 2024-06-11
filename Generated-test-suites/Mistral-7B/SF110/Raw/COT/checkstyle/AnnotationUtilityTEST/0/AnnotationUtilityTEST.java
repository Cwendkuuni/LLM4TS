package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @org.junit.Test
    public void testContainsAnnotationWithNotNullAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast, "annotationName");
    }

    @org.junit.Test
    public void testContainsAnnotationWithEmptyAst() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @org.junit.Test
    public void testContainsAnnotationWithNotNullAst() {
        DetailAST ast = new DetailAST();
        ast.setType(1); // Arbitrary non-null type
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @org.junit.Test
    public void testGetAnnotationHolderWithNotNullAst() {
        DetailAST ast = new DetailAST();
        DetailAST expected = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @org.junit.Test
    public void testGetAnnotationWithNotNullAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
        assertEquals(annotation, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotation(ast, "annotationName");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, "");
    }

    @org.junit.Test
    public void testGetAnnotationWithNoMatchingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "MismatchedAnnotation");
        assertNull(result);
    }
}