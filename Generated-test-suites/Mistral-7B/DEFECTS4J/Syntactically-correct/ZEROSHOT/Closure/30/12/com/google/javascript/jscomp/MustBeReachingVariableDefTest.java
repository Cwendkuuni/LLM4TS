import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testBasicFlow() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node aAssign = new Node(Token.ASSIGN, root, new Node(Token.NAME, "a"),
        new Node(Token.INT_LITERAL, 5));
    root.addChild(aAssign);

    Node bAssign = new Node(Token.ASSIGN, root, new Node(Token.NAME, "b"),
        new Node(Token.INT_LITERAL, 10));
    root.addChild(bAssign);

    Node cAssign = new Node(Token.ASSIGN, root, new Node(Token.NAME, "c"),
        new Node(Token.NAME, "a"));
    root.addChild(cAssign);

    Node printC = new Node(Token.CALL, root, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "c"));
    root.addChild(printC);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defA = analyzer.getDef("a", printC);
    Node defB = analyzer.getDef("b", bAssign);
    Node defC = analyzer.getDef("c", printC);

    assertEquals(aAssign, defA);
    assertEquals(bAssign, defB);
    assertEquals(aAssign, defC);

    assertFalse(analyzer.dependsOnOuterScopeVars("a", printC));
    assertFalse(analyzer.dependsOnOuterScopeVars("b", bAssign));
    assertFalse(analyzer.dependsOnOuterScopeVars("c", printC));
  }

  @Test
  public void testLoopFlow() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node iAssign = new Node(Token.ASSIGN, root, new Node(Token.NAME, "i"),
        new Node(Token.INT_LITERAL, 0));
    Node loopCondition = new Node(Token.WHILE, root, iAssign, new Node(Token.NAME, "i"),
        new Node(Token.INT_LITERAL, 10));
    Node increment = new Node(Token.ASSIGN, loopCondition, new Node(Token.NAME, "i"),
        new Node(Token.PLUS, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 1)));
    root.addChild(iAssign);
    root.addChild(loopCondition);
    root.addChild(increment);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defI = analyzer.getDef("i", increment);

    assertEquals(iAssign, defI);
  }

  @Test
  public void testFunctionCallFlow() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node callFunction = new Node(Token.CALL, root, new Node(Token.NAME, "myFunc"),
        new Node(Token.INT_LITERAL, 5));
    root.addChild(callFunction);

    Node function = new Node(Token.FUNCTION, null, new Node(Token.NAME, "myFunc"),
        new Node(Token.BLOCK));

    Node aAssign = new Node(Token.ASSIGN, function, new Node(Token.NAME, "a"),
        new Node(Token.INT_LITERAL, 10));
    Node bAssign = new Node(Token.ASSIGN, function, new Node(Token.NAME, "b"),
        new Node(Token.INT_LITERAL, 5));

    Node printA = new Node(Token.CALL, function, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "a"));
    Node printB = new Node(Token.CALL, function, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "b"));

    function.addChild(aAssign);
    function.addChild(bAssign);
    function.addChild(printA);
    function.addChild(printB);

    cfg.addNode(root);
    cfg.addNode(function);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defA = analyzer.getDef("a", printA);
    Node defB = analyzer.getDef("b", printB);

    assertEquals(aAssign, defA);
    assertEquals(bAssign, defB);
  }

  @Test
  public void testFunctionWithLoopFlow() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node callFunction = new Node(Token.CALL, root, new Node(Token.NAME, "myFunc"),
        new Node(Token.INT_LITERAL, 5));
    root.addChild(callFunction);

    Node function = new Node(Token.FUNCTION, null, new Node(Token.NAME, "myFunc"),
        new Node(Token.BLOCK));

    Node iAssign = new Node(Token.ASSIGN, function, new Node(Token.NAME, "i"),
        new Node(Token.INT_LITERAL, 0));

    Node loopCondition = new Node(Token.WHILE, function, iAssign, new Node(Token.NAME, "i"),
        new Node(Token.INT_LITERAL, 10));
    Node increment = new Node(Token.ASSIGN, loopCondition, new Node(Token.NAME, "i"),
        new Node(Token.PLUS, new Node(Token.NAME, "i"), new Node(Token.INT_LITERAL, 1)));

    Node printI = new Node(Token.CALL, function, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "i"));

    function.addChild(iAssign);
    function.addChild(loopCondition);
    function.addChild(increment);
    function.addChild(printI);

    cfg.addNode(root);
    cfg.addNode(function);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defI = analyzer.getDef("i", printI);

    assertEquals(iAssign, defI);
  }

  @Test
  public void testFunctionWithFunctionCallFlow() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node callFunction = new Node(Token.CALL, root, new Node(Token.NAME, "myFunc"),
        new Node(Token.INT_LITERAL, 5));
    root.addChild(callFunction);

    Node function = new Node(Token.FUNCTION, null, new Node(Token.NAME, "myFunc"),
        new Node(Token.BLOCK));

    Node aAssign = new Node(Token.ASSIGN, function, new Node(Token.NAME, "a"),
        new Node(Token.INT_LITERAL, 10));
    Node bAssign = new Node(Token.ASSIGN, function, new Node(Token.NAME, "b"),
        new Node(Token.INT_LITERAL, 5));

    Node innerFunction = new Node(Token.FUNCTION, function, new Node(Token.NAME, "innerFunc"),
        new Node(Token.BLOCK));

    Node innerAAssign = new Node(Token.ASSIGN, innerFunction, new Node(Token.NAME, "innerA"),
        new Node(Token.INT_LITERAL, 15));
    Node innerBAssign = new Node(Token.ASSIGN, innerFunction, new Node(Token.NAME, "innerB"),
        new Node(Token.INT_LITERAL, 10));

    Node innerPrintA = new Node(Token.CALL, innerFunction, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "innerA"));
    Node innerPrintB = new Node(Token.CALL, innerFunction, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "innerB"));

    function.addChild(aAssign);
    function.addChild(bAssign);
    function.addChild(innerFunction);
    innerFunction.addChild(innerAAssign);
    innerFunction.addChild(innerBAssign);
    innerFunction.addChild(innerPrintA);
    innerFunction.addChild(innerPrintB);

    cfg.addNode(root);
    cfg.addNode(function);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defA = analyzer.getDef("a", innerPrintA);
    Node defB = analyzer.getDef("b", innerPrintB);
    Node defInnerA = analyzer.getDef("innerA", innerPrintA);
    Node defInnerB = analyzer.getDef("innerB", innerPrintB);

    assertEquals(aAssign, defA);
    assertEquals(bAssign, defB);
    assertEquals(innerAAssign, defInnerA);
    assertEquals(innerBAssign, defInnerB);
  }

  @Test
  public void testEscapedVariable() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node function = new Node(Token.FUNCTION, null, new Node(Token.NAME, "outerFunc"),
        new Node(Token.BLOCK));
    root.addChild(function);

    Node innerFunction = new Node(Token.FUNCTION, function, new Node(Token.NAME, "innerFunc"),
        new Node(Token.BLOCK));
    Node escapeVariable = new Node(Token.ASSIGN, innerFunction, new Node(Token.NAME, "x"),
        new Node(Token.NAME, "outerFunc.x"));
    innerFunction.addChild(escapeVariable);

    function.addChild(innerFunction);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defX = analyzer.getDef("x", escapeVariable);

    assertNull(defX);
  }

  @Test
  public void testFunctionWithEscapedVariable() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(scope, compiler);

    Node root = new Node(Token.BLOCK);
    Node function = new Node(Token.FUNCTION, null, new Node(Token.NAME, "outerFunc"),
        new Node(Token.BLOCK));
    root.addChild(function);

    Var outerX = new Var(scope, new Node(Token.NAME, "x"));
    outerX.scope = function.getScope();
    outerX.scope.setParentNode(function);

    Node innerFunction = new Node(Token.FUNCTION, function, new Node(Token.NAME, "innerFunc"),
        new Node(Token.BLOCK));
    Node escapeVariable = new Node(Token.ASSIGN, innerFunction, new Node(Token.NAME, "x"),
        new Node(Token.NAME, "outerFunc.x"));
    innerFunction.addChild(escapeVariable);

    Node printX = new Node(Token.CALL, innerFunction, new Node(Token.NAME, "print"),
        new Node(Token.NAME, "x"));

    function.addChild(innerFunction);
    innerFunction.addChild(printX);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    MustDef output = analyzer.run(root);

    Node defX = analyzer.getDef("x", printX);

    assertNull(defX);
  }
}