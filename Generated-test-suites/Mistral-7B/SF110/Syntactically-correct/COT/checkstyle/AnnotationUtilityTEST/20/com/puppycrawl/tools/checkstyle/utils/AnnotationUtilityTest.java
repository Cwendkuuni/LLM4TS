package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, " ");
    }

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotation = new DetailAST();
        annotationHolder.setType(155);
        annotation.setType(159);
        annotation.setText("TestAnnotation");
        annotationHolder.addChild(annotation);
        ast.setParent(annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst2() {
        AnnotationUtility.containsAnnotation(null, "TestAnnotation");
    }

    @Test
    public void testContainsAnnotationWithValidAstButNoAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "TestAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotation = new DetailAST();
        annotationHolder.setType(155);
        annotation.setType(159);
        annotation.setText("TestAnnotation");
        annotationHolder.addChild(annotation);
        ast.setParent(annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationWithValidAstButNoAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotation = new DetailAST();
        annotationHolder.setType(155);
        ast.setParent(annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithInvalidAstType() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        AnnotationUtility.getAnnotation(ast, "TestAnnotation");
    }

}