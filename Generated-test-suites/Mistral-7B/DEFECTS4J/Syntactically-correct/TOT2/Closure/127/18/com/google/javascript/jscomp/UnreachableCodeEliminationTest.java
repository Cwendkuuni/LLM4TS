import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.AbstractShallowCallbackType.FUNCTION;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Config;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions(new Config());
    compiler = new Compiler(options);
  }

  @Test
  public void testUnreachableCodeElimination() {
    String code = "function foo() {" +
        "  if (false) {" +
        "    return;" +
        "  }" +
        "  alert('This should be removed');" +
        "}";

    Node tree = compiler.parseTree(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, tree);
    Node result = tree;

    // Check that the return statement and the alert call have been removed.
    assertNull(result.getChild(1));

    // Test a more complex case with a control flow graph and a function.
    code = "function bar() {" +
        "  if (x) {" +
        "    return;" +
        "  }" +
        "  function baz() {" +
        "    alert('This should be removed');" +
        "  }" +
        "  baz();" +
        "}";

    tree = compiler.parseTree(code, null, null);
    pass.process(null, tree);
    result = tree;

    // Check that the function baz() has been removed.
    assertNull(result.getChild(3));
  }

  @Test
  public void testRemoveNoOpStatements() {
    String code = "function foo() {" +
        "  var x = a.b.MyClass.prototype.propertyName;" +
        "  return;" +
        "}";

    Node tree = compiler.parseTree(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, tree);
    Node result = tree;

    // Check that the assignment statement has been removed.
    assertNull(result.getChild(1));
  }

  @Test(expected = AssertionError.class)
  public void testThrowsExceptionForUnreachableCodeAfterReturn() {
    String code = "function foo() {" +
        "  if (false) {" +
        "    return;" +
        "  }" +
        "  throw new Error('This should not be reached');" +
        "}";

    Node tree = compiler.parseTree(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, tree);
    Node result = tree;

    // The exception should not be removed.
    assertNotNull(result.getChild(1));
  }

  @Test
  public void testKeepSideEffectFreeCode() {
    String code = "function foo() {" +
        "  var x = a.b.MyClass.prototype.propertyName;" +
        "  x++;" +
        "}";

    Node tree = compiler.parseTree(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, tree);
    Node result = tree;

    // The side effect free code should not be removed.
    assertNotNull(result.getChild(1));
  }

  @Test
  public void testKeepControlFlowGraph() {
    String code = "function foo() {" +
        "  if (x) {" +
        "    return;" +
        "  }" +
        "  if (y) {" +
        "    return;" +
        "  }" +
        "  alert('This should not be removed');" +
        "}";

    Node tree = compiler.parseTree(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, tree);
    Node result = tree;

    // The control flow graph should not be altered.
    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.isFunction() && n.getFirstChild().getType() == Token.IF) {
          DiGraphNode<Node, Branch> gNode = n.getGraphNode();
          assertTrue(gNode != null);
          assertTrue(gNode.getAnnotation() == GraphReachability.REACHABLE);
        }
        super.visit(nt, n, parent);
      }
    };
    NodeTraversal.traverse(compiler, result, traverser);
  }
}