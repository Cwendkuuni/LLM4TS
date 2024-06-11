import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> createControlFlowGraph() {
    // Create a simple control flow graph for testing.
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = new Node(Token.BLOCK, 0);
    Node assignment = new Node(Token.ASSIGN, 1);
    Node ifStatement = new Node(Token.IF, 2);
    Node printStatement = new Node(Token.PRINT, 3);
    Node returnStatement = new Node(Token.RETURN, 4);

    entry.addChild(assignment);
    assignment.addChild(new Node(Token.NAME, "x", 2));
    assignment.addChild(new Node(Token.NUMBER, 5));

    ifStatement.addChild(new Node(Token.IF, 5));
    ifStatement.addChild(new Node(Token.NAME, "x", 6));
    ifStatement.addChild(new Node(Token.NUMBER, 10));
    ifStatement.addChild(printStatement);
    ifStatement.addChild(returnStatement);

    entry.addChild(ifStatement);

    cfg.addNodes(entry, assignment, ifStatement, printStatement, returnStatement);

    cfg.addEdge(new DiGraphEdge<>(entry, assignment));
    cfg.addEdge(new DiGraphEdge<>(assignment, ifStatement));
    cfg.addEdge(new DiGraphEdge<>(ifStatement, printStatement));
    cfg.addEdge(new DiGraphEdge<>(ifStatement, returnStatement));

    return cfg;
  }

  @org.junit.Test
  public void testMaybeReachingVariableUse() {
    Scope scope = new Scope(null, null);

    ControlFlowGraph<Node> controlFlowGraph = createControlFlowGraph();

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, scope, null);

    ReachingUses reachingUses = maybeReachingVariableUse.computeAnalysis();

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", controlFlowGraph.getEntryNode());

    assertEquals(ImmutableSet.of(assignment, ifStatement), ImmutableSet.copyOf(uses));
  }
}