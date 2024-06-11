package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = Mockito.mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testEmptyFile() {
    Node root = Node.createTree("");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testSimpleAlias() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node scopeFunction = scopeCall.getChildAtIndex(1);
    assertEquals("function() {}", scopeFunction.getString());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    Node aliasReference = aliasedNode.getAliasReference();
    assertEquals("goog.dom.createElement", aliasReference.getString());
    Node aliasDefinition = aliasedNode.getAliasDefinition();
    assertEquals("goog.dom", aliasDefinition.getString());
  }

  @Test
  public void testMultipleAliases() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var sayHello = function(name) {" +
        "    print('Hello, ' + name);" +
        "  };" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  sayHello('world');" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    Node aliasDefinitionDom = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinitionDom.getString());
    Node aliasDefinitionSayHello = aliasDefinitionsInOrder.get(1);
    assertEquals("function sayHello(name)", aliasDefinitionSayHello.getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node scopeFunction = scopeCall.getChildAtIndex(1);
    assertEquals("function() {", scopeFunction.getString());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(3, aliasUsages.size());
    AliasUsage aliasUsageDom = aliasUsages.get(0);
    assertTrue(aliasUsageDom instanceof AliasedNode);
    AliasedNode aliasedNodeDom = (AliasedNode) aliasUsageDom;
    Node aliasReferenceDom = aliasedNodeDom.getAliasReference();
    assertEquals("goog.dom.createElement", aliasReferenceDom.getString());
    Node aliasDefinitionDom = aliasedNodeDom.getAliasDefinition();
    assertEquals("goog.dom", aliasDefinitionDom.getString());

    AliasUsage aliasUsageSayHello = aliasUsages.get(1);
    assertTrue(aliasUsageSayHello instanceof AliasedNode);
    AliasedNode aliasedNodeSayHello = (AliasedNode) aliasUsageSayHello;
    Node aliasReferenceSayHello = aliasedNodeSayHello.getAliasReference();
    assertEquals("sayHello", aliasReferenceSayHello.getString());
    Node aliasDefinitionSayHello = aliasedNodeSayHello.getAliasDefinition();
    assertEquals("function sayHello(name)", aliasDefinitionSayHello.getString());

    AliasUsage aliasUsagePrint = aliasUsages.get(2);
    assertTrue(aliasUsagePrint instanceof AliasedTypeNode);
    AliasedTypeNode aliasedTypeNodePrint = (AliasedTypeNode) aliasUsagePrint;
    Node aliasReferencePrint = aliasedTypeNodePrint.getAliasReference();
    assertEquals("print", aliasReferencePrint.getString());
    String correctedTypePrint = aliasedTypeNodePrint.getCorrectedType();
    assertEquals("console.print", correctedTypePrint);
  }

  @Test
  public void testImproperScopeUsage() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom;" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType());
  }

  @Test
  public void testScopeWithFunctionParameter() {
    String source = "goog.scope(function(x) {" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  x();" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostic.getType());
  }

  @Test
  public void testScopeWithNonFunctionParameter() {
    String source = "goog.scope(42) {" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "}";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType());
  }

  @Test
  public void testScopeWithReturnStatement() {
    String source = "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getType());
  }

  @Test
  public void testScopeWithThrowStatement() {
    String source = "goog.scope(function() {" +
        "  throw new Error('An error occurred');" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getType());
  }

  @Test
  public void testScopeWithAliasRedefinition() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  var dom = 'world';" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType());
  }

  @Test
  public void testScopeWithNonAliasLocal() {
    String source = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var x = 42;" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})";

    Node root = Node.createTree(source);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostic.getType());
  }
}