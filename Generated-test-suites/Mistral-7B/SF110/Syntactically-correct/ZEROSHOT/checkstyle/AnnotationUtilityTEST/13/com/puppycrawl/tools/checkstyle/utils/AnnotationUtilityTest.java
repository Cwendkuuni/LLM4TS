package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNotNullAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        assertNull(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNotNullAst() {
        DetailAST ast = new DetailAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndAnnotation() {
        assertNull(AnnotationUtility.containsAnnotation(null, "TestAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithNotNullAst() {
        DetailAST ast = new DetailAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(holder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationWithNotNullAstAndNotNullAnnotation() {
        DetailAST ast = new DetailAST();
        DetailAST annotation = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotationType = new DetailAST();
        DetailAST name = new DetailAST();

        annotationType.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        name.setText("TestAnnotation");
        annotation.addChild(annotationType);
        annotation.addChild(name);
        annotationHolder.addChild(annotation);
        ast.addChild(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "TestAnnotation");
        assertEquals(annotation, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        assertNull(AnnotationUtility.getAnnotation(null, "TestAnnotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, " "));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "   "));
        assertFalse(AnnotationUtility.containsAnnotation(ast, "\tTestAnnotation\t"));
    }

}