package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation
        DetailAST annotation = new DetailAST();
        annotation.setType(159); // Annotation
        DetailAST annotationName = new DetailAST();
        annotationName.setText("MyAnnotation");
        annotation.addChild(annotationName);
        DetailAST annotationValue = new DetailAST();
        annotationValue.setText("true");
        annotation.addChild(annotationValue);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotationHolder.addChild(annotation);
        ast.addChild(annotationHolder);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithValidASTAndNoAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation

        assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation

        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        DetailAST annotation = new DetailAST();
        annotation.setType(159); // Annotation
        DetailAST annotationName = new DetailAST();
        annotationName.setText("MyAnnotation");
        annotation.addChild(annotationName);
        DetailAST annotationValue = new DetailAST();
        annotationValue.setText("true");
        annotation.addChild(annotationValue);
        annotationHolder.addChild(annotation);
        ast.addChild(annotationHolder);

        DetailAST expectedAnnotation = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
        assertEquals(expectedAnnotation, annotation);
    }

    @Test
    public void testGetAnnotationWithValidASTAndNoAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation

        assertNull(AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertNull(AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotationName() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation

        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullASTAndAnnotationName() {
        assertNull(AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setType(155); // TypeAnnotation
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        ast.addChild(annotationHolder);

        DetailAST expectedAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, annotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolderWithInvalidASTType() {
        DetailAST ast = new DetailAST();
        ast.setType(16); // PackageDeclaration

        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }
}