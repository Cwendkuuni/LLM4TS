import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.MustDefJoin;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @org.junit.Test
  public void testBasicFunctionality() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node varB = Node.newVar("b");
    Node addNode = Node.newName("add");
    Node assignNode = Node.newAssign(varA, addNode, Node.newNumber(1));
    Node assignNodeB = Node.newAssign(varB, Node.newNumber(2));
    Node ifNode = Node.newIf(Node.newBool(true));
    Node addNodeAB = Node.newName("add");
    Node assignNodeC = Node.newAssign(Node.newVar("c"), addNodeAB, varA, varB);
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, varA);
    cfg.addEdge(varA, addNode);
    cfg.addEdge(addNode, assignNode);
    cfg.addEdge(entryNode, varB);
    cfg.addEdge(varB, assignNodeB);
    cfg.addEdge(assignNodeB, addNodeAB);
    cfg.addEdge(addNodeAB, assignNodeC);
    cfg.addEdge(assignNodeC, returnNode);

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("a"), new Definition(assignNode));
    expected.reachingDef.put(jsScope.getVar("b"), new Definition(assignNodeB));
    expected.reachingDef.put(jsScope.getVar("c"), new Definition(assignNodeC));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testFunctionCall() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node callNode = Node.newCall(Node.newName("function"), varA);
    Node assignNode = Node.newAssign(Node.newVar("c"), Node.newNumber(3));
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, varA);
    cfg.addEdge(varA, callNode);
    cfg.addEdge(callNode, returnNode);
    cfg.addEdge(returnNode, assignNode);

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("a"), new Definition(varA));
    expected.reachingDef.put(jsScope.getVar("c"), new Definition(assignNode));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testControlFlow() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node ifNode = Node.newIf(Node.newBool(true));
    Node assignNodeA = Node.newAssign(varA, Node.newNumber(1));
    Node assignNodeB = Node.newAssign(Node.newVar("b"), Node.newNumber(2));
    Node elseNode = Node.newElse();
    Node assignNodeAC = Node.newAssign(varA, Node.newNumber(3));
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, ifNode);
    cfg.addEdge(ifNode, assignNodeA);
    cfg.addEdge(ifNode, returnNode);
    cfg.addEdge(elseNode, assignNodeB);
    cfg.addEdge(elseNode, assignNodeAC);
    cfg.addEdge(returnNode, assignNodeA);

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("a"), new Definition(assignNodeA));
    expected.reachingDef.put(jsScope.getVar("b"), new Definition(assignNodeB));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testExceptionHandling() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node tryNode = Node.newTry();
    Node assignNodeA = Node.newAssign(varA, Node.newNumber(1));
    Node catchNode = Node.newCatch(Node.newException("Exception"));
    Node assignNodeB = Node.newAssign(Node.newVar("b"), Node.newNumber(2));
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, tryNode);
    cfg.addEdge(tryNode, assignNodeA);
    cfg.addEdge(tryNode, returnNode);
    cfg.addEdge(catchNode, assignNodeB);
    cfg.addEdge(catchNode, returnNode);

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("a"), new Definition(assignNodeA));
    expected.reachingDef.put(jsScope.getVar("b"), new Definition(assignNodeB));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testEscapedVariables() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node varB = Node.newVar("b");
    Node escNode = Node.newVar("escaped");
    Node assignNodeA = Node.newAssign(varA, Node.newNumber(1));
    Node assignNodeB = Node.newAssign(varB, Node.newNumber(2));
    Node assignNodeESCAPed = Node.newAssign(escNode, varA);
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, varA);
    cfg.addEdge(varA, assignNodeA);
    cfg.addEdge(entryNode, varB);
    cfg.addEdge(varB, assignNodeB);
    cfg.addEdge(assignNodeA, assignNodeESCAPed);
    cfg.addEdge(assignNodeESCAPed, returnNode);

    // Set escaped variable
    analysis.escaped.add(jsScope.getVar("a"));

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("b"), new Definition(assignNodeB));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testUseOfOuterScopeVariables() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node outerVar = Node.newVar("outer");
    Node varB = Node.newVar("b");
    Node assignNodeA = Node.newAssign(varA, Node.newNumber(1));
    Node assignNodeB = Node.newAssign(varB, outerVar);
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, varA);
    cfg.addEdge(entryNode, outerVar);
    cfg.addEdge(outerVar, assignNodeB);
    cfg.addEdge(assignNodeB, returnNode);

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDef();
    expected.reachingDef.put(jsScope.getVar("a"), new Definition(assignNodeA));
    expected.reachingDef.put(jsScope.getVar("b"), new Definition(assignNodeB));

    assertTrue(analysis.dependsOnOuterScopeVars("b", entryNode));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testMultipleDefinitions() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Add some nodes to the CFG
    Node entryNode = Node.newBlock("entry");
    Node varA = Node.newVar("a");
    Node assignNodeA1 = Node.newAssign(varA, Node.newNumber(1));
    Node assignNodeA2 = Node.newAssign(varA, Node.newNumber(2));
    Node returnNode = Node.newName("return");

    // Add edges between nodes
    cfg.addEdge(entryNode, varA);
    cfg.addEdge(varA, assignNodeA1);
    cfg.addEdge(entryNode, varA);
    cfg.addEdge(varA, assignNodeA2);
    cfg.addEdge(assignNodeA1, returnNode);
    cfg.addEdge(assignNodeA2, returnNode);

    // Run the analysis
    MustDef result = analysis.compute();

    // Check the result
    MustDef expected = new MustDefJoin().join(
        new MustDef(jsScope.getVars()),
        new MustDef(jsScope.getVars()) {{
          reachingDef.put(jsScope.getVar("a"), new Definition(assignNodeA1));
        }},
        new MustDef(jsScope.getVars()) {{
          reachingDef.put(jsScope.getVar("a"), new Definition(assignNodeA2));
        }}
    );

    assertEquals(expected, result);
  }
}