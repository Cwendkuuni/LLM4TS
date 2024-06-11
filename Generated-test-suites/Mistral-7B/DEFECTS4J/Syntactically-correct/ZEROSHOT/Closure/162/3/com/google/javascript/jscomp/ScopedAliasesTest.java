package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private static final String SCOPING_METHOD_NAME = "goog.scope";

  private ScopedAliases scopedAliases;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setUp() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testProcess() {
    Node root = NodeUtil.createRootNode("test.js");

    scopedAliases.process(null, root);
  }

  @Test
  public void testHotSwapScript() {
    Node root = NodeUtil.createRootNode("test.js");
    Node rootClone = NodeUtil.cloneTree(root);

    scopedAliases.hotSwapScript(root, rootClone);

    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Test with a valid scoped alias block
    Node validScopeNode = NodeUtil.createScopeNode("test.js", 1, 1,
        NodeUtil.createCallNode(SCOPING_METHOD_NAME, Lists.newArrayList()));
    Node validAnonymousFnNode = NodeUtil.createFunctionNode(null, null,
        Lists.newArrayList(), Lists.newArrayList());
    Node validAliasDefinitionNode = NodeUtil.createAssignNode(
        NodeUtil.createNameNode("alias"), NodeUtil.cloneTree(validAnonymousFnNode));
    validScopeNode.getLastChild().addChild(validAliasDefinitionNode);

    Node validScopeCallNode = NodeUtil.createCallNode(
        NodeUtil.createNameNode(SCOPING_METHOD_NAME),
        Lists.newArrayList(NodeUtil.createFunctionNode(null, null,
            Lists.newArrayList(), Lists.newArrayList())));

    Node validRootNode = NodeUtil.createRootNode("test.js");
    Node validRootCloneNode = NodeUtil.cloneTree(validRootNode);

    scopedAliases.hotSwapScript(validRootNode, validRootCloneNode);

    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node scopeCallNode = scopedAliases.getScopeCalls().get(0);
    Node aliasDefinitionNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    Node aliasUsageNode = scopedAliases.getAliasUsages().get(0);

    assertEquals(validScopeCallNode, scopeCallNode);
    assertEquals(validAliasDefinitionNode, aliasDefinitionNode);
    assertEquals(aliasDefinitionNode, aliasUsageNode.getAliasDefinition());
    assertEquals(aliasDefinitionNode, aliasUsageNode.getAliasReference());

    // Test with an invalid scoped alias block
    Node invalidScopeNode = NodeUtil.createScopeNode("test.js", 1, 1,
        NodeUtil.createCallNode(SCOPING_METHOD_NAME, Lists.newArrayList()));
    Node invalidAnonymousFnNode = NodeUtil.createFunctionNode(null, null,
        Lists.newArrayList(NodeUtil.createNameNode("invalidAlias")),
        Lists.newArrayList());
    Node invalidAliasDefinitionNode = NodeUtil.createAssignNode(
        NodeUtil.createNameNode("alias"), NodeUtil.cloneTree(invalidAnonymousFnNode));
    invalidScopeNode.getLastChild().addChild(invalidAliasDefinitionNode);

    Node invalidRootNode = NodeUtil.createRootNode("test.js");
    Node invalidRootCloneNode = NodeUtil.cloneTree(invalidRootNode);

    scopedAliases.hotSwapScript(invalidRootNode, invalidRootCloneNode);

    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    Node invalidScopeCallNode = scopedAliases.getScopeCalls().get(0);
    Node invalidAliasDefinitionNode = scopedAliases.getAliasDefinitionsInOrder().get(0);
    Node invalidAliasUsageNode = scopedAliases.getAliasUsages().get(0);

    assertNotNull(invalidScopeCallNode);
    assertNull(invalidScopeCallNode);
    assertNotNull(invalidAliasDefinitionNode);
    assertNull(invalidAliasDefinitionNode);
    assertNotNull(invalidAliasUsageNode);
    assertNull(invalidAliasUsageNode);

    // Test with a scoped alias block with multiple alias definitions
    Node multipleAliasesScopeNode = NodeUtil.createScopeNode("test.js", 1, 1,
        NodeUtil.createCallNode(SCOPING_METHOD_NAME, Lists.newArrayList()));
    Node firstAliasDefinitionNode = NodeUtil.createAssignNode(
        NodeUtil.createNameNode("alias1"), NodeUtil.cloneTree(validAnonymousFnNode));
    Node secondAliasDefinitionNode = NodeUtil.createAssignNode(
        NodeUtil.createNameNode("alias2"), NodeUtil.cloneTree(validAnonymousFnNode));
    multipleAliasesScopeNode.getLastChild().addChild(firstAliasDefinitionNode);
    multipleAliasesScopeNode.getLastChild().addChild(secondAliasDefinitionNode);

    Node multipleAliasesRootNode = NodeUtil.createRootNode("test.js");
    Node multipleAliasesRootCloneNode = NodeUtil.cloneTree(multipleAliasesRootNode);

    scopedAliases.hotSwapScript(multipleAliasesRootNode, multipleAliasesRootCloneNode);

    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(2, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(2, scopedAliases.getAliasUsages().size());

    Node firstAliasDefinitionNodeFromList = scopedAliases.getAliasDefinitionsInOrder().get(0);
    Node secondAliasDefinitionNodeFromList = scopedAliases.getAliasDefinitionsInOrder().get(1);

    assertEquals(firstAliasDefinitionNode, firstAliasDefinitionNodeFromList);
    assertEquals(secondAliasDefinitionNode, secondAliasDefinitionNodeFromList);

    Node firstAliasUsageNode = scopedAliases.getAliasUsages().get(0);
    Node secondAliasUsageNode = scopedAliases.getAliasUsages().get(1);

    assertEquals(firstAliasDefinitionNodeFromList, firstAliasUsageNode.getAliasDefinition());
    assertEquals(secondAliasDefinitionNodeFromList, secondAliasUsageNode.getAliasDefinition());
    assertEquals(firstAliasDefinitionNodeFromList, firstAliasUsageNode.getAliasReference());
    assertEquals(secondAliasDefinitionNodeFromList, secondAliasUsageNode.getAliasReference());
  }
}