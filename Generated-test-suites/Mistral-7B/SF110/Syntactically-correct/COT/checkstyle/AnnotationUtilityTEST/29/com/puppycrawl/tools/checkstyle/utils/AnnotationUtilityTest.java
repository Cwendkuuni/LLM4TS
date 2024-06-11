package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = " ";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullASTForContainsAnnotationMethodWithoutAnnotation() {
        DetailAST ast = null;
        AnnotationUtility.containsAnnotation(ast);
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming a valid DetailAST object for annotation holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationHolderWithAnnotationType155() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object for type 155
        DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming a valid DetailAST object for annotation holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithAnnotationType16() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object for type 16
        DetailAST expectedAnnotationHolder = new DetailAST(); // Assuming a valid DetailAST object for annotation holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = "MyAnnotation";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming a valid DetailAST object for the expected annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithInvalidASTAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming an invalid DetailAST object
        String annotation = "MyAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
}