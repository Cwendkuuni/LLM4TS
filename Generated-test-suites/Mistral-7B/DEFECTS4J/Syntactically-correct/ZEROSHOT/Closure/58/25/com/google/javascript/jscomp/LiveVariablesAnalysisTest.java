import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Create a sample control flow graph for testing
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope, null);

    Node root = newNode("function test() {");
    Node block = newNode("{", root);
    Node ifStmt = newNode("if (x > 5) {", block);
    Node assign1 = newNode("x = 10;", ifStmt);
    Node ifElse = newNode("}", ifStmt);
    Node assign2 = newNode("x = 20;", ifElse);
    Node returnStmt = newNode("return x;", block);
    Node varX = newNode("var x;", root);

    root.addChild(varX);
    block.addChild(ifStmt);
    ifStmt.addChild(newNode("if (", block));
    ifStmt.addChild(newNode("(x > 5)", ifStmt));
    ifStmt.addChild(ifElse);
    ifElse.addChild(assign2);
    ifStmt.addChild(returnStmt);

    LiveVariablesAnalysis liveAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test empty function
    LiveVariableLattice emptyFunction = liveAnalysis.createEntryLattice();
    assertEquals("{},", emptyFunction.toString());

    // Test live variables for the given function
    LiveVariableLattice functionLattice = liveAnalysis.compute(root, emptyFunction);
    assertEquals("{1},", functionLattice.toString());
    assertTrue(functionLattice.isLive(jsScope.getVar("x")));

    // Test live variables for if statement
    LiveVariableLattice ifStatementLattice = liveAnalysis.getJoinResult(functionLattice);
    assertEquals("{1},", ifStatementLattice.toString());
    assertTrue(ifStatementLattice.isLive(jsScope.getVar("x")));

    // Test live variables for if condition
    LiveVariableLattice conditionLattice = liveAnalysis.getJoinResult(ifStatementLattice);
    assertEquals("{1},", conditionLattice.toString());
    assertTrue(conditionLattice.isLive(jsScope.getVar("x")));

    // Test live variables for if else branch
    LiveVariableLattice ifElseBranch = liveAnalysis.getJoinResult(conditionLattice);
    assertEquals("{1,},", ifElseBranch.toString());
    assertTrue(ifElseBranch.isLive(jsScope.getVar("x")));

    // Test live variables for assign statement
    LiveVariableLattice assign1Lattice = liveAnalysis.getJoinResult(ifElseBranch);
    assertEquals("{1,},", assign1Lattice.toString());
    assertTrue(assign1Lattice.isLive(jsScope.getVar("x")));

    // Test live variables for assign statement with constant value
    LiveVariableLattice assign2Lattice = liveAnalysis.getJoinResult(assign1Lattice);
    assertEquals("{1,},", assign2Lattice.toString());
    assertTrue(assign2Lattice.isLive(jsScope.getVar("x")));

    // Test live variables for return statement
    LiveVariableLattice returnStatementLattice = liveAnalysis.getJoinResult(assign2Lattice);
    assertEquals("{1},", returnStatementLattice.toString());
    assertTrue(returnStatementLattice.isLive(jsScope.getVar("x")));

    // Test live variables for the whole function
    LiveVariableLattice finalLattice = liveAnalysis.getJoinResult(returnStatementLattice);
    assertEquals("{1},", finalLattice.toString());
    assertTrue(finalLattice.isLive(jsScope.getVar("x")));
  }
}