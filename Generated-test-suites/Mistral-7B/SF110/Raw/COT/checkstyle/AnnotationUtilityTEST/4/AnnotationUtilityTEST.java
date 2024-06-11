package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNotNullAstAndAnnotation() {
        DetailAST ast = new DetailAST(); // Create a valid DetailAST instance
        String annotation = "AnnotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithAstContainingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotation = new DetailAST();
        Preconditions.checkArgument(ast != null, "the ast is null");
        Preconditions.checkArgument(annotationHolder != null, "the annotation holder is null");
        Preconditions.checkArgument(annotation != null, "the annotation is null");
        annotationHolder.setType(158);
        annotation.setType(159);
        annotation.setText("AnnotationName");
        ast.addChild(annotationHolder);
        ast.addChild(annotation);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithAstOfType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(158, holder.getType());
    }

    @Test
    public void testGetAnnotationHolderWithAstOfType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(158, holder.getType());
    }

    @Test
    public void testGetAnnotationHolderWithAstOfOtherType() {
        DetailAST ast = new DetailAST();
        ast.setType(5);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(5, holder.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationName";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithAstContainingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotation = new DetailAST();
        Preconditions.checkArgument(ast != null, "the ast is null");
        Preconditions.checkArgument(annotationHolder != null, "the annotation holder is null");
        Preconditions.checkArgument(annotation != null, "the annotation is null");
        annotationHolder.setType(158);
        annotation.setType(159);
        annotation.setText("AnnotationName");
        annotation.setText("AnnotationName");
        ast.addChild(annotationHolder);
        ast.addChild(annotation);
        DetailAST result = AnnotationUtility.getAnnotation(ast, "AnnotationName");
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationWithNullAnnotationName() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(result);
    }

    @Test
    public void testGetAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "AnnotationName");
        assertNull(result);
    }
}