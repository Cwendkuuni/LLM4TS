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

    private DetailAST mockDetailAST;

    @Before
    public void setup() {
        mockDetailAST = mock(DetailAST.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContainsAnnotationNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationNullAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockDetailAST, null));
    }

    @Test
    public void testContainsAnnotationEmptyAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(mockDetailAST, ""));
    }

    @Test
    public void testGetAnnotationHolderNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderNotAnnotationOrPackage() {
        when(mockDetailAST.getType()).thenReturn(1);
        assertNull(AnnotationUtility.getAnnotationHolder(mockDetailAST));
    }

    @Test
    public void testGetAnnotationHolderAnnotation() {
        when(mockDetailAST.getType()).thenReturn(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        assertThat(annotationHolder).isNotNull();
        assertThat(annotationHolder.getType()).isEqualTo(TokenTypes.ANNOTATION);
    }

    @Test
    public void testGetAnnotationHolderPackage() {
        when(mockDetailAST.getType()).thenReturn(16);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        assertThat(annotationHolder).isNotNull();
        assertThat(annotationHolder.getType()).isEqualTo(TokenTypes.PACKAGE_DEF);
    }

    @Test
    public void testGetAnnotationNullAST() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotationNullAnnotation() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(mockDetailAST, null));
    }

    @Test
    public void testGetAnnotationEmptyAnnotation() {
        when(mockDetailAST.getType()).thenReturn(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        DetailAST annotation = AnnotationUtility.getAnnotation(annotationHolder, "");
        assertNull(annotation);
    }

    @Test
    public void testGetAnnotation() {
        String annotationName = "MyAnnotation";
        when(mockDetailAST.getType()).thenReturn(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        DetailAST annotation = AnnotationUtility.getAnnotation(annotationHolder, annotationName);
        assertThat(annotation).isNotNull();
        DetailAST annotationNameToken = annotation.getFirstChild();
        DetailAST annotationValueToken = annotation.getChildByType(TokenTypes.ANNOTATION_VALUE);
        String annotationValue = annotationValueToken.getText();
        assertThat(annotationNameToken.getText()).isEqualTo(annotationName);
        assertThat(annotationValue).isEqualTo("value");
    }

    @Test
    public void testGetAnnotationNotFound() {
        when(mockDetailAST.getType()).thenReturn(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        DetailAST annotation = AnnotationUtility.getAnnotation(annotationHolder, "AnotherAnnotation");
        assertNull(annotation);
    }

    @Test
    public void testContainsAnnotationWithAnnotation() {
        when(mockDetailAST.getType()).thenReturn(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        DetailAST annotation = new DetailAST() {};
        when(annotation.getParent()).thenReturn(annotationHolder);
        DetailAST annotationType = new DetailAST() {};
        when(annotationType.getText()).thenReturn("MyAnnotation");
        when(annotation.getFirstChild()).thenReturn(annotationType);
        DetailAST annotationValue = new DetailAST() {};
        when(annotation.getChildByType(TokenTypes.ANNOTATION_VALUE)).thenReturn(annotationValue);
        when(annotationValue.getText()).thenReturn("value");
        DetailAST child = new DetailAST() {};
        when(annotationHolder.getFirstChild()).thenReturn(child);
        DetailAST nextSibling = new DetailAST() {};
        when(child.getNextSibling()).thenReturn(nextSibling);
        DetailAST annotationTypeChild = new DetailAST() {};
        when(annotationTypeChild.getText()).thenReturn("Type");
        when(nextSibling.getFirstChild()).thenReturn(annotationTypeChild);
        DetailAST annotationNameChild = new DetailAST() {};
        when(annotationTypeChild.getFirstChild()).thenReturn(annotationNameChild);
        when(annotationNameChild.getText()).thenReturn("MyAnnotation");

        boolean result = AnnotationUtility.containsAnnotation(mockDetailAST, "MyAnnotation");
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        when(mockDetailAST.getType()).thenReturn(155);
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(mockDetailAST);
        DetailAST child = new DetailAST() {};
        when(annotationHolder.getFirstChild()).thenReturn(child);

        boolean result = AnnotationUtility.containsAnnotation(mockDetailAST, "MyAnnotation");
        assertFalse(result);
    }

}