package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createTestAST();
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = createTestAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast, "AnnotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullASTAndAnnotation() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createTestAST();
        DetailAST expectedAnnotationHolder = ast.findFirstToken(5);
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createTestAST();
        String annotation = "AnnotationName";
        DetailAST expectedAnnotation = ast.findFirstToken(159).getFirstChild();
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotation(ast, "AnnotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullASTAndAnnotationName() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotationName() {
        DetailAST ast = createTestAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithEmptyAST() {
        DetailAST ast = createEmptyAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    private DetailAST createTestAST() {
        // Create a test DetailAST here
        // Replace this line with the actual code to create a valid DetailAST
        return null;
    }

    private DetailAST createEmptyAST() {
        // Create an empty DetailAST here
        // Replace this line with the actual code to create an empty DetailAST
        return null;
    }
}