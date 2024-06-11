package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotation.setType(TokenTypes.ANNOTATION_TYPE);
        annotationHolder.appendChild(annotation);
        ast.setParent(annotationHolder);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithValidAstAndNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithValidAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithValidAstAndWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "   ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotation.setType(TokenTypes.ANNOTATION_TYPE);
        annotationHolder.appendChild(annotation);
        ast.setParent(annotationHolder);
        DetailAST expectedAnnotation = new DetailAST();
        expectedAnnotation.setType(TokenTypes.ANNOTATION);
        expectedAnnotation.setName("TestAnnotation");

        DetailAST resultAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertThat(resultAnnotation).isEqualTo(expectedAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndNullAnnotationName() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithValidAstAndEmptyAnnotationName() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithValidAstAndWhitespaceAnnotationName() {
        DetailAST ast = new DetailAST();
        String annotationName = "   ";
        assertNull(AnnotationUtility.getAnnotation(ast, annotationName));
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationWithEmptyAst() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotation.setType(TokenTypes.ANNOTATION_TYPE);
        annotationHolder.appendChild(annotation);
        ast.setParent(annotationHolder);
        DetailAST expectedAnnotationHolder = new DetailAST();
        expectedAnnotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST resultAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(resultAnnotationHolder).isEqualTo(expectedAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderAndAnnotation() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.ANNOTATION_TYPE);
        annotationHolder.appendChild(annotation);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderAndNullAnnotation() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderAndEmptyAnnotation() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderAndWhitespaceAnnotation() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);
        String annotation = "   ";

        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolder() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationWithAnnotationHolderAndAnnotation() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.ANNOTATION_TYPE);
        annotationHolder.appendChild(annotation);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);
        DetailAST expectedAnnotation = new DetailAST();
        expectedAnnotation.setType(TokenTypes.ANNOTATION);
        expectedAnnotation.setName("TestAnnotation");

        DetailAST resultAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertThat(resultAnnotation).isEqualTo(expectedAnnotation);
    }

    @Test
    public void testGetAnnotationWithAnnotationHolderAndNullAnnotationName() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithAnnotationHolderAndEmptyAnnotationName() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithAnnotationHolderAndWhitespaceAnnotationName() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);
        String annotationName = "   ";

        assertNull(AnnotationUtility.getAnnotation(ast, annotationName));
    }

    @Test
    public void testGetAnnotationWithAnnotationHolder() {
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);

        DetailAST ast = new DetailAST();
        ast.setParent(annotationHolder);

        assertNull(AnnotationUtility.getAnnotation(ast, "TestAnnotation"));
    }
}