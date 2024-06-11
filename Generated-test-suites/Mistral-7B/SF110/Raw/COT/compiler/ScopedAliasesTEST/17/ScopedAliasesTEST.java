import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.rhino.NodeTag.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

/**
 * Test cases for ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private Traversal traversal;
  private Diagnostics diagnostics;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    compiler = Mockito.mock(AbstractCompiler.class);
    rootNode = Mockito.mock(Node.class);
    diagnostics = Mockito.mock(Diagnostics.class);

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME_GOOG);

    // Set up compiler with mocks
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getDiagnostics()).thenReturn(diagnostics);

    traversal = new Traversal();
  }

  /**
   * Test basic functionality of process method.
   */
  @Test
  public void testProcess() {
    // Given
    Node root = Mockito.mock(Node.class);
    when(root.isScript()).thenReturn(true);

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    // Then
    verify(compiler, times(1)).reportCodeChange();
  }

  /**
   * Test handling of improperly used goog.scope method.
   */
  @Test
  public void testGoogScopeUsedImproperly() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node googScopeCall = Mockito.mock(Node.class);
    Node googScopeFunction = Mockito.mock(Node.class);
    Node invalidCall = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(googScopeCall.getFirstChild()).thenReturn(
        Mockito.mock(Node.class).withReturnValue(
            NodeTag.NAME, "goog.scope"));
    when(googScopeCall.getLastChild()).thenReturn(googScopeFunction);
    when(googScopeFunction.getParent()).thenReturn(root);
    when(googScopeFunction.getLastChild()).thenReturn(invalidCall);

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1)).makeError(googScopeCall, GOOG_SCOPE_USED_IMPROPERLY);
  }

  /**
   * Test handling of goog.scope method with bad parameters.
   */
  @Test
  public void testGoogScopeHasBadParameters() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node googScopeCall = Mockito.mock(Node.class);
    Node googScopeFunction = Mockito.mock(Node.class);
    Node invalidParam = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(googScopeCall.getFirstChild()).thenReturn(
        Mockito.mock(Node.class).withReturnValue(
            NodeTag.NAME, "goog.scope"));
    when(googScopeCall.getLastChild()).thenReturn(googScopeFunction);
    when(googScopeFunction.getParent()).thenReturn(root);
    when(googScopeFunction.getFirstChild()).thenReturn(invalidParam);

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1)).makeError(googScopeCall, GOOG_SCOPE_HAS_BAD_PARAMETERS);
  }

  /**
   * Test handling of goog.scope method that references 'this'.
   */
  @Test
  public void testGoogScopeRefersToThis() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node googScopeCall = Mockito.mock(Node.class);
    Node googScopeFunction = Mockito.mock(Node.class);
    Node thisReference = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(googScopeCall.getFirstChild()).thenReturn(
        Mockito.mock(Node.class).withReturnValue(
            NodeTag.NAME, "goog.scope"));
    when(googScopeCall.getLastChild()).thenReturn(googScopeFunction);
    when(googScopeFunction.getParent()).thenReturn(root);
    when(googScopeFunction.getFirstChild()).thenReturn(thisReference);

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1)).makeError(googScopeFunction, GOOG_SCOPE_REFERENCES_THIS);
  }

  /**
   * Test handling of goog.scope method that uses 'return'.
   */
  @Test
  public void testGoogScopeUsesReturn() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node googScopeCall = Mockito.mock(Node.class);
    Node googScopeFunction = Mockito.mock(Node.class);
    Node returnStatement = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(googScopeCall.getFirstChild()).thenReturn(
        Mockito.mock(Node.class).withReturnValue(
            NodeTag.NAME, "goog.scope"));
    when(googScopeCall.getLastChild()).thenReturn(googScopeFunction);
    when(googScopeFunction.getParent()).thenReturn(root);
    when(googScopeFunction.getLastChild()).thenReturn(returnStatement);

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1)).makeError(googScopeFunction, GOOG_SCOPE_USES_RETURN);
  }

  /**
   * Test handling of goog.scope method that uses 'throw'.
   */
  @Test
  public void testGoogScopeUsesThrow() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node googScopeCall = Mockito.mock(Node.class);
    Node googScopeFunction = Mockito.mock(Node.class);
    Node throwStatement = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(googScopeCall.getFirstChild()).thenReturn(
        Mockito.mock(Node.class).withReturnValue(
            NodeTag.NAME, "goog.scope"));
    when(googScopeCall.getLastChild()).thenReturn(googScopeFunction);
    when(googScopeFunction.getParent()).thenReturn(root);
    when(googScopeFunction.getLastChild()).thenReturn(throwStatement);

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1)).makeError(googScopeFunction, GOOG_SCOPE_USES_THROW);
  }

  /**
   * Test handling of alias redefined.
   */
  @Test
  public void testAliasRedefined() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition1 = Mockito.mock(Node.class);
    Node aliasDefinition2 = Mockito.mock(Node.class);
    Node aliasUsage = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(aliasDefinition1.getFirstChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue(NODE_VAR));
    when(aliasDefinition1.getLastChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue("alias1"));
    when(aliasDefinition2.getFirstChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue(NODE_VAR));
    when(aliasDefinition2.getLastChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue("alias1"));
    when(aliasUsage.getParent()).thenReturn(Mockito.mock(Node.class));
    when(aliasUsage.getFirstChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue(NODE_NAME));
    when(aliasUsage.getLastChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue("alias1"));

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Traversal traversal = new Traversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1))
        .makeError(aliasDefinition2, GOOG_SCOPE_ALIAS_REDEFINED, "alias1");
  }

  /**
   * Test handling of non-alias local in goog.scope.
   */
  @Test
  public void testNonAliasLocalInGoogScope() {
    // Given
    Node root = Mockito.mock(Node.class);
    Node nonAliasLocal = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);

    when(root.isScript()).thenReturn(true);
    when(nonAliasLocal.getParent()).thenReturn(Mockito.mock(Node.class));
    when(nonAliasLocal.getFirstChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue(NODE_VAR));
    when(nonAliasLocal.getLastChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue("local"));
    when(aliasDefinition.getFirstChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue(NODE_VAR));
    when(aliasDefinition.getLastChild())
        .thenReturn(Mockito.mock(Node.class).withReturnValue("alias"));

    // When
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Traversal traversal = new Traversal();
    traversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    verify(diagnostics, times(1))
        .makeError(nonAliasLocal, GOOG_SCOPE_NON_ALIAS_LOCAL, "local");
  }
}