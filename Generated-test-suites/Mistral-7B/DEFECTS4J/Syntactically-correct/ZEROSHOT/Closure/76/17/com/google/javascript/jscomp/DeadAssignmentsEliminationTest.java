package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerPass;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    Scope scope = mock(Scope.class);
    LiveVariablesAnalysis liveness = mock(LiveVariablesAnalysis.class);

    when(compiler.getControlFlowGraph()).thenReturn(cfg);
    when(compiler.getScope()).thenReturn(scope);
    when(liveness.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = Node.newProgram(1);
    Node n1 = Node.newAssign(1, Node.newName("x"), Node.newNumber(1));
    Node n2 = Node.newAssign(1, Node.newName("x"), Node.newNumber(2));
    Node n3 = Node.newAssign(1, Node.newName("x"), Node.newNumber(3));
    Node ifStatement = Node.newIf(1, Node.newName("x"), Node.newBoolean(true));
    Node assignInIf = Node.newAssign(1, Node.newName("x"), Node.newNumber(4));
    Node n4 = Node.newName("x");

    root.addChild(n1);
    root.addChild(ifStatement);
    ifStatement.addChild(n2);
    ifStatement.addChild(assignInIf);
    ifStatement.addChild(n3);
    ifStatement.addChild(n4);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null);
    nodeTraversal.setCurrentNode(root);
    deadAssignmentsElimination.process(null, root);

    DiGraph graph = cfg.getDirectedGraph();
    Iterable<DiGraphNode<Node, Branch>> nodes = graph.getDirectedGraphNodes();

    assertEquals(3, nodes.size());

    DeadAssignmentsElimination.FlowState state = null;
    for (DiGraphNode node : nodes) {
      Node currentNode = node.getValue();
      if (currentNode != null && currentNode.getType() != Token.EMPTY) {
        state = (DeadAssignmentsElimination.FlowState) currentNode.getAnnotation();
        break;
      }
    }

    assertEquals(DeadAssignmentsElimination.FlowState.LIVE, state.getIn());
    assertEquals(DeadAssignmentsElimination.FlowState.DEAD, state.getOut());

    assertEquals(1, nodeTraversal.getNumChildrenRemoved());
    assertEquals(2, nodeTraversal.getNumNodesRemoved());

    nodeTraversal.dispose();
  }
}

class NodeUtil {
  // Helper methods for testing.
  public static boolean isAssign(Node node) {
    return node.getType() == Token.ASSIGN;
  }

  public static boolean isFunction(Node node) {
    return node.getType() == Token.FUNCTION;
  }

  public static boolean isName(Node node) {
    return node.getType() == Token.NAME;
  }

  public static boolean isLhs(Node node, Node parent) {
    return parent.getType() == Token.ASSIGN && node.getFirstChild() == node;
  }

  public static boolean has(Node node, Predicate<Node> predicate,
      Predicate<Node> additionalCondition) {
    Node currentNode = node;
    while (currentNode != null) {
      if (predicate.apply(currentNode) && (additionalCondition == null || additionalCondition.apply(currentNode))) {
        return true;
      }
      currentNode = currentNode.getParent();
    }
    return false;
  }

  public static Node getConditionExpression(Node node) {
    if (node.getType() == Token.IF) {
      return node.getFirstChild();
    } else if (node.getType() == Token.WHILE) {
      return Node.newExpression(1, Node.OP_LOGIC_AND,
          Node.newName("condition"), Node.newName("expression"));
      // Assuming while(condition) {expression}
    } else if (node.getType() == Token.DO) {
      Node whileStatement = Node.newWhile(
          Node.newName("condition"), Node.newList(node.getLastChild()));
      return whileStatement.getFirstChild();
    } else if (node.getType() == Token.FOR) {
      Node forStatement = Node.newFor(
          Node.newName("initializer"), Node.newName("condition"),
          Node.newList(Node.newName("incrementer"), node.getLastChild()));
      return forStatement.getFirstChild();
    } else if (node.getType() == Token.SWITCH) {
      return node.getFirstChild();
    } else if (node.getType() == Token.CASE) {
      return node.getPrevious();
    } else if (node.getType() == Token.RETURN) {
      return Node.newExpression(1, Node.OP_VOID, null);
    }
    return null;
  }

  public static Node newAssign(int line, Node lhs, Node rhs) {
    return new Node(Token.ASSIGN, lhs, rhs);
  }

  public static Node newName(int line, String name) {
    return new Node(Token.NAME, new String(name), null);
  }

  public static Node newNumber(int line, double value) {
    return new Node(Token.NUMBER, null, new Double(value));
  }

  public static Node newProgram(int line) {
    return Node.newProgram(line);
  }

  public static Node newFunction(int line, String name, int length) {
    return Node.newFunction(line, name, length, Node.newList());
  }

  public static Node newAssignOp(int line, int op) {
    return new Node(Token.OP_ASSIGN, null, null);
  }

  public static Node newIf(int line, Node condition, Node thenBranch,
      Node elseBranch) {
    return new Node(Token.IF, condition, thenBranch, elseBranch);
  }

  public static Node newWhile(int line, Node condition, Node body) {
    return Node.newWhile(line, condition, body);
  }

  public static Node newDo(int line, Node body, Node condition) {
    return Node.newDo(line, body, condition);
  }

  public static Node newFor(int line, Node initializer, Node condition,
      Node incrementer, Node body) {
    return Node.newFor(line, initializer, condition, incrementer, body);
  }

  public static Node newSwitch(int line, Node expression) {
    return new Node(Token.SWITCH, expression);
  }

  public static Node newCase(int line, int caseValue, Node body) {
    return new Node(Token.CASE, Node.newNumber(caseValue), body);
  }

  public static Node newList(Node... nodes) {
    return new Node(Token.LIST, null, nodes);
  }

  public static boolean isExpressionNode(Node node) {
    return node.getLastChild() == null;
  }

  public static Node newExpression(int line, int op, Node left, Node right) {
    return new Node(Token.BINARY_EXPRESSION, op, left, right);
  }

  public static int getOpFromAssignmentOp(Node node) {
    return node.getType();
  }
}