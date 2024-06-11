import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Tests for FunctionRewriter class.
 */
public class FunctionRewriterTest {
  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {} ;
  }

  @org.junit.Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = new Node(BLOCK, 1, 1);
    root.addChildToBack(new Node(
        FUNCTION_DECLARATION, 1, 1, "emptyFunction",
        ImmutableList.of(), new Node(BLOCK, 1, 1), null));
    Node traversalRoot = NodeTraversal.create(compiler, root);

    Node expectedRoot = new Node(BLOCK, 1, 1);
    expectedRoot.addChildToBack(new Node(
        CALL_EXPR, 1, 1, new Node(IDENT, 1, 1, "JSCompiler_emptyFn"), null));

    functionRewriter.process(null, root);
    NodeTraversal.traverse(compiler, traversalRoot, new NodeCheck(expectedRoot));
  }

  @org.junit.Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = new Node(BLOCK, 1, 1);
    root.addChildToBack(new Node(
        FUNCTION_DECLARATION, 1, 1, "identityFunction",
        ImmutableList.of("value"), new Node(BLOCK, 1, 1), null));
    root.addChildToBack(new Node(
        VAR_DECLARATION, 1, 1, "value", new Node(ASSIGN, 1, 1,
                new Node(IDENT, 1, 1, "value"), new Node(IDENT, 1, 1, "value")),
        null));
    root.addChildToBack(new Node(BLOCK, 1, 1, null));

    Node traversalRoot = NodeTraversal.create(compiler, root);

    Node expectedRoot = new Node(BLOCK, 1, 1);
    expectedRoot.addChildToBack(new Node(
        CALL_EXPR, 1, 1, new Node(IDENT, 1, 1, "JSCompiler_identityFn"),
        new Node(IDENT, 1, 1, "value")));

    functionRewriter.process(null, root);
    NodeTraversal.traverse(compiler, traversalRoot, new NodeCheck(expectedRoot));
  }

  @org.junit.Test
  public void testReturnConstantReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = new Node(BLOCK, 1, 1);
    root.addChildToBack(new Node(
        FUNCTION_DECLARATION, 1, 1, "returnConstantFunction",
        ImmutableList.of(), new Node(BLOCK, 1, 1), null));
    root.addChildToBack(new Node(BLOCK, 1, 1, null));
    root.addChildToBack(new Node(
        EXPR_STATEMENT, 1, 1, new Node(RETURN, 1, 1,
                new Node(NUMBER_LITERAL, 1, 1, 42)), null));

    Node traversalRoot = NodeTraversal.create(compiler, root);

    Node expectedRoot = new Node(BLOCK, 1, 1);
    expectedRoot.addChildToBack(new Node(
        CALL_EXPR, 1, 1, new Node(IDENT, 1, 1, "JSCompiler_returnArg"),
        new Node(NUMBER_LITERAL, 1, 1, 42)));

    functionRewriter.process(null, root);
    NodeTraversal.traverse(compiler, traversalRoot, new NodeCheck(expectedRoot));
  }

  @org.junit.Test
  public void testGetPropertyReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = new Node(BLOCK, 1, 1);
    root.addChildToBack(new Node(
        FUNCTION_DECLARATION, 1, 1, "getPropertyFunction",
        ImmutableList.of(), new Node(BLOCK, 1, 1), null));
    root.addChildToBack(new Node(
        VAR_DECLARATION, 1, 1, "propertyName", null, null));
    root.addChildToBack(new Node(BLOCK, 1, 1, null));
    root.addChildToBack(new Node(
        EXPR_STATEMENT, 1, 1, new Node(ASSIGN, 1, 1,
                new Node(GET_PROP, 1, 1, new Node(IDENT, 1, 1, "this"),
                        new Node(IDENT, 1, 1, "propertyName")),
                new Node(IDENT, 1, 1, "propertyValue")), null));

    Node traversalRoot = NodeTraversal.create(compiler, root);

    Node expectedRoot = new Node(BLOCK, 1, 1);
    expectedRoot.addChildToBack(new Node(
        CALL_EXPR, 1, 1, new Node(IDENT, 1, 1, "JSCompiler_get"),
        new Node(STRING_LITERAL, 1, 1, "propertyName")));

    functionRewriter.process(null, root);
    NodeTraversal.traverse(compiler, traversalRoot, new NodeCheck(expectedRoot));
  }

  @org.junit.Test
  public void testSetPropertyReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = new Node(BLOCK, 1, 1);
    root.addChildToBack(new Node(
        FUNCTION_DECLARATION, 1, 1, "setPropertyFunction",
        ImmutableList.of("newValue"), new Node(BLOCK, 1, 1), null));
    root.addChildToBack(new Node(
        VAR_DECLARATION, 1, 1, "propertyName", null, null));
    root.addChildToBack(new Node(BLOCK, 1, 1, null));
    root.addChildToBack(new Node(
        EXPR_STATEMENT, 1, 1, new Node(ASSIGN, 1, 1,
                new Node(GET_PROP, 1, 1, new Node(IDENT, 1, 1, "this"),
                        new Node(IDENT, 1, 1, "propertyName")),
                new Node(IDENT, 1, 1, "newValue")), null));

    Node traversalRoot = NodeTraversal.create(compiler, root);

    Node expectedRoot = new Node(BLOCK, 1, 1);
    expectedRoot.addChildToBack(new Node(
        CALL_EXPR, 1, 1, new Node(IDENT, 1, 1, "JSCompiler_set"),
        new Node(STRING_LITERAL, 1, 1, "propertyName"),
        new Node(IDENT, 1, 1, "newValue")));

    functionRewriter.process(null, root);
    NodeTraversal.traverse(compiler, traversalRoot, new NodeCheck(expectedRoot));
  }

  /**
   * Base class for Node checking test case.
   */
  private static class NodeCheck implements NodeTraversal.Callback {
    private final Node expected;

    NodeCheck(Node node) {
      expected = node;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
                                  Node parent) {
      assertEquals(expected, node);
      return false;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      // Do nothing.
    }
  }
}