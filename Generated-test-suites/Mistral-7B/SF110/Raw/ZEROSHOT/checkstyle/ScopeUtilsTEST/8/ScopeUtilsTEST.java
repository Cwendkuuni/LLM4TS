package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

@RunWith(Parameterized.class)
public class ScopeUtilsTest {

    private DetailAST ast;
    private Scope expectedScope;

    public ScopeUtilsTest(DetailAST ast, Scope expectedScope) {
        this.ast = ast;
        this.expectedScope = expectedScope;
    }

    @Parameterized.Parameters(name = "{index}: getScopeFromMods({0}) = {1}")
    public static Iterable<Object[]> dataForGetScopeFromMods() {
        return EnumSet.allOf(Scope.class).stream().map(scope -> new Object[] { createTestASTForMods("public"), scope })
                .map(obj -> new Object[] { (DetailAST) obj[0], (Scope) obj[1] }).build();
    }

    private static DetailAST createTestASTForMods(String modifier) {
        final DetailAST ast = new DetailAST();
        ast.setType(2);
        ast.setLine(1);
        ast.setColumn(1);
        ast.setText(modifier);
        final DetailAST modifiers = new DetailAST();
        modifiers.setType(5);
        modifiers.addChild(ast);
        return modifiers;
    }

    @Test
    public void testGetScopeFromMods() {
        assertEquals(expectedScope, ScopeUtils.getScopeFromMods(ast));
    }

    @Test(expected = NullPointerException.class)
    public void testGetScopeFromModsNull() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createTestAST();
        assertNull(ScopeUtils.getSurroundingScope(node));

        final DetailAST classNode = createTestAST();
        final DetailAST packageDeclaration = createTestASTForPackage();
        classNode.setParent(packageDeclaration);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(classNode));

        final DetailAST interfaceNode = createTestAST();
        final DetailAST interfaceDeclaration = createTestASTForInterface();
        interfaceNode.setParent(interfaceDeclaration);
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(interfaceNode));

        final DetailAST methodNode = createTestAST();
        final DetailAST methodDeclaration = createTestASTForMethod();
        methodNode.setParent(methodDeclaration);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(methodNode));

        final DetailAST annotationNode = createTestAST();
        final DetailAST annotationDeclaration = createTestASTForAnnotation();
        annotationNode.setParent(annotationDeclaration);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(annotationNode));

        final DetailAST enumNode = createTestAST();
        final DetailAST enumDeclaration = createTestASTForEnum();
        enumNode.setParent(enumDeclaration);
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(enumNode));

        final DetailAST innerClassNode = createTestAST();
        final DetailAST innerClassDeclaration = createTestASTForInnerClass();
        innerClassNode.setParent(createTestASTForMethod());
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(innerClassNode));
    }

    @Test
    public void testIsInInterfaceBlock() {
        assertFalse(ScopeUtils.isInInterfaceBlock(createTestAST()));

        final DetailAST interfaceNode = createTestASTForInterface();
        assertTrue(ScopeUtils.isInInterfaceBlock(interfaceNode));
    }

    @Test
    public void testIsInAnnotationBlock() {
        assertFalse(ScopeUtils.isInAnnotationBlock(createTestAST()));

        final DetailAST annotationNode = createTestASTForAnnotation();
        assertTrue(ScopeUtils.isInAnnotationBlock(annotationNode));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(createTestAST()));

        final DetailAST interfaceNode = createTestASTForInterface();
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(interfaceNode));

        final DetailAST annotationNode = createTestASTForAnnotation();
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(annotationNode));
    }

    @Test
    public void testIsInEnumBlock() {
        assertFalse(ScopeUtils.isInEnumBlock(createTestAST()));

        final DetailAST enumNode = createTestASTForEnum();
        assertTrue(ScopeUtils.isInEnumBlock(enumNode));
    }

    @Test
    public void testIsInCodeBlock() {
        assertFalse(ScopeUtils.isInCodeBlock(createTestAST()));

        final DetailAST nodeInCodeBlock = createTestASTForStatement();
        assertTrue(ScopeUtils.isInCodeBlock(nodeInCodeBlock));
    }

    @Test
    public void testIsOuterMostType() {
        assertTrue(ScopeUtils.isOuterMostType(createTestASTForClass()));

        final DetailAST methodNode = createTestASTForMethod();
        assertFalse(ScopeUtils.isOuterMostType(methodNode));
    }

    @Test
    public void testIsLocalVariableDef() {
        assertFalse(ScopeUtils.isLocalVariableDef(createTestAST()));

        final DetailAST localVariableDef = createTestASTForLocalVariable();
        assertTrue(ScopeUtils.isLocalVariableDef(localVariableDef));
    }

    @Test
    public void testIsClassFieldDef() {
        assertFalse(ScopeUtils.isClassFieldDef(createTestAST()));

        final DetailAST classFieldDef = createTestASTForClassField();
        assertTrue(ScopeUtils.isClassFieldDef(classFieldDef));
    }

    @Test
    public void testIsInScope() {
        DetailAST node = createTestASTForClass();
        assertTrue(ScopeUtils.isInScope(node, Scope.CLASS));

        node = createTestASTForMethod();
        assertFalse(ScopeUtils.isInScope(node, Scope.PACKAGE));

        node = createTestASTForMethod();
        assertTrue(ScopeUtils.isInScope(node, Scope.CLASS));
    }

    private DetailAST createTestAST() {
        return new DetailAST();
    }

    private DetailAST createTestASTForPackage() {
        final DetailAST ast = new DetailAST();
        ast.setType(1);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForInterface() {
        final DetailAST ast = new DetailAST();
        ast.setType(15);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForClass() {
        final DetailAST ast = new DetailAST();
        ast.setType(14);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForMethod() {
        final DetailAST ast = new DetailAST();
        ast.setType(157);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForAnnotation() {
        final DetailAST ast = new DetailAST();
        ast.setType(157);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForEnum() {
        final DetailAST ast = new DetailAST();
        ast.setType(154);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForInnerClass() {
        final DetailAST ast = new DetailAST();
        ast.setType(136);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForStatement() {
        final DetailAST ast = new DetailAST();
        ast.setType(9);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForLocalVariable() {
        final DetailAST ast = new DetailAST();
        ast.setType(10);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }

    private DetailAST createTestASTForClassField() {
        final DetailAST ast = new DetailAST();
        ast.setType(10);
        ast.setLine(1);
        ast.setColumn(1);
        return ast;
    }
}