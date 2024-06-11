import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testBasicFlow() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newNumber(1.0));
    Node useA = Node.newName("A", Token.NAME);
    Node printA = Node.newCall("print", Token.CALL, Node.newThis(), useA);

    entryNode.addChild(defA);
    entryNode.addChild(assignA);
    entryNode.addChild(printA);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> uses = analysis.getUses("A", entryNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useA));
    assertEquals(1, uses.size());

    // The result should be the same as the entry node
    assertEquals(result, analysis.createEntryLattice());
  }

  @Test
  public void testAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newName("B", Token.NAME));
    Node useA = Node.newName("A", Token.NAME);
    Node printA = Node.newCall("print", Token.CALL, Node.newThis(), useA);

    entryNode.addChild(defA);
    entryNode.addChild(assignA);
    entryNode.addChild(printA);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> uses = analysis.getUses("A", entryNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useA));
    assertEquals(1, uses.size());

    // The result should have only the definition node for A
    ReachingUses entry = analysis.createEntryLattice();
    assertEquals(entry, result);
  }

  @Test
  public void testIfStatement() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node cond = Node.newBinaryOp(Token.LOGICAL_OR, Node.newName("x", Token.NAME), Node.newNumber(1));
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newNumber(1));
    Node useA = Node.newName("A", Token.NAME);
    Node printA = Node.newCall("print", Token.CALL, Node.newThis(), useA);

    Node ifNode = Node.newIf(Token.IF, cond, assignA, Node.newBlock(Token.BLOCK, null));
    entryNode.addChild(defA);
    entryNode.addChild(ifNode);
    entryNode.addChild(printA);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> uses = analysis.getUses("A", entryNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useA));
    assertEquals(1, uses.size());

    // The result should have only the definition node for A if the condition is true
    ReachingUses entryTrue = analysis.createEntryLattice();
    entryTrue.mayUseMap.put(jsScope.getVar("A"), assignA);
    ReachingUses entryFalse = new ReachingUses();
    assertEquals(entryFalse, result);

    // The result should have only the definition node for A if the condition is true
    ReachingUses trueState = analysis.flowThrough(cond, entryTrue);
    assertEquals(assignA, trueState.mayUseMap.get(jsScope.getVar("A")).getFirst());
  }

  @Test
  public void testForStatement() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node forNode = Node.newFor(Token.FOR, Node.newName("i", Token.NAME), Node.newName("j", Token.NAME), Node.newName("j", Token.NAME), Node.newBlock(Token.BLOCK, null));
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newNumber(1));
    Node useA = Node.newName("A", Token.NAME);
    Node printA = Node.newCall("print", Token.CALL, Node.newThis(), useA);

    entryNode.addChild(defA);
    entryNode.addChild(forNode);
    entryNode.addChild(assignA);
    entryNode.addChild(printA);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> uses = analysis.getUses("A", entryNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useA));
    assertEquals(1, uses.size());

    // The result should have only the definition node for A
    ReachingUsuses entry = analysis.createEntryLattice();
    assertEquals(entry, result);
  }

  @Test
  public void testWhileStatement() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node cond = Node.newBinaryOp(Token.LOGICAL_AND, Node.newName("x", Token.NAME), Node.newNumber(1));
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newNumber(1));
    Node useA = Node.newName("A", Token.NAME);
    Node printA = Node.newCall("print", Token.CALL, Node.newThis(), useA);

    Node whileNode = Node.newWhile(Token.WHILE, cond, Node.newBlock(Token.BLOCK, null));
    entryNode.addChild(defA);
    entryNode.addChild(assignA);
    entryNode.addChild(whileNode);
    entryNode.addChild(printA);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> uses = analysis.getUses("A", entryNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useA));
    assertEquals(1, uses.size());

    // The result should have only the definition node for A
    ReachingUses entry = analysis.createEntryLattice();
    assertEquals(entry, result);
  }

  @Test
  public void testFunctionCall() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node useA = Node.newName("A", Token.NAME);
    Node call = Node.newCall("print", Token.CALL, Node.newThis(), useA);

    Node funcNode = Node.newFunction(Token.FUNCTION, null, Node.newBlock(Token.BLOCK, null), new Token[0], Node.newBlock(Token.BLOCK, null));
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newNumber(1));
    funcNode.addChild(defA);
    funcNode.addChild(assignA);

    entryNode.addChild(funcNode);
    entryNode.addChild(call);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> uses = analysis.getUses("A", entryNode);
    assertFalse(uses.isEmpty());
    assertTrue(uses.contains(useA));
    assertEquals(1, uses.size());

    // The result should have only the definition node for A
    ReachingUses entry = analysis.createEntryLattice();
    assertEquals(entry, result);
  }

  @Test
  public void testComplexFlow() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope, new CompilerOptions());

    Node entryNode = Node.newBlock(Token.BLOCK, null);
    Node defA = Node.newName("A", Token.NAME, Token.NO_INIT);
    Node defB = Node.newName("B", Token.NAME, Token.NO_INIT);
    Node assignA = Node.newAssign(Token.ASSIGN, Node.newName("A", Token.NAME), Node.newNumber(1));
    Node assignB = Node.newAssign(Token.ASSIGN, Node.newName("B", Token.NAME), Node.newNumber(2));
    Node useA = Node.newName("A", Token.NAME);
    Node useB = Node.newName("B", Token.NAME);
    Node printA = Node.newCall("print", Token.CALL, Node.newThis(), useA);
    Node printB = Node.newCall("print", Token.CALL, Node.newThis(), useB);

    Node ifNode = Node.newIf(Token.IF, Node.newBinaryOp(Token.LOGICAL_AND, Node.newName("x", Token.NAME), Node.newNumber(1)), assignA, Node.newBlock(Token.BLOCK, null));
    Node andNode = Node.newBinaryOp(Token.LOGICAL_AND, Node.newName("y", Token.NAME), Node.newNumber(1));
    Node orNode = Node.newBinaryOp(Token.LOGICAL_OR, Node.newName("z", Token.NAME), Node.newNumber(1));

    Node entryIf = Node.newBlock(Token.BLOCK, null);
    Node entryAnd = Node.newBlock(Token.BLOCK, null);
    Node entryOr = Node.newBlock(Token.BLOCK, null);

    entryIf.addChild(defA);
    entryIf.addChild(ifNode);
    entryIf.addChild(assignB);
    entryIf.addChild(printB);
    entryIf.addChild(printA);

    entryAnd.addChild(defA);
    entryAnd.addChild(andNode);
    entryAnd.addChild(assignB);
    entryAnd.addChild(printB);
    entryAnd.addChild(printA);
    entryAnd.addChild(ifNode);

    entryOr.addChild(defA);
    entryOr.addChild(orNode);
    entryOr.addChild(assignB);
    entryOr.addChild(printB);
    entryOr.addChild(printA);
    entryOr.addChild(ifNode);

    entryNode.addChild(entryIf);
    entryNode.addChild(entryAnd);
    entryNode.addChild(entryOr);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);
    ReachingUses result = analysis.compute(entryNode);

    Collection<Node> usesA = analysis.getUses("A", entryNode);
    Collection<Node> usesB = analysis.getUses("B", entryNode);

    // A should only be defined at the root node
    assertEquals(1, usesA.size());
    assertFalse(usesA.contains(assignA));
    assertTrue(usesA.contains(useA));
    assertTrue(usesA.contains(printA));

    // B should be defined at both ifNode and assignB
    assertEquals(2, usesB.size());
    assertTrue(usesB.contains(assignB));
    assertTrue(usesB.contains(printB));
    assertFalse(usesB.contains(useB));
    assertFalse(usesB.contains(printA));
  }
}