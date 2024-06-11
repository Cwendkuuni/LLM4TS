package com.google.javascript.jscomp;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizeExitPoints;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    minimizeExitPoints = new MinimizeExitPoints(compiler);
    nodeTraversal = NodeTraversal.create(compiler);
  }

  @Test
  public void testMinimizeExitPoints_label() {
    Node root = NodeUtil.simpleFunctionNode("test", new Node[0]);
    Node labelNode = NodeUtil.labelNode("label");
    Node exitNode = NodeUtil.breakNode("label");
    Node body = NodeUtil.blockNode(new Node[] { labelNode, exitNode });
    root.addChildToBack(body);

    minimizeExitPoints.process(null, root);
    NodeUtil.traverse(nodeTraversal, root);

    Node actual = findNodeByTypeAndName(root, Token.LABEL, "label");
    assertSame("Expected label node to be removed", null, actual);

    Node expectedExitNode = findNodeByType(root, Token.BREAK);
    assertSame("Expected break node to be moved to label", labelNode, expectedExitNode.getParent());
  }

  @Test
  public void testMinimizeExitPoints_forLoop() {
    Node root = NodeUtil.simpleFunctionNode("test", new Node[0]);
    Node forNode = NodeUtil.forInitNode("i", null);
    Node condition = NodeUtil.literalNode(5);
    Node increment = NodeUtil.assignNode(NodeUtil.identifierNode("i"), NodeUtil.literalNode(1));
    Node loopBody = NodeUtil.blockNode(new Node[0]);
    Node breakNode = NodeUtil.breakNode(null);
    Node loop = NodeUtil.forLoopNode(forNode, condition, increment, loopBody);
    root.addChildToBack(loop);

    minimizeExitPoints.process(null, root);
    NodeUtil.traverse(nodeTraversal, root);

    Node actual = findNodeByTypeAndName(root, Token.FOR, null);
    assertSame("Expected for loop node to be removed", null, actual);

    Node expectedExitNode = findNodeByType(root, Token.BREAK);
    assertSame("Expected break node to be moved to for loop", loop, expectedExitNode.getParent());
  }

  @Test
  public void testMinimizeExitPoints_doWhileLoop() {
    Node root = NodeUtil.simpleFunctionNode("test", new Node[0]);
    Node doNode = NodeUtil.doNode();
    Node condition = NodeUtil.literalNode(true);
    Node loopBody = NodeUtil.blockNode(new Node[0]);
    Node breakNode = NodeUtil.breakNode(null);
    Node loop = NodeUtil.doWhileLoopNode(doNode, condition, loopBody);
    root.addChildToBack(loop);

    minimizeExitPoints.process(null, root);
    NodeUtil.traverse(nodeTraversal, root);

    Node actual = findNodeByTypeAndName(root, Token.DO, null);
    assertSame("Expected do loop node to be removed", null, actual);

    Node expectedExitNode = findNodeByType(root, Token.BREAK);
    assertSame("Expected break node to be moved to do loop", loop, expectedExitNode.getParent());
  }

  @Test
  public void testMinimizeExitPoints_functionReturn() {
    Node root = NodeUtil.simpleFunctionNode("test", new Node[0]);
    Node functionNode = NodeUtil.functionNode("function", new Node[0], null, null, NodeUtil.blockNode(new Node[0]));
    Node returnNode = NodeUtil.returnNode(NodeUtil.literalNode(42));
    functionNode.getLastChild().addChildToBack(returnNode);
    root.addChildToBack(functionNode);

    minimizeExitPoints.process(null, root);
    NodeUtil.traverse(nodeTraversal, root);

    Node actual = findNodeByTypeAndName(root, Token.FUNCTION, "function");
    assertSame("Expected function node to be removed", null, actual);

    Node expectedReturnNode = findNodeByType(root, Token.RETURN);
    assertSame("Expected return node to be moved to function", root, expectedReturnNode.getParent());
  }

  private Node findNodeByType(Node node, int type) {
    for (Node child : node.children()) {
      if (child.getType() == type) {
        return child;
      }
      Node result = findNodeByType(child, type);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findNodeByTypeAndName(Node node, int type, String name) {
    for (Node child : node.children()) {
      if (child.getType() == type && child.getFirstChild() != null && child.getFirstChild().getString().equals(name)) {
        return child;
      }
      Node result = findNodeByTypeAndName(child, type, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}