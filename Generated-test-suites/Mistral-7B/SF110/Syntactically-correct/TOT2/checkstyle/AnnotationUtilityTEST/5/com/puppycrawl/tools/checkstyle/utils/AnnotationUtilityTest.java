
package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createSampleAST();
        String annotation = "ValidAnnotation";
        check(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidASTAndEmptyAnnotation() {
        DetailAST ast = createSampleAST();
        check(!AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAST() {
        checkArgument(AnnotationUtility.containsAnnotation(null, "ValidAnnotation") instanceof IllegalArgumentException);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        checkArgument(AnnotationUtility.containsAnnotation(createSampleAST(), null) instanceof IllegalArgumentException);
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = createSampleAST();
        check(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullASTForMethodTwo() {
        checkArgument(AnnotationUtility.containsAnnotation(null) instanceof IllegalArgumentException);
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = createSampleAST();
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        checkNotNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAST() {
        checkArgument(AnnotationUtility.getAnnotationHolder(null) instanceof IllegalArgumentException);
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = createSampleAST();
        String annotation = "ValidAnnotation";
        DetailAST annotationFound = AnnotationUtility.getAnnotation(ast, annotation);
        checkNotNull(annotationFound);
        check(annotation.equals(annotationFound.getText()));
    }

    @Test
    public void testGetAnnotationWithValidASTAndEmptyAnnotation() {
        DetailAST ast = createSampleAST();
        DetailAST annotationFound = AnnotationUtility.getAnnotation(ast, "");
        checkNull(annotationFound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAST() {
        checkArgument(AnnotationUtility.getAnnotation(null, "ValidAnnotation") instanceof IllegalArgumentException);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = createSampleAST();
        checkArgument(AnnotationUtility.getAnnotation(ast, null) instanceof IllegalArgumentException);
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotationMismatch() {
        DetailAST ast = createSampleAST();
        String annotation = "InvalidAnnotation";
        DetailAST annotationFound = AnnotationUtility.getAnnotation(ast, annotation);
        checkNull(annotationFound);
    }

    private DetailAST createSampleAST() {
        DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.ANNOTATION);
        // Add other necessary elements to the DetailAST
        return ast;
    }

    private void check(boolean condition) {
        assertTrue(condition);
    }

    private void check(boolean condition, String message) {
        assertTrue(condition);
        if (!condition) {
            fail(message);
        }
    }

    private void checkArgument(Object argument) {
        checkArgument(argument, "argument cannot be null");
    }

    private void checkArgument(Object argument, String errorMessage) {
        checkState(argument != null, errorMessage);
    }

    private void checkState(boolean condition) {
        assertTrue(condition);
    }
}
