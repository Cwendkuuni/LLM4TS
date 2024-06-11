import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new BasicAST(); // AST object to simulate the input
        Scope expectedScope = PUBLIC;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_DefaultToPackageScope() {
        DetailAST mods = new BasicAST(); // AST object to simulate the input with no valid scope keywords
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_NullModsInput() {
        ScopeUtils.getScopeFromMods(null);
    }

    // Add similar test cases for other methods, replacing the method name and expected scope values as needed

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new BasicAST(); // AST object to simulate the input
        Scope expectedScope = null;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_ReturnScopeAnnoninner() {
        DetailAST node = new BasicAST(); // AST object to simulate the input as an anonymous inner class
        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSurroundingScope_NullNodeInput() {
        ScopeUtils.getSurroundingScope(null);
    }

    // Add test cases for other methods following a similar pattern, don't forget to check for edge cases and exceptions

    @Test
    public void testIsInInterfaceOrAnnotationBlock_True() {
        DetailAST node = new BasicAST(); // AST object to simulate the input inside an interface or annotation
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_False() {
        DetailAST node = new BasicAST(); // AST object to simulate the input not inside an interface or annotation
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NullNodeInput() {
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(null));
    }

    // Add similar test cases for other methods, replacing the method name and expected return values as needed

    @Test
    public void testIsInScope_InScope() {
        DetailAST ast = new BasicAST(); // AST object to simulate the input
        Scope expectedScope = PACKAGE;
        Scope actualScope = ScopeUtils.getSurroundingScope(ast);
        assertTrue(ScopeUtils.isInScope(ast, expectedScope));
    }

    @Test
    public void testIsInScope_NotInScope() {
        DetailAST ast = new BasicAST(); // AST object to simulate the input
        Scope expectedScope = PUBLIC;
        Scope actualScope = ScopeUtils.getSurroundingScope(ast);
        assertFalse(ScopeUtils.isInScope(ast, expectedScope));
    }

    @Test
    public void testIsInScope_NullAstInput() {
        assertFalse(ScopeUtils.isInScope(null, Scope.PACKAGE));
    }

    @Test
    public void testIsInScope_NullScopeInput() {
        DetailAST ast = new BasicAST(); // AST object to simulate the input
        assertFalse(ScopeUtils.isInScope(ast, null));
    }
}