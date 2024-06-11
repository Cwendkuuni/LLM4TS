import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.enterScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.exitScope;
import static com.google.javascript.jscomp.NodeTraversal.NodeUtil.redeclareVarsInsideBranch;
import static com.google.javascript.jscomp.NodeTraversal.ScopedCallback.process;
import static com.google.javascript.jscomp.UnreachableCodeElimination.*;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testBasicFunctionality() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { return; alert('unreachable'); }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.RETURN);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testNoOpStatementsRemoval() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var x = true; }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testEdgeCaseWithLoops() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { for (var i = 0; i < 10; i++) { if (i > 5) { break; } } }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testExceptionHandling() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { try { throw new Error('test'); } finally { alert('unreachable'); } }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.TRY);
    assertTrue(root.getLastChild() != null);
  }

  @Test
  public void testComplexEdgeCases() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var a = 5; try { try { var b = 10; throw new Error('test2'); } finally { return; } } finally { alert('unreachable'); } }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testComplexControlFlowGraph() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { try { if (false) { throw new Error('test'); } } catch (e) { if (false) { throw new Error('test2'); } } }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testComplexControlFlowGraphWithSideEffects() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { try { if (false) { throw new Error('test'); } } catch (e) { var x = 5; } }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() != null);
  }

  @Test
  public void testComplexControlFlowGraphWithConditionalBranches() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var a = 5; if (false) { if (a > 5) { return; } } alert('unreachable'); }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testComplexControlFlowGraphWithSwitchStatements() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var x = 5; switch (x) { case 5: return; } alert('unreachable'); }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testComplexControlFlowGraphWithLoops() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var i = 0; while (true) { if (i > 5) { return; } i++; } alert('unreachable'); }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testComplexControlFlowGraphWithLoopsAndConditionalBranches() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var i = 0; while (true) { if (i > 5) { return; } if (i < 3) { continue; } i++; } alert('unreachable'); }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }

  @Test
  public void testComplexControlFlowGraphWithLoopsAndReturnStatements() {
    Compiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = AbstractSyntaxTree.parseJs("function test() { var i = 0; while (true) { if (i > 5) { return; } i++; } alert('unreachable'); }");
    elimination.process(null, root);

    assertTrue(root.getFirstChild().getType() == Token.FUNCTION);
    assertTrue(root.getLastChild() == null);
  }
}