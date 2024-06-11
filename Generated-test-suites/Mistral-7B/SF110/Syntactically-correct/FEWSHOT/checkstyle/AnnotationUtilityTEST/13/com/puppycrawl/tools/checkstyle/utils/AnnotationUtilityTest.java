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

    private DetailAST sampleAst;
    private DetailAST annotationHolder;
    private DetailAST annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sampleAst = mock(DetailAST.class);
        annotationHolder = mock(DetailAST.class);
        annotation = mock(DetailAST.class);
    }

    @Test
    public void testContainsAnnotation_astNotNullAndAnnotationPresent() {
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation.getFirstChild()).thenReturn(mock(DetailAST.class));

        when(sampleAst.findFirstToken(TokenTypes.ANNOTATION)).thenReturn(annotationHolder);

        assertThat(AnnotationUtility.containsAnnotation(sampleAst, "testAnnotation")).isTrue();
    }

    @Test
    public void testContainsAnnotation_astNotNullAndAnnotationNotPresent() {
        when(annotationHolder.getFirstChild()).thenReturn(annotation);
        when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(annotation.getFirstChild()).thenReturn(mock(DetailAST.class));

        when(sampleAst.findFirstToken(TokenTypes.ANNOTATION)).thenReturn(annotationHolder);

        assertThat(AnnotationUtility.containsAnnotation(sampleAst, "otherAnnotation")).isFalse();
    }

    @Test
    public void testContainsAnnotation_astNotNull() {
        when(sampleAst.findFirstToken(TokenTypes.ANNOTATION)).thenReturn(annotationHolder);

        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(sampleAst(), null));
    }

    @Test
    public void testContainsAnnotation_astNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, ""));
    }

    @Test
    public void testGetAnnotationHolder_astTypeIsAnnotation() {
        when(sampleAst.getType()).thenReturn(TokenTypes.ANNOTATION);

        DetailAST result = AnnotationUtility.getAnnotationHolder(sampleAst());

        assertThat(result).isNotNull();
    }

    @Test
    public void testGetAnnotationHolder_astTypeIsNotAnnotation() {
        when(sampleAst.getType()).thenReturn(TokenTypes.METHOD_CALL);

        assertNull(AnnotationUtility.getAnnotationHolder(sampleAst()));
    }

    @Test
    public void testGetAnnotationHolder_astNull() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotation_astNotNullAndAnnotationPresent() {
        when(annotation.getText()).thenReturn("testAnnotation");
        when(annotationHolder.getFirstChild()).thenReturn(annotation);

        DetailAST result = AnnotationUtility.getAnnotation(sampleAst(), "testAnnotation");

        assertThat(result).isEqualTo(annotation);
    }

    @Test
    public void testGetAnnotation_astNotNullAndAnnotationNotPresent() {
        when(annotationHolder.getFirstChild()).thenReturn(annotation);

        assertNull(AnnotationUtility.getAnnotation(sampleAst(), "otherAnnotation"));
    }

    @Test
    public void testGetAnnotation_astNotNull() {
        when(sampleAst.findFirstToken(TokenTypes.ANNOTATION)).thenReturn(annotationHolder);

        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(sampleAst(), null));
    }

    @Test
    public void testGetAnnotation_astNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, ""));
    }

    @Test
    public void testGetAnnotation_annotationTextIsEmptyOrWhitespace() {
        when(annotation.getText()).thenReturn("");

        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(sampleAst(), ""));
    }

    @Test
    public void testGetAnnotation_annotationTextIsEmptyOrWhitespace_nullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, ""));
    }

    private DetailAST sampleAst() {
        return sampleAst;
    }
}