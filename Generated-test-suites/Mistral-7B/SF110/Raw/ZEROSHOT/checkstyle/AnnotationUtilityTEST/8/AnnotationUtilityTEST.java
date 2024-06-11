package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNotNullAstAndAnnotation() {
        DetailAST ast = createMockAst();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithNotNullAst() {
        DetailAST ast = createMockAst();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndNullAnnotation() {
        DetailAST ast = null;
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndEmptyAnnotation() {
        DetailAST ast = null;
        String annotation = "";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndAnnotationWithSpaces() {
        DetailAST ast = null;
        String annotation = "MyAnnotation   ";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationHolderWithType155() {
        DetailAST ast = createMockAstWithAnnotation("AnnotationType1");
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(TokenTypes.ANNOTATION, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotationHolderWithType16() {
        DetailAST ast = createMockAstWithAnnotation("AnnotationType2");
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(TokenTypes.ANNOTATION, annotationHolder.getType());
    }

    @Test
    public void testGetAnnotationHolderWithTypeOtherThanAnnotation() {
        DetailAST ast = createMockAst();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAstAndNullAnnotation() {
        DetailAST ast = null;
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAstAndEmptyAnnotation() {
        DetailAST ast = null;
        String annotation = "";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAstAndAnnotationWithSpaces() {
        DetailAST ast = null;
        String annotation = "MyAnnotation   ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndNotNullAnnotation() {
        DetailAST ast = createMockAstWithAnnotation("MyAnnotation");
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
        assertNotNull(annotation);
        assertEquals(TokenTypes.ANNOTATION, annotation.getType());
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndNullAnnotation() {
        DetailAST ast = createMockAstWithAnnotation("MyAnnotation");
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, null);
        assertNull(annotation);
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndEmptyAnnotation() {
        DetailAST ast = createMockAstWithAnnotation("MyAnnotation");
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "");
        assertNull(annotation);
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndAnnotationWithSpaces() {
        DetailAST ast = createMockAstWithAnnotation("MyAnnotation");
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, " MyAnnotation ");
        assertNotNull(annotation);
        assertEquals("MyAnnotation", annotation.getText());
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndAnnotationNameNotMatching() {
        DetailAST ast = createMockAstWithAnnotation("MyAnnotation");
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "OtherAnnotation");
        assertNull(annotation);
    }

    private DetailAST createMockAst() {
        return new DetailAST();
    }

    private DetailAST createMockAstWithAnnotation(String annotation) {
        DetailAST ast = new DetailAST();
        DetailAST annotationToken = new DetailAST();
        annotationToken.setType(TokenTypes.ANNOTATION);
        annotationToken.setText(annotation);
        DetailAST annotationHolder = new DetailAST();
        annotationHolder.setType(annotationToken.getType());
        annotationHolder.addChild(annotationToken);
        ast.setParent(annotationHolder);
        ast.setType(TokenTypes.ANNOTATION);
        return ast;
    }
}