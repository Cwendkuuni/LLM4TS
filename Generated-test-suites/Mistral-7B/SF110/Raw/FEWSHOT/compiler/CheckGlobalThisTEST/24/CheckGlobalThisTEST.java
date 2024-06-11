package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal nodeTraversal;
  private Node node;
  private Node parentNode;
  private Node grandParentNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler);
    nodeTraversal = mock(NodeTraversal.class);
    node = mock(Node.class);
    parentNode = mock(Node.class);
    grandParentNode = mock(Node.class);

    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(grandParentNode);
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    when(node.getParent()).thenReturn(null);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNonGlobalContext() {
    when(node.getParent()).thenReturn(grandParentNode);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContext() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.OBJECT_LITERAL);
    when(parentNode.getFirstChild()).thenReturn(node);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(parentNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.getLendsName()).thenReturn("myObj");
    when(jsDoc.getLendsName().endsWith("prototype")).thenReturn(true);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButNotLent() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.OBJECT_LITERAL);
    when(parentNode.getFirstChild()).thenReturn(node);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(parentNode.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.getLendsName()).thenReturn(null);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButNotObjectLiteral() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild()).thenReturn(node);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButNotGetProperty() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild()).thenReturn(node);
    Node getProp = mock(Node.class);
    when(parentNode.getLastChild()).thenReturn(getProp);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButGetPropertyOfPrototype() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.GETPROP);
    when(parentNode.getString()).thenReturn("prototype");
    when(parentNode.getFirstChild()).thenReturn(node);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButGetPropertyThatIsNotPrototype() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.GETPROP);
    when(parentNode.getString()).thenReturn("myProp");
    when(parentNode.getFirstChild()).thenReturn(node);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoPrototypeContext() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.BLOCK);
    when(parentNode.getFirstChild()).thenReturn(node);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoPrototypeContextButIsConstructor() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.BLOCK);
    when(parentNode.getFirstChild()).thenReturn(node);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoPrototypeContextButHasThisType() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.BLOCK);
    when(parentNode.getFirstChild()).thenReturn(node);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.hasThisType()).thenReturn(true);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoPrototypeContextButIsOverride() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.BLOCK);
    when(parentNode.getFirstChild()).thenReturn(node);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isOverride()).thenReturn(true);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoPrototypeContextButCanHaveThisType() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild()).thenReturn(node);
    when(parentNode.getLastChild()).thenReturn(node);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButGetPropertyOfThis() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.GETPROP);
    when(parentNode.getString()).thenReturn("this");
    when(parentNode.getFirstChild()).thenReturn(node);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeContextButAssignedToThis() {
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild()).thenReturn(node);
    when(parentNode.getLastChild()).thenReturn(node);
    Node thisNode = mock(Node.class);
    when(parentNode.getParent()).thenReturn(thisNode);
    when(thisNode.isThis()).thenReturn(true);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    when(node.isThis()).thenReturn(true);
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);
    when(parentNode.getFirstChild()).thenReturn(node);
    assertTrue(checkGlobalThis.shouldReportThis(node));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    when(node.isThis()).thenReturn(true);
    when(node.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.GETPROP);
    when(parentNode.getString()).thenReturn("myProp");
    when(parentNode.getFirstChild()).thenReturn(node);
    assertTrue(checkGlobalThis.shouldReportThis(node));
  }
}