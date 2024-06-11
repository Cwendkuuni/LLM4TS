// Final NodeUtilTest.java file with all test cases combined

package com.example.junit4;

import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node(); // assuming a Node instance can be created
    assertTrue(NodeUtil.isForIn(node)); // add more test cases as necessary
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = new Node();
    assertTrue(NodeUtil.isStatement(node)); // add more test cases as necessary
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(); // assume a valid function declaration Node can be created
    assertTrue(NodeUtil.isFunctionDeclaration(node)); // add more test cases as necessary
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(); // assume a valid hoisted function declaration Node can be created
    Node scriptNode = new Node(); // assume a valid script Node can be created
    Node parentNode = new Node(); // assume a valid parent Node for scriptNode
    scriptNode.setParent(parentNode);
    parentNode.setParent(functionNode); // set the parent node's parent to functionNode
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode)); // add more test cases as necessary
  }

  @org.junit.Test
  public void testIsForInNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isForIn(node));
    // Add more test cases for null or invalid nodes as necessary
  }

  @org.junit.Test
  public void testIsForInInvalidNodeType() {
    Node node = new Node();
    node.setType(Token.IDENTIFIER);
    assertFalse(NodeUtil.isForIn(node));
    // Add more test cases for invalid node types as necessary
  }

  @org.junit.Test
  public void testIsStatementNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isStatement(node));
    // Add more test cases for null or invalid nodes as necessary
  }

  @org.junit.Test
  public void testIsStatementInvalidParentType() {
    Node node = new Node();
    Node parent = new Node();
    parent.setType(Token.IF);
    node.setParent(parent);
    assertFalse(NodeUtil.isStatement(node));
    // Add more test cases for invalid parent types as necessary
  }

  @org.junit.Test
  public void testIsFunctionDeclarationNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isFunctionDeclaration(node));
    // Add more test cases for null or invalid nodes as necessary
  }

  @org.junit.Test
  public void testIsFunctionDeclarationInvalidNodeType() {
    Node node = new Node();
    node.setType(Token.NUMBER_LITERAL);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
    // Add more test cases for invalid node types as necessary
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    // Add more test cases for null or invalid nodes as necessary
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationInvalidParentType() {
    Node functionNode = new Node();
    Node parent = new Node();
    parent.setType(Token.IF);
    functionNode.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    // Add more test cases for invalid parent types as necessary
  }

  @org.junit.Test
  public void testIsFunctionDeclarationAndIsStatementFalse() {
    Node node = new Node();
    node.setType(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(node)); // function expression
    node.setParent(new Node());
    assertFalse(NodeUtil.isStatement(node)); // not in a statement context
    // Add more test cases with the same logic as necessary
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationNoParent() {
    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);
    assertThrows(IllegalArgumentException.class, () -> NodeUtil.isHoistedFunctionDeclaration(functionNode));
    // Add more test cases with the same logic as necessary
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationNoParentStatement() {
    Node functionNode = new Node();
    Node scriptNode = new Node();
    scriptNode.setParent(functionNode);
    functionNode.setType(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    // Add more test cases with the same logic as necessary
  }

  // Add more test cases for edge cases and exceptional scenarios as necessary
}