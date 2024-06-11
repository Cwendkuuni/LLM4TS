package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.utils.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // ...
    }

    // Test other methods with similar structure

    @Test
    public void testGetScopeFromMods_NoModifiers() {
        DetailAST ast = new DetailAST();
        ast.setType(5); // MODS
        DetailAST returnValue = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, returnValue);
    }

    @Test(expected = NullPointerException.class)
    public void testGetScopeFromMods_NullDetailAST() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetScopeFromMods_MultipleModifiers() {
        // ...
    }

    @Test
    public void testGetScopeFromMods_InvalidModifier() {
        DetailAST ast = new DetailAST();
        ast.setType(5); // MODS
        ast.addChild(new DetailAST().setText("invalid"));
        DetailAST returnValue = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, returnValue);
    }

    // Test other methods with edge cases and exceptions
}