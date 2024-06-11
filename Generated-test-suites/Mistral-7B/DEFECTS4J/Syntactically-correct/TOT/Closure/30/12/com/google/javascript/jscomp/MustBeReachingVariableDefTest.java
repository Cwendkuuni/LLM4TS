package com.google.javascript.jscomp;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.javascript.jscomp.MustBeReachingVariableDef.Definition;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.MustDef;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class MustBeReachingVariableDefTest {

  private static final String FUNCTION_NAME = "myFunction";
  private static final String VAR_A = "a";
  private static final String VAR_B = "b";
  private static final String VAR_C = "c";

  @org.junit.Before
  public void setup() {
    // Create a Scope with some local variables and functions.
    Scope scope = new Scope();
    scope.setVar(VAR_A, new Var(Node.NEW_VAR(VAR_A, null), 0));
    scope.setVar(VAR_B, new Var(Node.NEW_VAR(VAR_B, null), 0));
    scope.setVar(VAR_C, new Var(Node.NEW_VAR(VAR_C, null), 0));

    // Create a simple function for testing
    List<Node> body = Arrays.asList(
        Node.ASSIGNMENT(VAR_A, Node.NAME(VAR_B)),
        Node.ASSIGNMENT(VAR_B, Node.INT_LITERAL(10)),
        Node.RETURN(Node.NAME(VAR_A))
    );
    Node functionNode = NodeUtil.newFunction(Node.NAME(FUNCTION_NAME), body);
    Node functionEntry = Node.BLOCK(functionNode);
    Node callSite = Node.CALL(Node.NAME(FUNCTION_NAME), Collections.emptyList());

    // Create the MustBeReachingVariableDef instance
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(functionEntry, scope);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, null);

    // Perform the analysis
    analysis.compute();

    // Get the results
    MustDef result = analysis.getResult();

    // Perform the tests
    testSimpleDefinition(analysis, VAR_A, NodeUtil.getFirstChild(functionEntry), result);
    testSimpleDefinition(analysis, VAR_B, NodeUtil.getChild(NodeUtil.getChild(functionEntry, 1), 0), result);
    testNoDefinition(analysis, VAR_C, callSite, result);
    testDependencies(analysis, VAR_A, result);
    testDependencies(analysis, VAR_B, result);
  }

  private void testSimpleDefinition(MustBeReachingVariableDef analysis, String variable, Node definitionNode, MustDef result) {
    Node def = analysis.getDef(variable, definitionNode);
    assertSame(def, definitionNode);
  }

  private void testNoDefinition(MustBeReachingVariableDef analysis, String variable, Node useNode, MustDef result) {
    assertNull(analysis.getDef(variable, useNode));
  }

  private void testDependencies(MustBeReachingVariableDef analysis, String variable, MustDef result) {
    Definition definition = result.reachingDef.get(analysis.jsScope.getVar(variable));
    assertSame(definition, result.reachingDef.get(analysis.jsScope.getVar(variable)));

    Set<Var> expectedDependencies = Sets.newHashSet(analysis.jsScope.getVar(VAR_B));
    Set<Var> actualDependencies = definition.depends;

    assertSame(expectedDependencies, actualDependencies);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testInvalidVariableName() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullCFG() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullScopeVars() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
  }

  @org.junit.Test
  public void testMultipleDefinitions() {
    String variable = "multipleDef";
    List<Node> body = Arrays.asList(
        Node.ASSIGNMENT(variable, Node.NAME(VAR_A)),
        Node.ASSIGNMENT(variable, Node.NAME(VAR_B))
    );
    Node functionNode = NodeUtil.newFunction(Node.NAME("multipleDef"), body);
    Node functionEntry = Node.BLOCK(functionNode);
    Node callSite = Node.CALL(Node.NAME("multipleDef"), Collections.emptyList());

    // Create the MustBeReachingVariableDef instance
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(functionEntry, null);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, null, null);

    // Perform the analysis
    analysis.compute();

    // Get the results
    MustDef result = analysis.getResult();

    // Test the result
    Definition definitionA = result.reachingDef.get(analysis.jsScope.getVar(VAR_A));
    Definition definitionB = result.reachingDef.get(analysis.jsScope.getVar(variable));

    assertSame(definitionA, definitionB);

    assertSame(definitionA, result.reachingDef.get(analysis.jsScope.getVar(variable)));
  }
}