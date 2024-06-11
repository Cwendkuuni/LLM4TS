import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testBasicCase() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  var dom = goog.dom;" +
                               "  var DIV = dom.TagName.DIV;" +
                               "  dom.createElement(DIV);" +
                               "});");
    scopedAliases.process(null, testCode);
    Node expectedCode = Node.parse("goog.dom.createElement(goog.dom.TagName.DIV);");
    Node actualCode = compiler.getOutput();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEmptyBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    assertNull(actualCode);
  }

  @Test
  public void testSingleStatementWithinBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  goog.dom.createElement(goog.dom.TagName.DIV);" +
                               "});");
    scopedAliases.process(null, testCode);
    Node expectedCode = Node.parse("goog.dom.createElement(goog.dom.TagName.DIV);");
    Node actualCode = compiler.getOutput();
    assertEquals(expectedCode, actualCode);
  }

  @Test(expected = AssertionError.class)
  public void testMultipleStatementsWithinBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  goog.dom.createElement(goog.dom.TagName.DIV);" +
                               "  goog.dom.createElement(goog.dom.TagName.DIV);" +
                               "});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    // This test should throw an AssertionError because the scoped aliases
    // test should not allow multiple statements within a goog.scope block.
  }

  @Test
  public void testThisKeyword() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  function test() {" +
                               "    this.test = 1;" +
                               "  }" +
                               "  test();" +
                               "});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    assertNull(actualCode);
    // This test should fail because the scoped aliases test does not allow
    // the use of 'this' within a goog.scope block.
  }

  @Test
  public void testReturnKeyword() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  return 1;" +
                               "});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    assertNull(actualCode);
    // This test should fail because the scoped aliases test does not allow
    // the use of 'return' within a goog.scope block.
  }

  @Test
  public void testThrowKeyword() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  throw new Error();" +
                               "});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    assertNull(actualCode);
    // This test should fail because the scoped aliases test does not allow
    // the use of 'throw' within a goog.scope block.
  }

  @Test
  public void testAliasRedefined() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  var a = 1;" +
                               "  var a = 2;" +
                               "});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    assertNull(actualCode);
    // This test should fail because the scoped aliases test does not allow
    // an alias to be reassigned.
  }

  @Test
  public void testNonAliasLocalVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  var a = 1;" +
                               "});");
    scopedAliases.process(null, testCode);
    Node actualCode = compiler.getOutput();
    assertNull(actualCode);
    // This test should fail because the scoped aliases test does not allow
    // non-alias local variables within a goog.scope block.
  }

  @Test
  public void testAliasUsage() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  function test() {" +
                               "    var a = goog.dom.TagName.DIV;" +
                               "    goog.dom.createElement(a);" +
                               "  }" +
                               "  test();" +
                               "});");
    scopedAliases.process(null, testCode);
    Node expectedCode = Node.parse("goog.dom.createElement(goog.dom.TagName.DIV);");
    Node actualCode = compiler.getOutput();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAliasUsageWithNameCollision() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  var a = 1;" +
                               "  var goog = {dom: {" +
                               "    TagName: {" +
                               "      DIV: 'div'" +
                               "    }" +
                               "  };" +
                               "  goog.dom.createElement(a);" +
                               "});");
    scopedAliases.process(null, testCode);
    Node expectedCode = Node.parse("goog.dom.createElement('div');");
    Node actualCode = compiler.getOutput();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testErrorReporting() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node testCode = Node.parse("goog.scope(function() {" +
                               "  function test() {" +
                               "    this.test = 1;" +
                               "  }" +
                               "  test();" +
                               "});");
    scopedAliases.process(null, testCode);
    String errorMessage = compiler.getErrorMessage();
    assertEquals("goog.scope function body cannot reference 'this'", errorMessage);
    // This test should fail because the scoped aliases test does not allow
    // the use of 'this' within a goog.scope block.
  }

  // Helper class to avoid cluttering the test class with a dependency on
  // a NoOpAliasTransformationHandler.
  static class NoOpAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(
        String filePath, SourcePosition position) {
      return null;
    }
  }
}