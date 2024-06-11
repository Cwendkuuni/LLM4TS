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
import org.mockito.Mockito;

public final class AnnotationUtilityTest {

    private DetailAST astMock;

    @Before
    public void setUp() {
        astMock = mock(DetailAST.class);
    }

    @Test
    public void testContainsAnnotation_NullAST_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(null, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(astMock, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.containsAnnotation(astMock, ""));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderIsEmpty_ReturnsFalse() {
        when(astMock.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(astMock.getFirstChild()).thenReturn(null);
        assertFalse(AnnotationUtility.containsAnnotation(astMock, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderContainsAnnotation_ReturnsTrue() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(astMock.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(astMock.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotation.getText()).thenReturn("AnnotationName");
        assertTrue(AnnotationUtility.containsAnnotation(astMock, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderDoesNotContainAnnotation_ReturnsFalse() {
        DetailAST annotationHolder = mock(DetailAST.class);
        when(astMock.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(astMock.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        assertFalse(AnnotationUtility.containsAnnotation(astMock, "AnnotationName"));
    }

    @Test
    public void testGetAnnotationHolder_NullAST_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_ASTWithAnnotationType_ReturnsAnnotationHolder() {
        DetailAST annotationHolder = mock(DetailAST.class);
        when(astMock.getType()).thenReturn(TokenTypes.CLASS_DEF);
        when(astMock.findFirstToken(158)).thenReturn(annotationHolder);
        DetailAST expectedAnnotationHolder = AnnotationUtility.getAnnotationHolder(astMock);
        assertEquals(annotationHolder, expectedAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolder_ASTWithoutAnnotationType_ReturnsFirstToken() {
        DetailAST firstToken = mock(DetailAST.class);
        when(astMock.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(astMock.findFirstToken(5)).thenReturn(firstToken);
        DetailAST expectedAnnotationHolder = AnnotationUtility.getAnnotationHolder(astMock);
        assertEquals(firstToken, expectedAnnotationHolder);
    }

    @Test
    public void testGetAnnotation_NullAST_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotation(null, "AnnotationName"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(mock(DetailAST.class));
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotationName_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(mock(DetailAST.class));
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotation_AnnotationHolderContainsAnnotation_ReturnsAnnotation() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        DetailAST annotationName = mock(DetailAST.class);
        when(astMock.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(astMock.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotationHolder.getFirstChild()).thenReturn(annotationName);
        when(annotationName.getText()).thenReturn("AnnotationName");
        DetailAST expectedAnnotation = AnnotationUtility.getAnnotation(astMock, "AnnotationName");
        assertEquals(annotation, expectedAnnotation);
    }

    @Test
    public void testGetAnnotation_AnnotationHolderDoesNotContainAnnotation_ReturnsNull() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST firstChild = mock(DetailAST.class);
        DetailAST annotationName = mock(DetailAST.class);
        when(astMock.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(astMock.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        when(annotationHolder.getFirstChild()).thenReturn(firstChild);
        assertNull(AnnotationUtility.getAnnotation(astMock, "AnnotationName"));
    }
}