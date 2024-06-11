package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import java.util.Map;

/**
 * Test class for FunctionToBlockMutator.
 */
public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    safeNameIdSupplier = () -> "test";
  }

  @org.junit.Test
  public void testMutate() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, NodeUtil.newString("testFunction"), null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node newFnNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);
  }

  @org.junit.Test
  public void testMutateWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, NodeUtil.newString("testFunction"), null);
    LinkedHashMap<String, Node> argMap = ImmutableMap.of("arg1", Node.newNumber(1), "arg2", Node.newNumber(2));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node newFnNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    // Check that arguments were correctly renamed and inline.
    for (Map.Entry<String, Node> entry : argMap.entrySet()) {
      Node arg = entry.getValue();
      String argName = entry.getKey();
      Node argNode = NodeUtil.getFirstChildByType(newFnNode, Token.VAR);
      Node nameNode = argNode.getFirstChild();
      assertEquals(argName, nameNode.getString());
      Node valueNode = argNode.getNext();
      assertEquals(arg.getNodeType(), valueNode.getType());
      assertEquals(arg.getNodeValue(), valueNode.getNodeValue());
    }
  }

  @org.junit.Test
  public void testMutateWithLabel() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, NodeUtil.newString("testFunction"), null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node newFnNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    // Check that label was correctly created.
    Node labelNode = NodeUtil.getFirstChildByType(newFnNode, Token.LABEL);
    String labelName = labelNode.getFirstChild().getString();
    assertEquals("JSCompiler_inline_label_" + fnName + "_test", labelName);
  }

  @org.junit.Test
  public void testMutateWithLabelAndResult() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, NodeUtil.newString("testFunction"), null);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node newFnNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    // Check that label and result were correctly created.
    Node labelNode = NodeUtil.getFirstChildByType(newFnNode, Token.LABEL);
    String labelName = labelNode.getFirstChild().getString();
    assertEquals("JSCompiler_inline_label_" + fnName + "_test", labelName);

    Node resultNode = NodeUtil.getFirstChildByType(newFnNode, Token.VAR);
    assertEquals(resultName, resultNode.getFirstChild().getString());
  }

  @org.junit.Test
  public void testReplaceReturns() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, NodeUtil.newString("testFunction"), null);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node newFnNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    Node block = NodeUtil.getFunctionBody(newFnNode);
    Node returnNode = NodeUtil.getFirstChildByType(block, Token.RETURN);

    if (needsDefaultResult) {
      if (returnNode == null) {
        fail("Expected return statement to be added.");
      }
      Node resultNode = NodeUtil.getFirstChildByType(block, Token.VAR);
      assertEquals(resultName, resultNode.getFirstChild().getString());
      assertEquals(Node.newNumber(0), resultNode.getNext());
    } else {
      assertNull(returnNode);
    }
  }
}