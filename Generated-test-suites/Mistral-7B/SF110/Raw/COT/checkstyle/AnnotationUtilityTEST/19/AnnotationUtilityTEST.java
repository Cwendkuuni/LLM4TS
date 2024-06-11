package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndMessage() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with annotation present
        String annotation = "MyAnnotation";
        DetailAST expectedResult = new DetailAST(); // Assuming an expected DetailAST instance for the result
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        DetailAST expectedResult = new DetailAST(); // Assuming an expected DetailAST instance for the result
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        String annotation = "";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithAnnotationContainingSpaces() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        String annotation = " MyAnnotation ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with an empty annotation present
        String annotation = "";
        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAnnotationWithAnnotationContainingSpaces() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with an annotation containing spaces present
        String annotation = " MyAnnotation ";
        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedResult, result);
    }
}