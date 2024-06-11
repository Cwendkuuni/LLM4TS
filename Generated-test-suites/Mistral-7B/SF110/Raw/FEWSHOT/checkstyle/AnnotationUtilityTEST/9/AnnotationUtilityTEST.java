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
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class AnnotationUtilityTest {
    private DetailAST mockAst;
    private DetailAST mockAnnotationHolder;
    private DetailAST mockAnnotation;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        mockAst = mock(DetailAST.class);
        mockAnnotationHolder = mock(DetailAST.class);
        mockAnnotation = mock(DetailAST.class);
    }

    @Test
    public void testContainsAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.branchContains(Mockito.anyInt())).thenReturn(true);

        assertThat(AnnotationUtility.containsAnnotation(mockAst, null)).isFalse();
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.branchContains(Mockito.anyInt())).thenReturn(false);

        assertThat(AnnotationUtility.containsAnnotation(mockAst, "")).isFalse();
    }

    @Test
    public void testContainsAnnotation_AnnotationHolderDoesNotExist() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(null);

        assertThat(AnnotationUtility.containsAnnotation(mockAst, "annotation")).isFalse();
    }

    @Test
    public void testContainsAnnotation_AnnotationExists() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.branchContains(Mockito.anyInt())).thenReturn(true);
        when(getAnnotation(mockAst, "annotation")).thenReturn(mockAnnotation);

        assertThat(AnnotationUtility.containsAnnotation(mockAst, "annotation")).isTrue();
    }

    @Test
    public void testContainsAnnotation_AnnotationDoesNotExist() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);
        when(getAnnotation(mockAst, "annotation")).thenReturn(null);

        assertThat(AnnotationUtility.containsAnnotation(mockAst, "annotation")).isFalse();
    }

    @Test
    public void testGetAnnotationHolder_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationTypeIsAnnotation() {
        when(mockAst.getFirstChild()).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.getType()).thenReturn(TokenTypes.ANNOTATION);

        assertThat(AnnotationUtility.getAnnotationHolder(mockAst)).isEqualTo(mockAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolder_AnnotationTypeIsNotAnnotation() {
        when(mockAst.getFirstChild()).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.getType()).thenReturn(TokenTypes.CLASS_DEF);

        assertNull(AnnotationUtility.getAnnotationHolder(mockAst));
    }

    @Test
    public void testGetAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);

        assertNull(AnnotationUtility.getAnnotation(mockAst, null));
    }

    @Test
    public void testGetAnnotation_AnnotationNameMatches() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.branchContains(Mockito.anyInt())).thenReturn(true);
        when(getAnnotation(mockAst, "annotation")).thenReturn(mockAnnotation);
        when(FullIdent.createFullIdent(mockAnnotation.getNextSibling())).thenReturn(Mockito.mock(FullIdent.class));

        assertThat(AnnotationUtility.getAnnotation(mockAst, "annotation")).isEqualTo(mockAnnotation);
    }

    @Test
    public void testGetAnnotation_AnnotationNameDoesNotMatch() {
        when(mockAst.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(getAnnotationHolder(mockAst)).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.branchContains(Mockito.anyInt())).thenReturn(true);
        when(getAnnotation(mockAst, "annotation")).thenReturn(mockAnnotation);
        when(FullIdent.createFullIdent(mockAnnotation.getNextSibling())).thenReturn(Mockito.mock(FullIdent.class));

        assertNull(AnnotationUtility.getAnnotation(mockAst, "notAnnotation"));
    }

    private DetailAST getAnnotationHolder(DetailAST ast) {
        return AnnotationUtility.getAnnotationHolder(ast);
    }
}