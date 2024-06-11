import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() throws Exception {
    // Test scenario: basic usage of ScopedAliases
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOGLE);

    Node rootNode = Node.fromText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; dom.createElement(DIV); });");

    Node externsNode = Node.objectLiteral();
    scopedAliases.process(externsNode, rootNode);

    Node processedNode = rootNode.getFirstChild();
    Node expectedProcessedNode = Node.fromText("goog.dom.createElement(goog.dom.TagName.DIV);");
    assertEquals(expectedProcessedNode.toString(), processedNode.toString());

    // Test scenario: improper usage of goog.scope
    Node improperUsageNode = Node.fromText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; dom.createElement(DIV); goog.scope(function() {}); });");

    scopedAliases.process(externsNode, improperUsageNode);

    DiagnosticType improperUsageError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        "The call to goog.scope must be alone in a single statement.");

    assertErrors(compiler, improperUsageNode, improperUsageError);

    // Test scenario: goog.scope with bad parameters
    Node badParametersNode = Node.fromText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; goog.scope(function(param) {}, param); dom.createElement(DIV); });");

    scopedAliases.process(externsNode, badParametersNode);

    DiagnosticType badParametersError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        "The call to goog.scope must take only a single parameter.  It must" +
            " be an anonymous function that itself takes no parameters.");

    assertErrors(compiler, badParametersNode, badParametersError);

    // Test scenario: goog.scope references 'this'
    Node referencesThisNode = Node.fromText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; this.createElement(DIV); });");

    scopedAliases.process(externsNode, referencesThisNode);

    DiagnosticType referencesThisError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_REFERENCES_THIS",
        "The body of a goog.scope function cannot reference 'this'.");

    assertErrors(compiler, referencesThisNode, referencesThisError);

    // Test scenario: goog.scope uses 'return'
    Node usesReturnNode = Node.fromText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; return dom.createElement(DIV); });");

    scopedAliases.process(externsNode, usesReturnNode);

    DiagnosticType usesReturnError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_USES_RETURN",
        "The body of a goog.scope function cannot use 'return'.");

    assertErrors(compiler, usesReturnNode, usesReturnError);

    // Test scenario: goog.scope uses 'throw'
    Node usesThrowNode = Node.fromText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; throw new Error('Test'); });");

    scopedAliases.process(externsNode, usesThrowNode);

    DiagnosticType usesThrowError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_USES_THROW",
        "The body of a goog.scope function cannot use 'throw'.");

    assertErrors(compiler, usesThrowNode, usesThrowError);

    // Test scenario: alias redefined
    Node redefinedAliasNode = Node.fromText("goog.scope(function() { " +
        "var name = 'foo'; goog.scope(function() { var name = 'bar'; }); " +
        "goog.scope(function() { var name = 'baz'; }); " +
        "return name; });");

    scopedAliases.process(externsNode, redefinedAliasNode);

    DiagnosticType redefinedAliasError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        "The alias {0} is assigned a value more than once.", "name");

    assertErrors(compiler, redefinedAliasNode, redefinedAliasError);

    // Test scenario: non-alias local in goog.scope
    Node nonAliasLocalInScopeNode = Node.fromText("goog.scope(function() { " +
        "var nonAliasLocal = 'nonAliasLocal'; return nonAliasLocal; });");

    scopedAliases.process(externsNode, nonAliasLocalInScopeNode);

    DiagnosticType nonAliasLocalInScopeError = DiagnosticType.error(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        "The local variable {0} is in a goog.scope and is not an alias.", "nonAliasLocal");

    assertErrors(compiler, nonAliasLocalInScopeNode, nonAliasLocalInScopeError);
  }

  private void assertErrors(AbstractCompiler compiler, Node node, DiagnosticType error) {
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(error, compiler.getDiagnostics().get(0).getType());
  }
}