package com.google.javascript.jscomp.test.dataflow;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Create mock scope with some variables
    Scope mockScope = mock(Scope.class);
    when(mockScope.getVarCount()).thenReturn(3);
    when(mockScope.getVar(0)).thenReturn(new Var("a", 0));
    when(mockScope.getVar(1)).thenReturn(new Var("b", 1));
    when(mockScope.getVar(2)).thenReturn(new Var("c", 2));

    // Create mock control flow graph
    ControlFlowGraph<Node> mockCfg = mock(ControlFlowGraph.class);
    when(mockCfg.getOutEdges(NodeUtil.getFunctionDeclarationNode("test")))
        .thenReturn(ImmutableList.of(
            new DiGraphEdge<>(NodeUtil.getFunctionDeclarationNode("test"),
                NodeUtil.getStatementNode("if (x > 0)"))));

    // Perform live variables analysis
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(mockCfg, mockScope,
        new CompilerOptions());

    // Test empty function
    LiveVariableLattice emptyFunctionResult = analysis.computeFixpoint(
        NodeUtil.getFunctionDeclarationNode("emptyFunction"));
    assertThat(emptyFunctionResult.isLive(0)).isTrue();
    assertThat(emptyFunctionResult.isLive(1)).isTrue();
    assertThat(emptyFunctionResult.isLive(2)).isTrue();

    // Test simple if statement
    LiveVariableLattice ifStatementResult = analysis.computeFixpoint(
        NodeUtil.getStatementNode("if (x > 0)"));
    assertThat(ifStatementResult.isLive(0)).isTrue();
    assertThat(ifStatementResult.isLive(1)).isTrue();
    assertThat(ifStatementResult.isLive(2)).isFalse();

    // Test if statement with else branch
    when(mockCfg.getOutEdges(NodeUtil.getStatementNode("if (x > 0)")))
        .thenReturn(ImmutableList.of(
            new DiGraphEdge<>(NodeUtil.getStatementNode("if (x > 0)"),
                NodeUtil.getStatementNode("x += 1")),
            new DiGraphEdge<>(NodeUtil.getStatementNode("if (x > 0)"),
                NodeUtil.getStatementNode("else"))));

    LiveVariableLattice ifElseStatementResult = analysis.computeFixpoint(
        NodeUtil.getStatementNode("if (x > 0)"));
    assertThat(ifElseStatementResult.isLive(0)).isTrue();
    assertThat(ifElseStatementResult.isLive(1)).isTrue();
    assertThat(ifElseStatementResult.isLive(2)).isTrue();

    LiveVariableLattice elseStatementResult = analysis.computeFixpoint(
        NodeUtil.getStatementNode("else"));
    assertThat(elseStatementResult.isLive(0)).isFalse();
    assertThat(elseStatementResult.isLive(1)).isFalse();
    assertThat(elseStatementResult.isLive(2)).isFalse();

    // Test while loop
    when(mockCfg.getOutEdges(NodeUtil.getStatementNode("while (x > 0)")))
        .thenReturn(ImmutableList.of(
            new DiGraphEdge<>(NodeUtil.getStatementNode("while (x > 0)"),
                NodeUtil.getStatementNode("x -= 1"))));

    LiveVariableLattice whileLoopResult = analysis.computeFixpoint(
        NodeUtil.getStatementNode("while (x > 0)"));
    assertThat(whileLoopResult.isLive(0)).isTrue();
    assertThat(whileLoopResult.isLive(1)).isTrue();
    assertThat(whileLoopResult.isLive(2)).isTrue();

    // Test function call
    when(mockCfg.getOutEdges(NodeUtil.getStatementNode("callFunction()")))
        .thenReturn(ImmutableList.of(
            new DiGraphEdge<>(NodeUtil.getStatementNode("callFunction()"),
                NodeUtil.getStatementNode("x += 1"))));

    LiveVariableLattice functionCallResult = analysis.computeFixpoint(
        NodeUtil.getStatementNode("callFunction()"));
    assertThat(functionCallResult.isLive(0)).isTrue();
    assertThat(functionCallResult.isLive(1)).isTrue();
    assertThat(functionCallResult.isLive(2)).isTrue();
  }
}