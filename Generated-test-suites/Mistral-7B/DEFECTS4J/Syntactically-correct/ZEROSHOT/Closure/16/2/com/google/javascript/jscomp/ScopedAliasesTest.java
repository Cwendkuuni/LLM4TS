import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String test = "" +
        "goog.scope(function() {" +
        "  var a = goog.a;" +
        "  var b = goog.b;" +
        "  var c = goog.c;" +
        "  var d = goog.dom;" +
        "  d.createElement('div');" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(TRANSFORM);

    Compiler compiler = new Compiler();
    compiler.compile(null, test, options, null);

    Node root = compiler.getRootNode();
    Node scopeCall = findScopeCall(root);
    assertEquals(4, scopeCall.getChildCount());

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    assertEquals(4, aliasDefinitionsInOrder.size());
    assertEquals(4, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    assertEquals("goog.a", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog.b", aliasDefinitionsInOrder.get(1).getString());
    assertEquals("goog.c", aliasDefinitionsInOrder.get(2).getString());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(3).getString());

    assertEquals("a", aliasUsages.get(0).aliasReference.getString());
    assertEquals("goog.a", aliasUsages.get(0).aliasDefinition.getString());

    assertEquals("b", aliasUsages.get(1).aliasReference.getString());
    assertEquals("goog.b", aliasUsages.get(1).aliasDefinition.getString());

    assertEquals("c", aliasUsages.get(2).aliasReference.getString());
    assertEquals("goog.c", aliasUsages.get(2).aliasDefinition.getString());

    assertEquals("goog.dom", aliasUsages.get(3).aliasReference.getString());
    assertEquals("goog.dom", aliasUsages.get(3).aliasDefinition.getString());

    assertEquals(scopeCall, scopeCalls.get(0));

    Node body = scopeCall.getLastChild().getLastChild();
    assertEquals(4, body.getChildCount());

    for (Node child = body.getFirstChild(); child != null; child = child.getNext()) {
      Node variableDeclaration = child.getFirstChild();
      Node aliasReference = variableDeclaration.getLastChild();
      Node aliasDefinition = findVariableDeclaration(body, aliasReference.getString());

      assertEquals(aliasDefinition, aliasDefinitionsInOrder.get(indexOf(aliasReference.getString(), aliasDefinitionsInOrder)));
    }
  }

  private Node findScopeCall(Node root) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isCall() && "goog.scope".equals(child.getFirstChild().getString())) {
        return child;
      }
    }
    throw new AssertionError("Could not find goog.scope call.");
  }

  private int indexOf(String value, List<Node> list) {
    for (int i = 0; i < list.size(); i++) {
      if (value.equals(list.get(i).getString())) {
        return i;
      }
    }
    return -1;
  }

  private Node findVariableDeclaration(Node parent, String name) {
    for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar() && child.getChildCount() > 1 && child.getChildAtIndex(0).getString().equals(name)) {
        return child;
      }
    }
    throw new AssertionError("Could not find variable declaration for name: " + name);
  }
}

class ScopedAliasesTest$Traversal implements NodeTraversal.ScopedCallback {
  // ... (same as in the original class)
}