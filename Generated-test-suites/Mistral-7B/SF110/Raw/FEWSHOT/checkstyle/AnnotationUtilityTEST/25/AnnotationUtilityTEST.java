package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_ValidASTAndAnnotation_ReturnsTrue() {
        DetailAST ast = createValidAST();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_ValidASTButNoAnnotation_ReturnsFalse() {
        DetailAST ast = createValidAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "OtherAnnotation"));
    }

    @Test
    public void testContainsAnnotation_NullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = createValidAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_EmptyOrWhitespaceAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = createValidAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, " \t\r\n "));
    }

    @Test
    public void testContainsAnnotation_ASTWithAnnotationHolder_ReturnsTrue() {
        DetailAST ast = createValidASTWithAnnotationHolder();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_ASTWithoutAnnotationHolder_ReturnsFalse() {
        DetailAST ast = createValidASTWithoutAnnotationHolder();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_ReturnsAnnotationHolder() {
        DetailAST ast = createValidASTWithAnnotationHolder();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(TokenTypes.ANNOTATION, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotationHolder_NullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotation_ValidASTAndAnnotation_ReturnsAnnotation() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = "MyAnnotation";
        DetailAST expectedAnnotation = createAnnotation("MyAnnotation", "value");
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotation_ValidASTButNoMatchingAnnotation_ReturnsNull() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = "OtherAnnotation";
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(actualAnnotation);
    }

    @Test
    public void testGetAnnotation_NullAST_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = createValidASTWithAnnotation();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_EmptyOrWhitespaceAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = createValidASTWithAnnotation();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, ""));
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, " \t\r\n "));
    }

    private DetailAST createValidAST() {
        // Create a valid DetailAST instance here.
    }

    private DetailAST createValidASTWithAnnotationHolder() {
        DetailAST ast = createValidAST();
        DetailAST annotationHolder = createAnnotationHolder();
        // Set the annotationHolder to the ast here.
        return ast;
    }

    private DetailAST createValidASTWithoutAnnotationHolder() {
        DetailAST ast = createValidAST();
        // Do not set the annotationHolder to the ast here.
        return ast;
    }

    private DetailAST createValidASTWithAnnotation() {
        DetailAST ast = createValidAST();
        DetailAST annotation = createAnnotation("MyAnnotation", "value");
        // Add the annotation to the ast here.
        return ast;
    }

    private DetailAST createAnnotationHolder() {
        // Create a DetailAST instance representing an annotation holder here.
    }

    private DetailAST createAnnotation(String annotationText, String value) {
        // Create a DetailAST instance representing an annotation with the given text and value here.
    }
}