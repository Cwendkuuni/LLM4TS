package com.google.javascript.jscomp.FunctionToBlockMutatorTest;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {
  private AbstractCompiler compiler;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutateFunctionWithNoArgumentsAndNoResult() {
    String fnName = "fn1";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null, null, null);

    Node callNode = NodeUtil.newCall(NodeUtil.newExpr(fnNode), Collections.emptyList());
    String resultName = null;
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
    assertEquals(Token.BLOCK, mutatedNode.getType());
  }

  @Test
  public void testMutateFunctionWithOneArgumentAndNoResult() {
    String fnName = "fn1";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null, null, null);
    Node argNode = NodeUtil.newString("arg1");
    Node callNode = NodeUtil.newCall(NodeUtil.newExpr(fnNode), Collections.singletonList(argNode));
    String resultName = null;
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
    assertEquals(Token.BLOCK, mutatedNode.getType());
  }

  @Test
  public void testMutateFunctionWithOneArgumentAndResult() {
    String fnName = "fn1";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null, null, null);
    Node argNode = NodeUtil.newString("arg1");
    Node resultNode = NodeUtil.newString("result1");
    Node callNode = NodeUtil.newCall(NodeUtil.newExpr(fnNode), Collections.singletonList(argNode));
    String resultName = resultNode.getName();
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
    assertEquals(Token.BLOCK, mutatedNode.getType());
    assertNotNull(mutatedNode.getChild(1));
    assertEquals(Token.ASSIGN, mutatedNode.getChild(1).getType());
    assertEquals(resultName, mutatedNode.getChild(1).getFirstChild().getName());
  }

  @Test
  public void testMutateFunctionWithMultipleArgumentsAndResult() {
    String fnName = "fn1";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null, null, null);
    Node arg1Node = NodeUtil.newString("arg1");
    Node arg2Node = NodeUtil.newString("arg2");
    Node callNode = NodeUtil.newCall(NodeUtil.newExpr(fnNode),
        Collections.singletonList(arg1Node), Collections.singletonList(arg2Node));
    String resultName = "result1";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
    assertEquals(Token.BLOCK, mutatedNode.getType());
    assertNotNull(mutatedNode.getChild(1));
    assertEquals(Token.VAR, mutatedNode.getChild(1).getType());
    assertEquals(2, mutatedNode.getChild(1).getNumChildren());

    Node arg1AssignmentNode = mutatedNode.getChild(1).getChild(0);
    Node arg1NameNode = arg1AssignmentNode.getFirstChild();
    assertEquals(Token.ASSIGN, arg1AssignmentNode.getType());
    assertEquals("arg1", arg1NameNode.getName());

    Node arg2AssignmentNode = mutatedNode.getChild(1).getChild(1);
    Node arg2NameNode = arg2AssignmentNode.getFirstChild();
    assertEquals(Token.ASSIGN, arg2AssignmentNode.getType());
    assertEquals("arg2", arg2NameNode.getName());

    assertNotNull(mutatedNode.getChild(2));
    assertEquals(Token.ASSIGN, mutatedNode.getChild(2).getType());
    assertEquals(resultName, mutatedNode.getChild(2).getFirstChild().getName());
  }

  @Test
  public void testMutateFunctionWithLoopCall() {
    String fnName = "fn1";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null, null, null);
    Node argNode = NodeUtil.newString("arg1");
    Node callNode = NodeUtil.newCall(NodeUtil.newExpr(fnNode), Collections.singletonList(argNode));
    String resultName = null;
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
    assertEquals(Token.BLOCK, mutatedNode.getType());
    assertNotNull(mutatedNode.getChild(0));
    assertEquals(Token.LABEL, mutatedNode.getChild(0).getType());
    assertEquals(fnName + "_label", mutatedNode.getChild(0).getFirstChild().getName());
  }
}