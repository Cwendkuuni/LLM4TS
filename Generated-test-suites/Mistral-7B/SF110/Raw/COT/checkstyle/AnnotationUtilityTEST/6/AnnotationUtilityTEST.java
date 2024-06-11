package com.example.junit4;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

/**
 * Test class for AnnotationUtility.
 */
public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationEmptyOrSpaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        AnnotationUtility.containsAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAstailandEmptyOrSpaceAnnotation() {
        AnnotationUtility.containsAnnotation(null, "");
    }

    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object with an annotation
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAst() {
        AnnotationUtility.getAnnotation(null, "annotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyOrSpaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAstlandEmptyOrSpaceAnnotation() {
        AnnotationUtility.getAnnotation(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotationOrInvalidASTType() {
        DetailAST ast = new DetailAST(); // Assuming an invalid DetailAST object
        ast.setType(1); // Setting an invalid type for testing
        AnnotationUtility.getAnnotation(ast, "annotation");
    }

    @Test
    public void testGetAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object with an annotation
        DetailAST expected = new DetailAST(); // Assuming a valid DetailAST object representing the annotation
        DetailAST actual = AnnotationUtility.getAnnotation(ast, "annotation");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolder() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        DetailAST expected = new DetailAST(); // Assuming a valid DetailAST object representing the annotation holder
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAstAndInvalidAnnotationType() {
        AnnotationUtility.containsAnnotation(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNameNull() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object
        String annotationName = null;
        String actual = AnnotationUtility.getAnnotationName(ast, annotationName);
        assertNull(actual);
    }

    @Test
    public void testGetAnnotationName() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST object with an annotation
        String annotationName = "annotation";
        String expected = "annotation";
        String actual = AnnotationUtility.getAnnotationName(ast, annotationName);
        assertEquals(expected, actual);
    }

}