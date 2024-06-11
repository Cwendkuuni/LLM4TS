import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DummyNode;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private static final Compiler COMPILER = new Compiler();
  private static final NodeFactory NODE_FACTORY = COMPILER.getFactory();

  @Test
  public void testScopedAliases() {
    Preconditions.checkState(CompilerOptions.isAvailable(ALIASES));

    CompilerOptions options = new CompilerOptions();
    options.setOption(ALIASES, true);

    String sourceCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  var x = goog.x;\n" +
        "  goog.x = \"foo\";\n" +
        "  goog.scope(function() {\n" +
        "    var y = goog.x;\n" +
        "    goog.x = goog.dom.TagName.DIV;\n" +
        "    dom.createElement(goog.x);\n" +
        "  });\n" +
        "  goog.scope(function() {\n" +
        "    var z = goog.x;\n" +
        "    goog.x = goog.dom.TagName.DIV;\n" +
        "    dom.createElement(goog.x);\n" +
        "  });\n" +
        "});";

    SourceFile input = new SourceFile("test.js", sourceCode);

    HotSwapCompilerPass pass = new ScopedAliases(
        COMPILER,
        null,
        new NoopAliasTransformationHandler());

    List<Node> ast = pass.process(input.getAST(), null);
    Node root = ast.get(0);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(COMPILER, root, traversal);
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(2, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node scopeCall1 = traversal.getScopeCalls().get(0);
    Node scopeCall2 = traversal.getScopeCalls().get(1);

    // The first call to 'goog.scope' does not define any aliases.
    assertEquals(Token.CALL, scopeCall1.getType());
    assertEquals(3, scopeCall1.getChildCount());
    assertEquals("goog.scope", scopeCall1.getChildAtIndex(0).getQualifiedName());

    Node anonymousFnNode1 = scopeCall1.getChildAtIndex(1);
    assertEquals(Token.LPAREN, anonymousFnNode1.getType());
    assertEquals(Token.RPAREN, anonymousFnNode1.getLastChild().getType());

    // The second call to 'goog.scope' defines an alias for 'dom.TagName.DIV'.
    assertEquals(Token.CALL, scopeCall2.getType());
    assertEquals(3, scopeCall2.getChildCount());
    assertEquals("goog.scope", scopeCall2.getChildAtIndex(0).getQualifiedName());

    Node anonymousFnNode2 = scopeCall2.getChildAtIndex(1);
    assertNotNull(anonymousFnNode2.getFirstChild());
    assertEquals(Token.LPAREN, anonymousFnNode2.getFirstChild().getType());
    assertEquals(Token.RPAREN, anonymousFnNode2.getLastChild().getType());

    Node body1 = anonymousFnNode1.getLastChild();
    Node body2 = anonymousFnNode2.getLastChild();

    // The body of the first 'goog.scope' function does not use 'goog.x'.
    assertNull(findVariableWithName("x", body1));

    // The body of the second 'goog.scope' function uses 'goog.x'.
    Node xAssignment = findVariableWithName("x", body2, 0);
    assertEquals(Token.ASSIGN, xAssignment.getType());

    Node stringLiteral = xAssignment.getLastChild();
    assertEquals("foo", stringLiteral.getString());

    // The body of the second 'goog.scope' function also defines an alias for
    // 'goog.x'.
    Node xDefinition = findVariableWithName("x", body2, 1);
    assertNotNull(xDefinition);
    assertEquals(Token.VAR, xDefinition.getType());
    assertEquals(Token.NAME, xDefinition.getLastChild().getType());
    assertEquals("x", xDefinition.getLastChild().getString());

    // The body of the second 'goog.scope' function also defines an alias for
    // 'goog.dom.TagName.DIV'.
    Node divDefinition = findVariableWithName("DIV", body2);
    assertNotNull(divDefinition);
    assertEquals(Token.ASSIGN, divDefinition.getType());
    assertEquals(Token.NAME, divDefinition.getLastChild().getType());
    assertEquals("DIV", divDefinition.getLastChild().getString());

    // The body of the second 'goog.scope' function does not define an alias for
    // 'goog'.
    Node googDefinition = findVariableWithName("goog", body2);
    assertNull(googDefinition);

    // The alias for 'goog.dom.TagName.DIV' is used in both 'goog.scope'
    // blocks.
    assertEquals("DIV", findVariableWithName("goog.x", root).getString());

    // The alias for 'goog.dom.TagName.DIV' is also used after the second
    // 'goog.scope' block.
    Node domCreateElement = findVariableWithName("goog.x", root, 1);
    assertEquals(Token.CALL, domCreateElement.getType());
    assertEquals(
        "dom.createElement",
        domCreateElement.getFirstChild().getQualifiedName());
  }

  // Helper method to find the first variable with a given name in a given node.
  private Node findVariableWithName(String name, Node node) {
    return findVariableWithName(name, node, 0);
  }

  private Node findVariableWithName(String name, Node node, int index) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (NodeUtil.isVar(child)) {
        Node variable = child;
        if (name.equals(variable.getLastChild().getString())) {
          if (index > 0) {
            fail("Expected variable at index " + index + " but found at " +
                "index 0.");
          }
          return variable;
        }
      }
      if (NodeUtil.isBlock(child) || NodeUtil.isFunction(child)) {
        Node found = findVariableWithName(name, child, index + 1);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private static class NoopAliasTransformationHandler implements
      AliasTransformationHandler {
    @Override
    public void enterScope(Scope scope) {}

    @Override
    public void leaveScope(Scope scope) {}

    @Override
    public void logAliasTransformation(
        String fileName, SourcePosition position,
        Map<String, String> aliases) {}
  }
}

class NodeUtil {
  public static boolean isBlock(Node n) {
    return n.getType() == Token.BLOCK;
  }

  public static boolean isFunction(Node n) {
    return n.getType() == Token.FUNCTION;
  }

  public static boolean isVar(Node n) {
    return n.getType() == Token.VAR;
  }

  public static Node getFunctionName(Node n) {
    if (n.isFunction()) {
      return n.getLastChild();
    }
    return null;
  }

  public static Node findVariableWithName(String name, Node node) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (NodeUtil.isVar(child)) {
        Node variable = child;
        String variableName = variable.getLastChild().getString();
        if (variableName.equals(name)) {
          return variable;
        }
      }
    }
    return null;
  }

  public static Node[] getFunctionParameters(Node functionNode) {
    List<Node> parameters = Lists.newArrayList();
    for (Node child = functionNode.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.COMMA) {
        continue;
      }
      parameters.add(child);
    }
    return parameters.toArray(new Node[parameters.size()]);
  }
}