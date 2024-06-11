import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_ALIASES);
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be no compile errors
    assertTrue(errorCollector.getDiagnostics().isEmpty());

    // And the output should be
    String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV)";
    String actualOutput = sourceFile.getCode();
    assertEquals(expectedOutput, actualOutput);

    // And the traversal should have collected alias definitions in order
    Traversal traversal = compiler.getPassResult(ScopedAliases.class);
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());
    assertEquals("DIV", aliasDefinitions.get(0).getString());

    // And the traversal should have collected alias usages
    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    Node aliasUsage = aliasUsages.get(0);
    assertEquals("dom.TagName.DIV", aliasUsage.getParent().toString());
  }

  @Test
  public void testScopedAliasesWithBrokenGoogScopeCall() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "})" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be one error related to "goog.scope" being called multiple times
    List<com.google.javascript.jscomp.Diagnostic> diagnostics = errorCollector.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostics.get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithImproperlyNamedAlias() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope('dom')({" +
        "  DIV: goog.dom.TagName.DIV," +
        "  createElement: goog.dom.createElement" +
        "})" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be one error related to improperly named alias
    List<com.google.javascript.jscomp.Diagnostic> diagnostics = errorCollector.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostics.get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithRedefinedAlias() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "})" +
        "goog.scope(function() {" +
        "  var DIV = 42;" +
        "})" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be one error related to redefined alias
    List<com.google.javascript.jscomp.Diagnostic> diagnostics = errorCollector.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostics.get(0).getType().name());

  }

  @Test
  public void testScopedAliasesWithFunctionInsideGoogScope() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "function foo() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "}" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be no errors
    assertTrue(errorCollector.getDiagnostics().isEmpty());

    // And the output should be
    String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV)";
    String actualOutput = sourceFile.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testScopedAliasesWithReturnInsideGoogScope() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be one error related to return inside goog.scope
    List<com.google.javascript.jscomp.Diagnostic> diagnostics = errorCollector.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostics.get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithThrowInsideGoogScope() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function() {" +
        "throw new Error('Test error');" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be one error related to throw inside goog.scope
    List<com.google.javascript.jscomp.Diagnostic> diagnostics = errorCollector.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostics.get(0).getType().name());
  }

  @Test
  public void testScopedAliasesWithAnonymousFunctionWithParameters() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function(a, b) {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be no errors
    assertTrue(errorCollector.getDiagnostics().isEmpty());

    // And the output should be
    String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV)";
    String actualOutput = sourceFile.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testScopedAliasesWithFunctionWithName() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function myFunction() {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be no errors
    assertTrue(errorCollector.getDiagnostics().isEmpty());

    // And the output should be
    String expectedOutput = "myFunction()";
    String actualOutput = sourceFile.getCode();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testScopedAliasesWithFunctionWithReturnType() {
    // Given
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.registerPass(new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler()));

    String sourceCode = "goog.scope(function myFunction(): number {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.dom.createElement(DIV).nodeType;" +
        "return 42;" +
        "})";

    SourceFile sourceFile = new SourceFile("Test.java", sourceCode, null);
    ErrorCollector errorCollector = new ErrorCollector();

    // When
    compiler.compile(options, sourceFile, errorCollector, null);

    // Then
    // There should be no errors
    assertTrue(errorCollector.getDiagnostics().isEmpty());

    // And the output should be
    String expectedOutput = "myFunction()";
    String actualOutput = sourceFile.getCode();
    assertEquals(expectedOutput, actualOutput);
  }
}