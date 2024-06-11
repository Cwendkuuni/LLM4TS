package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilationTestRunner;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private static final String TEST_FILE = "ScopedAliasesTest.js";

  @org.junit.Test
  public void testScopedAliases() throws Exception {
    AbstractCompiler compiler = new CompilationTestRunner(new CompilerOptions())
        .setAllowAllWarnings(true)
        .compile(TEST_FILE);

    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
    Node root = compiler.getRootNode();
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check that the alias definitions were removed.
    List<Node> originalAliasDefinitions = traversal.getAliasDefinitionsInOrder();
    for (Node originalAliasDefinition : originalAliasDefinitions) {
      Node aliasDefinition = root.getChildWithName("goog.scope", originalAliasDefinition.getParent());
      assertThat(aliasDefinition).isNull();
    }

    // Check that the aliases were applied.
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    for (AliasUsage aliasUsage : aliasUsages) {
      Node aliasReference = ((AliasUsage) aliasUsage).getAliasReference();
      Node aliasDefinition = ((AliasUsage) aliasUsage).getAliasDefinition();
      Node expectedAliasDefinition = root.getChildWithName(
          aliasDefinition.getString(), aliasReference.getParent());
      assertThat(aliasReference).isEqualTo(expectedAliasDefinition);
    }

    // Check that the scopes were collapsed.
    List<Node> scopeCalls = traversal.getScopeCalls();
    for (Node scopeCall : scopeCalls) {
      Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
      Node expectedScopeClosureBlock = root.getChildWithName(
          "goog.scope", scopeCall.getParent());
      NodeUtil.tryMergeBlock(expectedScopeClosureBlock);
      Node expectedExpressionWithScopeCall = expectedScopeClosureBlock.getParent();
      assertThat(scopeClosureBlock).isEqualTo(expectedExpressionWithScopeCall.getLastChild());
    }
  }

  /**
   * Helper class that does nothing.
   */
  private static class NoOpAliasTransformationHandler implements
      AliasTransformationHandler {

    @Override
    public AliasTransformation logAliasTransformation(
        String sourceFileName, SourcePosition position) {
      return null;
    }
  }
}