import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleVariableDefinition() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignNode = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
    Node useNode = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, assignNode, useNode));

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDef = Node.newName("b");
    Node actualDef = analysis.getDef("a", useNode);

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInCondition() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignNode = Node.newBinaryOp("!=", Node.newName("a"), Node.newName("b"));
    Node assignNodeWithDef = Node.newBlock(
        "", List.of(assignNode, Node.newName("c")), Node.newName("c"));
    Node useNode = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, useNode, assignNodeWithDef), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDef = Node.newName("c");
    Node actualDef = analysis.getDef("a", useNode);

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node whileConditionNode = Node.newName("condition");
    Node assignNode = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
    Node loopNode = Node.newFor(
        whileConditionNode, Node.newName("i"), Node.newList(Node.newName("i"), Node.newName("i"), Node.newName("1")), assignNode);
    Node useNode = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, loopNode, useNode), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDef = Node.newName("b");
    Node actualDef = analysis.getDef("a", useNode);

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testMultipleVariableDefinitions() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignA = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
    Node assignB = Node.newBinaryOp("=", Node.newName("b"), Node.newNumber(42));
    Node useNodeA = Node.newName("a");
    Node useNodeB = Node.newName("b");

    Node program = Node.newBlock("", List.of(entryNode, assignA, assignB, useNodeA, useNodeB), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDefA = Node.newName("b");
    Node actualDefA = analysis.getDef("a", useNodeA);
    assertEquals(expectedDefA, actualDefA);

    Node expectedDefB = Node.newNumber(42);
    Node actualDefB = analysis.getDef("b", useNodeB);
    assertEquals(expectedDefB, actualDefB);
  }

  @Test
  public void testVariablesInFunctionCall() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node functionCallNode = Node.newCall(Node.newName("func"), Node.newList(Node.newName("a")));
    Node useNodeA = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, functionCallNode, useNodeA), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDefA = Node.newName("functionCallNode.A"); // Assuming "func" has a local variable "A"
    Node actualDefA = analysis.getDef("a", useNodeA);

    assertEquals(expectedDefA, actualDefA);
  }

  @Test
  public void testVariablesInFunctionDefinition() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node functionDefinitionNode = Node.newFunction("func", Node.newList(Node.newName("a")), null, Node.newBlock("", List.of()));
    Node useNodeA = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, functionDefinitionNode, useNodeA), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDefA = Node.newName("functionDefinitionNode.A"); // Assuming "func" has a local variable "A"
    Node actualDefA = analysis.getDef("a", useNodeA);

    assertEquals(expectedDefA, actualDefA);
  }

  @Test(expected = IllegalStateException.class)
  public void testEscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignNode = Node.newBinaryOp("=", Node.newName("a"), Node.newName("window"));
    Node useNode = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, assignNode, useNode), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDef = null;
    Node actualDef = analysis.getDef("a", useNode);

    assertNull(actualDef);
  }

  @Test
  public void testVariableDefinitionInCatchBlock() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node tryBlockStartNode = Node.newTryStatement();
    Node tryBlockBodyNode = Node.newBlock("", List.of());
    Node catchBlockStartNode = new Node(Token.TRY_CATCH, new Node[0], null);
    Node catchBlockBodyNode = Node.newBlock("", List.of(Node.newName("a")), null);
    Node finallyBlockNode = Node.newBlock("", List.of(), null);
    Node useNode = Node.newName("a");

    Node program = Node.newBlock("", List.of(entryNode, tryBlockStartNode, tryBlockBodyNode, catchBlockStartNode, catchBlockBodyNode, finallyBlockNode, useNode), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDef = Node.newName("catchBlockBodyNode.a"); // Assuming "a" is defined in "catchBlockBodyNode"
    Node actualDef = analysis.getDef("a", useNode);

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableUseBeforeDefinition() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignNode = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
    Node useNodeA = Node.newName("a");

    Node program = Node.newBlock("", List.of(useNodeA, assignNode), null);

    try {
      analysis.compute(program, analysis.createEntryLattice());
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testVariableUseAfterDefinitionButBeforeAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignNode = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
    Node useNodeA = Node.newName("a");

    Node program = Node.newBlock("", List.of(useNodeA, entryNode, assignNode), null);

    try {
      analysis.compute(program, analysis.createEntryLattice());
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testVariableUseAfterAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node entryNode = jsScope.getRootNode();
    Node assignNode = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
    Node useNodeA = Node.newName("a");

    Node program = Node.newBlock("", List.of(assignNode, useNodeA), null);

    analysis.compute(program, analysis.createEntryLattice());
    Node expectedDef = Node.newName("assignNode.a");
    Node actualDef = analysis.getDef("a", useNodeA);

    assertEquals(expectedDef, actualDef);
  }
}