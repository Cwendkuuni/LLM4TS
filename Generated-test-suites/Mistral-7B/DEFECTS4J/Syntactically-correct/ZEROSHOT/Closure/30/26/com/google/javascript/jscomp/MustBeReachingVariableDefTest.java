import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.MustDefJoin;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @SuppressWarnings("unchecked")
  private static <T extends LatticeElement<T>> T createMustDefInstance(
      Class<T> mustDefClass) {
    try {
      return (T) new MustDef();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends LatticeElement<T>> T createMustDefJoinInstance() {
    try {
      return (T) new MustDefJoin();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends LatticeElement<T>> T createEmptyMustDefInstance() {
    try {
      return (T) new MustDef();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static ControlFlowGraph<Node> createControlFlowGraph() {
    return new ControlFlowGraph<Node>() {};
  }

  @SuppressWarnings("unchecked")
  private static Scope createScope() {
    return new Scope() {};
  }

  @SuppressWarnings("unchecked")
  private static AbstractCompiler createAbstractCompiler() {
    return new AbstractCompiler() {};
  }

  @SuppressWarnings("unchecked")
  private static MustBeReachingVariableDef createMustBeReachingVariableDefInstance(
      ControlFlowGraph<Node> cfg, Scope jsScope, AbstractCompiler compiler) {
    try {
      return new MustBeReachingVariableDef(cfg, jsScope, compiler);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCreateEntryLattice() {
    MustDef entryLattice = createMustDefInstance(MustDef.class);
    assertEquals(entryLattice.reachingDef.size(), 1);
    for (Var var : Arrays.asList(new Var[0])) {
      assertEquals(null, entryLattice.reachingDef.get(var));
    }
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MustDef initialEstimateLattice = createEmptyMustDefInstance();
    assertEquals(initialEstimateLattice.reachingDef.size(), 0);
  }

  @Test
  public void testFlowThroughForward() {
    MustDef input = createMustDefInstance(MustDef.class);
    MustDef output = createMustDefInstance(MustDef.class);
    Node node = Node.newIntLiteral(1);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            createControlFlowGraph(), createScope(), createAbstractCompiler());
    mustBeReachingVariableDef.flowThrough(node, input, output);

    assertEquals(output.reachingDef.size(), 1);
    assertEquals(output.reachingDef.get(new Var("")), null);
  }

  @Test
  public void testFlowThroughForwardWithDependencies() {
    MustDef input = createMustDefInstance(MustDef.class);
    MustDef output = createMustDefInstance(MustDef.class);
    Node variableNode = Node.newName("x");
    Node assignmentNode = Node.newAssIGN(Token.ASSIGN, variableNode,
        Node.newIntLiteral(1));

    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            createControlFlowGraph(), createScope(), createAbstractCompiler());
    mustBeReachingVariableDef.flowThrough(assignmentNode, input, output);

    assertEquals(output.reachingDef.size(), 1);
    assertEquals(output.reachingDef.get(new Var("x")), variableNode);
  }

  @Test
  public void testFlowThroughForwardWithMultipleDefinitions() {
    MustDef input = createMustDefInstance(MustDef.class);
    MustDef output = createMustDefInstance(MustDef.class);
    Node xDefNode1 = Node.newName("x");
    Node xDefNode2 = Node.newName("x");
    Node assignmentNode1 = Node.newAssIGN(Token.ASSIGN, xDefNode1,
        Node.newIntLiteral(1));
    Node assignmentNode2 = Node.newAssIGN(Token.ASSIGN, xDefNode2,
        Node.newIntLiteral(2));

    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            createControlFlowGraph(), createScope(), createAbstractCompiler());
    mustBeReachingVariableDef.flowThrough(assignmentNode1, input, output);
    mustBeReachingVariableDef.flowThrough(assignmentNode2, output, output);

    assertEquals(output.reachingDef.get(new Var("x")), null);
  }

  @Test
  public void testComputeEscaped() {
    Scope scope = createScope();
    AbstractCompiler abstractCompiler = createAbstractCompiler();
    Set<Var> escapedVariables = new HashSet<>();
    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            createControlFlowGraph(), scope, abstractCompiler);
    mustBeReachingVariableDef.computeEscaped(scope, escapedVariables,
        abstractCompiler);

    assertEquals(escapedVariables.size(), 0);
  }

  @Test
  public void testComputeEscapedWithOneEscapedVariable() {
    Scope scope = createScope();
    AbstractCompiler abstractCompiler = createAbstractCompiler();
    Set<Var> escapedVariables = new HashSet<>();
    Var escapedVariable = new Var("") {
      @Override
      public Node getRootNode() {
        return Node.newName("escapedVariable");
      }
    };
    scope.putVar(escapedVariable.getName(), escapedVariable);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            createControlFlowGraph(), scope, abstractCompiler);
    mustBeReachingVariableDef.computeEscaped(scope, escapedVariables,
        abstractCompiler);

    assertTrue(escapedVariables.contains(escapedVariable));
  }

  @Test
  public void testGetDef() {
    ControlFlowGraph<Node> controlFlowGraph = createControlFlowGraph();
    Scope scope = createScope();
    AbstractCompiler abstractCompiler = createAbstractCompiler();
    Node xDefinitionNode = Node.newName("x");
    Node xUseNode = Node.newName("x");
    Node xDefinition = Node.newAssIGN(Token.ASSIGN, xDefinitionNode,
        Node.newIntLiteral(1));
    controlFlowGraph.addEdge(xDefinitionNode, xUseNode);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            controlFlowGraph, scope, abstractCompiler);
    mustBeReachingVariableDef.computeEscaped(scope, Collections.emptySet(),
        abstractCompiler);
    Node result = mustBeReachingVariableDef.getDef("x", xUseNode);
    assertEquals(result, xDefinitionNode);
  }

  @Test
  public void testGetDefWithNullUseNode() {
    ControlFlowGraph<Node> controlFlowGraph = createControlFlowGraph();
    Scope scope = createScope();
    AbstractCompiler abstractCompiler = createAbstractCompiler();
    Node xDefinitionNode = Node.newName("x");
    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            controlFlowGraph, scope, abstractCompiler);
    mustBeReachingVariableDef.computeEscaped(scope, Collections.emptySet(),
        abstractCompiler);
    Node result = mustBeReachingVariableDef.getDef("x", null);
    assertNull(result);
  }

  @Test
  public void testGetDefWithOuterScopeVariable() {
    ControlFlowGraph<Node> controlFlowGraph = createControlFlowGraph();
    Scope scope1 = createScope();
    Scope scope2 = createScope();
    AbstractCompiler abstractCompiler = createAbstractCompiler();
    Node xDefinitionNode1 = Node.newName("x");
    Node xDefinitionNode2 = Node.newName("x");
    Node xUseNode = Node.newName("x");
    Node xDefinition = Node.newAssIGN(Token.ASSIGN, xDefinitionNode1,
        Node.newIntLiteral(1));
    Node outerScopeUseNode = Node.newName("outerScopeX");
    controlFlowGraph.addEdge(xDefinitionNode1, xUseNode);
    controlFlowGraph.addEdge(xDefinitionNode2, outerScopeUseNode);
    MustBeReachingVariableDef mustBeReachingVariableDef =
        createMustBeReachingVariableDefInstance(
            controlFlowGraph, scope1, abstractCompiler);
    mustBeReachingVariableDef.computeEscaped(scope1, Collections.emptySet(),
        abstractCompiler);
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", xUseNode));
    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("x",
        outerScopeUseNode));
  }

  @Test
  public void testJoin() {
    MustDef mustDef1 = createMustDefInstance(MustDef.class);
    MustDef mustDef2 = createMustDefInstance(MustDef.class);
    MustDefJoin mustDefJoin = createMustDefJoinInstance();

    MustDef result = mustDefJoin.apply(mustDef1, mustDef2);

    assertEquals(result.reachingDef.size(), 2);
    for (Var var : Arrays.asList(new Var[0])) {
      assertEquals(null, result.reachingDef.get(var));
    }
  }
}