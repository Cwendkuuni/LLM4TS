package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {
  @Test
  public void testEmptyFunctionReduction() {
    String code = "function EmptyFunction() {}";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_emptyFn"), null);
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testIdentityFunctionReduction() {
    String code = "function IdentityFunction(input) { return input }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 2, 1,
        new Node(Token.NAME, "JSCompiler_identityFn"),
        new Node(Token.NAME, "input"));
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testReturnConstantReduction() {
    String code = "function ReturnConstantFunction() { return 1 }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_returnArg"),
        new Node(Token.INT_LITERAL, 1));
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testGetterReduction() {
    String code = "function getterFunction() { return this.property }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_get"),
        new Node(Token.NAME, "property"));
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testSetterReduction() {
    String code = "function setterFunction(value) { this.property = value }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 3, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.NAME, "property"),
        new Node(Token.INT_LITERAL, 1));
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testComplexFunctionReduction() {
    String code = "function ComplexFunction() { return 2 * this.property }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 2, 1,
        new Node(Token.NAME, "JSCompiler_mul"),
        new Node(Token.INT_LITERAL, 2),
        new Node(Token.CALL, 1, 1,
            new Node(Token.NAME, "JSCompiler_get"),
            new Node(Token.NAME, "property")));
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testNestedFunctionReduction() {
    String code = "function OuterFunction() { return function InnerFunction() { return 2 } }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    Node expectedResult = new Node(Token.CALL, 2, 1,
        new Node(Token.NAME, "JSCompiler_returnArg"),
        new Node(Token.CALL, 1, 1,
            new Node(Token.NAME, "JSCompiler_emptyFn"),
            new Node(Token.NAME, "InnerFunction")));
    assertEquals(expectedResult, nodeTraversal.head());
  }

  @Test
  public void testInvalidFunctionReduction() {
    String code = "function InvalidFunction() { return this.invalidProperty }";
    Node node = RhinoLoader.compileToNode(code);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    NodeTraversal nodeTraversal = new NodeTraversal(node);
    nodeTraversal.traverse(functionRewriter);

    assertNotNull(nodeTraversal.head());
    // The function should not be reduced since "invalidProperty" does not exist.
  }
}