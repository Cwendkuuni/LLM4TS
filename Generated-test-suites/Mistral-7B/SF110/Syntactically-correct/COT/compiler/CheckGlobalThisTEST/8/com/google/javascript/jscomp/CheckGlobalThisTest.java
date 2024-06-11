import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticsCollector;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Type;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private static final DiagnosticType GLOBAL_THIS = new DiagnosticType(
      "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }
    };

    Node tree = createNodeTree("function foo() {}");
    traversal.traverse(tree, null);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testShouldTraverseGlobalContexts() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() {})()");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    tree = createNodeTree("(function foo() {})()");
    traversal.traverse(tree, null);
    diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    tree = createNodeTree("var foo = function() {};");
    traversal.traverse(tree, null);
    diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    tree = createNodeTree("function foo() {}; var bar = function() {};");
    traversal.traverse(tree, null);
    diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    tree = createNodeTree("function Foo() {}");
    traversal.traverse(tree, null);
    diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    tree = createNodeTree("function Foo() { this.x = 1; }");
    traversal.traverse(tree, null);
    diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());

    tree = createNodeTree("function Foo() { this.x = 1; } Foo.prototype.y = 2;");
    traversal.traverse(tree, null);
    diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() { this = 1; })()");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.size() == 1);
    Diagnostics expected = new Diagnostics(GLOBAL_THIS, tree.getFirstChild());
    assertEquals(diagnostics.get(0), expected);
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() { this.x = 1; return this.x; })()");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.size() == 1);
    Diagnostics expected = new Diagnostics(GLOBAL_THIS, tree.getLastChild().getLastChild());
    assertEquals(diagnostics.get(0), expected);
  }

  @Test
  public void testShouldNotReportThisInPrototypeContext() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() { return {prototype: {x: 1}} })()");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testShouldNotReportThisInObjectLiteralContext() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() { return {x: function() {}} })()");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testShouldReportThisInPrototypePropertyAccessContext() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() { return {prototype: {x: 1}} })().x");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.size() == 1);
    Diagnostics expected = new Diagnostics(GLOBAL_THIS, tree.getLastChild());
    assertEquals(diagnostics.get(0), expected);
  }

  @Test
  public void testShouldReportThisInNestedAssignmentContext() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() { var a = this; this.x = a = {x: 1}; return a.x; })()");
    traversal.traverse(tree, null);
    ImmutableList<Diagnostics> diagnostics = traversal.getDiagnostics();
    assertTrue(diagnostics.size() == 1);
    Diagnostics expected = new Diagnostics(GLOBAL_THIS, tree.getLastChild().getLastChild());
    assertEquals(diagnostics.get(0), expected);
  }

  @Test(expected = AssertionError.class)
  public void testShouldThrowExceptionWhenShouldTraverseReturnsFalse() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return false;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(t, n, parent);
      }
    };

    Node tree = createNodeTree("(function() {})()");
    traversal.traverse(tree, null);
  }

  private Node createNodeTree(String jsSource) {
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    DiagnosticsCollector diagnosticsCollector = new DiagnosticsCollector();
    Checker checker = new Checker(abstractCompiler, new DiagnosticsCollector());
    checker.checkSourceInternal(jsSource, null, diagnosticsCollector, Type.SCRIPT, new SourceFile());
    Node rootNode = diagnosticsCollector.getSourceNode();
    return rootNode;
  }

  private void assertEquals(Diagnostics expected, Diagnostics actual) {
    assertEquals(expected.getMessage(), expected.getLocation().getLineNumber(),
        expected.getLocation().getColumnNumber(), expected.getLocation().getSourceName(),
        actual.getMessage(), actual.getLocation().getLineNumber(),
        actual.getLocation().getColumnNumber(), actual.getLocation().getSourceName());
  }
}