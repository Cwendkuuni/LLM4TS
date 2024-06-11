import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.NodeFactory;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private Node rootNode;
  private CompilerPassManager passManager;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = new TestingCompiler();
    nodeFactory = compiler.getFactory();
    rootNode = new Node(Token.PROGRAM, 1, 1);
    passManager = new CompilerPassManager(compiler);
    functionRewriter = new FunctionRewriter(passManager);
  }

  @org.junit.Test
  public void testIdentityReducer() {
    Node identifierNode = nodeFactory.createIdentifier("foo");
    Node functionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunctionDeclaration(
            "IdentityFunction",
            NodeUtil.createEmptyFormalParameterList(),
            nodeFactory.createReturnStatement(identifierNode),
            null,
            null,
            null,
            null));

    rootNode.addChildToBack(functionNode);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    Node expected = nodeFactory.createCallExpression(
        nodeFactory.createIdentifier("JSCompiler_identityFn"), null);

    assertEquals(expected, functionNode.getFirstChild());
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node functionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunctionDeclaration(
            "EmptyFunction",
            NodeUtil.createEmptyFormalParameterList(),
            null,
            null,
            null,
            null,
            null));

    rootNode.addChildToBack(functionNode);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    Node expected = nodeFactory.createCallExpression(
        new Node(Token.IDENTIFIER, "JSCompiler_emptyFn", null), null);

    assertEquals(expected, functionNode.getFirstChild());
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node identifierNode1 = nodeFactory.createIdentifier("foo");
    Node identifierNode2 = nodeFactory.createIdentifier("bar");
    Node functionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunctionDeclaration(
            "GetterFunction",
            NodeUtil.createEmptyFormalParameterList(),
            nodeFactory.createCallExpression(
                nodeFactory.createMemberExpression(
                    nodeFactory.createThisExpression(),
                    identifierNode1.toString()), null),
            null,
            null,
            null,
            null));

    rootNode.addChildToBack(functionNode);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    Node expected = nodeFactory.createCallExpression(
        new Node(Token.IDENTIFIER, "JSCompiler_get", null),
        new Node(Token.STRING, "foo", null));

    assertEquals(expected, functionNode.getFirstChild());
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node identifierNode1 = nodeFactory.createIdentifier("foo");
    Node identifierNode2 = nodeFactory.createIdentifier("bar");
    Node functionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunctionDeclaration(
            "SetterFunction",
            NodeUtil.createEmptyFormalParameterList(),
            null,
            nodeFactory.createCallExpression(
                nodeFactory.createAssign(
                    nodeFactory.createMemberExpression(
                        nodeFactory.createThisExpression(),
                        identifierNode1.toString()),
                    nodeFactory.createIdentifier("bar")),
                new Node(Token.NUM, 42, null)),
            null,
            null,
            null,
            null));

    rootNode.addChildToBack(functionNode);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    Node expected = nodeFactory.createCallExpression(
        new Node(Token.IDENTIFIER, "JSCompiler_set", null),
        new Node(Token.STRING, "foo", null),
        new Node(Token.NUM, 42, null));

    assertEquals(expected, functionNode.getFirstChild());
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Node constantNode = nodeFactory.createLiteral(42);
    Node functionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunctionDeclaration(
            "ReturnConstantFunction",
            NodeUtil.createEmptyFormalParameterList(),
            nodeFactory.createReturnStatement(constantNode),
            null,
            null,
            null,
            null));

    rootNode.addChildToBack(functionNode);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    Node expected = nodeFactory.createCallExpression(
        new Node(Token.IDENTIFIER, "JSCompiler_returnArg", null),
        constantNode);

    assertEquals(expected, functionNode.getFirstChild());
  }

  @org.junit.Test
  public void testEdgeCaseWithNestedFunctionCalls() {
    Node functionNode1 = nodeFactory.createFunctionDeclaration(
        "Function1",
        NodeUtil.createEmptyFormalParameterList(),
        nodeFactory.createCallExpression(
            nodeFactory.createMemberExpression(
                nodeFactory.createThisExpression(),
                "Function2"),
            null),
        null,
        null,
        null,
        null);

    Node functionNode2 = nodeFactory.createFunctionDeclaration(
        "Function2",
        NodeUtil.createEmptyFormalParameterList(),
        nodeFactory.createCallExpression(
            nodeFactory.createMemberExpression(
                nodeFactory.createThisExpression(),
                "Function1"),
            null),
        null,
        null,
        null,
        null);

    rootNode.addChildToBack(functionNode1);
    rootNode.addChildToBack(functionNode2);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    // FunctionRewriter will reduce Function1 to a call to Function2,
    // and Function2 to a call to a helper function.
    Node expected = nodeFactory.createCallExpression(
        new Node(Token.IDENTIFIER, "JSCompiler_functionCall", null),
        new Node(Token.IDENTIFIER, "Function2", null));

    Node actual = (Node) NodeTraversal.depthFirst(rootNode, new NodeFilter() {
      @Override
      public boolean shouldTraverse(Node node) {
        return node.getType() == Token.FUNCTION;
      }

      @Override
      public void visit(Node node) {
        if (node.getType() == Token.FUNCTION) {
          return;
        }
        assertEquals(expected, node);
      }
    }).getResult();

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testThrowsExceptionWhenFunctionCannotBeReduced() {
    Node functionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunctionDeclaration(
            "CannotBeReducedFunction",
            NodeUtil.createEmptyFormalParameterList(),
            null,
            null,
            null,
            null,
            null));

    rootNode.addChildToBack(functionNode);

    passManager.addPass(functionRewriter);
    passManager.run(rootNode);

    // Should not make any changes to the node.
    assertEquals(functionNode, rootNode.getLastChild());
  }

  @org.junit.After
  public void tearDown() {
    compiler = null;
    nodeFactory = null;
    rootNode = null;
    passManager = null;
    functionRewriter = null;
  }
}