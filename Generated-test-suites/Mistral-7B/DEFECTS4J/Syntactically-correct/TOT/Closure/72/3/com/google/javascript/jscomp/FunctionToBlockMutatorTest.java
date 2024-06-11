package com.google.javascript.jscomp;

import static com.google.javascript.rhino.NodeProperty.TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SupplierFactor;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithNoArgumentsAndNoReturnValue() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(), null);
    String resultName = "result";

    Node mutatedNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertEquals(TYPE.BLOCK, mutatedNode.getType());
    assertEquals(0, mutatedNode.getChildCount());
  }

  @Test
  public void testMutateFunctionWithArgumentsAndNoReturnValue() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(
            NodeUtil.newIdentifierNode("arg1"),
            NodeUtil.newIdentifierNode("arg2")),
        null);
    String resultName = "result";

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newIdentifierNode("arg1"));
    argMap.put("arg2", NodeUtil.newIdentifierNode("arg2"));

    Node mutatedNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertEquals(TYPE.BLOCK, mutatedNode.getType());
    assertEquals(0, mutatedNode.getChildCount());

    // Check that arguments have been injected
    for (Node c : fnNode.getChildren()) {
      if (NodeUtil.isVar(c)) {
        String name = c.getFirstChild().getNodeValue().toString();
        assertSame(argMap.get(name), c.getLastChild());
      }
    }
  }

  @Test
  public void testMutateFunctionWithArgumentsAndReturnValue() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(
            NodeUtil.newIdentifierNode("arg1"),
            NodeUtil.newIdentifierNode("arg2")),
        NodeUtil.newIdentifierNode("result"));
    String resultName = "result";

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newIdentifierNode("arg1"));
    argMap.put("arg2", NodeUtil.newIdentifierNode("arg2"));

    Node mutatedNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, false);

    assertEquals(TYPE.BLOCK, mutatedNode.getType());
    assertEquals(1, mutatedNode.getChildCount());

    // Check that arguments have been injected
    for (Node c : fnNode.getChildren()) {
      if (NodeUtil.isVar(c)) {
        String name = c.getFirstChild().getNodeValue().toString();
        assertSame(argMap.get(name), c.getLastChild());
      }
    }

    // Check that return value has been inlined
    Node returnNode = mutatedNode.getFirstChild();
    assertSame(NodeUtil.newIdentifierNode("result"), returnNode);

    // Check that body has been replaced
    Node expectedBody = NodeUtil.newExprNode(
        NodeUtil.newBlock(
            NodeUtil.newAssignStatementNode(resultName, NodeUtil.newIdentifierNode("arg1")),
            NodeUtil.newAssignStatementNode(resultName, NodeUtil.newBinaryOpNode(
                NodeUtil.newToken(Token.PLUS),
                NodeUtil.newIdentifierNode("arg1"),
                NodeUtil.newIdentifierNode("arg2")))));
    Node actualBody = NodeUtil.getFunctionBody(mutatedNode);
    assertEquals(expectedBody, actualBody);
  }

  @Test
  public void testMutateFunctionWithArgumentsAndReturnValueInLoop() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(
            NodeUtil.newIdentifierNode("arg1"),
            NodeUtil.newIdentifierNode("arg2")),
        NodeUtil.newIdentifierNode("result"));
    String resultName = "result";

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newIdentifierNode("arg1"));
    argMap.put("arg2", NodeUtil.newIdentifierNode("arg2"));

    Node mutatedNode = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, true);

    assertEquals(TYPE.BLOCK, mutatedNode.getType());
    assertEquals(1, mutatedNode.getChildCount());

    // Check that arguments have been injected
    for (Node c : fnNode.getChildren()) {
      if (NodeUtil.isVar(c)) {
        String name = c.getFirstChild().getNodeValue().toString();
        assertSame(argMap.get(name), c.getLastChild());
      }
    }

    // Check that return value has been inlined
    Node returnNode = mutatedNode.getFirstChild();
    assertSame(NodeUtil.newIdentifierNode(resultName), returnNode);

    // Check that body has been replaced
    Node expectedBody = NodeUtil.newExprNode(
        NodeUtil.newBlock(
            NodeUtil.newAssignStatementNode(resultName, NodeUtil.newIdentifierNode("arg1")),
            NodeUtil.newAssignStatementNode(resultName, NodeUtil.newBinaryOpNode(
                NodeUtil.newToken(Token.PLUS),
                NodeUtil.newIdentifierNode("arg1"),
                NodeUtil.newIdentifierNode("arg2"))),
            NodeUtil.newLabelNode(NodeUtil.newString("JSCompiler_inline_label_testFunction_0"))));
    Node actualBody = NodeUtil.getFunctionBody(mutatedNode);
    assertEquals(expectedBody, actualBody);

    // Check that variables have been initialized
    Node firstChild = mutatedNode.getFirstChild();
    for (Node child = firstChild.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isVar(child)) {
        String name = child.getFirstChild().getNodeValue().toString();
        Node valueNode = child.getLastChild();
        if (NodeUtil.isUndefinedNode(valueNode)) {
          Node value = NodeUtil.newNumberNode(1);
          Node newAssign = NodeUtil.newAssignStatementNode(
              new NodeUtil.LabeledStatementNode(
                  NodeUtil.newLabelNode(NodeUtil.newString("JSCompiler_inline_label_testFunction_0")),
                  NodeUtil.newStatementNode(NodeUtil.newAssignStatementNode(name, value))));
          firstChild.replaceChild(child, newAssign);
          break;
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithNullCompiler() {
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        null, safeNameIdSupplier);
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullSafeNameIdSupplier() {
    AbstractCompiler compiler = new MockCompiler();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, null);
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullFnName() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNode("", null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(), null);
    String resultName = "result";

    functionToBlockMutator.mutate(null, fnNode, callNode, resultName, false, false);
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullFnNode() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(), null);
    String resultName = "result";

    functionToBlockMutator.mutate(fnName, null, callNode, resultName, false, false);
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullCallNode() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    String resultName = "result";

    functionToBlockMutator.mutate(fnName, fnNode, null, resultName, false, false);
  }

  @Test(expected = NullPointerException.class)
  public void testMutateFunctionWithNullResultName() {
    AbstractCompiler compiler = new MockCompiler();
    Supplier<String> safeNameIdSupplier = SupplierFactor.createStringSupplier();
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifierNode("testFunction"),
        NodeUtil.newListNode(), null);

    functionToBlockMutator.mutate(fnName, fnNode, callNode, null, false, false);
  }

  private static class MockCompiler implements AbstractCompiler {
    @Override
    public Node compile(Node node, Function<Node, Node> currentScope) {
      return null;
    }

    @Override
    public Node compileExpression(Node node, Function<Node, Node> currentScope) {
      return null;
    }

    @Override
    public Node compileStatement(Node node, Function<Node, Node> currentScope) {
      return null;
    }

    @Override
    public Node compileConditional(Node node, Function<Node, Node> thenScope,
        Function<Node, Node> elseScope) {
      return null;
    }

    @Override
    public Node compileLoop(Node node, Function<Node, Node> loopScope) {
      return null;
    }

    @Override
    public Node compileBlock(Node node, Function<Node, Node> currentScope) {
      return null;
    }

    @Override
    public Node compileDeclaration(Node node, Function<Node, Node> currentScope) {
      return null;
    }

    @Override
    public Node compileStatementList(List<Node> nodes, Function<Node, Node> currentScope) {
      return null;
    }

    @Override
    public Node getUniqueNameIdSupplier() {
      return null;
    }

    @Override
    public Node getCodingConvention() {
      return null;
    }
  }
}