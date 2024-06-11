
package com.google.javascript.jscomp.jsonml;

import org.junit.Assert;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR, Node.newNode(Token.IDENT, "i"), Node.newNode(Token.KEYWORD, "in"), Node.newNode(Token.IDENT, "arr"));
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.PRINT, Node.newNode(Token.STRING, "Hello World!"));
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42)))));
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.NUMBER, 42))));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getChild(0)));
  }

  @Test
  public void testIsForInNullNode() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsForInEmptyNode() {
    Node emptyNode = Node.newNode(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(emptyNode));
  }

  @Test
  public void testIsStatementNullNode() {
    assertFalse(NodeUtil.isStatement(null));
  }

  @Test
  public void testIsStatementEmptyNode() {
    Node emptyNode = Node.newNode(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(emptyNode));
  }

  @Test
  public void testIsFunctionDeclarationInvalidNodeType() {
    Node invalidNode = Node.newNode(Token.VAR, Node.newNode(Token.IDENT, "myVar"));
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInvalidNodeType() {
    Node invalidNode = Node.newNode(Token.VAR, Node.newNode(Token.IDENT, "myVar"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
  }
}