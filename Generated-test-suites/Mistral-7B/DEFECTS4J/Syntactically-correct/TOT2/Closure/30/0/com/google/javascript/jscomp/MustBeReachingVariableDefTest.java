import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleVariableDefinition() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node assignment = Node.newAssign(root, Node.newName("x"), Node.newNumber(42));
    root.addChild(assignment);

    Node use = Node.newGetProp(Node.newName("x"), Node.newIdentifier("length"));

    analysis.compute(root.getFirstChild());
    Node expectedDef = analysis.getDef("x", use);
    Node actualDef = assignment;

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInCondition() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node ifNode = Node.newIf(root, Node.newBinaryOp(Node.newName("x"), Token.LT, Node.newNumber(0)));

    Node assignment = Node.newAssign(ifNode, Node.newName("y"), Node.newNumber(42));
    Node use = Node.newGetProp(Node.newName("y"), Node.newIdentifier("length"));

    root.addChild(Node.newAssign(Node.newName("x"), Node.newNumber(3)));
    root.addChild(assignment);
    root.addChild(use);

    analysis.compute(root.getFirstChild());
    Node expectedDef = analysis.getDef("y", use);
    Node actualDef = assignment;

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInLoop() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node forNode = Node.newFor(root, Node.newName("i"), Node.newNumber(0), Node.newNumber(5));

    Node assignment = Node.newAssign(forNode, Node.newName("j"), Node.newNumber(42));
    Node use = Node.newGetProp(Node.newName("j"), Node.newIdentifier("length"));

    forNode.addChild(assignment);
    forNode.addChild(use);

    root.addChild(forNode);

    analysis.compute(root.getFirstChild());
    Node expectedDef = analysis.getDef("j", use);
    Node actualDef = assignment;

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInCatchBlock() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");

    Node tryBlock = Node.newTry(root);
    Node catchBlock = Node.newCatch(tryBlock, Node.newName("e"), Node.newIdentifier("Class"));

    Node assignment = Node.newAssign(catchBlock, Node.newName("x"), Node.newNumber(42));
    Node use = Node.newGetProp(Node.newName("x"), Node.newIdentifier("length"));

    tryBlock.addChild(assignment);
    tryBlock.addChild(use);
    catchBlock.addChild(use);

    root.addChild(tryBlock);

    analysis.compute(root.getFirstChild());
    Node expectedDef = analysis.getDef("x", use);
    Node actualDef = assignment;

    assertEquals(expectedDef, actualDef);
  }

  @Test(expected = AssertionError.class)
  public void testUndefinedVariable() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node use = Node.newGetProp(Node.newName("y"), Node.newIdentifier("length"));

    analysis.compute(root.getFirstChild());

    assertNull(analysis.getDef("y", use));
  }

  @Test
  public void testVariableThatDoesNotExistInScope() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node use = Node.newGetProp(Node.newName("outerVariable"), Node.newIdentifier("length"));

    analysis.compute(root.getFirstChild());

    assertNull(analysis.getDef("outerVariable", use));
  }

  @Test
  public void testVariableDefinitionInFunction() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node function = Node.newFunction("myFunction", Node.newList(), root);
    Node assignment = Node.newAssign(function, Node.newName("x"), Node.newNumber(42));
    Node use = Node.newGetProp(Node.newName("x"), Node.newIdentifier("length"));

    root.addChild(function);
    function.addChild(assignment);
    function.addChild(use);

    analysis.compute(root.getFirstChild());
    Node expectedDef = analysis.getDef("x", use);
    Node actualDef = assignment;

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInAnonymousFunction() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node function = Node.newExpressionStatement(
        Node.newAssign(
            Node.newVariable("x"),
            Node.newFunction(null, Node.newList(), Node.newBlock("func"))));
    Node block = Node.newBlock("func");
    Node assignment = Node.newAssign(block, Node.newName("y"), Node.newNumber(42));
    Node use = Node.newGetProp(Node.newName("y"), Node.newIdentifier("length"));

    root.addChild(function);
    function.addChild(block);
    block.addChild(assignment);
    block.addChild(use);

    analysis.compute(root.getFirstChild());
    Node expectedDef = analysis.getDef("y", use);
    Node actualDef = assignment;

    assertEquals(expectedDef, actualDef);
  }

  @Test
  public void testVariableDefinitionInFunctionCall() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node functionCall = Node.newCall(root, Node.newName("myFunction"), Node.newList());
    Node use = Node.newGetProp(Node.newName("x"), Node.newIdentifier("length"));

    analysis.compute(root.getFirstChild());
    // The definition of 'x' should be in 'myFunction'
    Node expectedDef = analysis.getDef("x", use);
    Node actualDef = cfg.getEntryNode();

    assertTrue(expectedDef != null && expectedDef.equals(actualDef));
  }

  @Test (expected = AssertionError.class)
  public void testVariableThatEscapes() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node variable = Node.newVar("x");
    Node assignment = Node.newAssign(root, variable, Node.newNumber(42));
    Node returnNode = Node.newReturn(root, Node.newName("x"));

    root.addChild(variable);
    root.addChild(assignment);
    root.addChild(returnNode);

    analysis.compute(root.getFirstChild());

    assertNull(analysis.getDef("x", Node.newIdentifier("x")));
  }

  @Test
  public void testVariableThatDependsOnOuterScopeVariable() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("root");
    Node outerVariable = Node.newVar("outerVariable");
    Node variable = Node.newVar("x");
    Node assignment = Node.newAssign(root, variable, Node.newName("outerVariable"));
    Node use = Node.newGetProp(Node.newName("x"), Node.newIdentifier("length"));

    root.addChild(outerVariable);
    root.addChild(variable);
    root.addChild(assignment);
    root.addChild(use);

    analysis.compute(root.getFirstChild());

    assertFalse(dependsOnOuterScopeVars("x", use));
  }

}