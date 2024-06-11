import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private static final String TEST_CODE = "function foo(x, y) {" +
      "  var z = 0;" +
      "  if (x > 0) {" +
      "    z = x;" +
      "  } else {" +
      "    z = y;" +
      "  }" +
      "  return z;" +
      "}";

  private ControlFlowGraph<Node> cfg;
  private Scope scope;
  private AbstractCompiler compiler;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    compiler = new Compiler();
    scope = new Scope(null);
    scope.putVar("foo", new Var(-1, "foo", Node.NAME));
    scope.putVar("x", new Var(0, "x", Node.NAME));
    scope.putVar("y", new Var(1, "y", Node.NAME));
    scope.putVar("z", new Var(2, "z", Node.NAME));
    scope.putVar("arguments", new Var(-2, "arguments", Node.NAME));
    cfg = compiler.process(TEST_CODE, scope);
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
  }

  @Test
  public void testEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    assertEquals("{0, 1, 2}", entryLattice.toString());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    assertEquals("{0, 1, 2}", initialEstimateLattice.toString());
  }

  @Test
  public void testFlowThroughSimpleStatement() {
    Node simpleStatementNode = NodeUtil.createSimpleStatementNode(Token.ASSIGN, Node.NAME, Node.NUM, 0);

    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(simpleStatementNode, input);

    assertTrue(output.isLive(0));
    assertTrue(output.isLive(1));
    assertTrue(output.isLive(2));
  }

  @Test
  public void testFlowThroughAssignment() {
    Node assignmentNode = NodeUtil.createAssignmentNode(Token.ASSIGN, Node.NAME, Node.NAME, Node.NAME);

    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(assignmentNode, input);

    // Assignment reads the variable, so it is live.
    assertTrue(output.isLive(input.liveSet.getIndex(assignmentNode.getFirstChild())));
  }

  @Test
  public void testFlowThroughCondition() {
    Node ifNode = NodeUtil.createIfNode(Token.IF, Node.BOOLOBJECT, Node.NAME);

    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(ifNode, input);

    // Both branches are live.
    assertTrue(output.isLive(0));
    assertTrue(output.isLive(1));
    assertTrue(output.isLive(2));
  }

  @Test
  public void testFlowThroughWhileLoop() {
    Node whileNode = NodeUtil.createWhileNode(Token.WHILE, Node.BOOLOBJECT, Node.NAME, Node.NAME);

    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(whileNode, input);

    // Variable is live inside the loop.
    assertTrue(output.isLive(input.liveSet.getIndex(whileNode.getFirstChild().getLastChild())));
  }

  @Test
  public void testFlowThroughIfElse() {
    Node ifElseNode = NodeUtil.createIfElseNode(Token.IF, Node.BOOLOBJECT, Node.NAME, Node.NAME, Node.NAME);

    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(ifElseNode, input);

    // Variable is live in both branches.
    assertTrue(output.isLive(0));
    assertTrue(output.isLive(1));
    assertTrue(output.isLive(2));
  }

  @Test
  public void testFlowThroughFunctionCall() {
    Node callNode = NodeUtil.createCallNode(Token.NAME, Node.NAME);

    LiveVariablesAnalysis.LiveVariableLattice input = liveVariablesAnalysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice output = liveVariablesAnalysis.flowThrough(callNode, input);

    // No change in liveness.
    assertEquals("{0, 1, 2}", output.toString());
  }

  @Test
  public void testGetEscapedLocals() {
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertFalse(escapedLocals.isEmpty());
    assertEquals(2, escapedLocals.size());

    Var argumentsVar = scope.getVar("arguments");
    assertTrue(escapedLocals.contains(argumentsVar));
    Var fooVar = scope.getVar("foo");
    assertTrue(escapedLocals.contains(fooVar));
  }
}