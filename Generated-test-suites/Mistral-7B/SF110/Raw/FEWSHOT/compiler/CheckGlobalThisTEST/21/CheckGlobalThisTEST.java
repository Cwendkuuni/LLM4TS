package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckGlobalThis;
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
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = NodeTraversal.create(null, null, checker);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    root.addChild(functionNode);

    Node globalContext = Node.newBlock("globalContext");
    globalContext.setType(Token.SCRIPT);
    root.addChild(globalContext);

    Node functionInGlobalContext = Node.newFunction("functionInGlobalContext", null, null, null, null);
    globalContext.addChild(functionInGlobalContext);

    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), functionNode, globalContext)).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    Node objectNode = Node.newObjectLiteral("objectName", null);
    root.addChild(functionNode);
    root.addChild(objectNode);

    Node functionInObject = Node.newFunction("functionInObject", null, null, null, null);
    Node propertyNode = Node.newName("propertyName");
    objectNode.addChildToChildren(propertyNode);
    propertyNode.addChild(functionInObject);

    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), functionNode, objectNode)).isFalse();
  }

  @Test
  public void testShouldTraverseAssignments() {
    Node root = Node.newBlock("root");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    root.addChild(assignNode);

    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    root.addChild(functionNode);

    Node leftSide = assignNode.getFirstChild();
    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), functionNode, leftSide)).isTrue();
  }

  @Test
  public void testShouldNotTraversePrototypePropertyAccess() {
    Node root = Node.newBlock("root");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    root.addChild(assignNode);

    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    Node prototypeNode = Node.newName("prototype");
    Node getNode = Node.getGetProp(Node.newName("a"), prototypeNode);
    root.addChild(functionNode);
    functionNode.addChildToChildren(getNode);

    Node leftSide = getNode.getLastChild();
    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), functionNode, leftSide)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithConstructor() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, Node.newArray());
    functionNode.setJSDocInfo(new JSDocInfo(null, new JSDocInfo.Tag("constructor", null), null));
    root.addChild(functionNode);

    Node leftSide = functionNode.getFirstChild();
    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), null, leftSide)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node root = Node.newBlock("root");
    Node functionNode =
        Node.newFunction("functionName", null, null, null, Node.newArray(new JSDocInfo.Tag("this", "Foo")));
    root.addChild(functionNode);

    Node leftSide = functionNode.getFirstChild();
    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), null, leftSide)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutLogicalPlaceForThisAnnotation() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    root.addChild(functionNode);

    Node leftSide = functionNode.getFirstChild();
    assertThat(checker.shouldTraverse(NodeTraversal.create(null, null, checker), null, leftSide)).isFalse();
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    Node thisNode = Node.newThis();
    functionNode.addChildToChildren(thisNode);
    root.addChild(functionNode);

    Node traversalResult = traversal.traverse(functionNode);
    Node thisNodeInTraversal = traversalResult.getFirstChild();

    checker.visit(NodeTraversal.create(compiler, null, null), thisNodeInTraversal, functionNode);

    when(compiler.report(NodeTraversal.create(compiler, null, null).makeError(thisNodeInTraversal, GLOBAL_THIS)))
        .thenCallRealMethod();

    traversal.traverse(functionNode);
  }

  @Test
  public void testShouldNotReportThisInPrototypeMethod() {
    Node root = Node.newBlock("root");
    Node objectNode = Node.newObjectLiteral("objectName", null);
    Node prototypeNode = Node.newName("prototype");
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    Node prototypeFunctionNode = Node.newFunction("prototypeFunctionName", null, null, null, null);
    Node thisNode = Node.newThis();
    Node prototypeFunctionThisNode = Node.newThis();

    objectNode.addChildToChildren(prototypeNode);
    prototypeNode.addChildToChildren(functionNode);
    functionNode.addChildToChildren(prototypeFunctionNode);
    prototypeFunctionNode.addChildToChildren(thisNode);
    prototypeFunctionNode.addChildToChildren(prototypeFunctionThisNode);

    Node traversalResult = traversal.traverse(functionNode);
    Node thisNodeInTraversal = traversalResult.getFirstChild();

    checker.visit(NodeTraversal.create(compiler, null, null), thisNodeInTraversal, functionNode);

    assertNull(compiler.report(NodeTraversal.create(compiler, null, null).makeError(thisNodeInTraversal, GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportThisInConstructor() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, Node.newArray());
    Node thisNode = Node.newThis();
    functionNode.setJSDocInfo(new JSDocInfo(null, new JSDocInfo.Tag("constructor", null), null));
    functionNode.addChildToChildren(thisNode);
    root.addChild(functionNode);

    Node traversalResult = traversal.traverse(functionNode);
    Node thisNodeInTraversal = traversalResult.getFirstChild();

    checker.visit(NodeTraversal.create(compiler, null, null), thisNodeInTraversal, functionNode);

    assertNull(compiler.report(NodeTraversal.create(compiler, null, null).makeError(thisNodeInTraversal, GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportThisInFunctionAnnotatedWithThis() {
    Node root = Node.newBlock("root");
    Node functionNode =
        Node.newFunction("functionName", null, null, null, Node.newArray(new JSDocInfo.Tag("this", "Foo")));
    Node thisNode = Node.newThis();
    functionNode.addChildToChildren(thisNode);
    root.addChild(functionNode);

    Node traversalResult = traversal.traverse(functionNode);
    Node thisNodeInTraversal = traversalResult.getFirstChild();

    checker.visit(NodeTraversal.create(compiler, null, null), thisNodeInTraversal, functionNode);

    assertNull(compiler.report(NodeTraversal.create(compiler, null, null).makeError(thisNodeInTraversal, GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportThisInFunctionWithoutLogicalPlaceForThisAnnotation() {
    Node root = Node.newBlock("root");
    Node functionNode = Node.newFunction("functionName", null, null, null, null);
    Node thisNode = Node.newThis();
    root.addChild(functionNode);

    Node traversalResult = traversal.traverse(functionNode);
    Node thisNodeInTraversal = traversalResult.getFirstChild();

    checker.visit(NodeTraversal.create(compiler, null, null), thisNodeInTraversal, functionNode);

    assertNull(compiler.report(NodeTraversal.create(compiler, null, null).makeError(thisNodeInTraversal, GLOBAL_THIS)));
  }
}