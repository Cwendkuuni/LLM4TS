package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        assertTrue(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        AnnotationUtility.containsAnnotation(null, "testAnnotation");
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstNode() {
        AnnotationUtility.containsAnnotation(null);
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(holder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotation = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        annotation.setType(TokenTypes.IDENTIFIER);
        annotation.setText("testAnnotation");
        ast.setChild(annotationHolder);
        annotationHolder.setChild(annotation);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "testAnnotation");
        assertSame(annotation, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        AnnotationUtility.getAnnotation(null, "testAnnotation");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(TokenTypes.ANNOTATION);
        DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.IDENTIFIER);
        annotation.setText(" ");
        ast.setChild(annotationHolder);
        annotationHolder.setChild(annotation);

        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

}