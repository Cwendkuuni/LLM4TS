package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Node;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new TestCompiler();

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, Suppliers.ofInstance(new IdGenerator()));

    String functionName = "testFunction";
    Node functionNode = NodeUtil.getFunctionNode(
        "function " + functionName + "() { return 1 + 2; }");
    Node callNode = NodeUtil.getCallNode(functionName, NodeUtil.newIntLiteral(3));

    Node resultNode = mutator.mutate(functionName, functionNode, callNode, "result", true, false);

    assertEquals(
        "{JSCompiler_inline_label_testFunction_0: {1:, 2:, result:, break JSCompiler_inline_label_testFunction_0;}, 3;}",
        NodeUtil.toStringWithSourceInformation(resultNode));

    assertNull(resultNode.getLastChild());
  }

  @Test
  public void testMutateWithLoop() {
    AbstractCompiler compiler = new TestCompiler();

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, Suppliers.ofInstance(new IdGenerator()));

    String functionName = "testFunction";
    Node functionNode = NodeUtil.getFunctionNode(
        "function " + functionName + "() { for (var i = 0; i < 3; i++) { return i; } }");
    Node callNode = NodeUtil.getCallNode(functionName, NodeUtil.newIntLiteral(4));

    Node resultNode = mutator.mutate(functionName, functionNode, callNode, "result", true, true);

    assertEquals(
        "{JSCompiler_inline_label_testFunction_0: {i:, result:, break JSCompiler_inline_label_testFunction_0;}, 4;}",
        NodeUtil.toStringWithSourceInformation(resultNode));

    assertNull(resultNode.getLastChild());
  }

  @Test
  public void testMutateWithArguments() {
    AbstractCompiler compiler = new TestCompiler();

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, Suppliers.ofInstance(new IdGenerator()));

    String functionName = "testFunction";
    Node functionNode = NodeUtil.getFunctionNode(
        "function " + functionName + "(" + "a," + " b) { return a + b; }");
    Node callNode = NodeUtil.getCallNode(functionName, NodeUtil.newIntLiteral(3), NodeUtil.newIntLiteral(4));

    Node resultNode = mutator.mutate(functionName, functionNode, callNode, "result", true, false);

    assertEquals(
        "{JSCompiler_inline_label_testFunction_0: {a:, b:, result:, break JSCompiler_inline_label_testFunction_0;}, 3, 4;}",
        NodeUtil.toStringWithSourceInformation(resultNode));

    Node aNode = resultNode.getFirstChild();
    Node bNode = aNode.getNextSibling();
    assertEquals("a", aNode.getFirstChild().getString());
    assertEquals("b", bNode.getFirstChild().getString());
  }

  @Test
  public void testMutateWithoutResult() {
    AbstractCompiler compiler = new TestCompiler();

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, Suppliers.ofInstance(new IdGenerator()));

    String functionName = "testFunction";
    Node functionNode = NodeUtil.getFunctionNode(
        "function " + functionName + "() { return; }");
    Node callNode = NodeUtil.getCallNode(functionName, NodeUtil.newIntLiteral(3));

    Node resultNode = mutator.mutate(functionName, functionNode, callNode, null, false, false);

    assertEquals(
        "{JSCompiler_inline_label_testFunction_0: {break JSCompiler_inline_label_testFunction_0;}, 3;}",
        NodeUtil.toStringWithSourceInformation(resultNode));

    assertNull(resultNode.getLastChild());
  }

  private static class TestCompiler extends AbstractCompiler {

    @Override
    public Node compile(Nodes nodes, CompilationLevel compilationLevel,
        CodeInfo codeInfo, SourceFile sourceFile, boolean allowAll) {
      return null;
    }

    @Override
    public Node compile(Nodes nodes, CompilationLevel compilationLevel,
        CodeInfo codeInfo, SourceFile sourceFile, boolean allowAll,
        boolean allowAdvancedOptimizations) {
      return null;
    }

    @Override
    public Node makeTopLevel(Node node, SourceFile sourceFile,
        boolean isCompilationUnit) {
      return node;
    }

    @Override
    public Node makeTopLevel(List<Node> nodes, SourceFile sourceFile,
        boolean isCompilationUnit) {
      return NodeUtil.newBlockNode(nodes);
    }
  }

  private static class IdGenerator implements Supplier<String> {

    private int counter = 0;

    @Override
    public String get() {
      return Integer.toString(counter++);
    }
  }
}