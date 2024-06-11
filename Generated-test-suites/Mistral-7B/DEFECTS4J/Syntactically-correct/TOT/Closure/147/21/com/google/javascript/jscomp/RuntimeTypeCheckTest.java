import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.Arrays;

public class RuntimeTypeCheckTest extends TestCase {

  private Compiler compiler;
  private String logFunction;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        RuntimeTypeCheck runtimeTypeCheck =
            new RuntimeTypeCheck(compiler, logFunction);
        runtimeTypeCheck.process(externs, root);
      }
    });
  }

  public void testRuntimeTypeCheck_NormalCase() throws IOException {
    String input = "function Foo() {}";
    String expectedOutput = "function Foo() { checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo');" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_InterfaceImplementation() throws IOException {
    String input = "function Bar() {}" +
        "Bar.prototype.doSomething = function() {};" +
        "function Foo implements Bar {}";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (this.doSomething instanceof Function);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_ArrayChecker() throws IOException {
    String input = "function Foo() { return [1, 2]; }";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (Array.isArray(this));" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_NullChecker() throws IOException {
    String input = "function Foo() { return null; }";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (null === this);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_BooleanValueType() throws IOException {
    String input = "function Foo() { return true; }";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (Boolean(this));" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_NumberValueType() throws IOException {
    String input = "function Foo() { return 1; }";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (Number(this));" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_StringValueType() throws IOException {
    String input = "function Foo() { return 'abc'; }";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (String(this));" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_VoidType() throws IOException {
    String input = "function Foo() {}";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (void 0);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_ExternClass() throws IOException {
    String input = "//{{{ exports.Foo = Foo; }}}" +
        "function Foo() {}";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (typeof exports !== 'undefined') && (exports.Foo === this);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_InterfaceChecker() throws IOException {
    String input = "interface Bar {}" +
        "function Foo implements Bar {}";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (typeof Bar !== 'undefined') && (this instanceof Bar);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_ClassChecker() throws IOException {
    String input = "class Bar {}" +
        "class Foo extends Bar {}";
    String expectedOutput = "function Foo() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'Foo') &&" +
        " (this instanceof Foo) && (this instanceof Bar);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_UnionType() throws IOException {
    String input = "function Foo() {}" +
        "function FooOrBar() {" +
        "if (Math.random() < 0.5) {" +
        "  return new Foo();" +
        "} else {" +
        "  return new Object();" +
        "}" +
        "}";
    String expectedOutput = "function FooOrBar() {" +
        "checkType(this, function() {" +
        "return isFunction(this) && (this.constructor && this.constructor.name === 'FooOrBar') &&" +
        " (this instanceof Foo || this instanceof Object);" +
        "}); }";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    Node actualOutput = compiler.getOutput();
    assertEquals(expectedOutput, actualOutput.toString());
  }

  public void testRuntimeTypeCheck_WrongType() throws IOException {
    String input = "function Foo() {}" +
        "function Bar() {}" +
        "function FooBar() {" +
        "checkType(new Bar(), function() { return this instanceof Foo; });" +
        "}";
    String expectedError = "TypeError: Assertion failed: checkType: Failed type check at FooBar";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertCompilationError(expectedError);
  }

  public void testRuntimeTypeCheck_MissingTypeChecker() throws IOException {
    String input = "function Foo() {}" +
        "function FooBar() {" +
        "checkType(new Foo(), function() { return this.toString === 'toString'; });" +
        "}";
    String expectedError = "ReferenceError: checkType is not defined";

    Node externalSourceNode = NodeUtil.newSourceNode("test.js", input);
    Node root = NodeUtil.parse(input);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse();

    assertCompilationError(expectedError);
  }

  private void assertCompilationError(String expectedError) {
    try {
      compiler.compile(null, null, NodeUtil.parseAndNormalize(
          Arrays.asList(NodeUtil.newSourceNode("test.js", ""))), "test.js");
      fail("Expected compilation error");
    } catch (CompilationErrorException e) {
      assertEquals(expectedError, e.getMessage());
    }
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    compiler = null;
  }
}