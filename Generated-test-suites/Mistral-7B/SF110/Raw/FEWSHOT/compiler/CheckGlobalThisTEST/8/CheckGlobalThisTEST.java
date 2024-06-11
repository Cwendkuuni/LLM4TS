package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private Node nodeUtil;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    nodeUtil = mock(NodeUtil.class);
    when(compiler.getUtilities()).thenReturn(nodeUtil);
  }

  @Test
  public void testShouldTraverse_function() {
    Node functionNode = Node.newFunction("foo");
    Node parentNode = Node.newName("bar");
    Node grandParentNode = Node.newBlock();

    when(nodeUtil.isObjectLitKey(parentNode)).thenReturn(false);
    when(nodeUtil.isGet(parentNode)).thenReturn(false);
    when(nodeUtil.isGetProp(parentNode, "prototype")).thenReturn(false);

    Node functionParentNode = Node.newName("baz");

    when(nodeUtil.isAssign(parentNode)).thenReturn(false);
    when(nodeUtil.isGet(functionParentNode)).thenReturn(false);

    when(nodeUtil.isObjectLitKey(functionParentNode)).thenReturn(false);
    when(nodeUtil.isGetProp(functionParentNode, "prototype")).thenReturn(false);

    when(nodeUtil.isAssign(grandParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(grandParentNode)).thenReturn(true);
    when(nodeUtil.isName(grandParentNode)).thenReturn(false);

    Node functionGrandParentNode = Node.newScript();

    when(nodeUtil.isAssign(functionGrandParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(functionGrandParentNode)).thenReturn(false);
    when(nodeUtil.isName(functionGrandParentNode)).thenReturn(true);

    when(functionNode.getParent()).thenReturn(functionParentNode);
    when(functionParentNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(grandParentNode);
    when(grandParentNode.getParent()).thenReturn(functionGrandParentNode);

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_prototypeMethod() {
    Node constructorNode = Node.newFunction("constructor");
    Node prototypeNode = Node.newName("prototype");
    Node functionNode = Node.newFunction("foo", "x");

    Node parentNode = Node.newBlock();
    Node grandParentNode = Node.newName("Bar");
    Node constructorParentNode = Node.newBlock();

    when(nodeUtil.isObjectLitKey(prototypeNode)).thenReturn(false);
    when(nodeUtil.isGet(prototypeNode)).thenReturn(false);
    when(nodeUtil.isGetProp(prototypeNode, "prototype")).thenReturn(false);

    when(nodeUtil.isAssign(parentNode)).thenReturn(false);
    when(nodeUtil.isBlock(parentNode)).thenReturn(true);
    when(nodeUtil.isName(parentNode)).thenReturn(false);

    when(nodeUtil.isAssign(grandParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(grandParentNode)).thenReturn(false);
    when(nodeUtil.isName(grandParentNode)).thenReturn(true);

    when(nodeUtil.isAssign(constructorNode)).thenReturn(true);
    when(nodeUtil.isBlock(constructorNode)).thenReturn(false);
    when(nodeUtil.isName(constructorNode)).thenReturn(false);

    when(nodeUtil.isAssign(constructorParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(constructorParentNode)).thenReturn(false);
    when(nodeUtil.isName(constructorParentNode)).thenReturn(false);

    when(nodeUtil.isGetProp(prototypeNode, "constructor")).thenReturn(constructorNode);
    when(nodeUtil.isAssign(functionNode, "prototype")).thenReturn(prototypeNode);

    when(functionNode.getParent()).thenReturn(constructorParentNode);
    when(constructorParentNode.getParent()).thenReturn(constructorNode);
    when(constructorNode.getParent()).thenReturn(constructorParentNode);
    when(constructorParentNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(grandParentNode);
    when(grandParentNode.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_constructor() {
    Node constructorNode = Node.newFunction("constructor");
    Node functionNode = Node.newFunction("foo", "x");

    Node parentNode = Node.newBlock();
    Node grandParentNode = Node.newName("Bar");

    when(nodeUtil.isAssign(parentNode)).thenReturn(false);
    when(nodeUtil.isBlock(parentNode)).thenReturn(true);
    when(nodeUtil.isName(parentNode)).thenReturn(false);

    when(nodeUtil.isAssign(grandParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(grandParentNode)).thenReturn(false);
    when(nodeUtil.isName(grandParentNode)).thenReturn(true);

    when(functionNode.getParent()).thenReturn(null);
    when(constructorNode.getParent()).thenReturn(parentNode);
    when(nodeUtil.isConstructor(constructorNode)).thenReturn(true);

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_thisAnnotatedFunction() {
    Node constructorNode = Node.newFunction("constructor");
    Node functionNode = Node.newFunction("foo", "@this x");

    Node parentNode = Node.newBlock();
    Node grandParentNode = Node.newName("Bar");

    when(nodeUtil.isAssign(parentNode)).thenReturn(false);
    when(nodeUtil.isBlock(parentNode)).thenReturn(true);
    when(nodeUtil.isName(parentNode)).thenReturn(false);

    when(nodeUtil.isAssign(grandParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(grandParentNode)).thenReturn(false);
    when(nodeUtil.isName(grandParentNode)).thenReturn(true);

    when(functionNode.getParent()).thenReturn(null);
    when(constructorNode.getParent()).thenReturn(parentNode);
    when(nodeUtil.isConstructor(constructorNode)).thenReturn(true);
    when(constructorNode.getJSDocInfo().hasThisType()).thenReturn(true);

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_noLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("foo");

    Node parentNode = Node.newBlock();
    Node grandParentNode = Node.newName("Bar");

    when(nodeUtil.isAssign(parentNode)).thenReturn(false);
    when(nodeUtil.isBlock(parentNode)).thenReturn(true);
    when(nodeUtil.isName(parentNode)).thenReturn(false);

    when(nodeUtil.isAssign(grandParentNode)).thenReturn(false);
    when(nodeUtil.isBlock(grandParentNode)).thenReturn(false);
    when(nodeUtil.isName(grandParentNode)).thenReturn(true);

    when(functionNode.getParent()).thenReturn(null);

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_leftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("a"));
    Node leftSideNode = Node.newThis();

    Node parentNode = Node.newBlock();

    when(assignNode.getFirstChild()).thenReturn(leftSideNode);

    boolean result = checker.shouldReportThis(leftSideNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_leftSideOfPropertyAccess() {
    Node getNode = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));
    Node leftSideNode = Node.newGetProp(Node.newThis(), Node.newName("prototype"));

    Node parentNode = Node.newBlock();

    when(getNode.getLastChild()).thenReturn(Node.newName("prototype"));

    boolean result = checker.shouldReportThis(leftSideNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverse_prototypePropertyAccess() {
    Node getNode = Node.newGetProp(Node.newThis(), Node.newName("prototype"));
    Node prototypePropertyNode = Node.newName("prototype");

    Node parentNode = Node.newBlock();
    Node prototypeNode = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));

    when(getNode.getLastChild()).thenReturn(Node.newName("prototype"));
    when(prototypePropertyNode.getLastChild()).thenReturn(Node.newName("prototype"));

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), getNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverse_prototypeSubPropertyAccess() {
    Node getNode = Node.newGetProp(Node.newThis(), Node.newGetProp(Node.newName("prototype"), Node.newName("x")));
    Node prototypePropertyNode = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));

    Node parentNode = Node.newBlock();

    when(getNode.getLastChild()).thenReturn(Node.newName("x"));
    when(prototypePropertyNode.getLastChild()).thenReturn(Node.newName("prototype"));

    boolean result = checker.shouldTraverse(NodeTraversal.create(checker), getNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithNoJSDoc() {
    Node functionNode = Node.newFunction("foo");

    Node parentNode = Node.newBlock();

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertNull(jsDocInfo);
  }

  @Test
  public void testGetFunctionJsDocInfo_functionWithJSDoc() {
    Node functionNode = Node.newFunction("foo", "@this x");

    Node parentNode = Node.newBlock();

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertThat(jsDocInfo).isNotNull();
    assertThat(jsDocInfo.isInterface()).isFalse();
    assertThat(jsDocInfo.isConstructor()).isFalse();
    assertThat(jsDocInfo.isOverride()).isFalse();
    assertThat(jsDocInfo.hasThisType()).isTrue();
  }
}