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
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // assign a = 5;
    Node assignNode = Node.newAssign(rootNode, Node.newName("a"), Node.newIntLiteral(5));
    cfg.addNode(assignNode);

    // use a;
    Node useNode = Node.newName("a");
    cfg.addNode(useNode);

    analysis.compute(rootNode);
    Node def = analysis.getDef("a", useNode);
    assertNotNull(def);
    assertEquals(assignNode, def);
  }

  @Test
  public void testVariableDefinitionWithDependencies() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // assign a = b + c;
    Node assignNode = Node.newAssign(rootNode, Node.newName("a"), Node.newBinOp("+", Node.newName("b"), Node.newName("c")));
    cfg.addNode(assignNode);

    // use a;
    Node useNode = Node.newName("a");
    cfg.addNode(useNode);

    analysis.compute(rootNode);
    Node def = analysis.getDef("a", useNode);
    assertNotNull(def);
    assertEquals(assignNode, def);

    // check dependencies
    Set<Var> dependencies = analysis.getDependencies("a");
    assertEquals(2, dependencies.size());
    assertTrue(dependencies.contains(jsScope.getVar("b")));
    assertTrue(dependencies.contains(jsScope.getVar("c")));
  }

  @Test
  public void testVariableDefinitionWithCycle() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // a = b;
    Node assignNodeA = Node.newAssign(rootNode, Node.newName("a"), Node.newName("b"));
    cfg.addNode(assignNodeA);

    // b = a;
    Node assignNodeB = Node.newAssign(rootNode, Node.newName("b"), Node.newName("a"));
    cfg.addNode(assignNodeB);

    // use a;
    Node useNode = Node.newName("a");
    cfg.addNode(useNode);

    analysis.compute(rootNode);
    Node def = analysis.getDef("a", useNode);
    assertNotNull(def);
    assertEquals(assignNodeA, def);
  }

  @Test
  public void testVariableDefinitionWithMultipleUses() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // assign a = b + c;
    Node assignNode = Node.newAssign(rootNode, Node.newName("a"), Node.newBinOp("+", Node.newName("b"), Node.newName("c")));
    cfg.addNode(assignNode);

    // use a first time
    Node useNode1 = Node.newName("a");
    cfg.addNode(useNode1);

    // use a second time
    Node useNode2 = Node.newName("a");
    cfg.addNode(useNode2);

    analysis.compute(rootNode);
    Node def = analysis.getDef("a", useNode1);
    assertNotNull(def);
    assertEquals(assignNode, def);

    def = analysis.getDef("a", useNode2);
    assertNotNull(def);
    assertEquals(assignNode, def);
  }

  @Test
  public void testVariableDefinitionWithEscapedVariable() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // assign a = arguments[0];
    Node assignNode = Node.newAssign(rootNode, Node.newName("a"), Node.newGetProp(Node.newName("arguments"), "0"));
    cfg.addNode(assignNode);

    // use a;
    Node useNode = Node.newName("a");
    cfg.addNode(useNode);

    analysis.compute(rootNode);
    Node def = analysis.getDef("a", useNode);
    assertNull(def);
  }

  @Test
  public void testVariableDefinitionWithOuterScopeDependency() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope outerScope = new Scope();
    Scope innerScope = new Scope(outerScope);
    AbstractCompiler compiler = new Compiler(cfg, innerScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, innerScope, compiler);
    Node rootNode = Node.newBlock(1);

    // assign a = outerScope.b;
    Node outerScopeVarNode = Node.newGetProp(Node.newName("outerScope"), "b");
    Node assignNode = Node.newAssign(rootNode, Node.newName("a"), outerScopeVarNode);
    cfg.addNode(assignNode);

    // use a;
    Node useNode = Node.newName("a");
    cfg.addNode(useNode);

    analysis.compute(rootNode);
    Node def = analysis.getDef("a", useNode);
    assertNotNull(def);
    assertEquals(assignNode, def);

    // check dependency on outerScope.b
    assertTrue(analysis.dependsOnOuterScopeVars("a", useNode));
  }

  @Test(expected = NullPointerException.class)
  public void testVariableDefinitionWithNullUseNode() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // use null;
    Node useNode = null;

    analysis.getDef("a", useNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableDefinitionWithNonExistentVariable() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node rootNode = Node.newBlock(1);

    // use nonExistentVariable;
    Node useNode = Node.newName("nonExistentVariable");

    analysis.getDef("nonExistentVariable", useNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableDefinitionWithNullCfg() {
    ControlFlowGraph cfg = null;
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableDefinitionWithNullJsScope() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = null;
    AbstractCompiler compiler = new Compiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVariableDefinitionWithNullCompiler() {
    ControlFlowGraph cfg = new ControlFlowGraph();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = null;

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }
}