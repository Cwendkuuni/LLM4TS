import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CollapseVariableDeclarations.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

import java.util.List;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a; " +
        "  var b = 1; " +
        "  var c = 2; " +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = NodeUtil.parseTree("function foo() {" +
        "  var a, b = 1, c = 2;" +
        "}", null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    assertEquals(expected, traversal.getSubtree(root, 0));
  }

  @Test
  public void testCollapseAssignment() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a = 1; " +
        "  var a = 2;" +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = NodeUtil.parseTree("function foo() {" +
        "  var a = 1;" +
        "}", null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    assertEquals(expected, traversal.getSubtree(root, 0));
  }

  @Test
  public void testCollapseVariableDeclarationsWithEmptyLine() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a;" +
        "" +
        "  var b = 1; " +
        "  var c = 2; " +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = NodeUtil.parseTree("function foo() {" +
        "  var a, b = 1, c = 2;" +
        "}", null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    assertEquals(expected, traversal.getSubtree(root, 0));
  }

  @Test
  public void testCollapseVariableDeclarationsWithFunctionDeclaration() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  function bar() {" +
        "    var a;" +
        "    var b = 1;" +
        "  }" +
        "  var c = 2;" +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = NodeUtil.parseTree("function foo() {" +
        "  function bar() {" +
        "    var a, b = 1;" +
        "  }" +
        "  var c = 2;" +
        "}", null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    assertEquals(expected, traversal.getSubtree(root, 0));
  }

  @Test
  public void testCollapseVariableDeclarationsWithFunctionExpression() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a;" +
        "  var bar = function() {};" +
        "  var c = 2;" +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = NodeUtil.parseTree("function foo() {" +
        "  var a, bar = function() {};" +
        "  var c = 2;" +
        "}", null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    assertEquals(expected, traversal.getSubtree(root, 0));
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithInvalidDeclaration() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a = 1;" +
        "  function bar() {}" +
        "  var b = 2;" +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);

    // This test should fail due to the invalid declaration of 'b'
    // after a function declaration
    Node foo = traversal.getSubtree(root, 0);
    Node bar = foo.getFirstChild();
    Node b = bar.getNext();

    assertEquals("b", b.getString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithInvalidAssignment() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);

    // This test should fail due to the invalid assignment of 'a'
    Node foo = traversal.getSubtree(root, 0);
    Node a = foo.getFirstChild();
    Node assign = a.getNext();

    assertEquals("=", assign.getType());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithNestedVariableDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    String input = "function foo() {" +
        "  var a;" +
        "  function bar() {" +
        "    var a;" +
        "  }" +
        "}";

    Node root = NodeUtil.parseTree(input, null, null);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler, null, null);

    // This test should fail due to the nested variable declaration of 'a'
    Node foo = traversal.getSubtree(root, 0);
    Node bar = foo.getFirstChild();
    Node a = bar.getFirstChild();

    assertEquals("a", a.getString());
  }
}