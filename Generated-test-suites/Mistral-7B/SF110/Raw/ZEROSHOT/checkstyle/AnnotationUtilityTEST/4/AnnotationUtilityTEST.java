package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Objects;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = createSampleAst();
        String annotation = "AnnotationTest";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationTest";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = createSampleAst();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST ast = createSampleAst();
        String annotation = " ";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAst() {
        DetailAST ast = createSampleAst();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotationWithInvalidAst() {
        DetailAST ast = null;
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testGetAnnotationHolderWithValidAstAndAnnotationType() {
        DetailAST ast = createSampleAst();
        DetailAST expectedAnnotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
        DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotationName() {
        DetailAST ast = createSampleAst();
        DetailAST expectedAnnotation = ast.findFirstToken(TokenTypes.ANNOTATION).getFirstChild();
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "AnnotationTest");
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationWithValidAstAndNonExistentAnnotationName() {
        DetailAST ast = createSampleAst();
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation");
        assertNull(actualAnnotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "AnnotationTest";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotationName() {
        DetailAST ast = createSampleAst();
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotationWithEmptyOrSpacesAnnotationName() {
        DetailAST ast = createSampleAst();
        String annotation = " ";
        DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(actualAnnotation);
    }

    private DetailAST createSampleAst() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.ANNOTATION);
        DetailAST annotationType = new DetailAST();
        annotationType.setType(TokenTypes.ANNOTATIONTYPE);
        annotationType.setText("AnnotationTest");
        annotation.setFirstChild(annotationType);
        DetailAST annotationMemberValue = new DetailAST();
        annotationMemberValue.setType(TokenTypes.ANNOTATIONMEMBERVALUE);
        DetailAST annotationMemberValueValue = new DetailAST();
        annotationMemberValueValue.setText("ValueTest");
        annotationMemberValue.setFirstChild(annotationMemberValueValue);
        annotation.setNextSibling(annotationMemberValue);
        ast.setType(TokenTypes.ANNOTATION);
        ast.setFirstChild(annotation);
        return ast;
    }

}