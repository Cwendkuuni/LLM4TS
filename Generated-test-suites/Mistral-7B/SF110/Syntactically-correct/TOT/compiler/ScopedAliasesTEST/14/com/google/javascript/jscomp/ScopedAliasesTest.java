import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.LogLevel;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  /**
   * Verifies that the alias is correctly transformed.
   *
   * @param compiler the compiler instance for testing.
   * @param aliasName the name of the alias to test.
   * @param aliasValue the value of the alias to test.
   */
  private static void testAliasTransformation(AbstractCompiler compiler,
                                             String aliasName,
                                             String aliasValue) {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {
      @Override
      public LogLevel logLevel() {
        return LogLevel.WARNING;
      }

      @Override
      public void logAliasTransformation(String fileName, SourcePosition pos,
                                        String aliasName, String aliasValue) {
        assertEquals("Unexpected alias transformation",
            aliasName, aliasName);
        assertEquals("Unexpected alias value", aliasValue, aliasValue);
      }
    });
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, options.getAliasTransformationHandler());

    String input = "goog.scope(function() {" +
        "  var " + aliasName + " = " + aliasValue + ";" +
        "});";

    NodeAST ast = NodePrinter.print(NodeUtil.compile(compiler, input));

    Node root = ast.getRootNode();
    scopedAliases.process(null, root);

    // Verify the alias has been transformed.
    Node newRoot = ast.getRootNode();
    Node newNode = newRoot.findChildByTypes(Node.CALL,
        Scope.VAR, Node.IDENTIFIER,
        Scope.FUNCTION, Node.BLOCK, Node.RETURN);
    assertTrue("Expected alias node to exist", newNode != null);
    Node newAliasNode = newNode.getFirstChild().getLastChild();
    assertEquals("Unexpected alias name", aliasValue, newAliasNode.getString());
  }

  /**
   * Verifies that the alias is not transformed if it's not defined in a goog.scope.
   */
  @Test
  public void testAliasNotTransformedOutsideGoogScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "var goog = {}; goog.x = 1;";

    String output = NodePrinter.print(
        NodeUtil.compile(compiler, input)).getCode();

    assertFalse("Expected alias not to be transformed", output.contains("goog.x = "));
  }

  /**
   * Verifies that the alias is not transformed if it's a function.
   */
  @Test
  public void testAliasNotTransformedIfFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "goog.scope(function() {" +
        "  function " + "goog" + "() {" +
        "    return 1;" +
        "  }}" +
        "});";

    String output = NodePrinter.print(
        NodeUtil.compile(compiler, input)).getCode();

    assertFalse("Expected alias not to be transformed", output.contains("goog = "));
  }

  /**
   * Verifies that the alias is not transformed if it's a variable declared
   * outside of a goog.scope.
   */
  @Test
  public void testAliasNotTransformedOutsideScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "var goog = {}; goog.scope(function() {" +
        "  var x = " + "goog" + ";" +
        "});";

    String output = NodePrinter.print(
        NodeUtil.compile(compiler, input)).getCode();

    assertFalse("Expected alias not to be transformed", output.contains("goog.x = "));
  }

  /**
   * Verifies that the alias is not transformed if it's a local variable.
   */
  @Test
  public void testAliasNotTransformedLocalVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "goog.scope(function() {" +
        "  function foo() {" +
        "    var " + "goog" + " = this;" +
        "  }" +
        "  foo();" +
        "});";

    String output = NodePrinter.print(
        NodeUtil.compile(compiler, input)).getCode();

    assertFalse("Expected alias not to be transformed", output.contains("goog = "));
  }

  /**
   * Verifies that the alias is not transformed if it's passed as a function
   * argument.
   */
  @Test
  public void testAliasNotTransformedFunctionArgument() {
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "function bar(goog) {" +
        "  // Do something with goog" +
        "}" +
        "goog.scope(function() {" +
        "  bar(goog);" +
        "});";

    String output = NodePrinter.print(
        NodeUtil.compile(compiler, input)).getCode();

    assertFalse("Expected alias not to be transformed", output.contains("goog = "));
  }

  /**
   * Verifies that the alias is transformed correctly when it's defined in a
   * goog.scope.
   */
  @Test
  public void testAliasTransformedInsideGoogScope() {
    testAliasTransformation(
        new AbstractCompiler(), "goog.dom", "dom");
  }

  /**
   * Verifies that the alias is transformed correctly when it's defined in a
   * goog.scope with a qualified name.
   */
  @Test
  public void testAliasTransformedInsideGoogScopeWithQualifiedName() {
    testAliasTransformation(
        new AbstractCompiler(), "goog.dom.Element", "Element");
  }

  /**
   * Verifies that the alias is transformed correctly when it's defined in a
   * goog.scope with a JSDoc comment.
   */
  @Test
  public void testAliasTransformedInsideGoogScopeWithJSDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "/** @suppress {alias} */" +
        "goog.scope(function() {" +
        "  /** @type {goog.dom.Element} */" +
        "  var " + "goog" + " = goog.dom.createElement;" +
        "});";

    String output = NodePrinter.print(
        NodeUtil.compile(compiler, input)).getCode();

    assertTrue("Expected alias to be transformed", output.contains("goog.dom.createElement"));
  }
}