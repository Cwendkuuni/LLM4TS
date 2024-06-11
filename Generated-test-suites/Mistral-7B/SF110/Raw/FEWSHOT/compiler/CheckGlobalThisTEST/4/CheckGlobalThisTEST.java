package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.NodeTraversal.CallbackType.SYNC;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = NodeTraversal.create(SYNC);

    Node functionNode = Node.newFunctionNode(null, null, null, null);
    Node constructorNode = Node.newFunctionNode(null, null, null, "@constructor");
    Node prototypeMethodNode = Node.newFunctionNode(null, null, null, null, null, "@prototype");
    Node thisAnnotationNode = Node.newFunctionNode(null, null, null, null, null, "@this");
    Node prototypeObjectNode = Node.newObjectLiteralNode(null, null, null);
    Node prototypeGetterNode = Node.newGetPropNode("prototype", null);
    Node prototypeSetterNode = Node.newSetPropNode("prototype", null, null);

    // Don't traverse functions that are constructors or have the @constructor or @this annotation.
    assertTrue(checker.shouldTraverse(traversal, null, functionNode));
    assertFalse(checker.shouldTraverse(traversal, null, constructorNode));
    assertFalse(checker.shouldTraverse(traversal, null, thisAnnotationNode));

    // Don't traverse functions unless they would normally be able to have a @this annotation associated with them.
    assertTrue(checker.shouldTraverse(traversal, null, functionNode));
    assertTrue(checker.shouldTraverse(traversal, null, prototypeMethodNode));
    assertTrue(checker.shouldTraverse(traversal, null, thisAnnotationNode));
    assertTrue(checker.shouldTraverse(traversal, null, prototypeObjectNode));
    assertTrue(checker.shouldTraverse(traversal, null, prototypeGetterNode));
    assertTrue(checker.shouldTraverse(traversal, null, prototypeSetterNode));

    // Don't traverse functions that are getting lent to a prototype property or subproperty.
    Node prototypeParentNode = Node.newObjectLiteralNode(null, null, null);
    Node prototypeLenderNode = Node.newGetPropNode("prototype", null);
    Node prototypeLeeNode = Node.newObjectLiteralNode(null, null, null);
    Node prototypeLeePropertyNode = Node.newStringNode("prototypeProperty");

    Node prototypeLenderGetterNode = Node.newGetPropNode("prototype", null);
    Node prototypeLenderLenderNode = Node.newGetPropNode("prototype", null);
    Node prototypeLenderLeeNode = Node.newObjectLiteralNode(null, null, null);
    Node prototypeLenderLeePropertyNode = Node.newStringNode("prototypePropertyOfPrototypeProperty");

    Node prototypeLeeAssigneeNode = Node.newObjectLiteralNode(null, null, null);
    Node prototypeLeeAssignmentNode = Node.newSetPropNode(prototypeLeeAssigneeNode, null, prototypeLeePropertyNode);

    Node prototypeParentAssignmentNode = Node.newSetPropNode(prototypeParentNode, prototypeLenderNode, prototypeObjectNode);

    Node prototypeLenderGetterAssignmentNode =
        Node.newSetPropNode(prototypeLenderGetterNode, null, prototypeLeeAssignmentNode);
    Node prototypeLenderLenderGetterAssignmentNode =
        Node.newSetPropNode(prototypeLenderLenderNode, null, prototypeLenderGetterAssignmentNode);

    Node prototypeLenderLeeAssignmentNode = Node.newSetPropNode(prototypeLenderLeeNode, null, prototypeLeePropertyNode);

    assertTrue(checker.shouldTraverse(traversal, null, functionNode));
    assertTrue(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLenderNode));
    assertFalse(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLenderLenderNode));
    assertFalse(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLeeNode));
    assertFalse(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLeeAssigneeNode));
    assertFalse(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLenderLenderLeeNode));
    assertFalse(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLenderLenderLeePropertyNode));
    assertTrue(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLenderGetterNode));
    assertTrue(checker.shouldTraverse(traversal, prototypeParentNode, prototypeLenderLenderGetterNode));
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = NodeTraversal.create(SYNC);

    Node functionNode = Node.newFunctionNode(null, null, null, null);
    Node thisNode = Node.newThisNode();

    when(compiler.report(Mockito.any())).thenReturn(null);

    checker.visit(traversal, null, functionNode);
    checker.visit(traversal, null, thisNode);

    Node assignLeftNode = Node.newIdentNode("assignLeft");
    Node assignRightNode = Node.newThisNode();
    Node assignmentNode = Node.newAssignNode(assignLeftNode, assignRightNode);

    checker.visit(traversal, null, assignmentNode);

    Node getNode = Node.newGetPropNode("get", null);
    Node thisPropertyNode = Node.newIdentNode("thisProperty");
    Node getResultNode = Node.newGetPropNode("getResult", null);

    Node getAssignmentNode = Node.newSetPropNode(getNode, null, thisPropertyNode);
    Node getAssignmentResultNode = Node.newSetPropNode(getResultNode, null, getAssignmentNode);

    checker.visit(traversal, getNode, getAssignmentNode);
    checker.visit(traversal, getNode, getAssignmentResultNode);

    assertTrue(compiler.reportCallCount(0));

    Node functionWithThisNode = Node.newFunctionNode(null, null, null, null);
    Node thisInFunctionNode = Node.newThisNode();
    Node functionAssignmentNode = Node.newAssignNode(functionWithThisNode, thisInFunctionNode);

    checker.visit(traversal, null, functionWithThisNode);
    checker.visit(traversal, null, thisInFunctionNode);
    checker.visit(traversal, null, functionAssignmentNode);

    assertTrue(compiler.reportCallCount(1));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Node functionNode = Node.newFunctionNode(null, null, null, null);
    Node constructorNode = Node.newFunctionNode(null, null, null, "@constructor");
    Node prototypeMethodNode = Node.newFunctionNode(null, null, null, null, null, "@prototype");
    Node thisAnnotationNode = Node.newFunctionNode(null, null, null, null, null, "@this");

    JSDocInfo jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc);

    jsDoc = checker.getFunctionJsDocInfo(constructorNode);
    assertNull(jsDoc);

    jsDoc = checker.getFunctionJsDocInfo(prototypeMethodNode);
    assertNull(jsDoc);

    jsDoc = checker.getFunctionJsDocInfo(thisAnnotationNode);
    assertNull(jsDoc);
  }
}