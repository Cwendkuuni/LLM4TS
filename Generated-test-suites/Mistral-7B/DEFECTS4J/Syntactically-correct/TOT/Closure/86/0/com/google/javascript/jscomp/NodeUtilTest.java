package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Tests for the NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    Node nodeNotForIn = Node.newExpression("var x = 5;");

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression("var x = 5;");
    Node nodeExpression = Node.newExpression("5 + 3"); ;
    Node nodeScript = Node.newScript();
    Node nodeBlock = Node.newBlock(null, null);
    Node nodeLabel = Node.newLabel("label1");

    nodeLabel.setParent(nodeScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertFalse(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("funcName", new Node[] { Node.newParameter("paramName", Node.newUndefined()) }, Node.newBlock(null, null));
    Node nodeFunctionExpression = Node.newExpression("function funcName(paramName) { }");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction("funcName", new Node[] { Node.newParameter("paramName", Node.newUndefined()) }, Node.newBlock(null, null));
    Node nodeFunctionDeclaration = Node.newFunction("funcName", new Node[] { Node.newParameter("paramName", Node.newUndefined()) }, Node.newBlock(null, null));
    Node nodeScript = Node.newScript();
    Node nodeFunction = Node.newFunction("funcName", new Node[] { Node.newParameter("paramName", Node.newUndefined()) }, Node.newBlock(null, null));
    nodeFunctionDeclaration.setParent(nodeScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }
}