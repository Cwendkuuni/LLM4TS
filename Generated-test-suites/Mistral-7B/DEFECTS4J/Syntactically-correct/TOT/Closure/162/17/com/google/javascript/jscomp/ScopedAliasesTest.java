import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private static final String GOOG_SCOPE_USAGE =
      "goog.scope(function() {\n" +
      "  var dom = goog.dom;\n" +
      "  var DIV = dom.TagName.DIV;\n" +
      "  dom.createElement(DIV);\n" +
      "});";

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.getPreprocessorSymbolTable()).thenReturn(preprocessorSymbolTable);
    when(compiler.report(mock(NodeTraversal.class), mock(Node.class),
        mock(DiagnosticType.class), (Object[]) new Object[0]))
        .thenReturn(null);
  }

  @Test
  public void testScopedAliases() {
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(GOOG_SCOPE_USAGE);
    pass.process(null, root);
    assertNull(root.getChildAtIndex(0)); // goog.scope should be gone
    assertEquals("goog.dom.createElement", root.getChildAtIndex(1).getQualifiedName());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithBadUsage() {
    String badUsage = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "});\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "});";
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(badUsage);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithBadParameter() {
    String badParameter = "goog.scope(function(param) {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(badParameter);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithThisReference() {
    String thisReference = "goog.scope(function() {\n" +
        "  this.dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(thisReference);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithReturnStatement() {
    String returnStatement = "goog.scope(function() {\n" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "});";
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(returnStatement);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithThrowStatement() {
    String throwStatement = "goog.scope(function() {\n" +
        "  throw new Error('Something went wrong');\n" +
        "});";
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(throwStatement);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithAliasRedefined() {
    String aliasRedefined = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var DIV = 'something else';\n" +
        "});";
    HotSwapCompilerPass pass =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.parse(aliasRedefined);
    pass.process(null, root);
  }
}