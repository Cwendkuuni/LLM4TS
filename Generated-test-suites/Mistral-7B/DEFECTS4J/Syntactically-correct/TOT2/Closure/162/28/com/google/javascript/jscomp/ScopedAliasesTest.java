import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setup() {
    compiler = new TestCompiler();
    preprocessorSymbolTable = new TestPreprocessorSymbolTable();
    transformationHandler = new TestAliasTransformationHandler();
  }

  @Test
  public void testSimpleAlias() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        transformationHandler.getTransformedCode());
  }

  @Test
  public void testMultipleAliases() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        transformationHandler.getTransformedCode());
  }

  @Test
  public void testInvalidAliasCall() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    String errorMessage = "The call to goog.scope must be alone in a single statement.";
    assertTrue(compiler.getDiagnostics().containsError(errorMessage));
  }

  @Test
  public void testAliasRedefinition() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var dom = goog.dom;" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    String errorMessage = "The alias dom is assigned a value more than once.";
    assertTrue(compiler.getDiagnostics().containsError(errorMessage));
  }

  @Test
  public void testAliasUsedAsFunction() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "function createElement() {" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "}" +
        "createElement();" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    String errorMessage = "The body of a goog.scope function cannot use 'return'.";
    assertTrue(compiler.getDiagnostics().containsError(errorMessage));
  }

  @Test
  public void testAliasUsedAsVariable() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var domVariable = dom;" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    String errorMessage = "The local variable domVariable is in a goog.scope and is not an alias.";
    assertTrue(compiler.getDiagnostics().containsError(errorMessage));
  }

  @Test
  public void testAliasWithFunctionParameter() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function(foo) {" +
        "var bar = goog.dom;" +
        "bar.createElement(goog.dom.TagName.DIV, foo);" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    assertEquals("function(foo){goog.dom.createElement(goog.dom.TagName.DIV, foo)}",
        transformationHandler.getTransformedCode());
  }

  @Test
  public void testAliasWithFunctionReturnValue() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "return goog.dom;" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    String errorMessage = "The body of a goog.scope function cannot use 'return'.";
    assertTrue(compiler.getDiagnostics().containsError(errorMessage));
  }

  @Test
  public void testAliasWithThrowStatement() {
    AliasTransformation aliasTransformation = new AliasTransformation();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String input = "goog.scope(function() {" +
        "throw new Error('Test error');" +
        "});";

    Node root = new Parser().parse(input, null, null);
    scopedAliases.process(null, root);

    String errorMessage = "The body of a goog.scope function cannot use 'throw'.";
    assertTrue(compiler.getDiagnostics().containsError(errorMessage));
  }

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public void report(Diagnostic diagnostic) {
      // Ignore errors for testing
    }
  }

  private static class TestPreprocessorSymbolTable implements PreprocessorSymbolTable {
    @Override
    public void addReference(Node node) {
      // Ignore for testing
    }

    @Override
    public Var getVar(String name) {
      return new Var("test", null);
    }
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    private String transformedCode;

    public void logAliasTransformation(String fileName, SourcePosition pos) {
      // Ignore for testing
    }

    public void addAlias(String name, String qualifiedName) {
      transformedCode += name + ":\"" + qualifiedName + "\";";
    }

    public String getTransformedCode() {
      return transformedCode;
    }
  }
}