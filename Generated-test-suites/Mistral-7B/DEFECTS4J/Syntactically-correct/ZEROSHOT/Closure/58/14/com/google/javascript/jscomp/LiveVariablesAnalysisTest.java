import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  private static final String TEST_CODE = "function f() {" +
      "var x = 1, y = 2;" +
      "if (true) {" +
      "  var z = 3;" +
      "  x = y;" +
      "}" +
      "return x; }";

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(options);
    Node root = parser.parse(TEST_CODE, null, null);
    cfg = new ControlFlowGraph<>(root);
    jsScope = cfg.getEntryScope();
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testLiveVariablesAnalysis() {
    Set<Var> expectedEscapedLocals = new HashSet<>();
    expectedEscapedLocals.add(jsScope.getVar("arguments"));
    expectedEscapedLocals.add(jsScope.getVar("x"));
    expectedEscapedLocals.add(jsScope.getVar("y"));
    expectedEscapedLocals.add(jsScope.getVar("z"));

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(expectedEscapedLocals.size(), escapedLocals.size());
    assertEquals(expectedEscapedLocals, escapedLocals);

    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();

    LiveVariablesAnalysis.LiveVariableLattice lattice1 = liveVariablesAnalysis.flowThrough(cfg.getEntryNode(), entryLattice);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = liveVariablesAnalysis.flowThrough(cfg.getStatements().get(0), lattice1);
    LiveVariablesAnalysis.LiveVariableLattice lattice3 = liveVariablesAnalysis.flowThrough(cfg.getStatements().get(1), lattice2);
    LiveVariablesAnalysis.LiveVariableLattice lattice4 = liveVariablesAnalysis.flowThrough(cfg.getStatements().get(2), lattice3);
    LiveVariablesAnalysis.LiveVariableLattice lattice5 = liveVariablesAnalysis.flowThrough(cfg.getStatements().get(3), lattice4);

    // Check that all local variables are live at the end of the function.
    for (Var var : jsScope.getVars()) {
      LiveVariablesAnalysis.LiveVariableLattice latticeLast = lattice5;
      boolean isLive = latticeLast.isLive(var);
      if (escapedLocals.contains(var)) {
        assertTrue(isLive);
      } else {
        assertTrue("Variable " + var.name + " should be live at the end of the function.", isLive);
      }
    }

    // Check that variables x and y are killed at the if statement.
    Var x = jsScope.getVar("x");
    Var y = jsScope.getVar("y");
    LiveVariablesAnalysis.LiveVariableLattice latticeIf = lattice3;
    assertFalse(latticeIf.isLive(x));
    assertFalse(latticeIf.isLive(y));

    // Check that z is killed at the assignment statement.
    Var z = jsScope.getVar("z");
    LiveVariablesAnalysis.LiveVariableLattice latticeAssignment = lattice4;
    assertFalse(latticeAssignment.isLive(z));
  }
}