package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static com.google.javascript.jscomp.NodeTraversal.CallbackType.CALLBACK_TRAVERSE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private Compiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node functionNode = Node.newFunction("foo");
    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(functionNode);

    assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSE), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorNode = Node.newFunction("constructor");
    Node jsDocInfo = Node.newJSDocInfo("constructor");
    constructorNode.setJSDocInfo(jsDocInfo);

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(constructorNode);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSE), constructorNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node functionNode = Node.newFunction("foo", new Node[0]);
    Node thisAnnotation = Node.newIdentifier("this");
    functionNode.addChildToStatementList(thisAnnotation);

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(functionNode);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSE), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    Node functionNode = Node.newFunction("foo");

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(functionNode);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSE), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node prototypeNode = Node.newObjectLit();
    prototypeNode.setProperty("prototype", Node.newProperty("prototype", Node.newIdentifier("prototype")));

    Node functionNode = Node.newFunction("foo", new Node[0]);
    Node prototypeProperty = Node.newIdentifier("prototype");
    prototypeNode.addChildToProperties(prototypeProperty);
    prototypeProperty.addChildToStatementList(functionNode);

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(prototypeNode);

    assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSE), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node leftSide = Node.newIdentifier("a");
    Node rightSide = Node.newThis();

    Node assignmentNode = Node.newAssign(Token.ASSIGN, leftSide, rightSide);

    assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSE), assignmentNode, null));
  }

  @Test
  public void testShouldTraverseThisWithPropertyAccess() {
    Node thisNode = Node.newThis();
    Node propertyAccessNode = Node.newGetProp(Node.newIdentifier("prototype"), thisNode);

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(propertyAccessNode);

    assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(compiler, CALLBACK_TRAVERSED), thisNode, parentNode));
  }

  @Test
  public void testShouldNotReportThisInConstructorFunction() {
    Node constructorNode = Node.newFunction("constructor");
    Node thisNode = Node.newThis();
    constructorNode.addChildToStatementList(thisNode);

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(constructorNode);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, CALLBACK_TRAVERSE);

    checkGlobalThis.visit(traversal, constructorNode, parentNode);

    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldReportThisInGlobalFunction() {
    Node functionNode = Node.newFunction("foo");
    Node thisNode = Node.newThis();
    functionNode.addChildToStatementList(thisNode);

    Node parentNode = Node.newBlock();
    parentNode.addChildToStatementList(functionNode);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, CALLBACK_TRAVERSE);

    checkGlobalThis.visit(traversal, functionNode, parentNode);

    assertTrue(traversal.getDiagnostics().size() > 0);
    assertTrue(traversal.getDiagnostics().get(0).equals(compiler.makeError(GLOBAL_THIS, functionNode)));
  }
}