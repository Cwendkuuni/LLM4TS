package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Strings;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        // Assuming ast is a valid DetailAST object
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "SomeAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testContainsAnnotationWithValidAstButNoAnnotation() {
        DetailAST ast = new DetailAST();
        // Assuming ast is a valid DetailAST object
        assertFalse(AnnotationUtility.containsAnnotation(ast, "NonExistentAnnotation"));
    }

    @Test
    public void testGetAnnotationHolderWithValidAst() {
        DetailAST ast = new DetailAST();
        // Assuming ast is a valid DetailAST object
        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(annotationHolder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderWithNullAst() {
        DetailAST ast = null;
        AnnotationUtility.getAnnotationHolder(ast);
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        DetailAST ast = new DetailAST();
        // Assuming ast is a valid DetailAST object
        String annotation = "ValidAnnotation";
        DetailAST annotationInstance = new DetailAST();
        annotationInstance.setType(TokenTypes.ANNOTATION);
        annotationInstance.setText("ValidAnnotation");

        DetailAST annotationHolder = new DetailAST();
        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(TokenTypes.ANNOTATION);
        annotationNode.setText("ValidAnnotation");
        annotationHolder.addChild(annotationNode);
        annotationHolder.addChild(annotationInstance);

        ast.addChild(annotationHolder);

        DetailAST annotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(annotation);
        assertEquals(annotationInstance.getText(), annotation.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST ast = null;
        String annotation = "SomeAnnotation";
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = Strings.emptyString();
        AnnotationUtility.getAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationWithNonExistentAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = "NonExistentAnnotation";
        DetailAST annotationHolder = new DetailAST();
        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(TokenTypes.ANNOTATION);
        annotationNode.setText("ValidAnnotation");
        annotationHolder.addChild(annotationNode);
        ast.addChild(annotationHolder);

        DetailAST annotation = AnnotationUtility.getAnnotation(ast, annotation);
        assertNull(annotation);
    }
}