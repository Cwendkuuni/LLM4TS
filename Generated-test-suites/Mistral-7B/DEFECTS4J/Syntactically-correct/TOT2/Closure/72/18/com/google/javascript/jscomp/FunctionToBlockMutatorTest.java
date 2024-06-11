package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Node;

/**
 * Test class for FunctionToBlockMutator.
 */
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new NoopCompiler(); // A mock compiler.
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() { var a = 1; return a; }");
    Node callNode = NodeUtil.getCallNode("testFunction()");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    // Check that the result is a statement that can be injected as a child
    // into another code block.
    assertNotNull(mutatedFunction);
    assertTrue(mutatedFunction instanceof Node);

    // Basic functionality test.
    Node expectedResult = NodeUtil.newNumberNode(1);
    Node expectedBlock = NodeUtil.newExpr(expectedResult);
    Node expectedStatement = NodeUtil.newStatement(expectedBlock);

    Node actualStatement = NodeUtil.getStatementFromBlock(mutatedFunction);
    assertEquals(expectedStatement, actualStatement);

    // Edge case: Function with arguments.
    fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  return a + b;"
        + "}");
    callNode = NodeUtil.getCallNode("testFunction(3, 4)");

    mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node actualBlock = mutatedFunction;

    // Check that the arguments have been inlined and aliased.
    assertEquals(2, NodeUtil.getChildCount(actualBlock));
    Node arg1Node = NodeUtil.getChild(actualBlock, 0);
    Node arg2Node = NodeUtil.getChild(actualBlock, 1);

    Node arg1Inline = FunctionArgumentInjector.getFunctionCallParameterMap(
        fnNode, callNode, safeNameIdSupplier).get("a");
    Node arg2Inline = FunctionArgumentInjector.getFunctionCallParameterMap(
        fnNode, callNode, safeNameIdSupplier).get("b");

    assertEquals(arg1Inline, arg1Node);
    assertEquals(arg2Inline, arg2Node);

    // Check that the addition has been replaced by a call to Add.
    Node expectedAdd = NodeUtil.newBinaryOp(Token.ADD, arg1Node, arg2Node);
    Node actualAdd = NodeUtil.getChild(actualBlock, 1);
    assertEquals(expectedAdd, actualAdd);

    // Error scenario: Function with VAR declarations without initialization.
    fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() {"
        + "  var a;"
        + "  return a;"
        + "}");
    callNode = NodeUtil.getCallNode("testFunction()");

    try {
      mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
          resultName, needsDefaultResult, isCallInLoop);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected.
    }

    // Error scenario: Function with VAR declarations and initialization.
    fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() {"
        + "  var a = 1;"
        + "  return a + 1;"
        + "}");
    callNode = NodeUtil.getCallNode("testFunction()");

    mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedStatementWithInitialization = NodeUtil.newStatement(
        NodeUtil.getFunctionBody(fnNode));
    Node actualStatementWithInitialization = NodeUtil.getStatementFromBlock(
        mutatedFunction);

    assertEquals(expectedStatementWithInitialization, actualStatementWithInitialization);

    // Error scenario: Function with loops.
    fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() {"
        + "  var a = 0;"
        + "  for (; a < 10; a++) {"
        + "    a++;"
        + "  }"
        + "  return a;"
        + "}");
    callNode = NodeUtil.getCallNode("testFunction()");

    try {
      mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
          resultName, needsDefaultResult, isCallInLoop);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected.
    }

    // Error scenario: Function with returns without result assignment.
    fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() {"
        + "  return;"
        + "}");
    callNode = NodeUtil.getCallNode("testFunction()");

    try {
      mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
          resultName, needsDefaultResult, isCallInLoop);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected.
    }

    // Error scenario: Function with non-void returns.
    fnNode = NodeUtil.getFunctionNode(
        "function " + fnName + "() {"
        + "  return 5;"
        + "}");
    callNode = NodeUtil.getCallNode("testFunction()");

    try {
      mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
          resultName, needsDefaultResult, isCallInLoop);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected.
    }

    // Test for the case where there is no need for any renaming or arguments
    // inlining.
    fnNode = NodeUtil.getFunctionNode("function testFunction() {}", "");
    callNode = NodeUtil.getCallNode("testFunction()");

    mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    Node expectedBlockNoRename = NodeUtil.getFunctionBody(fnNode);
    Node actualBlockNoRename = mutatedFunction;

    assertEquals(expectedBlockNoRename, actualBlockNoRename);
  }

  /**
   * A compiler that does nothing.
   */
  private static class NoopCompiler implements AbstractCompiler {

    @Override
    public Node compile(Node root, CompilerOptions options) {
      return root;
    }

    @Override
    public void reportCode(int severity, String message, Node node) {}

    @Override
    public void reportCode(int severity, String message) {
      fail("Unexpected reportCode call");
    }

    @Override
    public Node getUniqueNameIdSupplier() {
      return NodeUtil.newStringNode("");
    }

    @Override
    public Node getCodingConvention() {
      return NodeUtil.newNull();
    }
  }
}