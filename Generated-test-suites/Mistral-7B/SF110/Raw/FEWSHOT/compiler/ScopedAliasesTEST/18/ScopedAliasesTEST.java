package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test that the ScopedAliases class is properly implemented.

    // Create a mock AbstractCompiler.
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);

    // Create a mock PreprocessorSymbolTable.
    PreprocessorSymbolTable mockSymbolTable = mock(PreprocessorSymbolTable.class);

    // Create a mock AliasTransformationHandler.
    AliasTransformationHandler mockHandler = mock(AliasTransformationHandler.class);

    // Create a ScopedAliases instance.
    ScopedAliases scopedAliases = new ScopedAliases(mockCompiler, mockSymbolTable, mockHandler);

    // Test that the process method correctly applies aliases.
    Node root = createTestNode("/test.js");
    scopedAliases.process(null, root);
    List<String> expectedAliasUsages = Lists.newArrayList(
        "goog.dom.createElement",
        "goog.dom.TagName.DIV"
    );
    List<Node> actualAliasUsages = getAliasUsages(root);
    assertTrue(actualAliasUsages.size() == expectedAliasUsages.size());
    for (int i = 0; i < expectedAliasUsages.size(); i++) {
      String expectedAliasUsage = expectedAliasUsages.get(i);
      Node actualAliasUsage = actualAliasUsages.get(i);
      assertEquals(expectedAliasUsage, getNodeName(actualAliasUsage));
    }

    // Test that the process method correctly removes alias definitions.
    List<Node> actualAliasDefinitions = getAliasDefinitions(root);
    assertTrue(actualAliasDefinitions.isEmpty());

    // Test that the process method correctly collapses scopes.
    Node expectedCollapsedScope = createTestNode("goog.scope(function() { goog.dom.createElement(goog.dom.TagName.DIV); })");
    Node actualCollapsedScope = getScopeCalls(root).get(0).getLastChild();
    assertEquals(expectedCollapsedScope, actualCollapsedScope);

    // Test that the process method correctly reports errors.
    Node testErrorNode = createTestNode("goog.scope(function() { this.x = 1; })");
    scopedAliases.process(null, testErrorNode);
    List<DiagnosticType> expectedDiagnostics = Lists.newArrayList(
        GOOG_SCOPE_REFERENCES_THIS
    );
    List<DiagnosticType> actualDiagnostics = getDiagnostics(testErrorNode);
    assertTrue(actualDiagnostics.size() == expectedDiagnostics.size());
    for (int i = 0; i < expectedDiagnostics.size(); i++) {
      DiagnosticType expectedDiagnostic = expectedDiagnostics.get(i);
      DiagnosticType actualDiagnostic = actualDiagnostics.get(i);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }

    // Test that the process method correctly handles multiple errors.
    Node testMultipleErrorsNode = createTestNode("goog.scope(function() { this.x = 1; return; goog.scope(function() { throw new Error(); }) })");
    scopedAliases.process(null, testMultipleErrorsNode);
    List<DiagnosticType> expectedMultipleDiagnostics = Lists.newArrayList(
        GOOG_SCOPE_REFERENCES_THIS,
        GOOG_SCOPE_USES_THROW
    );
    List<DiagnosticType> actualMultipleDiagnostics = getDiagnostics(testMultipleErrorsNode);
    assertTrue(actualMultipleDiagnostics.size() == expectedMultipleDiagnostics.size());
    for (int i = 0; i < expectedMultipleDiagnostics.size(); i++) {
      DiagnosticType expectedDiagnostic = expectedMultipleDiagnostics.get(i);
      DiagnosticType actualDiagnostic = actualMultipleDiagnostics.get(i);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }
  }

  private Node createTestNode(String nodeContent) {
    return Node.newCompilationUnit(new JSDocInfo(), new SourcePosition(), nodeContent);
  }

  private List<Node> getAliasDefinitions(Node root) {
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(mockCompiler, root, traversal);
    return traversal.getAliasDefinitionsInOrder();
  }

  private List<Node> getAliasUsages(Node root) {
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(mockCompiler, root, traversal);
    return traversal.getAliasUsages();
  }

  private List<Node> getScopeCalls(Node root) {
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(mockCompiler, root, traversal);
    return traversal.getScopeCalls();
  }

  private List<DiagnosticType> getDiagnostics(Node root) {
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(mockCompiler, root, traversal);
    return Lists.newArrayList(traversal.getDiagnostics());
  }

  private static final DiagnosticType GOOG_SCOPE_REFERENCES_THIS =
      DiagnosticType.error(
          "JSC_GOOG_SCOPE_REFERENCES_THIS",
          "The body of a goog.scope function cannot reference 'this'."
      );

  private static final DiagnosticType GOOG_SCOPE_USES_THROW =
      DiagnosticType.error(
          "JSC_GOOG_SCOPE_USES_THROW",
          "The body of a goog.scope function cannot use 'throw'."
      );
}