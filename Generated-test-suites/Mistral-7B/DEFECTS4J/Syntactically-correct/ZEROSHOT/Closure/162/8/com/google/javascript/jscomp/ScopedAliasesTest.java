package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.test.BaseCompilerTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest extends BaseCompilerTestCase {

  @Test
  public void testScopedAliases() {
    AliasTransformationHandler handler = new TestAliasTransformationHandler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPES);
    options.setAliasTransformationHandler(handler);

    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(getCompiler(),
        getPreprocessorSymbolTable(), handler);

    scopedAliases.process(getExterns(), root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    Var aliasVar = aliasDefinition.getParent().asVar();
    assertEquals("dom", aliasVar.getName());

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    assertEquals(2, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    Node aliasUsesNode1 = ((AliasedNode) aliasUsage1).getAliasReference();
    assertEquals("dom", aliasUsesNode1.getString());
    Node aliasDefinition1 = ((AliasedNode) aliasUsage1).getAliasDefinition();
    assertEquals("goog.dom", aliasDefinition1.getQualifiedName());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    Node aliasUsesNode2 = ((AliasedTypeNode) aliasUsage2).getAliasReference();
    assertEquals("DIV", aliasUsesNode2.getString());
    String correctedType = ((AliasedTypeNode) aliasUsage2).getCorrectedType();
    assertEquals("goog.dom.TagName.DIV", correctedType);

    assertFalse(scopedAliases.hasErrors());
    assertEquals(AliasTransformation.ALLOW_GOOG_SCOPES, handler.getTransformation());

    Node transformedRoot = scopedAliases.getTransformedRoot();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        transformedRoot.toString());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    AliasTransformationHandler handler = new TestAliasTransformationHandler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPES);
    options.setAliasTransformationHandler(handler);

    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom2 = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "  this.foo = 'bar';" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(getCompiler(),
        getPreprocessorSymbolTable(), handler);

    scopedAliases.process(getExterns(), root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    assertEquals(3, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    Node aliasUsesNode1 = ((AliasedNode) aliasUsage1).getAliasReference();
    assertEquals("dom", aliasUsagesNode1.getString());
    Node aliasDefinition1 = ((AliasedNode) aliasUsage1).getAliasDefinition();
    assertEquals("goog.dom", aliasDefinition1.getQualifiedName());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    Node aliasUsesNode2 = ((AliasedNode) aliasUsage2).getAliasReference();
    assertEquals("dom2", aliasUsagesNode2.getString());
    assertTrue(scopedAliases.hasErrors());
    assertNull(handler.getTransformation());

    Node transformedRoot = scopedAliases.getTransformedRoot();
    assertNull(transformedRoot);
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    AliasTransformationHandler handler = new TestAliasTransformationHandler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPES);
    options.setAliasTransformationHandler(handler);

    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  return dom.TagName.DIV;" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(getCompiler(),
        getPreprocessorSymbolTable(), handler);

    scopedAliases.process(getExterns(), root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    assertEquals(0, aliasUsages.size());

    assertTrue(scopedAliases.hasErrors());
    assertNull(handler.getTransformation());

    Node transformedRoot = scopedAliases.getTransformedRoot();
    assertNull(transformedRoot);
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    AliasTransformationHandler handler = new TestAliasTransformationHandler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPES);
    options.setAliasTransformationHandler(handler);

    String sourceCode =
        "goog.scope(function() {" +
        "  try {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  } catch (e) {" +
        "    throw new Error('An error occurred.');" +
        "  }" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(getCompiler(),
        getPreprocessorSymbolTable(), handler);

    scopedAliases.process(getExterns(), root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    assertEquals(0, aliasUsages.size());

    assertTrue(scopedAliases.hasErrors());
    assertNull(handler.getTransformation());

    Node transformedRoot = scopedAliases.getTransformedRoot();
    assertNull(transformedRoot);
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    AliasTransformationHandler handler = new TestAliasTransformationHandler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPES);
    options.setAliasTransformationHandler(handler);

    String sourceCode =
        "goog.scope(function() {" +
        "  this.dom = goog.dom;" +
        "  var DIV = this.dom.TagName.DIV;" +
        "  this.dom.createElement(DIV);" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(getCompiler(),
        getPreprocessorSymbolTable(), handler);

    scopedAliases.process(getExterns(), root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    assertEquals(0, aliasUsages.size());

    assertTrue(scopedAliases.hasErrors());
    assertNull(handler.getTransformation());

    Node transformedRoot = scopedAliases.getTransformedRoot();
    assertNull(transformedRoot);
  }

  @Test
  public void testScopedAliasesWithRedefinedAlias() {
    AliasTransformationHandler handler = new TestAliasTransformationHandler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPES);
    options.setAliasTransformationHandler(handler);

    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom.TagName.DIV;" +
        "});";

    Node root = parse(sourceCode);

    ScopedAliases scopedAliases = new ScopedAliases(getCompiler(),
        getPreprocessorSymbolTable(), handler);

    scopedAliases.process(getExterns(), root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", aliasDefinition.getString());

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);

    assertEquals(0, aliasUsages.size());

    assertTrue(scopedAliases.hasErrors());
    assertNull(handler.getTransformation());

    Node transformedRoot = scopedAliases.getTransformedRoot();
    assertNull(transformedRoot);
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    private AliasTransformation transformation;

    public void logAliasTransformation(String fileName, SourcePosition position) {
      this.transformation = AliasTransformation.ALLOW_GOOG_SCOPES;
    }

    public AliasTransformation getTransformation() {
      return transformation;
    }
  }
}