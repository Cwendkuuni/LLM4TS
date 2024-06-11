package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        String annotation = "javax.annotation.Nonnull";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "javax.annotation.Nonnull";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndMessage() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming a valid DetailAST for the annotation holder
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
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        String annotation = "javax.annotation.Nonnull";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming a valid DetailAST for the annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "javax.annotation.Nonnull";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        String annotation = " ";
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithAnnotationNameMatched() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        String annotation = "javax.annotation.Nonnull";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming a valid DetailAST for the annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithAnnotationNameNotMatched() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        String annotation = "javax.annotation.Nullabel"; // Different from the expected one
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(actualAnnotation);
    }
}