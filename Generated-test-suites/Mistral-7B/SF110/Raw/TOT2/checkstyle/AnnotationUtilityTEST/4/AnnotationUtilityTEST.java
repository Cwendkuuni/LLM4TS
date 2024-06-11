package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;

public class AnnotationUtilityTest {

    DetailAST ast;
    AnnotationUtility utility;

    @Before
    public void setUp() {
        utility = new AnnotationUtility();
        ast = mock(DetailAST.class);
    }

     // Previous test cases

    @Test
    public void testGetAnnotation_MultipleAnnotations() {
        checkNotNull(ast);
        checkArgument(ast != null, AnnotationUtility.THE_AST_IS_NULL);
        DetailAST annotationHolder = mock(DetailAST.class);
        when(ast.getFirstChild()).thenReturn(annotationHolder);
        DetailAST annotation1 = mock(DetailAST.class);
        DetailAST annotation2 = mock(DetailAST.class);
        when(annotationHolder.getFirstChild()).thenReturn(annotation1);
        when(annotation1.getType()).thenReturn(159);
        when(annotation1.getFirstChild()).thenReturn(mock(DetailAST.class));
        when(annotation1.getFirstChild().getText()).thenReturn("TestAnnotation1");
        when(annotationHolder.getNextSibling()).thenReturn(annotation2);
        when(annotation2.getType()).thenReturn(159);
        when(annotation2.getFirstChild()).thenReturn(mock(DetailAST.class));
        when(annotation2.getFirstChild().getText()).thenReturn("TestAnnotation2");

        DetailAST result = utility.getAnnotation(ast, "TestAnnotation1");
        assertNotNull(result);
        assertSame(result, annotation1);

        result = utility.getAnnotation(ast, "TestAnnotation2");
        assertNotNull(result);
        assertSame(result, annotation2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAst() {
        utility.containsAnnotation(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAst() {
        utility.getAnnotationHolder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAst() {
        utility.getAnnotation(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyAnnotationName() {
        utility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_WhitespaceAnnotationName() {
        utility.getAnnotation(ast, " ");
    }
}