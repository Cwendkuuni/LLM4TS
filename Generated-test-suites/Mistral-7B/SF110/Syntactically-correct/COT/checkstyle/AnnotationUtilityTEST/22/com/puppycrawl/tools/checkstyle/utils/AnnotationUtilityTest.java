package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithAstAndAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = "AnnotationTest";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationTest";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithAst() {
        DetailAST ast = createMockDetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithAst() {
        DetailAST ast = createMockDetailAST();
        DetailAST expected = createMockDetailAST(5);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithAstAndAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = "AnnotationTest";
        DetailAST expected = createMockDetailAST(159, "AnnotationTest");
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationTest";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithAstAndNonExistentAnnotation() {
        DetailAST ast = createMockDetailAST();
        String annotation = "NonExistentAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    private DetailAST createMockDetailAST() {
        return new DetailAST() {};
    }

    private DetailAST createMockDetailAST(int type) {
        DetailAST ast = new DetailAST() {};
        ast.setType(type);
        return ast;
    }

    private DetailAST createMockDetailAST(int type, String text) {
        DetailAST ast = new DetailAST() {};
        ast.setType(type);
        ast.setText(text);
        return ast;
    }

}