package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[0]);
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.IF, new int[0]);
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node statementNode = new Node(Token.IF, new int[0]);
 assertTrue(NodeUtil.isStatement(statementNode));

 Node expressionNode = new Node(Token.FUNCTION, new int[0]);
 assertFalse(NodeUtil.isStatement(expressionNode));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node functionDeclarationNode = new Node(Token.FUNCTION, new int[0]);
 functionDeclarationNode.setProperty("id", new Node(Token.IDENTIFIER, new int[0], "myFunction"));
 assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

 Node functionExpressionNode = new Node(Token.FUNCTION, new int[0]);
 assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[0]);
 hoistedFunctionDeclarationNode.setProperty("id", new Node(Token.IDENTIFIER, new int[0], "myFunction"));
 Node scriptNode = new Node(Token.SCRIPT, new int[0]);
 scriptNode.addChild(hoistedFunctionDeclarationNode);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

 Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[0]);
 notHoistedFunctionDeclarationNode.setProperty("id", new Node(Token.IDENTIFIER, new int[0], "myFunction"));
 Node labelNode = new Node(Token.LABEL, new int[0]);
 labelNode.addChild(notHoistedFunctionDeclarationNode);
 Node functionNode = new Node(Token.FUNCTION, new int[0]);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
 }
}