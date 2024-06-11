package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AnnotationUtilityTest {

    private DetailAST ast;
    private DetailAST annotationHolder;
    private DetailAST annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ast = mock(DetailAST.class);
        annotationHolder = mock(DetailAST.class);
        annotation = mock(DetailAST.class);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotationHolder.branchContains(159)).thenReturn(true);
    }

    @Test
    public void testContainsAnnotation_ast_annotation() {
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_ast() {
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolder_astType155() {
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_astType16() {
        when(ast.getType()).thenReturn(TokenTypes.INTERFACE_DEF);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_astTypeOther() {
        when(ast.getType()).thenReturn(TokenTypes.METHOD_DEF);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testContainsAnnotation_astNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_astNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_annotationNull() {
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_annotationEmptyOrSpaces() {
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, ""));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, " "));
    }

    @Test
    public void testGetAnnotation_annotationNotFound() {
        DetailAST child = mock(DetailAST.class);
        DetailAST name = mock(DetailAST.class);
        when(child.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(name.getText()).thenReturn("NotAnnotation");
        when(child.getFirstChild()).thenReturn(name);
        when(annotationHolder.getFirstChild()).thenReturn(child);

        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    private void assertEquals(DetailAST expected, DetailAST result) {
        assertThat(expected).isEqualTo(result);
    }
}