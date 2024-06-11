package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, null, createNode(Token.IDENT, "i"), null, createNode(Token.IDENT, "array"));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.IF, null, null, createNode(Token.IDENT, "condition"), null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeEmptyNodeUtil = createNode(Token.SCRIPT, null, null, null, null);
    Node nodeForInWithEmptyNodeUtil = createNode(Token.FOR, null, createNode(Token.IDENT, "i"), null, createNode(Token.IDENT, "array"));
    nodeEmptyNodeUtil.addChild(nodeForInWithEmptyNodeUtil);
    assertFalse(NodeUtil.isForIn(nodeForInWithEmptyNodeUtil));

    Node nodeNullNode = null;
    assertFalse(NodeUtil.isForIn(nodeNullNode));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.ASSIGN, null, createNode(Token.IDENT, "x"), null, createNode(Token.INT_LITERAL, 10));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode(Token.ASSIGN, null, null, createNode(Token.IDENT, "x"), createNode(Token.INT_LITERAL, 10));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeEmptyNodeUtil = createNode(Token.SCRIPT, null, null, null, null);
    Node nodeStatementWithEmptyNodeUtil = createNode(Token.ASSIGN, null, createNode(Token.IDENT, "x"), null, createNode(Token.INT_LITERAL, 10));
    nodeEmptyNodeUtil.addChild(nodeStatementWithEmptyNodeUtil);
    assertFalse(NodeUtil.isStatement(nodeStatementWithEmptyNodeUtil));

    Node nodeNullNode = null;
    assertFalse(NodeUtil.isStatement(nodeNullNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(Token.FUNCTION, null, null, null, null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeEmptyNodeUtil = createNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunctionDeclarationWithEmptyNodeUtil = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), null, null, null, null);
    nodeEmptyNodeUtil.addChild(nodeFunctionDeclarationWithEmptyNodeUtil);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithEmptyNodeUtil));

    Node nodeNullNode = null;
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNullNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = createNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), null, null, null, null);
    nodeScript.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = createNode(Token.FUNCTION, null, null, null, null, null, null);
    Node nodeScriptInFunction = createNode(Token.SCRIPT, null, null, null, nodeFunctionDeclarationInFunction);
    nodeFunctionDeclarationInFunction.addChild(nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeEmptyNodeUtil = createNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunctionDeclarationWithEmptyNodeUtil = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), null, null, null, null);
    nodeEmptyNodeUtil.addChild(nodeFunctionDeclarationWithEmptyNodeUtil);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationWithEmptyNodeUtil));

    Node nodeNullNode = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNullNode));
  }
}