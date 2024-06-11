import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions$AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions$AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope$Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock private AbstractCompiler compiler;
  @Mock private CompilerOptions$AliasTransformationHandler transformationHandler;
  @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock private Node rootNode;
  @Mock private Node externsNode;

  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoError() {
    // No error
  }

  @Test
  public void testProcess_InvalidCallToScopeMethod_BadParameterCount() {
    Node scopeCallNode = createCallNode("goog.scope", 2, new Object[]{new Object[0], new Object[1]} );
    Node scopeFunctionNode = createFunctionNode("function() {}", null);
    Node parentNode = createBlockNode("{goog.scope(function() {})}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(GOOG_SCOPE_HAS_BAD_PARAMETERS)))
        .thenReturn(null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);
    when(traversal.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(GOOG_SCOPE_HAS_BAD_PARAMETERS),
        ArgumentMatchers.isA(DiagnosticType[].class))).thenReturn(null);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeCallNode), ArgumentMatchers.eq(GOOG_SCOPE_HAS_BAD_PARAMETERS));
  }

  @Test
  public void testProcess_InvalidCallToScopeMethod_ImproperUsage() {
    Node scopeCallNode1 = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node scopeCallNode2 = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node scopeCallNode3 = createCallNode("goog.scope", 2, new Object[]{new Object[0], new Object[0] });
    Node scopeFunctionNode = createFunctionNode("function() {}", null);
    Node parentNode = createBlockNode("{goog.scope(function() {})}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(GOOG_SCOPE_USED_IMPROPERLY)))
        .thenReturn(null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);
    when(traversal.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(GOOG_SCOPE_USED_IMPROPERLY),
        ArgumentMatchers.isA(DiagnosticType[].class))).thenReturn(null);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeCallNode1), ArgumentMatchers.eq(GOOG_SCOPE_USED_IMPROPERLY));
    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeCallNode2), ArgumentMatchers.eq(GOOG_SCOPE_USED_IMPROPERLY));
    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeCallNode3), ArgumentMatchers.eq(GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testProcess_InvalidCallToScopeMethod_ThisReference() {
    Node scopeCallNode = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node scopeFunctionNode = createFunctionNode("function() { this.name = 'value'; }", null);
    Node parentNode = createBlockNode("{goog.scope(function() {})}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(GOOG_SCOPE_REFERENCES_THIS)))
        .thenReturn(null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);
    when(traversal.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(GOOG_SCOPE_REFERENCES_THIS),
        ArgumentMatchers.isA(DiagnosticType[].class))).thenReturn(null);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeFunctionNode), ArgumentMatchers.eq(GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test
  public void testProcess_InvalidCallToScopeMethod_ReturnStatement() {
    Node scopeCallNode = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node scopeFunctionNode = createFunctionNode("function() { return 42; }", null);
    Node parentNode = createBlockNode("{goog.scope(function() {})}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(GOOG_SCOPE_USES_RETURN)))
        .thenReturn(null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);
    when(traversal.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(GOOG_SCOPE_USES_RETURN),
        ArgumentMatchers.isA(DiagnosticType[].class))).thenReturn(null);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeFunctionNode), ArgumentMatchers.eq(GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testProcess_InvalidCallToScopeMethod_ThrowStatement() {
    Node scopeCallNode = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node scopeFunctionNode = createFunctionNode("function() { throw new Error(); }", null);
    Node parentNode = createBlockNode("{goog.scope(function() {})}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(GOOG_SCOPE_USES_THROW)))
        .thenReturn(null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);
    when(traversal.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(GOOG_SCOPE_USES_THROW),
        ArgumentMatchers.isA(DiagnosticType[].class))).thenReturn(null);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    verify(compiler).report(traversal, ArgumentMatchers.eq(scopeFunctionNode), ArgumentMatchers.eq(GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testProcess_DuplicateAliasDefinition() {
    Node aliasDefinitionNode1 = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node aliasDefinitionNode2 = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node aliasDefinitionNode3 = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node scopeFunctionNode = createFunctionNode("function() {}", null);
    Node parentNode = createBlockNode("{goog.scope(function() {})}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(GOOG_SCOPE_ALIAS_REDEFINED),
        ArgumentMatchers.containsString(aliasDefinitionNode1.getString())))
        .thenReturn(null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);
    when(traversal.makeError(ArgumentMatchers.any(Node.class), ArgumentMatchers.eq(GOOG_SCOPE_ALIAS_REDEFINED),
        ArgumentMatchers.isA(DiagnosticType[].class)))
        .thenReturn(null);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    verify(compiler).report(traversal, ArgumentMatchers.eq(aliasDefinitionNode1), ArgumentMatchers.eq(GOOG_SCOPE_ALIAS_REDEFINED),
        ArgumentMatchers.containsString(aliasDefinitionNode1.getString()));
    verify(compiler).report(traversal, ArgumentMatchers.eq(aliasDefinitionNode2), ArgumentMatchers.eq(GOOG_SCOPE_ALIAS_REDEFINED),
        ArgumentMatchers.containsString(aliasDefinitionNode1.getString()));
    verify(compiler).report(traversal, ArgumentMatchers.eq(aliasDefinitionNode3), ArgumentMatchers.eq(GOOG_SCOPE_ALIAS_REDEFINED),
        ArgumentMatchers.containsString(aliasDefinitionNode1.getString()));
  }

  @Test
  public void testProcess_AliasUsage() {
    Node aliasDefinitionNode = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node aliasNode = createCallNode("goog.dom", null);
    Node parentNode = createBlockNode("{goog.scope(function() { goog.dom.createElement('DIV'); })}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parentNode);
    when(traversal.getParentNode()).thenReturn(rootNodeMock);
    when(traversal.getScopeRoot()).thenReturn(rootNodeMock);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);

    assertEquals(aliasDefinitionNode, aliasUsage.getAliasDefinition());
    assertEquals(aliasNode, aliasUsage.getAliasReference());
  }

  @Test
  public void testProcess_CollapseScope() {
    Node scopeCallNode = createCallNode("goog.scope", 1, new Object[]{new Object[0] });
    Node aliasDefinitionNode = createCallNode("goog.dom", null);
    Node aliasDefinitionNode2 = createCallNode("goog.dom2", null);
    Node scopeFunctionNode = createFunctionNode("function() {}", null);
    Node scopeClosureBlock = createBlockNode("{goog.dom.createElement('DIV'); goog.dom2.createElement('DIV'); }", null);
    Node parentNode = createBlockNode("{goog.scope(function() { })}", null);
    Node rootNodeMock = createBlockNode("{}", parentNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);

    scopedAliases.process(externsNode, rootNode);
    NodeTraversal.traverse(compiler, rootNode, traversal);

    NodeUtil.tryMergeBlock(scopeClosureBlock);

    assertEquals(1, scopeClosureBlock.getChildCount());
    assertEquals(NodeUtil.getFunctionBody(scopeFunctionNode), scopeClosureBlock.getChildAtIndex(0));
  }

  private Node createCallNode(String name, int argumentCount, Object[] arguments) {
    Node n = new NodeToken(Token.CALL);
    n.setString(name);
    Node argsNode = new Node[] { new NodeToken(Token.LP) }[argumentCount + 1];
    for (int i = 0; i < argumentCount; i++) {
      argsNode[i + 1] = new NodeToken(Token.LITERAL, String.valueOf(arguments[i]));
    }
    n.setChildren(argsNode);
    return n;
  }

  private Node createFunctionNode(String body, SourcePosition sourcePosition) {
    Node node = new NodeToken(Token.FUNCTION);
    node.setSourcePosition(sourcePosition);
    node.setString(body);
    return node;
  }

  private Node createBlockNode(String body, Node parent) {
    Node node = new NodeToken(Token.BLOCK);
    node.setString(body);
    node.setParent(parent);
    return node;
  }
}