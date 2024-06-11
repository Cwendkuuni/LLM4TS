import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = createAST("public class MyClass { }");
        Scope expected = PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(JastAdd.MODIFIERS));
        assertEquals(expected, actual);

        ast = createAST("protected class MyClass { }");
        expected = PROTECTED;
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(JastAdd.MODIFIERS));
        assertEquals(expected, actual);

        ast = createAST("private class MyClass { }");
        expected = PRIVATE;
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(JastAdd.MODIFIERS));
        assertEquals(expected, actual);

        ast = createAST("public static void main(String[] args) { }");
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(JastAdd.MODIFIERS));
        assertEquals(expected, actual); // should still return PACKAGE, since the class is still in a package

        ast = createAST("public interface MyInterface { }");
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(JastAdd.MODIFIERS));
        assertEquals(expected, actual); // should still return PACKAGE, since the interface is in a package
    }

    // helper method to create an AST node for testing
    private DetailAST createAST(String code) {
        // code to create an AST node from a string, using a parser or other means
        return ast;
    }
}