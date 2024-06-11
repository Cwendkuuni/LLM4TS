package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationEmptyOrWhiteSpaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        AnnotationUtility.containsAnnotation(ast, " ");
    }

    @Test
    public void testContainsAnnotationPresent() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with an annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAstInGetAnnotationHolder() {
        String annotation = "annotationName";
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolderType155() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with annotation type 155
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(158, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotationHolderType16() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with annotation type 16
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(5, annotationHolder.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAst() {
        String annotation = "annotationName";
        AnnotationUtility.getAnnotation(null, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyOrWhiteSpaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        AnnotationUtility.getAnnotation(ast, " ");
    }

    @Test
    public void testGetAnnotationPresent() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance with an annotation
        String annotationName = "annotationName";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming a valid DetailAST instance representing the expected annotation
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, annotationName);
        assertEquals(expectedAnnotation, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAstInGetFirstChild() {
        DetailAST annotationHolder = new DetailAST(); // Assuming a valid DetailAST instance representing the annotation holder
        AnnotationUtility.getAnnotation(annotationHolder, "annotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullChild() {
        DetailAST annotationHolder = new DetailAST(); // Assuming a valid DetailAST instance representing the annotation holder
        DetailAST annotationHolderChild = null;
        AnnotationUtility.getAnnotation(annotationHolder, annotationHolderChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationInvalidAnnotationType() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        DetailAST annotationHolder = new DetailAST(); // Assuming a valid DetailAST instance representing the annotation holder
        DetailAST child = new DetailAST(); // Assuming a valid DetailAST instance representing a child node with type other than 159
        AnnotationUtility.getAnnotation(ast, annotationHolder, child);
    }

    @Test
    public void testGetAnnotationPresent() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST instance
        DetailAST annotationHolder = new DetailAST(); // Assuming a valid DetailAST instance representing the annotation holder
        DetailAST child = new DetailAST(); // Assuming a valid DetailAST instance representing the annotation
        DetailAST expectedAnnotation = new DetailAST(); // Assuming a valid DetailAST instance representing the expected annotation
        AnnotationUtility.getAnnotation(ast, annotationHolder, child);
        assertEquals(expectedAnnotation, annotation);
    }
}