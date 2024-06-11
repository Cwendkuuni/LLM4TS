package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizeExitPoints;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = new NodeFactory();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExits_label() {
    Node root = NodeUtil.createLabel("test");
    root.addChild(NodeUtil.createIf(NodeUtil.createBinaryOp(
        NodeUtil.createIdentifier("x"), Token.EQUALS, NodeUtil.createLiteral(1))));
    root.addChild(NodeUtil.createBreak("test"));
    root.addChild(NodeUtil.createStatement("foo()"));

    minimizeExitPoints.process(null, root);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(NodeUtil.createStatement("")),
        NodeUtil.getLastChild(root));
    NodeUtil.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement("foo()"),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        null, NodeUtil.getLastChild(NodeUtil.getLastChild(NodeUtil.getLastChild(root))));
    NodeUtil.assertNodeEquals(
        null, NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }

  @Test
  public void testMinimizeExits_loop() {
    Node root = NodeUtil.createFor(
        NodeUtil.createLabel("test"),
        NodeUtil.createIf(NodeUtil.createBinaryOp(
            NodeUtil.createIdentifier("x"), Token.LESSEQUAL, NodeUtil.createLiteral(1))),
        NodeUtil.createStatement("x++"),
        NodeUtil.createStatement(""),
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")));

    minimizeExitPoints.process(null, root);

    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        null, NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }

  @Test
  public void testMinimizeExits_if() {
    Node root = NodeUtil.createBlock(
        NodeUtil.createLabel("test"),
        NodeUtil.createIf(NodeUtil.createBinaryOp(
            NodeUtil.createIdentifier("x"), Token.LESSEQUAL, NodeUtil.createLiteral(1))),
        NodeUtil.createStatement("x++"),
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")));

    minimizeExitPoints.process(null, root);

    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement("x++"),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }

  @Test
  public void testMinimizeExits_if_else() {
    Node root = NodeUtil.createBlock(
        NodeUtil.createLabel("test"),
        NodeUtil.createIf(NodeUtil.createBinaryOp(
            NodeUtil.createIdentifier("x"), Token.LESSEQUAL, NodeUtil.createLiteral(1))),
        NodeUtil.createStatement("x++"),
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")),
        NodeUtil.createElseBranch(
            NodeUtil.createBlock(
                NodeUtil.createStatement("y++"))));

    minimizeExitPoints.process(null, root);

    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement("y++"),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        null, NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }

  @Test
  public void testMinimizeExits_function() {
    Node root = NodeUtil.createBlock(
        NodeUtil.createLabel("test"),
        NodeUtil.createFunction(
            NodeUtil.createIdentifier("foo"),
            NodeUtil.createParameters(""),
            NodeUtil.createBlock(
                NodeUtil.createIf(NodeUtil.createLiteral(false)),
                NodeUtil.createStatement("x++"),
                NodeUtil.createBlock(
                    NodeUtil.createStatement(""),
                    NodeUtil.createStatement("")))));

    minimizeExitPoints.process(null, root);

    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        null, NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }

  @Test
  public void testMinimizeExits_try_catch() {
    Node root = NodeUtil.createBlock(
        NodeUtil.createLabel("test"),
        NodeUtil.createTry(
            NodeUtil.createBlock(
                NodeUtil.createIf(NodeUtil.createLiteral(false)),
                NodeUtil.createStatement("x++")),
            NodeUtil.createBlock(),
            NodeUtil.createBlock(
                NodeUtil.createStatement(""),
                NodeUtil.createStatement(""))));

    minimizeExitPoints.process(null, root);

    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement(""),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement("x++"),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }

  @Test
  public void testMinimizeExits_try_catch_finally() {
    Node root = NodeUtil.createBlock(
        NodeUtil.createLabel("test"),
        NodeUtil.createTry(
            NodeUtil.createBlock(
                NodeUtil.createIf(NodeUtil.createLiteral(false)),
                NodeUtil.createStatement("x++")),
            NodeUtil.createBlock(),
            NodeUtil.createBlock(
                NodeUtil.createStatement("y++"))));

    minimizeExitPoints.process(null, root);

    NodeUtils.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement("y++"),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));

    NodeUtil.assertNodeEquals(
        NodeUtil.createBlock(
            NodeUtil.createStatement("x++"),
            NodeUtil.createStatement("")),
        NodeUtil.getLastChild(NodeUtil.getLastChild(root)));
  }
}