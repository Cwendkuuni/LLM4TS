import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.rhino.Node;

public class FunctionRewriterTest {

  @Test
  public void testIdentityFunctionReduction() {
    AbstractCompiler compiler = new CompilerWithContextForTesting();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String originalCode = "function Identity(x) { return x; }";
    Node root = compiler.parse(originalCode, null, null);
    functionRewriter.process(null, root);

    Node expectedCode = new Node(Token.CALL, 1, 1,
        new Node(Token.STRING, "JSCompiler_identityFn"));

    Node actualCode = root.getFirstChild();
    assertEquals("Expected identity function to be reduced to JSCompiler_identityFn call",
        expectedCode, actualCode);
  }

  @Test
  public void testEmptyFunctionReduction() {
    AbstractCompiler compiler = new CompilerWithContextForTesting();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String originalCode = "function EmptyFunction() {}";
    Node root = compiler.parse(originalCode, null, null);
    functionRewriter.process(null, root);

    Node expectedCode = new Node(Token.CALL, 1, 1,
        new Node(Token.STRING, "JSCompiler_emptyFn"));

    Node actualCode = root.getFirstChild();
    assertEquals("Expected empty function to be reduced to JSCompiler_emptyFn call",
        expectedCode, actualCode);
  }

  @Test
  public void testSingleArgumentFunctionReduction() {
    AbstractCompiler compiler = new CompilerWithContextForTesting();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String originalCode = "function AddOne(x) { return x + 1; }";
    Node root = compiler.parse(originalCode, null, null);
    functionRewriter.process(null, root);

    Node expectedCode = new Node(Token.CALL, 1, 1,
        new Node(Token.STRING, "JSCompiler_returnArg"),
        new Node(Token.INT_LITERAL, 1));

    Node actualCode = root.getFirstChild();
    assertEquals("Expected add one function to be reduced to JSCompiler_returnArg call",
        expectedCode, actualCode);
  }

  @Test
  public void testFunctionWithMultipleStatementsReduction() {
    AbstractCompiler compiler = new CompilerWithContextForTesting();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String originalCode = "function MultiplyAndAdd(x, y) {" +
        "  var product = x * y;" +
        "  return product + x;" +
        "}";
    Node root = compiler.parse(originalCode, null, null);
    functionRewriter.process(null, root);

    Node expectedCode = new Node(Token.CALL, 1, 1,
        new Node(Token.STRING, "JSCompiler_returnArg"),
        new Node(Token.INT_LITERAL, 2),
        new Node(Token.STRING, "JSCompiler_multiply"),
        new Node(Token.INT_LITERAL, 1));

    Node actualCode = root.getFirstChild();
    assertEquals("Expected multiply and add function to be reduced to JSCompiler_returnArg call",
        expectedCode, actualCode);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithInvalidReduction() {
    AbstractCompiler compiler = new CompilerWithContextForTesting();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String originalCode = "function InvalidReduction(x) {" +
        "  if (x < 0) {" +
        "    return 1;" +
        "  }" +
        "  return x;" +
        "}";
    Node root = compiler.parse(originalCode, null, null);
    functionRewriter.process(null, root);

    fail("Expected an AssertionError when processing an invalid reduction");
  }

  @Test
  public void testFunctionWithNestedCallReduction() {
    AbstractCompiler compiler = new CompilerWithContextForTesting();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String originalCode = "function NestedFunction(x) {" +
        "  function InnerFunction(y) {" +
        "    return x + y;" +
        "  }" +
        "  return InnerFunction(2);" +
        "}";
    Node root = compiler.parse(originalCode, null, null);
    functionRewriter.process(null, root);

    Node expectedCode = new Node(Token.CALL, 1, 1,
        new Node(Token.STRING, "JSCompiler_returnArg"),
        new Node(Token.STRING, "JSCompiler_returnArg"),
        new Node(Token.INT_LITERAL, 2),
        new Node(Token.STRING, "JSCompiler_add"));

    Node actualCode = root.getFirstChild();
    assertEquals("Expected nested function to be reduced to JSCompiler_returnArg calls",
        expectedCode, actualCode);
  }
}