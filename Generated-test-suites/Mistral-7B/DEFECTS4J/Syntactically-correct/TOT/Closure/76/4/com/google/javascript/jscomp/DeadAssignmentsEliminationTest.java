package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.rhino.Token.ASSIGN;
import static com.google.javascript.rhino.Token.IF;
import static com.google.javascript.rhino.Token.RETURN;
import static org.junit.Assert.*;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for the {@link DeadAssignmentsElimination} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeadAssignmentsEliminationTest {

  @Mock private AbstractCompiler compiler;
  @Mock private ControlFlowGraph<Node> controlFlowGraph;
  @Mock private LiveVariablesAnalysis liveVariablesAnalysis;
  @Mock private NodeTraversal nodeTraversal;
  @Mock private Node rootNode;

  @Test
  public void testDeadAssignmentElimination() {
    // Setup
    when(compiler.getControlFlowGraph()).thenReturn(controlFlowGraph);
    when(controlFlowGraph.newInstance()).thenReturn(new ControlFlowGraph<Node>() {});

    when(liveVariablesAnalysis.analyze()).thenReturn(true);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // Test case: Dead assignment elimination for simple if statement.
    Node ifStatementNode = createNode(IF, new Node[0], new Node[0]);
    Node assignmentNode1 = createNode(ASSIGN, new Node[0], new Node[0]);
    Node assignmentNode2 = createNode(ASSIGN, new Node[0], new Node[0]);
    Node variableNode = createNameNode("x");

    assignmentNode1.setNext(variableNode);
    assignmentNode1.setParent(ifStatementNode);
    assignmentNode2.setNext(new Node(RETURN, new Node[0], new Node[0]));
    assignmentNode2.setParent(ifStatementNode);

    variableNode.setParent(assignmentNode1);

    Node conditionNode = createNode(createLiteral(true));
    ifStatementNode.setNext(conditionNode);
    ifStatementNode.setParent(rootNode);

    conditionNode.setParent(ifStatementNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, rootNode);

    assertEquals(1, nodeTraversal.size());
    Node eliminatedAssignmentNode = nodeTraversal.getFirst();
    assertFalse(nodeTraversal.has(assignmentNode1));
    assertFalse(nodeTraversal.has(assignmentNode2));
    assertTrue(nodeTraversal.has(eliminatedAssignmentNode));

    // Test case: Dead assignment elimination for if statement with else branch.
    Node elseBranchNode = createNode(RETURN, new Node[0], new Node[0]);
    Node assignmentNode1ElseBranch = createNode(ASSIGN, new Node[0], new Node[0]);

    assignmentNode1ElseBranch.setParent(elseBranchNode);
    elseBranchNode.setParent(ifStatementNode);

    variableNode.setParent(assignmentNode1ElseBranch);

    assertFalse(nodeTraversal.has(assignmentNode1));
    assertFalse(nodeTraversal.has(assignmentNode2));
    assertTrue(nodeTraversal.has(assignmentNode1ElseBranch));

    // Test case: Dead assignment elimination for for loop.
    Node forLoopNode = createNode(Token.FOR, new Node[0], new Node[0]);
    Node forConditionNode = createNode(createLiteral(true));

    forLoopNode.setParent(rootNode);
    forConditionNode.setParent(forLoopNode);

    Node assignmentNode1For = createNode(ASSIGN, new Node[0], new Node[0]);
    Node assignmentNode2For = createNode(ASSIGN, new Node[0], new Node[0]);
    Node incrementNodeFor = createNode(Token.INC, new Node[0], new Node[0]);

    assignmentNode1For.setNext(assignmentNode2For);
    assignmentNode1For.setParent(forLoopNode);
    assignmentNode2For.setNext(incrementNodeFor);
    assignmentNode2For.setParent(forLoopNode);
    incrementNodeFor.setParent(assignmentNode2For);

    Node variableNodeFor = createNameNode("i");
    variableNodeFor.setParent(assignmentNode1For);

    assertFalse(nodeTraversal.has(assignmentNode1For));
    assertFalse(nodeTraversal.has(assignmentNode2For));
    assertTrue(nodeTraversal.has(incrementNodeFor));

    // Test case: Dead assignment elimination for function call.
    Node functionCallNode = createFunctionCallNode("unusedFunction");
    Node assignmentNode1Function = createNode(ASSIGN, new Node[0], new Node[0]);
    Node assignmentNode2Function = createNode(ASSIGN, new Node[0], new Node[0]);

    assignmentNode1Function.setNext(functionCallNode);
    assignmentNode1Function.setParent(rootNode);
    assignmentNode2Function.setNext(new Node(RETURN, new Node[0], new Node[0]));
    assignmentNode2Function.setParent(rootNode);

    functionCallNode.setParent(assignmentNode1Function);

    assertFalse(nodeTraversal.has(assignmentNode1Function));
    assertFalse(nodeTraversal.has(assignmentNode2Function));
    assertTrue(nodeTraversal.has(functionCallNode));

    // Test case: Dead assignment elimination for complex expression.
    Node complexExpression1 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression2 = createNameNode("x");
    Node complexExpression3 = createNode(Token.INC, new Node[0], new Node[0]);
    Node complexExpression4 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression5 = createNameNode("y");
    Node complexExpression6 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression7 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression8 = createNameNode("z");
    Node complexExpression9 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression10 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression11 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression12 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression13 = createNode(Token.ASSIGN, new Node[0], new Node[0]);
    Node complexExpression14 = createNode(Token.ASSIGN, new Node[0], new Node[0]);

    complexExpression1.setNext(complexExpression2);
    complexExpression1.setParent(complexExpression10);
    complexExpression10.setNext(complexExpression11);
    complexExpression10.setParent(rootNode);

    complexExpression2.setNext(complexExpression3);
    complexExpression2.setParent(complexExpression1);

    complexExpression3.setNext(complexExpression4);
    complexExpression3.setParent(complexExpression2);

    complexExpression4.setNext(complexExpression5);
    complexExpression4.setParent(complexExpression3);

    complexExpression5.setNext(complexExpression6);
    complexExpression5.setParent(complexExpression4);

    complexExpression6.setNext(complexExpression7);
    complexExpression6.setParent(complexExpression5);

    complexExpression7.setNext(complexExpression8);
    complexExpression7.setParent(complexExpression6);

    complexExpression8.setNext(complexExpression9);
    complexExpression8.setParent(complexExpression7);

    complexExpression9.setNext(complexExpression10);
    complexExpression9.setParent(complexExpression8);

    complexExpression11.setNext(complexExpression12);
    complexExpression11.setParent(complexExpression10);

    complexExpression12.setNext(complexExpression13);
    complexExpression12.setParent(complexExpression11);

    complexExpression13.setNext(complexExpression14);
    complexExpression13.setParent(complexExpression12);

    complexExpression14.setNext(createNameNode("unusedVariable"));
    complexExpression14.setParent(complexExpression13);

    assertFalse(nodeTraversal.has(complexExpression1));
    assertFalse(nodeTraversal.has(complexExpression11));
    assertFalse(nodeTraversal.has(complexExpression12));
    assertFalse(nodeTraversal.has(complexExpression13));
    assertTrue(nodeTraversal.has(complexExpression14));

    // Test case: Dead assignment elimination for return statement.
    Node returnStatementNode = createNode(RETURN, new Node[0], new Node[0]);
    Node assignmentNode1Return = createNode(ASSIGN, new Node[0], new Node[0]);
    Node assignmentNode2Return = createNode(ASSIGN, new Node[0], new Node[0]);

    assignmentNode1Return.setParent(returnStatementNode);
    assignmentNode2Return.setNext(returnStatementNode);
    returnStatementNode.setParent(rootNode);

    assertFalse(nodeTraversal.has(assignmentNode1Return));
    assertFalse(nodeTraversal.has(assignmentNode2Return));
    assertTrue(nodeTraversal.has(returnStatementNode));
  }

  private Node createFunctionCallNode(String name) {
    Node functionCallNode = new Node(Token.CALL, new Node[0], new Node[0]);
    Node functionNode = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    Node functionNameNode = createNameNode(name);
    Node argumentsNode = new Node(Token.ARRAY_LITERAL, new Node[0], new Node[0]);

    functionNameNode.setParent(functionCallNode);
    functionNode.setParent(functionCallNode);
    functionCallNode.setParent(rootNode);
    functionNode.setNext(argumentsNode);

    return functionCallNode;
  }

  private Node createNameNode(String name) {
    return new Node(Token.NAME, new Node[0], new Node[0], name);
  }

  private Node createLiteral(boolean value) {
    return new Node(Token.BOOL, new Node[0], new Node[0], value);
  }

  private Node createNode(int token, Node... children) {
    Node node = new Node(token, children);
    node.setInformation(rootNode.getInformation());
    return node;
  }

  private Node createNode(int token, Node parent, Node... children) {
    Node node = new Node(token, children);
    node.setParent(parent);
    node.setInformation(parent.getInformation());
    return node;
  }
}