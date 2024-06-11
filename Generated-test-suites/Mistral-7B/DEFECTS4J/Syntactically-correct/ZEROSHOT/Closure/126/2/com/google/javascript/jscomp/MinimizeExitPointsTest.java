package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import junit.framework.TestCase;

/**
 * Unit tests for the MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest extends TestCase {

  private static final String TEST_CASE_NAME = "MinimizeExitPointsTest";

  private AbstractCompiler compiler;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler();
  }

  /**
   * Helper function to create a simple AST node structure that can be used
   * to test MinimizeExitPoints.
   *
   * @return A simple Node structure that can be used for testing.
   */
  private Node getTestNode() {
    NodeFactory nodeFactory = new NodeFactory();
    Node root = nodeFactory.createRoot();
    Node label = nodeFactory.createLabel("testLabel");
    Node body = nodeFactory.createBlock();
    Node exit1 = nodeFactory.createBreak(0);
    Node exit2 = nodeFactory.createReturnStatement(nodeFactory.createName("exit"));
    Node cond = nodeFactory.createBinaryOperator(Token.LOGICAL_AND,
        nodeFactory.createLiteral(true), nodeFactory.createLiteral(true), null);
    Node ifNode = nodeFactory.createIfStatement(cond, body, null);
    Node exit1Parent = nodeFactory.createBlock();
    exit1Parent.addChildToBack(ifNode);
    exit1Parent.addChildToBack(exit1);
    Node exit2Parent = nodeFactory.createBlock();
    exit2Parent.addChildToBack(ifNode);
    exit2Parent.addChildToBack(exit2);
    root.addChildToBack(label);
    root.addChildToBack(exit1Parent);
    root.addChildToBack(exit2Parent);
    return root;
  }

  /**
   * Tests MinimizeExitPoints process method.
   */
  public void testMinimizeExitPointsProcess() {
    Node node = getTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(null, node);

    // Check that all exit nodes have been removed.
    assertNull("Exit 1 should be removed",
        NodeUtil.findFirstDescendantWithType(node, Token.BREAK));
    assertNull("Exit 2 should be removed",
        NodeUtil.findFirstDescendantWithType(node, Token.RETURN));

    assertTrue("Test label should be present",
        NodeUtil.findFirstDescendantWithType(node, Token.LABEL) != null);
  }

  /**
   * Tests MinimizeExitPoints visit method handling a label.
   */
  public void testMinimizeExitPointsLabel() {
    Node node = getTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node label = NodeUtil.findFirstDescendantWithType(node, Token.LABEL);
    Node labelBlock = label.getLastChild();

    minimizeExitPoints.visit(null, labelBlock, null);

    // Check that the break node has been moved into the labelBlock.
    Node movedBreakNode = NodeUtil.findFirstDescendantWithType(labelBlock,
        Token.BREAK);
    assertNotNull("Moved break node should be present", movedBreakNode);

    // Check that the return node has been removed.
    assertNull("Exit 2 should be removed",
        NodeUtil.findFirstDescendantWithType(labelBlock, Token.RETURN));
  }

  /**
   * Tests MinimizeExitPoints visit method handling a for loop.
   */
  public void testMinimizeExitPointsForLoop() {
    Node node = getTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node forNode = NodeUtil.findFirstDescendantWithType(node, Token.FOR);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(forNode);

    minimizeExitPoints.visit(null, loopCodeBlock, null);

    // Check that the break node has been moved into the loopCodeBlock.
    Node movedBreakNode = NodeUtil.findFirstDescendantWithType(loopCodeBlock,
        Token.BREAK);
    assertNotNull("Moved break node should be present", movedBreakNode);

    // Check that the return node has been removed.
    assertNull("Exit 2 should be removed",
        NodeUtil.findFirstDescendantWithType(loopCodeBlock, Token.RETURN));
  }

  /**
   * Tests MinimizeExitPoints visit method handling a do-while loop.
   */
  public void testMinimizeExitPointsDoWhileLoop() {
    Node node = getTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node doNode = NodeUtil.findFirstDescendantWithType(node, Token.DO);
    Node loopCodeBlock = NodeUtil.getLoopCodeBlock(doNode);

    minimizeExitPoints.visit(null, loopCodeBlock, null);

    // Check that the break node has been moved into the loopCodeBlock.
    Node movedBreakNode = NodeUtil.findFirstDescendantWithType(loopCodeBlock,
        Token.BREAK);
    assertNotNull("Moved break node should be present", movedBreakNode);

    // Check that the return node has been removed.
    assertNull("Exit 2 should be removed",
        NodeUtil.findFirstDescendantWithType(loopCodeBlock, Token.RETURN));

    // Check that the break node for the label has been moved into the loopCodeBlock.
    Node movedLabelBreakNode = NodeUtil.findFirstDescendantWithType(loopCodeBlock,
        Token.BREAK, "testLabel");
    assertNotNull("Moved label break node should be present", movedLabelBreakNode);
  }

  /**
   * Tests MinimizeExitPoints visit method handling a function.
   */
  public void testMinimizeExitPointsFunction() {
    Node node = getTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node functionNode = NodeUtil.findFirstDescendantWithType(node, Token.FUNCTION);

    minimizeExitPoints.visit(null, functionNode, null);

    // Check that the break node has been moved into the functionNode.
    Node movedBreakNode = NodeUtil.findFirstDescendantWithType(functionNode,
        Token.BREAK);
    assertNotNull("Moved break node should be present", movedBreakNode);

    // Check that the return node has been removed.
    assertNull("Exit 2 should be removed",
        NodeUtil.findFirstDescendantWithType(functionNode, Token.RETURN));
  }

  /**
   * Tests MinimizeExitPoints visit method handling a if statement.
   */
  public void testMinimizeExitPointsIfStatement() {
    Node node = getTestNode();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    Node ifNode = NodeUtil.findFirstDescendantWithType(node, Token.IF);
    Node trueBlock = ifNode.getFirstChild().getNext();
    Node falseBlock = trueBlock.getNext();

    minimizeExitPoints.visit(null, ifNode, null);

    // Check that the break node has been moved into the trueBlock.
    Node movedBreakNode = NodeUtil.findFirstDescendantWithType(trueBlock,
        Token.BREAK);
    assertNotNull("Moved break node should be present", movedBreakNode);

    // Check that the return node has been removed.
    assertNull("Exit 2 should be removed",
        NodeUtil.findFirstDescendantWithType(trueBlock, Token.RETURN));

    // Check that the falseBlock is empty.
    assertFalse("False block should be empty", falseBlock.hasChildren());
  }

  /**
   * Tests MinimizeExitPoints visit method handling a switch statement.
   */
  public void testMinimizeExitPointsSwitchStatement() {
    // TODO: Implement test cases for switch statements.
  }

  /**
   * Helper class to create a Compiler instance.
   */
  private static class Compiler extends AbstractCompiler {
    // Empty implementation.
  }
}