package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithAstAndAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstForSecondMethod() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        DetailAST expectedAnnotationHolder = MockASTFactory.createMockDetailAST();
        when(ast.getType()).thenReturn(155);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, holder);
    }

    @Test
    public void testGetAnnotationHolderWithType16Ast() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        DetailAST expectedAnnotationHolder = MockASTFactory.createMockDetailAST();
        when(ast.getType()).thenReturn(16);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, holder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = "AnnotationName";
        DetailAST expectedAnnotation = MockASTFactory.createMockDetailAST();
        DetailAST annotationHolder = MockASTFactory.createMockDetailAST();
        DetailAST annotationNode = MockASTFactory.createMockDetailAST();
        when(ast.getType()).thenReturn(155);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode);
        when(annotationNode.getText()).thenReturn(annotation);
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndDifferentAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = "AnnotationName1";
        String annotation2 = "AnnotationName2";
        DetailAST expectedAnnotation = MockASTFactory.createMockDetailAST();
        DetailAST annotationHolder = MockASTFactory.createMockDetailAST();
        DetailAST annotationNode1 = MockASTFactory.createMockDetailAST();
        DetailAST annotationNode2 = MockASTFactory.createMockDetailAST();
        when(ast.getType()).thenReturn(155);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode1);
        when(annotationNode1.getText()).thenReturn(annotation2);
        when(annotationHolder.getNextSibling()).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode2);
        when(annotationNode2.getText()).thenReturn(annotation);
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNoMatchingAnnotation() {
        DetailAST ast = MockASTFactory.createMockDetailAST();
        String annotation = "AnnotationName";
        DetailAST annotationHolder = MockASTFactory.createMockDetailAST();
        DetailAST annotationNode = MockASTFactory.createMockDetailAST();
        when(ast.getType()).thenReturn(155);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode);
        when(annotationNode.getText()).thenReturn("AnnotationName2");
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    private static DetailAST getAnnotationHolder(DetailAST ast) {
        return AnnotationUtility.getAnnotationHolder(ast);
    }
}