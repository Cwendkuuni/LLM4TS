package com.google.javascript.jscomp.jsonml.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;

import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = createNode(Token.IF, new int[]{}, new int[]{0, 1});
    assertFalse(NodeUtil.isForIn(invalidNode));

    Node edgeCaseNode1 = createNode(Token.FOR, new int[]{Token.IDENTIFIER, Token.ASSIGN, Token.IDENTIFIER}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(edgeCaseNode1));

    Node edgeCaseNode2 = createNode(Token.FOR, new int[]{Token.IDENTIFIER, Token.ASSIGN, Token.IDENTIFIER, Token.SEMICOLON}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(edgeCaseNode2));

    Node edgeCaseNode3 = createNode(Token.FOR, new int[]{Token.IDENTIFIER, Token.ASSIGN, Token.IDENTIFIER, Token.IN, Token.IDENTIFIER}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(edgeCaseNode3));

    Node exceptionNode = new Node();
    assertThrows(IllegalArgumentException.class, () -> NodeUtil.isForIn(exceptionNode));

    Node nullNode = null;
    assertThrows(NullPointerException.class, () -> NodeUtil.isForIn(nullNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.IF, new int[]{}, new int[]{0, 1});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(Token.ADD, new int[]{}, new int[]{0, 1, 2});
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  // Add other test cases here...

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(Token.FUNCTION, new int[]{Token.IDENTIFIER}, new int[]{0, 1});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = createNode(Token.FUNCTION, new int[]{Token.IDENTIFIER, Token.LEFT_PAREN}, new int[]{0, 1});
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createNode(Token.FUNCTION, new int[]{Token.IDENTIFIER}, new int[]{0, 1, 0});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = createNode(Token.FUNCTION, new int[]{Token.IDENTIFIER}, new int[]{0, 1, 1});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}