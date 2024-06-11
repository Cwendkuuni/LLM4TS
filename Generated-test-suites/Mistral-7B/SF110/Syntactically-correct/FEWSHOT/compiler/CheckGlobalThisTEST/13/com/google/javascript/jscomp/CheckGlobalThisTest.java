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
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private static final String FUNCTION_ANNOTATED_WITH_THIS =
      "function @this Foo() {}";
  private static final String FUNCTION_ANNOTATED_WITH_CONSTRUCTOR =
      "function constructor Foo() {}";
  private static final String PROTOTYPE_METHOD = "Foo.prototype.bar = function() {};";
  private static final String FUNCTION_ANNOTATED_WITH_OVERRIDE =
      "class Foo { @override foo() {} }";
  private static final String FUNCTION_IN_OBJECT_LITERAL =
      "var obj = { foo: function() {} };";
  private static final String FUNCTION_GETTING_LOANED_TO_PROTOTYPE =
      "var obj = { prototype: { bar: function() {} } };";
  private static final String FUNCTION_IS_ASSIGN_LEFT_SIDE =
      "var a = function() {}; a = this;";
  private static final String FUNCTION_IS_ASSIGN_RIGHT_SIDE =
      "var a = {}; a.b = function() {}; a.b = this;";
  private static final String FUNCTION_IS_GET_PROPERTY =
      "var a = {}; get a() { return this; }";

  private NodeFactory nodeFactory;
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo", null, null, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createName("foo");
    Node scriptsNode = nodeFactory.createScript(FUNCTION_ANNOTATED_WITH_THIS);

    when(compiler.getSourceFile()).thenReturn(scriptsNode);
    when(traversal.getCurrentNode()).thenReturn(parentNode);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo", NodeFactory.getConstructorKeyword(), null, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createName("foo");

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThisType() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo", null, NodeFactory.getThisKeyword(), null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createName("foo");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo", null, null, NodeFactory.getOverrideKeyword());
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createName("foo");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraversePrototypeMethod() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo.prototype.bar", null, null, null);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createName("Foo");

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionInObjectLiteral() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo", null, null, null);
    Node objectNode = nodeFactory.createObjectLiteral();
    Node assignNode = nodeFactory.createAssign(NodeFactory.getAssignOperator(), null, objectNode, functionNode);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createName("obj");

    when(traversal.getCurrentNode()).thenReturn(parentNode);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionGettingLentToPrototype() {
    Node functionNode = nodeFactory.createFunctionDeclaration("", "Foo.prototype.bar", null, null, null);
    Node objectNode = nodeFactory.createObjectLiteral();
    Node assignNode = nodeFactory.createAssign(NodeFactory.getAssignOperator(), NodeFactory.getIdentifier("prototype"), objectNode);
    Node scriptsNode = nodeFactory.createScript(FUNCTION_GETTING_LOANED_TO_PROTOTYPE);
    Node parentNode = nodeFactory.createName("obj");

    when(compiler.getSourceFile()).thenReturn(scriptsNode);
    when(traversal.getCurrentNode()).thenReturn(parentNode);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseAssignLeftSide() {
    Node assignNode = nodeFactory.createAssign(NodeFactory.getAssignOperator(), NodeFactory.getIdentifier("a"), NodeFactory.getThisKeyword());
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createAssign(NodeFactory.getAssignOperator(), null, NodeFactory.getIdentifier("a"), null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseAssignRightSide() {
    Node assignNode = nodeFactory.createAssign(NodeFactory.getAssignOperator(), null, NodeFactory.getIdentifier("a"), NodeFactory.getThisKeyword());
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createScript(FUNCTION_IS_ASSIGN_RIGHT_SIDE);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseGetProperty() {
    Node getNode = nodeFactory.createGetProperty(NodeFactory.getIdentifier("a"));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createScript(FUNCTION_IS_GET_PROPERTY);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, getNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnAssignLeftSide() {
    Node assignNode = nodeFactory.createAssign(NodeFactory.getAssignOperator(), NodeFactory.getIdentifier("a"), NodeFactory.getThisKeyword());
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createScript(FUNCTION_IS_ASSIGN_LEFT_SIDE);

    assertTrue(checkGlobalThis.shouldReportThis(assignNode));
  }

  @Test
  public void testShouldReportThisOnGetProperty() {
    Node getNode = nodeFactory.createGetProperty(NodeFactory.getIdentifier("a"));
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = nodeFactory.createScript(FUNCTION_IS_GET_PROPERTY);

    Node nodeToReport = getNode;
    assertTrue(checkGlobalThis.shouldReportThis(nodeToReport));

    nodeToReport = getNode.getParent();
    assertTrue(checkGlobalThis.shouldReportThis(nodeToReport));
  }
}