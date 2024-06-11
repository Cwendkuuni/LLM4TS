import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterCallback.Enter;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ExitCallback.Exit;
import static com.google.javascript.jscomp.NodeTraversal.ScopedCallback.Scope;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.GraphTraversal.NodeFunction;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalType;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link DeadAssignmentsElimination} class. This class tests
 * if the dead assignments elimination pass correctly eliminates the dead
 * assignments when there are no errors in the code.
 */
public class DeadAssignmentsEliminationTest {

  // Test instance of DeadAssignmentsElimination.
  private DeadAssignmentsElimination deadAssignmentsElimination;

  // Test compiler.
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  /**
   * Tests if the dead assignments elimination pass correctly eliminates the
   * dead assignments when there is a simple assignment.
   */
  @Test
  public void testSimpleAssignment() {
    String code = "function test() { " +
                  "  var x = 1; " +
                  "  x = 2; " +
                  "  return x; " +
                  "}";

    Node node = AbstractNode.getInitialNode(code);

    Scope scope = new Scope(AbstractNode.EMPTY_NODE, null, null);
    NodeTraversal traversal = new NodeTraversal(node, TraversalType.POSTORDER,
        Enter(scope), Exit(null), null, null);

    deadAssignmentsElimination.process(null, node);
    GraphTraversal.traverse(
        compiler, node, new NodeFunction<Void, Node>() {
          @Override
          public Void apply(Node n) {
            assertEquals(1, node.getChildCount());
            return null;
          }
        });
  }

  /**
   * Tests if the dead assignments elimination pass correctly eliminates the
   * dead assignments when there is a complex assignment and a conditional
   * statement.
   */
  @Test
  public void testComplexAssignment() {
    String code = "function test() { " +
                  "  var x = 1, y = 2; " +
                  "  if (true) { " +
                  "    x = 3; " +
                  "    y = 4; " +
                  "  } " +
                  "  return x + y; " +
                  "}";

    Node node = AbstractNode.getInitialNode(code);

    Scope scope = new Scope(AbstractNode.EMPTY_NODE, null, null);
    NodeTraversal traversal = new NodeTraversal(node, TraversalType.POSTORDER,
        Enter(scope), Exit(null), null, null);

    deadAssignmentsElimination.process(null, node);
    GraphTraversal.traverse(
        compiler, node, new NodeFunction<Void, Node>() {
          @Override
          public Void apply(Node n) {
            if (n.getType() == Token.IF) {
              assertEquals(4, n.getChildCount());
              Node cond = n.getFirstChild();
              Node xAssign = n.getChild(1);
              Node yAssign = n.getChild(2);
              Node sum = n.getChild(3);

              assertEquals(NodeUtil.isName(xAssign.getFirstChild()),
                          "x");
              assertEquals(NodeUtil.isName(yAssign.getFirstChild()),
                          "y");
              assertEquals(NodeUtil.isBinaryOp(sum, Token.PLUS), true);
              assertEquals(sum.getChildCount(), 2);
              assertEquals(sum.getChild(0).getType(), Token.NAME);
              assertEquals(sum.getChild(0).getString(), "x");
              assertEquals(sum.getChild(1).getType(), Token.NAME);
              assertEquals(sum.getChild(1).getString(), "y");
            }
            return null;
          }
        });
  }

