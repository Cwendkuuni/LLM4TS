import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

/**
 * Unit tests for {@link DeadAssignmentsElimination}.
 */
public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case for dead assignment elimination.
    String sourceCode = "function f() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    x = 2;\n" +
        "  } else {\n" +
        "    x = 3;\n" +
        "  }\n" +
        "  return x;\n" +
        "}\n";

    Node root = AbstractNode.parse(sourceCode, null, null);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    traverse(compiler, root, deadAssignmentsElimination);

    ControlFlowGraph<Node> cfg = deadAssignmentsElimination.liveness.getCfg();
    assertNotNull(cfg);

    DiGraphNode<Node, Branch> xNode = null;
    for (DiGraphNode<Node, Branch> node : cfg.getDirectedGraphNodes()) {
      if (node.getValue() != null &&
          hasType(node.getValue(), Token.VAR) &&
          node.getValue().getString().equals("x")) {
        xNode = node;
        break;
      }
    }

    assertNotNull(xNode);

    FlowState<LiveVariablesAnalysis.LiveVariableLattice> state = xNode.getAnnotation();
    assertEquals(LiveVariablesAnalysis.LiveVariableLattice.LIVE, state.getOut().getValue());
    assertEquals(LiveVariablesAnalysis.LiveVariableLattice.DEAD, state.getIn().getValue());

    Node xAssignNode = null;
    for (Node n = root; n != null; n = n.getParent()) {
      if (n.getType() == Token.ASSIGN &&
          hasType(n.getFirstChild(), Token.NAME) &&
          n.getFirstChild().getString().equals("x")) {
        xAssignNode = n;
        break;
      }
    }

    assertNotNull(xAssignNode);

    AbstractNode removedNode = xAssignNode.getNext();
    assertNull(removedNode);

    // Test case for dead assignment elimination with a function.
    sourceCode = "function f() {\n" +
        "  var x = 1;\n" +
        "  return function g() {\n" +
        "    x = 2;\n" +
        "  };\n" +
        "}\n";

    root = AbstractNode.parse(sourceCode, null, null);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    traverse(compiler, root, deadAssignmentsElimination);

    cfg = deadAssignmentsElimination.liveness.getCfg();
    assertNotNull(cfg);

    xNode = null;
    for (DiGraphNode<Node, Branch> node : cfg.getDirectedGraphNodes()) {
      if (node.getValue() != null &&
          hasType(node.getValue(), Token.VAR) &&
          node.getValue().getString().equals("x")) {
        xNode = node;
        break;
      }
    }

    assertNotNull(xNode);

    state = xNode.getAnnotation();
    assertEquals(LiveVariablesAnalysis.LiveVariableLattice.LIVE, state.getOut().getValue());
    assertEquals(LiveVariablesAnalysis.LiveVariableLattice.LIVE, state.getIn().getValue());

    xAssignNode = null;
    for (Node n = root; n != null; n = n.getParent()) {
      if (n.getType() == Token.ASSIGN &&
          hasType(n.getFirstChild(), Token.NAME) &&
          n.getFirstChild().getString().equals("x")) {
        xAssignNode = n;
        break;
      }
    }

    assertNotNull(xAssignNode);

    removedNode = xAssignNode.getNext();
    assertNotNull(removedNode);

    assertEquals(Token.NAME, removedNode.getType());
    assertEquals("g", removedNode.getString());
  }
}