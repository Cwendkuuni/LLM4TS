package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Preconditions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Test class for {@link ScopedAliases}
 */
@RunWith(JUnit4.class)
public class ScopedAliasesTest extends CompilationTestBase {
  @Test
  public void testNoGoogScope() {
    // No goog.scope() present
    String code = "function foo() {}";
    compileAndCheckNoErrors(code);
  }

  @Test
  public void testEmptyGoogScope() {
    // Empty goog.scope() present
    String code = "goog.scope(function() {});";
    compileAndCheckNoErrors(code);
  }

  @Test
  public void testBadGoogScopeUsage() {
    // goog.scope usage with improper syntax
    String code = "goog.scope();";
    compileAndExpectError(DiagnosticType.error("JSC_GOOG_SCOPE_USED_IMPROPERLY"));
  }

  @Test
  public void testBadGoogScopeParameters() {
    // goog.scope usage with improper parameters
    String code = "goog.scope(function foo() {});";
    compileAndExpectError(DiagnosticType.error("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS"));
  }

  @Test
  public void testGoogScopeWithOnlyThis() {
    // goog.scope usage with only 'this'
    String code = "goog.scope(function() { this; });";
    compileAndExpectError(DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS"));
  }

  @Test
  public void testGoogScopeWithReturn() {
    // goog.scope usage with 'return'
    String code = "goog.scope(function() { return; });";
    compileAndExpectError(DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN"));
  }

  @Test
  public void testGoogScopeWithThrow() {
    // goog.scope usage with 'throw'
    String code = "goog.scope(function() { throw new Error(); });";
    compileAndExpectError(DiagnosticType.error("JSC_GOOG_SCOPE_USES_THROW"));
  }

  @Test
  public void testGoogScopeWithAliasRedefinition() {
    // goog.scope usage with alias redefinition
    String code = "goog.scope(function() { var x = goog.x; var x = 1; });";
    compileAndExpectError(
        DiagnosticType.error("JSC_GOOG_SCOPE_ALIAS_REDEFINED", "x"));
  }

  @Test
  public void testGoogScopeWithNonAliasLocal() {
    // goog.scope usage with non-alias local variable
    String code = "goog.scope(function() { var x = 1; });";
    compileAndExpectError(
        DiagnosticType.error("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", "x"));
  }

  @Test
  public void testGoogScopeWithAlias() {
    // goog.scope usage with alias
    String code = "goog.scope(function() { var x = goog.x; });";
    compileAndCheckNoErrors();
    AbstractCompiler compiler = getLastCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, getDefaultAliasTransformationHandler());
    scopedAliases.process(null, getSourceUnit("Test.java").getTree());
    assertFalse(scopedAliases.hasErrors());
    Node root = getSourceUnit("Test.java").getTree();
    Node googScopeCall = findNode(root, "goog.scope");
    Node googScopeBlock = googScopeCall.getLastChild();
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasDefinitions.size() > 0);
    assertTrue(scopeCalls.size() > 0);
    assertTrue(aliasUsages.size() > 0);
    Node x = findNode(googScopeBlock, "x");
    assertEquals(x, aliasUsages.get(0).aliasReference);
    assertEquals(findNode(googScopeBlock, "goog.x"), aliasUsages.get(0).aliasDefinition);
  }

