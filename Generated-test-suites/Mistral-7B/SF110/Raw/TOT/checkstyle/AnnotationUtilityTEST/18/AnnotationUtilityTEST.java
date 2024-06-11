package com.example.junit;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidDetailAST() {
        DetailAST ast = AnnotationUtilityTest.createValidAST();
        boolean result = AnnotationUtility.containsAnnotation(ast, "ValidAnnotation");
        assertTrue(result);
    }

    @Test
    public void testContainsAnnotationWithInvalidDetailAST() {
        DetailAST ast = null;
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotationWithEmptyDetailAST() {
        DetailAST ast = AnnotationUtilityTest.createEmptyAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotationWithInvalidAnnotationName() {
        DetailAST ast = AnnotationUtilityTest.createValidAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "invalidAnnotationName"));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolder() {
        DetailAST ast = AnnotationUtilityTest.createValidASTWithAnnotationHolder();
        DetailAST annotationHolder = ast.findFirstToken(AnnotationUtility.ANNOTATION_TYPE);
        DetailAST annotation = ast.findFirstToken(AnnotationUtility.ANNOTATION_TYPE);
        DetailAST annotationHolderInsideAnnotation = annotation.findFirstToken(AnnotationUtility.ANNOTATION_HOLDER);
        assertSame(annotationHolderInsideAnnotation, AnnotationUtility.getAnnotationHolder(ast));
        boolean result = AnnotationUtility.containsAnnotation(ast, AnnotationUtility.ANNOTATION_NAME);
        assertTrue(result);
    }

    @Test
    public void testGetAnnotationWithValidDetailASTAndAnnotation() {
        DetailAST ast = AnnotationUtilityTest.createValidASTWithAnnotation();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "ValidAnnotation");
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationWithNullDetailAST() {
        DetailAST ast = null;
        assertNull(AnnotationUtility.getAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testGetAnnotationWithEmptyDetailAST() {
        DetailAST ast = AnnotationUtilityTest.createEmptyAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testGetAnnotationWithInvalidDetailASTType() {
        DetailAST ast = AnnotationUtilityTest.createInvalidASTType();
        assertNull(AnnotationUtility.getAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testGetAnnotationWithMultipleAnnotations() {
        DetailAST ast = AnnotationUtilityTest.createValidASTWithMultipleAnnotations();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "ValidAnnotation");
        assertEquals(expectedAnnotation, result);
    }

    @Test
    public void testGetAnnotationWithInvalidAnnotationName() {
        DetailAST ast = AnnotationUtilityTest.createValidASTWithAnnotation();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "InvalidAnnotation");
        assertNull(result);
    }

    private DetailAST createValidAST() {
        // Create a valid DetailAST object here
    }

    private DetailAST createValidASTWithAnnotation() {
        // Create a valid DetailAST object with an annotation here
    }

    private DetailAST createValidASTWithMultipleAnnotations() {
        // Create a valid DetailAST object with multiple annotations here
    }

    private DetailAST createInvalidASTType() {
        // Create an invalid DetailAST object here
    }

    private DetailAST expectedAnnotation; // Set the expected DetailAST object here

    static {
        ANNOTATION_TYPE = 159;
        ANNOTATION_NAME = "ValidAnnotation";
    }
}