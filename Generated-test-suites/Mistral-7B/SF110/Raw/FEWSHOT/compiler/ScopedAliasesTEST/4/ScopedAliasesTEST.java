package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test cases for ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.SCOPED;
    Compiler compiler = new Compiler(options);

    String rootCode = "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    Node root = Node.parse(rootCode);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    // Check that no errors were reported.
    assertFalse(compiler.hasErrors());

    // Check that the alias definitions have been removed.
    assertTrue(root.getLastChild().isBlockStatement());
    Node scopeBlock = root.getLastChild();
    assertFalse(scopeBlock.getFirstChild().isCall());

    // Check that the alias usages have been replaced.
    Node elementCreationCall = scopeBlock.getChildAtIndex(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        elementCreationCall.toString());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.SCOPED;
    Compiler compiler = new Compiler(options);

    String rootCode = "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.scope(function() {" +
            "      goog.dom.createElement(DIV);" +
            "  });" +
            "});";

    Node root = Node.parse(rootCode);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    // Check that an error was reported for the nested goog.scope call.
    assertTrue(compiler.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        compiler.getErrors().get(0).getType().getName());
  }

  @Test
  public void testScopedAliasesWithParameter() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.SCOPED;
    Compiler compiler = new Compiler(options);

    String rootCode = "goog.scope(function(a) {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    Node root = Node.parse(rootCode);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    // Check that no errors were reported.
    assertFalse(compiler.hasErrors());

    // Check that the alias definitions have been removed.
    assertTrue(root.getLastChild().isBlockStatement());
    Node scopeBlock = root.getLastChild();
    assertFalse(scopeBlock.getFirstChild().isCall());

    // Check that the alias usages have been replaced.
    Node elementCreationCall = scopeBlock.getChildAtIndex(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        elementCreationCall.toString());

    // Check that the 'a' parameter has not been touched.
    Node parameterNode = scopeBlock.getChildAtIndex(1);
    assertEquals("a", parameterNode.getString());
  }

  @Test
  public void testScopedAliasesWithForbiddenLocal() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.SCOPED;
    Compiler compiler = new Compiler(options);

    String rootCode = "goog.scope(function() {" +
            "  var goog = {};" +
            "  goog.dom = {};" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    Node root = Node.parse(rootCode);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    // Check that an error was reported for the forbidden local 'goog'.
    assertTrue(compiler.hasErrors());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        compiler.getErrors().get(0).getType().getName());
  }

  @Test
  public void testScopedAliasesWithNsAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.SCOPED;
    Compiler compiler = new Compiler(options);

    String rootCode = "goog.scope(function() {" +
            "  var goog = {};" +
            "  goog.dom = {};" +
            "  goog.dom.TagName = {" +
            "      DIV: 0" +
            "  };" +
            "  goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    Node root = Node.parse(rootCode);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    pass.process(null, root);

    // Check that no errors were reported.
    assertFalse(compiler.hasErrors());

    // Check that the alias definitions have been removed.
    assertTrue(root.getLastChild().isBlockStatement());
    Node scopeBlock = root.getLastChild();
    assertFalse(scopeBlock.getFirstChild().isCall());

    // Check that the alias usages have been replaced.
    Node elementCreationCall = scopeBlock.getChildAtIndex(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        elementCreationCall.toString());

    // Check that the 'goog' alias has been renamed to avoid conflict with
    // the forbidden local 'goog'.
    Node googNode = scopeBlock.getChildAtIndex(1);
    assertEquals("goog__0", googNode.getString());
  }

  private void assertEquals(String expected, String actual) {
    assertEquals(expected, actual, null);
  }
}