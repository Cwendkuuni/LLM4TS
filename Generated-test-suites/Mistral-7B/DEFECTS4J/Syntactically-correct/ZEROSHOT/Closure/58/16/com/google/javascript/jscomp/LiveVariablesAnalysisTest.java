import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Sets;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function test() {" +
        "  var a = 1;" +
        "  if (true) {" +
        "    var b = 2;" +
        "    var c = a;" +
        "  }" +
        "  return a + b; // returns 3" +
        "}";

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node root = Parser.parse(jsCode);
    liveVariablesAnalysis.analyze(root);

    Set<Var> expectedEscapedLocals = Sets.newHashSet();
    Set<Var> actualEscapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals("Unexpected number of escaped locals", 0, actualEscapedLocals.size());

    LiveVariableLattice entryLattice = liveVariablesAnalysis.getEntryLattice();
    LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.getInitialEstimateLattice();

    // Both entry and initial estimate lattices should contain all local variables.
    int numVars = jsScope.getVarCount();
    BitSetbitSet liveSetEntry = entryLattice.liveSet;
    BitSet liveSetInitial = initialEstimateLattice.liveSet;

    for (int i = 0; i < numVars; i++) {
      Var var = jsScope.getVar(i);
     assertTrue("Variable " + var.name + " is not live at entry", liveSetEntry.get(i));
      assertTrue("Variable " + var.name + " is not live in initial estimate", liveSetInitial.get(i));
    }

    // Test flow through nodes.
    Node ifNode = findNodeByType(root, Token.IF);
    LiveVariableLattice ifLattice = liveVariablesAnalysis.flowThrough(ifNode, initialEstimateLattice);

    // Variables a and c are live at ifNode because they are assigned before the if statement.
    Var a = jsScope.getVar("a");
    Var c = jsScope.getVar("c");
    assertTrue("Variable " + a.name + " is not live at ifNode", ifLattice.isLive(a));
    assertTrue("Variable " + c.name + " is not live at ifNode", ifLattice.isLive(c));

    // Variable b is not live at ifNode because it is only assigned within the if statement.
    Var b = jsScope.getVar("b");
    assertFalse("Variable " + b.name + " is live at ifNode", ifLattice.isLive(b));

    // Test flow through assignment nodes.
    Node assignCNode = findNodeByType(root, Token.ASSIGN);
    Node assignANode = assignCNode.getNext();
    LiveVariableLattice assignCLattice = liveVariablesAnalysis.flowThrough(assignANode, ifLattice);

    // Variable a is still live because it is the value being assigned.
    assertTrue("Variable " + a.name + " is not live after assignment", assignCLattice.isLive(a));

    // Variable b is live after the assignment because it is assigned a value.
    assertTrue("Variable " + b.name + " is not live after assignment", assignCLattice.isLive(b));

    // Variable c is still live because it is the target of the assignment.
    assertTrue("Variable " + c.name + " is not live after assignment", assignCLattice.isLive(c));

    // Test flow through return node.
    Node returnNode = findNodeByType(root, Token.RETURN);
    LiveVariableLattice returnLattice = liveVariablesAnalysis.flowThrough(returnNode, assignCLattice);

    // Both a and b are live at returnNode because they are used in the expression that is returned.
    assertTrue("Variable " + a.name + " is not live at returnNode", returnLattice.isLive(a));
    assertTrue("Variable " + b.name + " is not live at returnNode", returnLattice.isLive(b));

    // Test flow through function node.
    Node functionNode = findNodeByType(root, Token.FUNCTION);
    LiveVariableLattice functionLattice = liveVariablesAnalysis.flowThrough(functionNode, returnLattice);

    // All local variables are live at the end of the function because they might be returned or used in a closure.
    for (int i = 0; i < numVars; i++) {
      Var var = jsScope.getVar(i);
      assertTrue("Variable " + var.name + " is not live at the end of the function", functionLattice.isLive(var));
    }
  }
}