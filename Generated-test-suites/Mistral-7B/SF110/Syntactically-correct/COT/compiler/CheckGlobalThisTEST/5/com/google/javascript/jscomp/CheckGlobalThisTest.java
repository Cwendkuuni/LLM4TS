import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private Compiler compiler;
  private Parser parser;
  private NodeFactory nodeFactory;
  private Checker checker;

  @Before
  public void setUp() {
    compiler = new Compiler();
    parser = new Parser(new SourceFactory());
    nodeFactory = compiler.getFactory();
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node node = parser.parse("function foo() {}", null, null).getFirstChild();
    assertTrue(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node node = parser.parse("function Foo() {}", null, null).getFirstChild();
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node node = parser.parse(
        "/** @this {Object} */ function foo() {}",
        null,
        null
    ).getFirstChild();
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    Node node = parser.parse(
        "/** @interface */ function foo() {}",
        null,
        null
    ).getFirstChild();
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node node = parser.parse(
        "function foo() { /** @override */ }",
        null,
        null
    ).getFirstChild();
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    Node node = parser.parse(
        "function foo(x) { return x + this.y; }",
        null,
        null
    ).getFirstChild();
    assertTrue(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionGettingLentToPrototype() {
    Node node = parser.parse(
        "var obj = {}; Object.defineProperty(obj.prototype, 'foo', { value: function() {} });",
        null,
        null
    ).getLastChild();
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, node.getParent()));
  }

  @Test
  public void testShouldTraverseAssignmentToThisOnLeftSide() {
    Node node = parser.parse(
        "var a = this;",
        null,
        null
    ).getFirstChild();
    assertTrue(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, null));
  }

  @Test
  public void testShouldTraversePropertyAccessToThis() {
    Node node = parser.parse(
        "var a = { get x() { return this.y; } };",
        null,
        null
    ).getLastChild();
    assertTrue(checker.shouldTraverse(NodeTraversal.emptyTraversal(), node, node.getParent()));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportGlobalThis() {
    Node node = parser.parse("this;", null, null).getFirstChild();
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public TraversalResult traverseCallChain(NodeTraversal t, Node n, Node parent) {
        return super.traverseCallChain(t, node, parent);
      }
    };
    checker.visit(traversal, node, null);
  }
}

class SourceFactory implements Compiler.SourceFactory {
  @Override
  public Source newSource(String code, String inputFile) {
    return new Source(inputFile, code, null);
  }
}