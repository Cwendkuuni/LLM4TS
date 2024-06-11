import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> mockControlFlowGraph;
  private Scope mockScope;
  private AbstractCompiler mockCompiler;
  private MaybeReachingVariableUse maybeReachingVariableUse;

  @Before
  public void setUp() {
    mockControlFlowGraph = mock(ControlFlowGraph.class);
    mockScope = mock(Scope.class);
    mockCompiler = mock(AbstractCompiler.class);

    maybeReachingVariableUse = new MaybeReachingVariableUse(mockControlFlowGraph, mockScope, mockCompiler);
  }

  @Test
  public void testEmpty() {
    ReachingUses entering = maybeReachingVariableUse.createEntryLattice();
    ReachingUses result = maybeReachingVariableUse.flowThrough(null, entering);

    assertEquals(entering, result);
  }

  @Test
  public void testAssignment() {
    Node defNode = Node.newNode(Token.ASSIGN, null, null, null);
    Node useNode = Node.newNode(Token.GETPROP, null, null, null);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(mockScope.getVar("test"), defNode);

    when(mockControlFlowGraph.getNode(defNode)).thenReturn(GraphNode.newNode(defNode));
    when(mockControlFlowGraph.getNode(useNode)).thenReturn(GraphNode.newNode(useNode));

    FlowState<ReachingUses> defState = FlowState.newFlowState(maybeReachingVariableUse.createInitialEstimateLattice());
    defState.setIn(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(defNode)).thenReturn(GraphNode.newNode(defNode).setAnnotation(defState));

    FlowState<ReachingUses> useState = FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice());
    useState.setOut(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(useNode)).thenReturn(GraphNode.newNode(useNode).setAnnotation(useState));

    ReachingUses entering = maybeReachingVariableUse.createEntryLattice();
    ReachingUses result = maybeReachingVariableUse.flowThrough(useNode, entering);

    assertEquals(expectedUses, result.mayUseMap);
  }

  @Test
  public void testAssignmentWithConditional() {
    Node defNode = Node.newNode(Token.ASSIGN, null, null, null);
    Node useNode = Node.newNode(Token.GETPROP, null, null, null);
    Node conditionNode = Node.newNode(Token.IF, null, null, null);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(mockScope.getVar("test"), defNode);

    when(mockControlFlowGraph.getNode(defNode)).thenReturn(GraphNode.newNode(defNode));
    when(mockControlFlowGraph.getNode(useNode)).thenReturn(GraphNode.newNode(useNode));
    when(mockControlFlowGraph.getNode(conditionNode)).thenReturn(GraphNode.newNode(conditionNode));

    FlowState<ReachingUses> defState = FlowState.newFlowState(maybeReachingVariableUse.createInitialEstimateLattice());
    defState.setIn(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(defNode)).thenReturn(GraphNode.newNode(defNode).setAnnotation(defState));

    FlowState<ReachingUses> useState = FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice());
    useState.setOut(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(useNode)).thenReturn(GraphNode.newNode(useNode).setAnnotation(useState));

    when(mockControlFlowGraph.getNode(conditionNode)).thenReturn(GraphNode.newNode(conditionNode).setAnnotation(FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice())));

    ReachingUses entering = maybeReachingVariableUse.createEntryLattice();
    ReachingUses result = maybeReachingVariableUse.flowThrough(useNode, entering);

    assertEquals(expectedUses, result.mayUseMap);
  }

  @Test
  public void testConditional() {
    Node conditionNode = Node.newNode(Token.IF, null, null, null);
    Node trueBranchNode = Node.newNode(Token.ASSIGN, null, null, null);
    Node falseBranchNode = Node.newNode(Token.ASSIGN, null, null, null);

    Multimap<Var, Node> expectedUsesTrue = HashMultimap.create();
    expectedUsesTrue.put(mockScope.getVar("test"), trueBranchNode);

    Multimap<Var, Node> expectedUsesFalse = HashMultimap.create();

    when(mockControlFlowGraph.getNode(conditionNode)).thenReturn(GraphNode.newNode(conditionNode));
    when(mockControlFlowGraph.getNode(trueBranchNode)).thenReturn(GraphNode.newNode(trueBranchNode));
    when(mockControlFlowGraph.getNode(falseBranchNode)).thenReturn(GraphNode.newNode(falseBranchNode));

    FlowState<ReachingUses> trueBranchState = FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice());
    trueBranchState.setIn(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(trueBranchNode)).thenReturn(GraphNode.newNode(trueBranchNode).setAnnotation(trueBranchState));

    FlowState<ReachingUses> falseBranchState = FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice());
    falseBranchState.setIn(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(falseBranchNode)).thenReturn(GraphNode.newNode(falseBranchNode).setAnnotation(falseBranchState));

    when(mockControlFlowGraph.getNode(conditionNode)).thenReturn(GraphNode.newNode(conditionNode).setAnnotation(FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice())));

    ReachingUses entering = maybeReachingVariableUse.createEntryLattice();
    ReachingUses result = maybeReachingVariableUse.flowThrough(conditionNode, entering);

    assertEquals(Sets.union(expectedUsesTrue, expectedUsesFalse), result.mayUseMap.asMap().values());
  }

  @Test
  public void testWhileLoop() {
    Node conditionNode = Node.newNode(Token.WHILE, null, null, null);
    Node assignmentNode = Node.newNode(Token.ASSIGN, null, null, null);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(mockScope.getVar("test"), assignmentNode);

    when(mockControlFlowGraph.getNode(conditionNode)).thenReturn(GraphNode.newNode(conditionNode));
    when(mockControlFlowGraph.getNode(assignmentNode)).thenReturn(GraphNode.newNode(assignmentNode));

    FlowState<ReachingUses> assignmentState = FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice());
    assignmentState.setIn(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(assignmentNode)).thenReturn(GraphNode.newNode(assignmentNode).setAnnotation(assignmentState));

    FlowState<ReachingUses> conditionState = FlowState.newFlowState(maybeReachingVariableUse.createEntryLattice());
    conditionState.setOut(maybeReachingVariableUse.createEntryLattice());
    when(mockControlFlowGraph.getNode(conditionNode)).thenReturn(GraphNode.newNode(conditionNode).setAnnotation(conditionState));

    ReachingUses entering = maybeReachingVariableUse.createEntryLattice();
    ReachingUses result = maybeReachingVariableUse.flowThrough(conditionNode, entering);

    assertEquals(expectedUses, result.mayUseMap);
  }
}