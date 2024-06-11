package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.*;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockMods("public protected private");
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createNode();
        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSurroundingScopeNullNode() {
        ScopeUtils.getSurroundingScope(null);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createNodeInInterfaceBlock();
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createNodeInAnnotationBlock();
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createNodeInCodeBlock();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    private DetailAST createMockMods(String modifiers) {
        // Create a mock DetailAST for the modifiers
        // ...
        return mockMods;
    }

    private DetailAST createNode() {
        // Create a mock DetailAST node
        // ...
        return node;
    }

    private DetailAST createNodeInInterfaceBlock() {
        // Create a mock DetailAST node in an interface block
        // ...
        return node;
    }

    private DetailAST createNodeInAnnotationBlock() {
        // Create a mock DetailAST node in an annotation block
        // ...
        return node;
    }

    private DetailAST createNodeInCodeBlock() {
        // Create a mock DetailAST node in a code block
        // ...
        return node;
    }
}