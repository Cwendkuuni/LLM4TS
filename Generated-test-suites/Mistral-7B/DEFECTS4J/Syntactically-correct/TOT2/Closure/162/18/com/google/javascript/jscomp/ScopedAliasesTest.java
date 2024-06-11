import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

/**
 * Unit tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Setup
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALIASES_ONLY);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    // Test case 1: Basic usage
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Assertions
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals("dom", aliasDefinition.getString());
    assertEquals("goog.dom", NodeUtil.getQualifiedName(aliasDefinition.getFirstChild()));

    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    // Test case 2: Multiple aliases
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    scopeCalls = scopedAliases.getTraversal().getScopeCalls();

    // Assertions
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());

    Node domAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("dom", domAliasDefinition.getString());
    assertEquals("goog.dom", NodeUtil.getQualifiedName(domAliasDefinition.getFirstChild()));

    Node divAliasDefinition = aliasDefinitionsInOrder.get(1);
    assertEquals("DIV", divAliasDefinition.getString());
    assertEquals("goog.dom.TagName.DIV", NodeUtil.getQualifiedName(divAliasDefinition.getFirstChild()));

    Node scopeCall2 = scopeCalls.get(0);
    assertEquals(scopeCall, scopeCall2);

    // Test case 3: Incorrect usage of goog.scope
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var d = goog.scope(function() { return dom.createElement(DIV); });" +
        "  d.createElement(DIV);" +
        "});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    // Assertions
    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(1, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getTraversal().getScopeCalls().size());

    Node dScopeCall = scopedAliases.getTraversal().getScopeCalls().get(0);
    assertNotNull(dScopeCall);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY,
        scopedAliases.getTraversal().getError(dScopeCall));

    // Test case 4: Empty goog.scope
    sourceCode =
        "goog.scope(function() {});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    // Assertions
    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(0, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getTraversal().getScopeCalls().size());

    Node emptyScopeCall = scopedAliases.getTraversal().getScopeCalls().get(0);
    assertNotNull(emptyScopeCall);
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS,
        scopedAliases.getTraversal().getError(emptyScopeCall));

    // Test case 5: goog.scope with arguments
    sourceCode =
        "goog.scope(function(a, b) {" +
        "  var dom = goog.dom;" +
        "  dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    // Assertions
    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(0, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getTraversal().getScopeCalls().size());

    Node argumentsScopeCall = scopedAliases.getTraversal().getScopeCalls().get(0);
    assertNotNull(argumentsScopeCall);
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS,
        scopedAliases.getTraversal().getError(argumentsScopeCall));

    // Test case 6: goog.scope with return statement
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  return dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    // Assertions
    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(0, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getTraversal().getScopeCalls().size());

    Node returnScopeCall = scopedAliases.getTraversal().getScopeCalls().get(0);
    assertNotNull(returnScopeCall);
    assertEquals(GOOG_SCOPE_USES_RETURN,
        scopedAliases.getTraversal().getError(returnScopeCall));

    // Test case 7: goog.scope with throw statement
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  throw new Error('Test error');" +
        "});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    // Assertions
    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(0, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getTraversal().getScopeCalls().size());

    Node throwScopeCall = scopedAliases.getTraversal().getScopeCalls().get(0);
    assertNotNull(throwScopeCall);
    assertEquals(GOOG_SCOPE_USES_THROW,
        scopedAliases.getTraversal().getError(throwScopeCall));

    // Test case 8: Multiple aliases with the same name
    sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = {};" +
        "  dom.createElement = function() {};" +
        "  dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    root = NodeUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    // Assertions
    assertTrue(scopedAliases.getTraversal().hasErrors());
    assertEquals(1, scopedAliases.getTraversal().getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getTraversal().getScopeCalls().size());

    Node secondDomAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertNotNull(secondDomAliasDefinition);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED,
        scopedAliases.getTraversal().getError(secondDomAliasDefinition));
  }
}