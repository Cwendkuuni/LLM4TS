package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST mods = new DetailAST();
        DetailAST publicKeyword = new DetailAST();
        publicKeyword.setText("public");
        mods.addChild(publicKeyword);
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST mods = new DetailAST();
        DetailAST protectedKeyword = new DetailAST();
        protectedKeyword.setText("protected");
        mods.addChild(protectedKeyword);
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PROTECTED, scope);
    }

    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST mods = new DetailAST();
        DetailAST privateKeyword = new DetailAST();
        privateKeyword.setText("private");
        mods.addChild(privateKeyword);
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testGetScopeFromMods_NoModifiers() {
        DetailAST mods = new DetailAST();
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope_ClassLevel() {
        DetailAST node = new DetailAST();
        node.setType(14); // Type.CLASS_DEF
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope_MethodLevel() {
        DetailAST node = new DetailAST();
        node.setType(15); // Type.METHOD_DEF
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope_AnonymousClassLevel() {
        DetailAST node = new DetailAST();
        node.setType(136); // Type.ANON_CLASS_DEF
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, scope);
    }

    @Test
    public void testGetSurroundingScope_InterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15); // Type.INTERFACE_DEF
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.INTERFACE, scope);
    }

    @Test
    public void testGetSurroundingScope_AnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157); // Type.ANNOTATION
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANNOTATION, scope);
    }

    @Test
    public void testGetSurroundingScope_EnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154); // Type.ENUM
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ENUM, scope);
    }

    @Test
    public void testGetSurroundingScope_CodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9); // Type.BLOCK
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.CODE, scope);
    }

    @Test
    public void testIsInInterfaceBlock_InterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15); // Type.INTERFACE_DEF
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_ClassLevel() {
        DetailAST node = new DetailAST();
        node.setType(14); // Type.CLASS_DEF
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_AnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157); // Type.ANNOTATION
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_ClassLevel() {
        DetailAST node = new DetailAST();
        node.setType(14); // Type.CLASS_DEF
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_InterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15); // Type.INTERFACE_DEF
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_AnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157); // Type.ANNOTATION
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_ClassLevel() {
        DetailAST node = new DetailAST();
        node.setType(14); // Type.CLASS_DEF
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock_EnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154); // Type.ENUM
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_ClassLevel() {
        DetailAST node = new DetailAST();
        node.setType(14); // Type.CLASS_DEF
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock_CodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9); // Type.BLOCK
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsInCodeBlock_NonCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(15); // Type.INTERFACE_DEF
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType_OuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(14); // Type.CLASS_DEF
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_InnerType() {
        DetailAST node = new DetailAST();
        node.setType(136); // Type.ANON_CLASS_DEF
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef_LocalVariable() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.VAR_DEF
        DetailAST parent = new DetailAST();
        parent.setType(7); // Type.VAR_DECL
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsLocalVariableDef_Field() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.FIELD_DEF
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef_ClassField() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.FIELD_DEF
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsClassFieldDef_LocalVariable() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.VAR_DEF
        DetailAST parent = new DetailAST();
        parent.setType(35); // Type.VAR_DECL_LIST
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope_InScope() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.FIELD_DEF
        Scope scope = Scope.PACKAGE;
        assertTrue(ScopeUtils.isInScope(node, scope));
    }

    @Test
    public void testIsInScope_OutOfScope() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.FIELD_DEF
        Scope scope = Scope.ANNOTATION;
        assertFalse(ScopeUtils.isInScope(node, scope));
    }

    @Test
    public void testIsInScope_NullScope() {
        DetailAST node = new DetailAST();
        node.setType(10); // Type.FIELD_DEF
        assertNull(ScopeUtils.getSurroundingScope(node));
    }
}