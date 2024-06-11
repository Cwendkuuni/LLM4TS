package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.javascript.rhino.NodeProperty.name;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable symbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = new Compiler();
    symbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, symbolTable, transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    scopedAliases.process(null, root);

    Node expectedOutput = ParserUtil.parseAndTraverse(
        "goog.dom.createElement(goog.dom.TagName.DIV);", compiler).getFirstChild();

    Node actualOutput = compiler.getRootNode().getFirstChild();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAliasWithSameName() {
    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(1, diagnostics.size());
      Diagnostic diagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType().getName());
      assertEquals("dom", diagnostic.getMessage(0));
    }
  }

  @Test
  public void testAliasWithFunctionCall() {
    String input = "goog.scope(function() {" +
        "function test() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "}" +
        "test();" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USED_IMPROPERLY error.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(1, diagnostics.size());
      Diagnostic diagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType().getName());
    }
  }

  @Test
  public void testAliasWithFunctionDeclaration() {
    String input = "goog.scope(function() {" +
        "function test() {" +
        "}" +
        "test();" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    scopedAliases.process(null, root);

    Node expectedOutput = ParserUtil.parseAndTraverse(
        "goog.dom.createElement(goog.dom.TagName.DIV);", compiler).getFirstChild();

    Node actualOutput = compiler.getRootNode().getFirstChild();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAliasWithReturnStatement() {
    String input = "goog.scope(function() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USES_RETURN error.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(1, diagnostics.size());
      Diagnostic diagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getType().getName());
    }
  }

  @Test
  public void testAliasWithThrowStatement() {
    String input = "goog.scope(function() {" +
        "throw new Error(\"Test\")";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USES_THROW error.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(1, diagnostics.size());
      Diagnostic diagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getType().getName());
    }
  }

  @Test
  public void testAliasWithFunctionExpression() {
    String input = "goog.scope(function() {" +
        "var test = function() {" +
        "}" +
        "test();" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USED_IMPROPERLY error.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(1, diagnostics.size());
      Diagnostic diagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType().getName());
    }
  }

  @Test
  public void testAliasWithMultipleAliases() {
    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = goog.dom.TagName.DIV;" +
        "var x = goog.x;" +
        "dom.createElement(DIV);" +
        "x = 5;" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected multiple GOOG_SCOPE_ALIAS_REDEFINED errors.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(2, diagnostics.size());
      Diagnostic domDiagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", domDiagnostic.getType().getName());
      assertEquals("dom", domDiagnostic.getMessage(0));
      Diagnostic xDiagnostic = diagnostics.get(1);
      assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", xDiagnostic.getType().getName());
      assertEquals("x", xDiagnostic.getMessage(0));
    }
  }

  @Test
  public void testAliasWithUndefinedVariable() {
    String input = "goog.scope(function() {" +
        "undefined.x = goog.x;" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = ParserUtil.parseAndTraverse(input, compiler);

    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_NON_ALIAS_LOCAL error.");
    } catch (CompilationErrorException e) {
      List<Diagnostic> diagnostics = e.getDiagnostics();
      assertEquals(1, diagnostics.size());
      Diagnostic diagnostic = diagnostics.get(0);
      assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostic.getType().getName());
      assertEquals("x", diagnostic.getMessage(0));
    }
  }
}