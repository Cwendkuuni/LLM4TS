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
    private DetailAST mockAnnotationHolder;
    private DetailAST mockAnnotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockAST = mock(DetailAST.class);
        mockAnnotationHolder = mock(DetailAST.class);
        mockAnnotation = mock(DetailAST.class);

        when(mockAnnotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(mockAnnotationHolder.getFirstChild()).thenReturn(mockAnnotation);
    }

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        assertThat(AnnotationUtility.containsAnnotation(mockAST, "validAnnotation")).isTrue();
    }

    @Test
    public void testContainsAnnotationWithValidASTAndNullAnnotation() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        assertNull(AnnotationUtility.containsAnnotation(mockAST, null));
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        when(mockAnnotationHolder.branchContains(159)).thenReturn(true);
        assertThat(AnnotationUtility.containsAnnotation(mockAST)).isTrue();
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockAST, null));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        when(mockAST.getType()).thenReturn(TokenTypes.METHOD_DEF);
        when(mockAST.findFirstToken(158)).thenReturn(mockAnnotationHolder);
        assertThat(AnnotationUtility.getAnnotationHolder(mockAST)).isEqualTo(mockAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithInvalidASTType() {
        when(mockAST.getType()).thenReturn(TokenTypes.CLASS_DEF);
        assertNull(AnnotationUtility.getAnnotationHolder(mockAST));
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        DetailAST expectedAnnotation = mock(DetailAST.class);
        when(mockAnnotationHolder.getFirstChild()).thenReturn(expectedAnnotation);
        when(expectedAnnotation.getType()).thenReturn(TokenTypes.ANNOTATION);
        when(expectedAnnotation.getFirstChild()).thenReturn(mockAnnotation);
        String expectedAnnotationName = "expectedAnnotationName";
        when(FullIdent.createFullIdent(mockAnnotation.getNextSibling())).thenReturn(mock(StringValueToken.class).withGetText(() -> expectedAnnotationName));
        DetailAST result = AnnotationUtility.getAnnotation(mockAST, expectedAnnotationName);
        assertThat(result).isEqualTo(expectedAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidASTAndNullAnnotationName() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        assertNull(AnnotationUtility.getAnnotation(mockAST, null));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotationName"));
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotationName() {
        when(mockAST.getFirstChild()).thenReturn(mockAnnotationHolder);
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationUtility.getAnnotation(mockAST, "   "));
    }
}