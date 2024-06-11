import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private Node root;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    root = nodeFactory("function () { return this.a; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, root);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function () {}", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);

    Node expected = nodeFactory("emptyFn()", 1, 1);
    assertEquals(expected, testNode);

    testNode = nodeFactory("function a() {}", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    expected = nodeFactory("emptyFn()", 1, 1);
    assertEquals(expected, testNode);
  }

  @org.junit.Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function (x) { return x; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);

    Node expected = nodeFactory("identityFn(x)", 1, 1);
    assertEquals(expected, testNode);

    testNode = nodeFactory("function (x) { return x * 2; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    assertNull(testNode.getFirstChild());
  }

  @org.junit.Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function () { return this.a; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);

    Node expected = nodeFactory("getter(\"a\")", 1, 1);
    assertEquals(expected, testNode);

    testNode = nodeFactory("function () { return this.a.b; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    expected = nodeFactory("getter(\"a.b\")", 1, 1);
    assertEquals(expected, testNode);

    testNode = nodeFactory("function () { return this.a.b.c; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    expected = nodeFactory("getter(\"a.b.c\")", 1, 1);
    assertEquals(expected, testNode);
  }

  @org.junit.Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function (x) { this.a = x; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);

    Node expected = nodeFactory("setter(\"a\")", 1, 1);
    assertEquals(expected, testNode);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function () { return 1; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);

    Node expected = nodeFactory("returnconst(1)", 1, 1);
    assertEquals(expected, testNode);

    testNode = nodeFactory("function () { return 2.5; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    expected = nodeFactory("returnconst(2.5)", 1, 1);
    assertEquals(expected, testNode);
  }

  @org.junit.Test
  public void testNoReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function () { return this + 1; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    assertNull(testNode.getFirstChild());
  }

  @org.junit.Test
  public void testNoSavings() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node testNode = nodeFactory("function () { return 1 + 1; }", 1, 1);
    NodeTraversal.traverse(functionRewriter, null, testNode);
    assertNull(testNode.getFirstChild());
  }

  private Node nodeFactory(String code, int lineno, int charno) {
    return compiler.parse(code, lineno, charno).getFirstChild();
  }

  private boolean isReduceableFunctionExpression(Node node) {
    return NodeUtil.isFunctionExpression(node);
  }

  private static class NodeFactory {
    private NodeFactory() {}

    static Node nodeFactory(String code, int lineno, int charno) {
      return new Node(Token.BLOCK, lineno, charno).addChild(
          compiler.parse(code, lineno, charno).getFirstChild());
    }
  }
}