import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestType.getTypes;
import static com.google.javascript.jscomp.CompilationTestHelper.checkCompilesTo;
import static com.google.javascript.jscomp.CompilationTestHelper.testOptions;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    CompilationTestHelper helper = new CompilationTestHelper(null);
    SourceFile source = new SourceFile("Test.java", sourceCode);

    // Test with default options
    Map<String, String> options = ImmutableMap.of();
    Node expected = null;
    try {
      expected = checkCompilesTo(helper, source, options);
    } catch (Exception e) {
      fail("Compilation failed: " + e.getMessage());
    }

    // Test with alias transformation enabled
    options = testOptions(
        "--jscomp_off=checkTypes,es5strict",
        "--jscomp_on=aliasTransformation:ENABLED");
    Node actual = null;
    try {
      actual = checkCompilesTo(helper, source, options);
    } catch (Exception e) {
      fail("Compilation failed: " + e.getMessage());
    }

    // Check that alias transformation was applied
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(new HotSwapCompilerPass() {}, null, traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(5, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(3, aliasUsages.size());

    assertEquals(
        "goog.dom.createElement",
        aliasDefinitionsInOrder.get(0).getString()
    );
    assertEquals(
        "goog.dom.TagName.DIV",
        aliasDefinitionsInOrder.get(1).getString()
    );
    assertEquals(
        "goog.dom",
        aliasDefinitionsInOrder.get(2).getString()
    );
    assertEquals(
        "goog",
        aliasDefinitionsInOrder.get(3).getString()
    );
    assertEquals(
        "goog.scope",
        aliasDefinitionsInOrder.get(4).getString()
    );
    assertEquals(
        "goog.dom.createElement",
        scopeCalls.get(0).getChildAtIndex(1).getString()
    );

    // Check that alias usage nodes were replaced with their definitions
    assertEquals(
        expected.getChildAtIndex(1),
        aliasUsages.get(0).aliasDefinition
    );
    assertEquals(
        expected.getChildAtIndex(4),
        aliasUsages.get(1).aliasDefinition
    );
    assertEquals(
        expected.getChildAtIndex(6),
        aliasUsages.get(2).aliasDefinition
    );
  }
}

class Traversal extends ScopedAliases.Traversal {
  @Override
  public void exitScope(NodeTraversal t) {
    super.exitScope(t);
    // Do not clear alias map and forbidden locals here to allow testing
    // of multiple nested goog.scope blocks.
  }
}