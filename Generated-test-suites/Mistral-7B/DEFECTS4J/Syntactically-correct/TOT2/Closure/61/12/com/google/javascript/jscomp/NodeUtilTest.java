package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {
  // Test cases for isForIn method
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENT, "var"));
    forNode.setChild(1, new Node(Token.IDENT, "i"));
    forNode.setChild(2, new Node(Token.LEFT_BRACE));

    Node listNode = new Node(Token.LIST);
    listNode.setChild(0, forNode);

    Node nodeToTest = new Node(listNode);

    assertTrue(NodeUtil.isForIn(nodeToTest));
  }

  @Test
  public void testIsForInNotForIn() {
    Node notForInNode = new Node(Token.IF);

    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  // Test cases for isStatement method
  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, new Node(Token.STRING, "Hello World"));

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsStatementPartOfExpression() {
    Node expressionNode = new Node(Token.ADD, new Node(Token.NUMBER, 5), new Node(Token.NUMBER, 3));

    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsStatementNullNode() {
    Node nullNode = null;

    try {
      NodeUtil.isStatement(nullNode);
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // Test cases for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.RIGHT_BRACE));

    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsFunctionExpression() {
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.RIGHT_BRACE));

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsFunctionDeclarationWithArguments() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.LIST, new Node(Token.IDENT, "x"), new Node(Token.IDENT, "y")));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  // Test cases for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "bar"), new Node(Token.RIGHT_BRACE));
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInFunction() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "test"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NUMBER, 42))));

    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.RIGHT_BRACE));
    hoistedFunctionDeclarationNode.setParent(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInConditional() {
    Node conditionalNode = new Node(Token.IF, new Node(Token.IDENT, "x"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NUMBER, 42))));

    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.RIGHT_BRACE));
    hoistedFunctionDeclarationNode.setParent(conditionalNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  // Test cases for other methods
  @Test
  public void testIsFunctionExpression() {
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "foo"), new Node(Token.RIGHT_BRACE));

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsStatementWithEmptyBlock() {
    Node statementNode = new Node(Token.BLOCK, new Node[0]);

    assertTrue(NodeUtil.isStatement(statementNode));
  }
}