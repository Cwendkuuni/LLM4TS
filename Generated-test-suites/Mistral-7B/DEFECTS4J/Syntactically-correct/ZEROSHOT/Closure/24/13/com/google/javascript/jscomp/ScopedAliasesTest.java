package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Set up the mock compiler and preprocessorSymbolTable.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);

    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Set up the ScopedAliases instance with the mocks.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Prepare the test root node.
    Node root = createTestNode("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});");

    // Test the processing of the ScopedAliases.
    scopedAliases.process(null, root);

    // Check the expected output.
    assertEquals(
        1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(
        1, scopedAliases.getScopeCalls().size());
    assertEquals(
        1, scopedAliases.getAliasUsages().size());

    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("goog.dom", aliasDefinition.getFirstChild().getQualifiedName());

    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    assertEquals(1, scopeCall.getChildCount());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    assertEquals(1, scopeCall.getLastChild().getChildCount());

    Node aliasUsage = scopedAliases.getAliasUsages().get(0);
    assertEquals("dom", aliasUsage.getAliasDefinition().getString());
    assertEquals("goog.dom", aliasUsage.getAliasDefinition().getFirstChild().getQualifiedName());
    assertEquals("dom.createElement(DIV)", aliasUsage.getNode().getString());
  }

  private Node createTestNode(String code) {
    Node node = NodeFactory.compile(code);
    Node root = NodeUtil.getOrCreateRoot(node);
    return root;
  }
}

class NodeFactory {
  // Implement NodeFactory to create Node instances for the unit tests.
  // ...
}