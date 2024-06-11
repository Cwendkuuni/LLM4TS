import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.AliasType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock private AbstractCompiler compiler;
  @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;
  private Traversal traversal;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    traversal = new Traversal();
  }

  @Test
  public void testValidScopedAlias() {
    Node root = createTestNode(
        "function foo() {",
        "  var goog = {};",
        "  goog.dom = {};",
        "  goog.dom.createElement = function(tagName) {",
        "    return document.createElement(tagName);",
        "  };",
        "  var element = goog.dom.createElement('div');",
        "}");

    scopedAliases.process(null, root);

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertEquals("goog.dom.createElement", aliasUsage.toString());

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
  }

  @Test
  public void testInvalidScopedAliasHasBadParameters() {
    Node root = createTestNode("function foo() { goog.scope('test'); }");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getName());
  }

  @Test
  public void testInvalidScopedAliasUsesThis() {
    Node root = createTestNode("function foo() { goog.scope(this); }");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS", error.getName());
  }

  @Test
  public void testInvalidScopedAliasUsesReturn() {
    Node root = createTestNode("function foo() { goog.scope(function() { return; }); }");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getName());
  }

  @Test
  public void testInvalidScopedAliasUsesThrow() {
    Node root = createTestNode("function foo() { goog.scope(function() { throw new Error(); }); }");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getName());
  }

  @Test
  public void testAliasRedefined() {
    Node root = createTestNode(
        "function foo() {",
        "  var goog = {};",
        "  goog.dom = {};",
        "  goog.dom.createElement = function(tagName) {",
        "    return document.createElement(tagName);",
        "  };",
        "  var goog = {'dom': function(tagName) { return document.createElement(tagName); }}",
        "}");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testNonAliasLocal() {
    Node root = createTestNode(
        "function foo() {",
        "  var goog = {};",
        "  goog.dom = {};",
        "  var x = {};",
        "}");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getName());
  }

  @Test
  public void testNamespaceShadow() {
    Node root = createTestNode(
        "function foo() {",
        "  var goog = {};",
        "  goog.dom = {};",
        "  goog.dom.createElement = function(tagName) {",
        "    return document.createElement(tagName);",
        "  };",
        "  var goog = {'dom': { createElement: function() {} }}");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testMultipleNamespaceShadows() {
    Node root = createTestNode(
        "function foo() {",
        "  var goog = {};",
        "  goog.dom = {};",
        "  goog.dom.createElement = function(tagName) {",
        "    return document.createElement(tagName);",
        "  };",
        "  var goog2 = {'dom': { createElement: function() {} }};",
        "}");

    scopedAliases.process(null, root);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getErrors().size());
    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testAliasTransformation() {
    Node root = createTestNode(
        "function foo() {",
        "  var goog = {};",
        "  goog.dom = {};",
        "  goog.dom.createElement = function(tagName) {",
        "    return document.createElement(tagName);",
        "  };",
        "  var element = goog.dom.createElement('div');",
        "}");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME, AliasType.ALL);

    scopedAliases.process(null, root);

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertEquals("goog.dom.createElement", aliasUsage.toString());

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog", aliasDefinition.getString());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    Node transformedRoot = traversal.getTransformedRoot();

    Node googNode = transformedRoot.getChildAtIndex(1);
    assertEquals("goog", googNode.getString());

    Node domNode = transformedRoot.getChildAtIndex(2);
    assertEquals("goog.dom", domNode.getString());

    Node createElementNode = transformedRoot.getChildAtIndex(3);
    assertEquals("goog.dom.createElement", createElementNode.getString());
  }

  private Node createTestNode(String... nodes) {
    Node root = new Node(Node.ROOT, null, null, null);
    for (String node : nodes) {
      Node n = NodeUtil.parseTree(node, null, null, null);
      root.addChild(n);
    }
    return root;
  }
}