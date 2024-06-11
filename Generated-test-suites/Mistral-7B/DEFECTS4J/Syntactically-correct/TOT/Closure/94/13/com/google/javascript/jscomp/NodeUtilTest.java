package com.google.javascript.jscomp.jsonml.test;

import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENT, "var"));
    forNode.setChild(1, new Node(Token.IDENT, "i"));
    forNode.setChild(2, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "array"), new Node(Token.LITERAL, 0)));

    assertTrue(isForIn(forNode));

    Node notForNode = new Node(Token.IF);
    assertFalse(isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5));
    Node expressionNode = new Node(Token.PLUS, null, new Node(Token.NUMBER, 3), new Node(Token.NUMBER, 4));

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "myFunction"), new NodeList(), new Node(Token.BLOCK, null, new NodeList()));
    Node functionExpressionNode = new Node(Token.FUNCTION, new NodeList(), new Node(Token.IDENT, "myFunction"), null, new NodeList());

    assertTrue(isFunctionDeclaration(functionDeclarationNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "myFunction"), new NodeList(), new Node(Token.BLOCK, null, new NodeList()));
    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "myFunction"), new NodeList(), new Node(Token.BLOCK, null, new NodeList()));
    Node hoistedFunctionDeclarationParentNode = new Node(Token.SCRIPT, null, new NodeList(), null);
    Node notHoistedFunctionDeclarationParentNode = new Node(Token.BLOCK, null, new NodeList(), null);

    hoistedFunctionDeclarationNode.setParent(hoistedFunctionDeclarationParentNode);
    notHoistedFunctionDeclarationNode.setParent(notHoistedFunctionDeclarationParentNode);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}