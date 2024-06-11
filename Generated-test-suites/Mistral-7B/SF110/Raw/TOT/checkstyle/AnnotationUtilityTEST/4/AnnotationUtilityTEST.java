package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST validAst = createValidAST();
        String annotation = "annotationName";
        assertTrue(AnnotationUtility.containsAnnotation(validAst, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAstAndEmptyAnnotation() {
        DetailAST validAst = createValidAST();
        assertFalse(AnnotationUtility.containsAnnotation(validAst, ""));
    }

    @Test
    public void testContainsAnnotationWithValidAstAndNullAnnotation() {
        DetailAST validAst = createValidAST();
        assertFalse(AnnotationUtility.containsAnnotation(validAst, null));
    }

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "annotationName"));
    }

    @Test
    public void testContainsAnnotationWithNullAstAndEmptyAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(null, ""));
    }

    @Test
    public void testContainsAnnotationWithNullAstAndNullAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(null, null));
    }

    @Test
    public void testContainsAnnotationWithValidAstAndDoesNotContainAnnotation() {
        DetailAST validAst = createValidAST();
        String annotation = "nonExistentAnnotation";
        assertFalse(AnnotationUtility.containsAnnotation(validAst, annotation));
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST validAst = createValidASTWithAnnotation();
        String annotation = "annotationName";
        DetailAST expectedAnnotation = createExpectedAnnotation();
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(validAst, annotation);
        assertSame(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndNullAnnotation() {
        DetailAST validAst = createValidAST();
        assertNull(AnnotationUtility.getAnnotation(validAst, null));
    }

    @Test
    public void testGetAnnotationWithValidAstAndEmptyAnnotation() {
        DetailAST validAst = createValidAST();
        assertNull(AnnotationUtility.getAnnotation(validAst, ""));
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, "annotationName"));
    }

    @Test
    public void testGetAnnotationWithNullAstAndEmptyAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(null, ""));
    }

    @Test
    public void testGetAnnotationWithNullAstAndNullAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(null, null));
    }

    @Test
    public void testGetAnnotationWithValidAstAndDoesNotContainAnnotation() {
        DetailAST validAst = createValidAST();
        String annotation = "nonExistentAnnotation";
        assertNull(AnnotationUtility.getAnnotation(validAst, annotation));
    }

    private DetailAST createValidAST() {
        // Create a valid DetailAST object here.
    }

    private DetailAST createValidASTWithAnnotation() {
        // Create a valid DetailAST object with an annotation.
    }

    private DetailAST createExpectedAnnotation() {
        // Create the expected DetailAST object for the test getAnnotation method.
    }

}