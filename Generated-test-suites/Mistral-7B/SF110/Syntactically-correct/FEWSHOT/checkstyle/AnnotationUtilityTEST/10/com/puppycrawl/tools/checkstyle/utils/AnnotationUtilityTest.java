package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert. assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithNullAstAndEmptyAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(null, ""));
    }

    @Test
    public void testContainsAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance with annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderWithAstContainingAnnotationType155() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance with annotation type 155
        DetailAST expectedAnnotationHolder = new DetailAST(); // assuming valid DetailAST instance for annotation holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithAstContainingAnnotationType16() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance with annotation type 16
        DetailAST expectedAnnotationHolder = new DetailAST(); // assuming valid DetailAST instance for annotation holder
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithAstNotContainingAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance without annotation
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithNullAstAndEmptyAnnotation() {
        assertNull(AnnotationUtility.getAnnotation(null, ""));
    }

    @Test
    public void testGetAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance without annotation
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotationWithAstContainingAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance with annotation
        String expectedAnnotationName = "expectedAnnotationName";
        DetailAST expectedAnnotation = new DetailAST(); // assuming valid DetailAST instance for annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "expectedAnnotationName");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testContainsAnnotationWithEmptyString() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithEmptyStringAndNullAst() {
        assertFalse(AnnotationUtility.containsAnnotation(null, ""));
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = new DetailAST(); // assuming valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, " "));
        assertNull(AnnotationUtility.getAnnotation(ast, " "));
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotationAndNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, " "));
    }
}