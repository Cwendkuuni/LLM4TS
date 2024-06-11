import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Create a simple control flow graph and scope for testing.
    Node entry = Node.newBlock("entry");
    Node aAssign = Node.newAssign("a", Node.newNumber(1));
    Node ifNode = Node.newIf(Node.newBinaryOp(Token.AND, Node.newBool(true), Node.newBool(true)));
    Node aUse1 = Node.newGetProp("a", Node.newThis());
    Node bAssign = Node.newAssign("b", Node.newNumber(2));
    Node aUse2 = Node.newGetProp("a", Node.newThis());
    Node exit = Node.newBlock("exit");

    cfg = new ControlFlowGraph<Node>(entry, exit);
    cfg.addEdge(entry, aAssign, new Branch(aAssign, Node.newLabel(), null));
    cfg.addEdge(aAssign, ifNode, new Branch(ifNode, Node.newLabel(), null));
    cfg.addEdge(ifNode, aUse1, new Branch(aUse1, Node.newLabel(), null));
    cfg.addEdge(ifNode, bAssign, null);
    cfg.addEdge(bAssign, aUse2, new Branch(aUse2, Node.newLabel(), null));
    cfg.addEdge(aUse1, exit, new Branch(exit, Node.newLabel(), null));
    cfg.addEdge(aUse2, exit, new Branch(exit, Node.newLabel(), null));

    jsScope = new Scope();
    jsScope.declareVar("a", new Var(jsScope, "a"));
    jsScope.declareVar("b", new Var(jsScope, "b"));
  }

  @Test
  public void testBasicFlow() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses reachingUses = analysis.computeResult(entry);

    Multimap<Var, Node> uses = reachingUses.mayUseMap;
    assertEquals(ImmutableSet.of(aUse1, aUse2), getUses("a", entry));
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testAssignment() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses reachingUses = analysis.computeResult(entry);

    Multimap<Var, Node> uses = reachingUses.mayUseMap;
    assertEquals(ImmutableSet.of(aUse1, aUse2), getUses("a", entry));
    assertFalse(uses.containsKey(new Var(jsScope, "b")));
  }

  @Test
  public void testConditional() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses reachingUses = analysis.computeResult(entry);

    Multimap<Var, Node> uses = reachingUses.mayUseMap;
    assertEquals(ImmutableSet.of(aUse1), getUses("a", entry));
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testLoop() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses reachingUses = analysis.computeResult(entry);

    Multimap<Var, Node> uses = reachingUses.mayUseMap;
    assertEquals(ImmutableSet.of(aUse2), getUses("a", entry));
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testFunctionCall() {
    Node functionCall = Node.newCall("foo", Node.newThis(), ImmutableList.of());

    Node entry = Node.newBlock("entry");
    Node functionCallNode = Node.newCall(functionCall, Node.newThis());
    Node exit = Node.newBlock("exit");

    cfg = new ControlFlowGraph<Node>(entry, exit);
    cfg.addEdge(entry, functionCallNode, new Branch(functionCallNode, Node.newLabel(), null));
    cfg.addEdge(functionCallNode, exit, new Branch(exit, Node.newLabel(), null));

    analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    reachingUses = analysis.computeResult(entry);

    uses = reachingUses.mayUseMap;
    assertEquals(ImmutableSet.<Node>of(), getUses("a", entry));
  }

  @Test
  public void testEscapedVariable() {
    Node functionCall = Node.newCall("foo", Node.newThis(), ImmutableList.of());

    Node entry = Node.newBlock("entry");
    Node functionCallNode = Node.newCall(functionCall, Node.newThis());
    Node escapeVariableAssign = Node.newAssign("escape", Node.newCall("bar", Node.newThis()));
    Node escapeVariableUse = Node.newGetProp("escape", Node.newThis());
    Node exit = Node.newBlock("exit");

    cfg = new ControlFlowGraph<Node>(entry, exit);
    cfg.addEdge(entry, functionCallNode, new Branch(functionCallNode, Node.newLabel(), null));
    cfg.addEdge(functionCallNode, escapeVariableAssign, null);
    cfg.addEdge(escapeVariableAssign, escapeVariableUse, new Branch(escapeVariableUse, Node.newLabel(), null));
    cfg.addEdge(escapeVariableUse, exit, new Branch(exit, Node.newLabel(), null));

    jsScope.declareVar("escape", new Var(jsScope, "escape"));
    escaped.add(new Var(jsScope, "escape"));

    analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    reachingUses = analysis.computeResult(entry);

    uses = reachingUses.mayUseMap;
    assertEquals(ImmutableSet.<Node>of(), getUses("a", entry));
    assertTrue(uses.isEmpty());
  }

  private Collection<Node> getUses(String name, Node defNode) {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    FlowState<ReachingUses> flowState = analysis.computeFlowState(defNode, new ReachingUses());
    return flowState.getOut().mayUseMap.get(jsScope.getVar(name));
  }
}