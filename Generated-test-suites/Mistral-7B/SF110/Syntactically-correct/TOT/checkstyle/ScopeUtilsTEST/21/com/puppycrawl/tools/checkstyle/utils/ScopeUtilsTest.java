import static org.junit.Assert.assertEquals;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Tree;

public class ScopeUtilsTest {

    @org.junit.Test
    public void testGetScopeFromMods() throws RecognitionException {
        String astInput = "public class TestClass {\n" +
                          "   protected int testProtected;\n" +
                          "   private int testPrivate;\n" +
                          "}";

        ANTLRFileStream input = new ANTLRFileStream(new ByteArrayInputStream(astInput.getBytes()));
        CommonTokenStream tokens = new CommonTokenStream(new com.puppycrawl.tools.checkstyle.utils.CheckStyleLexer(input));
        Tree tree = tokens.getTree();

        DetailAST mods = (DetailAST) tree.getChildByType(tree.getChildCount() - 1, 15); // class modifiers
        Scope expected = PRIVATE; // last modifier wins
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        // test for no modifiers
        String astInputNoModifiers = "class TestClass {}";
        ANTLRFileStream inputNoModifiers = new ANTLRFileStream(new ByteArrayInputStream(astInputNoModifiers.getBytes()));
        CommonTokenStream tokensNoModifiers = new CommonTokenStream(new com.puppycrawl.tools.checkstyle.utils.CheckStyleLexer(inputNoModifiers));
        Tree treeNoModifiers = tokensNoModifiers.getTree();
        DetailAST modsNoModifiers = (DetailAST) treeNoModifiers.getChild(0);
        Scope actualNoModifiers = ScopeUtils.getScopeFromMods(modsNoModifiers);
        assertEquals(Scope.PACKAGE, actualNoModifiers);

        // test for public modifier
        String astInputPublic = "public class TestClass {}";
        ANTLRFileStream inputPublic = new ANTLRFileStream(new ByteArrayInputStream(astInputPublic.getBytes()));
        CommonTokenStream tokensPublic = new CommonTokenStream(new com.puppycrawl.tools.checkstyle.utils.CheckStyleLexer(inputPublic));
        Tree treePublic = tokensPublic.getTree();
        DetailAST modsPublic = (DetailAST) treePublic.getChild(0);
        Scope expectedPublic = PUBLIC;
        Scope actualPublic = ScopeUtils.getScopeFromMods(modsPublic);
        assertEquals(expectedPublic, actualPublic);
    }
}