package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import java.util.Objects;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_ValidAST_AnnotationPresent_ReturnTrue() {
        DetailAST ast = createValidAST();
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_ValidAST_AnnotationNotPresent_ReturnFalse() {
        DetailAST ast = createValidAST();
        String annotation = "WrongAnnotation";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotation_NullAST_ThrowException() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotation_ValidAST_NoAnnotationHolder_ReturnFalse() {
        DetailAST ast = createValidASTWithoutAnnotation();
        assertFalse(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_ValidAST_AnnotationHolder_ReturnTrue() {
        DetailAST ast = createValidAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolder_NullAST_ThrowException() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_AnnotationHolderFound_ReturnAnnotationHolder() {
        DetailAST ast = createValidAST();
        DetailAST expected = ast.findFirstToken(158);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, result);
    }

    @Test
    public void testGetAnnotationHolder_ValidAST_NoAnnotationHolder_ReturnNull() {
        DetailAST ast = createValidASTWithoutAnnotation();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullAST_ThrowException() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_NullASTAnnotation_ThrowException() {
        DetailAST ast = createValidAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation_EmptyOrSpacesAnnotation_ThrowException() {
        DetailAST ast = createValidAST();
        String annotation = " ";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotation_ValidASTValidAnnotation_ReturnAnnotation() {
        DetailAST ast = createValidAST();
        String annotation = "MyAnnotation";
        DetailAST expected = createAnnotationWithName("MyAnnotation");
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, result);
    }

    @Test
    public void testGetAnnotation_ValidASTInvalidAnnotation_ReturnNull() {
        DetailAST ast = createValidAST();
        String annotation = "WrongAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    private DetailAST createValidAST() {
        return new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            value = "annotationClass MyAnnotation {}";
        }};
    }

    private DetailAST createValidASTWithoutAnnotation() {
        return new DetailAST() {{
            type = TokenTypes.CLASS_DEF;
            value = "class MyClass {}";
        }};
    }

    private DetailAST createAnnotationWithName(String name) {
        return new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            value = "annotationClass MyAnnotation {}";
            children = new DetailAST[]{new DetailAST(){{ type = TokenTypes.IDENTIFIER; text = name; }}};
        }};
    }
}