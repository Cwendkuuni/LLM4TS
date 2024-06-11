import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    // Create a simple control flow graph for testing.
    Node root = Node.newBlock("root");
    Node function = Node.newFunction("function", null, root, null);
    Node entry = Node.newVariableO("entry", Node.SCRIPT_VAR, function, 0);
    Node assignment = Node.newAssign(entry, Node.newName("x"));
    Node ifStatement = Node.newIf(Node.newName("condition"), Node.newBlock("if"), Node.newBlock("else"));
    Node elseStatement = Node.newBlock("else");
    Node exit = Node.newVariableO("exit", Node.SCRIPT_VAR, function, 1);

    root.addChild(function);
    function.addChild(entry);
    function.addChild(assignment);
    function.addChild(ifStatement);
    ifStatement.addChild(Node.newBlock("ifBody"));
    ifStatement.addChild(elseStatement);
    elseStatement.addChild(exit);
    nodeSetUp(assignment, "x");
    nodeSetUp(exit, "x");

    cfg = new ControlFlowGraph<Node>(function, NodeUtil.getTopSortOrder(function), new DiGraph<Node, Branch>());
    jsScope = Scope.makeroot();
    compiler = new AbstractCompiler(new CompilerOptions()) {
      @Override
      ControlFlowGraph<Node> getControlFlowGraph(Scope pScope, AbstractCompiler compiler) {
        return cfg;
      }
    };
  }

  @Test
  public void testLiveVariablesAnalysis() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariablesAnalysis.LiveVariableLattice entryEstimate = analysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice ifBodyEstimate = analysis.flowThrough(analysis.getCFG().getRootNode().getChild(1), entryEstimate);
    LiveVariablesAnalysis.LiveVariableLattice exitEstimate = analysis.flowThrough(analysis.getCFG().getRootNode().getChild(2), ifBodyEstimate);

    // Test if variables are live at the entry point.
    assertTrue(analysis.isLive(entryEstimate, "entry"));

    // Test if variables are killed by assignments.
    assertFalse(analysis.isLive(ifBodyEstimate, "entry"));

    // Test if variables are live after an if statement.
    assertTrue(analysis.isLive(exitEstimate, "entry"));
  }

  private void nodeSetUp(Node node, String variableName) {
    jsScope.put(variableName, Scope.Var.make(Node.newName(variableName), 0));
  }
}