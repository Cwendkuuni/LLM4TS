package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    when(transformationHandler.logAliasTransformation(String.class, String.class))
        .thenReturn(new AliasTransformation());
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testValidScopedAlias() {
    Node root = createTestRoot();
    Node scopeCall = createScopeCallNode();
    when(preprocessorSymbolTable.addReference(Node.class)).thenReturn(null);
    when(compiler.report(NodeTraversal.Error.class)).thenReturn(null);

    scopedAliases.hotSwapScript(root, null);

    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // We should have one alias definition in order.
    assertThat(aliasDefinitionsInOrder).hasSize(1);
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    // The aliasDefinition should be a Node with no children.
    assertThat(aliasDefinition).doesNotHaveValue(Node.class, "name");
    // The aliasDefinition should have a source position.
    SourcePosition sourcePosition = aliasDefinition.getSourcePosition();
    // We should have one scope call.
    assertThat(scopeCalls).hasSize(1);
    Node scopeCallNode = scopeCalls.get(0);
    // The scopeCallNode should have exactly two children.
    assertThat(scopeCallNode).hasChildren(2);
    // The first child should be "goog.scope".
    Node googScope = scopeCallNode.getChildAtIndex(0);
    assertThat(googScope.getString()).isEqualTo("goog.scope");
    // The second child should be an anonymous function.
    Node anonymousFunction = scopeCallNode.getChildAtIndex(1);
    assertThat(anonymousFunction).isInstanceOf(Node.class);

    // We should have one alias usage.
    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
    Node aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;

    // The aliasReference should be the "DOM" Node.
    Node domNode = root.getChildAtIndex(0);
    assertThat(aliasReference).isEqualTo(domNode);
    // The aliasedNode should be the "goog.dom" Node.
    Node googDomNode = root.getChildAtIndex(1);
    assertThat(aliasedNode).isEqualTo(googDomDomNode);

    // The aliasUsage should have applied the alias.
    Node replacedNode = aliasReference.getParent();
    assertThat(replacedNode.getChildAtIndex(0)).isEqualTo(aliasedNode);

    // All the nodes should be in the same order.
    Node[] nodes = {domNode, googDomNode, scopeCallNode};
    List<Node> nodesList = Lists.newArrayList(nodes);
    Node originalRoot = root.cloneTree();
    NodeTraversal.traverse(compiler, originalRoot, null);
    List<Node> traversedNodes = NodeTraversal.getTraversedNodes(compiler);
    List<Node> traversedNodesList = Lists.newArrayList(traversedNodes.toArray(new Node[0]));
    // The traversed nodes should be in the same order.
    assertThat(traversedNodesList).isEqualTo(nodesList);
  }

  @Test
  public void testInvalidScopedAlias() {
    Node root = createTestRoot();
    Node invalidScopeCall = createInvalidScopeCallNode();
    when(preprocessorSymbolTable.addReference(Node.class)).thenReturn(null);
    when(compiler.report(NodeTraversal.Error.class))
        .thenReturn(null);

    scopedAliases.hotSwapScript(root, null);

    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // There should be no alias definitions.
    assertThat(aliasDefinitionsInOrder).isEmpty();
    // There should be no alias usages.
    assertThat(aliasUsages).isEmpty();
    // There should be no scope calls.
    assertThat(scopeCalls).isEmpty();
    // There should be an error for the invalid scope call.
    Node errorNode = root.getChildAtIndex(1);
    // The error node should have a diagnostic type.
    DiagnosticType diagnosticType = errorNode.getDiagnostics().get(0).getType();
    // The diagnostic type should be "JSC_GOOG_SCOPE_USED_IMPROPERLY".
    assertThat(diagnosticType).isEqualTo(GOOG_SCOPE_USED_IMPROPERLY);
  }

  private Node createTestRoot() {
    Node root = new Node();
    Node domNode = new Node();
    domNode.setString("DOM");
    root.addChild(domNode);

    Node googDomNode = new Node();
    googDomNode.setString("goog.dom");
    root.addChild(googDomNode);

    Node scopeCallNode = createScopeCallNode();
    root.addChild(scopeCallNode);

    return root;
  }

  private Node createScopeCallNode() {
    Node scopeCall = new Node();
    Node googScope = new Node();
    googScope.setString("goog.scope");
    scopeCall.addChild(googScope);

    Node anonymousFunction = new Node();
    anonymousFunction.setType(Token.FUNCTION);
    scopeCall.addChild(anonymousFunction);

    return scopeCall;
  }

  private Node createInvalidScopeCallNode() {
    Node invalidScopeCall = new Node();
    Node googScope = new Node();
    googScope.setString("goog.scope");
    invalidScopeCall.addChild(googScope);

    Node invalidAnonymousFunction = new Node();
    invalidAnonymousFunction.setType(Token.FUNCTION);
    invalidAnonymousFunction.setString("function() { return 1; }");
    invalidScopeCall.addChild(invalidAnonymousFunction);

    return invalidScopeCall;
  }
}