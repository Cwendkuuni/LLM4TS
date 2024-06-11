package com.example.junit; // replace with your test package name

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
    public void testContainsAnnotationWithNotNullAstAndNonNullAnnotation() {
        DetailAST ast = new DetailAST(); // replace with a valid DetailAST
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
    public void testContainsAnnotationWithNotNullAstAndEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithNotNullAstAndNoAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertSame(TokenTypes.ANNOTATION, holder.getType());
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithValidAstAndValidAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "MyAnnotation";
        DetailAST expected = new DetailAST(); // replace with a valid DetailAST
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertSame(expected, result);
    }

    @Test
    public void testGetAnnotationWithValidAstAndInvalidAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "WrongAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotationWithSpaces() {
        DetailAST ast = new DetailAST();
        String annotation = " MyAnnotation ";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }
}