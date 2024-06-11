import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    options = mock(CompilerOptions.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);

    when(compiler.getCompilerOptions()).thenReturn(options);
    when(compiler.getPreprocessorSymbolTable()).thenReturn(preprocessorSymbolTable);
    when(options.getAliasTransformationHandler()).thenReturn(transformationHandler);

    when(compiler.report(ArgumentMatchers.any(Node.class),
        ArgumentMatchers.eq(DiagnosticType.class), ArgumentMatchers.anyString()))
        .thenReturn(null);
  }

  @Test
  public void testSimpleAlias() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = goog.x; });");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();
    ArgumentCaptor<AliasTransformation> aliasCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    when(transformationHandler.logAliasTransformation(ArgumentMatchers.anyString(), ArgumentMatchers.eq(position)))
        .thenReturn(mock(AliasTransformation.class));

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    Node aliasDefinitionNode = traversal.getAliasDefinitionsInOrder().get(0);
    Node aliasUsageNode = traversal.getAliasUsages().get(0);

    assertThat(scopeCallNode.getFirstChild().getQualifiedName())
        .isEqualTo("goog.scope");
    assertThat(scopeCallNode.getChildAtIndex(1).getQualifiedName())
        .isEqualTo("anonymous function");
    assertThat(aliasDefinitionNode.getQualifiedName())
        .isEqualTo("goog.x");
    assertThat(aliasUsageNode.getParent().getParent().getQualifiedName())
        .isEqualTo("goog.scope(function() { ... });");
    assertThat(aliasUsageNode.getParent().getString())
        .isEqualTo("x");
  }

  @Test(expected = AssertionError.class)
  public void testInvalidCallToScopeMethod() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(123);");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    scopedAliases.report(traversal, scopeCallNode, GOOG_SCOPE_USED_IMPROPERLY);
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithParameters() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(function(x) {});");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    scopedAliases.report(traversal, scopeCallNode, GOOG_SCOPE_HAS_BAD_PARAMETERS);
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithReturnStatement() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(function() { return 123; });");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    scopedAliases.report(traversal, scopeCallNode, GOOG_SCOPE_USES_RETURN);
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithThrowStatement() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(function() { throw new Error(); });");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    scopedAliases.report(traversal, scopeCallNode, GOOG_SCOPE_USES_THROW);
  }

  @Test(expected = AssertionError.class)
  public void testAliasRedefined() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = goog.x; var x = 123; });");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();

    Node aliasDefinitionNode1 = traversal.getAliasDefinitionsInOrder().get(0);
    Node aliasDefinitionNode2 = traversal.getAliasDefinitionsInOrder().get(1);

    scopedAliases.report(traversal, aliasDefinitionNode2, GOOG_SCOPE_ALIAS_REDEFINED, "x");
  }

  @Test(expected = AssertionError.class)
  public void testNonAliasLocalVariable() {
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    SourcePosition position = new SourcePosition<AliasTransformation>() {};
    Node rootNode = Node.newTextNode("goog.scope(function() { var x = 123; });");
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getLastTraversal();

    Node localVariableNode = traversal.getScope().getVar("x");
    scopedAliases.report(traversal, localVariableNode.getNode(), GOOG_SCOPE_NON_ALIAS_LOCAL);
  }
}