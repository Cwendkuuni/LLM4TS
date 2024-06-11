package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, ""));
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, " \t\r\n"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getText()).thenReturn("annotation");
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        final boolean result = AnnotationUtility.containsAnnotation(ast, "annotation");
        assertThat(result).isTrue();
    }

    @Test
    public void testContainsAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingMultipleAnnotations() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST firstAnnotation = mock(DetailAST.class);
        DetailAST secondAnnotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(firstAnnotation, secondAnnotation);
        when(firstAnnotation.getText()).thenReturn("annotation1");
        when(secondAnnotation.getText()).thenReturn("annotation2");

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation3"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation1"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation2"));
    }

    @Test
    public void testContainsAnnotationHolder() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderWithClassDefAST() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithAnnotationAST() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotationName() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getText()).thenReturn("");

        assertNull(AnnotationUtility.getAnnotation(ast, ""));
        assertNull(AnnotationUtility.getAnnotation(ast, " \t\r\n"));
    }

    @Test
    public void testGetAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithAstContainingAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getText()).thenReturn("annotation");
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationWithAstContainingMultipleAnnotations() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST firstAnnotation = mock(DetailAST.class);
        DetailAST secondAnnotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(firstAnnotation, secondAnnotation);
        when(firstAnnotation.getText()).thenReturn("annotation1");
        when(secondAnnotation.getText()).thenReturn("annotation2");

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation1");
        assertEquals(firstAnnotation, result);

        assertNull(AnnotationUtility.getAnnotation(ast, "annotation3"));
    }
}