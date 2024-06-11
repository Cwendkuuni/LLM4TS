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
    public void testContainsAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotation_NullAnnotation() {
        DetailAST ast = getTestAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotation_EmptyAnnotation() {
        DetailAST ast = getTestAST();
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotation_NoAnnotation() {
        DetailAST ast = getTestAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotation"));
    }

    @Test
    public void testContainsAnnotation_HasAnnotation() {
        DetailAST ast = getTestASTWithAnnotation();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
    }

    @Test
    public void testContainsAnnotation_Type155() {
        DetailAST ast = getTestASTWithAnnotationType155();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
    }

    @Test
    public void testContainsAnnotation_Type16() {
        DetailAST ast = getTestASTWithAnnotationType16();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "testAnnotation"));
    }

    @Test
    public void testGetAnnotationHolder_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationHolder_NullAnnotationHolder() {
        DetailAST ast = getTestAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_Type155() {
        DetailAST ast = getTestASTWithAnnotationType155();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(annotationHolder).isNotNull();
        assertThat(annotationHolder.getType()).isEqualTo(TokenTypes.ANNOTATION);
    }

    @Test
    public void testGetAnnotationHolder_Type16() {
        DetailAST ast = getTestASTWithAnnotationType16();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(annotationHolder).isNotNull();
        assertThat(annotationHolder.getType()).isEqualTo(TokenTypes.PACKAGE_DECLARATION);
    }

    @Test
    public void testGetAnnotation_NullAst() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "annotation"));
    }

    @Test
    public void testGetAnnotation_NullAnnotation() {
        DetailAST ast = getTestAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_EmptyAnnotation() {
        DetailAST ast = getTestAST();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotation_NoAnnotation() {
        DetailAST ast = getTestAST();
        assertNull(AnnotationUtility.getAnnotation(ast, "annotation"));
    }

    @Test
    public void testGetAnnotation_HasAnnotation() {
        DetailAST ast = getTestASTWithAnnotation();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "testAnnotation");
        assertThat(annotation).isNotNull();
        assertThat(annotation.getType()).isEqualTo(TokenTypes.ANNOTATION);
    }

    @Test
    public void testGetAnnotation_WrongAnnotation() {
        DetailAST ast = getTestASTWithAnnotation();
        assertNull(AnnotationUtility.getAnnotation(ast, "wrongAnnotation"));
    }

    private DetailAST getTestAST() {
        return new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
        }};
    }

    private DetailAST getTestASTWithAnnotation() {
        DetailAST annotation = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setName("testAnnotation");
            setLine(1);
            setColumn(1);
        }};
        DetailAST annotationType = new DetailAST() {{
            setType(TokenTypes.ANNOTATION_TYPE_DEF);
            setName("testAnnotation");
            setLine(1);
            setColumn(1);
        }};
        DetailAST annotationDeclaration = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setName("testAnnotation");
            setLine(1);
            setColumn(1);
            addChild(annotationType);
            addChild(annotation);
        }};
        DetailAST ast = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setLine(1);
            setColumn(1);
            addChild(annotationDeclaration);
        }};
        return ast;
    }

    private DetailAST getTestASTWithAnnotationType155() {
        DetailAST annotation = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setName("testAnnotation");
            setLine(1);
            setColumn(1);
        }};
        DetailAST annotationType = new DetailAST() {{
            setType(TokenTypes.ANNOTATION_TYPE_DECL);
            setName("testAnnotation");
            setLine(1);
            setColumn(1);
        }};
        DetailAST annotationDeclaration = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setLine(1);
            setColumn(1);
            addChild(annotationType);
            addChild(annotation);
        }};
        DetailAST ast = new DetailAST() {{
            setType(TokenTypes.CLASS_DEF);
            setName("TestClass");
            setLine(1);
            setColumn(1);
            addChild(annotationDeclaration);
        }};
        return ast;
    }

    private DetailAST getTestASTWithAnnotationType16() {
        DetailAST annotation = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setName("testAnnotation");
            setLine(1);
            setColumn(1);
        }};
        DetailAST annotationType = new DetailAST() {{
            setType(TokenTypes.PACKAGE_DECLARATION);
            setName("package test.package");
            setLine(1);
            setColumn(1);
        }};
        DetailAST annotationDeclaration = new DetailAST() {{
            setType(TokenTypes.ANNOTATION);
            setLine(1);
            setColumn(1);
            addChild(annotationType);
            addChild(annotation);
        }};
        DetailAST ast = new DetailAST() {{
            setType(TokenTypes.CLASS_DEF);
            setName("TestClass");
            setLine(1);
            setColumn(1);
            addChild(annotationDeclaration);
        }};
        return ast;
    }
}