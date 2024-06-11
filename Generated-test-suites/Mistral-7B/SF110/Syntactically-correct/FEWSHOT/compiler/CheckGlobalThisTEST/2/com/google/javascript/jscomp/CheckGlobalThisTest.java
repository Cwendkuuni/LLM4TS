package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private static void checkFunctionJsDocInfo(Node node, JSDocInfo expectedJsDocInfo) {
    JSDocInfo jsDocInfo = new CheckGlobalThis(null).getFunctionJsDocInfo(node);
    assertTrue(jsDocInfo != null);
    assertTrue(jsDocInfo.equals(expectedJsDocInfo));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node node = AbstractCompilerTest.createNode("function foo() {}", 1, 0);
    JSDocInfo expectedJsDocInfo = new JSDocInfo();
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("function foo() {}", 1, 0, "foo");
    expectedJsDocInfo = new JSDocInfo("foo");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("var foo = function() {}", 1, 0);
    expectedJsDocInfo = new JSDocInfo();
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("var foo = function() {}", 1, 0, "foo");
    expectedJsDocInfo = new JSDocInfo("foo");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, "constructor");
    expectedJsDocInfo = new JSDocInfo("constructor");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, "this");
    expectedJsDocInfo = new JSDocInfo("this");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, "override");
    expectedJsDocInfo = new JSDocInfo("override");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, "interface", "SomeInterface");
    expectedJsDocInfo = new JSDocInfo("interface", "SomeInterface");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);

    node = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, null, null, "constructor", "MyConstructor");
    expectedJsDocInfo = new JSDocInfo("constructor", "MyConstructor");
    checkFunctionJsDocInfo(node, expectedJsDocInfo);
  }

  @Test
  public void testShouldTraverse() {
    Node root = AbstractCompilerTest.createNode("({ foo: function() {} })");

    // Traverse this node
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, root, checker);
    traversal.traverse();

    // Prototype method
    Node prototypeMethod = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, null, null, "prototype", "Object.prototype");
    assertFalse(checker.shouldTraverse(null, prototypeMethod, root));

    // Constructor
    Node constructor = AbstractCompilerTest.createNode("function foo() { constructor() }", 1, 0, null, null, "constructor");
    assertFalse(checker.shouldTraverse(null, constructor, root));

    // @this function
    Node thisAnnotation = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, null, null, null, null, "@this");
    assertFalse(checker.shouldTraverse(null, thisAnnotation, root));

    // Function inside object literal
    Node functionInsideObjectLiteral = AbstractCompilerTest.createNode("({ foo: function() {} })", 1, 0);
    assertFalse(checker.shouldTraverse(null, functionInsideObjectLiteral, root));

    // Function annotated with @constructor
    Node functionAnnotatedWithConstructorAnnotation = AbstractCompilerTest.createNode("(@constructor) function foo() {}", 1, 0);
    assertFalse(checker.shouldTraverse(null, functionAnnotatedWithConstructorAnnotation, root));

    // Function annotated with @this
    Node functionAnnotatedWithThisAnnotation = AbstractCompilerTest.createNode("(@this) function foo() {}", 1, 0);
    assertFalse(checker.shouldTraverse(null, functionAnnotatedWithThisAnnotation, root));

    // Function with no logical place to put a @this annotation
    Node functionWithNoLogicalPlaceToPutThisAnnotation = AbstractCompilerTest.createNode("function foo() {}", 1, 0);
    assertTrue(checker.shouldTraverse(null, functionWithNoLogicalPlaceToPutThisAnnotation, root));

    // Function that's being lent to a prototype
    Node functionBeingLentToPrototype = AbstractCompilerTest.createNode("({ foo: function foo() {} })", 1, 0);
    assertFalse(checker.shouldTraverse(null, functionBeingLentToPrototype, root));

    // Function inside a block
    Node functionInsideBlock = AbstractCompilerTest.createNode("({ foo: function() {} })", 1, 0, null, null, "script");
    assertTrue(checker.shouldTraverse(null, functionInsideBlock, root));

    // Function inside a script
    Node functionInsideScript = AbstractCompilerTest.createNode("function foo() {}", 1, 0, null, null, "script");
    assertTrue(checker.shouldTraverse(null, functionInsideScript, root));

    // Function inside a var declaration
    Node functionInsideVarDeclaration = AbstractCompilerTest.createNode("var foo = function() {}", 1, 0, null, null, "assign");
    assertTrue(checker.shouldTraverse(null, functionInsideVarDeclaration, root));

    // Function where parent is a get property
    Node getPropertyParent = AbstractCompilerTest.createNode("({ get foo() {} })", 1, 0, null, null, null, null, null, null, "get", "Object.prototype");
    Node getPropertyFunction = AbstractCompilerTest.createNode("({ get foo() {} })", 1, 0);
    assertTrue(checker.shouldTraverse(null, getPropertyFunction, getPropertyParent));

    // Function where parent is a get property and the get property is an object literal key
    Node objectLiteralKeyParent = AbstractCompilerTest.createNode("({ get foo() { get bar() {} } })", 1, 0);
    Node objectLiteralKeyPropertyParent = AbstractCompilerTest.createNode("({ get foo() { get bar() {} } })", 1, 0, null, null, null, null, NodeUtil.createIdentifier("bar"));
    Node objectLiteralKeyFunction = AbstractCompilerTest.createNode("({ get foo() { get bar() {} } })", 1, 0);
    assertTrue(checker.shouldTraverse(null, objectLiteralKeyFunction, objectLiteralKeyParent));

    // Function where parent is an assignment to a prototype property or subproperty
    Node prototypePropertyParent = AbstractCompilerTest.createNode("({ get foo() {} }).prototype = { foo: function() {} }", 1, 0);
    Node prototypePropertyFunction = AbstractCompilerTest.createNode("({ get foo() {} }).prototype = { foo: function() {} }", 1, 0, null, null, null, "assign", "Object.prototype");
    assertFalse(checker.shouldTraverse(null, prototypePropertyFunction, prototypePropertyParent));

    Node prototypeSubpropertyParent = AbstractCompilerTest.createNode("({ get foo() {} }).prototype.subProperty = { foo: function() {} }", 1, 0);
    Node prototypeSubpropertyFunction = AbstractCompilerTest.createNode("({ get foo() {} }).prototype.subProperty = { foo: function() {} }", 1, 0, null, null, null, null, "prototype", "Object.prototype", "subProperty");
    assertFalse(checker.shouldTraverse(null, prototypeSubpropertyFunction, prototypeSubpropertyParent));
  }
}