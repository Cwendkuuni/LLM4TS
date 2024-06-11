import static com.google.javascript.jscomp.FunctionRewriter.SAVINGS_THRESHOLD;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

/**
 * Test case for the FunctionRewriter class.
 */
public class FunctionRewriterTest {
  private static Node compileTestSource(String source) {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = NodeFactory.outOfBandSource(source, null, null);
    Node root = compiler.parse(externs, null);
    NodeTraversal.traverse(compiler, root, new FunctionRewriter());
    return root;
  }

  /**
   * Test empty function reduction.
   */
  @Test
  public void testEmptyFunctionReduction() {
    String testSource = "function EmptyFunction() {}";
    Node root = compileTestSource(testSource);
    Node emptyFunction = root.getChildByName("EmptyFunction");

    // Basic functionality test.
    assertEquals(NodeUtil.toString(emptyFunction.getLastChild()),
                 "JSCompiler_emptyFn()");

    // Edge case: argument passed to empty function.
    Node argNode = NodeFactory.newExpressionStatement(
        NodeFactory.newAssign(NodeFactory.newIdent("x"),
                              NodeFactory.newIntLiteral(42)));
    Node newRoot = new Node(Node.BLOCK, 0, 0);
    newRoot.addChildToBack(emptyFunction);
    newRoot.addChildToBack(argNode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, newRoot);

    Node reducedEmptyFunction = newRoot.getChildByName("EmptyFunction");
    assertEquals(NodeUtil.toString(reducedEmptyFunction.getLastChild()),
                 "emptyFn()");

    // Error scenario: function with multiple statements.
    testSource = "function EmptyFunction() {return 1; /* comment */ }";
    root = compileTestSource(testSource);
    emptyFunction = root.getChildByName("EmptyFunction");

    // Basic functionality test should fail.
    try {
      functionRewriter.process(null, root);
      fail("Expected exception, but none was thrown.");
    } catch (AssertionError e) {
      // Expected.
    }
  }

  /**
   * Test identity function reduction.
   */
  @Test
  public void testIdentityFunctionReduction() {
    String testSource = "function IdentityFunction(a) {return a}";
    Node root = compileTestSource(testSource);
    Node identityFunction = root.getChildByName("IdentityFunction");

    // Basic functionality test.
    assertEquals(NodeUtil.toString(identityFunction.getLastChild()),
                 "IdentityFunction_value");

    // Edge case: argument passed to identity function.
    Node argNode = NodeFactory.newExpressionStatement(
        NodeFactory.newAssign(NodeFactory.newIdent("x"),
                              NodeFactory.newIntLiteral(42)));
    Node newRoot = new Node(Node.BLOCK, 0, 0);
    newRoot.addChildToBack(identityFunction);
    newRoot.addChildToBack(argNode);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, newRoot);

    Node reducedIdentityFunction = newRoot.getChildByName("IdentityFunction");
    assertEquals(NodeUtil.toString(reducedIdentityFunction.getLastChild()),
                 "IdentityFunction_value");

    // Error scenario: function returns non-immutable value.
    testSource = "function IdentityFunction(a) {return a + \"test\";}";
    root = compileTestSource(testSource);
    identityFunction = root.getChildByName("IdentityFunction");

    // Basic functionality test should fail.
    try {
      functionRewriter.process(null, root);
      fail("Expected exception, but none was thrown.");
    } catch (AssertionError e) {
      // Expected.
    }
  }

  /**
   * Test getter method reduction.
   */
  @Test
  public void testGetterMethodReduction() {
    String testSource = "function C() {C.prototype.getA = function() {return this.a_};}";
    Parser parser = new Parser();
    Node externs = parser.parse(testSource, null, null);
    Node root = NodeFactory.outOfBandSource(null, externs, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(externs, root);

    Node getA = root.getChildByName("C").getLastChild().getLastChild();
    assertEquals(NodeUtil.toString(getA.getLastChild()),
                 "JSCompiler_get(\"a_\")");

    // Edge case: getter with argument.
    testSource = "function C() {C.prototype.getB = function(x) {return this.b_[x]};}";
    externs = parser.parse(testSource, null, null);
    root = NodeFactory.outOfBandSource(null, externs, null);

    functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(externs, root);

    Node getB = root.getChildByName("C").getLastChild().getLastChild().getLastChild();
    assertEquals(NodeUtil.toString(getB.getLastChild()),
                 "JSCompiler_get(\"b_\")");
  }

  /**
   * Test setter method reduction.
   */
  @Test
  public void testSetterMethodReduction() {
    String testSource = "function C() {C.prototype.setA = function(value) {this.a_ = value};}";
    Parser parser = new Parser();
    Node externs = parser.parse(testSource, null, null);
    Node root = NodeFactory.outOfBandSource(null, externs, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(externs, root);

    Node setA = root.getChildByName("C").getLastChild().getLastChild();
    assertEquals(NodeUtil.toString(setA.getLastChild()),
                 "JSCompiler_set(\"a_\")");

    // Edge case: setter with argument.
    testSource = "function C() {C.prototype.setB = function(value, x) {this.b_[x] = value};}";
    externs = parser.parse(testSource, null, null);
    root = NodeFactory.outOfBandSource(null, externs, null);

    functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(externs, root);

    Node setB = root.getChildByName("C").getLastChild().getLastChild().getLastChild();
    assertEquals(NodeUtil.toString(setB.getLastChild()),
                 "JSCompiler_set(\"b_\", JSCompiler_get(\"x\"))");
  }

  /**
   * Test helper method parsing.
   */
  @Test
  public void testHelperMethodParsing() {
    String testSource = "function EmptyFunctionHelper() {}";
    Node root = compileTestSource(testSource);
    Node emptyFunctionHelper = root.getChildByName("EmptyFunctionHelper");

    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node helperCode = functionRewriter.parseHelperCode(new EmptyFunctionReducer());

    assertEquals(NodeUtil.toString(helperCode),
                 NodeUtil.toString(emptyFunctionHelper));

    // Edge case: helper method with arguments.
    testSource = "function IdentityFunctionHelper(a) {}";
    root = compileTestSource(testSource);
    emptyFunctionHelper = root.getChildByName("IdentityFunctionHelper");

    helperCode = functionRewriter.parseHelperCode(new IdentityReducer());

    assertEquals(NodeUtil.toString(helperCode),
                 NodeUtil.toString(emptyFunctionHelper));
  }
}