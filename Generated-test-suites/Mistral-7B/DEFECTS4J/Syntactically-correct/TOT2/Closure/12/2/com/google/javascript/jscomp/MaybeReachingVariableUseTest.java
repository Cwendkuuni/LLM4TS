import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleAssignment() {
    String code = "var x = 1; x = 2;";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeResult();

    Collection<Node> uses = analysis.getUses("x", analysis.getEntryNode());
    assertEquals(ImmutableSet.of(analysis.getEntryNode().getNext()), uses);
  }

  @Test
  public void testAssignmentInCondition() {
    String code = "var x = 1; if (x = 2) { alert(x); }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeResult();

    Collection<Node> uses = analysis.getUses("x", analysis.getEntryNode());
    assertEquals(ImmutableSet.of(analysis.getEntryNode(), analysis.getEntryNode().getFirstSuccessor()), uses);
  }

  @Test
  public void testAssignmentInLoop() {
    String code = "var x = 1; for (var i = 0; i < 10; i++) { x = i; } alert(x);";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeResult();

    Collection<Node> uses = analysis.getUses("x", analysis.getEntryNode());
    assertEquals(ImmutableSet.of(analysis.getEntryNode(), analysis.getEntryNode().getNthSuccessor(9)), uses);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableOutOfScope() {
    String code = "var x = 1; function foo() { var y = 2; alert(x); } foo();";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    analysis.computeResult();

    analysis.getUses("y", analysis.getEntryNode());
  }

  @Test
  public void testEscapedVariable() {
    String code = "function foo() { var x = arguments[0]; return x; } var y = foo(1);";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeResult();

    Collection<Node> uses = analysis.getUses("x", analysis.getEntryNode());
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testEdgeCaseWithFunctionCall() {
    String code = "function foo() { var x = 1; return x * bar(); } var bar = function() { return 2; }; alert(foo());";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeResult();

    Collection<Node> uses = analysis.getUses("x", analysis.getEntryNode());
    assertEquals(ImmutableSet.of(analysis.getEntryNode().getNthSuccessor(1), analysis.getEntryNode().getNthSuccessor(2)), uses);
  }

  @Test
  public void testExceptionHandler() {
    String code = "try { var x = 1; throw new Error(); } catch (e) { alert(x); }";
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(Node.createProgram(code));
    Scope jsScope = new Scope(cfg.getEntryNode());

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    ReachingUses result = analysis.computeResult();

    Collection<Node> uses = analysis.getUses("x", analysis.getEntryNode());
    assertEquals(ImmutableSet.of(analysis.getEntryNode().getFirstSuccessor()), uses);
  }
}