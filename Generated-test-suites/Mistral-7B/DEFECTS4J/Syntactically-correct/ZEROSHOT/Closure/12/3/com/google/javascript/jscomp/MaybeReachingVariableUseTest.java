import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.GraphEdge;
import com.google.javascript.jscomp.graph.GraphNodeFactory;
import com.google.javascript.jscomp.graph.GraphTraversal;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  interface JoinOp<E extends LatticeElement> {
    E apply(List<E> from);
  }

  interface LatticeElement {
    boolean equals(Object other);
    int hashCode();
  }

  static class ReachingUses implements LatticeElement {
    Multimap<String, Node> mayUseMap;

    public ReachingUses() {
      mayUseMap = HashMultimap.create();
    }

    public ReachingUses(ReachingUses other) {
      mayUseMap = HashMultimap.create(other.mayUseMap);
    }

    @Override
    public boolean equals(Object other) {
      return (other instanceof ReachingUses) &&
          ((ReachingUses) other).mayUseMap.equals(this.mayUseMap);
    }

    @Override
    public int hashCode() {
      return mayUseMap.hashCode();
    }
  }

  static class ReachingUsesJoinOp implements JoinOp<ReachingUses> {

    @Override
    public ReachingUses apply(List<ReachingUses> from) {
      ReachingUses result = new ReachingUses();
      for (ReachingUses uses : from) {
        result.mayUseMap.putAll(uses.mayUseMap);
      }
      return result;
    }
  }

  static class MaybeReachingVariableUseUnderTest extends MaybeReachingVariableUse {

    MaybeReachingVariableUseUnderTest(ControlFlowGraph<Node> cfg, Scope jsScope, AbstractCompiler compiler) {
      super(cfg, jsScope, compiler);
    }
  }

  @org.junit.Test
  public void testSimpleFlow() {
    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new Compiler();

    Node aAssign = new Node(Token.ASSIGN, "", 0, null, new Node[]{
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.NUMBER, "1", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    });

    Node printA = new Node(Token.NAME, "print", 0, null, new Node[]{
        new Node(Token.LPAREN, "", 0, null),
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.RPAREN, "", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    });

    Node bAssign = new Node(Token.ASSIGN, "", 0, null, new Node[]{
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.NUMBER, "2", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    });

    Node entry = new Node(Token.BLOCK, "", 0, null);
    entry.addChild(aAssign);
    entry.addChild(printA);
    entry.addChild(bAssign);

    cfg.addNode(entry);

    MaybeReachingVariableUseUnderTest maybeReachingVariableUse = new MaybeReachingVariableUseUnderTest(cfg, jsScope, compiler);

    GraphNode<Node, Branch> entryNode = cfg.getNode(entry);
    FlowState<ReachingUses> entryState = new FlowState<>(maybeReachingVariableUse.createEntryLattice(), null);
    entryNode.setAnnotation(entryState);

    GraphTraversal<Node> traverser = new GraphTraversal<>(cfg);
    traverser.depthFirstPostOrder(entry);

    ReachingUses expectedResult = new ReachingUses();
    Var aVar = jsScope.getVar("A");
    expectedResult.mayUseMap.put(aVar.getName(), aAssign);

    ReachingUses result = maybeReachingVariableUse.computeAnalysis(entryNode, entryState);
    assertEquals(expectedResult, result);

    Map<String, Collection<Node>> uses = maybeReachingVariableUse.getUses("A", aAssign);
    assertSize(1, uses);
    Node use = uses.get("A").iterator().next();
    assertEquals(aAssign, use);
  }

  @org.junit.Test
  public void testConditionals() {
    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    AbstractCompiler compiler = new Compiler();

    Node ifStatement = new Node(Token.IF, "", 0, null, new Node[]{
        new Node(Token.LPAREN, "", 0, null),
        new Node(Token.NAME, "x", 0, null),
        new Node(Token.COND, "", 0, null),
        new Node(Token.LPAREN, "", 0, null),
        new Node(Token.NAME, "false", 0, null),
        new Node(Token.RPAREN, "", 0, null),
        new Node(Token.BLOCK, "", 0, null),
        new Node(Token.RPAREN, "", 0, null)
    });

    Node trueBranch = new Node(Token.BLOCK, "", 0, null);
    trueBranch.addChild(new Node(Token.NAME, "print", 0, null, new Node[]{
        new Node(Token.LPAREN, "", 0, null),
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.RPAREN, "", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    }));

    Node falseBranch = new Node(Token.BLOCK, "", 0, null);

    ifStatement.addChild(trueBranch);
    ifStatement.addChild(falseBranch);
    cfg.addNode(ifStatement);

    Node aAssign = new Node(Token.ASSIGN, "", 0, null, new Node[]{
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.NUMBER, "1", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    });

    Node printA = new Node(Token.NAME, "print", 0, null, new Node[]{
        new Node(Token.LPAREN, "", 0, null),
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.RPAREN, "", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    });

    Node bAssign = new Node(Token.ASSIGN, "", 0, null, new Node[]{
        new Node(Token.NAME, "A", 0, null),
        new Node(Token.NUMBER, "2", 0, null),
        new Node(Token.SEMICOLON, "", 0, null)
    });

    Node entry = new Node(Token.BLOCK, "", 0, null);
    entry.addChild(aAssign);
    entry.addChild(ifStatement);
    entry.addChild(bAssign);

    cfg.addNode(entry);

    MaybeReachingVariableUseUnderTest maybeReachingVariableUse = new MaybeReachingVariableUseUnderTest(cfg, jsScope, compiler);

    GraphNode<Node, Branch> entryNode = cfg.getNode(entry);
    FlowState<ReachingUses> entryState = new FlowState<>(maybeReachingVariableUse.createEntryLattice(), null);
    entryNode.setAnnotation(entryState);

    GraphTraversal<Node> traverser = new GraphTraversal<>(cfg);
    traverser.depthFirstPostOrder(entry);

    ReachingUses expectedResult = new ReachingUses();
    Var aVar = jsScope.getVar("A");
    expectedResult.mayUseMap.put(aVar.getName(), aAssign);

    ReachingUses result = maybeReachingVariableUse.computeAnalysis(entryNode, entryState);
    assertEquals(expectedResult, result);

    Map<String, Collection<Node>> uses = maybeReachingVariableUse.getUses("A", aAssign);
    assertSize(1, uses);
    Node use = uses.get("A").iterator().next();
    assertEquals(aAssign, use);

    ReachingUses trueBranchResult = maybeReachingVariableUse.computeAnalysis(cfg.getNode(trueBranch), entryState);
    Var aVarInTrueBranch = jsScope.getVar("A");
    expectedResult.mayUseMap.putAll(trueBranchResult.mayUseMap);
    expectedResult.mayUseMap.remove(aVarInTrueBranch.getName());
    expectedResult.mayUseMap.put(aVarInTrueBranch.getName(), printA);

    ReachingUses falseBranchResult = maybeReachingVariableUse.computeAnalysis(cfg.getNode(falseBranch), entryState);
    expectedResult.mayUseMap.putAll(falseBranchResult.mayUseMap);

    assertEquals(expectedResult, maybeReachingVariableUse.computeAnalysis(entryNode, entryState));

    uses = maybeReachingVariableUse.getUses("A", aAssign);
    assertSize(2, uses);
    Node useInTrueBranch = uses.get("A").iterator().next();
    assertEquals(printA, useInTrueBranch);

    Node useInFalseBranch = uses.get("A").iterator().next();
    assertEquals(aAssign, useInFalseBranch);
  }

  private static <T> void assertSize(int expectedSize, Collection<T> collection) {
    int actualSize = collection.size();
    if (expectedSize != actualSize) {
      fail("Unexpected size. Expected: " + expectedSize + ". Actual: " + actualSize);
    }
  }
}