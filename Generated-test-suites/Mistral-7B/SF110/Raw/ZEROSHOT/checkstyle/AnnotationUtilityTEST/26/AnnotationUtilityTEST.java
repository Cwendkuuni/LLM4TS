package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = createMockAST();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithOtherType() {
        DetailAST ast = createMockAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createMockAST();
        DetailAST expected = ast.findFirstToken(5);
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
        String annotation = "MyAnnotation";
        DetailAST expected = createMockAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrWhiteSpaceAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithInvalidASTAndAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "InvalidAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    private DetailAST createMockAST() {
        DetailAST ast = MockastBuilder.createTypeDeclaration("MyClass");
        DetailAST annotation = MockastBuilder.createAnnotation("MyAnnotation");
        ast.addLastChild(annotation);
        return ast;
    }

}