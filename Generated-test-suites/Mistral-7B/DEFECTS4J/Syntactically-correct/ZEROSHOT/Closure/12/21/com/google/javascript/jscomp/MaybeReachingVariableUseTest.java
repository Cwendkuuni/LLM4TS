import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DataFlowAnalysis.JoinOp;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private static class TestControlFlowGraphBuilder extends ControlFlowGraph.Builder {

    @Override
    public DiGraph buildGraph(List<Node> nodes) {
      DiGraph diGraph = new DiGraph();
      for (Node node : nodes) {
        diGraph.addNode(new GraphNode<Node, Branch>(node, null));
      }
      for (int i = 0; i < nodes.size(); i++) {
        if (i > 0) {
          diGraph.addEdge(new DiGraphEdge(nodes.get(i - 1), nodes.get(i)));
        }
      }
      return diGraph;
    }
  }

  private static class TestScope implements Scope {

    private final Multimap<String, Var> vars = HashMultimap.create();

    @Override
    public Var getVar(String name) {
      return vars.get(name.intern()).orNull();
    }

    @Override
    public void setVar(String name, Var var) {
      vars.put(name.intern(), var);
    }

    @Override
    public boolean isVarDefined(String name) {
      return getVar(name) != null;
    }
  }

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    Node node1 = Node.newLiteral(Token.INT_LITERAL, 1);
    Node node2 = Node.newVar("A");
    Node node3 = Node.newName("print");
    Node node4 = Node.newAssign(node2, Node.newName("foo"));
    Node node5 = Node.newAssign(node2, Node.newLiteral(Token.INT_LITERAL, 1));
    Node node6 = Node.newName("alert");
    Node node7 = Node.newAssign(node2, Node.newLiteral(Token.INT_LITERAL, 1));

    cfg = new TestControlFlowGraphBuilder().buildGraph(List.of(node1, node2, node3, node4, node5, node6, node7));
    jsScope = new TestScope();
    jsScope.setVar("A", new Var(node2, null, null, true, false, 0));
    compiler = mock(AbstractCompiler.class);
  }

  @Test
  public void testMaybeReachingVariableUse() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    AnalysisResult<Node, ReachingUses> result = analysis.run();

    assertNotNull(result);
    FlowState<ReachingUsuses> initialState = result.getInitialState();
    assertNotNull(initialState);
    ReachingUses initialUses = initialState.getOut();
    assertEquals(initialUses.getClass(), ReachingUses.class);
    assertEquals(initialUses.mayUseMap.size(), 0);

    FlowState<ReachingUses> finalState = result.getFinalState(cfg.getEntryNode());
    assertNotNull(finalState);
    ReachingUses finalUses = finalState.getOut();
    assertEquals(finalUses.getClass(), ReachingUses.class);
    assertEquals(finalUses.mayUseMap.size(), 1);
    Set<Node> uses = finalUses.mayUseMap.get(jsScope.getVar("A"));
    assertNotNull(uses);
    assertEquals(uses.size(), 5);
    assertEquals(uses.contains(node4), true);
    assertEquals(uses.contains(node5), true);
    assertEquals(uses.contains(node7), true);
    assertEquals(uses.contains(node1), false);
    assertEquals(uses.contains(node3), false);
    assertEquals(uses.contains(node6), false);
  }
}

class ReachingUses {
  // same as in the original class
}