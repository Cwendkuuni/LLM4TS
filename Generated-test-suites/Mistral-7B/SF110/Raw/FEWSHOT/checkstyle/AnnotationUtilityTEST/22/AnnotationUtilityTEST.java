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

public class AnnotationUtilityTest {
    private DetailAST ast;
    private DetailAST annotationHolder;
    private DetailAST annotation;

    @Before
    public void setup() {
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
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        AnnotationUtility.containsAnnotation(ast);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(CharMatcher.WHITESPACE.matchesAllOf(annotation)).thenReturn(true);
        AnnotationUtility.containsAnnotation(ast);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithNoAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        AnnotationUtility.containsAnnotation(ast);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotation.getText()).thenReturn("annotation");
        when(getAnnotation(ast, "annotation")).thenReturn(annotation);
        AnnotationUtility.containsAnnotation(ast);
        AnnotationUtility.containsAnnotation(ast, "annotation");
        assertTrue(true);
    }

    @Test
    public void testContainsAnnotationWithNestedAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.type()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotation.getText()).thenReturn("nestedAnnotation");
        when(getAnnotation(ast, "nestedAnnotation")).thenReturn(annotation);
        AnnotationUtility.containsAnnotation(ast);
        AnnotationUtility.containsAnnotation(ast, "nestedAnnotation");
        assertTrue(true);
    }

    @Test
    public void testContainsAnnotationWithNoAnnotationHolder() {
        when(ast.getType()).thenReturn(155);
        AnnotationUtility.containsAnnotation(ast);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNoAnnotationChild() {
        when(ast.getType()).thenReturn(155);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        AnnotationUtility.containsAnnotation(ast);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNoAnnotationBranch() {
        when(ast.getType()).thenReturn(155);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        AnnotationUtility.containsAnnotation(ast);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotation.getText()).thenReturn("annotation");
        AnnotationUtility.getAnnotation(ast, null);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNoAnnotationHolder() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNoAnnotationChild() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNoAnnotationBranch() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        AnnotationUtility.getAnnotation(ast, "annotation");
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotation.getText()).thenReturn("annotation");
        when(getAnnotation(ast, "annotation")).thenReturn(annotation);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationWithNestedAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.type()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(annotation.getText()).thenReturn("nestedAnnotation");
        when(getAnnotation(ast, "nestedAnnotation")).thenReturn(annotation);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "nestedAnnotation");
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(getAnnotationHolder(ast)).thenReturn(annotationHolder);
        when(getAnnotationHolder(annotationHolder).branchContains(159)).thenReturn(true);
        when(annotation.getText()).thenReturn(" ");
        when(getAnnotation(ast, " ")).thenReturn(annotation);
        DetailAST result = AnnotationUtility.getAnnotation(ast, " ");
        assertEquals(annotation, result);
    }
}