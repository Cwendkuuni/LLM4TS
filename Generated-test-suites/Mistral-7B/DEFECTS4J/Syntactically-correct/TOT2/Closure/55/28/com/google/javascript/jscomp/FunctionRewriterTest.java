import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test() {}";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, 0, 0);
    Node expectedEmptyFunction = new Node(Token.CALL, 0, 0);
    expectedEmptyFunction.putBooleanProp(Node.FREE_CALL, true);
    expectedEmptyFunction.addChildToBack(new Node(Token.NAME, 0, 0, "JSCompiler_emptyFn"));

    assertEquals("Empty function not replaced correctly",
                 expectedRoot.toString(),
                 root.toString());
  }

  @Test
  public void testIdentityFunctionReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test(arg) { return arg }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, 0, 0);
    Node expectedIdentityFunction = new Node(Token.CALL, 0, 0);
    expectedIdentityFunction.putBooleanProp(Node.FREE_CALL, true);
    expectedIdentityFunction.addChildToBack(new Node(Token.NAME, 0, 0, "JSCompiler_identityFn"));
    expectedIdentityFunction.addChildToBack(new Node(Token.STRING, 0, 0, "arg"));

    assertEquals("Identity function not replaced correctly",
                 expectedRoot.toString(),
                 root.toString());
  }

  @Test
  public void testReturnConstantReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test() { return 123 }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, 0, 0);
    Node expectedReturnConstant = new Node(Token.CALL, 0, 0);
    expectedReturnConstant.putBooleanProp(Node.FREE_CALL, true);
    expectedReturnConstant.addChildToBack(new Node(Token.NAME, 0, 0, "JSCompiler_returnArg"));
    expectedReturnConstant.addChildToBack(new Node(Token.INT, 0, 0, 123));

    assertEquals("Return constant not replaced correctly",
                 expectedRoot.toString(),
                 root.toString());
  }

  @Test
  public void testGetPropertyReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test() { return this.prop }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, 0, 0);
    Node expectedGetProperty = new Node(Token.CALL, 0, 0);
    expectedGetProperty.putBooleanProp(Node.FREE_CALL, true);
    expectedGetProperty.addChildToBack(new Node(Token.NAME, 0, 0, "JSCompiler_get"));
    expectedGetProperty.addChildToBack(new Node(Token.STRING, 0, 0, "prop"));

    assertEquals("Get property not replaced correctly",
                 expectedRoot.toString(),
                 root.toString());
  }

  @Test
  public void testSetPropertyReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test(value) { this.prop = value }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, 0, 0);
    Node expectedSetProperty = new Node(Token.CALL, 0, 0);
    expectedSetProperty.putBooleanProp(Node.FREE_CALL, true);
    expectedSetProperty.addChildToBack(new Node(Token.NAME, 0, 0, "JSCo
hline{|}
Compiler.set"));
    expectedSetProperty.addChildToBack(new Node(Token.STRING, 0, 0, "prop"));
    expectedSetProperty.addChildToBack(new Node(Token.INT, 0, 0, 1));
    expectedSetProperty.addChildToBack(new Node(Token.INT, 0, 0, 1));

    assertEquals("Set property not replaced correctly",
                 expectedRoot.toString(),
                 root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidFunctionReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test() { return 1; return 2 }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    fail("Expected an AssertionError due to the invalid function reduction");
  }

  @Test
  public void testNoReductionNeeded() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test() { return this.prop + 1 }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    assertEquals("Function not simplified correctly",
                 sourceCode,
                 root.toString());
  }

  @Test
  public void testComplexFunctionReduction() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String sourceCode = "function Test(a, b) { return a + b + this.c }";
    Node root = compiler.parse(sourceCode, null, null);

    functionRewriter.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, 0, 0);
    Node expectedComplexFunction = new Node(Token.CALL, 0, 0);
    expectedComplexFunction.putBooleanProp(Node.FREE_CALL, true);
    expectedComplexFunction.addChildToBack(new Node(Token.NAME, 0, 0, "JSCompiler_get"));
    expectedComplexFunction.addChildToBack(new Node(Token.STRING, 0, 0, "c"));
    expectedComplexFunction.addChildToBack(new Node(Token.CALL, 0, 0));
    Node expectedAddition = new Node(Token.CALL, 0, 0);
    expectedAddition.putBooleanProp(Node.FREE_CALL, true);
    expectedAddition.addChildToBack(new Node(Token.PLUS, 0, 0));
    Node expectedAddend1 = new Node(Token.NAME, 0, 0, "a");
    Node expectedAddend2 = new Node(Token.NAME, 0, 0, "b");
    expectedAddition.addChildToBack(expectedAddend1);
    expectedAddition.addChildToBack(expectedAddend2);
    expectedComplexFunction.addChildToBack(expectedAddition);

    assertEquals("Complex function not reduced correctly",
                 expectedRoot.toString(),
                 root.toString());
  }
}