import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Assuming the setup logic for creating ControlFlowGraph, Scope and AbstractCompiler is provided
    // by the test environment.
    this.cfg = new ControlFlowGraph<>();
    this.jsScope = new Scope(null);
    this.compiler = new AbstractCompiler(cfg, jsScope);
  }

  @Test
  public void testSimpleVariableDefinition() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node use = Node.newIdentifier("a");
    Node definition = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));

    analysis.compute(definition, analysis.createEntryLattice(), null);
    Node def = analysis.getDef("a", use);

    assertEquals(definition, def);
  }

  @Test
  public void testMultipleVariableDefinitions() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useA = Node.newIdentifier("a");
    Node useB = Node.newIdentifier("b");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node definitionB = Node.newAssignment(Node.newName("b"), Node.newLiteral(2));

    analysis.compute(definitionA, analysis.createEntryLattice(), null);
    analysis.compute(definitionB, analysis.createEntryLattice(), definitionA);

    Node defA = analysis.getDef("a", useA);
    Node defB = analysis.getDef("b", useB);

    assertEquals(definitionA, defA);
    assertEquals(definitionB, defB);
  }

  @Test
  public void testVariableDefinitionDependsOnAnotherVariable() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";
    String varB = "b";

    Node useA = Node.newIdentifier("a");
    Node useB = Node.newIdentifier("b");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newIdentifier("b"));

    analysis.compute(definitionA, analysis.createEntryLattice(), null);

    // Since 'b' is not defined in this example, the reached definition of 'a' should be null.
    assertNull(analysis.getDef(varA, useA));
  }

  @Test
  public void testVariableDefinedInAnotherFunction() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";
    String varB = "b";

    // Function defining 'a'
    Node functionA = Node.newFunction("functionA", ImmutableSet.of("b"), null, null);
    Node useA = Node.newIdentifier("a");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node functionAEntry = Node.newFunctionEntry(functionA);

    // Function using 'a'
    Node functionB = Node.newFunction("functionB", ImmutableSet.of(), null, null);
    Node useB = Node.newIdentifier("a");
    Node functionBEntry = Node.newFunctionEntry(functionB);

    analysis.compute(functionAEntry, analysis.createEntryLattice(), null);
    analysis.compute(functionBEntry, analysis.createEntryLattice(), functionAEntry);

    Node defA = analysis.getDef(varA, useA);
    assertEquals(definitionA, defA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidVariableName() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node useInvalid = Node.newIdentifier("$invalid");

    Node definition = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));

    analysis.compute(definition, analysis.createEntryLattice(), null);
    analysis.getDef("$invalid", useInvalid);
  }

  @Test
  public void testVariableInCondition() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";

    Node useA = Node.newIdentifier("a");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node condition = Node.newBinaryOp(Token.OP_LOGICAL_OR, Node.newLiteral(true), Node.newIdentifier("a"));

    analysis.compute(definitionA, analysis.createEntryLattice(), null);
    analysis.compute(condition, analysis.createEntryLattice(), definitionA);

    Node defA = analysis.getDef(varA, useA);
    assertEquals(definitionA, defA);
  }

  @Test
  public void testVariableInLoop() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";

    Node useA = Node.newIdentifier("a");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node loopCondition = Node.newIdentifier("a");
    Node loopBody = Node.newEmptyStatement();
    Node loop = Node.newForStatement(loopCondition, loopBody, null, null);

    analysis.compute(definitionA, analysis.createEntryLattice(), null);
    analysis.compute(loop, analysis.createEntryLattice(), definitionA);

    Node defA = analysis.getDef(varA, useA);
    assertEquals(definitionA, defA);
  }

  @Test
  public void testVariableInNestedFunction() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";

    // Outer function
    Node outerFunction = Node.newFunction("outerFunction", ImmutableSet.of(), null, null);
    Node useA = Node.newIdentifier("a");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node outerFunctionEntry = Node.newFunctionEntry(outerFunction);

    // Inner function
    Node innerFunction = Node.newFunction("innerFunction", ImmutableSet.of(), null, null);
    Node innerFunctionEntry = Node.newFunctionEntry(innerFunction);
    Node innerUseA = Node.newIdentifier("a");

    analysis.compute(outerFunctionEntry, analysis.createEntryLattice(), null);
    analysis.compute(innerFunctionEntry, analysis.createEntryLattice(), outerFunctionEntry);

    Node defA = analysis.getDef(varA, useA);
    assertEquals(definitionA, defA);
  }

  @Test
  public void testVariableInCatchBlock() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";

    Node useA = Node.newIdentifier("a");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node tryBlock = Node.newTryStatement(null, null, null, null);
    Node catchBlock = Node.newCatchBlock(Node.newName("e"), null, null);
    Node useAInCatch = Node.newIdentifier("a");

    analysis.compute(definitionA, analysis.createEntryLattice(), null);
    analysis.compute(tryBlock, analysis.createEntryLattice(), definitionA);
    analysis.compute(catchBlock, analysis.createEntryLattice(), tryBlock);

    Node defA = analysis.getDef(varA, useA);
    assertEquals(definitionA, defA);
  }

  @Test
  public void testVariableDefinedInCatchBlock() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    String varA = "a";

    Node useA = Node.newIdentifier("a");
    Node definitionA = Node.newAssignment(Node.newName("a"), Node.newLiteral(1));
    Node tryBlock = Node.newTryStatement(null, null, null, null);
    Node catchBlock = Node.newCatchBlock(Node.newName("e"), null, null);
    Node definitionAInCatch = Node.newAssignment(Node.newName("a"), Node.newLiteral(2));

    analysis.compute(definitionA, analysis.createEntryLattice(), null);
    analysis.compute(definitionAInCatch, analysis.createEntryLattice(), definitionA);
    analysis.compute(tryBlock, analysis.createEntryLattice(), definitionA);
    analysis.compute(catchBlock, analysis.createEntryLattice(), tryBlock);

    Node defA = analysis.getDef(varA, useA);
    assertEquals(definitionAInCatch, defA);
  }
}