package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "AnnotationName"));
        assertFalse(AnnotationUtility.containsAnnotation(null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        String annotation = "";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        DetailAST expectedAnnotationHolder = new DetailAST(); // create a valid DetailAST instance as expected holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithInvalidASTType() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        ast.setType(2); // set an invalid type for the test
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        DetailAST expectedAnnotation = new DetailAST(); // create a valid DetailAST instance as expected annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "AnnotationName");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAST() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        DetailAST expectedAnnotation = new DetailAST(); // create a valid DetailAST instance as expected annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "AnnotationType");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertNull(AnnotationUtility.getAnnotation(null, "AnnotationName"));
        assertNull(AnnotationUtility.getAnnotation(null));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        assertNull(AnnotationUtility.getAnnotation(ast, null));
        assertNull(AnnotationUtility.getAnnotation(ast));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        String annotation = "";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
        assertNull(AnnotationUtility.getAnnotation(ast));
    }

    @Test
    public void testGetAnnotationWithInvalidASTType_155() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        ast.setType(1); // set an invalid type for the test
        assertNull(AnnotationUtility.getAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testGetAnnotationWithInvalidASTType_16() {
        DetailAST ast = new DetailAST(); // create a valid DetailAST instance
        ast.setType(2); // set an invalid type for the test
        assertNull(AnnotationUtility.getAnnotation(ast, "AnnotationName"));
    }
}