package com.google.javascript.jscomp.CheckGlobalThis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticMessageCategory;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(Compiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
    when(compiler.getTraversalResult()).thenReturn(traversal);
  }

  @Test
  public void testSafeFunction() {
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testConstructorFunction() {
    Node constructorNode = Node.newFunction("Test", new Node[0], new Node[0]);
    constructorNode.setJSDocInfo(new JSDocInfo("Test", new String[]{"@constructor"}));

    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));
  }

  @Test
  public void testAnnotatedFunction() {
    Node annotatedNode = Node.newFunction("test", new Node[0], new Node[0]);
    annotatedNode.setJSDocInfo(new JSDocInfo("test", new String[]{"@this"}));

    assertFalse(checker.shouldTraverse(traversal, annotatedNode, null));
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testAssignmentToThis() {
    Node assignNode = Node.newAssign(Token.NAME, Node.thisNode(), Node.newNull());
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);
    Node parentNode = Node.newAssign(Token.NAME, Node.newName("a"), assignNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testAssignmentToPropertyOfPrototype() {
    Node assignNode = Node.newAssign(Token.NAME, Node.newGetProp(Node.thisNode(), "prototype"), Node.newNull());
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);
    Node parentNode = Node.newAssign(Token.NAME, Node.newName("a"), Node.newGetProp(Node.thisNode(), "prototype"));

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssign(Token.NAME, Node.newGetProp(Node.thisNode(), "prototype"), Node.newName("x"));
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);
    Node parentNode = Node.newAssign(Token.NAME, Node.newGetProp(Node.thisNode(), "prototype"), Node.newName("x"));

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testAssignmentToNonPrototypeProperty() {
    Node assignNode = Node.newAssign(Token.NAME, Node.newGetProp(Node.thisNode(), "x"), Node.newName("y"));
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);
    Node parentNode = Node.newName("this");

    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testReportGlobalThis() {
    Node globalThisNode = Node.thisNode();
    Node parentNode = Node.newAssign(Token.NAME, Node.newName("a"), globalThisNode);

    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);

    when(compiler.report).thenReturn(mock(DiagnosticMessageCategory.class));
    checker.visit(traversal, functionNode, parentNode);

    NodeUtil.assertErrorMessage(compiler, functionNode, DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testWithinPrototypeMethod() {
    Node functionNode = Node.newFunction("test", new Node[0], new Node[0]);
    Node prototype = Node.newObject("Object");
    Node prototypeFunction = Node.newFunction("prototypeTest", new Node[0], new Node[0]);
    prototypeFunction.setJSDocInfo(new JSDocInfo("prototypeTest", new String[]{"@this"}));
    prototype.setProperty("prototype", prototypeFunction);
    Node constructorNode = Node.newFunction("Object", new Node[0], new Node[0]);
    constructorNode.setJSDocInfo(new JSDocInfo("Object", new String[]{"@constructor"}));
    prototypeFunction.setParent(constructorNode.getFirstChild());

    Node parentNode = Node.newGetProp(prototype, "prototype");

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }
}