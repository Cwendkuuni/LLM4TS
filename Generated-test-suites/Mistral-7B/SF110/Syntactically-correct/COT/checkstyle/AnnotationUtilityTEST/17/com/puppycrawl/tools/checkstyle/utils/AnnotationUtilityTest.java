package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        AnnotationUtility.containsAnnotation(ast, " ");
    }

    @Test
    public void testContainsAnnotationNoAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotationWithAnnotation() {
        DetailAST ast = createAnnotationNode();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolderEmptyAst() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationHolderType155() {
        DetailAST ast = createAnnotationNodeType155();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(158, holder.getType());
    }

    @Test
    public void testGetAnnotationHolderType16() {
        DetailAST ast = createAnnotationNodeType16();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(5, holder.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAstOrAnnotation() {
        DetailAST ast = createAnnotationNode();
        AnnotationUtility.getAnnotation(null, ast.getText());
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyAnnotation() {
        DetailAST ast = createAnnotationNode();
        AnnotationUtility.getAnnotation(ast, " ");
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullAstOrAnnotationThrowsNullPointerException() {
        DetailAST ast = createAnnotationNode();
        AnnotationUtility getAnnotation = new AnnotationUtility() {}; // create an instance to suppress "do not instantiate" error
        getAnnotation.getAnnotation(null, ast.getText());
        getAnnotation.getAnnotation(ast, null);
    }

    @Test
    public void testGetAnnotationNoAnnotationFound() {
        DetailAST ast = createAnnotationNode();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "nonExistentAnnotation");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationFound() {
        DetailAST ast = createAnnotationNode();
        DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
        assertEquals(159, result.getType());
    }

    private DetailAST createAnnotationNode() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        DetailAST holder = new DetailAST();
        holder.setType(158);
        ast.setParent(holder);
        DetailAST name = new DetailAST();
        name.setText("packageName.annotationName");
        DetailAST firstChild = new DetailAST();
        firstChild.setType(159);
        firstChild.setParent(name);
        name.setParent(ast);
        return ast;
    }

    private DetailAST createAnnotationNodeType155() {
        DetailAST ast = new DetailAST();
        ast.setType(155);
        return ast;
    }

    private DetailAST createAnnotationNodeType16() {
        DetailAST ast = new DetailAST();
        ast.setType(16);
        return ast;
    }
}