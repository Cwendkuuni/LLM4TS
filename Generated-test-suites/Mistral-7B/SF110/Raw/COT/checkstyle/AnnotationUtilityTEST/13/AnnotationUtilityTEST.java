package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithEmptyWhiteSpaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithAstContainingAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a DetailAST object with a valid annotation
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object with an annotation
        DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming an expected DetailAST object as the annotation holder
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
        DetailAST ast = new DetailAST(); // Assuming a DetailAST object with a valid annotation
        String annotation = "AnnotationName";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming an expected DetailAST object as the annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithInvalidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a DetailAST object without an annotation
        String annotation = "AnnotationName";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
}