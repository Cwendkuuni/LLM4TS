import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private AbstractCompiler compiler;
  private Scope scope;
  private ControlFlowGraph<Node> controlFlowGraph;

  @Before
  public void setUp() {
    compiler = new Compiler();
    scope = new Scope(compiler.getRootNode());
    String source = "function foo(x, y) {" +
        "  var z = x + y;" +
        "  if (x > 5) {" +
        "    var a = x;" +
        "  } else {" +
        "    var a = y;" +
        "  }" +
        "  return z + a;" +
        "}";
    controlFlowGraph = new ControlFlowGraph<>(new Node(source), scope);
  }

  @Test
  public void testLiveVariablesAnalysis() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, scope, compiler);
    LiveVariableLattice entry = analysis.createEntryLattice();
    LiveVariableLattice result = analysis.computeResult(controlFlowGraph.getEntryNode(), entry);

    Set<Var> expected = ImmutableSet.of(scope.getVar("x"), scope.getVar("y"), scope.getVar("z"), scope.getVar("a"));
    Set<Var> actual = analysis.getEscapedLocals();
    assertEquals(expected, actual);

    // Test for live variables at each node
    for (Node node : controlFlowGraph.getNodes()) {
      LiveVariableLattice lattice = analysis.flowThrough(node, entry);
      for (Var var : scope.getVars()) {
        boolean expectedLive = node.getFirstChild() != null ? node.getFirstChild().getType() != Token.NAME ||
            IS_ASSIGNMENT_OP(node) || NodeUtil.isName(node.getFirstChild().getNext()) &&
            var.getName().equals(NodeUtil.getName(node.getFirstChild().getNext())) : false;
        boolean actualLive = lattice.isLive(var);
        if (node.getType() == Token.FUNCTION || node.getType() == Token.SCRIPT || node.getType() == Token.BLOCK) {
          // Function, script or block nodes have all variables alive by default
          expectedLive = true;
        }
        assertEquals(expectedLive, actualLive);
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testExceptionThrown() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, scope, compiler);
    analysis.flowThrough(new Node(Token.THROW, null), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullControlFlowGraph() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, scope, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, null, compiler);
  }
}