package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createMockAst();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidASTAndNoAnnotation() {
        DetailAST ast = createMockAst();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
        try {
            AnnotationUtility.containsAnnotation(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAst();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
        try {
            AnnotationUtility.containsAnnotation(ast, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = createMockAst();
        String annotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createMockAstWithAnnotation();
        String annotation = "MyAnnotation";
        DetailAST expected = createMockAnnotationNode();
        DetailAST actual = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAnnotationWithValidASTAndNoAnnotation() {
        DetailAST ast = createMockAst();
        assertNull(AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertNull(AnnotationUtility.getAnnotation(null, "MyAnnotation"));
        try {
            AnnotationUtility.getAnnotation(null, "MyAnnotation");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAst();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createMockAst();
        DetailAST expected = createMockAnnotationHolderNode();
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderAndAnnotation() {
        DetailAST ast = createMockAst();
        DetailAST annotationHolder = createMockAnnotationHolderNode();
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotationHolder, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithAnnotationHolderAndNoAnnotation() {
        DetailAST ast = createMockAst();
        DetailAST annotationHolder = createMockAnnotationHolderNode();
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotationHolder, null));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotationHolder() {
        DetailAST ast = createMockAst();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null, "MyAnnotation"));
        try {
            AnnotationUtility.containsAnnotation(ast, null, "MyAnnotation");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation holder is null", e.getMessage());
        }
    }

    private DetailAST createMockAst() {
        // Implement a factory method to create a mock DetailAST instance
    }

    private DetailAST createMockAstWithAnnotation() {
        // Implement a factory method to create a mock DetailAST instance with an annotation
    }

    private DetailAST createMockAnnotationHolderNode() {
        // Implement a factory method to create a mock DetailAST instance representing an annotation holder
    }

    private DetailAST createMockAnnotationNode() {
        // Implement a factory method to create a mock DetailAST instance representing an annotation node
    }
}