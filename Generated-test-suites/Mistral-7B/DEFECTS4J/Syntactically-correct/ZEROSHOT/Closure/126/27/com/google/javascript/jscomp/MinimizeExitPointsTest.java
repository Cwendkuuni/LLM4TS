package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for MinimizeExitPoints class
 */
public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testLabelExit() {
    Node root = Node.newBlock();
    Node labelNode = Node.newLabel("label");
    Node breakNode = Node.newBreak("label");

    root.addChildToBack(labelNode);
    root.addChildToBack(Node.newStatement(breakNode));

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    assertEquals(0, root.getLastChildIndex(breakNode));
  }

  @Test
  public void testForLoopExit() {
    Node root = Node.newBlock();
    Node forNode = Node.newFor();
    Node body = Node.newBlock();
    Node breakNode = Node.newBreak();

    root.addChildToBack(forNode);
    forNode.setBody(body);
    body.addChildToBack(breakNode);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    assertEquals(0, body.getLastChildIndex(breakNode));
  }

  @Test
  public void testIfElseExit() {
    Node root = Node.newBlock();
    Node ifNode = Node.newIf();
    Node trueBlock = Node.newBlock();
    Node falseBlock = Node.newBlock();
    Node exitNodeTrue = Node.newReturn(null);
    Node exitNodeFalse = Node.newStatement(Node.newBreak());

    root.addChildToBack(ifNode);
    ifNode.setElse(falseBlock);
    ifNode.setThen(trueBlock);
    trueBlock.addChildToBack(exitNodeTrue);
    falseBlock.addChildToBack(exitNodeFalse);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    assertEquals(0, trueBlock.getLastChildIndex(exitNodeTrue));
    assertEquals(-1, falseBlock.getChildCount());
  }

  @Test
  public void testIfExit() {
    Node root = Node.newBlock();
    Node ifNode = Node.newIf();
    Node exitNode = Node.newReturn(null);

    root.addChildToBack(ifNode);
    ifNode.setElse(Node.newBlock());
    ifNode.setThen(Node.newBlock().setType(Token.EMPTY));
    ifNode.addChildToBack(exitNode);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    assertEquals(0, root.getLastChildIndex(exitNode));
  }

  @Test
  public void testTryCatchExit() {
    Node root = Node.newBlock();
    Node tryNode = Node.newTry();
    Node tryBlock = Node.newBlock();
    Node catchNode = Node.newCatch();
    Node exitNode = Node.newReturn(null);

    root.addChildToBack(tryNode);
    tryNode.setTryDirectly(true);
    tryNode.setTryBlock(tryBlock);
    tryBlock.addChildToBack(exitNode);
    catchNode.setException(Node.newName("Error"));
    tryNode.setHandler(catchNode);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    assertEquals(0, tryBlock.getLastChildIndex(exitNode));
  }

  @Test
  public void testTryCatchFinallyExit() {
    Node root = Node.newBlock();
    Node tryNode = Node.newTry();
    Node tryBlock = Node.newBlock();
    Node catchBlock = Node.newBlock();
    Node finallyBlock = Node.newBlock();
    Node exitNode = Node.newReturn(null);

    root.addChildToBack(tryNode);
    tryNode.setTryDirectly(true);
    tryNode.setTryBlock(tryBlock);
    tryBlock.addChildToBack(exitNode);
    tryNode.setHandler(Node.newBlock().setType(Token.EMPTY));
    tryNode.setFinally(finallyBlock);
    finallyBlock.addChildToBack(exitNode);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    assertEquals(0, finallyBlock.getLastChildIndex(exitNode));
  }

  @Test(expected = AssertionError.class)
  public void testNonBlockNode() {
    Node root = Node.newInt(1);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, minimizeExitPoints);
    nodeTraversal.traverse();

    throw new AssertionError();
  }
}