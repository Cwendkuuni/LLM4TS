package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*; import com.google.javascript.rhino.Node; import org.junit.Test;

public class NodeUtilTest {
  @Test  public void testIsForIn() { Node forNode = new Node(Token.FOR); forNode.setChild(0, new Node(Token.IDENT, "for")); forNode.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.NAME, "i"), new Node(Token.COMMA), new Node(Token.LITERAL, 0))); forNode.setChild(2, new Node(Token.BLOCK, null, new Node(Token.STATEMENT, null, new Node(Token.ASSIGN, null, new Node(Token.NAME, "array"), new Node(Token.LITERAL, "[1,2,3]"))))); assertTrue(NodeUtil.isForIn(forNode));

Node notForNode = new Node(Token.IF); assertFalse(NodeUtil.isForIn(notForNode)); }

@Test public void testIsStatement() { Node statementNode = new Node(Token.PRINT, null, new Node(Token.STRING, "Hello, World!")); assertTrue(NodeUtil.isStatement(statementNode));

Node expressionNode = new Node(Token.PLUS, null, new Node(Token.NUMBER, 2), new Node(Token.NUMBER, 3)); assertFalse(NodeUtil.isStatement(expressionNode)); }

@Test public void testIsFunctionDeclaration() { Node functionNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK, null)); assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

Node functionExpressionNode = new Node(Token.FUNCTION, null, null, new Node(Token.BLOCK, null)); assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode)); }

@Test public void testIsHoistedFunctionDeclaration() { Node hoistedFunctionNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK, null)); Node scriptNode = new Node(Token.SCRIPT, null, new Node(Token.STATEMENT, null, hoistedFunctionNode)); assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

Node functionNodeInFunction = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK, null)); Node functionNodeInScript = new Node(Token.SCRIPT, null, new Node(Token.STATEMENT, null, functionNodeInFunction)); assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction)); }
}