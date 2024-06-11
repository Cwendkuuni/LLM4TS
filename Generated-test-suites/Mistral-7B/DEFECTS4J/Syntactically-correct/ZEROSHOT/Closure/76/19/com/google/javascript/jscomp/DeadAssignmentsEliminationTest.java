import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeOp.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private Compiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = new Compiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testEmptyProgram() {
    Node root = Node.NEW_VAR(Token.NAME, "root");
    Node externs = Node.SCOPE();

    deadAssignmentsElimination.process(externs, root);
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertNull(n);
      }

      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });
  }

  @Test
  public void testDeadAssignments() {
    String sourceCode =
        "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  var y = 3;" +
        "  y = x;" +
        "}";

    Node root = AbstractCompiler.parse(sourceCode, null);
    Node externs = Node.SCOPE();

    deadAssignmentsElimination.process(externs, root);
    Node deadNode = null;
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN) {
          Node lhs = n.getFirstChild();
          Node rhs = lhs.getNext();
          if (NodeUtil.isName(lhs) && lhs.getString().equals("x")) {
            deadNode = n;
            return;
          }
        }
      }

      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });

    assertNotNull(deadNode);
    assertEquals(ASSIGN, deadNode.getType());
    assertEquals(TOKEN_IDENT, deadNode.getFirstChild().getType());
    assertEquals(ASSIGN, deadNode.getParent().getType());
    assertEquals(TOKEN_IDENT, deadNode.getParent().getFirstChild().getType());
    assertEquals("x", deadNode.getFirstChild().getString());
    assertEquals("x", deadNode.getParent().getFirstChild().getString());
  }

  @Test
  public void testDeadAssignmentInsideConditionalBranch() {
    String sourceCode =
        "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  } else {" +
        "    x = 3;" +
        "  }" +
        "}";

    Node root = AbstractCompiler.parse(sourceCode, null);
    Node externs = Node.SCOPE();

    deadAssignmentsElimination.process(externs, root);
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN &&
            NodeUtil.isName(n.getFirstChild()) &&
            n.getFirstChild().getString().equals("x")) {
          assertEquals(2, n.getParent().getFirstChild().getIntValue());
          return;
        }
      }

      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });

    // The dead assignment x = 3 is eliminated.
    Node deadNode = null;
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN &&
            NodeUtil.isName(n.getFirstChild()) &&
            n.getFirstChild().getString().equals("x")) {
          assertEquals(1, n.getParent().getFirstChild().getIntValue());
          deadNode = n;
          return;
        }
      }

      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });

    assertNotNull(deadNode);
    assertEquals(ASSIGN, deadNode.getType());
    assertEquals(TOKEN_IDENT, deadNode.getFirstChild().getType());
    assertEquals(ASSIGN, deadNode.getParent().getType());
    assertEquals(TOKEN_IDENT, deadNode.getParent().getFirstChild().getType());
    assertEquals("x", deadNode.getFirstChild().getString());
    assertEquals("x", deadNode.getParent().getFirstChild().getString());
  }

  @Test
  public void testDeadAssignmentInsideLoop() {
    String sourceCode =
        "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x = i;" +
        "  }" +
        "}";

    Node root = AbstractCompiler.parse(sourceCode, null);
    Node externs = Node.SCOPE();

    deadAssignmentsElimination.process(externs, root);
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN &&
            NodeUtil.isName(n.getFirstChild()) &&
            n.getFirstChild().getString().equals("x")) {
          assertEquals(1, n.getParent().getFirstChild().getIntValue());
          return;
        }
      }

      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });

    // The dead assignment x = i is eliminated for each i in the loop.
    Node deadNode = null;
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == ASSIGN &&
            NodeUtil.isName(n.getFirstChild()) &&
            n.getFirstChild().getString().equals("x")) {
          Node loopCondition = NodeUtil.getConditionExpression(parent);
          if (loopCondition != null &&
              loopCondition.getFirstChild().getIntValue() != 0) {
            return;
          }
          deadNode = n;
          return;
        }
      }

      @Override
      public void enterScope(NodeTraversal t) {}

      @Override
      public void exitScope(NodeTraversal t) {}
    });

    assertNotNull(deadNode);
    assertEquals(ASSIGN, deadNode.getType());
    assertEquals(TOKEN_IDENT, deadNode.getFirstChild().getType());
    assertEquals(ASSIGN, deadNode.getParent().getType());
    assertEquals(TOKEN_IDENT, deadNode.getParent().getFirstChild().getType());
    assertEquals("x", deadNode.getFirstChild().getString());
    assertEquals("x", deadNode.getParent().getFirstChild().getString());
  }
}