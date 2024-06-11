package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        mods.addChild(AST.make("public"));
        AST returnValue = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, returnValue);

        mods.addChild(AST.make("protected"));
        returnValue = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PROTECTED, returnValue);

        mods.addChild(AST.make("private"));
        returnValue = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, returnValue);

        mods.addChild(AST.make("default"));
        returnValue = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, returnValue);
    }

    @Test
    public void testGetScopeFromMods_noAccessModifier() {
        AST mods = new DetailAST();
        AST returnValue = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, returnValue);
    }

    @Test
    public void testGetScopeFromMods_multipleAccessModifiers() {
        DetailAST mods = new DetailAST();
        mods.addChild(AST.make("public"));
        mods.addChild(AST.make("protected"));
        mods.addChild(AST.make("private"));
        AST returnValue = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, returnValue);
    }

    @Test
    public void testGetSurroundingScope_publicClass() {
        DetailAST node = new DetailAST();
        node.setType(14);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, returnValue);
    }

    @Test
    public void testGetSurroundingScope_packageClass() {
        DetailAST node = new DetailAST();
        node.setType(7);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, returnValue);
    }

    @Test
    public void testGetSurroundingScope_protectedClass() {
        DetailAST node = new DetailAST();
        node.setType(15);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PROTECTED, returnValue);
    }

    @Test
    public void testGetSurroundingScope_privateClass() {
        DetailAST node = new DetailAST();
        node.setType(16);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PRIVATE, returnValue);
    }

    @Test
    public void testGetSurroundingScope_interface() {
        DetailAST node = new DetailAST();
        node.setType(15);
        DetailAST parent = new DetailAST();
        parent.setType(154);
        parent.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.INTERFACE, returnValue);
    }

    @Test
    public void testGetSurroundingScope_innerClass() {
        DetailAST node = new DetailAST();
        node.setType(136);
        DetailAST parent = new DetailAST();
        parent.setType(15);
        parent.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, returnValue);
    }

    @Test
    public void testIsInInterfaceBlock_interfaceDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(15);
        AST returnValue = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInInterfaceBlock_classDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(14);
        AST returnValue = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(returnValue);
    }

    @Test
    public void testIsInAnnotationBlock_annotationDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(157);
        AST returnValue = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInAnnotationBlock_classDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(14);
        AST returnValue = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(returnValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_interfaceDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(15);
        AST returnValue = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_classDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(14);
        AST returnValue = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(returnValue);
    }

    @Test
    public void testIsInEnumBlock_enumDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(154);
        AST returnValue = ScopeUtils.isInEnumBlock(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInEnumBlock_classDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(14);
        AST returnValue = ScopeUtils.isInEnumBlock(node);
        assertFalse(returnValue);
    }

    @Test
    public void testIsInCodeBlock_codeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9);
        AST returnValue = ScopeUtils.isInCodeBlock(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(14);
        AST returnValue = ScopeUtils.isOuterMostType(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsOuterMostType_innerClass() {
        DetailAST node = new DetailAST();
        node.setType(136);
        AST returnValue = ScopeUtils.isOuterMostType(node);
        assertFalse(returnValue);
    }

    @Test
    public void testIsLocalVariableDef_localVariableDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(10);
        AST returnValue = ScopeUtils.isLocalVariableDef(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsLocalVariableDef_classVariableDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(10);
        AST returnValue = ScopeUtils.isLocalVariableDef(node);
        assertFalse(returnValue);
    }

    @Test
    public void testIsClassFieldDef_classVariableDeclaration() {
        DetailAST node = new DetailAST();
        node.setType(10);
        AST returnValue = ScopeUtils.isClassFieldDef(node);
        assertTrue(returnValue);
    }

    @Test
    public void testIsInScope() {
        DetailAST node = new DetailAST();
        node.setType(10);
        Scope expectedScope = Scope.PACKAGE;
        DetailAST surroundingScope = new DetailAST();
        surroundingScope.setType(5);
        surroundingScope.addChild(new DetailAST().setType(14).setName("MyClass"));
        surroundingScope.addChild(node);
        AST returnValue = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, returnValue);
        assertTrue(ScopeUtils.isInScope(node, expectedScope));

        surroundingScope.addChild(new DetailAST().setType(15).setName("MyInterface"));
        assertFalse(ScopeUtils.isInScope(node, expectedScope));

        surroundingScope.addChild(new DetailAST().setType(157).setName("MyAnnotation"));
        assertFalse(ScopeUtils.isInScope(node, expectedScope));

        surroundingScope.addChild(new DetailAST().setType(154).setName("MyEnum"));
        assertFalse(ScopeUtils.isInScope(node, expectedScope));

        surroundingScope.addChild(new DetailAST().setType(136).setName("MyInnerClass"));
        assertFalse(ScopeUtils.isInScope(node, expectedScope));
    }

    @Test
    public void testIsInScope_outermostType() {
        DetailAST node = new DetailAST();
        node.setType(14);
        Scope expectedScope = Scope.PACKAGE;
        DetailAST surroundingScope = new DetailAST();
        surroundingScope.setType(5);
        surroundingScope.addChild(node);
        assertTrue(ScopeUtils.isInScope(node, expectedScope));
    }

    @Test
    public void testIsInScope_outermostType_withParent() {
        DetailAST node = new DetailAST();
        node.setType(14);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);
        Scope expectedScope = Scope.PUBLIC;
        DetailAST surroundingScope = new DetailAST();
        surroundingScope.setType(5);
        surroundingScope.addChild(parent);
        assertTrue(ScopeUtils.isInScope(node, expectedScope));
    }

    @Test
    public void testIsInScope_outermostType_withParent_differentScope() {
        DetailAST node = new DetailAST();
        node.setType(14);
        DetailAST parent = new DetailAST();
        parent.setType(15);
        parent.addChild(node);
        Scope expectedScope = Scope.INTERFACE;
        DetailAST surroundingScope = new DetailAST();
        surroundingScope.setType(5);
        surroundingScope.addChild(parent);
        assertFalse(ScopeUtils.isInScope(node, expectedScope));
    }

}