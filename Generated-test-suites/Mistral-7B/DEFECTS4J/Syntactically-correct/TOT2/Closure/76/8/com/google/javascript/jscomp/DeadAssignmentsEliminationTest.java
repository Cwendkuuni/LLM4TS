import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testBasicAssignment() {
    String code = "function test() { var x = 1; x = 2; }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node ast = Parser.parse(code, null, null);
    deadAssignmentsElimination.process(null, ast);

    NodeTraversal t = new NodeTraversal(ast, null);
    Node result = t.getRoot();

    // The test passes if the code is not changed after dead assignments elimination.
    assertTrue(Nodes.isEqual(ast, result));
  }

  @Test
  public void testDeadAssignmentWithCondition() {
    String code = "function test() { if (true) { var x = 1; x = 2; } }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node ast = Parser.parse(code, null, null);
    deadAssignmentsElimination.process(null, ast);

    NodeTraversal t = new NodeTraversal(ast, null);
    Node result = t.getRoot();

    // The test passes if the code is not changed after dead assignments elimination.
    assertTrue(Nodes.isEqual(ast, result));
  }

  @Test
  public void testDeadAssignmentWithLoop() {
    String code = "function test() { for (var i = 0; i < 10; i++) { var x = 1; x = 2; } }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node ast = Parser.parse(code, null, null);
    deadAssignmentsElimination.process(null, ast);

    NodeTraversal t = new NodeTraversal(ast, null);
    Node result = t.getRoot();

    // The test passes if the code is not changed after dead assignments elimination.
    assertTrue(Nodes.isEqual(ast, result));
  }

  @Test
  public void testDeadAssignmentWithReturn() {
    String code = "function test() { return var x = 1; x = 2; }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node ast = Parser.parse(code, null, null);
    deadAssignmentsElimination.process(null, ast);

    NodeTraversal t = new NodeTraversal(ast, null);
    Node result = t.getRoot();

    // The test passes if the code is not changed after dead assignments elimination.
    assertTrue(Nodes.isEqual(ast, result));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentWithException() {
    String code = "function test() { try { var x = 1; x = 2; } catch (e) {} }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node ast = Parser.parse(code, null, null);
    deadAssignmentsElimination.process(null, ast);

    NodeTraversal t = new NodeTraversal(ast, null);
    Node result = t.getRoot();

    // It is expected that the dead assignment elimination fails when there
    // is an exception handler.
    deadAssignmentsElimination.tryRemoveDeadAssignments(t, null);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentWithFunctionCall() {
    String code = "function test() { foo(); var x = 1; x = 2; }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node ast = Parser.parse(code, null, null);
    deadAssignmentsElimination.process(null, ast);

    NodeTraversal t = new NodeTraversal(ast, null);
    Node result = t.getRoot();

    // It is expected that the dead assignment elimination fails when there
    // is a function call.
    deadAssignmentsElimination.tryRemoveDeadAssignments(t, null);
  }
}