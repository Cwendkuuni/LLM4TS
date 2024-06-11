package com.google.javascript.jscomp;

import static com.google.javascript.rhino.NodeOp.ASSIGN;
import static com.google.javascript.rhino.NodeOp.AND;
import static com.google.javascript.rhino.NodeOp.IF;
import static com.google.javascript.rhino.NodeOp.IS_ASSIGN;
import static com.google.javascript.rhino.NodeOp.OR;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setUp() {
    // Set up test data here. Replace with your own test data if needed.
    String code = "function foo(x, y) {" +
        "  var a = 0, b = 0, c = 0;" +
        "  if (x > 0) {" +
        "    a = x;" +
        "  }" +
        "  if (y > 0) {" +
        "    b = y;" +
        "  }" +
        "  c = a + b;" +
        "  return c;" +
        "}";

    jsScope = Scope.newFunctionScope("foo", Sets.newHashSet("x", "y"));
    jsScope.setVar("a", new Var("a"));
    jsScope.setVar("b", new Var("b"));
    jsScope.setVar("c", new Var("c"));

    Node root = RhinoUtil.compileToNode(code, null);
    cfg = new ControlFlowGraph<Node>(root, jsScope);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testLiveVariablesAnalysis() {
    // Test the basic flow of the program.
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(entryLattice);

    assertTrue("Variable 'a' should be live", result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live", result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live", result.isLive(jsScope.getVar("c")));

    // Test a conditional branch.
    Node ifNode = ((Node) cfg.getSourceNode().getFirstChild().getNext());
    LiveVariablesAnalysis.LiveVariableLattice ifLattice = analysis.createEntryLattice();
    analysis.flowThrough(ifNode, ifLattice);

    assertTrue("Variable 'a' should be live after the if statement",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the if statement",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the if statement",
        result.isLive(jsScope.getVar("c")));

    // Test a loop.
    Node whileNode = ((Node) cfg.getSourceNode().getFirstChild().getFirstChild().getNext());
    LiveVariablesAnalysis.LiveVariableLattice whileLattice = analysis.createEntryLattice();
    analysis.flowThrough(whileNode, whileLattice);

    assertTrue("Variable 'a' should be live after the while loop",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the while loop",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the while loop",
        result.isLive(jsScope.getVar("c")));

    // Test a function call.
    Node callNode = ((Node) cfg.getSourceNode().getLastChild());
    LiveVariablesAnalysis.LiveVariableLattice callLattice = analysis.createEntryLattice();
    analysis.flowThrough(callNode, callLattice);

    assertTrue("Variable 'a' should be live after the function call",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the function call",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the function call",
        result.isLive(jsScope.getVar("c")));

    // Test an assignment.
    Node assignNode = ((Node) cfg.getSourceNode().getLastChild().getPrevious());
    LiveVariablesAnalysis.LiveVariableLattice assignLattice = analysis.createEntryLattice();
    analysis.flowThrough(assignNode, assignLattice);

    assertTrue("Variable 'c' should be live after the assignment",
        result.isLive(jsScope.getVar("c")));

    // Test an assignment with a conditional branch.
    Node ifAssignNode = ((Node) assignNode.getNext());
    LiveVariablesAnalysis.LiveVariableLattice ifAssignLattice = analysis.createEntryLattice();
    analysis.flowThrough(ifAssignNode, ifAssignLattice);

    assertTrue("Variable 'a' should be live after the conditional assignment",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the conditional assignment",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the conditional assignment",
        result.isLive(jsScope.getVar("c")));

    // Test a logical operation.
    Node andNode = ((Node) cfg.getSourceNode().getFirstChild().getPrevious());
    LiveVariablesAnalysis.LiveVariableLattice andLattice = analysis.createEntryLattice();
    analysis.flowThrough(andNode, andLattice);

    assertTrue("Variable 'a' should be live after the logical AND",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the logical AND",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the logical AND",
        result.isLive(jsScope.getVar("c")));

    // Test a logical operation with a conditional branch.
    Node ifAndNode = ((Node) andNode.getPrevious());
    LiveVariablesAnalysis.LiveVariableLattice ifAndLattice = analysis.createEntryLattice();
    analysis.flowThrough(ifAndNode, ifAndLattice);

    assertTrue("Variable 'a' should be live after the logical AND and the if statement",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the logical AND and the if statement",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the logical AND and the if statement",
        result.isLive(jsScope.getVar("c")));

    // Test a logical operation with a loop.
    Node whileAndNode = ((Node) ifAndNode.getPrevious());
    LiveVariablesAnalysis.LiveVariableLattice whileAndLattice = analysis.createEntryLattice();
    analysis.flowThrough(whileAndNode, whileAndLattice);

    assertTrue("Variable 'a' should be live after the logical AND, the while loop, and the if statement",
        result.isLive(jsScope.getVar("a")));
    assertTrue("Variable 'b' should be live after the logical AND, the while loop, and the if statement",
        result.isLive(jsScope.getVar("b")));
    assertTrue("Variable 'c' should be live after the logical AND, the while loop, and the if statement",
        result.isLive(jsScope.getVar("c")));
  }

  @Test
  public void testLiveVariablesAnalysisWithDeadAssignments() {
    // Set up test data here. Replace with your own test data if needed.
    String code = "function foo() {" +
        "  var x = 1, y = 1;" +
        "  x = null;" + // Dead assignment
        "  y = x + 1;" +
        "  return y;" +
        "}";

    jsScope = Scope.newFunctionScope("foo", Collections.emptySet());
    jsScope.setVar("x", new Var("x"));
    jsScope.setVar("y", new Var("y"));

    Node root = RhinoUtil.compileToNode(code, null);
    cfg = new ControlFlowGraph<Node>(root, jsScope);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(entryLattice);

    assertTrue("Variable 'x' should not be live after the dead assignment",
        !result.isLive(jsScope.getVar("x")));
    assertTrue("Variable 'y' should be live after the dead assignment",
        result.isLive(jsScope.getVar("y")));
  }

  @Test
  public void testLiveVariablesAnalysisWithEscapedVariables() {
    // Set up test data here. Replace with your own test data if needed.
    String code = "function foo(x) {" +
        "  function bar(y) {" +
        "    var a = 0, b = 0;" +
        "    a = x;" +
        "    b = a + y;" +
        "    return b;" +
        "  }" +
        "  return bar(1);" +
        "}" +
        "function main() {" +
        "  var result = foo(1);" +
        "  print(result);" +
        "}" +
        "main();";

    jsScope = Scope.newGlobalScope();
    jsScope.setVar("result", new Var("result"));

    Node root = RhinoUtil.compileToNode(code, null);
    cfg = new ControlFlowGraph<Node>(root, jsScope);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeResult(entryLattice);

    assertTrue("Variable 'a' in the nested function 'bar' should be considered escaped",
        analysis.getEscapedLocals().contains(jsScope.getFunctionScope("bar").getVar("a")));
    assertTrue("Variable 'b' in the nested function 'bar' should be considered escaped",
        analysis.getEscapedLocals().contains(jsScope.getFunctionScope("bar").getVar("b")));
    assertTrue("Variable 'x' passed to the nested function 'bar' should be considered escaped",
        analysis.getEscapedLocals().contains(jsScope.getVar("x")));
    assertTrue("Variable 'result' in the global scope should not be considered escaped",
        !analysis.getEscapedLocals().contains(jsScope.getVar("result")));
  }
}