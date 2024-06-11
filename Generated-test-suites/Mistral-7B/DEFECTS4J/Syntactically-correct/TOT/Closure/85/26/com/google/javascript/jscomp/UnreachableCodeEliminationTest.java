import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setup() {
    // Initialize the compiler and pass manager.
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    passManager = new CompilerPassManager(compiler, options);
  }

  @Test
  public void testEmptyReturnStatement() {
    String code = "function test() { return; }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The return statement should be removed.
    assertNull(result.getFirstChild().getValue());
  }

  @Test
  public void testEmptyBlockStatement() {
    String code = "function test() { }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The empty block statement should be removed.
    assertNull(result.getFirstChild());
  }

  @Test
  public void testSimpleStatementWithDeadCode() {
    String code = "function test() { alert('unreachable'); x = 5; }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The dead code 'alert('unreachable')' should be removed.
    assertNull(result.getLastChild());
  }

  @Test
  public void testStatementWithSideEffects() {
    String code = "function test() { x = 5; alert('reached'); }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The statement 'alert('reached')' has side effects and should not be removed.
    assertTrue(result.getLastChild().getValue().hasSideEffects());
  }

  @Test
  public void testStatementWithConditionalBranching() {
    String code = "function test() { if (true) { return; } alert('unreachable'); }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The dead code 'alert('unreachable')' should be removed.
    assertNull(result.getLastChild());
  }

  @Test
  public void testStatementWithUnconditionalBranching() {
    String code = "function test() { break label1; alert('unreachable'); }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The dead code 'alert('unreachable')' should be removed.
    assertNull(result.getLastChild());

    // Check if the break statement is still valid.
    Node breakStatement = result.getFirstChild();
    Node label1 = NodeUtil.getLabelNode(breakStatement.getFirstChild());
    assertTrue(breakStatement.getValue().isBreak());
    assertTrue(label1 != null);
  }

  @Test
  public void testStatementWithNestedUnconditionalBranching() {
    String code = "function test() { break label1; break label2; alert('unreachable'); }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The dead code 'alert('unreachable')' should be removed.
    assertNull(result.getLastChild());

    // Check if the outer break statement is still valid.
    Node breakStatement = result.getFirstChild();
    Node label1 = NodeUtil.getLabelNode(breakStatement.getFirstChild());
    assertTrue(breakStatement.getValue().isBreak());
    assertTrue(label1 != null);

    // Check if the inner break statement is still valid.
    Node innerBreakStatement = result.getLastChild().getFirstChild();
    Node label2 = NodeUtil.getLabelNode(innerBreakStatement.getFirstChild());
    assertTrue(innerBreakStatement.getValue().isBreak());
    assertTrue(label2 != null);
  }

  @Test
  public void testStatementWithNoOpExpression() {
    String code = "function test() { true && false; }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInConditionalBranching() {
    String code = "function test() { if (true) { true && false; } }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInUnconditionalBranching() {
    String code = "function test() { break; true && false; }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInLoop() {
    String code = "function test() { for (;;) { true && false; } }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInSwitchCase() {
    String code = "function test() { switch (1) { case 1: true && false; } }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInTryCatchBlock() {
    String code = "function test() { try { true && false; } catch (e) {} }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInTryFinallyBlock() {
    String code = "function test() { try { true && false; } finally {} }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithPrototypePropertyAccess() {
    String code = "function test() { var x = { foo: 5 }; x.foo; }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The statement 'x.foo' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithPrototypeMethodCall() {
    String code = "function test() { var x = { foo: function() {} }; x.foo(); }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The statement 'x.foo()' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithConditionalBranchingAndNoOpExpression() {
    String code = "function test() { if (true) { true && false; } alert('unreachable'); }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());

    // The dead code 'alert('unreachable')' should be removed.
    assertNull(result.getLastChild());
  }

  @Test
  public void testStatementWithUnconditionalBranchingAndNoOpExpression() {
    String code = "function test() { break; true && false; }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInLoopAndConditionalBranching() {
    String code = "function test() { for (;;) { if (true) { true && false; } } }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }

  @Test
  public void testStatementWithNoOpExpressionInSwitchCaseAndConditionalBranching() {
    String code = "function test() { switch (1) { case 1: if (true) { true && false; } } }";
    Node root = NodeUtil.parseJavaScript(code);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(pass);
    Node result = passManager.process(root, null);

    // The no-op expression 'true && false' should not be removed.
    assertTrue(result.getFirstChild().getValue().mayHaveSideEffects());
  }
}