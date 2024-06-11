import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String input =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    compiler.process(null, sourceFile.getTree());
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);",
        scopedAliases.getRewrittenCode().getCode());
  }

  @Test
  public void testScopedAliasesWithBadUsage() {
    String input =
        "goog.scope(function() {" +
            "function foo() {" +
                "goog.scope(function() {" +
                    "this.x = 42;" +
                "});" +
                "return this.x;" +
            "}}());";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      compiler.process(null, sourceFile.getTree());
      fail("Expected GOOG_SCOPE_REFERENCES_THIS error.");
    } catch (CompilationError error) {
      assertEquals(
          "JSC_GOOG_SCOPE_REFERENCES_THIS: The body of a goog.scope function cannot reference 'this'.",
          error.getMessage());
    }
  }

  @Test
  public void testScopedAliasesWithBadUsage2() {
    String input =
        "goog.scope(function() {" +
            "function foo() {" +
                "return goog.scope(function() {" +
                    "throw new Error('Something went wrong.');" +
                "});" +
            "}}());";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      compiler.process(null, sourceFile.getTree());
      fail("Expected GOOG_SCOPE_USES_THROW error.");
    } catch (CompilationError error) {
      assertEquals(
          "JSC_GOOG_SCOPE_USES_THROW: The body of a goog.scope function cannot use 'throw'.",
          error.getMessage());
    }
  }

  @Test
  public void testScopedAliasesWithAliasesRedefined() {
    String input =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
                "var DIV = 42;" +
            "});" +
            "dom.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      compiler.process(null, sourceFile.getTree());
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error.");
    } catch (CompilationError error) {
      assertEquals(
          "JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias DIV is assigned a value more than once.",
          error.getMessage());
    }
  }

  @Test
  public void testScopedAliasesWithEmptyScope() {
    String input =
        "goog.scope(function() {});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    compiler.process(null, sourceFile.getTree());
    assertTrue("Expected empty result.", scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    String input =
        "goog.scope(function() {" +
            "goog.scope(function() {});" + // valid call
            "goog.scope(function() { goog.scope(function() {}); });" + // invalid call
        "});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    compiler.process(null, sourceFile.getTree());
    assertTrue("Expected GOOG_SCOPE_USED_IMPROPERLY error.", scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliasesWithMissingParameter() {
    String input =
        "goog.scope(" + // missing function parameter
        "function() {});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    try {
      compiler.process(null, sourceFile.getTree());
      fail("Expected GOOG_SCOPE_HAS_BAD_PARAMETERS error.");
    } catch (CompilationError error) {
      assertEquals(
          "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter.  It must be an anonymous function that itself takes no parameters.",
          error.getMessage());
    }
  }

  @Test
  public void testScopedAliasesWithFunctionCallInsideScope() {
    String input =
        "goog.scope(function() {" +
            "function foo() {" +
                "goog.scope(function() {" +
                    "return 42;" +
                "});" +
                "return foo();" +
            "}});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    compiler.process(null, sourceFile.getTree());
    assertEquals("42", scopedAliases.getRewrittenCode().getCode());
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    String input =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
                "var DIV = 'div';" +
                "goog.scope(function() {" +
                    "var DIV = document.createElement(DIV);" +
                "});" +
                "goog.scope(function() {" +
                    "var DIV = 'div2';" +
                "});" +
                "dom.createElement(DIV);" +
            "});" +
            "dom.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    compiler.process(null, sourceFile.getTree());
    assertEquals(
        "goog.dom.createElement(document.createElement('div'));goog.dom.createElement('div2');",
        scopedAliases.getRewrittenCode().getCode());
  }

  @Test
  public void testScopedAliasesWithNestedScopes() {
    String input =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
                "function foo() {" +
                    "goog.scope(function() {" +
                        "var x = 1;" +
                    "});" +
                    "return x;" +
                "}}());" +
                "return foo();" +
            "});" +
            "goog.scope(function() {" +
                "function foo() {" +
                    "goog.scope(function() {" +
                        "var x = 2;" +
                    "});" +
                    "return x;" +
                "}}());" +
                "return foo();" +
            "});";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    SourceFile sourceFile = new SourceFile("test.js", input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    compiler.process(null, sourceFile.getTree());
    assertEquals("1", scopedAliases.getRewrittenCode().getChild(0).getCode());
    assertEquals("2", scopedAliases.getRewrittenCode().getChild(1).getCode());
  }

  private static class Compiler extends AbstractCompiler {

  }
}