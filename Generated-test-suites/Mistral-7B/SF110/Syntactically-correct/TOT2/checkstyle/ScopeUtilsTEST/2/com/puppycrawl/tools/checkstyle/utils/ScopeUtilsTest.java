import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST(); // Assuming you can create a DetailAST object
        String[] expectedScopes = {Scope.PUBLIC, Scope.PROTECTED, Scope.PRIVATE};
        for (String scope : expectedScopes) {
            mods.addChild(new SimpleNode("Modifier") {
                {
                    setProperty("text", scope);
                }
            });
            Scope actualScope = ScopeUtils.getScopeFromMods(mods);
            assertEquals(scope, actualScope);
        }
    }
}