import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleAssignment() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; x = x + 2; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("x");
    Node defNode = analysis.getDef("x", useNode);
    assertEquals( Node.newNumber(7), defNode );
  }

  @Test
  public void testConditionalAssignment() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; if (true) { x = 3; } else { x = 7; } }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNodeTrue = Node.newGlobal("x");
    Node defNodeTrue = analysis.getDef("x", useNodeTrue);
    assertEquals( Node.newNumber(3), defNodeTrue );

    Node useNodeFalse = Node.newGlobal("x");
    Node defNodeFalse = analysis.getDef("x", useNodeFalse);
    assertEquals( Node.newNumber(7), defNodeFalse );
  }

  @Test
  public void testMultipleAssignments() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; x = x + 2; x = x * 2; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("x");
    Node defNode = analysis.getDef("x", useNode);
    assertEquals( Node.newNumber(12), defNode );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUseNode() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = null;
    analysis.getDef("x", useNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDefNode() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("x");
    Node defNode = Node.newUndefined();
    analysis.getDef("x", useNode, defNode);
  }

  @Test
  public void testFunctionCall() {
    Node rootNode = Node.newBlock("function foo() { function bar() { var x = 5; } x = bar(); }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("x");
    Node defNode = analysis.getDef("x", useNode);
    assertEquals( Node.newFunction("bar", new Node[0]), defNode );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUndefinedVariable() {
    Node rootNode = Node.newBlock("function foo() { undefined = 5; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("undefined");
    analysis.getDef("undefined", useNode);
  }

  @Test
  public void testEscapeVariable() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; escape('x'); x = 3; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("x");
    Node defNode = analysis.getDef("x", useNode);
    assertNull(defNode);
  }

  @Test
  public void testUseOuterScopeVariable() {
    Node rootNode = Node.newBlock("function foo() { function bar() { var x = 5; } var y = x; } var z = foo.bar();");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("z");
    Node defNode = analysis.getDef("x", useNode);
    assertNull(defNode);

    assertTrue(analysis.dependsOnOuterScopeVars("x", useNode));
  }

  @Test
  public void testUseFunctionParameter() {
    Node rootNode = Node.newBlock("function foo(x) { var y = x; }");
    Scope jsScope = new Scope(rootNode);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode, jsScope);
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = Node.newGlobal("arguments.callee.x");
    Node defNode = analysis.getDef("x", useNode);
    assertNull(defNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; }");
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
    public void testNullCompiler() {
    Node rootNode = Node.newBlock("function foo() { var x = 5; }");
    Scope jsScope = new Scope(rootNode);
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        null, jsScope, null);
  }
}