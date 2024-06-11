import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testCheckSideEffects_SimpleStatement() {
    String code = "function foo() { x = 1; }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    Node root = ast.get(0);
    checker.process(null, root);

    assertTrue(checker.problemNodes.isEmpty());

    NodeUtil.checkNoErrors(compiler);
  }

  @Test
  public void testCheckSideEffects_EmptyStatement() {
    String code = "function foo() { }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    Node root = ast.get(0);
    checker.process(null, root);

    assertTrue(checker.problemNodes.isEmpty());

    NodeUtil.checkNoErrors(compiler);
  }

  @Test
  public void testCheckSideEffects_VoidStatement() {
    String code = "function foo() { ; }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    Node root = ast.get(0);
    checker.process(null, root);

    assertTrue(checker.problemNodes.isEmpty());

    NodeUtil.checkNoErrors(compiler);
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffect() {
    String code = "function foo() { document.write('Hello, World!'); }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    Node root = ast.get(0);
    checker.process(null, root);

    assertFalse(checker.problemNodes.isEmpty());

    Node problemNode = checker.problemNodes.get(0);
    NodeUtil.checkType(problemNode, Node.CALL_EXPR);

    NodeUtil.checkNoErrors(compiler);
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffect_Protected() {
    String code = "function foo() { JSCOMPILER_PRESERVE(document.write('Hello, World!')); }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
    Node root = ast.get(0);
    checker.process(null, root);

    assertTrue(checker.problemNodes.isEmpty());

    NodeUtil.checkNoErrors(compiler);
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffect_ProtectedAndRemoved() {
    String code = "function foo() { JSCOMPILER_PRESERVE(document.write('Hello, World!')); }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, true);
    Node root = ast.get(0);
    checker.process(null, root);

    checker.protectSideEffects();

    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripper);

    NodeUtil.checkNoErrors(compiler);

    CheckSideEffects newChecker = new CheckSideEffects(compiler, WARNING, false);
    newChecker.process(null, root);

    assertTrue(newChecker.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffects_StatementWithSideEffect_Error() {
    String code = "function foo() { document.write('Hello, World!'); }";
    List<Node> ast = NodeTraversal.parse(compiler, code, options);

    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    Node root = ast.get(0);
    checker.process(null, root);

    assertFalse(checker.problemNodes.isEmpty());

    Node problemNode = checker.problemNodes.get(0);
    NodeUtil.checkType(problemNode, Node.CALL_EXPR);

    CompilerPass passer = new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        throw new AssertionError("This should not be called.");
      }
    };

    NodeTraversal.traverse(compiler, root, passer);
  }
}