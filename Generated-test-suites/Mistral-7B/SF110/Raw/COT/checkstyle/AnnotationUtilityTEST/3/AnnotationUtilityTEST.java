package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.InternalASTVisitor2;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = " ";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = createMockAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createMockAST();
        DetailAST expected = createMockAnnotationHolder();
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "ValidAnnotation";
        DetailAST expected = createMockAnnotation();
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "TestAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithValidASTAndNonExistentAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "NonExistentAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    private DetailAST createMockAnnotation() {
        DetailAST mockAnnotation = Mockito.mock(DetailAST.class);
        return mockAnnotation;
    }

    private DetailAST createMockAnnotationHolder() {
        DetailAST mockAnnotationHolder = Mockito.mock(DetailAST.class);
        return mockAnnotationHolder;
    }

    private DetailAST createMockAST() {
        DetailAST mockAST = Mockito.mock(DetailAST.class);
        return mockAST;
    }
}