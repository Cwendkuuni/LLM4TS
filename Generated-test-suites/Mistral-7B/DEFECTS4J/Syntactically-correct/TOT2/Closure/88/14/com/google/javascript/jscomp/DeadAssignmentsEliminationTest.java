import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getNodeFactory();
  }

  @Test
  public void testSimpleDeadAssignment() {
    String source = "function f() { var x = 1; x = 2; }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertFalse(isAssignmentsPresent(root, "x"));
  }

  @Test
  public void testDeadAssignmentInCondition() {
    String source = "function f() { var x = 1; if (x = 2) {} }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertFalse(isAssignmentsPresent(root, "x"));
  }

  @Test
  public void testDeadAssignmentInLoop() {
    String source = "function f() { for (var i = 0; i = 1; i++) {} }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertFalse(isAssignmentsPresent(root, "i"));
  }

  @Test
  public void testDeadAssignmentInFunctionCall() {
    String source = "function f() { var x = 1; g(x = 2); } function g(x) {}";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertFalse(isAssignmentsPresent(root, "x"));
  }

  @Test
  public void testDeadAssignmentInReturnStatement() {
    String source = "function f() { return x = 2; }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertTrue(isAssignmentsPresent(root, "x"));
  }

  @Test
  public void testDeadAssignmentInLoopWithReturn() {
    String source = "function f() { for (var i = 0; i = 1; i++) { return; } }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertFalse(isAssignmentsPresent(root, "i"));
  }

  @Test
  public void testDeadAssignmentInConditionalReturn() {
    String source = "function f() { if (true) { return x = 2; } else { return x; } }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertTrue(isAssignmentsPresent(root, "x"));
  }

  @Test
  public void testNoDeadAssignments() {
    String source = "function f() { var x = 1; x = 1; }";

    Node root = compiler.parseTree(source, null, null);
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(nodeFactory.getRootNode(), root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.addCallback(eliminator);
    NodeTraversal.traverse(compiler, root, traversal);
    compiler.dispose();

    assertTrue(isAssignmentsPresent(root, "x"));
  }

  private boolean isAssignmentsPresent(Node root, String variable) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.getType() == Token.ASSIGN) {
        Node lhs = child.getFirstChild();
        if (lhs.getString().equals(variable)) {
          return true;
        }
      }
      if (child.getType() == Token.VAR) {
        if (variable.equals(child.getString())) {
          return true;
        }
      }
      if (child.getFirstChild() != null) {
        boolean assignmentsPresent = isAssignmentsPresent(child, variable);
        if (assignmentsPresent) {
          return true;
        }
      }
    }
    return false;
  }
}