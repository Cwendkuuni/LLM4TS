package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new AstFactory().newList(); // Empty list of AST nodes
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);

        // Test for public modifier
        DetailAST publicToken = new AstFactory().newToken(TokenTypes.MODIFIER_KEYWORD, "public");
        mods.add(publicToken);
        expectedScope = Scope.PUBLIC;
        actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);

        // Test for protected modifier
        DetailAST protectedToken = new AstFactory().newToken(TokenTypes.MODIFIER_KEYWORD, "protected");
        mods.add(protectedToken);
        expectedScope = Scope.PROTECTED;
        actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);

        // Test for private modifier
        DetailAST privateToken = new AstFactory().newToken(TokenTypes.MODIFIER_KEYWORD, "private");
        mods.add(privateToken);
        expectedScope = Scope.PRIVATE;
        actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);

        // Test for multiple modifiers
        DetailAST publicProtectedToken = new AstFactory().newToken(TokenTypes.MODIFIER_KEYWORD, "public protected");
        mods.add(publicProtectedToken);
        expectedScope = Scope.PROTECTED; // Protected has higher precedence than public
        actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = AssertionError.class)
    public void testGetScopeFromMods_NullMods() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        DetailAST parentToken = new AstFactory().newToken(TokenTypes.KEYWORD, "public");
        DetailAST mods = new AstFactory().newList();
        mods.add(parentToken);
        node.setParent(mods);
        expectedScope = Scope.PUBLIC;
        actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        DetailAST classDefToken = new AstFactory().newToken(TokenTypes.CLASS_DEF, "{ }");
        node.setParent(classDefToken);
        expectedScope = Scope.CLASS;
        actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        DetailAST interfaceToken = new AstFactory().newToken(TokenTypes.INTERFACE_DEF, "{ }");
        node.setParent(interfaceToken);
        expectedScope = Scope.INTERFACE;
        actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        DetailAST annotationToken = new AstFactory().newToken(TokenTypes.ANNOTATION, "@{ }");
        node.setParent(annotationToken);
        expectedScope = Scope.ANNOTATION;
        actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        DetailAST enumToken = new AstFactory().newToken(TokenTypes.ENUM_DEF, "{ }");
        node.setParent(enumToken);
        expectedScope = Scope.ENUM;
        actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        DetailAST anonInnerToken = new AstFactory().newToken(TokenTypes.ANON_CLASS_DEF, "{ }");
        node.setParent(anonInnerToken);
        expectedScope = Scope.ANONINNER;
        actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    // Add more test cases for other methods in ScopeUtils class
    // ...

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new AstFactory().newToken(TokenTypes.KEYWORD, "interface");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new AstFactory().newToken(TokenTypes.KEYWORD, "annotation");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new AstFactory().newToken(TokenTypes.KEYWORD, "interface");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new AstFactory().newToken(TokenTypes.KEYWORD, "annotation");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NullNode() {
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(null));
    }

    // Add more test cases for other methods in ScopeUtils class
    // ...

    @Test
    public void testIsInScope() {
        DetailAST node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        Scope expectedScope = Scope.CLASS;
        DetailAST ast = new AstFactory().newAST();
        ast.setType(TokenTypes.FILE_UNIT);
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertTrue(ScopeUtils.isInScope(ast, expectedScope));
        assertTrue(ScopeUtils.isInScope(node, expectedScope));
        assertFalse(ScopeUtils.isInScope(node, Scope.PACKAGE));
    }

    @Test
    public void testIsInScope_NullAst() {
        DetailAST node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        Scope expectedScope = Scope.CLASS;
        assertFalse(ScopeUtils.isInScope(null, expectedScope));
        assertFalse(ScopeUtils.isInScope(node, null));
    }

    @Test
    public void testIsInScope_InvalidScope() {
        DetailAST node = new AstFactory().newToken(TokenTypes.IDENTIFIER, "TestClass");
        Scope expectedScope = Scope.CLASS;
        Scope invalidScope = new Scope("INVALID_SCOPE");
        assertFalse(ScopeUtils.isInScope(node, invalidScope));
    }
}