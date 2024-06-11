import static com.google.javascript.jscomp.NodeUtil.createNode;
import static com.google.javascript.jscomp.NodeUtil.isTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

  @org.junit.Test
  public void testEmptyFunction() {
    String jsCode = "function foo() {}";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithReturnAndDeadCode() {
    String jsCode = "function foo() {" +
        "return;" +
        "alert('unreachable');" +
        "}";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeOnly() {
    String jsCode = "function foo() {" +
        "alert('unreachable');" +
        "}";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeAndSideEffects() {
    String jsCode = "function foo() {" +
        "alert('unreachable');" +
        "document.write('Hello World');" +
        "}";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());

    // Make sure document.write was never called
    assertTrue(compiler.getOutput().getFirstChild().getType() == Token.EOF);
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInNestedBlock() {
    String jsCode = "function foo() {" +
        "if (true) {" +
        "  return;" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInConditionalBranch() {
    String jsCode = "function foo() {" +
        "if (false) {" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInSwitchStatement() {
    String jsCode = "function foo() {" +
        "switch (1) {" +
        "  case 0:" +
        "    alert('unreachable');" +
        "    break;" +
        "  case 1:" +
        "    break;" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInLoop() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInLoopWithBreak() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    break;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInLoopWithContinue() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    continue;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInLoopWithNoOpStatement() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    true;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInLoopWithRemoveNoOpStatement() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  if (i == 5) {" +
        "    true;" +
        "  }" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());

    // Make sure the no-op statement was removed
    Node expected = NodeUtil.parseJs("function foo() {" +
      "for (var i = 0; i < 10; i++) {" +
      "  if (i == 5) {" +
      "    return;" +
      "  }" +
      "}" +
      "}");
    NodeUtil.compareTrees(result, expected);
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInCondition() {
    String jsCode = "function foo() {" +
        "if (true) {" +
        "  alert('unreachable');" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInSwitchStatementWithNoOpStatement() {
    String jsCode = "function foo() {" +
        "switch (1) {" +
        "  case 0:" +
        "    true;" +
        "    alert('unreachable');" +
        "    break;" +
        "  case 1:" +
        "    break;" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInSwitchStatementWithBreakStatement() {
    String jsCode = "function foo() {" +
        "switch (1) {" +
        "  case 0:" +
        "    break;" +
        "    alert('unreachable');" +
        "    break;" +
        "  case 1:" +
        "    break;" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithDeadCodeInSwitchStatementWithContinueStatement() {
    String jsCode = "function foo() {" +
        "switch (1) {" +
        "  case 0:" +
        "    continue;" +
        "    alert('unreachable');" +
        "    break;" +
        "  case 1:" +
        "    break;" +
        "}" +
        "}" +
        "foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    Node rootNode = NodeUtil.parseJs(jsCode, options);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeUtil.assertNoErrors(compiler.getErrors());
    Node result = eliminator.compiler.getOutput();
    assertTrue(result.toString().isEmpty());
  }
}