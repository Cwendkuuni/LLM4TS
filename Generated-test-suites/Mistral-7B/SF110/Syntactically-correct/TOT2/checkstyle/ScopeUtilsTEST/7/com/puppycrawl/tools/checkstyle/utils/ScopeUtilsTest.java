import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        Token publicToken = new Token("public");
        Token packageToken = new Token("package");
        Token protectedToken = new Token("protected");
        Token privateToken = new Token("private");

        mods.addChild(packageToken);
        mods.addChild(publicToken);
        mods.addChild(privateToken);

        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        Scope expectedScope = Scope.PUBLIC;

        assertEquals(expectedScope, actualScope);
    }

    // Add test cases for other methods
}