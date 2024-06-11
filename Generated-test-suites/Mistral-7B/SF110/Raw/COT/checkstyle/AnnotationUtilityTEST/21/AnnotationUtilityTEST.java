package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "ValidAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndMessage() {
        DetailAST ast = null;
        String message = "the ast is null";
        AnnotationUtility.containsAnnotation(ast, message);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming a valid DetailAST object representing the annotation holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "ValidAnnotation";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming a valid DetailAST object representing the annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "ValidAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "";
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndEmptyAnnotationHolder() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        DetailAST annotationHolder = new DetailAST(); // Assuming a DetailAST object representing an empty annotation holder
        DetailAST expectedAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "EmptyAnnotation");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndNonExistentAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "NonExistentAnnotation";
        DetailAST expectedAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }
}