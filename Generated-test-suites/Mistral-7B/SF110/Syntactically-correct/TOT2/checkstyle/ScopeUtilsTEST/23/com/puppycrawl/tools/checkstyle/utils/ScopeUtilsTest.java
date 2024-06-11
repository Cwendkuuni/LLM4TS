import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        // Add public, protected, private tokens to mods
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        DetailAST protectedToken = new DetailAST();
        protectedToken.setText("protected");
        DetailAST privateToken = new DetailAST();
        privateToken.setText("private");

        mods.addChild(publicToken);
        mods.addChild(protectedToken);
        mods.addChild(privateToken);

        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);

        // Test for private scope
        mods.removeChild(publicToken);
        mods.removeChild(protectedToken);
        mods.addChild(privateToken);

        expectedScope = Scope.PRIVATE;
        actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }
}