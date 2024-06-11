import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis analyzer;

  @Before
  public void setUp() {
    // Replace this with the actual code to initialize ControlFlowGraph and Scope objects.
    Node rootNode = Node.newCompound(Node.SCOPE, 5);
    jsScope = new Scope(rootNode);
    cfg = new ControlFlowGraph<>();
    analyzer = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testEmptyProgram() {
    LiveVariablesAnalysis emptyAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    Set<Var> result = emptyAnalysis.getEscapedLocals();
    assertEquals(ImmutableSet.of(), result);
  }

  @Test
  public void testSimpleAssignment() {
    // Replace this with the actual code to set up the test case nodes.
    Node assignmentNode = Node.newAssignment(Token.VAR, "x", Node.newNumber(5));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice result = analysis.flowThrough(assignmentNode, analysis.createEntryLattice());
    assertTrue(result.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testDeadAssignment() {
    // Replace this with the actual code to set up the test case nodes.
    Node assignmentNode = Node.newAssignment(Token.VAR, "x", Node.newNumber(5));
    Node deadAssignmentNode = Node.newAssignment(Token.VAR, "x", Node.newNumber(7));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice deadAssignmentLattice = analysis.flowThrough(deadAssignmentNode, analysis.flowThrough(assignmentNode, entryLattice));
    assertFalse(deadAssignmentLattice.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testConditionalAssignment() {
    // Replace this with the actual code to set up the test case nodes.
    Node ifNode = Node.newIf(Token.IF, Node.newBoolean(false));
    Node assignmentNode1 = Node.newAssignment(Token.VAR, "x", Node.newNumber(5));
    Node assignmentNode2 = Node.newAssignment(Token.VAR, "x", Node.newNumber(7));
    Node conditionNode = Node.newBoolean(true);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice ifLattice = analysis.flowThrough(ifNode, analysis.flowThrough(assignmentNode1, entryLattice));
    LiveVariableLattice assignmentLattice1 = analysis.flowThrough(conditionNode, ifLattice);
    LiveVariableLattice assignmentLattice2 = analysis.flowThrough(assignmentNode2, assignmentLattice1);

    assertTrue(assignmentLattice2.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testFunctionWithLocalVariables() {
    // Replace this with the actual code to set up the test case nodes for a function with local variables.
    Node functionNode = Node.newFunction(null, null, null, null);
    Node localVariableAssignmentNode1 = Node.newAssignment(Token.VAR, "x", Node.newNumber(5));
    Node localVariableAssignmentNode2 = Node.newAssignment(Token.VAR, "y", Node.newNumber(10));
    Node functionBodyNode = Node.newBlock(null);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice functionEntryLattice = analysis.flowThrough(functionNode, entryLattice);
    LiveVariableLattice functionBodyLattice = analysis.flowThrough(functionBodyNode, functionEntryLattice);

    Set<Var> result = analyzer.getEscapedLocals();
    Set<Var> expectedEscapedLocals = ImmutableSet.of();

    assertEquals(expectedEscapedLocals, result);

    assertTrue(functionBodyLattice.isLive(analysis.getVarIndex("x")));
    assertTrue(functionBodyLattice.isLive(analysis.getVarIndex("y")));
  }

  @Test
  public void testFunctionWithEscapedVariable() {
    // Replace this with the actual code to set up the test case nodes for a function with an escaped variable.
    Node functionNode = Node.newFunction(null, null, null, null);
    Node argumentNode = Node.newName("arguments");
    Node localVariableAssignmentNode = Node.newAssignment(Token.VAR, "x", argumentNode);
    Node functionBodyNode = Node.newBlock(null);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice functionEntryLattice = analysis.flowThrough(functionNode, entryLattice);
    LiveVariableLattice argumentLattice = analysis.flowThrough(argumentNode, functionEntryLattice);
    LiveVariableLattice functionBodyLattice = analysis.flowThrough(functionBodyNode, argumentLattice);

    Set<Var> result = analyzer.getEscapedLocals();
    Set<Var> expectedEscapedLocals = ImmutableSet.of(jsScope.getVar("arguments"));

    assertEquals(expectedEscapedLocals, result);

    assertTrue(functionBodyLattice.isLive(analysis.getVarIndex("x")));
  }

  @Test
  public void testNestedFunction() {
    // Replace this with the actual code to set up the test case nodes for nested functions.
    Node outerFunctionNode = Node.newFunction(null, null, null, null);
    Node outerVariableAssignmentNode = Node.newAssignment(Token.VAR, "x", Node.newNumber(5));
    Node outerFunctionBodyNode = Node.newBlock(null);
    Node innerFunctionNode = Node.newFunction(null, null, null, null);
    Node innerVariableAssignmentNode = Node.newAssignment(Token.VAR, "y", Node.newNumber(10));
    Node innerFunctionBodyNode = Node.newBlock(null);
    Node outerFunctionBodyNode = Node.newBlock(null);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, null);

    LiveVariableLattice outerFunctionEntryLattice = analysis.flowThrough(outerFunctionNode, analysis.createEntryLattice());
    LiveVariableLattice outerVariableLattice = analysis.flowThrough(outerVariableAssignmentNode, outerFunctionEntryLattice);
    LiveVariableLattice outerFunctionBodyLattice = analysis.flowThrough(outerFunctionBodyNode, outerVariableLattice);

    LiveVariableLattice innerFunctionEntryLattice = analysis.flowThrough(innerFunctionNode, outerFunctionBodyLattice);
    LiveVariableLattice innerVariableLattice = analysis.flowThrough(innerVariableAssignmentNode, innerFunctionEntryLattice);
    LiveVariableLattice innerFunctionBodyLattice = analysis.flowThrough(innerFunctionBodyNode, innerVariableLattice);

    Set<Var> expectedEscapedLocals = ImmutableSet.of();
    Set<Var> result = analyzer.getEscapedLocals();

    assertEquals(expectedEscapedLocals, result);

    assertTrue(outerFunctionBodyLattice.isLive(analysis.getVarIndex("x")));
    assertTrue(innerFunctionBodyLattice.isLive(analysis.getVarIndex("y")));
  }
}