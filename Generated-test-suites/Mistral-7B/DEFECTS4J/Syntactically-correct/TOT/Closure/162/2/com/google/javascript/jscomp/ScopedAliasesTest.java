package com.google.javascript.jscomp.tests;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import static com.google.javascript.rhino.Node.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testSimpleAlias() {
    String input = "goog.scope(function() {" +
        "  var goog = {dom: {createElement: function(tagName) {" +
        "    return document.createElement(tagName);" +
        "  }}};" +
        "  goog.dom.createElement('div');" +
        "});";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test
    scopedAliases.process(null, root);

    // Check the result
    Node expected = makeNode("goog.dom.createElement");
    Node actual = root.getFunctionBody();
    NodeUtil.assertNodeEquals(expected, actual);
  }

  @Test
  public void testNestedAlias() {
    String input = "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      createElement: function(tagName) {" +
        "        return document.createElement(tagName);" +
        "      }," +
        "      createElementWithClass: function(tagName, className) {" +
        "        var element = this.createElement(tagName);" +
        "        element.className = className;" +
        "        return element;" +
        "      }" +
        "    }," +
        "    logger: {" +
        "      log: function() {" +
        "        // Do nothing" +
        "      }" +
        "    }" +
        "  };" +
        "  goog.dom.createElementWithClass('div', 'test');" +
        "});";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test
    scopedAliases.process(null, root);

    // Check the result
    Node expected = makeNode("goog.dom.createElementWithClass");
    Node actual = root.getFunctionBody();
    NodeUtil.assertNodeEquals(expected, actual);
  }

  @Test
  public void testMultipleAliases() {
    String input = "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      createElement: function(tagName) {" +
        "        return document.createElement(tagName);" +
        "      }," +
        "      createElementWithClass: function(tagName, className) {" +
        "        var element = this.createElement(tagName);" +
        "        element.className = className;" +
        "        return element;" +
        "      }," +
        "      getElementById: function(id) {" +
        "        return document.getElementById(id);" +
        "      }" +
        "    }," +
        "    logger: {" +
        "      log: function() {" +
        "        // Do nothing" +
        "      }" +
        "    }" +
        "  };" +
        "  goog.dom.createElement('div');" +
        "  goog.dom.createElementWithClass('div', 'test');" +
        "  goog.logger.log('Hello, world!');" +
        "  document.getElementById('test').innerHTML = 'Hello, world!';" +
        "});";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test
    scopedAliases.process(null, root);

    // Check the result
    Node expected = makeNode("goog.dom.createElement");
    Node elementCreate = root.getFunctionBody().getFirstChild();
    NodeUtil.assertNodeEquals(expected, elementCreate);

    expected = makeNode("goog.dom.createElementWithClass");
    Node elementCreateWithClass = elementCreate.getNext();
    NodeUtil.assertNodeEquals(expected, elementCreateWithClass);

    expected = makeNode("document.getElementById");
    Node documentGetElementById =
        elementCreateWithClass.getLastChild().getLastChild();
    NodeUtil.assertNodeEquals(expected, documentGetElementById);
  }

  @Test
  public void testAliasErrorNoFunction() {
    String input = "goog.scope(function() {" +
        "  var goog = {dom: {createElement: function() {}}, logger: {" +
        "      log: function() {" +
        "        // Do nothing" +
        "      }}};";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalArgumentException.class);
    IllegalArgumentException ex = (IllegalArgumentException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The call to goog.scope must be alone in a single statement.");
  }

  @Test
  public void testAliasErrorFunctionWithParam() {
    String input = "goog.scope(function(x) {" +
        "  var goog = {dom: {createElement: function(tagName) {" +
        "    return document.createElement(tagName);" +
        "  }}};";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalArgumentException.class);
    IllegalArgumentException ex = (IllegalArgumentException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The call to goog.scope must take only a single parameter.  It must be an anonymous function that itself takes no parameters.");
  }

  @Test
  public void testAliasErrorThisReference() {
    String input = "goog.scope(function() {" +
        "  var goog = {dom: {createElement: function() {" +
        "    this.createElement('div');" +
        "  }}};";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalArgumentException.class);
    IllegalArgumentException ex = (IllegalArgumentException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The body of a goog.scope function cannot reference 'this'.");
  }

  @Test
  public void testAliasErrorReturn() {
    String input = "goog.scope(function() {" +
        "  var goog = {dom: {createElement: function() {" +
        "    return; // Should not be here!" +
        "  }}};";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalArgumentException.class);
    IllegalArgumentException ex = (IllegalArgumentException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The body of a goog.scope function cannot use 'return'.");
  }

  @Test
  public void testAliasErrorThrow() {
    String input = "goog.scope(function() {" +
        "  var goog = {dom: {createElement: function() {" +
        "    throw new Error('Something went wrong!');" +
        "  }}};";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalArgumentException.class);
    IllegalArgumentException ex = (IllegalArgumentException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The body of a goog.scope function cannot use 'throw'.");
  }

  @Test
  public void testAliasErrorAliasRedefinition() {
    String input = "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      createElement: function(tagName) {" +
        "        return document.createElement(tagName);" +
        "      }," +
        "      createElement: function(tagName) {" +
        "        return document.createElement(tagName);" + // Redefinition
        "      }" +
        "    }" +
        "  };";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalStateException.class);
    IllegalStateException ex = (IllegalStateException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The alias goog.dom.createElement is assigned a value more than once.");
  }

  @Test
  public void testAliasErrorNonAliasLocal() {
    String input = "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      createElement: function(tagName) {" +
        "        return document.createElement(tagName);" +
        "      }," +
        "      createElement: function(tagName) {" +
        "        var element = this.createElement(tagName);" + // Should not be here!
        "        element.innerHTML = 'Hello, world!';" +
        "      }" +
        "    }," +
        "    element: document.createElement('div')" + // Non-alias local
        "  };";

    Compiler compiler = new Compiler();
    Scope.Root root = compiler.parseTree(input);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Run the test and expect an error
    scopedAliases.process(null, root);

    // Check the diagnostics
    List<String> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics).hasSize(1);
    assertThat(diagnostics.get(0)).isInstanceOf(IllegalStateException.class);
    IllegalStateException ex = (IllegalStateException) diagnostics.get(0);
    assertThat(ex.getMessage()).isEqualTo("The local variable element is in a goog.scope and is not an alias.");
  }

  private Node makeNode(String name) {
    return makeNode(name, null);
  }

  private Node makeNode(String name, Node value) {
    if (value == null) {
      return makeName(name);
    }
    return makeCall(makeName("goog.dom"), "createElement", makeName(name), value);
  }

  private Node makeName(String name) {
    SourcePosition position =
        new SourcePosition(new SourceFile("Test.java", 1, 1), "");
    return new Name(name, position, null);
  }

  private Node makeCall(Node callee, String methodName, Node... arguments) {
    SourcePosition position =
        new SourcePosition(new SourceFile("Test.java", 1, 1), "");
    CallExpr call = new CallExpr(callee, methodName, arguments, position);
    return call;
  }
}