package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Test class for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String jsSource =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformations(
        Lists.newArrayList(new AliasTransformation("goog.dom.TagName", "DIV")));
    AliasTransformationHandler transformationHandler =
        new AliasTransformationHandler(options);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setPrintStackTracesOnError(false);
    ScopedAliases scopedAliases =
        new ScopedAliases(compiler, null, transformationHandler);

    Node root = ParserUtilities.parse(jsSource, null, null, null);
    scopedAliases.process(null, root);

    Collection<Node> aliasDefinitionsInOrder =
        scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertEquals("Unexpected number of aliasDefinitionsInOrder", 1,
        aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("Unexpected aliasDefinition type", NAME, aliasDefinition.getType());
    assertEquals("Unexpected aliasDefinition string", "goog.dom.TagName",
        aliasDefinition.getString());

    assertEquals("Unexpected number of scopeCalls", 1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("Unexpected scopeCall type", CALL, scopeCall.getType());
    Node scopeNode = scopeCall.getFirstChild();
    assertEquals("Unexpected scopeNode type", NAME, scopeNode.getType());
    assertEquals("Unexpected scopeNode string", "goog.scope",
        scopeNode.getString());

    assertEquals("Unexpected number of aliasUsages", 1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      Node aliasReference = aliasedNode.aliasReference;
      Node aliasDefinition = aliasedNode.aliasDefinition;
      assertEquals("Unexpected aliasReference type", NAME, aliasReference.getType());
      assertEquals("Unexpected aliasReference string", "DIV",
          aliasReference.getString());
      assertEquals("Unexpected aliasDefinition type", NAME, aliasDefinition.getType());
      assertEquals("Unexpected aliasDefinition string",
          "goog.dom.TagName.DIV", aliasDefinition.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
      Node aliasReference = aliasedTypeNode.aliasReference;
      String correctedType = aliasedTypeNode.correctedType;
      assertEquals("Unexpected aliasReference type", NAME, aliasReference.getType());
      assertEquals("Unexpected aliasReference string",
          "goog.dom.TagName", aliasReference.getString());
      assertEquals("Unexpected correctedType string",
          "goog.dom.TagName.DIV", correctedType);
    } else {
      fail("Unexpected aliasUsage type");
    }

    // Validate the transformed output.
    Node transformedRoot = compiler.getOutput();
    assertEquals("Unexpected transformedRoot type", SCRIPT,
        transformedRoot.getType());
    List<Node> transformedStatements = NodeUtil.getStatements(transformedRoot);
    assertEquals("Unexpected number of transformedStatements", 1,
        transformedStatements.size());
    Node transformedStatement = transformedStatements.get(0);
    assertEquals("Unexpected transformedStatement type",
        EXPRESSION, transformedStatement.getType());
    Node transformedExpression = transformedStatement.getLastChild();
    assertEquals("Unexpected transformedExpression type", CALL,
        transformedExpression.getType());
    Node transformedScopeCall = transformedExpression.getFirstChild();
    assertEquals("Unexpected transformedScopeCall type", NAME,
        transformedScopeCall.getType());
    assertEquals("Unexpected transformedScopeCall string",
        "goog.dom", transformedScopeCall.getString());
    Node transformedScopeBlock = transformedScopeCall.getLastChild();
    assertEquals("Unexpected transformedScopeBlock type", BLOCK,
        transformedScopeBlock.getType());
    List<Node> transformedBlockStatements =
        NodeUtil.getStatements(transformedScopeBlock);
    assertEquals("Unexpected number of transformedBlockStatements", 1,
        transformedBlockStatements.size());
    Node transformedStatementInScopeBlock = transformedBlockStatements.get(0);
    assertEquals("Unexpected transformedStatementInScopeBlock type",
        ASSIGNMENT, transformedStatementInScopeBlock.getType());
    Node transformedAssignment = transformedStatementInScopeBlock;
    for (Node child = transformedAssignment.getFirstChild();
         child != null; child = child.getNext()) {
      if (child.getType() == NAME) {
        String name = child.getString();
        if (name.equals("dom")) {
          transformedAssignment = child;
          break;
        }
      }
    }
    Node transformedInitialValue = transformedAssignment.getLastChild();
    assertEquals("Unexpected transformedInitialValue type", DOT_NAME,
        transformedInitialValue.getType());
    Node transformedAlias = transformedInitialValue.getLastChild();
    assertEquals("Unexpected transformedAlias type", NAME, transformedAlias.getType());
    assertEquals("Unexpected transformedAlias string", "TagName",
        transformedAlias.getString());
    Node transformedDot = transformedAlias.getPrevious();
    assertEquals("Unexpected transformedDot type", DOT, transformedDot.getType());
    Node transformedQualifiedName = transformedDot.getLastChild();
    assertEquals("Unexpected transformedQualifiedName type", NAME,
        transformedQualifiedName.getType());
    assertEquals("Unexpected transformedQualifiedName string", "DIV",
        transformedQualifiedName.getString());

    // Validate the diagnostics.
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals("Unexpected number of diagnostics", 0, diagnostics.size());
  }
}