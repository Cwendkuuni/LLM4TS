package com.google.javascript.jscomp.FunctionToBlockMutatorTest;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for the {@link FunctionToBlockMutator} class.
 */
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithNoArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "myFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList());
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, false, false);

    // TODO(user): Add assertions here to verify the mutatedBlock is as expected.
    // For example, you can compare it with a manually created expected block.
    assertThat(mutatedBlock).isNotNull();
  }

  @Test
  public void testMutateFunctionWithOneArgument() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "myFunction";
    String argumentName = "arg";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList(
        NodeUtil.newVarNode(argumentName, NodeUtil.newNumberNode(42))));
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, false, false);

    // TODO(user): Add assertions here to verify the mutatedBlock is as expected.
    // For example, you can compare it with a manually created expected block.
    assertThat(mutatedBlock).isNotNull();
  }

  @Test
  public void testMutateFunctionWithMultipleArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "myFunction";
    String arg1Name = "arg1";
    String arg2Name = "arg2";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList(
        NodeUtil.newVarNode(arg1Name, NodeUtil.newNumberNode(42)),
        NodeUtil.newVarNode(arg2Name, NodeUtil.newNumberNode(24))));
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, false, false);

    // TODO(user): Add assertions here to verify the mutatedBlock is as expected.
    // For example, you can compare it with a manually created expected block.
    assertThat(mutatedBlock).isNotNull();
  }

  @Test
  public void testMutateFunctionWithIsCallInLoopTrue() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "myFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList());
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, false, true);

    // TODO(user): Add assertions here to verify the mutatedBlock is as expected.
    // For example, you can compare it with a manually created expected block.
    assertThat(mutatedBlock).isNotNull();
  }
}