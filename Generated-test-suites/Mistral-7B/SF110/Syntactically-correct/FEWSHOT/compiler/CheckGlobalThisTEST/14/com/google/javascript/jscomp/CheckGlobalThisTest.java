package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.JSDocInfo.FunctionAnnotationType.INTERFACE;
import static com.google.javascript.rhino.JSDocInfo.FunctionAnnotationType.OVERRIDE;
import static com.google.javascript.rhino.JSDocInfo.FunctionAnnotationType.CONSTRUCTOR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    checkGlobalThis = new CheckGlobalThis(compiler);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testShouldTraverseFunctionNoAnnotation() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);
    when(functionParent.getType()).thenReturn(Token.NAME);
    when(functionParent.getJSDocInfo()).thenReturn(null);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getParent()).thenReturn(Node.newNull());

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    JSDocInfo jsDoc = new JSDocInfo("functionName", CONSTRUCTOR);
    when(functionParent.getJSDocInfo()).thenReturn(jsDoc);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    JSDocInfo jsDoc = new JSDocInfo("functionName", null, "@this", null, null, null);
    when(functionParent.getJSDocInfo()).thenReturn(jsDoc);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    when(functionParent.getType()).thenReturn(Token.ASSIGN);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseIfNotFunction() {
    Node nonFunctionNode = Node.newNumber(1);
    Node functionParent = Node.newName("parentName");

    when(functionParent.getType()).thenReturn(Token.NAME);
    when(functionParent.getParent()).thenReturn(Node.newBlock());

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, nonFunctionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseIfFunctionIsConstructor() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    JSDocInfo jsDoc = new JSDocInfo("functionName", CONSTRUCTOR);
    when(functionParent.getJSDocInfo()).thenReturn(jsDoc);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseIfFunctionIsAnnotatedWithInterface() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    JSDocInfo jsDoc = new JSDocInfo("functionName", INTERFACE);
    when(functionParent.getJSDocInfo()).thenReturn(jsDoc);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseIfFunctionIsAnnotatedWithOverride() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();

    JSDocInfo jsDoc = new JSDocInfo("functionName", OVERRIDE);
    when(functionParent.getJSDocInfo()).thenReturn(jsDoc);
    when(functionParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(functionParent);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeProperty() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();
    Node prototypeProperty = Node.newName("prototypeProperty");
    Node prototypePropertyParent = Node.newGetProp(prototypeProperty, "prototype");

    when(functionParent.getParent()).thenReturn(prototypePropertyParent);
    when(prototypePropertyParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(prototypePropertyParent);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubProperty() {
    Node functionNode = Node.newFunction("functionName");
    Node functionParent = Node.newName("parentName");
    Node functionGrandparent = Node.newBlock();
    Node prototypeProperty = Node.newName("prototypeProperty");
    Node prototypePropertyParent = Node.newGetProp(prototypeProperty, "prototype");
    Node prototypeSubProperty = Node.newName("prototypeSubProperty");
    Node prototypeSubPropertyParent = Node.newGetProp(prototypeSubProperty, "");

    when(functionParent.getParent()).thenReturn(prototypeSubPropertyParent);
    when(prototypeSubPropertyParent.getParent()).thenReturn(prototypePropertyParent);
    when(prototypePropertyParent.getParent()).thenReturn(functionGrandparent);
    when(functionGrandparent.getType()).thenReturn(Token.SCRIPT);

    when(traversal.getCurrentNode()).thenReturn(prototypeSubPropertyParent);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node assignmentParent = Node.newName("parentName");

    when(assignmentParent.getType()).thenReturn(Token.NAME);

    when(traversal.getCurrentNode()).thenReturn(assignmentParent);

    verify(compiler).report(isA(ErrorReporter.Error.class));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignmentNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node assignmentParent = Node.newName("parentName");
    Node assignmentLhs = Node.newName("a");

    when(assignmentParent.getType()).thenReturn(Token.NAME);
    when(assignmentParent.getFirstChild()).thenReturn(assignmentLhs);

    when(traversal.getCurrentNode()).thenReturn(assignmentLhs);

    assertFalse(checkGlobalThis.shouldReportThis(assignmentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfGetProperty() {
    Node getPropertyNode = Node.newGetProp(Node.newName("a"), Node.newThis());
    Node getPropertyParent = Node.newName("parentName");

    when(getPropertyParent.getType()).thenReturn(Token.NAME);

    when(traversal.getCurrentNode()).thenReturn(getPropertyParent);

    verify(compiler).report(isA(ErrorReporter.Error.class));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfGetProperty() {
    Node getPropertyNode = Node.newGetProp(Node.newName("a"), Node.newThis());
    Node getPropertyParent = Node.newName("parentName");
    Node getPropertyLhs = Node.newName("a");

    when(getPropertyParent.getType()).thenReturn(Token.NAME);
    when(getPropertyParent.getFirstChild()).thenReturn(getPropertyLhs);

    when(traversal.getCurrentNode()).thenReturn(getPropertyLhs);

    assertFalse(checkGlobalThis.shouldReportThis(getPropertyNode));
  }
}