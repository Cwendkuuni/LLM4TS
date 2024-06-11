package com.google.javascript.jscomp.tests;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String js = "" +
        "goog.scope(function() {" +
        "  var foo = goog.foo; " +
        "  var bar = goog.bar; " +
        "  var baz = goog.baz; " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.MANUAL;

    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> expected = Lists.newArrayList(
        "goog.foo = foo;",
        "goog.bar = bar;",
        "goog.baz = baz;");

    List<String> actual = Lists.newArrayList();
    NodeUtil.forEachTree(root, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        if (n.isAssign()) {
          String left = NodeUtil.getLeftHandSideOfAssignment(n).getString();
          if (left.startsWith("goog.")) {
            actual.add(n.toString());
          }
        }
        t.traverseChildren(n);
      }
    });

    assertEquals(expected, actual);
  }

  @Test
  public void testScopedAliasesWithNoAliases() {
    String js = "" +
        "goog.scope(function() {" +
        "  var x = 1; " +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    assertTrue("No errors or warnings were reported.",
        compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testScopedAliasesWithBadParams() {
    String js = "" +
        "goog.scope(function(foo, bar) {" +
        "  // body" +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> errors = Lists.newArrayList();
    for (DiagnosticType error : compiler.getDiagnostics()) {
      errors.add(error.getMessage());
    }

    assertEquals(1, errors.size());
    assertTrue(errors.get(0).contains("The call to goog.scope must take only" +
        " a single parameter. It must be an anonymous function that itself takes" +
        " no parameters."));
  }

  @Test
  public void testScopedAliasesWithImproperUsage() {
    String js = "" +
        "goog.scope(function() {" +
        "  var x = 1;" +
        "  return x;" +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> errors = Lists.newArrayList();
    for (DiagnosticType error : compiler.getDiagnostics()) {
      errors.add(error.getMessage());
    }

    assertEquals(3, errors.size());
    assertTrue(errors.get(0).contains("The call to goog.scope must be alone" +
        " in a single statement."));
    assertTrue(errors.get(1).contains("The body of a goog.scope function" +
        " cannot reference 'this'."));
    assertTrue(errors.get(2).contains("The body of a goog.scope function" +
        " cannot use 'return'."));
  }

  @Test
  public void testScopedAliasesWithRedefinition() {
    String js = "" +
        "goog.scope(function() {" +
        "  var foo = 'bar';" +
        "  var foo = 1;" +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> errors = Lists.newArrayList();
    for (DiagnosticType error : compiler.getDiagnostics()) {
      errors.add(error.getMessage());
    }

    assertEquals(1, errors.size());
    assertTrue(errors.get(0).contains("The alias foo is assigned a value more than once."));
  }

  @Test
  public void testScopedAliasesWithInvalidAlias() {
    String js = "" +
        "goog.scope(function() {" +
        "  var this = goog.this;" +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> errors = Lists.newArrayList();
    for (DiagnosticType error : compiler.getDiagnostics()) {
      errors.add(error.getMessage());
    }

    assertEquals(1, errors.size());
    assertTrue(errors.get(0).contains("The body of a goog.scope function" +
        " cannot reference 'this'."));
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    String js = "" +
        "goog.scope(function() {" +
        "  throw new Error('Test');" +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> errors = Lists.newArrayList();
    for (DiagnosticType error : compiler.getDiagnostics()) {
      errors.add(error.getMessage());
    }

    assertEquals(1, errors.size());
    assertTrue(errors.get(0).contains("The body of a goog.scope function" +
        " cannot use 'throw'."));
  }

  @Test
  public void testScopedAliasesWithEmptyBlock() {
    String js = "" +
        "goog.scope(function() {});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> errors = Lists.newArrayList();
    for (DiagnosticType error : compiler.getDiagnostics()) {
      errors.add(error.getMessage());
    }

    assertEquals(0, errors.size());
  }

  @Test
  public void testScopedAliasesWithComplexBlock() {
    String js = "" +
        "goog.scope(function() {" +
        "  var foo = goog.foo;" +
        "  var bar = goog.bar;" +
        "  var baz = goog.baz;" +
        "  function qux() {" +
        "    var quux = goog.quux;" +
        "  }" +
        "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setSourceFileName("test.js");

    Node root = compiler.parseTree(js, null);
    HotSwapCompilerPass scopedAliasesPass =
        new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    List<String> expected = Lists.newArrayList(
        "goog.foo = foo;",
        "goog.bar = bar;",
        "goog.baz = baz;");

    List<String> actual = Lists.newArrayList();
    NodeUtil.forEachTree(root, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        if (n.isAssign()) {
          String left = NodeUtil.getLeftHandSideOfAssignment(n).getString();
          if (left.startsWith("goog.")) {
            actual.add(n.toString());
          }
        }
        t.traverseChildren(n);
      }
    });

    assertEquals(expected, actual);
  }
}