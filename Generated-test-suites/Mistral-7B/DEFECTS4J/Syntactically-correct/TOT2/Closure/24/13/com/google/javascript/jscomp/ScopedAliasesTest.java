package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Test class for ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});";

    Node rootNode = ParserUtil.parse(sourceCode);

    scopedAliases.process(null, rootNode);

    List<String> expectedAliases = Lists.newArrayList("goog.dom", "DIV");

    assertEquals(expectedAliases.size(), scopedAliases.getAliasDefinitionsInOrder().size());
    for (Node node : scopedAliases.getAliasDefinitionsInOrder()) {
      assertEquals("goog.dom", node.getFirstChild().getString());
      assertEquals(NodeUtil.getFunctionName(node.getLastChild()), "DIV");
    }

    // Test error scenarios
    String errorSourceCode =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "});" +
            "});";

    Node errorRootNode = ParserUtil.parse(errorSourceCode);

    scopedAliases.process(null, errorRootNode);

    List<DiagnosticType> expectedErrors = Lists.newArrayList(
        GOOG_SCOPE_USED_IMPROPERLY,
        GOOG_SCOPE_HAS_BAD_PARAMETERS
    );

    assertEquals(expectedErrors.size(), scopedAliases.getDiagnostics().size());
    for (DiagnosticType error : scopedAliases.getDiagnostics()) {
      assertEquals(expectedErrors.get(expectedErrors.size() - 1).getType(),
          error.getType());
    }

    String invalidScopeSourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "this.createElement(DIV);" +
            "});";

    Node invalidScopeRootNode = ParserUtil.parse(invalidScopeSourceCode);

    scopedAliases.process(null, invalidScopeRootNode);

    List<DiagnosticType> invalidScopeErrors = Lists.newArrayList(
        GOOG_SCOPE_REFERENCES_THIS
    );

    assertEquals(invalidScopeErrors.size(), scopedAliases.getDiagnostics().size());
    for (DiagnosticType error : scopedAliases.getDiagnostics()) {
      assertEquals(invalidScopeErrors.get(0).getType(), error.getType());
    }

    String aliasRedefinedSourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "var DIV = 'DIV2';" +
            "});" +
            "dom.createElement(DIV);" +
            "});";

    Node aliasRedefinedRootNode = ParserUtil.parse(aliasRedefinedSourceCode);

    scopedAliases.process(null, aliasRedefinedRootNode);

    List<DiagnosticType> aliasRedefinedErrors = Lists.newArrayList(
        GOOG_SCOPE_ALIAS_REDEFINED
    );

    assertEquals(aliasRedefinedErrors.size(), scopedAliases.getDiagnostics().size());
    for (DiagnosticType error : scopedAliases.getDiagnostics()) {
      assertEquals(aliasRedefinedErrors.get(0).getType(), error.getType());
    }

    String nonAliasLocalSourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var local = 1;" +
            "});";

    Node nonAliasLocalRootNode = ParserUtil.parse(nonAliasLocalSourceCode);

    scopedAliases.process(null, nonAliasLocalRootNode);

    List<DiagnosticType> nonAliasLocalErrors = Lists.newArrayList(
        GOOG_SCOPE_NON_ALIAS_LOCAL
    );

    assertEquals(nonAliasLocalErrors.size(), scopedAliases.getDiagnostics().size());
    for (DiagnosticType error : scopedAliases.getDiagnostics()) {
      assertEquals(nonAliasLocalErrors.get(0).getType(), error.getType());
    }

    // Test multiple scopes
    String multipleScopesSourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "goog.scope(function() {" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});" +
            "});";

    Node multipleScopesRootNode = ParserUtil.parse(multipleScopesSourceCode);

    scopedAliases.process(null, multipleScopesRootNode);

    assertEquals(expectedAliases.size(), scopedAliases.getAliasDefinitionsInOrder().size());
    for (Node node : scopedAliases.getAliasDefinitionsInOrder()) {
      assertEquals("goog.dom", node.getFirstChild().getString());
      assertEquals(NodeUtil.getFunctionName(node.getLastChild()), "DIV");
    }
  }
}