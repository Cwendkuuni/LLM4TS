package com.example;

import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.google.common.base.Joiner;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.mockito.MockitoAnnotations;

public class AnnotationUtilityTest {
    private static final String SAMPLE_ANNOTATION = "sampleAnnotation";
    private static final String EMPTY_ANNOTATION = " ";

    @Test
    public void testContainsAnnotation() {
        DetailAST sampleAst = SampleASTProvider.getSampleAST();

        // Test with valid ast and annotation
        assertTrue(AnnotationUtility.containsAnnotation(sampleAst, SAMPLE_ANNOTATION));

        // Test with null ast
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, SAMPLE_ANNOTATION));

        // Test with null annotation
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(sampleAst, null));

        // Test with empty or whitespace annotation
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(sampleAst, EMPTY_ANNOTATION));
    }

    @Test
    public void testContainsAnnotationWithNullAstThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, SAMPLE_ANNOTATION));
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST sampleAst = mock(DetailAST.class);
        when(sampleAst.branchContains(159)).thenReturn(true);

        assertTrue(AnnotationUtility.containsAnnotation(sampleAst));
    }

    @Test
    public void testGetAnnotationHolderWithNullAstThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST sampleAst = mock(DetailAST.class);
        DetailAST expectedAnnotationHolder;

        when(sampleAst.getType()).thenReturn(155);

        expectedAnnotationHolder = mock(DetailAST.class);
        when(sampleAst.findFirstToken(158)).thenReturn(expectedAnnotationHolder);

        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(sampleAst);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithType16() {
        DetailAST sampleAst = mock(DetailAST.class);
        DetailAST expectedAnnotationHolder;

        when(sampleAst.getType()).thenReturn(16);

        expectedAnnotationHolder = mock(DetailAST.class);
        when(sampleAst.findFirstToken(5)).thenReturn(expectedAnnotationHolder);

        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(sampleAst);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationWithNullAstThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, SAMPLE_ANNOTATION));
    }

    @Test
    public void testGetAnnotationWithNullAstAndAnnotationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, null));
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotationThrowsException() {
        DetailAST sampleAst = mock(DetailAST.class);

        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(sampleAst, EMPTY_ANNOTATION));
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST sampleAst = mock(DetailAST.class);
        DetailAST annotation = mock(DetailAST.class);
        DetailAST annotationHolder;

        when(sampleAst.getFirstChild()).thenReturn(annotation);

        String sampleAnnotation = SAMPLE_ANNOTATION;
        DetailAST expectedAnnotation = mock(DetailAST.class);
        when(expectedAnnotation.getText()).thenReturn(sampleAnnotation);
        when(annotation.getType()).thenReturn(159);
        when(annotation.getFirstChild()).thenReturn(sampleAst);

        annotationHolder = AnnotationUtility.getAnnotationHolder(sampleAst);
        when(annotationHolder.branchContains(159)).thenReturn(true);

        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(sampleAst, sampleAnnotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }
}