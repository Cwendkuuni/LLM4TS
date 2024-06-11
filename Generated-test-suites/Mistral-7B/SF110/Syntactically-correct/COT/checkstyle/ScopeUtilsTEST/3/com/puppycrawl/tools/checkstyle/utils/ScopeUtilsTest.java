package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createModsAST("public protected private");
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_NoScopeDefined() {
        DetailAST mods = createModsAST("");
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, actualScope);
    }

    @Test
    public void testGetScopeFromMods_EmptyMods() {
        DetailAST mods = createModsAST(null);
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_NullMods() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createNode("class Test { public void test() { int x; } }");
        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_TopLevelClass() {
        DetailAST node = createNode("class Test { }");
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_Interface() {
        DetailAST node = createNode("interface Test { }");
        Scope expectedScope = Scope.INTERFACE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_Enum() {
        DetailAST node = createNode("enum Test { VALUE }");
        Scope expectedScope = Scope.ENUM;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_AnonymousInnerClass() {
        DetailAST node = createNode("class Test { void test() { new TestInner() { int x; }; } } class TestInner { }");
        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createNode("interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_NoInterface() {
        DetailAST node = createNode("class Test { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createNode("@annotation Test { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_NoAnnotation() {
        DetailAST node = createNode("class Test { }");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createNode("interface Test { } @annotation Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NoInterfaceOrAnnotation() {
        DetailAST node = createNode("class Test { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createNode("enum Test { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_NoEnum() {
        DetailAST node = createNode("class Test { }");
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createNode("class Test { int x; }");
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createNode("class Test { int x; }");
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_InnerClass() {
        DetailAST node = createNode("class Test { int x; class Inner { int y; } }");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createNode("class Test { int x; }");
        DetailAST localVariableNode = createNode("int x;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        assertTrue(ScopeUtils.isLocalVariableDef(localVariableNode));
    }

    @Test
    public void testIsLocalVariableDef_NoLocalVariable() {
        DetailAST node = createNode("class Test { int x; }");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createNode("class Test { int x; }");
        DetailAST classFieldNode = createNode("int x;");
        assertTrue(ScopeUtils.isClassFieldDef(node));
        assertTrue(ScopeUtils.isClassFieldDef(classFieldNode));
    }

    @Test
    public void testIsClassFieldDef_NoField() {
        DetailAST node = createNode("class Test { int x; }");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST node = createNode("class Test { int x; }");
        Scope expectedScope = Scope.CLASS;
        assertTrue(ScopeUtils.isInScope(node, expectedScope));
    }

    @Test
    public void testIsInScope_OutOfScope() {
        DetailAST node = createNode("class Test { int x; }");
        Scope unexpectedScope = Scope.PACKAGE;
        assertFalse(ScopeUtils.isInScope(node, unexpectedScope));
    }

    @Test
    public void testIsInScope_NullNode() {
        Scope scope = Scope.PACKAGE;
        assertNull(ScopeUtils.isInScope(null, scope));
    }

    @Test
    public void testIsInScope_NullScope() {
        DetailAST node = createNode("class Test { int x; }");
        assertNull(ScopeUtils.isInScope(node, null));
    }

    private DetailAST createNode(String code) {
        final DetailAST ast = new JlexVisitor().visit(CodeModelUtil.createStringTree(code), null);
        return ast;
    }

    private DetailAST createModsAST(String modifiers) {
        final MapToken modsToken = new MapToken("modifiers", null, null, 0);
        modsToken.setLine(1);
        modsToken.setColumn(1);
        for (String modifier : modifiers.split("\\s+")) {
            TokenType tokenType = null;
            if ("public".equals(modifier)) {
                tokenType = TokenTypes.MOD_PUBLIC;
            } else if ("protected".equals(modifier)) {
                tokenType = TokenTypes.MOD_PROTECTED;
            } else if ("private".equals(modifier)) {
                tokenType = TokenTypes.MOD_PRIVATE;
            }
            if (tokenType != null) {
                final Token modifierToken = new Token(tokenType, modifier, null, null, null);
                modifierToken.setLine(1);
                modifierToken.setColumn(modifier.length() + 1);
                modsToken.addElement(modifierToken);
            }
        }
        return modsToken;
    }
}