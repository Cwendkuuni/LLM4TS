package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NotNullAstAndAnnotation_ReturnsTrue() {
        DetailAST ast = new DetailAST(); // Assuming this is a valid DetailAST instance
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst_ThrowsException() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAnnotation_ThrowsException() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotation_NotNullAst_ReturnsTrueWithAnnotationType() {
        DetailAST ast = new DetailAST();
        String annotationType = "TypeAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotationType));
    }

    @Test
    public void testContainsAnnotation_NotNullAst_ReturnsFalseWithoutAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "NonExistentAnnotation";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationHolder_NotNullAst_ReturnsAnnotationHolder() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst_ThrowsException() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotation_NotNullAstAndAnnotation_ReturnsAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotationName = new DetailAST();
        annotationName.setText("AnnotationName");
        annotationHolder.addChild(annotationName);
        annotation.setParent(annotationHolder);
        ast.setParent(annotationHolder);
        DetailAST foundAnnotation = AnnotationUtility.getAnnotation(ast, "AnnotationName");
        assertEquals(annotation, foundAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst_ThrowsException() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAnnotation_ThrowsException() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyOrSpacesAnnotation_ThrowsException() {
        DetailAST ast = new DetailAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotation_NotNullAst_NullAnnotationNotPresent_ReturnsNull() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotationName = new DetailAST();
        annotationName.setText("AnnotationName");
        annotationHolder.addChild(annotationName);
        ast.setParent(annotationHolder);
        DetailAST foundAnnotation = AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation");
        assertNull(foundAnnotation);
    }
}