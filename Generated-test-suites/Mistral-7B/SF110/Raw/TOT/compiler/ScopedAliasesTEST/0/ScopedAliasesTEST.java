import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.aliasing.AliasTransformation;
import com.google.javascript.jscomp.aliasing.AliasTransformationHandler;
import com.google.javascript.jscomp.diagnostics.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Test;
import org.junit.Ignore;

/**
 * Test cases for the ScopedAliases class.
 */
public class ScopedAliasesTest {
  @Test
  public void testSimpleScopedAlias() {
    // Given: A simple goog.scope call with a single alias.
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: The alias should have been replaced with the actual value.
    Traversal traversal = pass.getTraversal();
    assertNotNull(traversal.getAliasDefinitionsInOrder().get(0));
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    Node aliasReference = aliasDefinition.getFirstChild();
    assertEquals("DOM", aliasDefinition.getString());
    assertEquals("dom", aliasReference.getString());
    assertEquals(Node.NOP, aliasReference.getType());

    // And: The goog.scope call should have been removed.
    Node googScopeCall = null;
    for (Node node : root.getChildren()) {
      if (node.isCall() && "goog.scope".equals(node.getFirstChild().getString())) {
        googScopeCall = node;
        break;
      }
    }
    assertNull(googScopeCall);
  }

  @Test
  public void testComplexScopedAlias() {
    // Given: A goog.scope call with multiple aliases.
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "var link = goog.link;" +
        "var RE = /foo/;" +
        "dom.createElement(DIV);" +
        "link(dom.createAnchor('a.html'), RE);" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: All aliases should have been replaced with the actual values.
    Traversal traversal = pass.getTraversal();
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();

    assertEquals(3, aliasDefinitions.size());
    Node domAliasDefinition = aliasDefinitions.get(0);
    Node linkAliasDefinition = aliasDefinitions.get(1);
    Node reAliasDefinition = aliasDefinitions.get(2);

    assertEquals("DOM", domAliasDefinition.getString());
    assertEquals("dom", domAliasDefinition.getLastChild().getString());
    assertEquals("link", linkAliasDefinition.getString());
    assertEquals("goog.link", linkAliasDefinition.getLastChild().getString());
    assertEquals("RE", reAliasDefinition.getString());
    assertEquals("/foo/", reAliasDefinition.getLastChild().getString());

    // And: The goog.scope call should have been removed.
    Node googScopeCall = null;
    for (Node node : root.getChildren()) {
      if (node.isCall() && "goog.scope".equals(node.getFirstChild().getString())) {
        googScopeCall = node;
        break;
      }
    }
    assertNull(googScopeCall);
  }

  @Test
  public void testBadScopeCall() {
    // Given: A goog.scope call with bad parameters.
    String code = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "});" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: There should be an error for the bad goog.scope call.
    Traversal traversal = pass.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType error = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getName());
  }

  @Test
  public void testBadScopeBody() {
    // Given: A goog.scope call with a body that references 'this'.
    String code = "goog.scope(function() {" +
        "this.x = 1;" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: There should be an error for the 'this' reference.
    Traversal traversal = pass.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType error = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", error.getName());
  }

  @Test
  public void testBadScopeReturn() {
    // Given: A goog.scope call with a body that uses 'return'.
    String code = "goog.scope(function() {" +
        "return 1;" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: There should be an error for the 'return' statement.
    Traversal traversal = pass.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType error = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getName());
  }

  @Test
  public void testBadScopeThrow() {
    // Given: A goog.scope call with a body that uses 'throw'.
    String code = "goog.scope(function() {" +
        "throw new Error('Test error');" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: There should be an error for the 'throw' statement.
    Traversal traversal = pass.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType error = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getName());
  }

  @Test
  public void testBadScopeAliasRedefinition() {
    // Given: A goog.scope call with an alias redefinition.
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "var DIV = 42;" +
        "dom.createElement(DIV);" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: There should be an error for the alias redefinition.
    Traversal traversal = pass.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType error = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testBadScopeAliasNonLocal() {
    // Given: A goog.scope call with a non-local alias.
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var x = 42;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    // When: Compiling the code with the ScopedAliases pass.
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = null;
    Node root = CodeCoverageTestUtil.parseJavaScript(code, "ScopedAliasesTest.java");
    pass.process(externs, root);

    // Then: There should be an error for the non-local alias.
    Traversal traversal = pass.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType error = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getName());
  }

  @Test
  @Ignore("TODO: Implement a real HotSwapCompiler")
  public void testHotSwap() {
    // TODO: Implement a real HotSwapCompiler and test hot swapping.
  }

  private void assertEquals(String expected, Object actual) {
    assertTrue(expected.equals(actual));
  }

  private void assertEquals(int expected, int actual) {
    assertEquals(Integer.toString(expected), Integer.toString(actual));
  }
}