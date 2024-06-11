package com.google.javascript.jscomp.test;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testSimpleFunctionInlining() {
    AbstractCompiler compiler = new AbstractCompiler();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, () -> "functionToBlockMutatorTest_simpleFunctionInlining");

    String fnName = "testFunction";
    String resultName = "result";
    Node fnNode = Node.newFunctionNode(fnName, null, null, null, null);
    Node callNode = Node.newCallNode(fnName, NodeUtil.newList(Node.newNumber(1)));
    Node newBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertNotNull(newBlock);
  }

  @Test
  public void testFunctionInliningWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, () -> "functionToBlockMutatorTest_functionInliningWithArguments");

    String fnName = "testFunction";
    String resultName = "result";
    String arg1 = "arg1";
    Node fnNode = Node.newFunctionNode(fnName, null, null, null, null);
    Node callNode = Node.newCallNode(fnName, NodeUtil.newList(
        Node.newExpressionNode(Token.NAME, arg1),
        Node.newNumber(1)));

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put(arg1, Node.newNumber(2));

    Node newBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertNotNull(newBlock);
  }

  @Test
  public void testFunctionInliningWithLoop() {
    AbstractCompiler compiler = new AbstractCompiler();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, () -> "functionToBlockMutatorTest_functionInliningWithLoop");

    String fnName = "testFunction";
    String resultName = "result";
    Node fnNode = Node.newFunctionNode(fnName, null, null, null, null);
    Node callNode = Node.newCallNode(fnName, NodeUtil.newList());
    String labelName = "label";

    Node newBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, true);

    assertNotNull(newBlock);
  }

  @Test
  public void testFunctionInliningWithAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, () -> "functionToBlockMutatorTest_functionInliningWithAliases");

    String fnName = "testFunction";
    String resultName = "result";
    String arg1 = "arg1";
    String alias1 = "alias1";

    Node fnNode = Node.newFunctionNode(fnName, null, null, null, null);
    Node callNode = Node.newCallNode(fnName, NodeUtil.newList(
        Node.newExpressionNode(Token.NAME, arg1)));

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put(arg1, Node.newNumber(2));

    Set<String> namesToAlias = Set.of(arg1);
    String newArgName = "newArg";
    Node newArgNode = Node.newVarNode(newArgName, null);

    Map<String, Node> newArgMap = Maps.newHashMap(argMap);
    newArgMap.put(newArgName, newArgNode);

    Node newBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    // Check that the alias is used in the new block.
    for (Node c = newBlock.getFirstChild(); c != null; c = c.getNext()) {
      if (NodeUtil.isAssignmentStatement(c)) {
        Node leftHandSide = c.getFirstChild();
        if (leftHandSide.getType() == Token.NAME) {
          String name = leftHandSide.getString();
          if (alias1.equals(name)) {
            Node rightHandSide = c.getLastChild();
            if (rightHandSide.getType() == Token.GET_PROP) {
              Node getProp = rightHandSide.getFirstChild();
              String propertyName = getProp.getString();
              if (newArgName.equals(propertyName)) {
                return;
              }
            }
          }
        }
      }
    }

    fail("Failed to use alias in the inline block.");
  }
}