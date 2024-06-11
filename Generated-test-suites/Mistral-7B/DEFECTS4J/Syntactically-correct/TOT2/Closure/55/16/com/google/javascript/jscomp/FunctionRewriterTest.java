import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.BeforeClass;
import org.junit.Test;

public class FunctionRewriterTest {
  private static Compiler compiler;
  private static NodeFactory nodeFactory;

  @BeforeClass
  public static void setup() {
    CompilerOptions options = new CompilerOptions(CompilationLevel.SIMPLE_OPTIMIZATIONS);
    compiler = new Compiler(options);
    nodeFactory = compiler.nodeFactory();
  }

  @Test
  public void testEmptyFunctionReducer() {
    String input = "function TestEmptyFunction() {}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestEmptyFunction() {emptyFn();}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    assertTrue("FunctionRewriter did not apply EmptyFunctionReducer", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }

  @Test
  public void testIdentityReducer() {
    String input = "function TestIdentityFunction(value) {return value;}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestIdentityFunction(value) {return this.identityFn(value);}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    assertTrue("FunctionRewriter did not apply IdentityReducer", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }

  @Test
  public void testReturnConstantReducer() {
    String input = "function TestReturnConstantFunction() {return 42;}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestReturnConstantFunction() {return 42;}{return 42;}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    Node helperNode = NodeUtil.getFirstChild(expectedRoot.getLastChild());
    assertEquals("FunctionRewriter helper code not as expected", "return 42;", helperNode.getString());
    assertTrue("FunctionRewriter did not apply ReturnConstantReducer", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }

  @Test
  public void testGetterReducer() {
    String input = "function TestGetterFunction() {return this.test_;}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestGetterFunction() {return getter(\"test_\");}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    assertTrue("FunctionRewriter did not apply GetterReducer", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }

  @Test
  public void testSetterReducer() {
    String input = "function TestSetterFunction(value) {this.test_ = value;}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestSetterFunction(value) {setter(\"test_\", value);}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    assertTrue("FunctionRewriter did not apply SetterReducer", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }

  @Test
  public void testComplexFunction() {
    String input = "function TestComplexFunction(a, b) {" +
        "  var c = a + b;" +
        "  this.result_ = c;" +
        "}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestComplexFunction(a, b) {" +
        "  var c = a + b;" +
        "  this.result_ = this.resultFn(a, b);" +
        "}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    assertTrue("FunctionRewriter did not apply FunctionRewriter", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }

  @Test(expected = AssertionError.class)
  public void testNonFunctionExpression() {
    String input = "function TestNonFunctionExpression() {}";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testEmptyFunctionExpression() {
    String input = "";
    Node root = compiler.parseJs(input);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);
  }

  @Test
  public void testNegativeSavingsThreshold() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.SAVINGS_THRESHOLD = -1;

    String input = "function TestNegativeSavingsThreshold() {return 42;}";
    Node root = compiler.parseJs(input);

    functionRewriter.process(null, root);

    Node expectedRoot = nodeFactory.parseJs("function TestNegativeSavingsThreshold() {return 42;}{return 42;}");
    Node actualRoot = root.cloneTree();
    NodeUtil.traverseAndSetEquals(NodeUtil.getFirstChild(expectedRoot), NodeUtil.getFirstChild(actualRoot));
    assertTrue("FunctionRewriter did not apply FunctionRewriter with savingsThreshold = -1", NodeUtil.isNodeEqual(expectedRoot, actualRoot));
  }
}