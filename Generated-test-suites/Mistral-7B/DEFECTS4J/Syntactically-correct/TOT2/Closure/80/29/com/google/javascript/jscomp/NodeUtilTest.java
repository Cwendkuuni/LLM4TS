package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.FOR);
    when(node.getChildCount()).thenReturn(3);
    assertEquals(true, NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    assertEquals(true, NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(node)).thenReturn(true);
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = mock(Node.class);
    Node functionNode = mock(Node.class);
    Node scriptNode = mock(Node.class);
    when(node.getParent()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(scriptNode);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(functionNode)).thenReturn(true);
    when(scriptNode.getType()).thenReturn(Token.SCRIPT);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsForIn_WithInvalidNodeType() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.IF);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_WithNullParent() {
    Node node = mock(Node.class);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_WithNoChildren() {
    Node node = mock(Node.class);
    when(node.getChildCount()).thenReturn(0);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_WithInvalidChildCount() {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.FOR);
    when(node.getChildCount()).thenReturn(2);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsFunctionDeclaration_WithNonFunctionParent() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithEmptyScript() {
    Node node = mock(Node.class);
    Node functionNode = mock(Node.class);
    Node scriptNode = mock(Node.class);
    when(node.getParent()).thenReturn(functionNode);
    when(functionNode.getParent()).thenReturn(scriptNode);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(functionNode)).thenReturn(true);
    when(scriptNode.getType()).thenReturn(Token.EMPTY);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}