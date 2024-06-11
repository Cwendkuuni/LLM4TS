package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.Scope;

import antlr.collections.List;

import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods_Package() {
        DetailAST ast = new DetailAST();
        ast.setType(1);
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST ast = new DetailAST();
        ast.setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST ast = new DetailAST();
        ast.setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST ast = new DetailAST();
        ast.setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetScopeFromMods_MultipleModifiers() {
        DetailAST ast = new DetailAST();
        DetailAST mod1 = new DetailAST();
        DetailAST mod2 = new DetailAST();
        List modifiersList = new ArrayList();

        mod1.setText("public");
        mod2.setText("protected");
        modifiersList.add(mod1);
        modifiersList.add(mod2);
        ast.setChildren(modifiersList);
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(ast));
    }

    @Test
    public void testGetSurroundingScope_LocalVariable() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(9);

        node.setParent(parent);
        parent.setParent(grandParent);

        assertEquals(Scope.LOCAL, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_ClassField() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(96);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(9);

        node.setParent(parent);
        parent.setParent(grandParent);

        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_Method() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(35);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(9);

        node.setParent(parent);
        parent.setParent(grandParent);

        assertEquals(Scope.METHOD, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_AnonymousClass() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(136);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(9);

        node.setParent(parent);
        parent.setParent(grandParent);

        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_Interface() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(9);

        node.setParent(parent);
        parent.setParent(grandParent);

        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_InterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);

        assertEquals(Scope.INTERFACE_BLOCK, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_Enum() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        DetailAST grandParent = new DetailAST();
        grandParent.setType(9);

        node.setParent(parent);
        parent.setParent(grandParent);

        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_EnumBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);

        assertEquals(Scope.ENUM_BLOCK, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock_Interface() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);

        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_Method() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(35);

        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_Annotation() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);

        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_Method() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(35);

        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_Interface() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);

        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_Method() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(35);

        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock_Enum() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);

        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_Method() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(35);

        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock_CodeBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9);

        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsInCodeBlock_Comment() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(8);

        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType_OuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);

        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_NonOuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);

        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef_LocalVariableDef() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(7);

        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsLocalVariableDef_ClassField() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(96);

        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef_ClassField() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(96);

        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsClassFieldDef_LocalVariable() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(10);

        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope_InScope() {
        DetailAST ast = new DetailAST();
        DetailAST node = new DetailAST();

        DetailAST surroundingScope = new DetailAST();
        surroundingScope.setType(Scope.CLASS.getValue());

        ast.setType(Scope.CLASS.getValue());
        node.setParent(ast);
        ast.setChildren(node);

        assertTrue(ScopeUtils.isInScope(node, surroundingScope));
    }

    @Test
    public void testIsInScope_OutOfScope() {
        DetailAST ast = new DetailAST();
        DetailAST node = new DetailAST();

        DetailAST surroundingScope = new DetailAST();
        surroundingScope.setType(Scope.METHOD.getValue());

        ast.setType(Scope.CLASS.getValue());
        node.setParent(ast);
        ast.setChildren(node);

        assertFalse(ScopeUtils.isInScope(node, surroundingScope));
    }
}