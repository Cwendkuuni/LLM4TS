package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.google.common.base.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = MockAST.newMockAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAst() {
        try {
            AnnotationUtility.containsAnnotation(null, "ValidAnnotation");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithInvalidAstType() {
        DetailAST ast = MockAST.newMockAST();
        ast.setType(1);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithValidAnnotationHolder() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newMockAST();
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotationHolder() {
        DetailAST ast = MockAST.newMockAST();
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), null);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithValidAnnotationAndAnnotationHolderBranch() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newMockAST();
        DetailAST annotation = MockAST.newMockAST();
        annotation.setType(159);
        annotation.setNextSibling(annotationHolder);
        annotationHolder.setNextSibling(MockAST.newMockAST());
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        assertTrue(AnnotationUtility.containsAnnotation(ast, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithInvalidAnnotationName() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newMockAST();
        DetailAST annotation = MockAST.newMockAST();
        annotation.setType(159);
        annotation.setNextSibling(annotationHolder);
        annotationHolder.setNextSibling(MockAST.newMockAST());
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "InvalidAnnotationName"));
    }

    @Test
    public void testContainsAnnotationWithBlankAnnotation() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newMockAST();
        DetailAST annotation = MockAST.newMockAST();
        annotation.setType(159);
        annotation.setNextSibling(annotationHolder);
        annotationHolder.setNextSibling(MockAST.newMockAST());
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newMockAST();
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithInvalidAstType() {
        DetailAST ast = MockAST.newMockAST();
        ast.setType(1);
        try {
            AnnotationUtility.getAnnotationHolder(ast);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithValidAnnotationHolder() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newMockAST();
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationHolderWithNullAnnotationHolder() {
        DetailAST ast = MockAST.newMockAST();
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), null);
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = MockAST.newMockAST();
        DetailAST annotationHolder = MockAST.newmockAST();
        DetailAST annotation = MockAST.newmockAST();
        annotation.setType(159);
        String annotationName = "ValidAnnotation";
        String annotationTypeName = "ValidAnnotationType";
        DetailAST name = MockAST.newmockAST();
        name.setText(annotationName);
        DetailAST type = MockAST.newmockAST();
        type.setText(annotationTypeName);
        annotation.setChild(1, name);
        annotation.setChild(2, type);
        annotationHolder.setNextSibling(annotation);
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotationName);
        assertEquals(annotation, result);
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        try {
            AnnotationUtility.getAnnotation(null, "ValidAnnotation");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithInvalidAstType() {
        DetailAST ast = MockAST.newmockAST();
        ast.setType(1);
        try {
            AnnotationUtility.getAnnotation(ast, "ValidAnnotation");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithInvalidAnnotationName() {
        DetailAST ast = MockAST.newmockAST();
        DetailAST annotationHolder = MockAST.newmockAST();
        DetailAST annotation = MockAST.newmockAST();
        annotation.setType(159);
        String annotationName = "ValidAnnotation";
        String annotationTypeName = "ValidAnnotationType";
        DetailAST name = MockAST.newmockAST();
        name.setText(annotationName);
        DetailAST type = MockAST.newmockAST();
        type.setText(annotationTypeName);
        annotation.setChild(1, name);
        annotation.setChild(2, type);
        annotationHolder.setNextSibling(annotation);
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        assertNull(AnnotationUtility.getAnnotation(ast, "InvalidAnnotationName"));
    }

    @Test
    public void testGetAnnotationWithBlankAnnotationName() {
        DetailAST ast = MockAST.newmockAST();
        DetailAST annotationHolder = MockAST.newmockAST();
        DetailAST annotation = MockAST.newmockAST();
        annotation.setType(159);
        String annotationName = "ValidAnnotation";
        String annotationTypeName = "ValidAnnotationType";
        DetailAST name = MockAST.newmockAST();
        name.setText(annotationName);
        DetailAST type = MockAST.newmockAST();
        type.setText(annotationTypeName);
        annotation.setChild(1, name);
        annotation.setChild(2, type);
        annotationHolder.setNextSibling(annotation);
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = MockAST.newmockAST();
        DetailAST annotationHolder = MockAST.newmockAST();
        DetailAST annotation = MockAST.newmockAST();
        String annotationName = "ValidAnnotation";
        String annotationTypeName = "ValidAnnotationType";
        DetailAST name = MockAST.newmockAST();
        name.setText(annotationName);
        DetailAST type = MockAST.newmockAST();
        type.setText(annotationTypeName);
        annotation.setChild(1, name);
        annotation.setChild(2, type);
        annotationHolder.setNextSibling(annotation);
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithValidAnnotationNameAndAnnotation() {
        DetailAST ast = MockAST.newmockAST();
        DetailAST annotationHolder = MockAST.newmockAST();
        DetailAST annotation = MockAST.newmockAST();
        String annotationName = "ValidAnnotation";
        String annotationTypeName = "ValidAnnotationType";
        DetailAST name = MockAST.newmockAST();
        name.setText(annotationName);
        DetailAST type = MockAST.newmockAST();
        type.setText(annotationTypeName);
        annotation.setChild(1, name);
        annotation.setChild(2, type);
        annotationHolder.setNextSibling(annotation);
        ast.setChild(AnnotationUtility.getAnnotationHolder(ast), annotationHolder);
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotationName);
        assertEquals(annotation, result);
    }
}