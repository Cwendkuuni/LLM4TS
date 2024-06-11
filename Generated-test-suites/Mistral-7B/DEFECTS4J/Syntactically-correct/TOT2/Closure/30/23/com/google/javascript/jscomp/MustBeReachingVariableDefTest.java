import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Assuming the setup of ControlFlowGraph, Scope, and AbstractCompiler is
    // done elsewhere.
    this.cfg = new ControlFlowGraph<Node>(new Node(), new Node());
    this.jsScope = new Scope();
    this.compiler = new AbstractCompiler();
  }

  @Test
  public void testNormalFlow() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = createTestNode(Token.NAME, "a");
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    cfg.addEdge(definitionNode, useNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition node to be the same.", definitionNode, actualDef);
  }

  @Test
  public void testMultipleDefinitions() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = createTestNode(Token.NAME, "a");
    Node definitionNode1 = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    Node definitionNode2 = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(20));
    cfg.addEdge(definitionNode1, useNode);
    cfg.addEdge(definitionNode2, useNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    // Bottom is used when there is more than one definition.
    assertNull("Expected definition node to be null.", actualDef);
  }

  @Test
  public void testUseBeforeDefinition() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    Node useNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    cfg.addEdge(useNode, definitionNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    assertNull("Expected definition node to be null.", actualDef);
  }

  @Test
  public void testUseBeforeDefinitionInConditional() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node conditionNode = createTestNode(Token.IF, null, null, NodeUtil.createBool(true));
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    Node useNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    cfg.addEdge(useNode, definitionNode);
    cfg.addEdge(conditionNode, useNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    assertNull("Expected definition node to be null.", actualDef);
  }

  @Test
  public void testUseAfterDefinition() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    Node useNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    cfg.addEdge(definitionNode, useNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition node to match.", definitionNode, actualDef);
  }

  @Test 
  public void testUseBeforeDefinitionInLoop() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node loopConditionNode = createTestNode(Token.WHILE, null, null, NodeUtil.createBool(true));
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    Node useNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    cfg.addEdge(useNode, definitionNode);
    cfg.addEdge(loopConditionNode, useNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    assertNull("Expected definition node to be null.", actualDef);
  }

  @Test 
  public void testUseAfterDefinitionInLoop() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node loopConditionNode = createTestNode(Token.WHILE, null, null, NodeUtil.createBool(true));
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    Node useNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    cfg.addEdge(definitionNode, useNode);
    cfg.addEdge(useNode, loopConditionNode);
    analysis.compute();
    Node actualDef = analysis.getDef("a", useNode);
    assertEquals("Expected definition node to match.", definitionNode, actualDef);
  }

  @Test 
  public void testVariablesInDifferentScopes() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);
    innerScope.declareVar("a", NodeUtil.createInt(10));
    Node innerUseNode = createTestNode(Token.NAME, "a");
    Node innerDefinitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    innerScope.setRootNode(innerDefinitionNode);
    outerScope.setRootNode(innerUseNode);
    cfg.setRootNode(outerScope.getRootNode());
    analysis.compute();
    Node actualDef = analysis.getDef("a", innerUseNode);
    assertEquals("Expected definition node to match.", innerDefinitionNode, actualDef);
  }

  @Test 
  public void testUseBeforeDefinitionInDifferentScopes() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);
    innerScope.declareVar("a", NodeUtil.createInt(10));
    Node innerDefinitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    Node innerUseNode = createTestNode(Token.NAME, "a");
    innerScope.setRootNode(innerUseNode);
    outerScope.setRootNode(innerDefinitionNode);
    cfg.setRootNode(outerScope.getRootNode());
    analysis.compute();
    Node actualDef = analysis.getDef("a", innerUseNode);
    assertNull("Expected definition node to be null.", actualDef);
  }

  @Test 
  public void testVariablesDependingOnEachOther() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode1 = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    Node definitionNode1 = createTestNode(Token.ASSIGN, null, "b", NodeUtil.createInt(10));
    Node useNode2 = createTestNode(Token.ASSIGN, null, "b", NodeUtil.createInt(5));
    Node definitionNode2 = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(10));
    cfg.addEdge(useNode1, definitionNode1);
    cfg.addEdge(useNode2, definitionNode2);
    analysis.compute();
    Node actualDef1 = analysis.getDef("a", useNode1);
    Node actualDef2 = analysis.getDef("b", useNode2);
    assertEquals("Expected definition node a to match.", definitionNode1, actualDef1);
    assertEquals("Expected definition node b to match.", definitionNode2, actualDef2);
  }

  @Test(expected = RuntimeException.class)
  public void testExceptionScenario() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = createTestNode(Token.NAME, "a");
    Node definitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createThrow(NodeUtil.createInt(1)));
    cfg.addEdge(definitionNode, useNode);
    analysis.compute();
  }

  @Test
  public void testDependencies() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    String name = "a";
    Node useNode = createTestNode(Token.NAME, name);
    Node definitionNode = createTestNode(Token.ASSIGN, null, name, NodeUtil.createInt(10));
    Node rValueNode = createTestNode(Token.NAME, "b");
    analysis.compute();
    Definition def = analysis.getDataFlowState(useNode).getIn();
    Set<Var> dependencies = def.reachingDef.keySet();
    assertEquals("Expected dependency set to have size 1.", 1, dependencies.size());
    assertTrue("Expected dependency set to contain variable 'b'.", dependencies.contains(jsScope.getVar(name)));
    assertTrue("Expected dependency set to contain variable 'a'.", dependencies.contains(jsScope.getVar(name)));
  }

  @Test
  public void testUseBeforeDefinitionInScopeWithEscapedVariables() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);
    innerScope.declareVar("a", NodeUtil.createInt(10));
    innerScope.declareVar("b", NodeUtil.createInt(20), ImmutableSet.of("a"));
    Node innerUseNode = createTestNode(Token.NAME, "a");
    Node innerDefinitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    innerScope.setRootNode(innerUseNode);
    outerScope.setRootNode(innerDefinitionNode);
    cfg.setRootNode(outerScope.getRootNode());
    analysis.compute();
    Node actualDef = analysis.getDef("a", innerUseNode);
    assertEquals("Expected definition node to match.", innerDefinitionNode, actualDef);
  }

  @Test
  public void testUseAfterDefinitionInScopeWithEscapedVariables() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);
    innerScope.declareVar("a", NodeUtil.createInt(10), ImmutableSet.of("b"));
    Node innerDefinitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    Node innerUseNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(1));
    innerScope.setRootNode(innerUseNode);
    outerScope.setRootNode(innerDefinitionNode);
    cfg.setRootNode(outerScope.getRootNode());
    analysis.compute();
    Node actualDef = analysis.getDef("a", innerUseNode);
    assertEquals("Expected definition node to match.", innerDefinitionNode, actualDef);
  }

  @Test
  public void testVariablesEscaped() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);
    innerScope.declareVar("a", NodeUtil.createInt(10), ImmutableSet.of("b"));
    Node innerDefinitionNode = createTestNode(Token.ASSIGN, null, "a", NodeUtil.createInt(5));
    Node innerUseNode = createTestNode(Token.ASSIGN, null, "b", NodeUtil.createInt(1));
    innerScope.setRootNode(innerUseNode);
    outerScope.setRootNode(innerDefinitionNode);
    cfg.setRootNode(outerScope.getRootNode());
    analysis.compute();
    Node actualDef = analysis.getDef("a", innerUseNode);
    assertNull("Expected definition node to be null.", actualDef);
  }

  private Node createTestNode(int type, @Nullable Node parent, String name,
      @Nullable Node rValue) {
    return NodeFactory.createNodeFromText(
        "test", type, parent, name, rValue);
  }
}