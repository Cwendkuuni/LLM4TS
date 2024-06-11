import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * FunctionRewriterTest class for testing FunctionRewriter class.
 */
public class FunctionRewriterTest {

  /**
   * Test FunctionRewriter with empty function.
   */
  @org.junit.Test
  public void testEmptyFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    String source = "function(){}";
    Node root = parser.parse(source, null, null);
    functionRewriter.process(null, root);

    Node expected = NodeUtil.newFunction("emptyFn", null, null, null, null);
    Node actual = root.getLastChild().getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  /**
   * Test FunctionRewriter with identity function.
   */
  @org.junit.Test
  public void testIdentityFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    String source = "function(x){return x}";
    Node root = parser.parse(source, null, null);
    functionRewriter.process(null, root);

    Node expected = NodeUtil.newCall("JSCompiler_identityFn", null);
    Node actual = root.getLastChild().getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  /**
   * Test FunctionRewriter with return constant function.
   */
  @org.junit.Test
  public void testReturnConstantFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    String source = "function(){return 10}";
    Node root = parser.parse(source, null, null);
    functionRewriter.process(null, root);

    Node expected = NodeUtil.newCall("JSCompiler_returnArg", 10);
    Node actual = root.getLastChild().getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  /**
   * Test FunctionRewriter with getter function.
   */
  @org.junit.Test
  public void testGetterFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    String source = "function(obj){return obj.a}";
    Node root = parser.parse(source, null, null);
    functionRewriter.process(null, root);

    Node expected = NodeUtil.newCall("JSCompiler_get", "a");
    Node actual = root.getLastChild().getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  /**
   * Test FunctionRewriter with setter function.
   */
  @org.junit.Test
  public void testSetterFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    String source = "function(obj, value){obj.a = value}";
    Node root = parser.parse(source, null, null);
    functionRewriter.process(null, root);

    Node expected = NodeUtil.newCall("JSCompiler_set", "a");
    Node actual = root.getLastChild().getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  /**
   * Test FunctionRewriter with non-reduceable function.
   */
  @org.junit.Test
  public void testNonReducibleFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Parser parser = new Parser();
    String source = "function(x, y){return x + y}";
    Node root = parser.parse(source, null, null);
    functionRewriter.process(null, root);

    Node actual = root.getLastChild();
    assertNull(actual);
  }

  /**
   * Test FunctionRewriter with null input.
   */
  @org.junit.Test
  public void testFunctionRewriterNullInput() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    assertThrows(NullPointerException.class, () -> functionRewriter.process(null, null));
  }

  /**
   * Test FunctionRewriter with null root input.
   */
  @org.junit.Test
  public void testFunctionRewriterNullRootInput() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = null;
    functionRewriter.process(null, root);

    Node actual = root;
    assertNull(actual);
  }
}