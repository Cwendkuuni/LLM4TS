package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {

  @Test
  public void testFunctionToBlockMutator() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallExpressionNode(fnName, null);

    Node resultNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, "result", true, false);

    // Verify that the local variable names are unique.
    Node blockNode = NodeUtil.getFunctionBody(fnNode);
    Map<String, Integer> nameCountMap = new HashMap<>();
    NameTraverser nameTraverser = new NameTraverser();
    nameTraverser.traverse(blockNode);
    for (String name : nameCountMap.keySet()) {
      int count = nameCountMap.get(name);
      assertEquals("Unexpected number of occurrences of local variable: " + name, 1, count);
    }

    // Verify that the arguments are inlined and aliased correctly.
    assertEquals(fnNode.toString(), resultNode.toString());
    Node argMapNode = NodeUtil.getFirstChildByType(resultNode, Token.BLOCK);
    assertEquals(1, argMapNode.getChildCount());
    Node argNode = argMapNode.getFirstChild();
    assertEquals("arg1", argNode.getFirstChild().getString());
    assertEquals(Token.ASSIGN, argNode.getType());
    assertEquals(Token.NAME, argNode.getLastChild().getType());
    assertEquals("arg1_test_0", argNode.getLastChild().getString());

    // Verify that uninitialized VAR declarations are initialized to 'undefined'.
    Node uninitializedVarNode = NodeUtil.newVarNode("uninitializedVar", null);
    Node blockNodeWithUninitializedVar = NodeUtil.newBlockNode(uninitializedVarNode);
    Node blockNodeWithInitializedUninitializedVar =
        functionToBlockMutator.mutate(null, null, null, null, false, false)
            .getFirstChild();
    Node initializedUninitializedVarNode =
        NodeUtil.getFirstChildByType(blockNodeWithInitializedUninitializedVar, Token.VAR);
    assertEquals("uninitializedVar", initializedUninitializedVarNode.getFirstChild().getString());
    assertEquals(Token.ASSIGN, initializedUninitializedVarNode.getType());
    assertEquals(Token.UNDEFINED, initializedUninitializedVarNode.getLastChild().getType());

    // Verify that return statements are replaced with assignments and breaks.
    String labelName = "testLabel";
    Node functionNodeWithReturn = NodeUtil.newFunctionNode("testFunctionWithReturn", null, null);
    Node returnNode = NodeUtil.newReturnNode(null, null);
    Node blockNodeWithReturn = NodeUtil.newBlockNode(returnNode);
    Node blockNodeWithLabelAndBreak =
        functionToBlockMutator.mutate(fnName, functionNodeWithReturn, null, null, false, true)
            .getFirstChild();
    Node labelNode = NodeUtil.getFirstChildByType(blockNodeWithLabelAndBreak, Token.LABEL);
    Node breakNode = NodeUtil.getFirstChildByType(blockNodeWithLabelAndBreak, Token.BREAK);
    assertEquals(labelName, labelNode.getFirstChild().getString());
    assertEquals(labelName, breakNode.getFirstChild().getString());

    // Verify that result is set if 'needsDefaultResult' is true.
    Node functionNodeWithNoResult = NodeUtil.newFunctionNode("testFunctionNoResult", null, null);
    Node blockNodeWithNoResult = NodeUtil.getFunctionBody(functionNodeWithNoResult);
    Node resultNodeWithNoResult = functionToBlockMutator.mutate(fnName, functionNodeWithNoResult, null, "result", true, false)
        .getFirstChild();
    Node assignNodeWithNoResult = NodeUtil.getFirstChildByType(resultNodeWithNoResult, Token.ASSIGN);
    assertNull(assignNodeWithNoResult);

    // Verify that result is not set if 'needsDefaultResult' is false.
    Node functionNodeWithResult = NodeUtil.newFunctionNode("testFunctionResult", null, null);
    Node resultNodeWithResult = functionToBlockMutator.mutate(fnName, functionNodeWithResult, null, "result", false, false)
        .getFirstChild();
    Node assignNodeWithResult = NodeUtil.getFirstChildByType(resultNodeWithResult, Token.ASSIGN);
    assertEquals(Token.UNDEFINED, assignNodeWithResult.getLastChild().getType());

    // Verify that 'isCallInLoop' is handled correctly.
    String labelNameForIsCallInLoop = "testLabelForIsCallInLoop";
    Node functionNodeWithCallInLoop = NodeUtil.newFunctionNode("testFunctionCallInLoop", null, null);
    Node loopStartNode = NodeUtil.newLabelNode(labelNameForIsCallInLoop);
    Node loopEndNode = NodeUtil.newLabelNode(labelNameForIsCallInLoop);
    Node blockNodeWithCallInLoop = NodeUtil.newBlockNode(loopStartNode, NodeUtil.newBreakNode(loopEndNode));
    Node callNodeWithCallInLoop = NodeUtil.newCallExpressionNode("testFunctionCallInLoop", null);
    Node blockNodeWithCallInLoopAndInlineResult =
        functionToBlockMutator.mutate(fnName, functionNodeWithCallInLoop, callNodeWithCallInLoop, "result", false, true)
            .getFirstChild();
    Node breakNodeWithCallInLoop = NodeUtil.getFirstChildByType(blockNodeWithCallInLoopAndInlineResult, Token.BREAK);
    assertEquals(labelNameForIsCallInLoop, breakNodeWithCallInLoop.getFirstChild().getString());
    Node assignNodeWithCallInLoop = NodeUtil.getFirstChildByType(blockNodeWithCallInLoopAndInlineResult, Token.ASSIGN);
    assertNotNull(assignNodeWithCallInLoop);
    assertEquals(Token.UNDEFINED, assignNodeWithCallInLoop.getLastChild().getType());

    // Verify that 'isCallInLoop' is handled correctly, with a result.
    String labelNameForIsCallInLoopWithResult = "testLabelForIsCallInLoopWithResult";
    Node functionNodeWithCallInLoopAndResult = NodeUtil.newFunctionNode(
        "testFunctionCallInLoopWithResult", null, null);
    Node loopStartNodeWithResult = NodeUtil.newLabelNode(labelNameForIsCallInLoopWithResult);
    Node loopEndNodeWithResult = NodeUtil.newLabelNode(labelNameForIsCallInLoopWithResult);
    Node blockNodeWithCallInLoopAndInlineResultAndLabel = NodeUtil.newBlockNode(
        loopStartNodeWithResult, NodeUtil.newAssignNode("result", NodeUtil.newNumberNode(42)), NodeUtil.newBreakNode(loopEndNodeWithResult));
    Node callNodeWithCallInLoopAndResult = NodeUtil.newCallExpressionNode(
        "testFunctionCallInLoopWithResult", null);
    Node blockNodeWithCallInLoopAndInlineResultAndLabelAndResult =
        functionToBlockMutator.mutate(fnName, functionNodeWithCallInLoopAndResult, callNodeWithCallInLoopAndResult, "result", false, true)
            .getFirstChild();
    Node assignNodeWithCallInLoopAndResult = NodeUtil.getFirstChildByType(blockNodeWithCallInLoopAndInlineResultAndLabelAndResult, Token.ASSIGN);
    assertEquals(Token.NUMBER, assignNodeWithCallInLoopAndResult.getLastChild().getType());
    assertEquals(42, ((NodeUtil.NumberNode) assignNodeWithCallInLoopAndResult.getLastChild()).getIntValue());
  }

  private static class NameTraverser extends NodeTraversal.AbstractTraverser<Void> {

    Map<String, Integer> nameCountMap;

    NameTraverser() {
      this.nameCountMap = new HashMap<>();
    }

    @Override
    public Void traverse(Node node) {
      if (NodeUtil.isVar(node)) {
        String name = node.getFirstChild().getString();
        nameCountMap.merge(name, 1, new Function<String, Integer>() {
          @Override
          public Integer apply(String input) {
            return Integer.valueOf(1 + nameCountMap.getOrDefault(input, 0));
          }
        });
      }
      return null;
    }
  }
}