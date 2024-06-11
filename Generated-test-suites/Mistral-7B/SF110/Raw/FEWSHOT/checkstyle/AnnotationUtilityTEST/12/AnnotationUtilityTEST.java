package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilityTest {

    private DetailAST ast;
    private DetailAST annotationHolder;
    private DetailAST annotation;

    @Before
    public void setUp() {
        ast = mock(DetailAST.class);
        annotationHolder = mock(DetailAST.class);
        annotation = mock(DetailAST.class);
    }

    @Test
    public void testContainsAnnotation_NullAst_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation_ThrowsIllegalArgumentException() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getText()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotation_NoAnnotation_ReturnsFalse() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.CLASS_DEF);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderContainingAnnotation_ReturnsTrue() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(getAnnotation(annotationHolder, "annotation")).thenReturn(annotation);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderNotContainingAnnotation_ReturnsFalse() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationHolder_NullAst_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_ClassDefAst_ReturnsNull() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.CLASS_DEF);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationAst_ReturnsAnnotationHolder() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        assertEquals(annotationHolder, AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_NullAst_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation_ReturnsNull() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getText()).thenReturn("annotation");
        when(annotation.getText()).thenReturn("type");
        when(annotation.getNextSibling()).thenReturn(null);
        when(annotation.getPreviousSibling()).thenReturn(null);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        assertNull(AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_AnnotationNotFound_ReturnsNull() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getText()).thenReturn("annotation");
        when(getAnnotationHolder(annotationHolder)).thenReturn(null);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_AnnotationFound_ReturnsAnnotation() {
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getText()).thenReturn("annotation");
        when(getAnnotationHolder(annotationHolder)).thenReturn(annotationHolder);
        when(annotation.getText()).thenReturn("type");
        when(annotation.getNextSibling()).thenReturn(null);
        when(annotation.getPreviousSibling()).thenReturn(null);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        assertEquals(annotation, AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    private DetailAST getAnnotationHolder(DetailAST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("the ast is null");
        }
        DetailAST annotationHolder;
        if (ast.getType() == TokenTypes.ANNOTATION || ast.getType() == TokenTypes.TYPE_DEF) {
            annotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
        } else {
            annotationHolder = ast.findFirstToken(TokenTypes.TYPE_DEF);
        }
        return annotationHolder;
    }
}