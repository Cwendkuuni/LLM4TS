import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testBasicFunctionAliasing() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new NodeTraversal.RootCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        // Do not traverse into the script body of the goog.scope call.
        if (parent.isCall() && SCOPING_METHOD_NAME.equals(parent.getFirstChild().getQualifiedName())) {
          Node scriptNode = parent.getLastChild();
          NodeTraversal.traverse(compiler, scriptNode, traversal);
          return false;
        }
        return true;
      }
    });

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();
    assertEquals(3, traversalInstance.getScopeCalls().size());
    assertEquals(1, traversalInstance.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversalInstance.getAliasUsages().size());

    Node aliasDefinition = traversalInstance.getAliasDefinitionsInOrder().get(0);
    assertEquals(Token.ASSIGN, aliasDefinition.getType());
    assertEquals("DOM", aliasDefinition.getLastChild().getString());
    assertEquals("dom", aliasDefinition.getFirstChild().getFirstChild().getString());

    assertEquals(Token.NAME, traversalInstance.getAliasUsages().get(0).getType());
    assertEquals("DOM", traversalInstance.getAliasUsages().get(0).aliasDefinition.getString());
    assertEquals("dom", traversalInstance.getAliasUsages().get(0).aliasReference.getString());
  }

  @Test(expected = AssertionError.class)
  public void testBadFunctionAliasing() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom; " +
        "  }); " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be an error for the second call to goog.scope.
    assertEquals(2, traversalInstance.getDiagnostics().size());
  }

  @Test(expected = AssertionError.class)
  public void testImproperUsageOfGoogScope() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "}); " +
        "goog.scope(function() {" +
        "  var x = 1; " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be an error for the improper usage of goog.scope.
    assertEquals(1, traversalInstance.getDiagnostics().size());
  }

  @Test
  public void testAliasRedefinition() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var a = 1; " +
        "  goog.scope(function() {" +
        "    var a = 2; " +
        "  }); " +
        "  var b = 3; " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be an error for the alias redefinition.
    assertEquals(1, traversalInstance.getDiagnostics().size());
  }

  @Test
  public void testAliasUsageOutsideOfScopeBlock() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var a = 1; " +
        "});" +
        "goog.dom.createElement('DIV');";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be no error.
    assertEquals(0, traversalInstance.getDiagnostics().size());
  }

  @Test
  public void testAliasUsageWithReturnStatement() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var a = 1;" +
        "  return a; " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be an error for the use of return statement.
    assertEquals(1, traversalInstance.getDiagnostics().size());
  }

  @Test
  public void testAliasUsageWithThrowStatement() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  try {" +
        "    var a = 1;" +
        "    throw new Error('Test Error'); " +
        "  } catch (e) {" +
        "  } " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be an error for the use of throw statement.
    assertEquals(1, traversalInstance.getDiagnostics().size());
  }

  @Test
  public void testAliasUsageWithThisKeyword() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var a = this;" +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be an error for the use of this keyword.
    assertEquals(1, traversalInstance.getDiagnostics().size());
  }

  @Test
  public void testAliasTransformation() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode = "goog.scope(function() {" +
        "  var a = goog.dom;" +
        "  var b = a.createElement; " +
        "  b('DIV'); " +
        "});";

    Node root = new Node(Token.PROGRAM, null, null);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.process(null, root);

    Traversal traversalInstance = traversal.getTraversal();

    // There should be no errors and the alias transformation should have occurred.
    assertEquals(0, traversalInstance.getDiagnostics().size());
    assertEquals(1, traversalInstance.getAliasDefinitionsInOrder().size());
    assertEquals(2, traversalInstance.getAliasUsages().size());

    Node aliasDefinition = traversalInstance.getAliasDefinitionsInOrder().get(0);
    assertEquals("DOM", aliasDefinition.getString());
    assertEquals("goog.dom", aliasDefinition.getLastChild().getString());

    assertEquals(Token.NAME, traversalInstance.getAliasUsages().get(0).getType());
    assertEquals("DOM", traversalInstance.getAliasUsages().get(0).aliasDefinition.getString());
    assertEquals("goog.dom", traversalInstance.getAliasUsages().get(0).aliasReference.getString());

    assertEquals(Token.CALL, traversalInstance.getAliasUsages().get(1).getType());
    assertEquals("createElement", traversalInstance.getAliasUsages().get(1).aliasDefinition.getFirstChild().getString());
    assertEquals("DOM.createElement", traversalInstance.getAliasUsages().get(1).aliasReference.getString());
  }
}