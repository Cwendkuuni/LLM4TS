package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.SCOPED);
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    String expectedJsCode = "goog.dom.createElement(goog.dom.TagName.DIV);";
    String actualJsCode = Parser.serialize(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  @Test
  public void testBadScopedCall() {
    String jsCode = "goog.scope(function() {\n" +
        "  function test() {\n" +
        "    return goog.scope(function() {\n" +
        "      this.x = 1;\n" +
        "    });\n" +
        "  };\n" +
        "  test();\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String error = errors.get(0);
    String expectedError = "JSC_GOOG_SCOPE_REFERENCES_THIS";
    assertEquals(expectedError, error.getTypeName());
  }

  @Test
  public void testScopeCallWithParameter() {
    String jsCode = "goog.scope(function(a) {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV, a);\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    String expectedJsCode = "goog.dom.createElement(goog.dom.TagName.DIV, arguments[0]);";
    String actualJsCode = Parser.serialize(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  @Test
  public void testScopeCallWithMultipleStatements() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "  dom.createTextNode(\"Hello World\");\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    String expectedJsCode = "goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "goog.dom.createTextNode(\"Hello World\");";
    String actualJsCode = Parser.serialize(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  @Test
  public void testScopeCallWithInvalidParameter() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV, 123);\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String error = errors.get(0);
    String expectedError = "JSC_TYPE_ERROR"; // The error message for this test is not specific to the scoped alias feature.
    assertTrue(error.contains(expectedError));
  }

  @Test
  public void testScopeCallWithReturnStatement() {
    String jsCode = "goog.scope(function() {\n" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String error = errors.get(0);
    String expectedError = "JSC_GOOG_SCOPE_USES_RETURN";
    assertEquals(expectedError, error.getTypeName());
  }

  @Test
  public void testScopeCallWithThrowStatement() {
    String jsCode = "goog.scope(function() {\n" +
        "  if (true) throw new Error('Test Error');\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String error = errors.get(0);
    String expectedError = "JSC_GOOG_SCOPE_USES_THROW";
    assertEquals(expectedError, error.getTypeName());
  }

  @Test
  public void testScopedAliasRedefinition() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var DIV = goog.dom.createElement(DIV);\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String error = errors.get(0);
    String expectedError = "JSC_GOOG_SCOPE_ALIAS_REDEFINED";
    assertEquals(expectedError, error.getTypeName());
  }

  @Test
  public void testScopedAliasNonLocalVariable() {
    String jsCode = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "  goog.scope(function() {\n" +
        "    var y = 2;\n" +
        "  });\n" +
        "  goog.scope(function() {\n" +
        "    var x = 3;\n" +
        "  });\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String error = errors.get(0);
    String expectedError = "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL";
    assertEquals(expectedError, error.getTypeName());
  }

  @Test
  public void testScopedAliasWithJSDocInfo() {
    String jsCode = "/**\n" +
        "* An alias for goog.dom\n" +
        "*/\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom.createElement(DIV);\n" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = Parser.parse(jsCode);
    scopedAliases.process(null, root);

    String expectedJsCode = "/**\n" +
        "* An alias for goog.dom\n" +
        "*/\n" +
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    String actualJsCode = Parser.serialize(root);
    assertEquals(expectedJsCode, actualJsCode);
  }
}