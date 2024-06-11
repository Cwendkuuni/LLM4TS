package com.google.javascript.jscomp.jsonml.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node forNode = new Node(); // Create a new Node instance for testing
 forNode.setType(Token.FOR);
 forNode.setChildCount(3);
 assertTrue(NodeUtil.isForIn(forNode));
 }

 @Test
 public void testIsStatement() {
 Node statementNode = new Node(); // Create a new Node instance for testing
 statementNode.setParent(new Node()); // Set a parent for the statementNode
 assertTrue(NodeUtil.isStatement(statementNode));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node functionNode = new Node(); // Create a new Node instance for testing
 functionNode.setType(Token.FUNCTION);
 functionNode.setParent(new Node()); // Set a parent for the functionNode
 assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node functionNode = new Node(); // Create a new Node instance for testing
 Node scriptNode = new Node(); // Create a new Node instance for testing
 functionNode.setType(Token.FUNCTION);
 functionNode.setParent(scriptNode); // Set the functionNode as a child of the scriptNode
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
 }

 @Test
 public void testIncorrectlyStructuredForIN() {
 Node forNode = new Node(); // Create a new Node instance for testing
 forNode.setType(Token.FOR);
 forNode.setChildCount(2); // Set an incorrect child count
 assertFalse(NodeUtil.isForIn(forNode));
 }

 @Test
 public void testFunctionExpression() {
 Node functionNode = new Node(); // Create a new Node instance for testing
 Node expressionNode = new Node(); // Create a new Node instance for testing
 functionNode.setType(Token.FUNCTION);
 expressionNode.setType(Token.ASSIGN);
 functionNode.setParent(expressionNode); // Set the functionNode as a child of the expressionNode
 assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
 }

}