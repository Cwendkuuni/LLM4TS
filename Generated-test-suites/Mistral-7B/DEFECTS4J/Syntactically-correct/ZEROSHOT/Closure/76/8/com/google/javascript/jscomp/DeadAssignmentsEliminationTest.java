import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DeadAssignmentsEliminationTest.*;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function foo() {" +
        "  var x = 1, y = 1;" +
        "  if (x > 0) {" +
        "    x = 2;" +
        "  } else {" +
        "    y = 2;" +
        "  }" +
        "  return x + y;" +
        "}";

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = AbstractSyntaxTree.getAbstractSyntaxTree(jsCode);
    deadAssignmentsElimination.process(node.getExterns(), node.getRoot());

    ControlFlowGraph<Node> controlFlowGraph = node.getControlFlowGraph();
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getRoot()),
        controlFlowGraph.getNode(node.getChild(1))));
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(1)),
        controlFlowGraph.getNode(node.getChild(3))));
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunctionCalls() {
    String jsCode = "function foo() {" +
        "  var x = 1, y = 1;" +
        "  if (x > 0) {" +
        "    x = 2;" +
        "  } else {" +
        "    y = 2;" +
        "    bar();" +
        "  }" +
        "  return x + y;" +
        "}" +
        "function bar() {}";

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = AbstractSyntaxTree.getAbstractSyntaxTree(jsCode);
    deadAssignmentsElimination.process(node.getExterns(), node.getRoot());

    ControlFlowGraph<Node> controlFlowGraph = node.getControlFlowGraph();
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getRoot()),
        controlFlowGraph.getNode(node.getChild(1))));
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(1)),
        controlFlowGraph.getNode(node.getChild(3))));
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(3)),
        controlFlowGraph.getNode(node.getChild(5))));
  }

  @Test
  public void testDeadAssignmentsEliminationWithForLoop() {
    String jsCode = "function foo() {" +
        "  var x = 1, sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x = 2;" +
        "    sum += x;" +
        "  }" +
        "  return sum;" +
        "}";

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = AbstractSyntaxTree.getAbstractSyntaxTree(jsCode);
    deadAssignmentsElimination.process(node.getExterns(), node.getRoot());

    ControlFlowGraph<Node> controlFlowGraph = node.getControlFlowGraph();
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getRoot()),
        controlFlowGraph.getNode(node.getChild(1))));
    for (int i = 1; i < node.getChildCount(); i++) {
      Node childNode = node.getChild(i);
      if (childNode.getType() == Token.FORINIT) {
        Node initializer = childNode.getFirstChild();
        assertEquals("i", initializer.getString());
        Node condition = childNode.getLastChild();
        assertEquals(Token.LT, condition.getType());
        Node assign = childNode.getPrevious();
        assertEquals(Token.VAR, assign.getType());
        assertEquals("i", assign.getFirstChild().getString());
        break;
      }
    }
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(1)),
        controlFlowGraph.getNode(node.getChild(3))));
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunctionDeclaration() {
    String jsCode = "function foo() {" +
        "  function bar() {}" +
        "  var x = 1, y = 1;" +
        "  if (x > 0) {" +
        "    x = 2;" +
        "  } else {" +
        "    y = 2;" +
        "    bar();" +
        "  }" +
        "  return x + y;" +
        "}";

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = AbstractSyntaxTree.getAbstractSyntaxTree(jsCode);
    deadAssignmentsElimination.process(node.getExterns(), node.getRoot());

    ControlFlowGraph<Node> controlFlowGraph = node.getControlFlowGraph();
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getRoot()),
        controlFlowGraph.getNode(node.getChild(1))));
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(1)),
        controlFlowGraph.getNode(node.getChild(3))));
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(3)),
        controlFlowGraph.getNode(node.getChild(6))));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithVariableDeclaration() {
    String jsCode = "function foo() {" +
        "  var x = 1, y = 1;" +
        "  var x = 2;" +
        "  return x + y;" +
        "}";

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = AbstractSyntaxTree.getAbstractSyntaxTree(jsCode);
    deadAssignmentsElimination.process(node.getExterns(), node.getRoot());

    ControlFlowGraph<Node> controlFlowGraph = node.getControlFlowGraph();
    fail("Dead assignments elimination should have removed the second x=");
  }

  @Test
  public void testDeadAssignmentsEliminationWithReturnStatement() {
    String jsCode = "function foo() {" +
        "  var x = 1, y = 1;" +
        "  if (x > 0) {" +
        "    x = 2;" +
        "    return x;" +
        "  } else {" +
        "    y = 2;" +
        "  }" +
        "  return x + y;" +
        "}";

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node node = AbstractSyntaxTree.getAbstractSyntaxTree(jsCode);
    deadAssignmentsElimination.process(node.getExterns(), node.getRoot());

    ControlFlowGraph<Node> controlFlowGraph = node.getControlFlowGraph();
    assertTrue(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getRoot()),
        controlFlowGraph.getNode(node.getChild(1))));
    assertFalse(controlFlowGraph.hasEdge(controlFlowGraph.getNode(node.getChild(1)),
        controlFlowGraph.getNode(node.getChild(3))));
  }
}