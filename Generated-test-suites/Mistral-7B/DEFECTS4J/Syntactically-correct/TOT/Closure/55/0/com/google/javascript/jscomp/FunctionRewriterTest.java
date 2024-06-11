import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function Foo() {}";
    Node root = compiler.parseTree(jsCode);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node expectedRoot = NodeFactory.createRoot(
        NodeFactory.createCall(
            NodeFactory.createName("JSCompiler_emptyFn"), null));

    assertEquals(expectedRoot, root);
  }

  @Test
  public void testIdentityFunctionReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function Foo(x) { return x; }";
    Node root = compiler.parseTree(jsCode);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node expectedRoot = NodeFactory.createRoot(
        NodeFactory.createCall(
            NodeFactory.createName("JSCompiler_identityFn"),
            NodeFactory.createName("x")));

    assertEquals(expectedRoot, root);
  }

  @Test
  public void testReturnConstantReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function Foo() { return 42; }";
    Node root = compiler.parseTree(jsCode);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node expectedRoot = NodeFactory.createRoot(
        NodeFactory.createCall(
            NodeFactory.createName("JSCompiler_returnArg"),
            NodeFactory.createInt(42)));

    assertEquals(expectedRoot, root);
  }

  @Test
  public void testGetterReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function Foo() { this.x = 42; } Foo.prototype.getX = function() " +
        "{ return this.x; };";
    Node root = compiler.parseTree(jsCode);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node expectedRoot = NodeFactory.createRoot(
        NodeFactory.createCall(
            NodeFactory.createName("JSCompiler_get"),
            NodeFactory.createName("x")));
    expectedRoot.addChildToBack(
        NodeFactory.createCall(
            NodeFactory.createName("Foo"), null));

    assertEquals(expectedRoot, root);
  }

  @Test
  public void testSetterReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function Foo() {} Foo.prototype.setX = function(x) {" +
        " this.x = x; }; Foo.prototype.getX = function() {" +
        " return this.x; };";
    Node root = compiler.parseTree(jsCode);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    Node expectedRoot = NodeFactory.createRoot(
        NodeFactory.createCall(
            NodeFactory.createName("JSCompiler_get"),
            NodeFactory.createName("x")));
    expectedRoot.addChildToBack(
        NodeFactory.createCall(
            NodeFactory.createName("Foo"), null));
    expectedRoot.addChildToBack(
        NodeFactory.createCall(
            NodeFactory.createName("JSCompiler_set"),
            NodeFactory.createName("x"),
            NodeFactory.createInt(42)));

    assertEquals(expectedRoot, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidReduction() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function Foo(x, y) {}";
    Node root = compiler.parseTree(jsCode);

    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(null, root);

    fail("Expected IllegalStateException");
  }
}