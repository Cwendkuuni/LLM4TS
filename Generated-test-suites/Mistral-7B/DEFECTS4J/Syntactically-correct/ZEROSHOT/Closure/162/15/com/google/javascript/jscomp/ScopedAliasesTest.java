package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

/**
 * Tests for {@link ScopedAliases} class.
 *
 * @author robbyw@google.com (Robby Walker)
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    transformationHandler = new TestAliasTransformationHandler();
    options.setAliasTransformationHandler(transformationHandler);
    compiler = new Compiler(options);
  }

  @Test
  public void testBasicScopedAliases() {
    String testSource =
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<AliasTransformation> transformations =
        transformationHandler.getTransformations();
    assertEquals(1, transformations.size());
    AliasTransformation transformation = transformations.get(0);
    assertEquals("dom", transformation.getAlias());
    assertEquals("goog.dom", transformation.getReplacement());
  }

  @Test
  public void testMultipleScopedAliases() {
    String testSource =
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var $ = goog.dom.getElementsById; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "  var divs = $('divs'); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<AliasTransformation> transformations =
        transformationHandler.getTransformations();
    assertEquals(2, transformations.size());
    AliasTransformation domTransformation = transformations.get(0);
    assertEquals("dom", domTransformation.getAlias());
    assertEquals("goog.dom", domTransformation.getReplacement());

    AliasTransformation $Transformation = transformations.get(1);
    assertEquals("$", $Transformation.getAlias());
    assertEquals("goog.dom.getElementsById", $Transformation.getReplacement());
  }

  @Test
  public void testNestedScopedAliases() {
    String testSource =
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  goog.scope(function() {" +
        "    var DIV = dom.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  }); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<AliasTransformation> transformations =
        transformationHandler.getTransformations();
    assertEquals(0, transformations.size());
  }

  @Test
  public void testScopedAliasWithInvalidFunction() {
    String testSource =
        "goog.scope(function() {" +
        "  function foo() {} " +
        "  goog.scope(function() {" +
        "    var DIV = goog.dom.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  }); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("TypeError: goog.scope is not a function", errors.get(0));
  }

  @Test
  public void testScopedAliasWithInvalidArguments() {
    String testSource =
        "goog.scope(function() {" +
        "  goog.scope(function(foo) {" +
        "    var DIV = goog.dom.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  }); " +
        "}, 42);";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("goog.scope function should have no arguments", errors.get(0));
  }

  @Test
  public void testScopedAliasWithReturnStatement() {
    String testSource =
        "goog.scope(function() {" +
        "  function foo() {" +
        "    return; " +
        "  } " +
        "  goog.scope(function() {" +
        "    var DIV = goog.dom.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  }); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("TypeError: Return statement is not allowed in goog.scope function",
        errors.get(0));
  }

  @Test
  public void testScopedAliasWithThrowStatement() {
    String testSource =
        "goog.scope(function() {" +
        "  function foo() {" +
        "    throw new Error('Test'); " +
        "  } " +
        "  goog.scope(function() {" +
        "    var DIV = goog.dom.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  }); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("TypeError: throw statement is not allowed in goog.scope function",
        errors.get(0));
  }

  @Test
  public void testScopedAliasWithProxy() {
    String testSource =
        "goog.scope(function() {" +
        "  var goog = {dom: {TagName: {DIV: null}}}; " +
        "  goog.scope(function() {" +
        "    goog.dom.TagName.DIV = 42; " +
        "  }); " +
        "  var div = goog.dom.createElement(goog.dom.TagName.DIV); " +
        "  assertEquals(42, div.tagName.value); " +
        "});";

    Node root = Node.parseTree(testSource);
    compiler.process(null, root);

    List<String> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());
  }
}

class TestAliasTransformationHandler implements AliasTransformationHandler {
  private final List<AliasTransformation> transformations =
      Lists.newArrayList();

  public void logAliasTransformation(
      String fileName, SourcePosition position, String alias, String replacement) {
    transformations.add(new AliasTransformation(fileName, position, alias, replacement));
  }

  public List<AliasTransformation> getTransformations() {
    return transformations;
  }
}