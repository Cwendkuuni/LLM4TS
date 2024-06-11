import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript. rhino.SourcePosition;

import java.util.List;

/**
 * Tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private static final String CLASS_NAME = ScopedAliases.class.getName();

  @org.junit.Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    ErrorCollector errorCollector = new ErrorCollector();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);

    String sourceCode =
        "goog.scope(function() {" +
            "  var x = goog.foo;" +
            "  var y = goog.bar;" +
            "  var z = goog.baz;" +
            "  goog.scope(function() {" +
            "    var x = 1;" +
            "    var y = 2;" +
            "    var z = 3;" +
            "  });" +
            "});";

    List<Node> abstractSyms = Lists.newArrayList();
    Node root = Node.createTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(2, aliasDefinitions.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinitionX = aliasDefinitions.get(0);
    Node aliasDefinitionY = aliasDefinitions.get(1);
    Node scopeCall = scopeCalls.get(0);

    assertEquals(Node.SCOPING_FUNCTION, aliasDefinitionX.getType());
    assertEquals("goog.foo", aliasDefinitionX.getQualifiedName());
    assertEquals("goog.foo", NodeUtil.getFunctionName(aliasDefinitionX));

    assertEquals(Node.SCOPING_FUNCTION, aliasDefinitionY.getType());
    assertEquals("goog.bar", aliasDefinitionY.getQualifiedName());
    assertEquals("goog.bar", NodeUtil.getFunctionName(aliasDefinitionY));

    assertEquals(Node.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    assertEquals(1, aliasUsageCount(aliasDefinitionX));
    assertEquals(1, aliasUsageCount(aliasDefinitionY));
  }

  @org.junit.Test
  public void testScopedAliasesException() {
    Compiler compiler = new Compiler();
    ErrorCollector errorCollector = new ErrorCollector();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);

    String sourceCode =
        "goog.scope(function() {" +
            "  throw new Error('Test error');" +
            "});";

    List<Node> abstractSyms = Lists.newArrayList();
    Node root = Node.createTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(0, aliasDefinitions.size());
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);

    assertEquals(Node.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    compiler.run(
        root,
        null,
        errorCollector,
        null,
        options,
        null,
        null,
        null,
        null);

    assertTrue(errorCollector.hasErrors());
    List<DiagnosticType> errors = errorCollector.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(
        GOOG_SCOPE_USES_THROW, errors.get(0).getType());
  }

  @org.junit.Test
  public void testScopedAliasesNonAliasLocal() {
    Compiler compiler = new Compiler();
    ErrorCollector errorCollector = new ErrorCollector();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);

    String sourceCode =
        "goog.scope(function() {" +
            "  var x = 1;" +
            "});";

    List<Node> abstractSyms = Lists.newArrayList();
    Node root = Node.createTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(0, aliasDefinitions.size());
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);

    assertEquals(Node.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    compiler.run(
        root,
        null,
        errorCollector,
        null,
        options,
        null,
        null,
        null,
        null);

    assertFalse(errorCollector.hasErrors());
  }

  @org.junit.Test
  public void testScopedAliasesImproperlyUsed() {
    Compiler compiler = new Compiler();
    ErrorCollector errorCollector = new ErrorCollector();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);

    String sourceCode =
        "goog.scope(function() {" +
            "  goog.scope(function() {});" +
            "});";

    List<Node> abstractSyms = Lists.newArrayList();
    Node root = Node.createTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(0, aliasDefinitions.size());
    assertEquals(1, scopeCalls.size());

    Node scopeCall1 = scopeCalls.get(0);

    assertEquals(Node.CALL, scopeCall1.getType());
    assertEquals("goog.scope", scopeCall1.getFirstChild().getQualifiedName());

    Node scopeCall2 = scopeCall1.getLastChild();

    assertEquals(Node.SCOPING_FUNCTION, scopeCall2.getType());

    compiler.run(
        root,
        null,
        errorCollector,
        null,
        options,
        null,
        null,
        null,
        null);

    assertTrue(errorCollector.hasErrors());
    List<DiagnosticType> errors = errorCollector.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(
        GOOG_SCOPE_USED_IMPROPERLY, errors.get(0).getType());
  }

  @org.junit.Test
  public void testScopedAliasesMultipleAssignments() {
    Compiler compiler = new Compiler();
    ErrorCollector errorCollector = new ErrorCollector();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);

    String sourceCode =
        "goog.scope(function() {" +
            "  var x = goog.foo;" +
            "  var x = goog.bar;" +
            "});";

    List<Node> abstractSyms = Lists.newArrayList();
    Node root = Node.createTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(2, aliasDefinitions.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinitionX = aliasDefinitions.get(0);
    Node aliasDefinitionY = aliasDefinitions.get(1);
    Node scopeCall = scopeCalls.get(0);

    assertEquals(Node.SCOPING_FUNCTION, aliasDefinitionX.getType());
    assertEquals("goog.foo", aliasDefinitionX.getQualifiedName());
    assertEquals("goog.foo", NodeUtil.getFunctionName(aliasDefinitionX));

    assertEquals(Node.SCOPING_FUNCTION, aliasDefinitionY.getType());
    assertEquals("goog.bar", aliasDefinitionY.getQualifiedName());
    assertEquals("goog.bar", NodeUtil.getFunctionName(aliasDefinitionY));

    assertEquals(Node.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    compiler.run(
        root,
        null,
        errorCollector,
        null,
        options,
        null,
        null,
        null,
        null);

    assertTrue(errorCollector.hasErrors());
    List<DiagnosticType> errors = errorCollector.getAllErrors();
    assertEquals(1, errors.size());
    assertEquals(
        GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0).getType());
  }

  private int aliasUsageCount(Node aliasDefinition) {
    Traversal traversal = new Traversal();
    int aliasUsageCount = 0;

    for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
      Node aliasNode = ((AliasedNode) aliasUsage).aliasReference;
      if (aliasDefinition.equals(aliasNode.getParent())) {
        aliasUsageCount++;
      }
    }

    return aliasUsageCount;
  }
}