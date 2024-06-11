import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.*;

import java.util.List;

/**
 * Test class for the ScopedAliases compiler pass.
 */
public class ScopedAliasesTest {

  @SuppressWarnings("unchecked")
  private static <T> T getNode(Node root, int index) {
    Node currentNode = root;
    for (int i = 0; i < index; i++) {
      currentNode = currentNode.getNext();
    }
    return (T) currentNode;
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Node> getNodes(Node root, int startIndex, int endIndex) {
    List<Node> nodes = Lists.newArrayList();
    Node currentNode = root;
    for (int i = startIndex; i <= endIndex; i++) {
      nodes.add((Node) currentNode);
      currentNode = currentNode.getNext();
    }
    return nodes;
  }

  @SuppressWarnings("unchecked")
  private static <T> T getSingleNode(Node root, Class<T> nodeType) {
    for (Node node = root; node != null; node = node.getNext()) {
      if (nodeType.isInstance(node)) {
        return (T) node;
      }
    }
    throw new AssertionError("Node not found");
  }

  @SuppressWarnings("unchecked")
  private static <T> T getSingleNodeWithName(Node root, String nodeName) {
    for (Node node = root; node != null; node = node.getNext()) {
      if (nodeName.equals(node.getString())) {
        return (T) node;
      }
    }
    throw new AssertionError("Node not found");
  }

  @Test
  public void testScopedAliases() {
    // Test the class without errors.
    testScopedAliases(true, null);

    // Test with errors.
    testScopedAliases(false, new DiagnosticType[] {
        GOOG_SCOPE_USED_IMPROPERLY,
        GOOG_SCOPE_HAS_BAD_PARAMETERS,
        GOOG_SCOPE_REFERENCES_THIS,
        GOOG_SCOPE_USES_RETURN,
        GOOG_SCOPE_USES_THROW,
        GOOG_SCOPE_ALIAS_REDEFINED,
        GOOG_SCOPE_NON_ALIAS_LOCAL
    });
  }

  private void testScopedAliases(boolean shouldPass, DiagnosticType... errors) {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);
    Compiler compiler = new Compiler();
    compiler.init(options);

    String testSource =
        "goog.scope(function() {\n" +
            "  var a = goog.a;\n" +
            "  var b = goog.b;\n" +
            "  var c = goog.c;\n" +
            "  var d = goog.dom;\n" +
            "  var e = goog.e;\n" +
            "  var f = goog.f;\n" +
            "  var g = goog.g;\n" +
            "  var h = goog.h;\n" +
            "  var i = goog.i;\n" +
            "  var j = goog.j;\n" +
            "  var k = goog.k;\n" +
            "  var l = goog.l;\n" +
            "  var m = goog.m;\n" +
            "  var n = goog.n;\n" +
            "  var o = goog.o;\n" +
            "  var p = goog.p;\n" +
            "  var q = goog.q;\n" +
            "  var r = goog.r;\n" +
            "  var s = goog.s;\n" +
            "  var t = goog.t;\n" +
            "  var u = goog.u;\n" +
            "  var v = goog.v;\n" +
            "  var w = goog.w;\n" +
            "  var x = goog.x;\n" +
            "  var y = goog.y;\n" +
            "  var z = goog.z;\n" +
            "});";

    compiler.addSource("Test", testSource, null);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler,
        null,
        new AliasTransformationHandler() {}
    );
    scopedAliases.process(null, null);

    List<Node> errorsNodes = compiler.getErrors();
    if (errors != null) {
      // Check if the expected errors were reported.
      for (DiagnosticType error : errors) {
        assertTrue("Expected error " + error + " was not reported",
            errorsNodes.stream().anyMatch(
                node -> error.equals(node.getType()))
        );
      }
    }

    if (!shouldPass) {
      // If errors were expected, we're done.
      return;
    }

    // Check if there are no errors.
    assertTrue("Unexpected errors", errorsNodes.isEmpty());

    // Validate the scopeCalls.
    Node root = compiler.getAST();
    List<Node> scopeCalls = getNodes(root, 0, 1);
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    List<Node> anonymousFnNodes = getNodes(scopeCall, 1, 1);
    assertEquals(1, anonymousFnNodes.size());
    Node anonymousFnNode = anonymousFnNodes.get(0);
    List<Node> params = NodeUtil.getFunctionParameters(anonymousFnNode);
    assertEquals(0, params.size());

    // Validate the aliasDefinitionsInOrder.
    Node scopeBlock = scopeCall.getLastChild();
    List<Node> variables = NodeUtil.getAllDeclarations(scopeBlock);
    assertEquals(11, variables.size());
    for (int i = 0; i < variables.size(); i++) {
      Node variable = variables.get(i);
      if (i < 11) {
        assertEquals("Expected " + (char) ('a' + i) + " to be defined",
            getSingleNodeWithName(scopeBlock, "a" + i).getName(),
            variable.getName()
        );
        assertEquals("Expected " + (char) ('a' + i) + " to be an alias",
            "goog." + (char) ('a' + i),
            NodeUtil.getExpression(variable).getQualifiedName()
        );
      } else {
        // The last variable should not be an alias.
        assertNotEquals("Expected last variable not to be an alias",
            "goog.",
            NodeUtil.getExpression(variable).getQualifiedName()
        );
      }
    }

    // Validate the aliasUsages.
    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(11, aliasUsages.size());
    for (Node aliasUsage : aliasUsages) {
      Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
      assertEquals("Expected alias usage to reference an alias",
          getSingleNodeWithName(scopeBlock, aliasReference.getString()),
          aliasReference
      );
      Node aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
      assertEquals("Expected alias usage to use the correct definition",
          getSingleNodeWithName(scopeBlock, aliasDefinition.getString()),
          aliasDefinition
      );
    }

    // Validate the aliases.
    Map<String, Scope.Var> aliases = scopedAliases.getAliases();
    assertEquals(11, aliases.size());
    for (Map.Entry<String, Scope.Var> aliasEntry : aliases.entrySet()) {
      assertEquals("Expected alias value to be defined",
          getSingleNodeWithName(scopeBlock, aliasEntry.getKey()),
          aliasEntry.getValue().getNode()
      );
    }

    // Validate the forbiddenLocals.
    List<String> forbiddenLocals = Lists.newArrayList(scopedAliases.getForbiddenLocals());
    assertEquals(11, forbiddenLocals.size());
    for (int i = 0; i < 11; i++) {
      assertEquals("Expected forbiddenLocals to contain " + (char) ('a' + i),
          "goog." + (char) ('a' + i),
          forbiddenLocals.get(i)
      );
    }
  }
}