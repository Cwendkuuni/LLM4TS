package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNullAst() {
        try {
            AnnotationUtility.containsAnnotation(null, "test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(AnnotationUtility.THE_AST_IS_NULL, e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        try {
            AnnotationUtility.containsAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        try {
            AnnotationUtility.containsAnnotation(ast, " ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotationWithAstContainingAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotationHolder = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotation = Preconditions.checkNotNull(new DetailAST());
        annotationHolder.setType(155);
        annotation.setType(159);
        annotation.setText("test");
        annotationHolder.appendChild(annotation);
        ast.appendChild(annotationHolder);

        assertTrue(AnnotationUtility.containsAnnotation(ast, "test"));
    }

    @Test
    public void testContainsAnnotationWithAstNotContainingAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        assertFalse(AnnotationUtility.containsAnnotation(ast, "test"));
    }

    @Test
    public void testContainsAnnotationWithAstContainingOtherType() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST otherType = Preconditions.checkNotNull(new DetailAST());
        otherType.setType(16);
        ast.appendChild(otherType);

        assertFalse(AnnotationUtility.containsAnnotation(ast, "test"));
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(AnnotationUtility.THE_AST_IS_NULL, e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationHolderWithAstNotHavingAnnotationHolder() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);
    }

    @Test
    public void testGetAnnotationHolderWithAstHavingAnnotationHolder() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotationHolder = Preconditions.checkNotNull(new DetailAST());
        annotationHolder.setType(155);
        ast.appendChild(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(annotationHolder, result);
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        try {
            AnnotationUtility.getAnnotation(null, "test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(AnnotationUtility.THE_AST_IS_NULL, e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        try {
            AnnotationUtility.getAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        try {
            AnnotationUtility.getAnnotation(ast, " ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithAstNotHavingAnnotation() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        assertNull(AnnotationUtility.getAnnotation(ast, "test"));
    }

    @Test
    public void testGetAnnotationWithAstHavingMultipleAnnotations() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST firstAnnotation = Preconditions.checkNotNull(new DetailAST());
        DetailAST secondAnnotation = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotationHolder = Preconditions.checkNotNull(new DetailAST());
        annotationHolder.setType(155);
        firstAnnotation.setType(159);
        firstAnnotation.setText("firstAnnotation");
        secondAnnotation.setType(159);
        secondAnnotation.setText("secondAnnotation");
        annotationHolder.appendChild(firstAnnotation);
        annotationHolder.appendChild(secondAnnotation);
        ast.appendChild(annotationHolder);

        DetailAST result = AnnotationUtility.getAnnotation(ast, "firstAnnotation");
        assertEquals(firstAnnotation, result);

        result = AnnotationUtility.getAnnotation(ast, "secondAnnotation");
        assertEquals(secondAnnotation, result);
    }

    @Test
    public void testGetAnnotationWithAstHavingAnnotationWithWrongName() {
        DetailAST ast = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotationHolder = Preconditions.checkNotNull(new DetailAST());
        DetailAST annotation = Preconditions.checkNotNull(new DetailAST());
        annotationHolder.setType(155);
        annotation.setType(159);
        annotation.setText("test");
        annotationHolder.appendChild(annotation);
        ast.appendChild(annotationHolder);

        assertNull(AnnotationUtility.getAnnotation(ast, "wrongName"));
    }

}