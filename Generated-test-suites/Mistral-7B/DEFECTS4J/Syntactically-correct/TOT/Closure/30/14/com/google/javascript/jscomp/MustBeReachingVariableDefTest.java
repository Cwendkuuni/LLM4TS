import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  private static ControlFlowGraph<Node> createControlFlowGraph(Node rootNode, Scope jsScope, AbstractCompiler compiler) {
    // Create a sample control flow graph for testing purposes.
    // This is a simple sample, feel free to add more complex cases.

    // sampleFunction(int a, int b) {
    //   int c = 1;
    //   if (a > b) {
    //     c = a + b;
    //   }
    //   return c;
    // }
    Node sampleFunction = new Node(Token.FUNCTION);
    sampleFunction.setProperty("name", "sampleFunction");
    sampleFunction.setProperty("params", new Node[]{new Node(Token.INT_PARAM), new Node(Token.INT_PARAM)});
    sampleFunction.setProperty("body", new Node(Token.BLOCK));

    Node ifStatement = new Node(Token.IF);
    ifStatement.setProperty("condition", new Node(Token.BINARY, Token.GT, new Node(Token.VAR, "a"), new Node(Token.VAR, "b")));

    Node add = new Node(Token.BINARY, Token.PLUS, new Node(Token.VAR, "c"), new Node(Token.VAR, "a"));
    Node assignment = new Node(Token.ASSIGN, Token.VAR, "c", add);
    Node elseStatement = new Node(Token.BLOCK);
    Node returnStatement = new Node(Token.RETURN, new Node(Token.VAR, "c"));

    sampleFunction.getLastChild().addChildren(ifStatement, elseStatement);

    ifStatement.addChildren(assignment);
    elseStatement.addChildren(returnStatement);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(rootNode, jsScope, compiler);
    cfg.addNode(sampleFunction);
    cfg.addEdge(sampleFunction, sampleFunction.getFirstChild(), true);
    cfg.addEdge(sampleFunction.getFirstChild(), ifStatement, false);
    cfg.addEdge(ifStatement, assignment, false);
    cfg.addEdge(ifStatement, elseStatement, true);
    cfg.addEdge(elseStatement, returnStatement, false);
    cfg.addEdge(elseStatement, sampleFunction.getLastChild(), true);

    return cfg;
  }

  @Test
  public void testMustDefAnalysis() {
    // Testing with a simple sample function.
    Scope testScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = createControlFlowGraph(new Node(Token.FUNCTION), testScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    Node useNode = testNode("c", sampleFunction.getLastChild());
    Node defNode = analyzer.getDef("c", useNode);
    assertNotNull("The definition of 'c' should not be null", defNode);
    assertEquals("The definition of 'c' should be the return statement",
        sampleFunction.getLastChild(), defNode);

    assertTrue("Variable 'c' does not depend on outer scope variables",
        analyzer.dependsOnOuterScopeVars("c", useNode) == false);

    // Testing with a for loop.
    Node forNode = new Node(Token.FOR);
    forNode.setProperty("condition", new Node(Token.BINARY, Token.LT, new Node(Token.NUM, 0), new Node(Token.VAR, "i")));
    forNode.setProperty("init", new Node(Token.ASSIGN, Token.VAR, "i", new Node(Token.NUM, 0)));
    forNode.setProperty("update", new Node(Token.BINARY, Token.PLUS, new Node(Token.VAR, "i"), new Node(Token.NUM, 1)));

    Node forLoopBody = new Node(Token.BLOCK);
    Node increment = new Node(Token.ASSIGN, Token.VAR, "i", new Node(Token.BINARY, Token.PLUS, new Node(Token.VAR, "i"), new Node(Token.NUM, 1)));
    forLoopBody.addChildren(increment);

    Node function = new Node(Token.FUNCTION);
    Node forLoopFunctionBody = new Node(Token.BLOCK);
    forLoopFunctionBody.addChildren(forNode, new Node(Token.RETURN, new Node(Token.VAR, "i")));
    function.setProperty("body", forLoopFunctionBody);

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChildren(function);

    cfg = new ControlFlowGraph<Node>(rootNode, testScope, compiler);
    analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    useNode = testNode("i", forLoopFunctionBody.getLastChild());
    defNode = analyzer.getDef("i", useNode);
    assertNotNull("The definition of 'i' should not be null", defNode);
    assertEquals("The definition of 'i' should be the for loop initialization",
        forNode.getLastChild(), defNode);

    assertTrue("Variable 'i' should depend on outer scope variables",
        analyzer.dependsOnOuterScopeVars("i", useNode));
  }

  private Node testNode(String name, Node node) {
    return NodeUtil.createNameNode(name, node.getParent());
  }

  @Test(expected = AssertionError.class)
  public void testNullScope() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.FUNCTION), null, null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, null, null);
  }

  @Test(expected = AssertionError.class)
  public void testNullCompiler() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.FUNCTION), new Scope(), null);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, new Scope(), null);
  }

  @Test(expected = AssertionError.class)
  public void testUnknownVariable() {
    Scope testScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.FUNCTION), testScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    Node useNode = testNode("unknownVar", new Node(Token.FUNCTION));
    Node defNode = analyzer.getDef("unknownVar", useNode);
    assertNull("The definition of 'unknownVar' should be null", defNode);

    // Testing with a variable that is not local.
    Node unknownVar = new Node(Token.VAR);
    unknownVar.setProperty("name", "unknownVar");
    unknownVar.setProperty("scope", new Scope());

    Node rootNode = new Node(Token.BLOCK);
    rootNode.addChildren(new Node(Token.ASSIGN, Token.VAR, "unknownVar", unknownVar));

    cfg = new ControlFlowGraph<Node>(rootNode, testScope, compiler);
    analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    useNode = testNode("unknownVar", new Node(Token.FUNCTION));
    defNode = analyzer.getDef("unknownVar", useNode);
    assertNull("The definition of 'unknownVar' should be null", defNode);
  }

  @Test(expected = AssertionError.class)
  public void testNullVariable() {
    Scope testScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.FUNCTION), testScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    Node useNode = testNode("nullVar", new Node(Token.FUNCTION));
    analyzer.getDef("nullVar", useNode);
  }

  @Test(expected = AssertionError.class)
  public void testNullUseNode() {
    Scope testScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.FUNCTION), testScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    analyzer.getDef(null, new Node(Token.FUNCTION));
  }

  @Test
  public void testEscapedVariables() {
    Scope testScope = new Scope();
    testScope.setVariable("window", ImmutableSet.of(Token.GLOBAL));
    AbstractCompiler compiler = new AbstractCompiler();

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.FUNCTION), testScope, compiler);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, testScope, compiler);

    Var escapedVar = testScope.getVar("window");
    analyzer.escaped.add(escapedVar);

    Node useNode = testNode("window", new Node(Token.FUNCTION));
    Node defNode = analyzer.getDef("window", useNode);
    assertNull("The definition of 'window' should be null", defNode);

    // Test with a variable that is not escaped.
    Var localVar = testScope.addVar("localVar", new Node(Token.VAR), false);
    Node localVarUseNode = testNode("localVar", new Node(Token.FUNCTION));
    defNode = analyzer.getDef("localVar", localVarUseNode);
    assertNotNull("The definition of 'localVar' should not be null", defNode);
    assertEquals("The definition of 'localVar' should be the variable declaration",
        localVar, defNode.getValue());
  }
}