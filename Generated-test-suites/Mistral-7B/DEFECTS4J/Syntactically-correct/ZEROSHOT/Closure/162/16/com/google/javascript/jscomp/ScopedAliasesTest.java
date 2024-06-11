package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private Node root;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    compiler = new Compiler(options);

    SourceFile sourceFile = new SourceFile("Test.js");
    root = NodeUtil.parseJsFile(sourceFile, "Test.js");
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, nodeTraversal);

    assertTrue(nodeTraversal.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(5, diagnostics.size());

    List<Node> aliasDefinitionsInOrder = nodeTraversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertSame(root.getChildAtIndex(1), aliasDefinitionsInOrder.get(0));

    List<AliasUsage> aliasUsages = nodeTraversal.getAliasUsages();
    assertEquals(3, aliasUsages.size());

    List<Node> scopeCalls = nodeTraversal.getScopeCalls();
    assertEquals(0, scopeCalls.size());

    assertEquals("DIV", aliasDefinitionsInOrder.get(0).getLastChild().getString());
    assertEquals("dom", aliasUsages.get(0).aliasDefinition.getString());
    assertEquals("goog.dom", aliasUsages.get(1).aliasDefinition.getString());
    assertEquals("TagName", aliasUsages.get(2).aliasDefinition.getString());
  }

  @Test
  public void testScopedAliasesWithoutAliasTransformation() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    NodeTraversal.traverse(compiler, root, nodeTraversal);

    assertFalse(nodeTraversal.hasErrors());

    List<Node> aliasDefinitionsInOrder = nodeTraversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    List<AliasUsage> aliasUsages = nodeTraversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> scopeCalls = nodeTraversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertSame(root.getChildAtIndex(1), scopeCalls.get(0));
  }

  @Test
  public void testScopedAliasesWithImproperUsage() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  if (true) {\n" +
        "    goog.scope(function() {\n" +
        "      dom.createElement(DIV);\n" +
        "    });\n" +
        "  }\n" +
        "});";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType());
    assertEquals(1, diagnostic.getSourceFile().getLineNumber());
    assertEquals(4, diagnostic.getSourceFile().getCharacterNumber());
  }

  @Test
  public void testScopedAliasesWithNonFunctionParamter() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(123);";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostic.getType());
    assertEquals(1, diagnostic.getSourceFile().getLineNumber());
    assertEquals(1, diagnostic.getSourceFile().getCharacterNumber());
  }

  @Test
  public void testScopedAliasesWithReferenceToThis() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(function() {\n" +
        "  this.test = 42;\n" +
        "});";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    thrown.expect(AssertionError.class);
    thrown.expectMessage("The body of a goog.scope function cannot reference 'this'.");
    nodeTraversal.traverse(compiler, testNode, null);
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(function() {\n" +
        "  return 42;\n" +
        "});";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    thrown.expect(AssertionError.class);
    thrown.expectMessage("The body of a goog.scope function cannot use 'return'.");
    nodeTraversal.traverse(compiler, testNode, null);
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(function() {\n" +
        "  throw new Error('Test');\n" +
        "});";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    thrown.expect(AssertionError.class);
    thrown.expectMessage("The body of a goog.scope function cannot use 'throw'.");
    nodeTraversal.traverse(compiler, testNode, null);
  }

  @Test
  public void testScopedAliasesWithAliasRedefined() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(function() {\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  var DIV = 123;\n" +
        "});";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType());
    assertEquals("DIV", diagnostic.getMessage(0));
    assertEquals(1, diagnostic.getSourceFile().getLineNumber());
    assertEquals(10, diagnostic.getSourceFile().getCharacterNumber());
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);

    compiler = new Compiler(options);
    SourcePosition position = new SourcePosition<>();

    String testCode = "goog.scope(function() {\n" +
        "  var test = 42;\n" +
        "});";
    Node testNode = NodeUtil.parseJsString(testCode, "Test.js");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(scopedAliases);
    nodeTraversal.traverse(compiler, testNode, null);

    thrown.expect(AssertionError.class);
    thrown.expectMessage("The local variable test is in a goog.scope and is not an alias.");
    nodeTraversal.traverse(compiler, testNode, null);
  }
}