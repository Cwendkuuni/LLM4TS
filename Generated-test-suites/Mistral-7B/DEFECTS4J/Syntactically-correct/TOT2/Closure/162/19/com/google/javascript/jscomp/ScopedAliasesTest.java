import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testValidScopedAliases() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(CompilerOptions.AliasTransformation.STRICT);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.createElement", aliasDefinitionsInOrder.get(0).getQualifiedName());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("dom.createElement", aliasUsage.getParent().getFirstChild().getString());
    assertEquals("goog.dom.createElement", aliasUsage.getAliasDefinition().getQualifiedName());

    Node trees = compiler.getOutput();
    Node root = trees.getFirstChild();
    assertTrue(NodeUtil.isFunction(root));
    assertEquals(1, root.getChildCount());
    Node body = root.getChildAtIndex(0);
    assertEquals(1, body.getChildCount());
    Node expression = body.getChildAtIndex(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", expression.toString());
  }

  @Test
  public void testInvalidScopedAliasUsesThis() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "this.dom = goog.dom;" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testInvalidScopedAliasUsesReturn() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "return goog.dom;" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testInvalidScopedAliasUsesThrow() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "throw new Error(\"test\");" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    List<DiagnosticType> errors = traversal.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(DiagnosticType.JSC_GOOG_SCOPE_USES_THROW, errors.get(0));
  }

  @Test
  public void testInvalidScopedAliasRedefinesAlias() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "dom.createElement = function() {}" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    List<DiagnosticType> errors = traversal.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(DiagnosticType.JSC_GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));
  }

  @Test
  public void testInvalidScopedAliasNonAliasLocal() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var elem = document.createElement('div');" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    List<DiagnosticType> errors = traversal.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(DiagnosticType.JSC_GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(0));
  }

  @Test
  public void testInvalidScopedAliasImproperlyUsed() {
    String javaScriptCode =
        "goog.scope(function() {" +
            "goog.scope(function() {" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "});";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node rootNode = compiler.parseTree(javaScriptCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    Traversal traversal = scopedAliases.getTraversal();
    List<DiagnosticType> errors = traversal.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }
}