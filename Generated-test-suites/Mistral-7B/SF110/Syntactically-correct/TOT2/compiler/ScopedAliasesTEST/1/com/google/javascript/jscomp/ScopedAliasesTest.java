import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ParserTestingUtils;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSON;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testSimpleAlias() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function() {" +
        "  var x = goog.dom; " +
        "  x.createElement('div'); " +
        "});");

    compiler.compile(sourceFile, null, options);
    String output = compiler.getCompiledOutput();

    String expectedOutput = "goog.dom.createElement('div')";
    assertEquals(expectedOutput, output);
  }

  @Test(expected = AssertionError.class)
  public void testAliasRedefinition() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function() {" +
        "  var x = goog.dom; " +
        "  var x = 'hello'; " +
        "});");

    compiler.compile(sourceFile, null, options);

    // This test should throw an AssertionError due to the GOOG_SCOPE_ALIAS_REDEFINED error.
  }

  @Test
  public void testAliasWithNamespace() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      createElement: function(tagName) {" +
        "        return document.createElement(tagName);" +
        "      }" +
        "    }" +
        "  };" +
        "  goog.dom.createElement('div');" +
        "});");

    compiler.compile(sourceFile, null, options);
    String output = compiler.getCompiledOutput();

    String expectedOutput = "document.createElement('div')";
    assertEquals(expectedOutput, output);
  }

  @Test(expected = AssertionError.class)
  public void testCallToScopeFunctionWithParameters() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function(x, y) {" +
        "});");

    compiler.compile(sourceFile, null, options);

    // This test should throw an AssertionError due to the GOOG_SCOPE_HAS_BAD_PARAMETERS error.
  }

  @Test(expected = AssertionError.class)
  public void testCallToScopeFunctionWithNonAnonymousFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function foo() {" +
        "});");

    compiler.compile(sourceFile, null, options);

    // This test should throw an AssertionError due to the GOOG_SCOPE_REFERENCES_THIS error.
  }

  @Test(expected = AssertionError.class)
  public void testCallToScopeFunctionWithReturnStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function() {" +
        "  return 42;" +
        "});");

    compiler.compile(sourceFile, null, options);

    // This test should throw an AssertionError due to the GOOG_SCOPE_USES_RETURN error.
  }

  @Test(expected = AssertionError.class)
  public void testCallToScopeFunctionWithThrowStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    SourceFile sourceFile = ParserTestingUtils.parseJavaScript("test.js",
        "goog.scope(function() {" +
        "  throw new Error('Something went wrong');" +
        "});");

    compiler.compile(sourceFile, null, options);

    // This test should throw an AssertionError due to the GOOG_SCOPE_USES_THROW error.
  }
}