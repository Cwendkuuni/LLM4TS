package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Test;
import org.mockito.Mockito;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = createTestNode("/test.js", "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "  goog.scope(function() {\n" +
        "    var dom2 = goog.dom;\n" +
        "    var DIV2 = dom2.TagName.DIV;\n" +
        "    dom2.createElement(DIV2);\n" +
        "  });\n" +
        "});");

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(1).getString());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(2, scopeCalls.size());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());

    Node domElement = aliasDefinitionsInOrder.get(0).getLastChild();
    Node domElementUsage = aliasUsages.get(0).getNode();
    assertEquals(domElement, domElementUsage);

    Node domElement2 = aliasDefinitionsInOrder.get(1).getLastChild();
    Node domElement2Usage = aliasUsages.get(1).getNode();
    assertEquals(domElement2, domElement2Usage);

    Var domVar = Mockito.spy(new Var("dom"));
    when(preprocessorSymbolTable.getVar("dom")).thenReturn(domVar);
    when(domVar.getInitialValue()).thenReturn(createTestNode(
        "/test.js", "goog.dom"));

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    rootNode = createTestNode("/test.js", "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "  goog.scope(function() {\n" +
        "    var dom2 = goog.dom;\n" +
        "    var DIV2 = dom2.TagName.DIV;\n" +
        "    dom2.createElement(DIV2);\n" +
        "  });\n" +
        "});");

    scopedAliases.process(null, rootNode);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(1).getString());

    scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(2, scopeCalls.size());

    aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());

    domElement = aliasDefinitionsInOrder.get(0).getLastChild();
    domElementUsage = aliasUsages.get(0).getNode();
    assertEquals(domElement, domElementUsage);

    domElement2 = aliasDefinitionsInOrder.get(1).getLastChild();
    domElement2Usage = aliasUsages.get(1).getNode();
    assertEquals(domElement2, domElement2Usage);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        NodeUtil.toSourceString(scopedAliases.getTraversal().getRootNode()));
  }

  private Node createTestNode(String fileName, String nodeString) {
    Node rootNode = new Node();
    Node testNode = new Node();
    testNode.setString(nodeString);
    testNode.setSourceFileName(fileName);
    rootNode.setFirstChild(testNode);
    return rootNode;
  }

  private ScopedAliases.Traversal getTraversal() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, createTestNode("/test.js", ""));
    return scopedAliases.getTraversal();
  }
}

class ScopedAliasesTest$TestTraversal implements ScopedAliases.Traversal {
  // Empty implementation to make ScopedAliasesTest testable
}

class ScopedAliasesTest$TestAliasedNode implements ScopedAliases.AliasedNode {
  private final Node aliasReference;

  private final Node aliasDefinition;

  TestAliasedNode(Node aliasReference, Node aliasDefinition) {
    this.aliasReference = aliasReference;
    this.aliasDefinition = aliasDefinition;
  }

  @Override
  public void applyAlias() {
    // Empty implementation to make ScopedAliasesTest testable
  }
}

class ScopedAliasesTest$TestAliasedTypeNode implements ScopedAliases.AliasedTypeNode {
  private final Node aliasReference;

  private final String correctedType;

  TestAliasedTypeNode(Node aliasReference, String correctedType) {
    this.aliasReference = aliasReference;
    this.correctedType = correctedType;
  }

  @Override
  public void applyAlias() {
    // Empty implementation to make ScopedAliasesTest testable
  }
}