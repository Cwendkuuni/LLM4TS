package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

/**
 * Test cases for AnnotationUtility class
 */
public class AnnotationUtilityTest {

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST, String)}
     */
    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "TestAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST, String)} with null ast
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        String annotation = "TestAnnotation";
        AnnotationUtility.containsAnnotation(null, annotation);
    }

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST, String)} with empty ast
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyAST() {
        DetailAST ast = new DetailAST();
        String annotation = "TestAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST, String)} with null annotation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST, String)} with empty annotation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST)}
     */
    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = createMockAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    /**
     * Test method for {@link AnnotationUtility#containsAnnotation(DetailAST)} with null ast
     */
    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST_bool() {
        AnnotationUtility.containsAnnotation(null);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotationHolder(DetailAST)}
     */
    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createMockAST();
        DetailAST expected = ast.findFirstToken(5);
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotationHolder(DetailAST)} with null ast
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotation(DetailAST, String)}
     */
    @Test
    public void testGetAnnotationWithValidAnnotationAndAST() {
        DetailAST ast = createMockAST();
        String annotation = "TestAnnotation";
        DetailAST expected = createMockAnnotation();
        DetailAST actual = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotation(DetailAST, String)} with null ast
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        String annotation = "TestAnnotation";
        AnnotationUtility.getAnnotation(null, annotation);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotation(DetailAST, String)} with null annotation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createMockAST();
        AnnotationUtility.getAnnotation(ast, null);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotation(DetailAST, String)} with empty annotation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    /**
     * Test method for {@link AnnotationUtility#getAnnotation(DetailAST, String)} with annotation not found
     */
    @Test
    public void testGetAnnotationWithNotFoundAnnotation() {
        DetailAST ast = createMockAST();
        String annotation = "NotExistingAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    /**
     * Helper method to create a mock DetailAST
     * @return a mock DetailAST
     */
    private DetailAST createMockAST() {
        // Implement mock DetailAST here
        return null;
    }

    /**
     * Helper method to create a mock DetailAST for an annotation
     * @return a mock DetailAST for an annotation
     */
    private DetailAST createMockAnnotation() {
        // Implement mock DetailAST for an annotation here
        return null;
    }
}