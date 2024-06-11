package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "ValidAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndThrowsException() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST expectedAnnotationHolder = new DetailAST();
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAnnotationWithValidAstAndInvalidAnnotationName() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "ValidAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndValidAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST expectedAnnotation = new DetailAST();
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "ValidAnnotation");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndNonExistentAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST expectedAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation");
        assertEquals(expectedAnnotation, actualAnnotation);
    }
}