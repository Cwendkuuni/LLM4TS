package com.google.javascript.jscomp;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef_SimpleAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = createAssignmentNode("a = b;", jsScope);
    analysis.compute();

    Node def = analysis.getDef("a", rootNode);
    assertThat(def).isNotNull();
    assertThat(def.getLineNumber()).isEqualTo(1);

    Node useNode = createAssignmentNode("c = a;", jsScope);
    Node useDef = analysis.getDef("a", useNode);
    Node useUseDef = analysis.getDef("c", useNode);
    assertThat(useDef).isSameInstanceAs(def);
    assertThat(useUseDef).isSameInstanceAs(def);
  }

  @Test
  public void testMustBeReachingVariableDef_ConditionalAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = createConditionalAssignmentNode("if (x > 5) { a = b; }", jsScope);
    analysis.compute();

    Node def = analysis.getDef("a", rootNode);
    assertThat(def).isNotNull();
    assertThat(def.getLineNumber()).isEqualTo(2);

    Node useNode = createAssignmentNode("c = a;", jsScope);
    Node useDef = analysis.getDef("a", useNode);
    Node useUseDef = analysis.getDef("c", useNode);
    assertThat(useDef).isSameInstanceAs(def);
    assertThat(useUseDef).isSameInstanceAs(def);
  }

  @Test
  public void testMustBeReachingVariableDef_MultipleDefinitions() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = createMultipleDefinitionsNode(jsScope);
    analysis.compute();

    Node def1 = analysis.getDef("a", rootNode);
    Node def2 = analysis.getDef("a", rootNode);
    assertThat(def1).isNotNull();
    assertThat(def1.getLineNumber()).isEqualTo(3);
    assertThat(def2).isNull();

    Node useNode = createAssignmentNode("c = a;", jsScope);
    Node useDef = analysis.getDef("a", useNode);
    Node useUseDef = analysis.getDef("c", useNode);
    assertThat(useDef).isSameInstanceAs(def1);
    assertThat(useUseDef).isSameInstanceAs(def1);
  }

  @Test
  public void testMustBeReachingVariableDef_EscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = createEscapedVariableNode(jsScope);
    analysis.compute();

    Node def = analysis.getDef("x", rootNode);
    assertThat(def).isNull();

    Node useNode = createAssignmentNode("y = x;", jsScope);
    Node useDef = analysis.getDef("x", useNode);
    Node useUseDef = analysis.getDef("y", useNode);
    assertThat(useDef).isNull();
    assertThat(useUseDef).isNull();
  }

  @Test
  public void testMustBeReachingVariableDef_OuterScopeVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node outerScopeRootNode = createOuterScopeVariableNode(jsScope);
    analysis.compute();

    Node def = analysis.getDef("a", outerScopeRootNode);
    Node useNode = createAssignmentNode("b = a;", jsScope);
    Node useDef = analysis.getDef("a", useNode);
    Node useUseDef = analysis.getDef("b", useNode);
    assertThat(def).isNotNull();
    assertThat(def.getLineNumber()).isEqualTo(1);
    assertThat(useDef).isSameInstanceAs(def);
    assertThat(useUseDef).isSameInstanceAs(def);

    // Check that outer scope variable is not reachable
    Node useNode2 = createAssignmentNode("c = a;", jsScope);
    Node useDef2 = analysis.getDef("a", useNode2);
    Node useUseDef2 = analysis.getDefinition("c", useNode2);
    Node outerScopeUseDef = analysis.getDefinition("a", useNode2);
    assertThat(useDef2).isNull();
    assertThat(useUseDef2).isNull();
    assertThat(outerScopeUseDef).isNull();
  }

  private Node createAssignmentNode(String expression, Scope jsScope) {
    NodeAST ast = new NodeAST();
    ast.parseAndReturnRootNode(expression);
    Node node = ast.getRootNode();
    Preconditions.checkState(node.isAssign());
    return node;
  }

  private Node createConditionalAssignmentNode(String expression, Scope jsScope) {
    NodeAST ast = new NodeAST();
    ast.parseAndReturnRootNode(expression);
    Node node = ast.getRootNode();
    Preconditions.checkState(node.getType() == Token.IF);
    Node assignmentNode = node.getFirstChild().getNext();
    Preconditions.checkState(assignmentNode.isAssign());
    return assignmentNode;
  }

  private Node createMultipleDefinitionsNode(Scope jsScope) {
    String code = "var a = 1;\n" +
                  "var a = 2;\n";
    NodeAST ast = new NodeAST();
    Node rootNode = ast.parseAndReturnRootNode(code);
    return rootNode;
  }

  private Node createEscapedVariableNode(Scope jsScope) {
    String code = "var x = arguments[0];\n" +
                  "for (var i = 0; i < arguments.length; i++) {\n" +
                  "  if (arguments[i] === x) {\n" +
                  "    x = i;\n" +
                  "  }\n" +
                  "}";
    NodeAST ast = new NodeAST();
    Node rootNode = ast.parseAndReturnRootNode(code);
    return rootNode;
  }

  private Node createOuterScopeVariableNode(Scope jsScope) {
    String code = "function outerScopeVariable() {\n" +
                  "  var a = 1;\n" +
                  "  function innerFunction() {\n" +
                  "    var b = a;\n" +
                  "  }\n" +
                  "  innerFunction();\n" +
                  "}\n" +
                  "outerScopeVariable();";
    NodeAST ast = new NodeAST();
    Node rootNode = ast.parseAndReturnRootNode(code);
    return rootNode;
  }
}