package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilityTest {

    private DetailAST mockAst;

    @Before
    public void setup() {
        mockAst = mock(DetailAST.class);
    }

    @Test
    public void testContainsAnnotationNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "Annotation"));
    }

    @Test
    public void testContainsAnnotationNullAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockAst, null));
    }

    @Test
    public void testContainsAnnotationEmptyAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockAst, ""));
    }

    @Test
    public void testContainsAnnotationNoAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(mockAst, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST annotationHolder = mockAst;
        DetailAST annotation = mock(DetailAST.class);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);

        assertTrue(AnnotationUtility.containsAnnotation(mockAst, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithInnerAnnotation() {
        DetailAST annotationHolder = mockAst;
        DetailAST annotationHolderToken = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(annotationHolderToken.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotationHolderToken);
        when(annotationHolderToken.getFirstChild()).thenReturn(annotation);

        assertTrue(AnnotationUtility.containsAnnotation(mockAst, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithNotAnnotation() {
        DetailAST annotationHolder = mockAst;
        DetailAST notAnnotation = mock(DetailAST.class);
        when(annotationHolder.getFirstChild()).thenReturn(notAnnotation);

        assertFalse(AnnotationUtility.containsAnnotation(mockAst, "Annotation"));
    }

    @Test
    public void testGetAnnotationNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "Annotation"));
    }

    @Test
    public void testGetAnnotationNullAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(mockAst, null));
    }

    @Test
    public void testGetAnnotationEmptyAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(mockAst, ""));
    }

    @Test
    public void testGetAnnotationNoAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(mockAst, "Annotation"));
    }

    @Test
    public void testGetAnnotationWithAnnotation() {
        DetailAST annotationHolder = mockAst;
        DetailAST annotation = mock(DetailAST.class);
        DetailAST annotationName = mock(DetailAST.class);
        String annotationNameText = "Annotation";
        when(annotationName.getType()).thenReturn(TokenTypes.IDENT);
        when(annotationName.getText()).thenReturn(annotationNameText);
        when(annotation.getFirstChild()).thenReturn(annotationName);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);

        DetailAST result = AnnotationUtility.getAnnotation(mockAst, "Annotation");
        assertThat(result).isEqualTo(annotation);
    }

    @Test
    public void testGetAnnotationWithNotAnnotation() {
        DetailAST annotationHolder = mockAst;
        DetailAST notAnnotation = mock(DetailAST.class);
        when(annotationHolder.getFirstChild()).thenReturn(notAnnotation);

        assertNull(AnnotationUtility.getAnnotation(mockAst, "Annotation"));
    }

    @Test
    public void testGetAnnotationHolderNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderIsComment() {
        DetailAST comment = mock(DetailAST.class);
        when(comment.getType()).thenReturn(TokenTypes.COMMENT);

        assertNull(AnnotationUtility.getAnnotationHolder(comment));
    }

    @Test
    public void testGetAnnotationHolderIsAnnotation() {
        DetailAST annotation = mock(DetailAST.class);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);

        DetailAST result = AnnotationUtility.getAnnotationHolder(annotation);
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationHolderIsToken() {
        DetailAST token = mock(DetailAST.class);
        when(token.getType()).thenReturn(TokenTypes.CLASS_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(token));
    }

    @Test
    public void testGetAnnotationHolderIsClassDeclaration() {
        DetailAST classDeclaration = mock(DetailAST.class);
        when(classDeclaration.getType()).thenReturn(TokenTypes.CLASS_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(classDeclaration));
    }

    @Test
    public void testGetAnnotationHolderIsMethodDeclaration() {
        DetailAST methodDeclaration = mock(DetailAST.class);
        when(methodDeclaration.getType()).thenReturn(TokenTypes.METHOD_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(methodDeclaration));
    }

    @Test
    public void testGetAnnotationHolderIsVariableDeclaration() {
        DetailAST variableDeclaration = mock(DetailAST.class);
        when(variableDeclaration.getType()).thenReturn(TokenTypes.VAR_DECL);

        assertNull(AnnotationUtility.getAnnotationHolder(variableDeclaration));
    }

    @Test
    public void testGetAnnotationHolderIsInterfaceDeclaration() {
        DetailAST interfaceDeclaration = mock(DetailAST.class);
        when(interfaceDeclaration.getType()).thenReturn(TokenTypes.INTERFACE_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(interfaceDeclaration));
    }

    @Test
    public void testGetAnnotationHolderIsAnnotation() {
        DetailAST annotation = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);

        DetailAST result = AnnotationUtility.getAnnotationHolder(annotation);
        assertEquals(annotationHolder, result);
    }
}