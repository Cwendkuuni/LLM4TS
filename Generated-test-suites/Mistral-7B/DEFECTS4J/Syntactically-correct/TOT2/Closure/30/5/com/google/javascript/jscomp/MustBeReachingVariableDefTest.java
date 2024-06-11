import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class MustBeReachingVariableDefTest {

  @org.junit.Test
  public void testSimpleAssignment() {
    // Given a simple assignment
    String jsCode = "function foo() { var x = 1; }";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When analyzing the code
    MustDef result = analyzer.computeResult();

    // Then the variable x should have its definition at the assignment node
    Node definitionNode = analyzer.getDef("x", cfg.getRootNode());
    assertThat(definitionNode).isNotNull();

    // And it should not depend on any outer variable
    assertThat(analyzer.dependsOnOuterScopeVars("x", cfg.getRootNode())).isFalse();
  }

  @org.junit.Test
  public void testConditionalAssignment() {
    // Given a conditional assignment
    String jsCode = "function foo() { var x; if (true) { x = 1; } }";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When analyzing the code
    MustDef result = analyzer.computeResult();

    // Then the variable x should have its definition at the assignment node when the condition is true
    Node definitionNode = analyzer.getDef("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getTrueBranch()));
    assertThat(definitionNode).isNotNull();

    // And it should not depend on any outer variable
    assertThat(analyzer.dependsOnOuterScopeVars("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getTrueBranch()))).isFalse();

    // And the variable x should not have a definition when the condition is false
    definitionNode = analyzer.getDef("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getFalseBranch()));
    assertThat(definitionNode).isNull();
  }

  @org.junit.Test
  public void testComplexControlFlow() {
    // Given a complex control flow
    String jsCode = "function foo() { if (true) { if (false) { var x = 1; } } else { var x = 2; } }";
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When analyzing the code
    MustDef result = analyzer.computeResult();

    // Then the variable x should have its definition at the first assignment node when the condition in the outer if is true
    Node definitionNode1 = analyzer.getDef("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getTrueBranch()));
    assertThat(definitionNode1).isNotNull();

    // And the variable x should have its definition at the second assignment node when the condition in the outer if is false
    Node definitionNode2 = analyzer.getDef("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getFalseBranch()));
    assertThat(definitionNode2).isNotNull();

    // And the variable x should not depend on any outer variable in both cases
    assertThat(analyzer.dependsOnOuterScopeVars("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getTrueBranch()))).isFalse();
    assertThat(analyzer.dependsOnOuterScopeVars("x", cfg.getNode(cfg.getConditionalBranch(Token.IF, 0).getFalseBranch()))).isFalse();
  }

  @org.junit.Test
  public void testEscapedVariable() {
    // Given a function with an escaped variable
    String jsCode = "function foo() { var x = 1; function bar() { return x; } }";
    Scope jsScope = new Scope();
    Scope barScope = new Scope(jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(jsCode, jsScope, compiler);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // When analyzing the code
    MustDef result = analyzer.computeResult();

    // Then the variable x should not have a definition in the function bar
    Node definitionNode = analyzer.getDef("x", cfg.getNode(cfg.getFunctionNode("bar").getBody()));
    assertThat(definitionNode).isNull();
  }
}