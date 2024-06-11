import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilationTest.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  goog.dom.createElement(DIV);"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    assertTrue(result.getDiagnostics().isEmpty());
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "goog.dom.createElement(goog.dom.TagName.DIV)",
        "Removed unused goog.scope block",
        "goog is now an alias for window",
        "dom is now an alias for goog.dom"
    );
    assertEquals(expectedMessages, messages);
  }

  @Test
  public void testScopedAliasesWithJSDoc() {
    String jsSource = ""
        + "/** @type {goog.dom.DomHelper} */"
        + "var helper = goog.scope(function() {"
        + "  return goog.dom.getDomHelper();"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    assertTrue(result.getDiagnostics().isEmpty());
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "goog.dom.getDomHelper is now an alias for window.goog.dom.getDomHelper",
        "Removed unused goog.scope block",
        "goog is now an alias for window"
    );
    assertEquals(expectedMessages, messages);
  }

  @Test
  public void testScopedAliasesWithFunctionCall() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  function foo() {}"
        + "});"
        + "function bar() {"
        + "  foo();"
        + "}"
        + "bar();";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    assertTrue(result.getDiagnostics().isEmpty());
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "Removed unused goog.scope block"
    );
    assertEquals(expectedMessages, messages);
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "});"
        + "goog.scope(function() {"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    List<DiagnosticType> diagnostics = result.getDiagnostics();
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "goog.scope can only be called once per file."
    );
    assertEquals(expectedMessages, messages);
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithInvalidFunction() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  var x = 1;"
        + "});"
        + "goog.scope(function() {"
        + "  return x;"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    List<DiagnosticType> diagnostics = result.getDiagnostics();
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "The body of a goog.scope function cannot use 'return'",
        "Removed unused goog.scope block"
    );
    assertEquals(expectedMessages, messages);
    assertEquals(2, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithInvalidThrow() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  throw new Error('Test');"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    List<DiagnosticType> diagnostics = result.getDiagnostics();
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "The body of a goog.scope function cannot use 'throw'"
    );
    assertEquals(expectedMessages, messages);
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithAliasRedefinition() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  var x = 1;"
        + "  var x = 2;"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    List<DiagnosticType> diagnostics = result.getDiagnostics();
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "The alias x is assigned a value more than once"
    );
    assertEquals(expectedMessages, messages);
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  var x = 1;"
        + "  goog.dom.createElement(goog.dom.TagName.DIV);"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    List<DiagnosticType> diagnostics = result.getDiagnostics();
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "The local variable x is in a goog.scope and is not an alias"
    );
    assertEquals(expectedMessages, messages);
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithAliasDefinitionOrder() {
    String jsSource = ""
        + "goog.scope(function() {"
        + "  var x = 1;"
        + "  var y = 2;"
        + "  var x = 3;"
        + "});";

    AbstractCompiler compiler = newCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    CompileResult result = compile(compiler, jsSource);

    List<DiagnosticType> diagnostics = result.getDiagnostics();
    List<String> messages = getAllMessages(result.toSource());
    List<String> expectedMessages = Lists.newArrayList(
        "The alias x is assigned a value more than once"
    );
    assertEquals(expectedMessages, messages);
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0).getType());

    Collection<Node> aliasDefinitions = result.getTree().getTraversal().getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitions.size());
    assertEquals("x", aliasDefinitions.get(0).getString());
    assertEquals("x", aliasDefinitions.get(1).getString());
    assertEquals("y", aliasDefinitions.get(2).getString());
  }
}