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
import org.mockito.Mockito;

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
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithAstWithoutAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.COMMENT);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getName()).thenReturn("annotation");
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        DetailAST firstChild = mock(DetailAST.class);
        when(firstChild.getText()).thenReturn("annotationName");
        when(annotation.getFirstChild()).thenReturn(firstChild);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingOtherAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder1 = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder1.getName()).thenReturn("annotation1");
        when(annotation1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation1.getName()).thenReturn("annotation2");
        DetailAST annotationHolder2 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);
        when(annotationHolder2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder2.getName()).thenReturn("annotation");
        when(annotation2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation2.getName()).thenReturn("annotationName");
        when(annotationHolder1.getFirstChild()).thenReturn(annotation2);
        when(annotationHolder2.getFirstChild()).thenReturn(Mockito.any(DetailAST.class));

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation1"));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation2"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingMultipleAnnotations() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder1 = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder1.getName()).thenReturn("annotation1");
        when(annotation1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation1.getName()).thenReturn("annotation2");
        DetailAST annotationHolder2 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);
        when(annotationHolder2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder2.getName()).thenReturn("annotation");
        when(annotation2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation2.getName()).thenReturn("annotationName");
        when(annotationHolder1.getFirstChild()).thenReturn(annotation2);
        when(annotationHolder2.getFirstChild()).thenReturn(Mockito.any(DetailAST.class));

        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderWithAstNotAnAnnotationDefinition() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.COMMENT);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithAstWithSingleAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(ast.getFirstToken(6)).thenReturn(annotationHolder);

        DetailAST expectedResult = annotationHolder;
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testGetAnnotationHolderWithAstWithMultipleAnnotations() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder1 = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        DetailAST annotationHolder2 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder1.getName()).thenReturn("annotation1");
        when(annotation1.getName()).thenReturn("annotation2");
        when(annotationHolder2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder2.getName()).thenReturn("annotation");
        when(annotation2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation2.getName()).thenReturn("annotationName");
        when(ast.getFirstChild()).thenReturn(annotationHolder1);
        when(annotationHolder1.getNextSibling()).thenReturn(annotationHolder2);

        DetailAST expectedResult = annotationHolder2;
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotationName() {
        DetailAST ast = mock(DetailAST.class);
        assertThrows(IllegalArgumentException.class, () ->
                AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithAstWithoutAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.COMMENT);
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithAstContainingAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder.getName()).thenReturn("annotation");
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation.getName()).thenReturn("annotationName");
        when(annotationHolder.getFirstChild()).thenReturn(annotation);

        DetailAST expectedResult = annotation;
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testGetAnnotationWithAstContainingOtherAnnotation() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder1 = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder1.getName()).thenReturn("annotation1");
        when(annotation1.getName()).thenReturn("annotation2");
        DetailAST annotationHolder2 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);
        when(annotationHolder2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder2.getName()).thenReturn("annotation");
        when(annotation2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation2.getName()).thenReturn("annotationName");
        when(annotationHolder1.getFirstChild()).thenReturn(annotation2);

        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation1");
        assertThat(result).isNull();

        expectedResult = annotation;
        result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testGetAnnotationWithAstContainingMultipleAnnotations() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder1 = mock(DetailAST.class);
        DetailAST annotation1 = mock(DetailAST.class);
        when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
        when(annotationHolder1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation1.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder1.getName()).thenReturn("annotation1");
        when(annotation1.getName()).thenReturn("annotation2");
        DetailAST annotationHolder2 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);
        when(annotationHolder2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotationHolder2.getName()).thenReturn("annotation");
        when(annotation2.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation2.getName()).thenReturn("annotationName");
        when(annotationHolder1.getFirstChild()).thenReturn(annotation2);

        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotation1");
        assertThat(result).isNull();

        expectedResult = annotation;
        result = AnnotationUtility.getAnnotation(ast, "annotation");
        assertThat(result).isEqualTo(expectedResult);
    }
}