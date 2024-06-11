package com.google.javascript.jscomp;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String resultName = "result";
    String labelName = "testLabel";

    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName);

    Node mutatedFunction = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, resultName, true, false);

    assertNotNull(mutatedFunction);
    assertEquals(NodeUtil.getFunctionBody(functionNode), mutatedFunction);

    // Test that arguments have been inlined and aliased properly
    Map<String, Node> argumentMap = Maps.newLinkedHashMap();
    argumentMap.put("arg1", NodeUtil.newIntLiteral(1));
    argumentMap.put("arg2", NodeUtil.newIntLiteral(2));

    Node expectedResult = NodeUtil.newBlock(
        new Node(Token.BLOCK,
            new Node(Token.VAR, Node.newString(Token.NAME, "arg1")),
            new Node(Token.VAR, Node.newString(Token.NAME, "arg2")),
            new Node(Token.ASSIGN, Node.newString(Token.NAME, "arg1"),
                NodeUtil.newIntLiteral(3)),
            new Node(Token.ASSIGN, Node.newString(Token.NAME, "arg2"),
                NodeUtil.newIntLiteral(4)),
            new Node(Token.RETURN, NodeUtil.newIntLiteral(5))));

    Node actualResult = FunctionArgumentInjector.inject(
        mutatedFunction, null, argumentMap);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutateFunctionWithoutArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String functionName = "testFunction";

    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName);

    Node mutatedFunction = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, null, true, false);

    assertNotNull(mutatedFunction);
    assertEquals(NodeUtil.getFunctionBody(functionNode), mutatedFunction);

    // Test that no arguments have been inlined and no aliases have been created
    Map<String, Node> argumentMap = Maps.newLinkedHashMap();
    Node expectedResult = NodeUtil.getFunctionBody(functionNode);

    Node actualResult = FunctionArgumentInjector.inject(
        mutatedFunction, null, argumentMap);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturnWithBreak() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String labelName = "testLabel";
    String resultName = "result";

    Node functionNode = createTestFunctionNodeWithReturn(functionName);

    Node mutatedFunction = functionToBlockMutator.mutate(
        functionName, functionNode, null, null, true, false);

    Node expectedResult = new Node(Token.BLOCK,
        new Node(Token.LABEL, new Node.LabelName(labelName)),
        new Node(Token.BREAK, new Node.LabelName(labelName)));

    assertEquals(expectedResult, mutatedFunction);
  }

  @Test
  public void testReplaceReturnWithBreakAndResult() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String labelName = "testLabel";
    String resultName = "result";

    Node functionNode = createTestFunctionNodeWithReturnAndResult(functionName);

    Node mutatedFunction = functionToBlockMutator.mutate(
        functionName, functionNode, null, resultName, true, false);

    Node expectedResult = new Node(Token.BLOCK,
        new Node(Token.LABEL, new Node.LabelName(labelName)),
        new Node(Token.ASSIGN, new Node.Name(resultName), NodeUtil.newUndefinedNode()),
        new Node(Token.BREAK, new Node.LabelName(labelName)));

    assertEquals(expectedResult, mutatedFunction);
  }

  private Node createTestFunctionNode(String name) {
    return new Node(Token.FUNCTION, null, null, null,
        new Node(Token.BLOCK,
            new Node(Token.VAR, Node.newString(Token.NAME, name)),
            new Node(Token.BLOCK, new Node(Token.RETURN, NodeUtil.newIntLiteral(5)))));
  }

  private Node createTestFunctionNodeWithReturn(String name) {
    return new Node(Token.FUNCTION, null, null, null,
        new Node(Token.BLOCK, new Node(Token.RETURN, NodeUtil.newIntLiteral(5))));
  }

  private Node createTestFunctionNodeWithReturnAndResult(String name) {
    return new Node(Token.FUNCTION, null, null, null,
        new Node(Token.BLOCK, new Node.Name(name), new Node(Token.BLOCK,
            new Node(Token.ASSIGN, Node.newString(Token.NAME, "result"),
                NodeUtil.newIntLiteral(4)),
            new Node(Token.RETURN, NodeUtil.newIntLiteral(5)))));
  }

  private Node createTestCallNode(String name) {
    return new Node(Token.CALL, NodeUtil.newIdentifier(name), null, null);
  }
}