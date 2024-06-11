import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test case for a valid scoped alias
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases.process(null, root);

    AliasTransformation aliasTransformation = scopedAliases.getTransformation();
    assertEquals(1, aliasTransformation.getAliases().size());
    assertEquals("dom", aliasTransformation.getAliases().get("goog.dom"));

    // Test case for an improperly used goog.scope
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    dom.createElement(DIV);" +
        "  });" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, scopedAliases.getError(0).getDiagnosticType());

    // Test case for a goog.scope with bad parameters
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope({}, function() {" +
        "    dom.createElement(DIV);" +
        "  });" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, scopedAliases.getError(0).getDiagnosticType());

    // Test case for a goog.scope that references 'this'
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  this.createElement(DIV);" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, scopedAliases.getError(0).getDiagnosticType());

    // Test case for a goog.scope that uses 'return'
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  return dom.createElement(DIV);" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_USES_RETURN, scopedAliases.getError(0).getDiagnosticType());

    // Test case for a goog.scope that uses 'throw'
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  throw new Error('Test Error');" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_USES_THROW, scopedAliases.getError(0).getDiagnosticType());

    // Test case for a goog.scope with an alias redefined
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV = com.google.javascript.jscomp.Scope.Var.createTopLevelVar('DIV');" +
        "  dom.createElement(DIV);" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, scopedAliases.getError(0).getDiagnosticType());

    // Test case for a goog.scope with a non-alias local variable
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var test = 'Test';" +
        "  dom.createElement(DIV);" +
        "});";

    root = JSCompiler.parseTree(sourceCode, null, null);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getErrorCount());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, scopedAliases.getError(0).getDiagnosticType());
  }
}