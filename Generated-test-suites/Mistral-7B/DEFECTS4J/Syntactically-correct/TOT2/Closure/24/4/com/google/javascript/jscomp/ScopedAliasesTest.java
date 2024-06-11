import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Test case for valid usage of ScopedAliases
    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});

    Node root = Parser.parse(sourceCode, null, null, null, null, null).program;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // Check that the alias has been correctly applied
    Node element = root.getChildAtIndex(1);
    assertNotNull(element);
    assertInstanceOf(Node.Name.class, element);
    assertEquals("goog.dom.createElement", element.getString());

    // Test case for improper usage of goog.scope
    String improperUsageSourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "});";

    root = Parser.parse(improperUsageSourceCode, null, null, null, null, null).program;
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // Check that an error has been reported
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertFalse(diagnostics.isEmpty());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0).getType());

    // Test case for goog.scope with bad parameters
    String badParametersSourceCode =
        "goog.scope(function() {" +
            "goog.scope({foo: function() {}});" +
            "});";

    root = Parser.parse(badParametersSourceCode, null, null, null, null, null).program;
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // Check that an error has been reported
    diagnostics = compiler.getDiagnostics();
    assertFalse(diagnostics.isEmpty());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithThis() {
    // Test case for goog.scope that references 'this'
    String sourceCode =
        "goog.scope(function() {" +
            "this.dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.dom.createElement(DIV);" +
            "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});

    Node root = Parser.parse(sourceCode, null, null, null, null, null).program;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      scopedAliases.process(null, root);
      fail("Expected an error for 'this' usage in goog.scope");
    } catch (AssertionError e) {
      // Expected
    }

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithReturn() {
    // Test case for goog.scope that uses 'return'
    String sourceCode =
        "goog.scope(function() {" +
            "return goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});

    Node root = Parser.parse(sourceCode, null, null, null, null, null).program;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      scopedAliases.process(null, root);
      fail("Expected an error for 'return' usage in goog.scope");
    } catch (AssertionError e) {
      // Expected
    }

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithThrow() {
    // Test case for goog.scope that uses 'throw'
    String sourceCode =
        "goog.scope(function() {" +
            "throw new Error('Test error');" +
            "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});

    Node root = Parser.parse(sourceCode, null, null, null, null, null).program;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      scopedAliases.process(null, root);
      fail("Expected an error for 'throw' usage in goog.scope");
    } catch (AssertionError e) {
      // Expected
    }

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0).getType());
  }

  @Test
  public void testScopedAliasesWithAliasRedefinition() {
    // Test case for goog.scope that redefines an alias
    String sourceCode =
        "goog.scope(function() {" +
            "var DIV = goog.dom.TagName.DIV;" +
            "var DIV = 'DIV';" +
            "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});

    Node root = Parser.parse(sourceCode, null, null, null, null, null).program;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      scopedAliases.process(null, root);
      fail("Expected an error for alias redefinition in goog.scope");
    } catch (AssertionError e) {
      // Expected
    }

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0).getType());
  }
}