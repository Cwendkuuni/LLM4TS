package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

/**
 * Test class for MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest {

  /**
   * Test method for MinimizeExitPoints class.
   */
  @org.junit.Test
  public void testMinimizeExitPoints() {
    // Create a mock compiler for testing.
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    // Create a NodeFactory for building test nodes.
    NodeFactory nodeFactory = new NodeFactory();

    // Initialize test data.
    Node root = nodeFactory.literal(null);
    Node functionNode = NodeUtil.createFunction(nodeFactory, "testFunction", new Node[0], null, null, null);
    Node functionBody = nodeFactory.block();
    Node ifNode1 = nodeFactory.ifStatement(nodeFactory.literal(true), null, functionBody);
    Node ifBody1 = nodeFactory.literal(0);
    Node returnNode1 = nodeFactory.returnStatement(nodeFactory.literal(1));
    Node ifElseNode1 = nodeFactory.elseKeyword();
    Node ifElseBody1 = nodeFactory.literal(2);
    Node labelNode1 = nodeFactory.labelStatement("label1");
    Node breakNode1 = nodeFactory.breakStatement("label1");
    Node elseIfNode1 = nodeFactory.elseifStatement(nodeFactory.literal(1), null, ifBody1, null);
    Node ifNode2 = nodeFactory.ifStatement(nodeFactory.literal(false), null, functionBody);
    Node ifBody2 = nodeFactory.literal(3);
    Node returnNode2 = nodeFactory.returnStatement(nodeFactory.literal(2));
    Node elseNode2 = nodeFactory.elseKeyword();
    Node ifElseBody2 = nodeFactory.literal(4);
    Node labelNode2 = nodeFactory.labelStatement("label2");
    Node continueNode = nodeFactory.continueStatement();
    Node bodyNode = nodeFactory.block();
    Node labelNode3 = nodeFactory.labelStatement("label3");
    Node breakNode3 = nodeFactory.breakStatement("label3");

    // Set up test data.
    functionBody.addChildToBack(ifNode1);
    functionBody.addChildToBack(ifElseNode1);
    functionBody.addChildToBack(ifNode2);
    functionBody.addChildToBack(elseNode2);
    functionBody.addChildToBack(labelNode1);
    functionBody.addChildToBack(breakNode1);
    functionBody.addChildToBack(elseIfNode1);
    functionBody.addChildToBack(ifBody1);
    functionBody.addChildToBack(ifElseBody1);
    functionBody.addChildToBack(labelNode2);
    functionBody.addChildToBack(continueNode);
    functionBody.addChildToBack(ifBody2);
    functionBody.addChildToBack(ifElseBody2);
    functionBody.addChildToBack(bodyNode);
    functionBody.addChildToBack(labelNode3);
    functionBody.addChildToBack(breakNode3);
    functionNode.setBody(functionBody);
    root.addChildToBack(functionNode);

    // Initialize test object.
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    // Perform test.
    minimizeExitPoints.process(null, root);

    // Verify test results.
    Node expectedRoot = nodeFactory.literal(null);
    Node expectedFunctionNode = nodeFactory.functionDeclaration(
        "testFunction", new Node[0], null, null, null, null, null, null, null,
        nodeFactory.block());
    Node expectedFunctionBody = nodeFactory.block();
    Node expectedIfNode1 = nodeFactory.ifStatement(nodeFactory.literal(true), null, null);
    Node expectedIfElseNode1 = nodeFactory.ifStatement(
        null, nodeFactory.block(), null, null);
    Node expectedIfBody1 = nodeFactory.block().addChild(ifBody1);
    Node expectedIfElseBody1 = nodeFactory.block().addChild(ifElseBody1);
    Node expectedLabelNode1 = nodeFactory.labelStatement("label1");
    Node expectedContinueNode = nodeFactory.continueStatement();
    Node expectedElseNode2 = nodeFactory.elseKeyword();
    Node expectedIfElseBody2 = nodeFactory.block().addChild(ifElseBody2);
    Node expectedLabelNode2 = nodeFactory.labelStatement("label2");
    Node expectedBodyNode = nodeFactory.block().addChild(bodyNode);
    Node expectedLabelNode3 = nodeFactory.labelStatement("label3");

    assertEquals(expectedRoot, root);
    assertEquals(expectedFunctionNode, functionNode);
    assertEquals(expectedFunctionBody, functionBody);
    assertEquals(expectedIfNode1, functionBody.getFirstChild());
    assertEquals(expectedIfElseNode1, expectedIfNode1.getNext());
    assertEquals(expectedIfBody1, expectedIfElseNode1.getNext());
    assertEquals(expectedLabelNode1, expectedIfElseNode1.getNext());
    assertEquals(expectedContinueNode, functionBody.getChild(3));
    assertEquals(expectedElseNode2, functionBody.getChild(5));
    assertEquals(expectedIfElseBody2, expectedElseNode2.getNext());
    assertEquals(expectedLabelNode2, functionBody.getChild(6));
    assertEquals(expectedBodyNode, functionBody.getLastChild());
    assertEquals(expectedLabelNode3, functionBody.getLastChild());
  }
}