package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = getTestAst();
        String ann1 = "MyAnnotation";
        String ann2 = " javax.annotation.Nonnull ";

        // test with valid annotation
        assertThat(AnnotationUtility.containsAnnotation(ast, ann1)).isTrue();

        // test with valid annotation including whitespace
        assertThat(AnnotationUtility.containsAnnotation(ast, ann2)).isTrue();
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertThrows("the ast is null", IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(null, "MyAnnotation");
        });
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = getTestAst();
        assertThrows("the annotation is null", IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, null);
        });
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = getTestAst();
        assertThrows("the annotation is empty or spaces", IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(ast, "");
        });

        DetailAST astWithAnnotation = getTestAstWithAnnotation();
        assertThrows("the annotation is empty or spaces", IllegalArgumentException.class, () -> {
            AnnotationUtility.containsAnnotation(astWithAnnotation, " ");
        });
    }

    @Test
    public void testContainsAnnotationWithInvalidAST() {
        DetailAST invalidAst = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(invalidAst, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNoAnnotation() {
        DetailAST ast = getTestAst();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NoAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = getTestAst();
        DetailAST expected = ast.findFirstToken(TokenTypes.CLASS_DEF);
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        assertNull(AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotationWithValidAST() {
        DetailAST ast = getTestAstWithAnnotation();
        String annotation = "MyAnnotation";
        DetailAST expected = ast.findFirstChildOfType(TokenTypes.ANNOTATION, 1);
        DetailAST actual = AnnotationUtility.getAnnotation(ast, annotation);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertNull(AnnotationUtility.getAnnotation(null, "MyAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = getTestAstWithAnnotation();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = getTestAstWithAnnotation();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotationWithInvalidAST() {
        DetailAST invalidAst = new DetailAST();
        String annotation = "MyAnnotation";
        assertNull(AnnotationUtility.getAnnotation(invalidAst, annotation));
    }

    @Test
    public void testGetAnnotationWithNoMatchingAnnotation() {
        DetailAST ast = getTestAstWithAnnotation();
        String noMatchingAnnotation = "NoMatchingAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, noMatchingAnnotation));
    }

    private DetailAST getTestAst() {
        // create a test DetailAST
        DetailAST ast = new DetailAST();
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST getTestAstWithAnnotation() {
        DetailAST ast = new DetailAST();
        ast.setLine(1);
        ast.setColumn(1);
        DetailAST classDef = new DetailAST();
        classDef.setType(TokenTypes.CLASS_DEF);
        DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.ANNOTATION);
        DetailAST name = new DetailAST();
        name.setText("MyAnnotation");
        DetailAST openParenthesis = new DetailAST();
        openParenthesis.setType(TokenTypes.LEFT_PAREN);
        DetailAST rightParenthesis = new DetailAST();
        rightParenthesis.setType(TokenTypes.RIGHT_PAREN);
        annotation.addChild(name);
        annotation.addChild(openParenthesis);
        annotation.addChild(rightParenthesis);
        classDef.addChild(annotation);
        ast.addChild(classDef);
        return ast;
    }
}