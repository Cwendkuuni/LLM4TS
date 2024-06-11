package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        final DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        final DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotation_AnnotationPresent() {
        final DetailAST ast = getAnnotationAST();
        assertThat(AnnotationUtility.containsAnnotation(ast, "annotation")).isTrue();
    }

    @Test
    public void testContainsAnnotation_AnnotationAbsent() {
        final DetailAST ast = getNonAnnotationAST();
        assertThat(AnnotationUtility.containsAnnotation(ast, "annotation")).isFalse();
    }

    @Test
    public void testGetAnnotationHolder_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_AnnotationPresent() {
        final DetailAST ast = getAnnotationAST();
        final DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        final int type = holder.getType();
        assertTrue(type == TokenTypes.ANNOTATION || type == TokenTypes.ANNOTATION_TYPE_DEF);
    }

    @Test
    public void testGetAnnotationHolder_AnnotationAbsent() {
        final DetailAST ast = getNonAnnotationAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        final DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        final DetailAST ast = new DetailAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotation_AnnotationPresent() {
        final DetailAST ast = getAnnotationAST();
        final DetailAST annotation = AnnotationUtility.getAnnotation(ast, "annotation");
        final int type = annotation.getType();
        assertTrue(type == TokenTypes.ANNOTATION);
    }

    @Test
    public void testGetAnnotation_AnnotationNotPresent() {
        final DetailAST ast = getNonAnnotationAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_WrongAnnotationType() {
        final DetailAST ast = getAnnotationAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "wrongAnnotationType"));
    }

    private DetailAST getAnnotationAST() {
        final DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.ANNOTATION);
        final DetailAST annotationType = new DetailAST();
        annotationType.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        annotationType.setText("annotation");
        final DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.ANNOTATION);
        annotation.setParent(ast);
        annotation.setChild(annotationType);
        return ast;
    }

    private DetailAST getNonAnnotationAST() {
        final DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.IDENTIFIER);
        ast.setText("identifier");
        return ast;
    }
}