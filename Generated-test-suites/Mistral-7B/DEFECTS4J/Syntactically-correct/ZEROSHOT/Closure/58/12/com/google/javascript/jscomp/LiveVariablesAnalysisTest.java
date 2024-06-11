import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isAssIGN;
import static com.google.javascript.jscomp.NodeUtil.isArgumentsName;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isVar;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function f(x, y) {\n" +
        "  var a, b;\n" +
        "  if (x > 5) {\n" +
        "    a = 7;\n" +
        "    b = x;\n" +
        "  } else {\n" +
        "    b = y;\n" +
        "    a = 3;\n" +
        "  }\n" +
        "  return a + b;\n" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    List<Node> entryNodes = cfg.getEntryNodes();
    Node entryNode = entryNodes.get(0);
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.analyze(entryNode, entryLattice);

    Set<String> expectedLiveVariables = ImmutableSet.of("a", "b", "x", "y");
    for (Var var : jsScope.getVars()) {
      String variableName = var.getName();
      if (expectedLiveVariables.contains(variableName)) {
        assertTrue(result.isLive(var));
      } else {
        assertFalse(result.isLive(var));
      }
    }

    Node ifStatement = entryNode.getFirstChild();
    Node elseStatement = ifStatement.getNext();

    LiveVariablesAnalysis.LiveVariableLattice ifStatementLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice ifStatementResult = liveVariablesAnalysis.analyze(ifStatement, ifStatementLattice);

    LiveVariablesAnalysis.LiveVariableLattice elseStatementLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice elseStatementResult = liveVariablesAnalysis.analyze(elseStatement, elseStatementLattice);

    Set<String> expectedIfVariables = ImmutableSet.of("a", "b", "x");
    for (Var var : jsScope.getVars()) {
      String variableName = var.getName();
      if (expectedIfVariables.contains(variableName)) {
        assertTrue(ifStatementResult.isLive(var));
      } else {
        assertFalse(ifStatementResult.isLive(var));
      }
    }

    Set<String> expectedElseVariables = ImmutableSet.of("b", "y");
    for (Var var : jsScope.getVars()) {
      String variableName = var.getName();
      if (expectedElseVariables.contains(variableName)) {
        assertTrue(elseStatementResult.isLive(var));
      } else {
        assertFalse(elseStatementResult.isLive(var));
      }
    }

    LiveVariablesAnalysis.LiveVariableLattice returnStatementLattice = liveVariablesAnalysis.createEntryLattice();
    Node returnStatement = cfg.getNodesByKindAndName(Node.RETURN, null).get(0);
    LiveVariablesAnalysis.LiveVariableLattice returnStatementResult = liveVariablesAnalysis.analyze(returnStatement, returnStatementLattice);

    Set<String> expectedReturnVariables = ImmutableSet.of("a", "b");
    for (Var var : jsScope.getVars()) {
      String variableName = var.getName();
      if (expectedReturnVariables.contains(variableName)) {
        assertTrue(returnStatementResult.isLive(var));
      } else {
        assertFalse(returnStatementResult.isLive(var));
      }
    }
  }

  @Test
  public void testEscapedVariables() {
    String jsCode = "function f() {\n" +
        "  function g(x) {\n" +
        "    var a = x;\n" +
        "    return a;\n" +
        "  }\n" +
        "  return g(5);\n" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    List<Node> entryNodes = cfg.getEntryNodes();
    Node entryNode = entryNodes.get(0);
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.analyze(entryNode, entryLattice);

    Set<String> expectedEscapedVariables = ImmutableSet.of("a");
    for (Var var : jsScope.getVars()) {
      String variableName = var.getName();
      if (expectedEscapedVariables.contains(variableName)) {
        assertTrue(liveVariablesAnalysis.getEscapedLocals().contains(var));
      } else {
        assertFalse(liveVariablesAnalysis.getEscapedLocals().contains(var));
      }
    }

    Set<String> expectedLiveVariables = ImmutableSet.of("x");
    for (Var var : jsScope.getVars()) {
      String variableName = var.getName();
      if (expectedLiveVariables.contains(variableName)) {
        assertTrue(result.isLive(var));
      } else {
        assertFalse(result.isLive(var));
      }
    }
  }
}