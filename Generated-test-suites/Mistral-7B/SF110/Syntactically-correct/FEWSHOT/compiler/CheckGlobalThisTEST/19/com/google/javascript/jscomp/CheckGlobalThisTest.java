package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeTraversal traversal = mock(NodeTraversal.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction");
    Node assignNode = Node.newAssign(Node.newThis(), Node.newNumber(1));
    Node objectLiteralNode = Node.newObjectLiteral();
    Node stringKeyNode = Node.newString("key");
    Node prototypeNode = Node.newName("prototype");

    when(compiler.getCheckerFactory()).thenReturn(new CheckerFactory() {
      @Override
      public Callback getCheck(DiagnosticType type) {
        if (type == CheckGlobalThis.GLOBAL_THIS) {
          return checker;
        }
        return null;
      }
    });

    // Traverse function nodes
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
    assertTrue(checker.shouldTraverse(traversal, functionNode, Node.newName("MyClass")));
    assertTrue(checker.shouldTraverse(traversal, functionNode, Node.newObjectLiteral()));

    // Don't traverse non-function nodes
    assertFalse(checker.shouldTraverse(traversal, Node.newNumber(1), null));
    assertFalse(checker.shouldTraverse(traversal, Node.newString("test"), null));
    assertFalse(checker.shouldTraverse(traversal, Node.newAssign(Node.newName("x"), Node.newNumber(1)), null));

    // Don't traverse function nodes with @constructor or @this annotations
    JSDocInfo jsDoc = Node.newJSDocInfo().setConstructor(true);
    Node constructorNode = Node.newFunction("testConstructor", jsDoc);
    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));

    jsDoc = Node.newJSDocInfo().setThisType("MyClass");
    Node constructorWithThisNode = Node.newFunction("testConstructorWithThis", jsDoc);
    assertFalse(checker.shouldTraverse(traversal, constructorWithThisNode, null));

    // Don't traverse functions in constructors or prototype methods
    Node constructorWithFunctionNode = Node.newFunction("testFunctionInConstructor");
    Node constructorWithPrototypeMethodNode = Node.newFunction("testFunctionInPrototypeMethod");
    Node prototypeNodeWithFunctionNode = Node.newName("MyClass.prototype.testFunction");

    Node constructorWithPrototypeMethodParent = Node.newName("MyClass");
    Node constructorWithPrototypeMethodGrandparent = Node.newObjectLiteral();
    Node constructorWithPrototypeMethodKey = Node.newString("prototype");

    when(NodeUtil.isObjectLitKey(prototypeNodeWithFunctionNode.getParent())).thenReturn(true);
    when(NodeUtil.isObjectLitKey(constructorWithPrototypeMethodParent)).thenReturn(true);

    assertFalse(checker.shouldTraverse(traversal, constructorWithFunctionNode, null));
    assertFalse(checker.shouldTraverse(traversal, constructorWithPrototypeMethodNode, constructorWithPrototypeMethodParent));
    assertFalse(checker.shouldTraverse(traversal, constructorWithPrototypeMethodNode, constructorWithPrototypeMethodGrandparent));

    // Don't traverse functions lent to prototypes
    Node objectWithFunctionNode = Node.newObjectLiteral();
    Node objectWithLendsNode = Node.newName("Object");
    Node prototypeNodeWithFunctionNode = Node.newName("Object.prototype.testFunction");

    Node assignNodeToPrototype = Node.newAssign(prototypeNode, Node.newName("testFunction"));

    JSDocInfo lendsInfo = Node.newJSDocInfo().setLendsName("MyClass.prototype");
    when(objectWithLendsNode.getJSDocInfo()).thenReturn(lendsInfo);

    assertTrue(checker.shouldTraverse(traversal, objectWithFunctionNode, null));
    assertFalse(checker.shouldTraverse(traversal, prototypeNodeWithFunctionNode, objectWithLendsNode));
    assertFalse(checker.shouldTraverse(traversal, assignNodeToPrototype, objectWithLendsNode));

    // Traverse the left side of assignments
    Node assignLeftSideNode = Node.newAssign(Node.newName("x"), Node.newThis());
    assertTrue(checker.shouldTraverse(traversal, assignLeftSideNode, null));

    // Don't traverse the right side of assignments to prototype properties
    Node assignRightSidePrototypeNode = Node.newAssign(prototypeNode, Node.newName("x"));
    Node assignRightSidePrototypeParent = Node.newName("MyClass.prototype");
    assertFalse(checker.shouldTraverse(traversal, assignRightSidePrototypeNode, assignRightSidePrototypeParent));

    // Traverse the left side of assignments to non-prototype properties
    Node assignLeftSideNode2 = Node.newAssign(Node.newName("x"), Node.newThis());
    Node assignLeftSideNode2Parent = Node.newObjectLiteral();
    assertTrue(checker.shouldTraverse(traversal, assignLeftSideNode2, assignLeftSideNode2Parent));
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeTraversal traversal = mock(NodeTraversal.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction");
    Node assignNode = Node.newAssign(Node.newThis(), Node.newNumber(1));
    Node objectLiteralNode = Node.newObjectLiteral();
    Node stringKeyNode = Node.newString("key");
    Node prototypeNode = Node.newName("prototype");

    when(compiler.report(traversal, Node.error(functionNode, 1, "")))
        .thenCallRealMethod();

    checker.visit(traversal, functionNode, null);

    checker.visit(traversal, assignNode, Node.newAssign(Node.newName("x"), Node.newThis()));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node functionNode = Node.newFunction("testFunction");
    Node assignNode = Node.newAssign(Node.newName("x"), Node.newThis());
    Node functionWithJsDocNode = Node.newFunction("testFunction", Node.newJSDocInfo());
    Node assignWithJsDocNode = Node.newAssign(Node.newName("x"), Node.newThis(), Node.newJSDocInfo());

    JSDocInfo jsDoc = CheckGlobalThis.getFunctionJsDocInfo(functionNode);
    assertEquals(null, jsDoc);

    jsDoc = CheckGlobalThis.getFunctionJsDocInfo(functionWithJsDocNode);
    assertEquals(jsDoc, functionWithJsDocNode.getJSDocInfo());

    jsDoc = CheckGlobalThis.getFunctionJsDocInfo(assignNode);
    assertEquals(null, jsDoc);

    jsDoc = CheckGlobalThis.getFunctionJsDocInfo(assignWithJsDocNode);
    assertEquals(assignWithJsDocNode.getJSDocInfo(), jsDoc);
  }
}