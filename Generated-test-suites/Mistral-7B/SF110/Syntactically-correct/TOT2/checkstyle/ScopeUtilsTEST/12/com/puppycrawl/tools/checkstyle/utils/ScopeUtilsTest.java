import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        AST publicMod = new SimpleNode("public");
        AST protectedMod = new SimpleNode("protected");
        AST privateMod = new SimpleNode("private");

        mods.addChild(publicMod);
        mods.addChild(protectedMod);
        mods.addChild(privateMod);
        mods.addChild(new SimpleNode("class"));

        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);

        assertEquals("Scope should be PUBLIC", expectedScope, actualScope);
    }

    // Add test cases for other possible scenarios and exceptions here
}