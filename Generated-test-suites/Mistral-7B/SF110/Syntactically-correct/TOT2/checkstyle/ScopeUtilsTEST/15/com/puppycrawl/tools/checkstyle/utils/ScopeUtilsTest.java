import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;
import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        Token publicToken = new SimpleToken(TokenTypes.IDENTIFIER, "public");
        Token privateToken = new SimpleToken(TokenTypes.IDENTIFIER, "private");
        Token protectedToken = new SimpleToken(TokenTypes.IDENTIFIER, "protected");

        mods.addChild(publicToken);
        mods.addChild(privateToken);
        mods.addChild(protectedToken);

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parentNode = new DetailAST();
        parentNode.addChild(node);

        DetailAST parent = new DetailAST();
        parent.addChild(parentNode);

        final int scopeType1 = 14;
        parent.setType(scopeType1);
        final int scopeType2 = 15;
        parentNode.setType(scopeType2);

        final Scope expected = Scope.valueOf(scopeType1.toString());
        final Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    // Add test cases for other methods here...
}