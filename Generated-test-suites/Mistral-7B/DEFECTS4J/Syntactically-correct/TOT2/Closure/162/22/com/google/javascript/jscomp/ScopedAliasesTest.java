import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.hotswapping.NodeUtil;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that the alias "dom" was correctly transformed.
    assertEquals("goog.dom", rootNode.getFirstChild().getString());

    // Check that the "createElement" method was correctly applied.
    Node createElementNode = rootNode.getFirstChild().getLastChild();
    assertEquals("createElement", createElementNode.getQualifiedName());

    // Check that the "DIV" constant was correctly transformed.
    Node divNode = createElementNode.getLastChild();
    assertEquals("DIV", divNode.getString());

    // Check that the "TagName" was correctly transformed.
    assertEquals("TagName.DIV", divNode.getParent().getQualifiedName());

    // Check that no errors were reported during processing.
    assertNull(compiler.getErrors());
  }

  @Test
  public void testImproperlyUsedGoogScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {});" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for improper use of goog.scope.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", error.getTypeName());
  }

  @Test
  public void testGoogScopeWithBadParameters() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {}, 42);" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for bad parameters in goog.scope.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getTypeName());
  }

  @Test
  public void testGoogScopeReferencesThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  this.test = 42;" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for "this" in goog.scope.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", error.getTypeName());
  }

  @Test
  public void testGoogScopeUsesReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  return 42;" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for "return" in goog.scope.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getTypeName());
  }

  @Test
  public void testGoogScopeUsesThrow() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('Test');" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for "throw" in goog.scope.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getTypeName());
  }

  @Test
  public void testAliasRedefined() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  var goog = {};" +
        "  goog.dom = goog.dom;" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for alias redefined.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getTypeName());
  }

  @Test
  public void testNonAliasLocalVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  var test = 42;" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that an error was reported for non-alias local variable.
    Assert.assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getTypeName());
  }

  @Test
  public void testHotSwap() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node rootNode = Parser.parse(sourceCode, null, null);

    scopedAliases.process(null, rootNode);

    // Check that the alias "dom" was correctly transformed.
    assertEquals("goog.dom", rootNode.getFirstChild().getString());

    // Update the source code to test hot swapping.
    sourceCode = "goog.scope(function() {" +
        "  var google = {};" +
        "  google.dom = google.dom;" +
        "  dom.createElement(google.dom.TagName.DIV);" +
        "});";

    scopedAliases.hotSwapScript(rootNode, null);

    // Check that the alias "google.dom" was correctly transformed.
    assertEquals("google.dom", rootNode.getFirstChild().getString());
  }
}