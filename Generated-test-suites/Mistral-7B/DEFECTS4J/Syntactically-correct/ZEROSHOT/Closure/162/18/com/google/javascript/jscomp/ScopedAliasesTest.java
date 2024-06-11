import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Set up the test
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS_ENABLED);
    options.setAliasTransformationHandler(new NoopAliasTransformationHandler());
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    // Create the test source
    String source =
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    // Run the test
    Node root = compiler.parseTree(source, null);
    Traversal traversal = new Traversal();
    compiler.getTraversalFactory().traverse(root, traversal);

    // Verify the results
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(scopeCalls.size() == 1);
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertTrue(anonymousFnNode.isFunction());
    assertEquals(0, anonymousFnNode.getFunctionParameters().getChildCount());
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("goog.dom", aliasDefinition.getLastChild().getString());
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    Node aliasReference = aliasedNode.aliasReference;
    Node aliasDefinitionInTree = aliasedNode.aliasDefinition;
    assertEquals("dom", aliasReference.getString());
    assertEquals("goog.dom", aliasDefinitionInTree.getString());
  }

  private static class NoopAliasTransformationHandler implements AliasTransformationHandler {

    @Override
    public AliasTransformation logAliasTransformation(String fileName,
        SourcePosition position) {
      return NO_OP;
    }
  }
}