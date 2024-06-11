package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnnotationUtility.class)
public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_astNotNullAndAnnotationPresent() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);
        DetailAST annotation = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(annotationHolder.getFirstChild()).thenReturn(annotation);

        PowerMockito.when(annotation.getType()).thenReturn(159);
        PowerMockito.when(annotation.getFirstChild()).thenReturn(PowerMockito.mock(DetailAST.class));
        PowerMockito.when(annotation.getFirstChild().getText()).thenReturn("annotationName");

        PowerMockito.when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotation_astNotNullAndAnnotationNotPresent() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(annotationHolder.getFirstChild()).thenReturn(PowerMockito.mock(DetailAST.class));

        PowerMockito.when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        assertFalse(AnnotationUtility.containsAnnotation(ast, "notAnnotationName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_astNull() {
        AnnotationUtility.containsAnnotation(null, "annotationName");
    }

    @Test
    public void testContainsAnnotation_astNotNullAndNoAnnotation() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(annotationHolder.getFirstChild()).thenReturn(null);

        PowerMockito.when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testGetAnnotationHolder_astNotNullAndHasAnnotationType() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolder_astNotNullAndNoAnnotationType() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(158)).thenReturn(null);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_astNull() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotation_astNotNullAndAnnotationPresent() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);
        DetailAST annotation = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(annotationHolder.getFirstChild()).thenReturn(annotation);

        PowerMockito.when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        PowerMockito.when(annotation.getType()).thenReturn(159);
        PowerMockito.when(annotation.getFirstChild()).thenReturn(PowerMockito.mock(DetailAST.class));
        PowerMockito.when(annotation.getFirstChild().getText()).thenReturn("annotationName");

        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotation_astNotNullAndAnnotationNotPresent() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);
        DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);

        PowerMockito.whenNew(DetailAST.class).withArguments(155).thenReturn(annotationHolder);
        PowerMockito.when(annotationHolder.getFirstChild()).thenReturn(PowerMockito.mock(DetailAST.class));

        PowerMockito.when(ast.findFirstToken(158)).thenReturn(annotationHolder);
        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        PowerMockito.when(ast.findFirstToken(16)).thenReturn(PowerMockito.mock(DetailAST.class));

        assertNull(AnnotationUtility.getAnnotation(ast, "annotationName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_astNull() {
        AnnotationUtility.getAnnotation(null, "annotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_annotationNull() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);

        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotation_annotationNameIsEmptyOrWhitespace() {
        DetailAST ast = PowerMockito.mock(DetailAST.class);

        PowerMockito.when(ast.findFirstToken(5)).thenReturn(PowerMockito.mock(DetailAST.class));

        assertNull(AnnotationUtility.getAnnotation(ast, ""));
        assertNull(AnnotationUtility.getAnnotation(ast, " "));
    }

}