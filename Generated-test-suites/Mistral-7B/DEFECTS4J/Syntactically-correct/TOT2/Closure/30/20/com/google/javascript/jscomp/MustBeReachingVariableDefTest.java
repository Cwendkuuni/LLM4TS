import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.cfg.ControlFlowGraph.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  private static AbstractCompiler compiler;
  private static Scope jsScope;
  private static ControlFlowGraph<Node> cfg;

  @BeforeClass
  public static void setup() {
    // Setup the compiler, scope, and control flow graph for testing.
    // Replace this with your setup code.
    compiler = new AbstractCompiler();
    jsScope = compiler.getGlobalScope();
    cfg = new CFG<Node>(compiler.getEntryNode(), jsScope);
  }

  @Test
  public void testBasicFunctionality() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add your test cases here.
    // For example:
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, "test"), ImmutableSet.of());
    Node useNode = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node definitionNode = new Node(Token.ASSIGN, new Node(Token.NAME, "result"), new Node(Token.NAME, "x"));
    Node entryNode = new Node(Token.BLOCK);

    entryNode.appendChild(definitionNode);
    entryNode.appendChild(functionNode);
    entryNode.appendChild(useNode);

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());
    Node def = analysis.getDef("x", useNode);
    assertNotNull(def);
    assertEquals(definitionNode, def);
  }

  @Test
  public void testComplexControlFlow() {
    // Add your test cases here.
    // For example:
    Node ifNode = new Node(Token.IF, new Node(Token.NAME, "condition"));
    Node thenNode = new Node(Token.BLOCK);
    Node elseNode = new Node(Token.BLOCK);
    Node definitionNode1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node definitionNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "z"));
    Node useNode1 = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node useNode2 = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));

    ifNode.appendChild(new Node(Token.NAME, "condition"));
    ifNode.appendChild(thenNode);
    ifNode.appendChild(elseNode);

    thenNode.appendChild(definitionNode1);
    thenNode.appendChild(new Node(Token.NAME, "useX1"));
    elseNode.appendChild(definitionNode2);
    elseNode.appendChild(new Node(Token.NAME, "useX2"));

    Node entryNode = new Node(Token.BLOCK);
    entryNode.appendChild(ifNode);
    entryNode.appendChild(new Node(Token.NAME, "useX1"));
    entryNode.appendChild(new Node(Token.NAME, "useX2"));

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());

    Node def1 = analysis.getDef("x", useNode1);
    Node def2 = analysis.getDef("x", useNode2);

    // Check the results here.
  }

  @Test
  public void testEdgeCases() {
    // Add your test cases for edge cases here.
    // For example:
    Node definitionNode1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node definitionNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "z"));
    Node useNode1 = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node useNode2 = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node conditionalDefinitionNode = new Node(Token.IF, new Node(Token.NAME, "condition"));
    Node conditionalUseNode = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));

    conditionalDefinitionNode.appendChild(new Node(Token.NAME, "condition"));
    conditionalDefinitionNode.appendChild(definitionNode1);
    conditionalDefinitionNode.appendChild(definitionNode2);

    Node mainDefinitionNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node mainUseNode = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));

    Node entryNode = new Node(Token.BLOCK);
    entryNode.appendChild(conditionalDefinitionNode);
    entryNode.appendChild(mainDefinitionNode);
    entryNode.appendChild(useNode1);
    entryNode.appendChild(useNode2);

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());

    Node def1 = analysis.getDef("x", useNode1);
    Node def2 = analysis.getDef("x", useNode2);

    // Check the results here.
    // For example:
    // assertEquals(definitionNode1, def1);
    // assertEquals(definitionNode2, def2);
  }

  @Test
  public void testExceptionHandling() {
    // Add your test cases for exception handling scenarios here.
    // For example:
    Node tryNode = new Node(Token.TRY);
    Node catchNode = new Node(Token.CATCH, new Node(Token.NAME, "exception"));
    Node definitionNode1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node definitionNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "z"));
    Node useNode1 = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node useNode2 = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node throwNode = new Node(Token.THROW, new Node(Token.NAME, "exception"));
    Node entryNode = new Node(Token.BLOCK);

    tryNode.appendChild(definitionNode1);
    tryNode.appendChild(definitionNode2);
    tryNode.appendChild(throwNode);
    tryNode.appendChild(catchNode);

    catchNode.appendChild(new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "result")));
    catchNode.appendChild(new Node(Token.NAME, "useX"));

    entryNode.appendChild(tryNode);
    entryNode.appendChild(useNode1);
    entryNode.appendChild(useNode2);

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());

    Node def1 = analysis.getDef("x", useNode1);
    Node def2 = analysis.getDef("x", useNode2);

    // Check the results here.
    // For example:
    // assertNotNull(def1);
    // assertNotNull(def2);
    // assertEquals(definitionNode1, def1);
    // assertEquals(definitionNode2, def2);
  }

  @Test
  public void testEscapedVariables() {
    // Add your test cases for escaped variables here.
    // For example:
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, "test"), ImmutableSet.of());
    Node definitionNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node useNode = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node escapedVariableDefinition = new Node(Token.ASSIGN, new Node(Token.NAME, "z"), new Node(Token.NAME, "x"));
    Node escapedVariableUse = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));

    functionNode.appendChild(definitionNode);
    functionNode.appendChild(new Node(Token.NAME, "useX"));
    functionNode.appendChild(escapedVariableDefinition);
    functionNode.appendChild(escapedVariableUse);

    Node entryNode = new Node(Token.BLOCK);
    entryNode.appendChild(new Node(Token.NAME, "callFunction"));

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());

    Node def = analysis.getDef("z", escapedVariableUse);
    assertNull(def);
  }

  @Test
  public void testVariablesInOuterScope() {
    // Add your test cases for variables in outer scope here.
    // For example:
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME, "test"), ImmutableSet.of());
    Node definitionNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node useNode = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));
    Node outerScopeDefinitionNode = new Node(Token.ASSIGN, new Node(Token.NAME, "outerX"), new Node(Token.NAME, "x"));
    Node outerScopeUse = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "outerResult"));

    functionNode.appendChild(definitionNode);
    functionNode.appendChild(new Node(Token.NAME, "useX"));
    functionNode.appendChild(outerScopeDefinitionNode);
    functionNode.appendChild(outerScopeUse);

    Node entryNode = new Node(Token.BLOCK);
    entryNode.appendChild(new Node(Token.NAME, "callFunction"));

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());

    boolean dependsOnOuterScope = analysis.dependsOnOuterScopeVars("x", useNode);
    assertTrue(dependsOnOuterScope);

    dependsOnOuterScope = analysis.dependsOnOuterScopeVars("outerX", outerScopeUse);
    assertTrue(dependsOnOuterScope);

    dependsOnOuterScope = analysis.dependsOnOuterScopeVars("x", outerScopeUse);
    assertFalse(dependsOnOuterScope);
  }

  @Test
  public void testConditionalDefinitions() {
    // Add your test cases for conditional definitions here.
    // For example:
    Node conditionNode = new Node(Token.IF, new Node(Token.NAME, "condition"));
    Node definitionNode1 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
    Node definitionNode2 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NAME, "z"));
    Node useNode = new Node(Token.ACCESS_PROPERTY, new Node(Token.NAME, "result"));

    conditionNode.appendChild(new Node(Token.NAME, "condition"));
    conditionNode.appendChild(new Node(Token.BLOCK));
    conditionNode.getLastChild().appendChild(definitionNode1);
    conditionNode.getLastChild().appendChild(definitionNode2);
    conditionNode.getLastChild().appendChild(new Node(Token.NAME, "useX"));

    Node entryNode = new Node(Token.BLOCK);
    entryNode.appendChild(conditionNode);
    entryNode.appendChild(new Node(Token.NAME, "useX"));

    GraphNode<Node, Branch> entryGraphNode = new GraphNode<>(entryNode, null, null);
    entryGraphNode.setEntryNode();
    cfg.addNode(entryGraphNode);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.flowThrough(entryNode, analysis.createInitialEstimateLattice());

    Node def1 = analysis.getDef("x", useNode);
    Node def2 = analysis.getDef("x", useNode);

    // Check the results here.
    // For example:
    // assertEquals(definitionNode1, def1);
    // assertEquals(definitionNode2, def2);
  }
}