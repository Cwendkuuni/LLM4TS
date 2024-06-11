package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_Package() {
        DetailAST ast = new DetailAST(1, 1, 1, 1, null);
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        Scope expected = Scope.PACKAGE;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST ast = new DetailAST(1, 1, "public".toCharArray(), 1, null);
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        Scope expected = Scope.PUBLIC;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST ast = new DetailAST(1, 1, "protected".toCharArray(), 1, null);
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        Scope expected = Scope.PROTECTED;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST ast = new DetailAST(1, 1, "private".toCharArray(), 1, null);
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        Scope expected = Scope.PRIVATE;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Null() {
        DetailAST ast = null;
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        assertNull(actual);
    }

    @Test
    public void testGetScopeFromMods_MultipleMods() {
        String[] mods = {"public", "protected", "private"};
        DetailAST ast = new DetailAST(1, 1, mods, mods.length, null);
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        Scope expected = Scope.PUBLIC; // assuming public takes precedence
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_NullNode() {
        DetailAST node = null;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertNull(actual);
    }

    @Test
    public void testGetSurroundingScope_InsideClass_PackageScope() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Scope expected = Scope.PACKAGE;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideClass_PublicScope() {
        DetailAST node = new DetailAST(1, 1, "public".toCharArray(), 1, null);
        DetailAST parent = new DetailAST(1, 1, 1, 1, null);
        DetailAST mods = new DetailAST(1, 1, null, 0, null);
        parent.setChild(0, node);
        parent.setChild(1, mods);
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Scope expected = Scope.PUBLIC;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideInterface_InterfaceScope() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        DetailAST parent = new DetailAST(1, 1, 15, 1, null);
        DetailAST mods = new DetailAST(1, 1, null, 0, null);
        parent.setChild(0, node);
        parent.setChild(1, mods);
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Scope expected = Scope.INTERFACE;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideAnnotation_AnnotationScope() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        DetailAST parent = new DetailAST(1, 1, 157, 1, null);
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Scope expected = Scope.ANNOTATION;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideAnonymousClass_AnonymousScope() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        DetailAST parent = new DetailAST(1, 1, 136, 1, null);
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Scope expected = Scope.ANONINNER;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInInterfaceBlock_InsideInterface() {
        DetailAST node = new DetailAST(1, 1, 15, 1, null);
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInInterfaceBlock_OutsideInterface() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInAnnotationBlock_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInAnnotationBlock_InsideAnnotation() {
        DetailAST node = new DetailAST(1, 1, 157, 1, null);
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInAnnotationBlock_OutsideAnnotation() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_InsideInterface() {
        DetailAST node = new DetailAST(1, 1, 15, 1, null);
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_InsideAnnotation() {
        DetailAST node = new DetailAST(1, 1, 157, 1, null);
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_OutsideInterfaceAndAnnotation() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInEnumBlock_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInEnumBlock_InsideEnum() {
        DetailAST node = new DetailAST(1, 1, 154, 1, null);
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInEnumBlock_OutsideEnum() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInCodeBlock_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsInCodeBlock_InsideCodeBlock() {
        DetailAST node = new DetailAST(1, 1, 9, 1, null);
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInCodeBlock_OutsideCodeBlock() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertFalse(actual);
    }

    @Test
    public void testIsOuterMostType_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertTrue(actual);
    }

    @Test
    public void testIsOuterMostType_OuterMostType() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertTrue(actual);
    }

    @Test
    public void testIsOuterMostType_NotOuterMostType() {
        DetailAST node = new DetailAST(1, 1, 15, 1, null);
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertFalse(actual);
    }

    @Test
    public void testIsLocalVariableDef_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertFalse(actual);
    }

    @Test
    public void testIsLocalVariableDef_LocalVariableDef() {
        DetailAST node = new DetailAST(1, 1, 10, 1, null);
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertTrue(actual);
    }

    @Test
    public void testIsLocalVariableDef_ClassFieldDef() {
        DetailAST node = new DetailAST(1, 1, 10, 1, null);
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertFalse(actual);
    }

    @Test
    public void testIsClassFieldDef_NullNode() {
        DetailAST node = null;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertFalse(actual);
    }

    @Test
    public void testIsClassFieldDef_ClassFieldDef() {
        DetailAST node = new DetailAST(1, 1, 10, 1, null);
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertTrue(actual);
    }

    @Test
    public void testIsInScope_NullNodeOrScope() {
        DetailAST node = null;
        Scope scope = null;
        boolean actual = ScopeUtils.isInScope(node, scope);
        assertFalse(actual);
    }

    @Test
    public void testIsInScope_InScope() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        Scope scope = Scope.PACKAGE;
        boolean actual = ScopeUtils.isInScope(node, scope);
        assertTrue(actual);
    }

    @Test
    public void testIsInScope_OutOfScope() {
        DetailAST node = new DetailAST(1, 1, 1, 1, null);
        Scope scope = Scope.INTERFACE;
        boolean actual = ScopeUtils.isInScope(node, scope);
        assertFalse(actual);
    }
}