package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createTestAST();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = createTestAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
        assertFalse(AnnotationUtility.containsAnnotation(null));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createTestAST();
        DetailAST expectedAnnotationHolder = ast.findFirstToken(5);
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithInvalidASTType() {
        DetailAST ast = createTestAST();
        ast.setType(123); // Invalid type
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createTestAST();
        String annotation = "MyAnnotation";
        DetailAST expectedAnnotation = createTestAnnotation();
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidASTAndInvalidAnnotation() {
        DetailAST ast = createTestAST();
        String invalidAnnotation = "";
        assertNull(AnnotationUtility.getAnnotation(ast, invalidAnnotation));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertNull(AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createTestAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = createTestAST();
        String whitespaceAnnotation = " ";
        assertNull(AnnotationUtility.getAnnotation(ast, whitespaceAnnotation));
    }

    private DetailAST createTestAST() {
        DetailAST ast = new AstFactory().buildAST();
        // Initialize the AST with valid structure for testing
        return ast;
    }

    private DetailAST createTestAnnotation() {
        DetailAST annotation = new AstFactory().buildToken(TokenTypes.ANNOTATION, "MyAnnotation");
        return annotation;
    }

    private void assertEquals(DetailAST expected, DetailAST actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            fail("Either expected or actual is null");
        }
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getLine(), actual.getLine());
        assertEquals(expected.getColumn(), actual.getColumn());
    }
}