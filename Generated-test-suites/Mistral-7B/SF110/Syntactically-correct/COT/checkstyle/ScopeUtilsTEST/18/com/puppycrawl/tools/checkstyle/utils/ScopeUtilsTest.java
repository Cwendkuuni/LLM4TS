package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.List;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockMods();
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_noMatchingModifier_returnsPackgeScope() {
        DetailAST mods = createMockMods();
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_nullMods_throwsIllegalArgumentException() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createMockNode();
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(15);
        expected = Scope.CLASS;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(157);
        expected = Scope.ANNOTATION;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(154);
        expected = Scope.ENUM;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(136);
        expected = Scope.ANONINNER;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(14);
        expected = null;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(154, 15);
        expected = Scope.INTERFACE;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSurroundingScope_nullNode_throwsIllegalArgumentException() {
        ScopeUtils.getSurroundingScope(null);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(15);
        expected = true;
        actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(157);
        expected = true;
        actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(15);
        expected = true;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(157);
        expected = true;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(154);
        expected = true;
        actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(9);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(8);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(11);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(12);
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createMockNode();
        boolean expected = true;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);

        node = createNodeWithParentOfType(14);
        expected = false;
        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        node = createMockVariableDefinition();
        expected = true;
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createMockNode();
        boolean expected = false;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);

        node = createMockVariableDefinition();
        expected = false;
        actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);

        node = createMockClassFieldDefinition();
        expected = true;
        actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope() {
        DetailAST node = createMockNode();
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getScopeFromMods(createMockMods());
        assertTrue(ScopeUtils.isInScope(node, expected));

        node = createNodeWithParentOfType(15);
        expected = Scope.CLASS;
        assertTrue(ScopeUtils.isInScope(node, expected));

        node = createNodeWithParentOfType(157);
        expected = Scope.ANNOTATION;
        assertTrue(ScopeUtils.isInScope(node, expected));

        node = createNodeWithParentOfType(154);
        expected = Scope.ENUM;
        assertTrue(ScopeUtils.isInScope(node, expected));

        node = createNodeWithParentOfType(136);
        expected = Scope.ANONINNER;
        assertTrue(ScopeUtils.isInScope(node, expected));

        node = createNodeWithParentOfType(14);
        expected = null;
        assertFalse(ScopeUtils.isInScope(node, expected));

        node = createNodeWithParentOfType(15, Scope.INTERFACE);
        expected = Scope.INTERFACE;
        assertTrue(ScopeUtils.isInScope(node, expected));
    }

    private DetailAST createMockMods() {
        List mockMods = new BasicList();
        DetailAST modsAST = new DetailAST();
        modsAST.setType(1);
        modsAST.setLine(1);
        modsAST.setColumn(1);
        modsAST.setParent(mockMods);
        DetailAST publicToken = new DetailAST();
        publicToken.setType(3);
        publicToken.setText("public");
        publicToken.setLine(1);
        publicToken.setColumn(1);
        publicToken.setParent(modsAST);
        return modsAST;
    }

    private DetailAST createMockNode() {
        DetailAST mockNode = new DetailAST();
        mockNode.setType(1);
        mockNode.setLine(1);
        mockNode.setColumn(1);
        return mockNode;
    }

    private DetailAST createNodeWithParentOfType(int... parentTypes) {
        DetailAST parent = createMockNode();
        for (int parentType : parentTypes) {
            parent = new DetailAST();
            parent.setType(parentType);
            parent.setParent(parent);
        }

        DetailAST child = createMockNode();
        child.setParent(parent);
        return child;
    }

    private DetailAST createMockVariableDefinition() {
        DetailAST mockVariableDefinition = new DetailAST();
        mockVariableDefinition.setType(10);
        mockVariableDefinition.setLine(1);
        mockVariableDefinition.setColumn(1);
        return mockVariableDefinition;
    }

    private DetailAST createMockClassFieldDefinition() {
        DetailAST mockClassFieldDefinition = new DetailAST();
        mockClassFieldDefinition.setType(10);
        mockClassFieldDefinition.setLine(1);
        mockClassFieldDefinition.setColumn(1);
        return mockClassFieldDefinition;
    }
}