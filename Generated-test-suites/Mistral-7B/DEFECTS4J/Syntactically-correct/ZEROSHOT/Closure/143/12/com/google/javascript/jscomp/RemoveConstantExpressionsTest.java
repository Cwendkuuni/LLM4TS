package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantExpressions.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test file for RemoveConstantExpressions class.
 */
public class RemoveConstantExpressionsTest {

  /**
   * Test method for RemoveConstantExpressions class.
   */
  @org.junit.Test
  public void testRemoveConstantExpressions() {
    AbstractCompiler compiler = new Compiler();
    RemoveConstantExpressions removeConstantExpressions =
        new RemoveConstantExpressions(compiler);

    Node rootNode = createTestNode();
    removeConstantExpressions.process(null, rootNode);

    List<Node> sideEffectNodes = List.of(
        createNode(Token.IDENT, "foo"),
        createNode(Token.IDENT, "bar")
    );

    Node expectedRootNode = createTestNodeWithSideEffectNodes(sideEffectNodes);

    compareNodes(rootNode, expectedRootNode);
  }

  /**
   * Helper method to create test node with given side effect nodes.
   */
  private Node createTestNodeWithSideEffectNodes(List<Node> sideEffectNodes) {
    Node testNode = createNode(Token.EXPR_STATEMENT, null);
    testNode.addChildren(Lists.newArrayList(
        createNode(Token.PLUS, null),
        createNode(Token.IDENT, "one"),
        createNode(Token.LPAREN, null),
        createNode(Token.IDENT, "foo"),
        createNode(Token.RPAREN, null),
        createNode(Token.PLUS, null),
        createNode(Token.LPAREN, null),
        createNode(Token.IDENT, "bar"),
        createNode(Token.RPAREN, null)
    ));

    for (Node node : sideEffectNodes) {
      testNode.addChild(node);
    }

    return testNode;
  }

  /**
   * Helper method to compare two nodes for equality.
   */
  private void compareNodes(Node node1, Node node2) {
    if (node1.getClass() != node2.getClass()) {
      throw new AssertionError(
          "Nodes have different classes: "
              + node1.getClass().getCanonicalName()
              + " vs " + node2.getClass().getCanonicalName());
    }

    if (node1.getType() != node2.getType()) {
      throw new AssertionError(
          "Nodes have different types: "
              + node1.getType() + " vs " + node2.getType());
    }

    if (node1.getFirstChild() != null && node2.getFirstChild() != null) {
      NodeTraversal.traverse(null, node1.getFirstChild(),
          new NodeComparator());
      NodeTraversal.traverse(null, node2.getFirstChild(),
          new NodeComparator());
    }
  }

  /**
   * Helper method to create test node.
   */
  private Node createTestNode() {
    return createNode(Token.EXPR_STATEMENT, null);
  }

  /**
   * Helper method to create node with given token.
   */
  private Node createNode(int tokenType, Node parent) {
    Node node = new Node(tokenType, parent);
    return node;
  }

  /**
   * Helper class to compare nodes during the test.
   */
  private static class NodeComparator extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal traversal, Node node1, Node node2) {
      if (node1 != node2) {
        assertFalse("Nodes are not equal: "
            + node1
            + " vs "
            + node2);
      }
    }
  }
}