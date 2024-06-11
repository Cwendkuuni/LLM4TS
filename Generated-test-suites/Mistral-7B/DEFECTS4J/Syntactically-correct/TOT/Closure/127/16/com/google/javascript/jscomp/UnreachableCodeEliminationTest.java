import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeUtil.isFunction;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    passManager = new CompilerPassManager(compiler, options);
  }

  @Test
  public void testUnreachableCodeElimination() {
    String jsSource = "function foo() { return; alert('unreachable'); }";
    Node ast = compiler.parseTree(jsSource);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isReturnStatement());
    assertFalse(root.getNext().isDefined());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testNoOpStatements() {
    String jsSource = "function foo() {"
        + "var a = {},"
        + "b = a.x," // intentionally uninitialized
        + "c = 5;"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, false);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isFunctionStatement());
  }

  @Test
  public void testDeadCodeAfterReturn() {
    String jsSource = "function foo() {"
        + "if (false) {"
        + "  return;"
        + "  alert('unreachable');"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isReturnStatement());
    assertFalse(root.getNext().isDefined());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testDeadCodeInsideLoop() {
    String jsSource = "function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i > 3) {"
        + "    return;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isFunctionStatement());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testDeadCodeAfterBreak() {
    String jsSource = "function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 3) {"
        + "    break;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isFunctionStatement());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testDeadCodeAfterContinue() {
    String jsSource = "function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 3) {"
        + "    continue;"
        + "  }"
        + "  alert('unreachable');"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isFunctionStatement());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testDeadCodeInsideSwitch() {
    String jsSource = "function foo() {"
        + "switch (foo) {"
        + "  case 1:"
        + "    return;"
        + "  case 2:"
        + "    alert('unreachable');"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isFunctionStatement());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testDeadCodeInsideTryCatch() {
    String jsSource = "function foo() {"
        + "try {"
        + "  throw new Error('test');"
        + "  alert('unreachable');"
        + "} catch (e) {"
        + "  alert('catch');"
        + "  return;"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunction());
    Node root = ast.getLastChild();
    assertTrue(root.isFunctionStatement());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });
  }

  @Test
  public void testDeadCodeInsideIf() {
    String jsSource = "function foo() {"
        + "if (false) {"
        + "  alert('unreachable');"
        + "}"
        + "}"
        + "function bar() {"
        + "if (foo()) {"
        + "  return;"
        + "} else {"
        + "  alert('bar');"
        + "}"
        + "}";

    Node ast = compiler.parseTree(jsSource);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(
        compiler, true);
    elimination.process(null, ast);

    assertTrue(ast.isFunctionStatement());
    Node foo = ast.getFirstChild();
    assertTrue(foo.isFunctionStatement());

    NodeTraversal.traverse(compiler, ast, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isExpressionStatement() && n.getFirstChild().isIdentifier(
            "alert")) {
          assertFalse(n.isDefined());
        }
      }
    });

    assertTrue(isFunction(ast.getLastChild()));
    Node bar = ast.getLastChild().getFirstChild();
    assertTrue(bar.isIfStatement());
    Node ifCondition = bar.getCondition();
    Node fooCall = ifCondition.getLastChild();
    assertTrue(fooCall.isCallExpression());
    assertTrue(fooCall.getExpression().equals(foo));
    assertTrue(ifCondition.getElsePart().isDefined());
  }
}