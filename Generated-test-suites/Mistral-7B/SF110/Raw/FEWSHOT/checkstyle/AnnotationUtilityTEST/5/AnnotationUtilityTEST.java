package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_ValidAnnotation_ReturnsTrue() {
        DetailAST ast = getTestAst();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_InvalidAST_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotation_InvalidAnnotation_ReturnsFalse() {
        DetailAST ast = getTestAst();
        String annotation = "";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_NoAnnotation_ReturnsFalse() {
        DetailAST ast = getTestAst();
        String annotation = "WrongAnnotation";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_ReturnsAnnotationHolder() {
        DetailAST ast = getTestAst();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertTrue(annotationHolder != null);
    }

    @Test
    public void testGetAnnotationHolder_InvalidAST_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotation_ValidASTWithAnnotation_ReturnsAnnotation() {
        DetailAST ast = getTestAstWithAnnotation();
        String annotation = "MyAnnotation";
        DetailAST annotationNode = AnnotationUtility.getAnnotation(ast, annotation);
        assertTrue(annotationNode != null);
    }

    @Test
    public void testGetAnnotation_ValidASTWithoutAnnotation_ReturnsNull() {
        DetailAST ast = getTestAst();
        String annotation = "MyAnnotation";
        DetailAST annotationNode = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(annotationNode);
    }

    @Test
    public void testGetAnnotation_InvalidAST_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotation_InvalidAnnotationName_ThrowsException() {
        DetailAST ast = getTestAstWithAnnotation();
        String annotation = "";
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotation_AnnotationNotFound_ReturnsNull() {
        DetailAST ast = getTestAstWithAnnotation();
        String annotation = "WrongAnnotation";
        DetailAST annotationNode = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(annotationNode);
    }

    private DetailAST getTestAst() {
        return getTestAst("EmptyAST.java");
    }

    private DetailAST getTestAstWithAnnotation() {
        return getTestAst("AnnotationTest.java");
    }

    private DetailAST getTestAst(String filename) {
        // Add the logic to create a DetailAST from the given filename here
        // For simplicity, we assume that getTestAst() returns a valid DetailAST
        return new DetailAST();
    }
}