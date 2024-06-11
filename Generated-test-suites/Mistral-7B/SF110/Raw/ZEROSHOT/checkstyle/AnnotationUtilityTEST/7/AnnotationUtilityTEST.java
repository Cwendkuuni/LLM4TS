package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = getSampleAst();
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = getSampleAst();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "ValidAnnotation"));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = getSampleAst();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = getSampleAst();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = getSampleAst();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(TokenTypes.ANNOTATION, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = getSampleAst();
        String annotation = "ValidAnnotation";
        DetailAST expectedAnnotation = getExpectedAnnotation(ast, annotation);
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAst() {
        DetailAST ast = getSampleAst();
        DetailAST expectedAnnotation = getExpectedAnnotation(ast, "ValidAnnotation");
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "ValidAnnotation");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, "ValidAnnotation"));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "ValidAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = getSampleAst();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = getSampleAst();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithNonMatchingAnnotation() {
        DetailAST ast = getSampleAst();
        assertNull(AnnotationUtility.getAnnotation(ast, "InvalidAnnotation"));
    }

    private DetailAST getSampleAst() {
        // Replace this with the actual creation of a DetailAST instance
        // that matches the conditions given in the AnnotationUtility class
        return null;
    }

    private DetailAST getExpectedAnnotation(DetailAST ast, String annotation) {
        // Replace this with the actual creation of a DetailAST instance
        // that matches the expected annotation given the provided ast and annotation
        return null;
    }

}