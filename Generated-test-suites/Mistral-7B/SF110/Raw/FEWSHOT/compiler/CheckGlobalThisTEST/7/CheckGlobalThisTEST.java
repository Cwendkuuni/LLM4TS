package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    final CheckGlobalThis checker = new CheckGlobalThis(null);

    // Test that functions inside prototypes are not traversed
    Node prototypeNode = new Node(Token.NAME, "Object", null);
    prototypeNode.setProperty("prototype", new Node(Token.OBJECT_LITERAL, null));
    prototypeNode.getLastChild().addChild(
        new Node(Token.PROPERTY, null, null, null, null, null, new Node(Token.NAME, "prototypeMethod", null)));
    Node functionNode = prototypeNode.getLastChild().getLastChild();

    assertFalse(checker.shouldTraverse(null, functionNode, prototypeNode));

    // Test that functions inside objects are traversed
    Node objectNode = new Node(Token.OBJECT_LITERAL, null);
    objectNode.addChild(new Node(Token.PROPERTY, null, null, null, null, null,
        new Node(Token.NAME, "method", null),
        new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null)));

    assertTrue(checker.shouldTraverse(null, objectNode.getLastChild().getLastChild(), objectNode));

    // Test that functions inside blocks are traversed
    Node blockNode = new Node(Token.BLOCK, null);
    blockNode.addChild(new Node(Token.VAR, null, null, null, null, null, new Node(Token.NAME, "function", null),
        new Node(Token.ASSIGN, null, null, null, null, null, functionNode, null, null, null, null, null, null),
        null));

    assertTrue(checker.shouldTraverse(null, functionNode, blockNode));

    // Test that functions with @constructor annotation are not traversed
    Node constructorNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null);
    constructorNode.addJSDocInfo(new JSDocInfo(true, null, null, null, true, null, null, null, null, null));

    assertFalse(checker.shouldTraverse(null, constructorNode, null));

    // Test that functions with @this annotation are not traversed
    Node thisAnnotatedFunctionNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null);
    thisAnnotatedFunctionNode.addJSDocInfo(new JSDocInfo(null, null, "@this", null, null, null, null));

    assertFalse(checker.shouldTraverse(null, thisAnnotatedFunctionNode, null));

    // Test that functions with @interface annotation are not traversed
    Node interfaceNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null);
    interfaceNode.addJSDocInfo(new JSDocInfo(null, null, null, "@interface", null, null, null, null, null, null));

    assertFalse(checker.shouldTraverse(null, interfaceNode, null));

    // Test that functions with @override annotation are not traversed
    Node overriddenNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null);
    overriddenNode.addJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, "@override", null, null, null));

    assertFalse(checker.shouldTraverse(null, overriddenNode, null));

    // Test that functions without a logical place to put a @this annotation are traversed
    Node functionWithoutThisAnnotatedNode = new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(checker.shouldTraverse(null, functionWithoutThisAnnotatedNode, null));
  }

  @Test
  public void testShouldReportThis() {
    final CheckGlobalThis checker = new CheckGlobalThis(null);

    Node thisNode = new Node(Token.THIS, null);
    assertTrue(checker.shouldReportThis(thisNode));

    Node assignmentNode = new Node(Token.ASSIGN, null, null, null, null, null, thisNode, null, null, null, null, null, null);
    assertTrue(checker.shouldReportThis(assignmentNode));
  }

  @Test
  public void testTraversal() {
    final CheckGlobalThis checker = new CheckGlobalThis(null);
    final AbstractCompiler compiler = new Compiler();

    Node rootNode = new Node(Token.SCRIPT, null);
    rootNode.addChild(new Node(Token.VAR, null, null, null, null, null, new Node(Token.NAME, "globalThisVariable", null), null, null, null, null, null));
    rootNode.addChild(new Node(Token.ASSIGN, null, null, null, null, null, thisNode("globalThisVariable"), new Node(Token.THIS, null), null, null, null, null));

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(checker, rootNode);

    assertEquals(1, checker.compiler.getDiagnostics().length);
    assertEquals("dangerous use of the global 'this' object", checker.compiler.getDiagnostics()[0].getMessage());
  }

  private Node thisNode(String name) {
    return new Node(Token.NAME, name, null);
  }
}