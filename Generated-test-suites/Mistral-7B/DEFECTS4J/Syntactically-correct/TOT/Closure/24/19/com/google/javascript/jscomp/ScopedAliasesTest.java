package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.jscomp.TestingUtils.*;
import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import junit.framework.Assert;

/**
 * Test for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    preprocessorSymbolTable = new PreprocessorSymbolTable();
  }

  @Test
  public void testSimpleAlias() {
    String source = "goog.scope(function() {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "});";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    scopedAliases.process(null, ast.get(0));

    Node output = ast.get(ast.size() - 1);
    assertNotNull(output);
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);",
        output.toString());
  }

  @Test
  public void testMultipleAliases() {
    String source = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "var A = goog.dom.querySelectorAll;" +
        "goog.dom.createElement(DIV);" +
        "goog.dom.querySelectorAll('div');" +
        "});";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    scopedAliases.process(null, ast.get(0));

    Node output = ast.get(ast.size() - 1);
    assertNotNull(output);
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "goog.dom.querySelectorAll(com.google.dom.Document.documentElement.tagName);",
        output.toString());

    // Verify that the aliases have been correctly applied.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);
    for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
      Node aliasNode = ((AliasedNode) aliasUsage).aliasDefinition;
      Node aliasReferenceNode = ((AliasedNode) aliasUsage).aliasReference;
      assertEquals(aliasNode.getQualifiedName(), aliasReferenceNode.getQualifiedName());
    }
  }

  @Test
  public void testAliasRedefinition() {
    String source = "goog.scope(function() {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "var DIV = 'div';" +
        "goog.dom.createElement(DIV);" +
        "});";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    try {
      scopedAliases.process(null, ast.get(0));
      fail("Expected an error due to alias redefinition.");
    } catch (RuntimeException e) {
      // Expected error.
    }
  }

  @Test
  public void testAliasUsedOutsideScope() {
    String source = "goog.scope(function() {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "});" +
        "goog.dom.createElement(DIV);";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    try {
      scopedAliases.process(null, ast.get(0));
      fail("Expected an error due to alias used outside scope.");
    } catch (RuntimeException e) {
      // Expected error.
    }
  }

  @Test
  public void testAliasWithJSDoc() {
    String source = "/** @type {goog.dom.TagName} */ var DIV;\n" +
        "goog.scope(function() {" +
        "  /** @type {goog.dom.TagName} */\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "});";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    scopedAliases.process(null, ast.get(0));

    Node output = ast.get(ast.size() - 1);
    assertNotNull(output);
    assertEquals(
        "/** @type {com.google.dom.TagName} */\n" +
        "var DIV;\n" +
        "\n" +
        "goog.scope(function() {" +
        "  /** @type {com.google.dom.TagName} */\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "});\n" +
        "\n" +
        "/** @type {com.google.dom.TagName} */\n" +
        "var DIV = goog.dom.TagName.DIV;\n",
        output.toString());
  }

  @Test
  public void testAliasWithJSDocInsideScope() {
    String source = "/** @type {goog.dom.TagName} */ var DIV;\n" +
        "goog.scope(function() {" +
        "  /** @type {goog.dom.TagName} */\n" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "});";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    scopedAliases.process(null, ast.get(0));

    Node output = ast.get(ast.size() - 1);
    assertNotNull(output);
    assertEquals(
        "/** @type {com.google.dom.TagName} */\n" +
        "var DIV;\n" +
        "\n" +
        "goog.scope(function() {" +
        "  /** @type {com.google.dom.TagName} */\n" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "});\n" +
        "\n" +
        "/** @type {com.google.dom.TagName} */\n" +
        "var DIV = goog.dom.TagName.DIV;\n",
        output.toString());

    // Verify that the alias has been correctly applied.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, ast.get(0), traversal);
    assertEquals(1, traversal.getAliasUsages().size());
    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    Node aliasNode = ((AliasedNode) aliasUsage).aliasDefinition;
    Node aliasReferenceNode = ((AliasedNode) aliasUsage).aliasReference;
    assertEquals(aliasNode.getQualifiedName(), aliasReferenceNode.getQualifiedName());
  }

  @Test
  public void testAliasWithJSDocNotInsideScope() {
    String source = "/** @type {goog.dom.TagName} */ var DIV;\n" +
        "goog.scope(function() {" +
        "});\n" +
        "goog.dom.createElement(DIV);";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    try {
      scopedAliases.process(null, ast.get(0));
      fail("Expected an error due to alias JSDoc not inside scope.");
    } catch (RuntimeException e) {
      // Expected error.
    }
  }

  @Test
  public void testAliasWithJSDocAndFunctionName() {
    String source = "function foo() {" +
        "/** @type {goog.dom.TagName} */ var DIV;" +
        "goog.scope(function() {" +
        "  /** @type {goog.dom.TagName} */\n" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "});" +
        "goog.dom.createElement(DIV);" +
        "}" +
        "foo();";

    List<Node> ast = parseTestSource(compiler, source);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        new AliasTransformationHandler() {
          // This is a no-op for testing, but in production it would log the
          // alias transformation.
          @Override
          public AliasTransformation logAliasTransformation(
              String filePath, SourcePosition position) {
            return null;
          }
        });
    try {
      scopedAliases.process(null, ast.get(0));
      fail("Expected an error due to alias JSDoc and function name.");
    } catch (RuntimeException e) {
      // Expected error.
    }
  }

  @After
  public void tearDown() {
    compiler = null;
    preprocessorSymbolTable = null;
  }

  private List<Node> parseTestSource(AbstractCompiler compiler, String source) {
    Node root = parser(compiler, source, null, null, null);
    return Lists.newArrayList(root);
  }
}