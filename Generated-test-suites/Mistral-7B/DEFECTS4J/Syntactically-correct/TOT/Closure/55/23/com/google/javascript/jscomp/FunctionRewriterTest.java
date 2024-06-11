import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class FunctionRewriterTest {

  private Compiler compiler;
  private FunctionRewriter rewriter;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    compiler = new Compiler();
    rewriter = new FunctionRewriter(compiler);
  }

  /**
   * Test that FunctionRewriter does not change the code if no reduction is possible.
   */
  @Test
  public void testNoReduction() {
    Node root = compiler.parseString("function foo() {}");
    rewriter.process(null, root);
    assertEquals("function foo() {}", compiler.getSource(root));
  }

  /**
   * Test that FunctionRewriter applies EmptyFunctionReducer.
   */
  @Test
  public void testEmptyFunctionReducer() {
    Node root = compiler.parseString("function foo() {}");
    rewriter.process(null, root);
    Node expected = compiler.parseString("function foo() {}" +
            "JSCompiler_emptyFn();");
    assertEquals(expected.toString(), compiler.getSource(root));
  }

  /**
   * Test that FunctionRewriter applies IdentityReducer.
   */
  @Test
  public void testIdentityReducer() {
    Node root = compiler.parseString("function foo(x) { return x }");
    rewriter.process(null, root);
    Node expected = compiler.parseString("function foo(x) {" +
            "  JSCompiler_identityFn('x')}" +
            "}");
    assertEquals(expected.toString(), compiler.getSource(root));
  }

  /**
   * Test that FunctionRewriter applies GetterReducer.
   */
  @Test
  public void testGetterReducer() {
    Node root = compiler.parseString("function Foo() {" +
            "  this.a_ = 1;" +
            "  this.getA = function() { return this.a_ };" +
            "}");
    rewriter.process(null, root);
    Node expected = compiler.parseString("function Foo() {" +
            "  this.a_ = 1;" +
            "  this.getA = JSCompiler_get('a_');" +
            "}");
    assertEquals(expected.toString(), compiler.getSource(root));
  }

  /**
   * Test that FunctionRewriter applies SetterReducer.
   */
  @Test
  public void testSetterReducer() {
    Node root = compiler.parseString("function Foo() {" +
            "  this.a_ = 1;" +
            "  this.setA = function(value) { this.a_ = value };" +
            "}");
    rewriter.process(null, root);
    Node expected = compiler.parseString("function Foo() {" +
            "  this.a_ = 1;" +
            "  this.setA = JSCompiler_set('a_');" +
            "}");
    assertEquals(expected.toString(), compiler.getSource(root));
  }

  /**
   * Test that FunctionRewriter does not change the code if saving bytes is not possible.
   */
  @Test
  public void testNoSavings() {
    Node root = compiler.parseString("function foo() {" +
            "  var x = 1;" +
            "  var y = 2;" +
            "  function bar() { return x + y }" +
            "}");
    rewriter.process(null, root);
    assertEquals("function foo() {" +
            "  var x = 1;" +
            "  var y = 2;" +
            "  function bar() { return x + y }" +
            "}", compiler.getSource(root));
  }
}