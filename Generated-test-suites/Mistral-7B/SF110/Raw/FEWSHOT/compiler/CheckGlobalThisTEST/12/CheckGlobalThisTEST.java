package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node root = new Node(Token.BLOCK);
    Node function1 = new Node(Token.FUNCTION);
    Node function2 = new Node(Token.FUNCTION);
    Node assign1 = new Node(Token.ASSIGN);
    Node assign2 = new Node(Token.ASSIGN);
    Node prototypeProperty = new Node(Token.GET_PROP, new Node("prototype"));

    function1.addChild(new Node("functionName"));
    function1.addChild(new Node("{}"));

    function2.addChild(new Node("functionName"));
    function2.addChild(new Node("return this;"));

    assign1.addChild(new Node("this"));
    assign2.addChild(new Node("Object.prototype.property"));

    assign1.addChild(function1);
    assign2.addChild(function2);

    Node object = new Node(Token.OBJECT_LITERAL, new Node[0]);
    Node prototype = new Node(Token.OBJECT_LITERAL, new Node[0]);
    prototype.addChild(new Node("prototype", null, object));

    root.addChild(assign1);
    root.addChild(assign2);
    root.addChild(prototype);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(function1);

    boolean result = checker.shouldTraverse(traversal, null, root);
    assertTrue(result);

    when(traversal.getCurrentNode()).thenReturn(function2);
    result = checker.shouldTraverse(traversal, null, root);
    assertTrue(result);

    when(traversal.getCurrentNode()).thenReturn(prototype);
    result = checker.shouldTraverse(traversal, null, root);
    assertFalse(result);

    when(traversal.getCurrentNode()).thenReturn(assign1);
    result = checker.shouldTraverse(traversal, null, root);
    assertTrue(result);

    when(traversal.getCurrentNode()).thenReturn(assign2);
    result = checker.shouldTraverse(traversal, null, root);
    assertTrue(result);

    when(traversal.getCurrentNode()).thenReturn(prototypeProperty);
    result = checker.shouldTraverse(traversal, null, root);
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node root = new Node(Token.BLOCK);
    Node thisKeyword = new Node(Token.THIS);
    Node propertyAccess = new Node(Token.GET_PROP, new Node("prototype"));

    thisKeyword.addChild(propertyAccess);
    root.addChild(thisKeyword);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(NodeUtil.getParent(thisKeyword), DiagnosticType.warning(null, null))).thenReturn(null);

    checker.visit(traversal, null, root);
    assertTrue(compiler.reportCalled());
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node function = new Node(Token.FUNCTION);
    Node constructorAnnotation = new Node(Token.ANNOTATION, new Node("@constructor"));
    Node thisAnnotation = new Node(Token.ANNOTATION, new Node("@this"));
    Node interfaceAnnotation = new Node(Token.ANNOTATION, new Node("@interface"));
    Node overrideAnnotation = new Node(Token.ANNOTATION, new Node("@override"));

    Node parent = new Node(Token.ASSIGN);
    Node assignLhs = new Node(Token.NAME, "assignLhs");
    Node assignRhs = function;
    parent.addChildren(assignLhs, assignRhs);

    Node var = new Node(Token.VAR, new Node("varName"));
    Node varFunction = new Node(Token.FUNCTION);
    Node varFunctionName = new Node("functionName");
    varFunction.addChild(varFunctionName);
    varFunction.addChild(new Node("return this;"));
    var.addChild(varFunction);

    Node functionWithJsDoc = new Node(Token.FUNCTION, null, null, null);
    functionWithJsDoc.addChild(constructorAnnotation);
    functionWithJsDoc.addChild(thisAnnotation);
    functionWithJsDoc.addChild(interfaceAnnotation);
    functionWithJsDoc.addChild(overrideAnnotation);
    functionWithJsDoc.addChild(varFunctionName);
    functionWithJsDoc.addChild(varFunction);

    function.addChildren(functionWithJsDoc);
    parent.addChild(parent);
    root.addChild(parent);

    JSDocInfo result = checker.getFunctionJsDocInfo(function);
    assertNull(result);

    result = checker.getFunctionJsDocInfo(functionWithJsDoc);
    assertNotNull(result);
  }
}