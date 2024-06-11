package com.example.junitTests;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = new DetailAST(); // Assuming valid DetailAST object
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    // ... other test cases for containsAnnotation method

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST(); // Assuming valid DetailAST object
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }

    // ... other test cases for getAnnotationHolder method

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming valid DetailAST object
        String annotation = "ValidAnnotation";
        DetailAST expectedAnnotation = new DetailAST(); // Assuming valid DetailAST object representing the annotation
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    // ... other test cases for getAnnotation method

}