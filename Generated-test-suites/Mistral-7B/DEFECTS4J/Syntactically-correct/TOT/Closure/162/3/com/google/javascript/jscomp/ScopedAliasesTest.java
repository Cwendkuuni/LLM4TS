import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    options = new CompilerOptions();
    diagnostics = new Diagnostics();
  }

  @Test
  public void testAliasTransformation() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new AliasTransformationHandler() {
          @Nullable
          @Override
          public AliasTransformation getAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation() {
              @Override
              public String getAlias(String name) {
                if (name.equals("dom")) {
                  return "goog.dom";
                }
                return null;
              }
            };
          }
        });

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        NodeUtil.printToSourceString(rootNode.getLastChild()));

    assertEquals(0, diagnostics.getNumErrors());
    assertEquals(0, diagnostics.getNumWarnings());
  }

  @Test
  public void testBadGoogScopeCall() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "})();"; // Extra parentheses

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("Unexpected token ')'", diagnostics.getError(0).getMessage());
  }

  @Test
  public void testGoogScopeWithThis() {
    String sourceCode = "goog.scope(function() {" +
        "  this.dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("goog.scope functions cannot reference 'this'",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testGoogScopeWithReturn() {
    String sourceCode = "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("goog.scope functions cannot use 'return'",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testGoogScopeWithThrow() {
    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('Something went wrong');" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("goog.scope functions cannot use 'throw'",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testGoogScopeWithAliasRedefinition() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("Alias 'dom' is assigned a value more than once",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testGoogScopeWithNonAliasLocal() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var elem = document.createElement('DIV');" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("Local variable 'elem' is in a goog.scope and is not an alias",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testNestedGoogScope() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.scope(function() {" +
        "    var DIV = dom.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        NodeUtil.printToSourceString(rootNode.getLastChild()));

    assertEquals(0, diagnostics.getNumErrors());
    assertEquals(0, diagnostics.getNumWarnings());
  }

  @Test
  public void testAliasTransformationHandler() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new AliasTransformationHandler() {
          @Nullable
          @Override
          public AliasTransformation getAliasTransformation(
              String fileName, SourcePosition position) {
            return new AliasTransformation() {
              @Override
              public String getAlias(String name) {
                if (name.equals("dom")) {
                  return "google.dom";
                }
                return null;
              }
            };
          }
        });

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("google.dom.createElement(google.dom.TagName.DIV)",
        NodeUtil.printToSourceString(rootNode.getLastChild()));

    assertEquals(0, diagnostics.getNumErrors());
    assertEquals(0, diagnostics.getNumWarnings());
  }

  @Test
  public void testEmptyScope() {
    String sourceCode = "goog.scope(function() {});";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals(0, rootNode.getChildCount());
    assertEquals(0, diagnostics.getNumErrors());
    assertEquals(0, diagnostics.getNumWarnings());
  }

  @Test
  public void testNestedFunctionWithScope() {
    String sourceCode = "function outerFunction() {" +
        "  goog.scope(function innerFunction() {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "}" +
        "outerFunction();";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("outerFunction()", NodeUtil.printToSourceString(rootNode));
    assertEquals(0, diagnostics.getNumErrors());
    assertEquals(0, diagnostics.getNumWarnings());
  }

  @Test
  public void testNestedFunctionWithoutScope() {
    String sourceCode = "function outerFunction() {" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  function innerFunction() {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  }" +
        "}" +
        "outerFunction();";

    compiler.compile(sourceCode, null, options, diagnostics);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, null);

    Node rootNode = compiler.getRootNode();

    scopedAliasesPass.process(null, rootNode);

    assertEquals("outerFunction()", NodeUtil.printToSourceString(rootNode));
    assertEquals(0, diagnostics.getNumErrors());
    assertEquals(0, diagnostics.getNumWarnings());
  }
}