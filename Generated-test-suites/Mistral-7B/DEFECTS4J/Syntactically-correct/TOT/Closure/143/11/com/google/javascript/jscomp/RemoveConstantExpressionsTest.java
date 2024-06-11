import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test_util.TreeUtil;

import org.junit.Test;

public class RemoveConstantExpressionsTest {

  @Test
  public void testSimpleExpression() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticUsers(true);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    String jsCode = "function foo() { return 1; } 1 + foo() + 2;";
    Node externalRoot = TreeUtil.parse(jsCode, null);
    pass.process(null, externalRoot);
    Node root = compiler.getOutput();

    // assert that the constant expression was removed
    Node expectedResult = new Node(Token.EXPR_LIST, Lists.newArrayList(
        new Node(Token.EXPR_IDENT, "foo"),
        new Node(Token.EXPR_NUM, 2.0)
    ));
    assertTrue(TreeUtil.isEquivalent(expectedResult, root));

    // assert that no diagnostics were produced
    assertFalse(compiler.getDiagnostics().hasErrors());
  }

  @Test
  public void testExpressionWithSideEffect() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticUsers(true);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    String jsCode = "function foo() { x = 1; } 1 + foo() + 2;"; // variable x added for side effect
    Node externalRoot = TreeUtil.parse(jsCode, null);
    pass.process(null, externalRoot);
    Node root = compiler.getOutput();

    // assert that the constant expression was not removed
    Node expectedResult = new Node(Token.EXPR_NUM, 4.0);
    assertTrue(TreeUtil.isEquivalent(expectedResult, root));

    // assert that no diagnostics were produced
    assertFalse(compiler.getDiagnostics().hasErrors());
  }

  @Test(expected = AssertionError.class)
  public void testErrorScenario() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticUsers(true);
    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);

    String jsCode = "function foo() { throw new Error('foo'); } 1 + foo() + 2;";
    Node externalRoot = TreeUtil.parse(jsCode, null);
    pass.process(null, externalRoot);

    // this test case should throw an AssertionError as the compiler should not be able to handle the error
  }
}