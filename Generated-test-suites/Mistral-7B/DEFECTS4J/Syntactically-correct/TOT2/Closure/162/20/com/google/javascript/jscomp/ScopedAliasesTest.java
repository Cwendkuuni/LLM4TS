import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.List;

@Category(IntegrationTest.class)
public class ScopedAliasesTest {
  private Compiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = new Compiler();
    sourceFile = new SourceFile("Test.js", "// Test file");
  }

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(Collections.singletonList(sourceFile), options);
    Node rootNode = sourceFile.getAST();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    // Test basic functionality
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        rootNode.toString());

    // Test edge cases and exception handling

    // Test goog.scope used improperly
    String invalidScopeCall = "goog.scope(function() {}) goog.scope(function() {})";
    SourceFile invalidSourceFile = new SourceFile("InvalidTest.js", invalidScopeCall);
    Node invalidRootNode = invalidSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY.getCode(), () -> {
      ScopedAliases invalidScopedAliases = new ScopedAliases(compiler, null, null);
      invalidScopedAliases.process(null, invalidRootNode);
    });

    // Test goog.scope has bad parameters
    String invalidScopeCallWithParameters = "goog.scope(function() {}, 123)";
    SourceFile invalidScopeCallWithParametersSourceFile =
        new SourceFile("InvalidTestWithParameters.js", invalidScopeCallWithParameters);
    Node invalidScopeCallWithParametersRootNode =
        invalidScopeCallWithParametersSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS.getCode(), () -> {
      ScopedAliases invalidScopeCallWithParametersScopedAliases =
          new ScopedAliases(compiler, null, null);
      invalidScopeCallWithParametersScopedAliases.process(null,
          invalidScopeCallWithParametersRootNode);
    });

    // Test goog.scope references this
    String invalidScopeCallWithThis = "goog.scope(function() { this.test = 123 })";
    SourceFile invalidScopeCallWithThisSourceFile =
        new SourceFile("InvalidTestWithThis.js", invalidScopeCallWithThis);
    Node invalidScopeCallWithThisRootNode = invalidScopeCallWithThisSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS.getCode(), () -> {
      ScopedAliases invalidScopeCallWithThisScopedAliases =
          new ScopedAliases(compiler, null, null);
      invalidScopeCallWithThisScopedAliases.process(null, invalidScopeCallWithThisRootNode);
    });

    // Test goog.scope uses return
    String invalidScopeCallWithReturn = "goog.scope(function() { return 123 })";
    SourceFile invalidScopeCallWithReturnSourceFile =
        new SourceFile("InvalidTestWithReturn.js", invalidScopeCallWithReturn);
    Node invalidScopeCallWithReturnRootNode = invalidScopeCallWithReturnSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_USES_RETURN.getCode(), () -> {
      ScopedAliases invalidScopeCallWithReturnScopedAliases =
          new ScopedAliases(compiler, null, null);
      invalidScopeCallWithReturnScopedAliases.process(null, invalidScopeCallWithReturnRootNode);
    });

    // Test goog.scope uses throw
    String invalidScopeCallWithThrow = "goog.scope(function() { throw new Error() })";
    SourceFile invalidScopeCallWithThrowSourceFile =
        new SourceFile("InvalidTestWithThrow.js", invalidScopeCallWithThrow);
    Node invalidScopeCallWithThrowRootNode = invalidScopeCallWithThrowSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_USES_THROW.getCode(), () -> {
      ScopedAliases invalidScopeCallWithThrowScopedAliases =
          new ScopedAliases(compiler, null, null);
      invalidScopeCallWithThrowScopedAliases.process(null, invalidScopeCallWithThrowRootNode);
    });

    // Test alias redefined
    String aliasRedefined = "goog.scope(function() { var g = goog; g.test = 123; g.test = 456 })";
    SourceFile aliasRedefinedSourceFile =
        new SourceFile("AliasRedefinedTest.js", aliasRedefined);
    Node aliasRedefinedRootNode = aliasRedefinedSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED.getCode(), () -> {
      ScopedAliases aliasRedefinedScopedAliases = new ScopedAliases(compiler, null, null);
      aliasRedefinedScopedAliases.process(null, aliasRedefinedRootNode);
    });

    // Test non-alias local
    String nonAliasLocal = "goog.scope(function() { var test = 123 })";
    SourceFile nonAliasLocalSourceFile =
        new SourceFile("NonAliasLocalTest.js", nonAliasLocal);
    Node nonAliasLocalRootNode = nonAliasLocalSourceFile.getAST();
    assertThrows(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL.getCode(), () -> {
      ScopedAliases nonAliasLocalScopedAliases = new ScopedAliases(compiler, null, null);
      nonAliasLocalScopedAliases.process(null, nonAliasLocalRootNode);
    });
  }
}