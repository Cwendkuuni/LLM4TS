package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ScopedAliasesTest {
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(null, null, transformationHandler);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldApplyAlias() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV; " +
            "  dom.createElement(DIV); " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    Node result = rootNode.getFirstChild();

    assertEquals(
        1,
        result.getChildCount(),
        "The root node should only contain the goog.scope call.");

    Node googScopeCall = result.getChildAtIndex(0);
    assertEquals(
        3,
        googScopeCall.getChildCount(),
        "The goog.scope call should have exactly 3 children: " +
            "goog.scope, anonymous function, and {} block.");

    Node anonymousFunction = googScopeCall.getChildAtIndex(1);
    assertEquals(
        1,
        anonymousFunction.getChildCount(),
        "The anonymous function should only contain a single statement.");

    Node statement = anonymousFunction.getChildAtIndex(0);
    assertEquals(
        2,
        statement.getChildCount(),
        "The statement should contain a variable declaration and an expression.");

    Node variableDeclaration = statement.getChildAtIndex(0);
    assertEquals(
        "dom",
        variableDeclaration.getString(),
        "The variable declaration should be for 'dom'.");

    Node expression = statement.getChildAtIndex(1);
    assertEquals(
        2,
        expression.getChildCount(),
        "The expression should contain a tagName lookup and a call to createElement.");

    Node tagNameLookup = expression.getChildAtIndex(0);
    assertEquals(
        "DIV",
        tagNameLookup.getString(),
        "The tagName lookup should be for 'DIV'.");

    Node createElementCall = expression.getChildAtIndex(1);
    assertEquals(
        2,
        createElementCall.getChildCount(),
        "The createElement call should only contain the argument and the keyword 'new'.");

    Node argument = createElementCall.getChildAtIndex(0);
    assertEquals(
        "goog.dom.TagName.DIV",
        argument.getQualifiedName(),
        "The argument to createElement should be the qualified name of the tag name.");
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallIsNotAlone() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV; " +
            "  dom.createElement(DIV); " +
            "  alert('Hello world!'); " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the goog.scope
    // call is not alone in a single statement.
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallHasBadParameters() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(" +
            "  function(a, b) {" +
            "    var dom = goog.dom; " +
            "    var DIV = dom.TagName.DIV; " +
            "    dom.createElement(DIV); " +
            "  }" +
            ");",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the goog.scope
    // call has bad parameters.
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallReferencesThis() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  this.dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV; " +
            "  dom.createElement(DIV); " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the goog.scope
    // call references 'this'.
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallUsesReturn() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  return goog.dom.createElement(goog.dom.TagName.DIV); " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the goog.scope
    // call uses 'return'.
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallUsesThrow() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  throw new Error('Something went wrong!'); " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the goog.scope
    // call uses 'throw'.
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallAssignsAliasTwice() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV; " +
            "  var DIV2 = dom.TagName.DIV; " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the alias 'DIV'
    // is assigned a value more than once.
  }

  @Test(expected = AssertionError.class)
  public void shouldFailIfScopeCallHasNonAliasLocalVariable() {
    Node rootNode = NodeUtil.parseJsFile(
        "test/ScopedAliasesTest.test.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var myVar = 'hello world'; " +
            "  var DIV = dom.TagName.DIV; " +
            "  dom.createElement(DIV); " +
            "});",
        null);

    scopedAliases.process(null, rootNode);

    // This test should fail with an AssertionError because the local
    // variable 'myVar' is in a goog.scope and is not an alias.
  }
}