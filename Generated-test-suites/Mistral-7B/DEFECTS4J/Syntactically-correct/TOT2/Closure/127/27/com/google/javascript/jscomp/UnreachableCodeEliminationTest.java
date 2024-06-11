import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.test.compilers.ProcessorTestHelper.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Compiler compiler;
  private CompilerPass unreachableCodeElimination;

  @Before
  public void setUp() {
    compiler = newCompiler();
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
  }

  @Test
  public void testSimpleFunction() {
    String source = "function main() { return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCode() {
    String source = "function main() { return 1; alert('dead code'); }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithReturnAfterDeadCode() {
    String source = "function main() { alert('dead code'); return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndSideEffect() {
    String source = "function main() { alert('dead code'); document.write('test'); }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { document.write('test'); }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeInBlock() {
    String source = "function main() { alert('dead code'); return 1; } function inner() {}";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; } function inner() {}");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeInBlockAndSideEffect() {
    String source = "function main() { alert('dead code'); document.write('test'); return 1; } function inner() {}";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { document.write('test'); return 1; } function inner() {}");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndIfStatement() {
    String source = "function main() { if (true) { alert('dead code'); } return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndIfStatementWithElse() {
    String source = "function main() { if (true) { alert('dead code'); } else { return 1; } }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndSwitchStatement() {
    String source = "function main() { switch (0) { case 0: alert('dead code'); break; case 1: return 1; } }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndSwitchStatementWithDefault() {
    String source = "function main() { switch (0) { case 0: alert('dead code'); break; case 1: return 1; default: return 2; } }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 2; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndSwitchStatementWithFallThrough() {
    String source = "function main() { switch (0) { case 0: alert('dead code'); case 1: return 1; } }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndCatchBlock() {
    String source = "try { throw new Error('test'); } catch (e) { alert('dead code'); } finally { return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "try { throw new Error('test'); } finally { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndTryCatchBlockWithReturn() {
    String source = "function main() { try { throw new Error('test'); } catch (e) { return 1; } }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { throw new Error('test'); }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndTryCatchBlockWithThrow() {
    String source = "function main() { try { throw new Error('test'); } catch (e) { throw new Error('test2'); } }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { throw new Error('test2'); }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndWhileLoop() {
    String source = "function main() { while (true) { alert('dead code'); } return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndForLoop() {
    String source = "function main() { for (int i = 0; i < 10; i++) { alert('dead code'); } return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testFunctionWithDeadCodeAndForInLoop() {
    String source = "function main() { for (var prop in Object.prototype) { alert('dead code'); } return 1; }";
    Node root = parseAndTraverse(compiler, source);
    unreachableCodeElimination.process(compilersExterns(), root);
    Node expected = parseAndTraverse(compiler, "function main() { return 1; }");
    assertEquals(expected.toString(), root.toString());
  }
}