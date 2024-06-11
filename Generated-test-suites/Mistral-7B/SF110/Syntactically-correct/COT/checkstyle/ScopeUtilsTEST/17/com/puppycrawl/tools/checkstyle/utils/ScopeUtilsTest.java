package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.List;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockMods("public protected private");
        Scope expected = Scope.PROTECTED;
        Scope result = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, result);
    }

    @Test
    public void testGetScopeFromMods_emptyMods() {
        DetailAST mods = createMockMods("");
        Scope expected = Scope.PACKAGE;
        Scope result = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_nullMods() {
        DetailAST mods = null;
        ScopeUtils.getScopeFromMods(mods);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createMockNode(15);
        Scope expected = Scope.CLASS;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_interfaceBlock() {
        DetailAST node = createMockNode(15);
        DetailAST interfaceBlock = createMockNode(15);
        node.setParent(interfaceBlock);
        Scope expected = Scope.INTERFACE;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_annotationBlock() {
        DetailAST node = createMockNode(157);
        DetailAST annotationBlock = createMockNode(157);
        node.setParent(annotationBlock);
        Scope expected = Scope.ANNOTATION;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_anonymousInnerClass() {
        DetailAST node = createMockNode(136);
        Scope expected = Scope.ANONINNER;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createMockNode(15);
        boolean expected = false;
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceBlock_interfaceNode() {
        DetailAST node = createMockNode(15);
        DetailAST interfaceBlock = createMockNode(15);
        node.setParent(interfaceBlock);
        boolean expected = true;
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createMockNode(157);
        boolean expected = false;
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInAnnotationBlock_annotationNode() {
        DetailAST node = createMockNode(157);
        DetailAST annotationBlock = createMockNode(157);
        node.setParent(annotationBlock);
        boolean expected = true;
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createMockNode(15);
        boolean expected = false;
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_interfaceOrAnnotationNode() {
        DetailAST node = createMockNode(15);
        DetailAST interfaceOrAnnotationBlock = createMockNode(15);
        node.setParent(interfaceOrAnnotationBlock);
        boolean expected = true;
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createMockNode(154);
        boolean expected = false;
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInEnumBlock_enumNode() {
        DetailAST node = createMockNode(154);
        DetailAST enumBlock = createMockNode(154);
        node.setParent(enumBlock);
        boolean expected = true;
        boolean result = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createMockNode(9);
        boolean expected = true;
        boolean result = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createMockNode(10);
        boolean expected = true;
        boolean result = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsOuterMostType_notOuterMostType() {
        DetailAST node = createMockNode(15);
        boolean expected = false;
        boolean result = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createMockNode(10);
        boolean expected = false;
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLocalVariableDef_localVariableDefNode() {
        DetailAST node = createMockNode(10);
        DetailAST localVariableDef = createMockNode(10);
        node.setParent(localVariableDef);
        boolean expected = true;
        boolean result = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createMockNode(10);
        boolean expected = false;
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsClassFieldDef_classFieldDefNode() {
        DetailAST node = createMockNode(10);
        DetailAST classFieldDef = createMockNode(10);
        node.setParent(classFieldDef);
        boolean expected = true;
        boolean result = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createMockNode(15);
        Scope scope = Scope.CLASS;
        boolean expected = true;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInScope_notInScope() {
        DetailAST ast = createMockNode(15);
        Scope scope = Scope.PACKAGE;
        boolean expected = false;
        boolean result = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, result);
    }

    private DetailAST createMockMods(String modifiers) {
        DetailAST mods = new DetailASTMock();
        List<Token> modifierTokens = new ArrayList<>();
        for (String modifier : modifiers.split(" ")) {
            modifierTokens.add(new TokenMock(Integer.MIN_VALUE, modifier, ""));
        }
        mods.setChildren(modifierTokens.toArray(new Token[modifierTokens.size()]));
        return mods;
    }

    private DetailAST createMockNode(int type) {
        DetailAST node = new DetailASTMock();
        node.setType(type);
        return node;
    }
}