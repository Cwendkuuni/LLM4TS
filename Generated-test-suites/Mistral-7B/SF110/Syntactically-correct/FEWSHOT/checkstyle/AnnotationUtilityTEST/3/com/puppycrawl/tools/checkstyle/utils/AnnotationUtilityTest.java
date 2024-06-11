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
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAst_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotationName"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation_ThrowsException() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation_ThrowsException() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotation_AstWithAnnotation_ReturnsTrue() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getText()).thenReturn("annotationName");

        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotation_AstWithoutAnnotation_ReturnsFalse() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getText()).thenReturn("");

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotationHolder_ThrowsException() {
        DetailAST ast = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderWithoutAnnotation_ReturnsFalse() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getText()).thenReturn("");

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testGetAnnotationHolder_NullAst_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationNotAnnotationType_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationTypeWithAnnotationHolder_ReturnsAnnotationHolder() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);

        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_AnnotationTypeWithoutAnnotationHolder_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_NullAst_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotationName"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);

        DetailAST result = AnnotationUtility.getAnnotation(ast, null);

        assertNull(result);
    }

    @Test
    public void testGetAnnotation_AstWithoutAnnotation_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getText()).thenReturn("");

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");

        assertNull(result);
    }

    @Test
    public void testGetAnnotation_AstWithAnnotation_ReturnsAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getText()).thenReturn("annotationName");

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");

        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotation_AstWithMultipleAnnotations_ReturnsCorrectAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotation1);
        when(annotation1.getText()).thenReturn("annotation1Name");
        when(annotationHolder.getNextSibling()).thenReturn(annotationHolder2);
        DetailAST annotationHolder2 = mock(DetailAST.class);
        when(annotationHolder2.getFirstChild()).thenReturn(annotation2);
        when(annotation2.getText()).thenReturn("annotation2Name");

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation1Name");

        assertEquals(annotation1, result);
    }

    @Test
    public void testGetAnnotation_AstWithMultipleAnnotations_DifferentNames_ReturnsNull() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);

        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getFirstChild()).thenReturn(annotation1);
        when(annotation1.getText()).thenReturn("annotation1Name");
        when(annotationHolder.getNextSibling()).thenReturn(annotationHolder2);
        DetailAST annotationHolder2 = mock(DetailAST.class);
        when(annotationHolder2.getFirstChild()).thenReturn(annotation2);
        when(annotation2.getText()).thenReturn("annotation2Name");

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation2Name");

        assertNull(result);
    }
}