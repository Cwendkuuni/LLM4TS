import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isVar;

import java.util.BitSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function foo(x, y) {\n" +
        "  var a = 1;\n" +
        "  if (x > y) {\n" +
        "    var b = 2;\n" +
        "    a = b;\n" +
        "  } else {\n" +
        "    var b = 3;\n" +
        "    a = b;\n" +
        "  }\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.getGlobalScope();
    ControlFlowGraph<Node> cfg = compiler.getFlowGraph(jsCode, jsScope);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();

    assertEquals("Unexpected number of escaped locals.", 0, escapedLocals.size());

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals("Entry lattice should be empty.", entryLattice.toString(), "{}");
    assertEquals("Initial estimate lattice should contain all variables.",
        initialEstimateLattice.toString(),
        IntStream.range(0, jsScope.getVarCount())
            .mapToObj(i -> String.format("var%d", i))
            .collect(Collectors.joining(",")) + "{}");

    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.flowThrough(cfg.getEntryNode(), initialEstimateLattice);

    // At entry point, all variables are live.
    assertEquals("Variables should be live at entry point.", resultLattice.toString(),
        IntStream.range(0, jsScope.getVarCount())
            .mapToObj(i -> String.format("var%d", i))
            .collect(Collectors.joining(",")) + "{}");

    List<Node> nodes = Lists.newArrayList(cfg.getNodes());
    for (Node node : nodes) {
      LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(node, resultLattice);
      if (node.getType() == Token.SCRIPT || node.getType() == Token.BLOCK || node.getType() == Token.FUNCTION) {
        // At function exit point, all variables should be live.
        assertEquals("Variables should be live at function exit point.", lattice.toString(),
            IntStream.range(0, jsScope.getVarCount())
                .mapToObj(i -> String.format("var%d", i))
                .collect(Collectors.joining(",")) + "{}");
      } else {
        // Inside a conditional statement, variables are alive only if the condition is true.
        if (isName(node) && node.getString().equals("x > y")) {
          BitSet liveVariables = lattice.liveSet;
          Var x = jsScope.getVar("x");
          Var y = jsScope.getVar("y");
          assertTrue("Variable x should be alive if condition is true.", liveVariables.get(x.index));
          assertTrue("Variable y should be alive if condition is true.", liveVariables.get(y.index));

          // If condition is false, all variables are killed.
          Node falseNode = node.getLastChild();
          LiveVariablesAnalysis.LiveVariableLattice killedLattice = analysis.flowThrough(falseNode, lattice);
          BitSet killedVariables = killedLattice.liveSet;
          assertFalse("Variable x should be dead if condition is false.", killedVariables.get(x.index));
          assertFalse("Variable y should be dead if condition is false.", killedVariables.get(y.index));
        } else if (isAssign(node) && isName(node.getFirstChild()) && !isAssign(node.getFirstChild().getNext())) {
          // Inside an assignment statement, the variable being assigned is killed before being made live again.
          Node lhs = node.getFirstChild();
          Var assignedVar = jsScope.getVar(lhs.getString());

          // Variable is killed before being assigned.
          LiveVariablesAnalysis.LiveVariableLattice killedLattice = analysis.flowThrough(lhs, lattice);
          BitSet killedVariables = killedLattice.liveSet;
          assertFalse("Variable should be dead before being assigned.", killedVariables.get(assignedVar.index));

          // Variable is made live again after being assigned.
          LiveVariablesAnalysis.LiveVariableLattice assignedLattice = analysis.flowThrough(lhs.getNext(), killedLattice);
          BitSet liveVariables = assignedLattice.liveSet;
          assertTrue("Variable should be live after being assigned.", liveVariables.get(assignedVar.index));
        }
      }
    }
  }

  @Test
  public void testLiveVariablesAnalysisWithEscapedLocals() {
    String jsCode = "function foo(x) {\n" +
        "  function bar(y) {\n" +
        "    var a = x;\n" +
        "    return a;\n" +
        "  }\n" +
        "  return bar(1);\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.getGlobalScope();
    ControlFlowGraph<Node> cfg = compiler.getFlowGraph(jsCode, jsScope);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();

    assertEquals("Unexpected number of escaped locals.", 1, escapedLocals.size());
    Var xVar = jsScope.getVar("x");
    assertTrue("Variable x should be escaped.", escapedLocals.contains(xVar));

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertEquals("Entry lattice should be empty.", entryLattice.toString(), "{}");
    assertEquals("Initial estimate lattice should contain all variables.",
        initialEstimateLattice.toString(),
        IntStream.range(0, jsScope.getVarCount())
            .mapToObj(i -> String.format("var%d", i))
            .collect(Collectors.joining(",")) + "{}");

    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.flowThrough(cfg.getEntryNode(), initialEstimateLattice);

    // At entry point, all variables are live.
    assertEquals("Variables should be live at entry point.", resultLattice.toString(),
        IntStream.range(0, jsScope.getVarCount())
            .mapToObj(i -> String.format("var%d", i))
            .collect(Collectors.joining(",")) + "{}");

    List<Node> nodes = Lists.newArrayList(cfg.getNodes());
    for (Node node : nodes) {
      LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.flowThrough(node, resultLattice);

      if (node.getType() == Token.FUNCTION && node.getString().equals("bar")) {
        // Inside the inner function, the argument x is alive.
        Var xVar = jsScope.getVar("x");
        assertTrue("Variable x should be alive inside the inner function.", lattice.isLive(xVar));

        // Inside the inner function, the local variable a is also alive because it escapes.
        Var aVar = jsScope.getVar("a");
        assertTrue("Variable a should be alive inside the inner function.", lattice.isLive(aVar));

        // At the exit point of the inner function, all variables are killed except the escaped one.
        LiveVariablesAnalysis.LiveVariableLattice exitLattice = analysis.flowThrough(cfg.getExitNode(node), lattice);
        assertTrue("Variable x should be dead at the exit point of the inner function.", !lattice.isLive(xVar));
        assertFalse("Variable a should be dead at the exit point of the inner function.", !lattice.isLive(aVar));
      }
    }
  }
}