package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.common.collect.*;

public class LiveVariablesAnalysisTest {
  @Test
  public void testLiveVariablesAnalysis() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.run();

    // Then
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(5);
    expected.liveSet.set(0); // variable 0 is live
    expected.liveSet.set(1); // variable 1 is live
    expected.liveSet.set(2); // variable 2 is live
    expected.liveSet.set(4); // variable 4 is live
    assertEquals(expected, result);
  }

  @Test
  public void testDeadVariables() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    analysis.computeEscaped(scope, analysis.escaped, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.run();
    int deadVariableIndex = 3;

    // Then
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(5);
    expected.liveSet.clear(deadVariableIndex); // variable 3 is dead
    assertEquals(expected, result);
  }

  @Test(expected = AssertionError.class)
  public void testDivisionByZero() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    analysis.run();

    // Then
    // This test case should throw an AssertionError due to division by zero
  }

  // Test for escaped variables
  @Test
  public void testEscapedVariables() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    analysis.computeEscaped(scope, analysis.escaped, null);

    // Then
    Set<Var> expectedEscapedVariables = Sets.newHashSet(
            scope.getVar("x"), scope.getVar("y")
    );
    assertEquals(expectedEscapedVariables, analysis.getEscapedLocals());
  }

  // Test for invalid scope
  @Test
  public void testInvalidScope() {
    // Given
    Scope scope = null;
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When and Then
    // This test case should not compile due to a NullPointerException
  }

  // Test for dead variables
  @Test
  public void testDeadVariablesInLoop() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    Node loopNode = new Node(Token.WHILE);
    Node conditionNode = new Node(Token.NAME, "condition");
    Node loopBodyNode = new Node(Token.BLOCK);
    Node deadVariableNode = new Node(Token.VAR, "deadVariable");
    loopBodyNode.addChild(deadVariableNode);
    loopBodyNode.addChild(new Node(Token.ASSIGN, NodeUtil.getIdentifier("deadVariable"), new Node(Token.NAME, "value")));
    loopNode.addChild(conditionNode);
    loopNode.addChild(loopBodyNode);
    cfg.addNode(loopNode);
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(5);
    expected.liveSet.clear(3); // variable 3 (deadVariable) is dead

    // When
    analysis.computeEscaped(scope, analysis.escaped, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.run();

    // Then
    assertEquals(expected, result);
  }

  // Test for exceptions
  @Test
  public void testExceptionHandling() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    Node tryNode = new Node(Token.TRY);
    Node catchNode = new Node(Token.CATCH, NodeUtil.getIdentifier("exceptionType"), new Node(Token.BLOCK));
    Node throwNode = new Node(Token.THROW);
    tryNode.addChild(new Node(Token.BLOCK));
    tryNode.addChild(throwNode);
    catchNode.addChild(new Node(Token.VAR, "catchVariable"));
    tryNode.addChild(catchNode);
    cfg.addNode(tryNode);

    // Then
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(5);
    expected.liveSet.set(0); // variable 0 is live
    expected.liveSet.set(1); // variable 1 is live
    expected.liveSet.set(2); // variable 2 is live
    expected.liveSet.set(4); // variable 4 is live
    expected.liveSet.set(5); // variable 5 (catchVariable) is live

    // When
    analysis.computeEscaped(scope, analysis.escaped, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.run();

    // Then
    assertEquals(expected, result);
  }
}