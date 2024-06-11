import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isOpFromAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.isVariableLhs;
import static com.google.javascript.jscomp.NodeUtil.matches;
import static com.google.javascript.jscomp.NodeUtil.matchesPredicate;
import static com.google.javascript.jscomp.NodeUtil.matchesToken;
import static com.google.javascript.jscomp.NodeUtil.newNode;
import static com.google.javascript.jscomp.NodeUtil.newNumber;
import static com.google.javascript.jscomp.NodeUtil.newToken;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private Node root;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();

    root = new Node(newToken(Token.PROGRAM, 1, 1), null);
    Node externs = new Node(newToken(Token.PROGRAM, 1, 1), root);

    Node a = newName("a");
    Node b = newName("b");
    Node c = newName("c");

    root.appendChild(externs);

    Node block = newNode(newToken(Token.BLOCK, 1, 1), root);

    // Assignment 1
    Node assignment1 = newNode(newToken(Token.ASSIGN, 1, 2), block);
    Node lhs1 = newNode(newToken(Token.NAME, 2, 3), a);
    Node rhs1 = newNode(newToken(Token.NUMBER, 3, 4), 1);
    assignment1.appendChild(lhs1);
    assignment1.appendChild(rhs1);

    // Comparison 1
    Node comparison1 = newNode(newToken(Token.COMPARISON, 1, 5), block);
    Node lhs2 = newNode(newToken(Token.NAME, 2, 6), a);
    Node rhs2 = newNode(newToken(Token.NAME, 2, 7), b);
    comparison1.appendChild(lhs2);
    comparison1.appendChild(rhs2);

    // Assignment 2
    Node assignment2 = newNode(newToken(Token.ASSIGN, 1, 8), block);
    Node lhs3 = newNode(newToken(Token.NAME, 2, 9), a);
    Node rhs3 = newNode(newToken(Token.NUMBER, 3, 10), 2);
    assignment2.appendChild(lhs3);
    assignment2.appendChild(rhs3);

    // Assignment 3
    Node assignment3 = newNode(newToken(Token.ASSIGN, 1, 11), block);
    Node lhs4 = newNode(newToken(Token.NAME, 2, 12), b);
    Node rhs4 = newNode(newToken(Token.NAME, 2, 13), c);
    assignment3.appendChild(lhs4);
    assignment3.appendChild(rhs4);

    // If statement
    Node ifStatement = newNode(newToken(Token.IF, 1, 14), block);
    Node condition = comparison1;
    ifStatement.appendChild(condition);

    // Assignment 4
    Node assignment4 = newNode(newToken(Token.ASSIGN, 1, 15), block);
    Node lhs5 = newNode(newToken(Token.NAME, 2, 16), a);
    Node rhs5 = newNode(newToken(Token.NAME, 2, 17), c);
    assignment4.appendChild(lhs5);
    assignment4.appendChild(rhs5);

    // Return statement
    Node returnStatement = newNode(newToken(Token.RETURN, 1, 18), block);

    // Assignment 5
    Node assignment5 = newNode(newToken(Token.ASSIGN, 1, 19), block);
    Node lhs6 = newNode(newToken(Token.NAME, 2, 20), c);
    Node rhs6 = newNode(newToken(Token.NUMBER, 3, 21), 3);
    assignment5.appendChild(lhs6);
    assignment5.appendChild(rhs6);

    block.appendChild(assignment1);
    block.appendChild(comparison1);
    block.appendChild(assignment2);
    block.appendChild(assignment3);
    block.appendChild(ifStatement);
    block.appendChild(assignment4);
    block.appendChild(returnStatement);
    block.appendChild(assignment5);

    compiler.process(externs, root);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    ControlFlowGraph<Node> controlFlowGraph =
        ((AbstractNodeTraversal) compiler.getTraversals().get(0))
            .getControlFlowGraph();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(externs, root);

    DiGraph<Node, Branch> annotatedGraph = controlFlowGraph.getDirectedGraph();

    // Verify that assignment 1 is removed
    assertEquals(2, annotatedGraph.getNodes().size());
    DiGraphNode<Node, Branch> deadNode1 = annotatedGraph.getNode(assignment1);
    assertNull(deadNode1);

    // Verify that assignment 3 is removed
    DiGraphNode<Node, Branch> liveNode3 = annotatedGraph.getNode(assignment3);
    assertNotNull(liveNode3);
    Node removedAssignment3 = liveNode3.getValue();
    assertEquals(Token.VOID, removedAssignment3.getType());

    // Verify that assignment 4 is removed
    DiGraphNode<Node, Branch> liveNode4 = annotatedGraph.getNode(assignment4);
    assertNotNull(liveNode4);
    Node removedAssignment4 = liveNode4.getValue();
    assertEquals(Token.VOID, removedAssignment4.getType());

    // Verify that assignment 5 is not removed
    DiGraphNode<Node, Branch> liveNode5 = annotatedGraph.getNode(assignment5);
    assertNotNull(liveNode5);

    // Verify that the control flow graph is not affected by the elimination
    List<DiGraphNode<Node, Branch>> nodes = annotatedGraph.getDirectedGraphNodes();
    List<Node> originalOrder = Arrays.asList(
        assignment1,
        comparison1,
        assignment2,
        assignment3,
        ifStatement,
        assignment4,
        returnStatement,
        assignment5);

    List<Node> newOrder = Lists.newArrayListWithExpectedSize(nodes.size());
    for (DiGraphNode<Node, Branch> node : nodes) {
      newOrder.add(node.getValue());
    }

    assertEquals(originalOrder, newOrder);
  }

  private Node newName(String name) {
    return new Node(newToken(Token.NAME, 1, 1), root);
  }

  private static Predicate<Node> matchesPredicate(Node node, Predicate<Node> predicate) {
    return new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return matches(n, predicate, null);
      }
    };
  }

  private static Predicate<Node> matchesToken(int type, int line, int column) {
    return matchesPredicate(new Node(newToken(type, line, column), null),
        node -> node.getType() == type);
  }

  private static Predicate<Node> isAssignmentOp() {
    return matchesPredicate(new Node(newToken(Token.ASSIGN, 1, 1), null),
        isAssignmentOp);
  }

  private static Predicate<Node> isFunction() {
    return matchPredicate(isFunction, null);
  }

  private static Predicate<Node> isName() {
    return matchesPredicate(new Node(newToken(Token.NAME, 1, 1), null),
        isName);
  }

  private static Predicate<Node> isVariableLhs() {
    return matchPredicate(isVariableLhs, null);
  }

  private static Predicate<Node> matchAssignmentOp(int opType) {
    return matchesPredicate(
        new Node(newToken(opType, 1, 1), null),
        node -> node.getType() == opType && isAssignmentOp().apply(node.getFirstChild()));
  }

  private static Predicate<Node> matchOpFromAssignmentOp(int opType) {
    return matchPredicate(matchOpFromAssignmentOp, opType);
  }

  private static Predicate<Node> matchRemovableAssigns() {
    return new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return (isAssignmentOp().apply(n) ||
               matchesToken(Token.INC, n.getLineNumber(), n.getLineOffsetInNode())
                   || matchesToken(Token.DEC, n.getLineNumber(), n.getLineOffsetInNode()));
      }
    };
  }
}