  /**
   * Tests if the dead assignments elimination pass correctly eliminates the
   * dead assignments when there is a complex assignment and a loop statement.
   */
  @Test
  public void testComplexAssignmentWithLoop() {
    String code = "function test() { " +
                  "  var x = 1, y = 2; " +
                  "  for (var i = 0; i < 5; i++) { " +
                  "    x = i; " +
                  "    y = i; " +
                  "  } " +
                  "  return x + y; " +
                  "}";

    Node node = AbstractNode.getInitialNode(code);

    Scope scope = new Scope(AbstractNode.EMPTY_NODE, null, null);
    NodeTraversal traversal = new NodeTraversal(node, TraversalType.POSTORDER,
        Enter(scope), Exit(null), null, null);

    deadAssignmentsElimination.process(null, node);
    GraphTraversal.traverse(
        compiler, node, new NodeFunction<Void, Node>() {
          @Override
          public Void apply(Node n) {
            if (n.getType() == Token.FOR) {
              assertEquals(6, n.getChildCount());
              Node cond = n.getFirstChild();
              Node init = n.getChild(1);
              Node assignX = n.getChild(2);
              Node assignY = n.getChild(3);
              Node loop = n.getChild(4);
              Node body = n.getChild(5);

              assertEquals(NodeUtil.isName(init.getFirstChild()),
                          "i");

              assertEquals(assignX.getChildCount(), 3);
              assertEquals(assignX.getChild(0).getType(), Token.VAR);
              assertEquals(assignX.getChild(0).getString(), "x");
              assertEquals(assignX.getChild(1).getType(), Token.ASSIGN);
              assertEquals(assignX.getChild(2).getType(), Token.NAME);
              assertEquals(assignX.getChild(2).getString(), "i");

              assertEquals(assignY.getChildCount(), 3);
              assertEquals(assignY.getChild(0).getType(), Token.VAR);
              assertEquals(assignY.getChild(0).getString(), "y");
              assertEquals(assignY.getChild(1).getType(), Token.ASSIGN);
              assertEquals(assignY.getChild(2).getType(), Token.NAME);
              assertEquals(assignY.getChild(2).getString(), "i");

              assertEquals(body.getChildCount(), 1);
            }
            return null;
          }
        });
  }

  /**
   * Tests if the dead assignments elimination pass correctly does not eliminate
   * a dead assignment when there is a local variable that might be escaped due
   * to closures.
   */
  @Test
  public void testLocalVariableEscaped() {
    String code = "function test(x) { " +
                  "  function inner() { " +
                  "    var y = x; " +
                  "    y = 1; " +
                  "  } " +
                  "  inner(); " +
                  "  return x; " +
                  "}";

    Node node = AbstractNode.getInitialNode(code);

    Scope scope = new Scope(AbstractNode.EMPTY_NODE, null, null);
    NodeTraversal traversal = new NodeTraversal(node, TraversalType.POSTORDER,
        Enter(scope), Exit(null), null, null);

    deadAssignmentsElimination.process(null, node);
    GraphTraversal.traverse(
        compiler, node, new NodeFunction<Void, Node>() {
          @Override
          public Void apply(Node n) {
            if (n.getFirstChild().getType() == Token.FUNCTION) {
              Node fn = n.getFirstChild();
              assertEquals(fn.getChildCount(), 1);
              Node body = fn.getChild(0);
              assertEquals(body.getChildCount(), 3);
              Node yAssign = body.getChild(1);
              assertEquals(yAssign.getChildCount(), 3);
              assertEquals(yAssign.getChild(0).getType(), Token.VAR);
              assertEquals(yAssign.getChild(0).getString(), "y");
              assertEquals(yAssign.getChild(1).getType(), Token.ASSIGN);
              assertEquals(yAssign.getChild(2).getType(), Token.NAME);
              assertEquals(yAssign.getChild(2).getString(), "x");
            }
            return null;
          }
        });
  }

  /**
   * Tests if the dead assignments elimination pass correctly eliminates the
   * dead assignments when there is an empty statement.
   */
  @Test
  public void testEmptyStatement() {
    String code = "function test() { " +
                  "  var x = 1; " +
                  "  x = 2; " +
                  "  ; " + // Empty statement
                  "  return x; " +
                  "}";

    Node node = AbstractNode.getInitialNode(code);

    Scope scope = new Scope(AbstractNode.EMPTY_NODE, null, null);
    NodeTraversal traversal = new NodeTraversal(node, TraversalType.POSTORDER,
        Enter(scope), Exit(null), null, null);

    deadAssignmentsElimination.process(null, node);
    GraphTraversal.traverse(
        compiler, node, new NodeFunction<Void, Node>() {
          @Override
          public Void apply(Node n) {
            assertEquals(1, node.getChildCount());
            return null;
          }
        });
  }

  /**
   * Tests if the dead assignments elimination pass correctly eliminates the
   * dead assignments when there is an error in the code.
   */
  @Test(expected = RuntimeException.class)
  public void testError() {
    String code = "function test() { " +
                  "  var x = 1; " +
                  "  x = 2; " +
                  "  throw new Error(\"Test Error\"); " +
                  "}";

    Node node = AbstractNode.getInitialNode(code);

    Scope scope = new Scope(AbstractNode.EMPTY_NODE, null, null);
    NodeTraversal traversal = new NodeTraversal(node, TraversalType.POSTORDER,
        Enter(scope), Exit(null), null, null);

    deadAssignmentsElimination.process(null, node);
  }
}