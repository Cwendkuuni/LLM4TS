package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    Node root = AbstractCompiler.createTopLevelNode("function foo() {}");
    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(null));

    // Function should be traversed.
    assertTrue(traversal.shouldTraverse(null, root, null));

    // Function declared in a block should be traversed.
    Node declaringBlock = Node.newBlock("{function foo() {}");
    Node functionNode = Node.newFunction("foo", null, null, null, null);
    Node assignNode = Node.newAssign(Node.newName("x"), functionNode);
    Node blockNode = Node.newBlock(assignNode);
    Node parentNode = Node.newBlock(Node.newStatements(declaringBlock, blockNode));
    assertTrue(traversal.shouldTraverse(null, functionNode, parentNode));

    // Function declared as part of an object literal should be traversed.
    Node objectNode = Node.newObjectLiteral();
    Node methodNode = Node.newMethod("foo", null, null, null, null);
    Object.putProperty(objectNode, "foo", methodNode);
    Node objectLiteralNode = Node.newObjectLiteral(objectNode);
    assertTrue(traversal.shouldTraverse(null, methodNode, objectLiteralNode));

    // Function declared as part of a prototype should not be traversed.
    Node prototypeNode = Node.newObjectLiteral();
    Object.putProperty(prototypeNode, "prototype", objectLiteralNode);
    Node constructorNode = Node.newFunction("Constructor", null, null, null, null);
    constructorNode.setIsConstructor(true);
    Node prototypeFunctionNode = Node.newMethod("foo", null, null, null, null);
    Object.putProperty(prototypeNode, "foo", prototypeFunctionNode);
    assertTrue(traversal.shouldTraverse(null, constructorNode, prototypeNode));
    assertFalse(traversal.shouldTraverse(null, prototypeFunctionNode, prototypeNode));

    // Function declared in a global context should be traversed.
    Node globalFunctionNode = Node.newFunction("foo", null, null, null, null);
    Node globalContextNode = Node.newScript(Node.newStatements(globalFunctionNode));
    assertTrue(traversal.shouldTraverse(null, globalFunctionNode, globalContextNode));

    // Function declared in a global context and getting lent to a prototype should not be traversed.
    Node prototypeObjectNode = Node.newObjectLiteral();
    Object.putProperty(prototypeObjectNode, "prototype", objectLiteralNode);
    Node constructorNode2 = Node.newFunction("Constructor", null, null, null, null);
    constructorNode2.setIsConstructor(true);
    Node prototypeFunctionNode2 = Node.newMethod("foo", null, null, null, null);
    Object.putProperty(prototypeObjectNode, "foo", prototypeFunctionNode2);
    Node prototypeFunctionAssignmentNode = Node.newAssign(prototypeFunctionNode2, globalFunctionNode);
    Node prototypeFunctionParentNode = Node.newBlock(Node.newStatements(prototypeObjectNode, prototypeFunctionAssignmentNode));
    assertFalse(traversal.shouldTraverse(null, globalFunctionNode, prototypeFunctionParentNode));

    // Function with an @override annotation should not be traversed.
    Node overriddenFunctionNode = Node.newFunction("foo", null, null, null, null);
    JSDocInfo overriddenFunctionJSDoc = Node.newJSDocInfo(null, "override");
    overriddenFunctionNode.setJSDocInfo(overriddenFunctionJSDoc);
    Node overriddenFunctionParentNode = Node.newBlock(Node.newStatements(overriddenFunctionNode));
    assertFalse(traversal.shouldTraverse(null, overriddenFunctionNode, overriddenFunctionParentNode));

    // Function with a @constructor annotation should not be traversed.
    Node constructorFunctionNode = Node.newFunction("Constructor", null, null, null, null);
    JSDocInfo constructorFunctionJSDoc = Node.newJSDocInfo(null, "constructor");
    constructorFunctionNode.setJSDocInfo(constructorFunctionJSDoc);
    Node constructorFunctionParentNode = Node.newBlock(Node.newStatements(constructorFunctionNode));
    assertFalse(traversal.shouldTraverse(null, constructorFunctionNode, constructorFunctionParentNode));

    // Function with a @this annotation should not be traversed.
    Node thisAnnotatedFunctionNode = Node.newFunction("foo", null, null, null, null);
    JSDocInfo thisAnnotatedFunctionJSDoc = Node.newJSDocInfo(null, "@this", null);
    thisAnnotatedFunctionNode.setJSDocInfo(thisAnnotatedFunctionJSDoc);
    Node thisAnnotatedFunctionParentNode = Node.newBlock(Node.newStatements(thisAnnotatedFunctionNode));
    assertFalse(traversal.shouldTraverse(null, thisAnnotatedFunctionNode, thisAnnotatedFunctionParentNode));
  }

  @Test
  public void testVisit() {
    Node root = AbstractCompiler.createTopLevelNode("function foo() {this;}");
    NodeTraversal traversal = new NodeTraversal(root, new CheckGlobalThis(null));
    traversal.traverse();

    // No error should be reported for a function with no occurrences of this.
    Node noThisFunctionNode = Node.newFunction("foo", null, null, null, null);
    Node noThisFunctionParentNode = Node.newBlock(Node.newStatements(noThisFunctionNode));
    NodeTraversal noThisTraversal = new NodeTraversal(noThisFunctionNode, new CheckGlobalThis(null));
    noThisTraversal.traverse();
    assertNotNull(noThisTraversal.getErrors());

    // An error should be reported for a this keyword on the left side of an assignment.
    Node assignThisNode = Node.newAssign(Node.newName("x"), Node.newThis());
    Node assignThisParentNode = Node.newBlock(Node.newStatements(assignThisNode));
    NodeTraversal assignThisTraversal = new NodeTraversal(assignThisNode, new CheckGlobalThis(null));
    assignThisTraversal.traverse();
    assertFalse(assignThisTraversal.getErrors().isEmpty());

    // An error should be reported for a this keyword on the left side of a property access.
    Node getThisNode = Node.newGetProp(Node.newName("x"), Node.newThis());
    Node getThisParentNode = Node.newBlock(Node.newStatements(getThisNode));
    NodeTraversal getThisTraversal = new NodeTraversal(getThisNode, new CheckGlobalThis(null));
    getThisTraversal.traverse();
    assertFalse(getThisTraversal.getErrors().isEmpty());

    // No error should be reported for a this keyword inside another function.
    Node nestedFunctionNode = Node.newFunction("nested", null, null, null, null);
    Node nestedFunctionParentNode = Node.newBlock(Node.newStatements(Node.newStatements(Node.newName("foo"), nestedFunctionNode)));
    Node thisNodeInsideFunction = Node.newThis();
    NodeTraversal thisInsideFunctionTraversal = new NodeTraversal(thisNodeInsideFunction, new CheckGlobalThis(null));
    thisInsideFunctionTraversal.traverse(nestedFunctionNode, null, nestedFunctionParentNode);
    assertNotNull(thisInsideFunctionTraversal.getErrors());

    // No error should be reported for a this keyword in a constructor function.
    Node constructorFunctionNode2 = Node.newFunction("Constructor", null, null, null, null);
    constructorFunctionNode2.setIsConstructor(true);
    Node thisKeywordInConstructor = Node.newThis();
    Node constructorFunctionParentNode2 = Node.newBlock(Node.newStatements(constructorFunctionNode2));
    NodeTraversal thisInConstructorTraversal = new NodeTraversal(thisKeywordInConstructor, new CheckGlobalThis(null));
    thisInConstructorTraversal.traverse(constructorFunctionNode2, null, constructorFunctionParentNode2);
    assertNotNull(thisInConstructorTraversal.getErrors());

    // No error should be reported for a this keyword in a prototype method.
    Node prototypeFunctionNode = Node.newMethod("foo", null, null, null, null);
    JSDocInfo prototypeFunctionJSDoc = Node.newJSDocInfo(null, "@this", null);
    prototypeFunctionNode.setJSDocInfo(prototypeFunctionJSDoc);
    Node prototypeFunctionParentNode = Node.newBlock(Node.newStatements(prototypeFunctionNode));
    Node thisKeywordInPrototype = Node.newThis();
    NodeTraversal thisInPrototypeTraversal = new NodeTraversal(thisKeywordInPrototype, new CheckGlobalThis(null));
    thisInPrototypeTraversal.traverse(prototypeFunctionNode, null, prototypeFunctionParentNode);
    assertNotNull(thisInPrototypeTraversal.getErrors());
  }
}