package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.ScopedAliases;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Test
  public void testEmpty() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Mockito.mock(Node.class);
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testGoogScopeUsedImproperly() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { var x = 1; })");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.argThat(error -> error.getMessage().equals("The call to goog.scope must be alone in a single statement.")));
  }

  @Test
  public void testGoogScopeHasBadParameters() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { }, 1)");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.argThat(error -> error.getMessage().equals("The call to goog.scope must take only a single parameter. It must " +
            "be an anonymous function that itself takes no parameters.")));
  }

  @Test
  public void testGoogScopeRefersToThis() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { return this; })");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.argThat(error -> error.getMessage().equals("The body of a goog.scope function cannot reference 'this'.")));
  }

  @Test
  public void testGoogScopeUsesReturn() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { return 1; })");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.argThat(error -> error.getMessage().equals("The body of a goog.scope function cannot use 'return'.")));
  }

  @Test
  public void testGoogScopeUsesThrow() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { throw new Error(); })");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.argThat(error -> error.getMessage().equals("The body of a goog.scope function cannot use 'throw'.")));
  }

  @Test
  public void testGoogScopeAliasRedefined() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = Mockito.mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { var x = 1; var x = 2; })");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.times(1)).report(
        Mockito.argThat(error -> error.getMessage().equals("The alias x is assigned a value more than once.")));
  }

  @Test
  public void testGoogScopeNonAliasLocal() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = Mockito.mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { var x = 1; var y = 2; })");
    scopedAliases.process(null, root);
    Mockito.verify(compiler, Mockito.never()).report(
        Mockito.argThat(error -> error.getMessage().startsWith("The local variable y is in a goog.scope and is not an alias.")));
  }

  @Test
  public void testApplyAlias() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasReference = createNode("goog.x");
    Node aliasDefinition = createNode("goog.y");

    Node root = createNode("goog.scope(function() { var x = goog.x; })");
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, null);

    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    aliasUsage.applyAlias();

    assertEquals("goog.y", aliasReference.getString());
  }

  @Test
  public void testApplyAliasType() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasReference = createNode("goog.x.Foo");
    Node aliasDefinition = createNode("goog.y.Bar");

    Node root = createNode("goog.scope(function() { var x = goog.x; })");
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, null);

    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    aliasUsage.applyAlias();

    assertEquals("goog.y.Bar", aliasReference.getQualifiedName());
  }

  @Test
  public void testNoAliasDefinitions() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { var x = 1; goog.scope(function() { var y = 2; }); })");
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, null);
    assertTrue(traversal.hasErrors());
    assertFalse(traversal.getAliasDefinitionsInOrder().isEmpty());
  }

  @Test
  public void testNoAliasUsages() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { goog.scope(function() { var x = 1; }) })");
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, null);
    assertTrue(traversal.hasErrors());
    assertFalse(traversal.getAliasUsages().isEmpty());
  }

  @Test
  public void testCollapseScopes() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createNode("goog.scope(function() { var x = 1; goog.scope(function() { var y = 2; }) })");
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, null);

    String expected = "var x = 1;var y = 2";
    assertEquals(expected, root.getString());
  }

  @Test
  public void testCollapseScopesWithAliases() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = null;
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node aliasDefinition = createNode("goog.x = 1");
    Node root = createNode("goog.scope(function() { var x = goog.x; goog.scope(function() { var y = 2; goog.x = 3; }) })");
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    scopedAliases.hotSwapScript(root, null);

    AliasUsage aliasUsage = traversal.getAliasDefinitionsInOrder().get(0);
    aliasUsage.applyAlias();
    assertEquals("1", aliasDefinition.getString());

    String expected = "var x = 1;var y = 2;var x = 3";
    assertEquals(expected, root.getString());
  }

  private Node createNode(String code) {
    Node node = new Node();
    node.setString(code);
    return node;
  }
}