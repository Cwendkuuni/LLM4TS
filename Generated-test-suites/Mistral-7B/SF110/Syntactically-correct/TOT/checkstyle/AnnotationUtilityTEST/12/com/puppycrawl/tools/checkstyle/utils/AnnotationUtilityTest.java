
package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Function;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.junit.Test;

import java.util.Optional;

public class AnnotationUtilityTest {

    private static DetailAST createValidASTWithAnnotation() {
        // Create a valid AST with an annotation
        DetailAST ast = new DetailAST(); // Create a new DetailAST
        DetailAST annotationHolder = new DetailAST(); // Create a new DetailAST to represent the annotation holder
        DetailAST annotation = new DetailAST(); // Create a new DetailAST to represent the annotation
        annotation.setType(159); // Set the type of the annotation to 159
        annotation.setText("@annotationName"); // Set the text of the annotation
        annotationHolder.setType(158); // Set the type of the annotation holder to 158
        annotationHolder.setFirstChild(annotation); // Set the first child of the annotation holder to the annotation
        DetailAST containingNode = new DetailAST(); // Create a new DetailAST to represent the containing node
        containingNode.setType(155); // Set the type of the containing node to 155
        containingNode.setFirstChild(annotationHolder); // Set the first child of the containing node to the annotation holder
        ast.setType(1); // Set the type of the root AST to 1 (Comment)
        ast.setFirstChild(containingNode); // Set the first child of the root AST to the containing node
        return ast;
    }

    private static DetailAST createValidASTWithAnnotationHolder() {
        // Create a valid AST with an annotation holder
        DetailAST ast = new DetailAST(); // Create a new DetailAST
        DetailAST annotationHolder = new DetailAST(); // Create a new DetailAST to represent the annotation holder
        annotationHolder.setType(158); // Set the type of the annotation holder to 158
        DetailAST annotation = new DetailAST(); // Create a new DetailAST to represent the annotation
        annotation.setType(159); // Set the type of the annotation to 159
        annotation.setText("@annotationName"); // Set the text of the annotation
        annotationHolder.setFirstChild(annotation); // Set the first child of the annotation holder to the annotation
        DetailAST containingNode = new DetailAST(); // Create a new DetailAST to represent the containing node
        containingNode.setType(155); // Set the type of the containing node to 155
        containingNode.setFirstChild(annotationHolder); // Set the first child of the containing node to the annotation holder
        ast.setType(1); // Set the type of the root AST to 1 (Comment)
        ast.setFirstChild(containingNode); // Set the first child of the root AST to the containing node
        return ast;
    }

    private static DetailAST createValidASTWithoutAnnotationHolder() {
        // Create a valid AST without annotation holder
        DetailAST ast = new DetailAST(); // Create a new DetailAST
        DetailAST containingNode = new DetailAST(); // Create a new DetailAST to represent the containing node
        containingNode.setType(155); // Set the type of the containing node to 155
        ast.setType(1); // Set the type of the root AST to 1 (Comment)
        ast.setFirstChild(containingNode); // Set the first child of the root AST to the containing node
        return ast;
    }

    private static DetailAST createExpectedAnnotation() {
        DetailAST expectedAnnotation = new DetailAST(); // Create a new DetailAST to represent the expected annotation
        expectedAnnotation.setText("@annotationName"); // Set the text of the expected annotation
        return expectedAnnotation;
    }

    @Test
    public void testContainsAnnotation_Valid() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = "annotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_NullAst() {
        DetailAST ast = null;
        String annotation = "annotationName";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testContainsAnnotation_NoAnnotation() {
        DetailAST ast = createValidASTWithoutAnnotation();
        String annotation = "annotationName";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = null;
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = " ";
        try {
            AnnotationUtility.containsAnnotation(ast, annotation);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetAnnotation_Valid() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = "annotationName";
        DetailAST expectedAnnotation = createExpectedAnnotation();
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(result);
        assertEquals(expectedAnnotation, result);
    }

    @Test
    public void testGetAnnotation_NullAst() {
        DetailAST ast = null;
        String annotation = "annotationName";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = null;
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = " ";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotation_NoMatchingAnnotation() {
        DetailAST ast = createValidASTWithAnnotation();
        String annotation = "wrongAnnotationName";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
}
