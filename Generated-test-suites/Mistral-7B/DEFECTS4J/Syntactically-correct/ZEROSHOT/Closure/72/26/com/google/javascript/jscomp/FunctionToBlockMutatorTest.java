package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import org.junit.Test;

/**
 * FunctionToBlockMutatorTest.java
 */
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node functionNode = NodeUtil.newFunctionNode("testFunction", NodeUtil.newList(), NodeUtil.newList(), NodeUtil.newList(), NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newVarNode("a", NodeUtil.newIntLitNode(1)),
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(2))
    )));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"), NodeUtil.newList(NodeUtil.newNumberNode(3)));
    Node resultName = NodeUtil.newIdentifierNode("result");
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node mutatedNode = mutator.mutate("testFunction", functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    Node expectedMutatedNode = NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newVarNode("a", NodeUtil.newIdentifierNode("arg0")),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newNumberNode(3)),
        NodeUtil.newReturnNode(NodeUtil.newIdentifierNode("result"))
    ));

    NodeUtil.traverse(
        mutatedNode,
        (node) -> NodeUtil.assertNodesEqualDeeply(expectedMutatedNode, node),
        null,
        false
    );
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node functionNode = NodeUtil.newFunctionNode("testFunction", NodeUtil.newList(
        NodeUtil.newParameterNode("arg0", NodeUtil.newString(""), null, null, null)
    ), NodeUtil.newList(), NodeUtil.newList(), NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newAssignStatementNode("a", NodeUtil.newIdentifierNode("arg0")),
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(2))
    )));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"), NodeUtil.newList(
        NodeUtil.newNumberNode(3)
    ));
    Node resultName = NodeUtil.newIdentifierNode("result");
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node mutatedNode = mutator.mutate("testFunction", functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    Node expectedMutatedNode = NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newVarNode("arg0", "arg0"),
        NodeUtil.newAssignStatementNode("a", "arg0"),
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(2))
    ));

    NodeUtil.traverse(
        mutatedNode,
        (node) -> NodeUtil.assertNodesEqualDeeply(expectedMutatedNode, node),
        null,
        false
    );
  }

  @Test
  public void testMutateFunctionWithAliases() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node functionNode = NodeUtil.newFunctionNode("testFunction", NodeUtil.newList(
        NodeUtil.newParameterNode("arg0", NodeUtil.newString("a"), null, null, null)
    ), NodeUtil.newList(), NodeUtil.newList(), NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newVarNode("unsafeName", NodeUtil.newIdentifierNode("a")),
        NodeUtil.newAssignStatementNode("a", NodeUtil.newIdentifierNode("arg0")),
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(2))
    )));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"), NodeUtil.newList(
        NodeUtil.newNumberNode(3)
    ));
    Node resultName = NodeUtil.newIdentifierNode("result");
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Map<String, Node> argMap = ImmutableMap.of("arg0", NodeUtil.newNumberNode(3));
    Set<String> namesToAlias = ImmutableSet.of("unsafeName");

    Node mutatedNode = mutator.mutate("testFunction", functionNode, callNode, resultName, needsDefaultResult, isCallInLoop, argMap, namesToAlias);

    assertNotNull(mutatedNode);

    Node expectedMutatedNode = NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newVarNode("arg0", "arg0"),
        NodeUtil.newVarNode("alias", "alias"),
        NodeUtil.newAssignStatementNode("alias", NodeUtil.newIdentifierNode("arg0")),
        NodeUtil.newAssignStatementNode("a", "alias"),
        NodeUtil.newReturnNode(NodeUtil.newNumberNode(2))
    ));

    NodeUtil.traverse(
        mutatedNode,
        (node) -> NodeUtil.assertNodesEqualDeeply(expectedMutatedNode, node),
        null,
        false
    );
  }

  @Test
  public void testMutateFunctionWithNoReturn() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node functionNode = NodeUtil.newFunctionNode("testFunction", NodeUtil.newList(), NodeUtil.newList(), NodeUtil.newList(), NodeUtil.newFunctionBody(NodeUtil.newList(
        NodeUtil.newAssignStatementNode("a", NodeUtil.newNumberNode(2))
    )));

    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"), NodeUtil.newList());
    Node resultName = NodeUtil.newIdentifierNode("result");
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node mutatedNode = mutator.mutate("testFunction", functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNull(mutatedNode);
  }
}