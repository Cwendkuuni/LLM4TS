package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Arrays;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation = "SomeAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "SomeAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithAst() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst2() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testGetAnnotationHolderWithAst() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        DetailAST expectedResult = new DetailAST(); // create a mock DetailAST for the result
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation = "SomeAnnotation";
        DetailAST expectedResult = new DetailAST(); // create a mock DetailAST for the result
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "SomeAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation = " ";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation = null;
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithDifferentAnnotationName() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation = "SomeOtherAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationHolderWithWrongTypeAst() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        ast.setType(TokenTypes.COMMENT); // set the wrong type for the test
        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAnnotationHolderWithCorrectTypeAst() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        ast.setType(TokenTypes.ANNOTATION); // set the correct type for the test
        DetailAST expectedResult = new DetailAST(); // create a mock DetailAST for the result
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAnnotationWithWrongTypeAst() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        ast.setType(TokenTypes.COMMENT); // set the wrong type for the test
        String annotation = "SomeAnnotation";
        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAnnotationWithCorrectTypeAst() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        ast.setType(TokenTypes.ANNOTATION); // set the correct type for the test
        String annotation = "SomeAnnotation";
        DetailAST expectedResult = new DetailAST(); // create a mock DetailAST for the result
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAnnotationWithMultipleAnnotations() {
        DetailAST ast = new DetailAST(); // create a mock DetailAST
        String annotation1 = "Annotation1";
        String annotation2 = "Annotation2";
        DetailAST annotation1Node = new DetailAST(); // create a mock DetailAST for Annotation1
        DetailAST annotation2Node = new DetailAST(); // create a mock DetailAST for Annotation2
        annotation1Node.setType(TokenTypes.ANNOTATION);
        annotation2Node.setType(TokenTypes.ANNOTATION);
        ast.addChild(annotation1Node);
        ast.addChild(annotation2Node);
        DetailAST expectedResult = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation1);
        assertNull(result);
        result = AnnotationUtility.getAnnotation(ast, annotation2);
        assertNull(result);
        result = AnnotationUtility.getAnnotation(ast, annotation1);
        assertEquals(annotation1Node, result);
    }
}