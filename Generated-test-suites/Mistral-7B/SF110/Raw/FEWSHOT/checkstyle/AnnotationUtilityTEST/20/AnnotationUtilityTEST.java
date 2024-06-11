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
import org.mockito.MockitoAnnotations;

public class AnnotationUtilityTest {

    private DetailAST ast;
    private DetailAST annotationHolder;
    private DetailAST annotation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ast = mock(DetailAST.class);
        annotationHolder = mock(DetailAST.class);
        annotation = mock(DetailAST.class);

        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation.getText()).thenReturn("MyAnnotation");
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(ast.findFirstToken(TokenTypes.ANNOTATION)).thenReturn(annotationHolder);
        when(ast.findFirstToken(TokenTypes.ANNOTATION)).thenReturn(annotationHolder);
        when(ast.findFirstToken(5)).thenReturn(annotationHolder);
        when(ast.findFirstToken(158)).thenReturn(annotationHolder);
    }

    @Test
    public void testContainsAnnotation_whenAstAndAnnotationPresent_thenReturnTrue() {
        when(ast.getType()).thenReturn(TokenTypes.PROGRAM);

        final boolean result = AnnotationUtility.containsAnnotation(ast, "MyAnnotation");

        assertThat(result).isTrue();
    }

    @Test
    public void testContainsAnnotation_whenAstIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotation_whenAnnotationIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_whenAstContainsAnnotation_thenReturnTrue() {
        when(ast.getType()).thenReturn(TokenTypes.METHOD_DECL);

        final boolean result = AnnotationUtility.containsAnnotation(ast);

        assertThat(result).isTrue();
    }

    @Test
    public void testContainsAnnotation_whenAstDoesNotContainAnnotation_thenReturnFalse() {
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DECL);

        final boolean result = AnnotationUtility.containsAnnotation(ast);

        assertThat(result).isFalse();
    }

    @Test
    public void testGetAnnotationHolder_whenAstIsMethodDeclaration_thenReturnCorrectAnnotationHolder() {
        when(ast.getType()).thenReturn(TokenTypes.METHOD_DECL);

        final DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertThat(result).isSameAs(annotationHolder);
    }

    @Test
    public void testGetAnnotationHolder_whenAstIsClassDeclaration_thenReturnCorrectAnnotationHolder() {
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DECL);

        final DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertNull(result);
    }

    @Test
    public void testGetAnnotation_whenAstAndAnnotationPresent_thenReturnCorrectAnnotation() {
        when(ast.getType()).thenReturn(TokenTypes.METHOD_DECL);

        final DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");

        assertThat(result).isSameAs(annotation);
    }

    @Test
    public void testGetAnnotation_whenAstIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotation_whenAnnotationIsNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_whenAnnotationDoesNotExistInAst_thenReturnNull() {
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DECL);

        final DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");

        assertNull(result);
    }

    @Test
    public void testGetAnnotation_whenThereIsNoAnnotationHolder_thenReturnNull() {
        when(ast.getType()).thenReturn(TokenTypes.PROGRAM);

        final DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");

        assertNull(result);
    }
}