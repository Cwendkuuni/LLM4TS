import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import static org.junit.Assert.assertEquals;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST(); // Create a mock DetailAST
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, scope);

        mods.addChild(new SimpleNode("public"));
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);

        mods.addChild(new SimpleNode("protected"));
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PROTECTED, scope);

        mods.addChild(new SimpleNode("private"));
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, scope);
    }
}