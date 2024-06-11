package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName", "annotationValue"};
            setProperty("annotationTypeName", "MyAnnotation");
        }};
        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithoutAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
        }};
        assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "Annotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
        }};
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
        }};
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithWhiteSpaceAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
        }};
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, " \t MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNonAnnotationNode() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ASSIGNMENT;
        }};
        assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName", "annotationValue"};
            setProperty("annotationTypeName", "MyAnnotation");
            setProperty("annotationValue", "value");
            DetailAST annotation = new DetailAST() {{
                type = TokenTypes.ANNOTATION;
                properties = new String[]{"annotationName"};
                setProperty("annotationName", "annotation");
            }};
            addChild(annotation);
        }};
        DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
        DetailAST expected = ast.getLastChild();
        assertEquals(expected, result);
    }

    @Test
    public void testGetAnnotationWithNonAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ASSIGNMENT;
        }};
        assertNull(AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "Annotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
        }};
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotationName() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
        }};
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithAnnotationNotPresent() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
        }};
        assertNull(AnnotationUtility.getAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithWhiteSpaceAnnotationName() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
        }};
        assertNull(AnnotationUtility.getAnnotation(ast, " \t MyAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
        }};
        DetailAST expected = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
        }};
        ast.addChild(expected);
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, result);
    }

    @Test
    public void testGetAnnotationHolderWithNonAnnotation() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ASSIGNMENT;
        }};
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderWithNullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testContainsAnnotationWithMultipleAnnotations() {
        DetailAST ast = new DetailAST() {{
            type = TokenTypes.ANNOTATION;
            properties = new String[]{"annotationTypeName"};
            DetailAST firstAnnotation = new DetailAST() {{
                type = TokenTypes.ANNOTATION;
                properties = new String[]{"annotationName"};
                setProperty("annotationName", "Annotation1");
            }};
            DetailAST secondAnnotation = new DetailAST() {{
                type = TokenTypes.ANNOTATION;
                properties = new String[]{"annotationName"};
                setProperty("annotationName", "Annotation2");
            }};
            addChild(firstAnnotation);
            addChild(secondAnnotation);
        }};
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation1"));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "Annotation2"));
    }

    private void assertEquals(DetailAST expected, DetailAST result) {
        assertThat(expected).isEqualTo(result);
    }
}