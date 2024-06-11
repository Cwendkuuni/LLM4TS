package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import junit.framework.TestCase;

/**
 * Tests for MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest extends TestCase {

  private Node createCodeBlock(NodeFactory nodeFactory, Node n) {
    Node block = nodeFactory.block();
    block.addChildToBack(n);
    return block;
  }

  private Node createIfStatement(NodeFactory nodeFactory, Node condition) {
    Node ifStatement = nodeFactory.ifStatement();
    ifStatement.setCondition(condition);
    Node ifBlock = createCodeBlock(nodeFactory, nodeFactory.block());
    ifStatement.setTrueQuad(ifBlock);
    if (condition.getType() == Token.IF) {
      Node elseBlock = createCodeBlock(nodeFactory, nodeFactory.block());
      ifStatement.setFalseQuad(elseBlock);
      return ifStatement;
    }
    return ifStatement;
  }

  private Node createWhileLoop(NodeFactory nodeFactory, Node condition) {
    Node whileLoop = nodeFactory.whileStatement();
    whileLoop.setCondition(condition);
    Node loopBlock = createCodeBlock(nodeFactory, nodeFactory.block());
    whileLoop.setBody(loopBlock);
    return whileLoop;
  }

  private Node createLabel(NodeFactory nodeFactory, String name) {
    Node label = nodeFactory.label();
    label.setName(name);
    return label;
  }

  private Node createBreakStatement(NodeFactory nodeFactory, String labelName) {
    Node breakStatement = nodeFactory.returnStatement();
    breakStatement.setType(Token.BREAK);
    if (labelName != null) {
      Node label = createLabel(nodeFactory, labelName);
      breakStatement.setLabel(label);
    }
    return breakStatement;
  }

  private Node createFunctionDeclaration(NodeFactory nodeFactory, String name) {
    Node functionDeclaration = nodeFactory.functionDeclaration();
    functionDeclaration.setName(name);
    return functionDeclaration;
  }

  private Node createReturnStatement(NodeFactory nodeFactory) {
    Node returnStatement = nodeFactory.returnStatement();
    return returnStatement;
  }

  private Node createContinueStatement(NodeFactory nodeFactory) {
    Node continueStatement = nodeFactory.continueStatement();
    return continueStatement;
  }

  /**
   * Test that explicit return statement is removed when there are no more
   * statements following it.
   */
  public void testReturnStatementAtEnd() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);

    Node returnStatement = createReturnStatement(nodeFactory);
    functionDeclaration.addChildToBack(returnStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertTrue("Return statement should not exist", returnStatement.isRemoved());
  }

  /**
   * Test that explicit return statement is removed when there are no more
   * statements following it except function return type.
   */
  public void testReturnStatementAtEndWithFunctionReturnType() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);
    functionDeclaration.setType(Token.FUNCTION);

    Node returnStatement = createReturnStatement(nodeFactory);
    functionDeclaration.addChildToBack(returnStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertTrue("Return statement should not exist", returnStatement.isRemoved());
  }

  /**
   * Test that explicit return statement is removed when there is no more
   * statements following it except function return type and comments.
   */
  public void testReturnStatementAtEndWithFunctionReturnTypeAndComments() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);
    functionDeclaration.setType(Token.FUNCTION);

    Node comment = nodeFactory.comment("This is a comment");
    Node returnStatement = createReturnStatement(nodeFactory);

    functionDeclaration.addChildToBack(comment);
    functionDeclaration.addChildToBack(returnStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertTrue("Return statement should not exist", returnStatement.isRemoved());
  }

  /**
   * Test that explicit return statement is not removed when there are more
   * statements following it.
   */
  public void testReturnStatementNotAtEnd() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);

    Node returnStatement = createReturnStatement(nodeFactory);
    Node nextStatement = createFunctionDeclaration(nodeFactory, "next");

    functionDeclaration.addChildToBack(returnStatement);
    functionDeclaration.addChildToBack(nextStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertFalse("Return statement should exist", returnStatement.isRemoved());
  }

  /**
   * Test that explicit continue statement is removed when there are no more
   * statements following it.
   */
  public void testContinueStatementAtEnd() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);

    Node whileLoop = createWhileLoop(nodeFactory, nodeFactory.literal(true));
    functionDeclaration.addChildToBack(whileLoop);

    Node continueStatement = createContinueStatement(nodeFactory);
    whileLoop.setBody(createCodeBlock(nodeFactory, continueStatement));

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertTrue("Continue statement should not exist", continueStatement.isRemoved());
  }

  /**
   * Test that explicit break statement is removed when there are no more
   * statements following it and there is a label.
   */
  public void testBreakStatementAtEndWithLabel() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);

    Node label = createLabel(nodeFactory, "outer");
    Node whileLoop = createWhileLoop(nodeFactory, nodeFactory.literal(true));
    Node breakStatement = createBreakStatement(nodeFactory, "outer");

    functionDeclaration.addChildToBack(label);
    functionDeclaration.addChildToBack(whileLoop);
    whileLoop.setBody(createCodeBlock(nodeFactory, breakStatement));

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertTrue("Break statement should not exist", breakStatement.isRemoved());
  }

  /**
   * Test that explicit break statement is not removed when there are more
   * statements following it or there is no label.
   */
  public void testBreakStatementNotAtEndOrNoLabel() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);

    Node label = createLabel(nodeFactory, "outer");
    Node whileLoop = createWhileLoop(nodeFactory, nodeFactory.literal(true));
    Node breakStatement = createBreakStatement(nodeFactory, null);

    functionDeclaration.addChildToBack(label);
    functionDeclaration.addChildToBack(whileLoop);
    whileLoop.setBody(createCodeBlock(nodeFactory, createContinueStatement(nodeFactory)));
    whileLoop.setBody(whileLoop.getBody().addChildToBack(breakStatement));

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertFalse("Break statement should exist", breakStatement.isRemoved());
  }

  /**
   * Test that explicit break statement is not removed when there are more
   * statements following it and there is a label.
   */
  public void testBreakStatementNotAtEndWithLabel() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    functionDeclaration.setBody(null);

    Node label = createLabel(nodeFactory, "outer");
    Node whileLoop = createWhileLoop(nodeFactory, nodeFactory.literal(true));
    Node breakStatement = createBreakStatement(nodeFactory, "outer");

    functionDeclaration.addChildToBack(label);
    functionDeclaration.addChildToBack(whileLoop);
    whileLoop.setBody(createCodeBlock(nodeFactory, createContinueStatement(nodeFactory)));
    whileLoop.setBody(whileLoop.getBody().addChildToBack(breakStatement));

    Node nextStatement = createFunctionDeclaration(nodeFactory, "next");
    functionDeclaration.addChildToBack(nextStatement);

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertFalse("Break statement should exist", breakStatement.isRemoved());
  }

  /**
   * Test that explicit label is not removed.
   */
  public void testLabelNotRemoved() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");
    Node label = createLabel(nodeFactory, "outer");

    functionDeclaration.addChildToBack(label);

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertNull("Label should not be removed", label.getNext());
  }

  /**
   * Test that explicit function declaration is not removed.
   */
  public void testFunctionDeclarationNotRemoved() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.program();

    Node functionDeclaration = createFunctionDeclaration(nodeFactory, "test");

    NodeTraversal nodeTraversal = new NodeTraversal(new MinimizeExitPoints(null));
    nodeTraversal.traverse(null, root, null);

    assertNull("Function declaration should not be removed", functionDeclaration.getNext());
  }
}