import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testProcess() {
    Node root = Node.newRootNode();
    scopedAliases.process(null, root);

    // No errors expected.
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testHotSwapScript() {
    Node root = Node.newRootNode();
    Node scopeCallNode = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction()));
    Node aliasDefinitionNode = Node.newAssignment(
        Node.createLocal("x"),
        Node.createQualifiedName("goog", "x"));
    Node aliasUsageNode = Node.newAssignment(
        Node.createLocal("y"), Node.createName("x"));

    Node scopeCallParentNode = Node.newExpressionStatement(scopeCallNode);
    Node aliasDefinitionParentNode = Node.newExpressionStatement(aliasDefinitionNode);
    Node aliasUsageParentNode = Node.newExpressionStatement(aliasUsageNode);

    Node rootWithScopeCall = Node.newBlock(
        Node.newList(scopeCallParentNode));
    Node rootWithAliasDefinition = Node.newBlock(
        Node.newList(aliasDefinitionParentNode));
    Node rootWithAliasUsage = Node.newBlock(
        Node.newList(aliasUsageParentNode));

    Node rootWithAllNodes = Node.newBlock(
        Node.newList(
            rootWithScopeCall,
            rootWithAliasDefinition,
            rootWithAliasUsage));

    scopedAliases.hotSwapScript(root, null);

    // No errors expected.
    assertFalse(compiler.hasErrors());

    // Check that aliasDefinitionNode and aliasUsageNode are applied.
    Node rootWithAppliedAliases = compiler.getRoot();
    assertSame(rootWithAliasDefinition, rootWithAppliedAliases.getChildAtIndex(1));
    assertSame(
        Node.createQualifiedName("goog", "x"),
        rootWithAppliedAliases.getChildAtIndex(1).getLastChild());
    assertSame(
        Node.createLocal("y"),
        rootWithAppliedAliases.getChildAtIndex(2).getChildAtIndex(0));
    assertSame(
        Node.createName("x"),
        rootWithAppliedAliases.getChildAtIndex(2).getChildAtIndex(1));
  }

  @Test
  public void testApplyAlias() {
    Node aliasDefinitionNode = Node.newAssignment(
        Node.createLocal("x"),
        Node.createQualifiedName("goog", "x"));
    Node aliasUsageNode = Node.newAssignment(
        Node.createLocal("y"), Node.createName("x"));

    AliasUsage aliasUsage = new ScopedAliases.AliasedNode(aliasUsageNode, aliasDefinitionNode);
    aliasUsage.applyAlias();

    // Check that aliasUsageNode is replaced by aliasDefinitionNode.
    assertSame(aliasDefinitionNode, aliasUsageNode.getParent());
  }

  @Test
  public void testFindAliases() {
    Traversal traversal = mock(Traversal.class);
    Scope scope = mock(Scope.class);

    when(compiler.getScopeRoot()).thenReturn(scope);

    Var aliasVar1 = mock(Var.class);
    Var aliasVar2 = mock(Var.class);
    when(scope.getVar("x")).thenReturn(aliasVar1);
    when(scope.getVar("y")).thenReturn(aliasVar2);

    when(aliasVar1.getInitialValue()).thenReturn(
        Node.newQualifiedName("goog", "x"));
    when(aliasVar2.getInitialValue()).thenReturn(
        Node.newQualifiedName("goog", "y"));

    when(traversal.getScope()).thenReturn(scope);

    scopedAliases.findAliases(traversal);

    // Check that aliases map contains expected entries.
    Map<String, Var> aliases = scopedAliases.aliases;
    assertSame(aliasVar1, aliases.get("x"));
    assertSame(aliasVar2, aliases.get("y"));
  }

  @Test
  public void testValidateScopeCall() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction()));

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    // No errors expected.
    assertNull(compiler.getLastError());
  }

  @Test
  public void testValidateScopeCall_badParameters() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newNumber(1), Node.newFunction()));

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    // Expected error.
    assertSame(GOOG_SCOPE_HAS_BAD_PARAMETERS, compiler.getLastError());
  }

  @Test
  public void testValidateScopeCall_improperlyUsed() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction(), Node.newName("x")));

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    // Expected error.
    assertSame(GOOG_SCOPE_USED_IMPROPERLY, compiler.getLastError());
  }

  @Test
  public void testValidateScopeCall_referencesThis() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction()));
    Node thisNode = Node.newThis();

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    Node bodyNode = node.getLastChild();
    node.setLastChild(Node.newExpressionStatement(thisNode));
    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    // Expected error.
    assertSame(GOOG_SCOPE_REFERENCES_THIS, compiler.getLastError());
  }

  @Test
  public void testValidateScopeCall_usesReturn() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction()));
    Node returnNode = Node.newReturn();

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    Node bodyNode = node.getLastChild();
    node.setLastChild(returnNode);
    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    // Expected error.
    assertSame(GOOG_SCOPE_USES_RETURN, compiler.getLastError());
  }

  @Test
  public void testValidateScopeCall_usesThrow() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction()));
    Node throwNode = Node.newThrow();

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    Node bodyNode = node.getLastChild();
    node.setLastChild(throwNode);
    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));

    // Expected error.
    assertSame(GOOG_SCOPE_USES_THROW, compiler.getLastError());
  }

  @Test
  public void testValidateScopeCall_aliasRedefined() {
    Traversal traversal = mock(Traversal.class);
    Node node = Node.newCall(
        Node.createQualifiedName("goog", "scope"), Lists.newArrayList(
            Node.newFunction()));

    Node aliasDefinitionNode1 = Node.newAssignment(
        Node.createLocal("x"), Node.createQualifiedName("goog", "x"));
    Node aliasDefinitionNode2 = Node.newAssignment(
        Node.createLocal("x"), Node.newNumber(1));

    scopedAliases.validateScopeCall(traversal, node, Node.newName("goog"));
    scopedAliases.aliasDefinitionsInOrder.add(aliasDefinitionNode1);
    scopedAliases.aliasDefinitionsInOrder.add(aliasDefinitionNode2);

    // Expected error.
    assertSame(GOOG_SCOPE_ALIAS_REDEFINED, compiler.getLastError());
  }

  @Test
  public void testTraverse() {
    Traversal traversal = mock(Traversal.class);
    Node root = Node.newRootNode();
    NodeTraversal.traverse(compiler, root, traversal);

    // No errors expected.
    assertFalse(compiler.hasErrors());
  }
}

enum DiagnosticType {
  error(1),
  warning(2);

  private final int level;

  DiagnosticType(int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }
}