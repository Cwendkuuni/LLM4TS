package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.GOOG_SCOPE_ALIAS;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.GOOG_SCOPE_NON_ALIAS;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScopedAliasesTest {
  private Compiler compiler;

  @Before
  public void setup() {
    compiler = new Compiler();
  }

  @Test
  public void testScopedAliases() {
    SourceFile input = new TestSourceFile("TestScopedAliases.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV; " +
            "  dom.createElement(DIV); " +
            "});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {
      @Override
      public void processAliases(List<AliasTransformation> transformations) {
        for (AliasTransformation alias : transformations) {
          System.out.println("Alias: " + alias.getAlias() + " => " +
              alias.getAliasDefinition());
        }
      }
    });

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node googScopeCall = NodeOperations.findDescendant(root, "goog.scope");
    assertEquals(2, googScopeCall.getChildCount());

    Node functionNode = googScopeCall.getChildAtIndex(1);
    assertEquals(1, functionNode.getChildCount());

    Node bodyNode = functionNode.getChildAtIndex(0);
    assertEquals(1, bodyNode.getChildCount());

    Node elementCreationCall = bodyNode.getChildAtIndex(0);
    assertEquals("dom.createElement", elementCreationCall.toString());

    assertEquals("DIV", elementCreationCall.getLastChild().toString());
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    SourceFile input = new TestSourceFile("TestScopedAliasesInvalidCall.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV;" +
            "});goog.scope(function() {});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node firstGoogScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(2, firstGoogScopeCall.getChildCount());

    assertEquals(1, firstGoogScopeCall.getChildAtIndex(1).getChildCount());

    Node secondGoogScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(1, secondGoogScopeCall.getChildCount());

    assertEquals(1, firstGoogScopeCall.getChildAtIndex(0).getChildCount());

    compiler.reportErrors();

    List<String> errors = compiler.getDiagnostics().getAllErrors();
    assertEquals(1, errors.size());

    assertEquals(
        "The call to goog.scope must be alone in a single statement.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithInvalidFunction() {
    SourceFile input = new TestSourceFile("TestScopedAliasesInvalidFunction.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV;" +
            "});function test(){}");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node googScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(2, googScopeCall.getChildCount());

    assertEquals(1, googScopeCall.getChildAtIndex(1).getChildCount());

    compiler.reportErrors();

    List<String> errors = compiler.getDiagnostics().getAllErrors();
    assertEquals(1, errors.size());

    assertEquals(
        "The call to goog.scope must take only a single parameter.  It must" +
            " be an anonymous function that itself takes no parameters.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithAliasDefinition() {
    SourceFile input = new TestSourceFile("TestScopedAliasesWithAliasDefinition.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = goog.dom.TagName.DIV;" +
            "});" +
            "var d = DIV;");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node googScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(2, googScopeCall.getChildCount());

    assertEquals(1, googScopeCall.getChildAtIndex(1).getChildCount());

    Node aliasDefinition = NodeOperations.findDescendant(root, "d");

    assertEquals(1, aliasDefinition.getChildCount());

    compiler.reportErrors();

    List<String> errors = compiler.getDiagnostics().getAllErrors();
    assertEquals(1, errors.size());

    assertEquals(
        "The local variable d is in a goog.scope and is not an alias.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithAliasRedefinition() {
    SourceFile input = new TestSourceFile("TestScopedAliasesWithAliasRedefinition.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV;" +
            "});" +
            "goog.scope(function() {" +
            "  var DIV = 'newValue';" +
            "});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node firstGoogScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(2, firstGoogScopeCall.getChildCount());

    assertEquals(1, firstGoogScopeCall.getChildAtIndex(1).getChildCount());

    Node secondGoogScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(1, secondGoogScopeCall.getChildCount());

    assertEquals(1, firstGoogScopeCall.getChildAtIndex(0).getChildCount());

    compiler.reportErrors();

    List<String> errors = compiler.getDiagnostics().getAllErrors();
    assertEquals(1, errors.size());

    assertEquals(
        "The alias DIV is assigned a value more than once.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithReturn() {
    SourceFile input = new TestSourceFile("TestScopedAliasesWithReturn.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV;" +
            "  return DIV;" +
            "});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node googScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(2, googScopeCall.getChildCount());

    assertEquals(1, googScopeCall.getChildAtIndex(1).getChildCount());

    compiler.reportErrors();

    List<String> errors = compiler.getDiagnostics().getAllErrors();
    assertEquals(1, errors.size());

    assertEquals(
        "The body of a goog.scope function cannot use 'return'.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithThrow() {
    SourceFile input = new TestSourceFile("TestScopedAliasesWithThrow.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV;" +
            "  throw new Error('test');" +
            "});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);

    compiler.compile(input, options, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);
    Node googScopeCall = NodeOperations.findDescendant(root, "goog.scope");

    assertEquals(2, googScopeCall.getChildCount());

    assertEquals(1, googScopeCall.getChildAtIndex(1).getChildCount());

    compiler.reportErrors();

    List<String> errors = compiler.getDiagnostics().getAllErrors();
    assertEquals(1, errors.size());

    assertEquals(
        "The body of a goog.scope function cannot use 'throw'.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithHotSwap() {
    TestSourceFile input = new TestSourceFile("TestScopedAliasesHotSwap.js",
        "goog.scope(function() {" +
            "  var dom = goog.dom; " +
            "  var DIV = dom.TagName.DIV;" +
            "});" +
            "function test(){" +
            "  var e = dom.createElement(DIV);" +
            "};");

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, null);

    Node node = compiler.parse(input);

    scopedAliases.process(null, node);

    compiler.compile(node, null, null);

    List<Node> tree = compiler.getResultTree();

    assertEquals(1, tree.size());

    Node root = tree.get(0);

    Node functionDeclaration = NodeOperations.findDescendant(root, "test");

    assertEquals(1, functionDeclaration.getChildCount());

    Node elementCreationCall = functionDeclaration.getChildAtIndex(0);

    assertEquals(1, elementCreationCall.getChildCount());

    Node aliasDefinition = NodeOperations.findDescendant(root, "DIV");

    assertEquals(1, aliasDefinition.getChildCount());

    assertEquals("dom.createElement", elementCreationCall.toString());

    assertEquals("DIV", aliasDefinition.toString());
  }
}