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

    private DetailAST mockAST;

    @Before
    public void setup() {
        mockAST = mock(DetailAST.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContainsAnnotation_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockAST, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockAST, ""));
    }

    @Test
    public void testContainsAnnotation_NullAnnotationHolder() {
        when(mockAST.findFirstToken(5)).thenReturn(null);
        assertFalse(AnnotationUtility.containsAnnotation(mockAST, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NonAnnotationHolder() {
        when(mockAST.findFirstToken(5)).thenReturn(mockAST);
        assertFalse(AnnotationUtility.containsAnnotation(mockAST, "annotation"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderWithAnnotation() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        when(mockAST.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation.getFirstChild()).thenReturn(mock(DetailAST.class));
        when(mockAST.getType()).thenReturn(TokenTypes.CLASS_DEF);
        assertTrue(AnnotationUtility.containsAnnotation(mockAST, "annotation"));
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderWithoutAnnotation() {
        DetailAST annotationHolder = mock(DetailAST.class);
        when(mockAST.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(false);
        assertFalse(AnnotationUtility.containsAnnotation(mockAST, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(mockAST, null));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(mockAST, ""));
    }

    @Test
    public void testGetAnnotation_NullAnnotationHolder() {
        when(mockAST.findFirstToken(5)).thenReturn(null);
        assertNull(AnnotationUtility.getAnnotation(mockAST, "annotation"));
    }

    @Test
    public void testGetAnnotation_NonAnnotationHolder() {
        DetailAST annotationHolder = mock(DetailAST.class);
        when(mockAST.findFirstToken(5)).thenReturn(annotationHolder);
        assertNull(AnnotationUtility.getAnnotation(mockAST, "annotation"));
    }

    @Test
    public void testGetAnnotation_AnnotationWithMatchingName() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        DetailAST name = mock(DetailAST.class);
        when(mockAST.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotation.getFirstChild()).thenReturn(name);
        when(name.getText()).thenReturn("annotation");
        when(mockAST.getType()).thenReturn(TokenTypes.CLASS_DEF);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        DetailAST result = AnnotationUtility.getAnnotation(mockAST, "annotation");
        assertThat(result).isEqualTo(annotation);
    }

    @Test
    public void testGetAnnotation_AnnotationWithMismatchingName() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        DetailAST name = mock(DetailAST.class);
        when(mockAST.findFirstToken(5)).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(annotation.getFirstChild()).thenReturn(name);
        when(name.getText()).thenReturn("otherAnnotation");
        when(mockAST.getType()).thenReturn(TokenTypes.CLASS_DEF);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        assertNull(AnnotationUtility.getAnnotation(mockAST, "annotation"));
    }
}