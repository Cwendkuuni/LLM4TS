import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST ast = createAST("public class Foo {}");
        Scope expected = PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST ast = createAST("protected class Foo {}");
        Scope expected = PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST ast = createAST("private class Foo {}");
        Scope expected = PRIVATE;
        Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_NoModifiers() {
        DetailAST ast = createAST("class Foo {}");
        Scope expected = PACKAGE;
        Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_NullAST() {
        DetailAST ast = null;
        ScopeUtils.getScopeFromMods(ast);
    }

    private DetailAST createAST(String code) {
        // Create a mock ANTLR parser and parser result for the given code.
        // Return the root node of the parser result.
    }

    // 
}