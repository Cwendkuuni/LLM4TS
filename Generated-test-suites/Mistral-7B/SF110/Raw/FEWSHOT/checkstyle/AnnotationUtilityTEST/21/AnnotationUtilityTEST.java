package com.puppycrawl.tools.checkstyle.utils;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Before;
import org.junit.Test;

public class AnnotationUtilityTest {
    private DetailAST ast;

    @Before
    public void setup() {
        String astString = "Annotation @Target(value = 1) Annotation2 @MyAnnotation name=\"John\"";
        ast = (DetailAST) CtParser.parse(astString).getRootNode();
    }

    @Test
    public void testContainsAnnotation_annotationPresent() {
        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));
    }

    @Test
    public void testContainsAnnotation_annotationNotFound() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testContainsAnnotation_astIsNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.containsAnnotation(null, "Annotation"));
    }

    @Test
    public void testContainsAnnotation_annotationHolderNull() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "Annotation"));
    }

    @Test
    public void testContainsAnnotation_annotationHolderContainsAnnotation() {
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test
    public void testContainsAnnotation_annotationHolderDoesNotContainAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testGetAnnotationHolder_annotationPresent() {
        DetailAST expected = ast.findFirstToken(TokenTypes.ANNOTATION);
        DetailAST actual = AnnotationUtility.getAnnotationHolder(ast);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetAnnotationHolder_annotationNotFound() {
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolder_astIsNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotationHolder(null));
    }

    @Test
    public void testGetAnnotation_annotationPresent() {
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
        DetailAST expected = ast.findFirstToken(TokenTypes.ANNOTATION).getFirstChild();
        DetailAST expectedName = expected.getFirstChild().getNextSibling();
        assertThat(expectedName.getText()).isEqualTo("MyAnnotation");
        assertThat(annotation).isEqualTo(expected);
    }

    @Test
    public void testGetAnnotation_annotationNotFound() {
        assertNull(AnnotationUtility.getAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testGetAnnotation_astIsNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(null, "Annotation"));
    }

    @Test
    public void testGetAnnotation_annotationNameNull() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotation_annotationNameEmptyOrSpaces() {
        assertThrows(IllegalArgumentException.class, () -> AnnotationUtility.getAnnotation(ast, ""));
    }

    @Test
    public void testGetAnnotation_annotationNameMatches() {
        String expectedName = "MyAnnotation";
        DetailAST actual = AnnotationUtility.getAnnotation(ast, expectedName);
        DetailAST expected = ast.findFirstToken(TokenTypes.ANNOTATION).getFirstChild();
        DetailAST expectedNameToken = expected.getFirstChild().getNextSibling();
        assertTrue(CharMatcher.quotedPattern(expectedName).matches(expectedNameToken.getText()));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetAnnotation_annotationNameDoesNotMatch() {
        String expectedName = "NonExistentAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, expectedName));
    }
}