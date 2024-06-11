import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

/**
 * Test class for ScopedAliases class.
 */
public class ScopedAliasesTest {
  /** Test data for testing ScopedAliases functionality. */
  private static final class TestData {
    private final String input;
    private final String expectedOutput;
    private final CompilerOptions compilerOptions;

    TestData(String input, String expectedOutput, CompilerOptions compilerOptions) {
      this.input = input;
      this.expectedOutput = expectedOutput;
      this.compilerOptions = compilerOptions;
    }
  }

  @org.junit.BeforeClass
  public static void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, new DefaultAliasTransformationHandler());
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setOption(AliasTransformation.GOOG_SCOPE_AGGRESSIVE, true);
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(compilerOptions, preprocessorSymbolTable);
    scopedAliasesPass.setTransformHandler(transformationHandler);
    compiler.addPass(scopedAliasesPass);
  }

  @org.junit.Test
  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {" +
        "  var goog = {};" +
        "  var dom = goog.dom;" +
        "  dom.createElement('DIV');" +
        "});";

    TestData testData = new TestData(input, "goog.dom.createElement('DIV');", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(testData.input);
    Node actualOutput = compiler.compileDeep(rootNode, testData.compilerOptions);
    String actual = NodePrinter.printTree(actualOutput);
    assertEquals(testData.expectedOutput, actual);
  }

  @org.junit.Test
  public void testScopedAliasWithParameters() {
    String input = "goog.scope(function(x) {" +
        "  var goog = {};" +
        "  var dom = goog.dom;" +
        "  dom.createElement('DIV', x);" +
        "});";

    TestData testData = new TestData(input, "goog.dom.createElement('DIV', arguments[0]);", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(testData.input);
    Node actualOutput = compiler.compileDeep(rootNode, testData.compilerOptions);
    String actual = NodePrinter.printTree(actualOutput);
    assertEquals(testData.expectedOutput, actual);
  }

  @org.junit.Test
  public void testScopedAliasWithReturnStatement() {
    String input = "goog.scope(function() {" +
        "  var goog = {};" +
        "  return goog.dom.createElement('DIV');" +
        "});";

    TestData testData = new TestData(input, "function() {" +
        "  return goog.dom.createElement('DIV');" +
        "}", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(testData.input);
    Node actualOutput = compiler.compileDeep(rootNode, testData.compilerOptions);
    String actual = NodePrinter.printTree(actualOutput);
    assertEquals(testData.expectedOutput, actual);
  }

  @org.junit.Test
  public void testScopedAliasWithThrowStatement() {
    String input = "goog.scope(function() {" +
        "  try {" +
        "    goog.dom.createElement('DIV');" +
        "  } catch (e) {" +
        "    throw new Error('Test Error');" +
        "  }" +
        "});";

    TestData testData = new TestData(input, "function() {" +
        "  try {" +
        "    goog.dom.createElement('DIV');" +
        "  } catch (e) {" +
        "    throw new Error('Test Error');" +
        "  }" +
        "}", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(testData.input);
    Node actualOutput = compiler.compileDeep(rootNode, testData.compilerOptions);
    String actual = NodePrinter.printTree(actualOutput);
    assertEquals(testData.expectedOutput, actual);
  }

  @org.junit.Test
  public void testScopedAliasWithThisKeyword() {
    String input = "goog.scope(function() {" +
        "  this.x = 'Test';" +
        "  goog.scope(function() {" +
        "    this.x = 'Test2';" +
        "  });" +
        "  return this.x;" +
        "});";

    TestData testData = new TestData(input, "function() {" +
        "  this.x = 'Test';" +
        "  function() {" +
        "    this.x = 'Test2';" +
        "  }();" +
        "  return this.x;" +
        "}", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(testData.input);
    Node actualOutput = compiler.compileDeep(rootNode, testData.compilerOptions);
    String actual = NodePrinter.printTree(actualOutput);
    assertEquals(testData.expectedOutput, actual);
  }

  @org.junit.Test
  public void testScopedAliasWithImproperUsage() {
    String input = "goog.scope(function() {" +
        "  var x = 1;" +
        "});" +
        "goog.scope(function() {" +
        "  var x = 2;" +
        "});";

    TestData testData = new TestData(input, "", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(input);
    try {
      compiler.compileDeep(rootNode, testData.compilerOptions);
      fail();
    } catch (RuntimeException e) {
      assertEquals("Expected JSC_GOOG_SCOPE_USED_IMPROPERLY error", e.getMessage());
    }
  }

  @org.junit.Test
  public void testScopedAliasWithBadParameters() {
    String input = "goog.scope(" +
        "  function(x, y) {" +
        "  }" +
        ");";

    TestData testData = new TestData(input, "", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(input);
    try {
      compiler.compileDeep(rootNode, testData.compilerOptions);
      fail();
    } catch (RuntimeException e) {
      assertEquals("Expected JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS error", e.getMessage());
    }
  }

  @org.junit.Test
  public void testScopedAliasWithAliasRedefined() {
    String input = "goog.scope(function() {" +
        "  var goog = {};" +
        "  var dom = goog.dom;" +
        "  goog.dom = function() {};" +
        "  dom.createElement('DIV');" +
        "});";

    TestData testData = new TestData(input, "", new CompilerOptions());
    Node rootNode = NodePrinter.parseJs(input);
    try {
      compiler.compileDeep(rootNode, testData.compilerOptions);
      fail();
    } catch (RuntimeException e) {
      assertEquals("Expected JSC_GOOG_SCOPE_ALIAS_REDEFINED error", e.getMessage());
    }
  }
}