package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.junit.Test;

public class AnnotationUtilityTest {
    // ... (previous tests)

    @Test
    public void testGetAnnotationWithNullAnnotationNode3() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, null);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationWithNullAnnotationNode4() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedExceptionNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertNull(expectedExceptionNullAnnotation);
        assertNotNull(actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithMissingAnnotationNode() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithInvalidAnnotationNodeType() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedAnnotation = new DetailAST();
        DetailAST expectedAnnotationNode = new DetailAST();
        expectedAnnotationNode.setType(156); // Set an unexpected type
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertNotNull(actualAnnotation);
        assertEquals(expectedAnnotation, actualAnnotation);
        assertEquals(expectedAnnotationNode.getType(), actualAnnotation.getFirstChild().getType());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationWithNullAnnotationNode5() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, null);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithMissingAnnotationNode2() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotationNode() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotationNode2() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotationNode3() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST expectedNullAnnotation = null;
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation, annotationHolder);
        assertEquals(expectedNullAnnotation, actualAnnotation);
    }
}