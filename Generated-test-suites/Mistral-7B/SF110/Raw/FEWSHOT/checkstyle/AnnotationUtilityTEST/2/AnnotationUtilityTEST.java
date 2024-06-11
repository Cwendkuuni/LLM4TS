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
import org.mockito.ArgumentCaptor;

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
    public void testContainsAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, " "));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolder() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        assertThat(AnnotationUtility.containsAnnotation(ast)).isTrue();
    }

    @Test
    public void testContainsAnnotationWithNoAnnotationHolder() {
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, ""));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, " "));
    }

    @Test
    public void testGetAnnotationWithNoAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(null);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithMatchingAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation.getNextSibling()).thenReturn(null);
        when(FullIdent.createFullIdent(annotation).getText()).thenReturn("annotation");
        ArgumentCaptor<String> annotationCapture = ArgumentCaptor.forClass(String.class);
        AnnotationUtility.getAnnotation(ast, "annotation");
        verify(ast).findFirstToken(annotationCapture.capture());
        assertThat(annotationCapture.getValue()).isEqualTo("annotation");
    }
}