import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String sourceCode = "function foo() {}";
    Node root = new AbstractCompiler().parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node fooFunction = findNodeNamed("foo", root);
    assertEquals(CALL, fooFunction.getType());
    assertNull(fooFunction.getFirstChild());
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String sourceCode = "function foo(x) { return x }";
    Node root = new AbstractCompiler().parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node fooFunction = findNodeNamed("foo", root);
    assertEquals(CALL, fooFunction.getType());
    assertEquals("identityFn_", fooFunction.getString());
  }

  @Test
  public void testReturnConstantReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String sourceCode = "function foo() { return 42 }";
    Node root = new AbstractCompiler().parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node fooFunction = findNodeNamed("foo", root);
    assertEquals(CALL, fooFunction.getType());
    assertEquals("returnconst", fooFunction.getString());
    assertEquals(NUMBER, fooFunction.getLastChild().getType());
    assertEquals(42, ((Number) fooFunction.getLastChild()).getNumberValue());
  }

  @Test
  public void testGetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String sourceCode = "function foo(x) { return this.x }";
    Node root = new AbstractCompiler().parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node fooFunction = findNodeNamed("foo", root);
    assertEquals(CALL, fooFunction.getType());
    assertEquals("getter", fooFunction.getString());
    assertEquals("x", fooFunction.getFirstChild().getString());
  }

  @Test
  public void testSetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String sourceCode = "function foo(x) { this.x = x }";
    Node root = new AbstractCompiler().parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node fooFunction = findNodeNamed("foo", root);
    assertEquals(CALL, fooFunction.getType());
    assertEquals("setter", fooFunction.getString());
    assertEquals("x", fooFunction.getFirstChild().getString());
  }

  private Node findNodeNamed(String name, Node root) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.getType() == FUNCTION && child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }
}