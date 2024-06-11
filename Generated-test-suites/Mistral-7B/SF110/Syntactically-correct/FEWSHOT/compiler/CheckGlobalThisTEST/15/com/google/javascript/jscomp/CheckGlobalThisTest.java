package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverse() {
    Node node = Node.newNode(Token.FUNCTION);
    Node parent = Node.newNode(Token.SCRIPT);
    Node grandparent = Node.newNode(Token.BLOCK);

    // Traverse a function in a script.
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, parent)).isTrue();

    // Don't traverse a constructor.
    Node constructorNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.NAME, "constructor"), null);
    JSDocInfo constructorJsDoc = Node.newNode(Token.JSDOC_INFO, null, null, null, new String[]{"constructor"});
    constructorNode.setJSDocInfo(constructorJsDoc);
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, parent)).isFalse();

    // Don't traverse a function with @this annotation.
    Node thisAnnotatedNode = Node.newNode(Token.FUNCTION, null, null);
    JSDocInfo thisAnnotatedJsDoc = Node.newNode(Token.JSDOC_INFO, null, null, null, new String[]{"@this"});
    thisAnnotatedNode.setJSDocInfo(thisAnnotatedJsDoc);
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, parent)).isFalse();

    // Traverse a function in a block.
    Node functionInBlockNode = Node.newNode(Token.FUNCTION, null, null);
    Node functionInBlockParent = Node.newNode(Token.BLOCK);
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, functionInBlockParent)).isTrue();

    // Don't traverse a function lent to a prototype.
    Node prototypeLendNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.NAME, "prototype"), node);
    Node prototypeLendParent = Node.newNode(Token.NAME, "Object", null);
    JSDocInfo prototypeLendDoc = Node.newNode(Token.JSDOC_INFO, null, null, null, null);
    prototypeLendNode.setJSDocInfo(prototypeLendDoc);
    Node prototypeLendGrandparent = Node.newNode(Token.BLOCK);
    Node prototypeLendGreatGrandparent = Node.newNode(Token.NAME, "Object", null);
    prototypeLendParent.setParent(prototypeLendGrandparent);
    prototypeLendParent.setParent(prototypeLendGreatGrandparent);
    prototypeLendGreatGrandparent.setType(Token.NAME);
    prototypeLendGreatGrandparent.setString("Object");
    Node prototypeLendProto = NodeUtil.getProto(prototypeLendParent);
    when(prototypeLendProto.getString()).thenReturn("prototype");
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, parent)).isFalse();

    // Traverse an assignment to the left side.
    Node leftAssignmentNode = Node.newNode(Token.ASSIGN, null, node);
    Node leftAssignmentParent = Node.newNode(Token.SCRIPT);
    Node leftAssignmentGrandparent = Node.newNode(Token.BLOCK);
    leftAssignmentParent.setParent(leftAssignmentGrandparent);
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, leftAssignmentParent)).isTrue();

    // Don't traverse an assignment to a prototype property or subproperty.
    Node prototypeAssignmentNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.NAME, "prototype"), Node.newNode(Token.NAME, "x"));
    Node prototypeAssignmentParent = Node.newNode(Token.NAME, "Object", null);
    Node prototypeAssignmentGrandparent = Node.newNode(Token.BLOCK);
    prototypeAssignmentParent.setParent(prototypeAssignmentGrandparent);
    Node prototypeAssignmentProto = NodeUtil.getProto(prototypeAssignmentParent);
    when(prototypeAssignmentProto.getString()).thenReturn("prototype");
    Node prototypeAssignmentProtoProto = NodeUtil.getProto(prototypeAssignmentProto);
    when(prototypeAssignmentProtoProto.getString()).thenReturn("prototype");
    assertThat(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, parent)).isFalse();
  }

  @Test
  public void testVisit() {
    Node node = Node.newNode(Token.THIS);
    Node parent = Node.newNode(Token.ASSIGN);
    Node visitParent = Node.newNode(Token.SCRIPT);

    // Report a THIS on the left side of an assignment.
    checker.visit(NodeTraversal.emptyTraversal(), node, parent);
    checker.compiler.reportCallCount.verify(times(1));

    // Don't report a THIS with a property access.
    Node propertyAccessParent = Node.newNode(Token.GETPROP, null, node);
    Node propertyAccessParentParent = Node.newNode(Token.SCRIPT);
    Node propertyAccessParentParentParent = Node.newNode(Token.BLOCK);
    propertyAccessParentParent.setParent(propertyAccessParentParentParent);
    propertyAccessParentParentParent.setType(Token.BLOCK);
    propertyAccessParentParentParent.setParent(Node.newNode(Token.SCRIPT));
    propertyAccessParent.setParent(propertyAccessParentParent);
    checker.visit(NodeTraversal.emptyTraversal(), node, propertyAccessParentParent);
    checker.compiler.reportCallCount.verifyNoMoreInteractions();
  }
}