package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.test.TestUtils.compileTypeCheck;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.test.testType.TestType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    String expectedOutput =
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    assertThat(compiler.getEmittedCode()).isEqualTo(expectedOutput);
  }

  @Test
  public void testScopedAliasesNestedFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        "goog.scope(function() {" +
            "function outerFunction() {" +
            "  goog.scope(function() {" +
            "    var DIV = goog.dom.TagName.DIV;" +
            "    goog.dom.createElement(DIV);" +
            "  });" +
            "  goog.dom.createElement(goog.dom.TagName.DIV);" +
            "}" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    String expectedOutput =
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    assertThat(compiler.getEmittedCode()).isEqualTo(expectedOutput);
  }

  @Test
  public void testScopedAliasesBadUsage() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    String sourceCode =
        "goog.scope(function() {" +
            "function outerFunction() {" +
            "  goog.scope(function() {" +
            "    goog.scope(function() {" +
            "      goog.scope(function() {" +
            "      });" +
            "    });" +
            "  });" +
            "}});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    // The test should fail with GOOG_SCOPE_USED_IMPROPERLY error.
    String errorMessage = "The call to goog.scope must be alone in a single statement.";
    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(errorMessage);
  }

  @Test
  public void testScopedAliasesWithPreprocessorSymbolTable() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    options.setAliasTransformation(
        CompilerOptions.AliasTransformation.PREPROCESSOR);

    String sourceCode =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    String expectedOutput =
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    assertThat(compiler.getEmittedCode()).isEqualTo(expectedOutput);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesRedefinition() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  var DIV = 'div';" +
            "});" +
            "goog.dom.createElement(DIV);" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    // The test should fail with GOOG_SCOPE_ALIAS_REDEFINED error.
    String errorMessage = "The alias DIV is assigned a value more than once.";
    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(errorMessage);
  }

  @Test
  public void testScopedAliasesNonAliasLocal() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "var local = {};" +
            "local.DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(local.DIV);" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    // The test should fail with GOOG_SCOPE_NON_ALIAS_LOCAL error.
    String errorMessage = "The local variable DIV is in a goog.scope and is not an alias.";
    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(errorMessage);
  }

  @Test
  public void testScopedAliasesAliasUsedOutsideScope() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  goog.dom.createElement(DIV);" +
            "})();" +
            "goog.dom.createElement(DIV);" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    // The test should fail with GOOG_SCOPE_ALIAS_REDEFINED error.
    String errorMessage = "The alias DIV is assigned a value more than once.";
    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(errorMessage);
  }

  @Test
  public void testScopedAliasesAliasUsedInsideBetterScope() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  goog.scope(function() {" +
            "    var DIV = goog.dom.TagName.DIV;" +
            "    goog.dom.createElement(DIV);" +
            "  });" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    // The test should fail with GOOG_SCOPE_ALIAS_REDEFINED error.
    String errorMessage = "The alias DIV is assigned a value more than once.";
    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(errorMessage);
  }

  @Test
  public void testScopedAliasesAliasUsedOutsideScopeWithImmutableMap() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  var map = ImmutableMap.of(" +
            "      'DIV', goog.dom.TagName.DIV);" +
            "  map.get('DIV');" +
            "})();" +
            "goog.dom.createElement(DIV);" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    // The test should fail with GOOG_SCOPE_ALIAS_REDEFINED error.
    String errorMessage = "The alias DIV is assigned a value more than once.";
    assertThat(compiler.getErrors().get(0).getMessage()).isEqualTo(errorMessage);
  }

  @Test
  public void testScopedAliasesAliasUsedInsideBetterScopeWithImmutableMap() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  goog.scope(function() {" +
            "    var map = ImmutableMap.of(" +
            "        'DIV', goog.dom.TagName.DIV);" +
            "    map.get('DIV');" +
            "  });" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "});";

    compileTypeCheck(
        compiler, sourceCode, options, scopedAliases, TestType.TYPE_CHECK);

    String expectedOutput =
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    assertThat(compiler.getEmittedCode()).isEqualTo(expectedOutput);
  }
}