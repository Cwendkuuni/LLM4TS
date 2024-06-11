package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.ScopedAliases;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    scopedAliases.process(null, root);

    Node expectedOutput = new Node();
    expectedOutput.setType(Token.BLOCK);
    Node expectedStatement = new Node();
    expectedStatement.setType(Token.EXPR_STATEMENT);
    Node expectedExpression = new Node();
    expectedExpression.setType(Token.CALL);
    expectedExpression.setProperty("callee", new Node("goog.dom"));
    expectedExpression.setProperty("arguments", new Node("TagName.DIV"));
    expectedStatement.setProperty("expression", expectedExpression);
    expectedOutput.setChild(expectedStatement);

    assertEquals(expectedOutput.toSource(), root.toString());
  }

  @Test
  public void testInvalidScopedAliasCall() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x; " +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("x", aliasUsage.getString());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog.x", aliasDefinition.getQualifiedName());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testScopedAliasWithForbiddenName() {
    String input = "goog.scope(function() {" +
        "  var goog = {}; " +
        "  goog.x = 42; " +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversversal.getAliasUsages().size());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("goog", aliasUsage.getString());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog", aliasDefinition.getString());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getName());
  }

  @Test
  public void testScopedAliasWithInvalidParameters() {
    String input = "goog.scope(function(a, b) {" +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());

    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals(2, scopeCall.getChildCount());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getName());
  }

  @Test
  public void testScopedAliasWithFunctionBody() {
    String input = "goog.scope(function() {" +
        "  function foo() {}" +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());

    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals(1, scopeCall.getChildCount());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THIS", error.getName());
  }

  @Test
  public void testScopedAliasWithReturnStatement() {
    String input = "goog.scope(function() {" +
        "  return 42;" +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());

    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals(1, scopeCall.getChildCount());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getName());
  }

  @Test
  public void testScopedAliasWithThrowStatement() {
    String input = "goog.scope(function() {" +
        "  throw new Error();" +
        "});";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAliases(true);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = compiler.parseTree(input, null);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getScopeCalls().size());

    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals(1, scopeCall.getChildCount());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getName());
  }
}