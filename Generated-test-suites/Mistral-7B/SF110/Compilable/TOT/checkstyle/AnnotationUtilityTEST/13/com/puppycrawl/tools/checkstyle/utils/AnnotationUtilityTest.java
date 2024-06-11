package com.puppycrawl.tools.checkstyle.utils;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    // Test Start
    @org.junit.Test
    public void testContainsAnnotation_Positive() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST for testing
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @org.junit.Test
    public void testContainsAnnotation_Negative() {
        DetailAST ast = new DetailAST();
        String annotation = "MyOtherAnnotation";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }
    // Test End
}