  @Test
  public void testGoogScopeWithAliasInFunction() {
    // goog.scope usage with alias inside a function
    String code = "goog.scope(function() {" +
        "  function foo() { var x = goog.x; } " +
        "});";
    compileAndCheckNoErrors();
    AbstractCompiler compiler = getLastCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, getDefaultAliasTransformationHandler());
    scopedAliases.process(null, getSourceUnit("Test.java").getTree());
    assertFalse(scopedAliases.hasErrors());
    Node root = getSourceUnit("Test.java").getTree();
    Node googScopeCall = findNode(root, "goog.scope");
    Node googScopeBlock = googScopeCall.getLastChild();
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasDefinitions.size() > 0);
    assertTrue(scopeCalls.size() > 0);
    assertTrue(aliasUsages.size() > 0);
    Node foo = findNode(root, "foo");
    Node xInFoo = findNode(foo, "x");
    assertEquals(xInFoo, aliasUsages.get(1).aliasReference);
    assertEquals(findNode(googScopeBlock, "goog.x"), aliasUsages.get(1).aliasDefinition);
  }

  @Test
  public void testGoogScopeWithAliasInFunctionAndReturnType() {
    // goog.scope usage with alias inside a function and return type
    String code = "goog.scope(function() {" +
        "  function foo() {" +
        "    return goog.x;" +
        "  }" +
        "});";
    compileAndCheckNoErrors();
    AbstractCompiler compiler = getLastCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, getDefaultAliasTransformationHandler());
    scopedAliases.process(null, getSourceUnit("Test.java").getTree());
    assertFalse(scopedAliases.hasErrors());
    Node root = getSourceUnit("Test.java").getTree();
    Node googScopeCall = findNode(root, "goog.scope");
    Node googScopeBlock = googScopeCall.getLastChild();
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasDefinitions.size() > 0);
    assertTrue(scopeCalls.size() > 0);
    assertTrue(aliasUsages.size() > 0);
    Node foo = findNode(root, "foo");
    Node returnNode = foo.getLastChild();
    Node x = findNode(googScopeBlock, "goog.x");
    assertEquals(x, aliasUsages.get(2).aliasReference);
    assertEquals(x, aliasUsages.get(2).aliasDefinition);
    assertEquals(returnNode, aliasUsages.get(2).node);
  }

  @Test
  public void testGoogScopeWithAliasAndTypeAlias() {
    // goog.scope usage with alias and type alias
    String code = "goog.scope(function() {" +
        "  var goog$Array = goog.Array;" +
        "  var x = goog$Array.TYPE; " +
        "});";
    compileAndCheckNoErrors();
    AbstractCompiler compiler = getLastCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, getDefaultAliasTransformationHandler());
    scopedAliases.process(null, getSourceUnit("Test.java").getTree());
    assertFalse(scopedAliases.hasErrors());
    Node root = getSourceUnit("Test.java").getTree();
    Node googScopeCall = findNode(root, "goog.scope");
    Node googScopeBlock = googScopeCall.getLastChild();
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasDefinitions.size() > 0);
    assertTrue(scopeCalls.size() > 0);
    assertTrue(aliasUsages.size() > 0);
    Node googArray = findNode(root, "goog.Array");
    Node x = findNode(googScopeBlock, "goog$Array.TYPE");
    assertEquals(x, aliasUsages.get(0).aliasReference);
    assertEquals(googArray, aliasUsages.get(0).aliasDefinition);
  }

  @Test
  public void testGoogScopeWithAliasesInNestedFunctions() {
    // goog.scope usage with aliases in nested functions
    String code = "goog.scope(function() {" +
        "  function foo() {" +
        "    goog.scope(function() {" +
        "      var x = goog.x;" +
        "      var y = goog.y;" +
        "    });" +
        "    function bar() {" +
        "      var z = goog.z;" +
        "    }" +
        "  }" +
        "});";
    compileAndCheckNoErrors();
    AbstractCompiler compiler = getLastCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, getDefaultAliasTransformationHandler());
    scopedAliases.process(null, getSourceUnit("Test.java").getTree());
    assertFalse(scopedAliases.hasErrors());
    Node root = getSourceUnit("Test.java").getTree();
    Node googScopeCall = findNode(root, "goog.scope");
    Node googScopeBlock = googScopeCall.getLastChild();
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasDefinitions.size() > 0);
    assertTrue(scopeCalls.size() > 0);
    assertTrue(aliasUsages.size() > 0);
    Node foo = findNode(root, "foo");
    Node googScopeInFoo = findNode(foo, "goog.scope");
    Node googScopeBlockInFoo = googScopeInFoo.getLastChild();
    Node x = findNode(googScopeBlockInFoo, "goog.x");
    assertEquals(x, aliasUsages.get(1).aliasReference);
    assertEquals(findNode(googScopeBlock, "goog.x"), aliasUsages.get(1).aliasDefinition);
    Node bar = findNode(foo, "bar");
    Node googScopeInBar = findNode(bar, "goog.scope");
    Node googScopeBlockInBar = googScopeInBar.getLastChild();
    Node z = findNode(googScopeBlockInBar, "goog.z");
    assertEquals(z, aliasUsages.get(3).aliasReference);
    assertEquals(findNode(googScopeBlock, "goog.z"), aliasUsages.get(3).aliasDefinition);
  }

  private AbstractCompiler getLastCompiler() {
    return (AbstractCompiler) getCompilationManager().getLatestCompilationPassManager()
        .getPasses()
        .get(getCompilationManager().getLatestCompilationPassManager().size() - 1);
  }

  private Node findNode(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isQualifiedName() && child.getString().equals(name)) {
        return child;
      }
      Node foundNode = findNode(child, name);
      if (foundNode != null) {
        return foundNode;
      }
    }
    return null;
  }

  private static AliasTransformationHandler getDefaultAliasTransformationHandler() {
    return new AliasTransformationHandler() {};
  }
}