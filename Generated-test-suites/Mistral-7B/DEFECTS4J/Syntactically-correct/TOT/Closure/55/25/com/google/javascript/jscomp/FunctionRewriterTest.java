package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {
  private NodeFactory nodeFactory;
  private FunctionRewriter functionRewriter;
  private NodeTraversal nodeTraversal;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    nodeTraversal = new NodeTraversal(functionRewriter);
  }

  @Test
  public void testEmptyFunctionReduction() {
    // Arrange: Empty function expression
    Node emptyFunction = nodeFactory.createFunction(
        null, null, NodeFactory.newList(), null, null, null, null);

    // Act: Traverse and reduce node
    nodeTraversal.traverse(null, emptyFunction, null);

    // Assert: Expected helper method call node instead of empty function
    Node expectedHelperCall = nodeFactory.createCall(
        "JSCompiler_emptyFn", null, null);
    Node actual = functionRewriter.compileToNode(emptyFunction);
    assertThat(actual).isEqualTo(expectedHelperCall);
  }

  @Test
  public void testIdentityFunctionReduction() {
    // Arrange: Identity function expression
    String functionString = "function f(x) { return x }";
    Node functionNode = nodeFactory.compile(functionString);

    // Act: Traverse and reduce node
    nodeTraversal.traverse(null, functionNode, null);

    // Assert: Expected helper method call node instead of identity function
    String helperSource = "function JSCompiler_identityFn(){" +
            "  return function(_JSCompiler_identityFn_value) {" +
            "    return _JSCompiler_identityFn_value" +
            "  }" +
            "}";
    Node expectedHelperCall = nodeFactory.parse(helperSource);
    Node expected = functionRewriter.compileToNode(functionNode);
    Node actual = functionRewriter.compileToNode(expectedHelperCall);
    assertThat(actual).isEqualTo(expectedHelperCall);
  }

  @Test
  public void testReturnConstantReduction() {
    // Arrange: Constant function expression
    String functionString = "function f() { return 42 }";
    Node functionNode = nodeFactory.compile(functionString);

    // Act: Traverse and reduce node
    nodeTraversal.traverse(null, functionNode, null);

    // Assert: Expected helper method call node instead of constant function
    String expectedHelperSource = "function JSCompiler_returnArg(v) {" +
            "  return function() { return v }" +
            "}";
    Node expectedHelperCall = nodeFactory.parse(expectedHelperSource);
    Node expected = functionRewriter.compileToNode(functionNode);
    Node actual = functionRewriter.compileToNode(expectedHelperCall);
    assertThat(actual).isEqualTo(expectedHelperCall);
  }

  @Test
  public void testGetterReduction() {
    // Arrange: Getter function expression
    String functionString = "function C() { this.a_ = 42; " +
            "C.prototype.getA = function() { return this.a_ } }";
    Node root = nodeFactory.compile(functionString);

    // Act: Traverse and reduce node
    nodeTraversal.traverse(null, root, null);

    // Assert: Expected helper method call node instead of getter function
    String helperSource = "function JSCompiler_get(name) {" +
            "  return function() { return this[" + name + "] }" +
            "}";
    Node expectedHelperCall = nodeFactory.parse(helperSource);
    Node expected = functionRewriter.compileToNode(
        root.getFirstChild().getLastChild());
    Node actual = functionRewriter.compileToNode(expectedHelperCall);
    assertThat(actual).isEqualTo(expectedHelperCall);
  }

  @Test
  public void testSetterReduction() {
    // Arrange: Setter function expression
    String functionString = "function C() { this.a_ = 42; " +
            "C.prototype.setA = function(value) { this.a_ = value } }";
    Node root = nodeFactory.compile(functionString);

    // Act: Traverse and reduce node
    nodeTraversal.traverse(null, root, null);

    // Assert: Expected helper method call node instead of setter function
    String helperSource = "function JSCompiler_set(name) {" +
            "  return function(value) {" +
            "this[" + name + "] = value" +
            "  }" +
            "}";
    Node expectedHelperCall = nodeFactory.parse(helperSource);
    Node expected = functionRewriter.compileToNode(
        root.getFirstChild().getLastChild().getNext());
    Node actual = functionRewriter.compileToNode(expectedHelperCall);
    assertThat(actual).isEqualTo(expectedHelperCall);
  }

  @Test(expected = AssertionError.class)
  public void testMultipleReturnStatements() {
    // Arrange: Function with multiple return statements
    String functionString = "function f() {" +
            "  return 42;" +
            "  return 24;" +
            "}";
    Node functionNode = nodeFactory.compile(functionString);

    // Act: Traverse and reduce node
    nodeTraversal.traverse(null, functionNode, null);
  }
}