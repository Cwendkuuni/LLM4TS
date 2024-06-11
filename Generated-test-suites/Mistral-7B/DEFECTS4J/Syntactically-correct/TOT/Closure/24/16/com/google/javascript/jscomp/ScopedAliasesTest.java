package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
  }

  @Test
  public void testValidScopedAliases() {
    // Given
    Node root = Node.newText("goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; " +
        "dom.createElement(DIV); })()");
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // No errors were reported, so nothing to check here
  }

  @Test(expected = AssertionError.class)
  public void testInvalidScopedAliasUsage() {
    // Given
    Node root = Node.newText("goog.scope(function() { " +
        "goog.scope(function() { var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; " +
        "dom.createElement(DIV); })(); " +
        "dom.createElement(DIV); })()");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // A single error was reported, so we expect the test to fail
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasRedefined() {
    // Given
    Node root = Node.newText("goog.scope(function() { " +
        "var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; " +
        "goog.scope(function() { var DIV = 'newDIV'; })(); " +
        "dom.createElement(DIV); })()");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // A single error was reported, so we expect the test to fail
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasNonAliasLocal() {
    // Given
    Node root = Node.newText("goog.scope(function() { " +
        "var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; " +
        "var div = document.createElement(DIV); })()");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // A single error was reported, so we expect the test to fail
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithReturn() {
    // Given
    Node root = Node.newText("goog.scope(function() { " +
        "return goog.dom.createElement(goog.dom.TagName.DIV); })()");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // A single error was reported, so we expect the test to fail
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasWithThrow() {
    // Given
    Node root = Node.newText("goog.scope(function() { " +
        "throw new Error('Something went wrong'); })()");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // A single error was reported, so we expect the test to fail
  }

  @Test
  public void testScopedAliasWithComplexParameter() {
    // Given
    Node root = Node.newText("goog.scope(function() { " +
        "function add(x, y) { " +
        "return x + y; } " +
        "goog.scope(function() { var DIV = goog.dom.TagName.DIV; " +
        "var div = document.createElement(DIV); " +
        "goog.scope(function() { " +
        "var sum = add(1, 2); }()); " +
        "document.body.appendChild(div); " +
        "div.innerHTML = sum; })(); })()");

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    scopedAliases.process(null, root);

    // Then
    // No errors were reported, and the inner HTML of the DIV should be "3"
    Scope scope = compiler.getContext().getCurrentScope();
    Var sumVar = scope.getVar("sum");
    Node sumNode = sumVar.getNode();
    assertThat(sumNode.getInt()).isEqualTo(3);
  }
}