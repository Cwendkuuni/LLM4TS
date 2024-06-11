import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  private static final String TEST_SRC = "" +
      "goog.scope(function() {" +
      "  var dom = goog.dom;" +
      "  var DIV = goog.dom.TagName.DIV;" +
      "  var x = goog.x;" +
      "  var y = goog.y;" +
      "  var z = goog.z;" +
      "  goog.scope(function() {" +
      "    var dom = goog.dom;" +
      "    var DIV = goog.dom.TagName.DIV;" +
      "    goog.x = 1;" +
      "    goog.y = 2;" +
      "    goog.z = 3;" +
      "  });" +
      "  goog.scope(function() {" +
      "    var dom = goog.dom;" +
      "    var DIV = goog.dom.TagName.DIV;" +
      "    var x = 10;" +
      "    var y = 20;" +
      "    var z = 30;" +
      "  });" +
      "  dom.createElement(DIV);" +
      "});";

  private static void test(Compiler compiler, String src,
      Traversal.AliasUsage[] expectedUsages) {
    SourceFile source = new SourceFile("test.js", src, "");
    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseInitialSrc(source, null);
    pass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<Traversal.AliasUsage> actualUsages = traversal.getAliasUsages();
    assertArrayEquals(expectedUsages, actualUsages.toArray(new Traversal.AliasUsage[0]));
  }

  public static void main(String[] args) {
    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);

    // Test without any scoped aliases.
    test(compiler, "", null);

    // Test with simple scoped aliases.
    test(compiler, TEST_SRC,
        new Traversal.AliasedNode[] {
            new Traversal.AliasedNode(
                findNode(compiler, "x", root),
                findNode(compiler, "goog.x", root))
        });

    // Test with multiple scoped aliases.
    test(compiler, TEST_SRC,
        new Traversal.AliasedNode[] {
            new Traversal.AliasedNode(
                findNode(compiler, "x", findNode(compiler, "goog.scope", root)),
                findNode(compiler, "goog.x", findNode(compiler, "goog.scope", root))),
            new Traversal.AliasedNode(
                findNode(compiler, "y", findNode(compiler, "goog.scope", root)),
                findNode(compiler, "goog.y", findNode(compiler, "goog.scope", root))),
            new Traversal.AliasedNode(
                findNode(compiler, "z", findNode(compiler, "goog.scope", root)),
                findNode(compiler, "goog.z", findNode(compiler, "goog.scope", root)))
        });

    // Test with scoped alias redefinition.
    test(compiler, TEST_SRC,
        new Traversal.AliasedNode[] {
            new Traversal.AliasedNode(
                findNode(compiler, "x", findNode(compiler, "goog.scope", root)),
                findNode(compiler, "goog.x", findNode(compiler, "goog.scope", root))),
            new Traversal.AliasedNode(
                findNode(compiler, "x", findNode(compiler, "goog.scope", findNode(compiler, "goog.scope", root))),
                findNode(compiler, "goog.x", findNode(compiler, "goog.scope", findNode(compiler, "goog.scope", root))))
        });

    // Test with scoped alias in function body.
    test(compiler,
        "goog.scope(function() {" +
        "  function foo() {" +
        "    var x = goog.x;" +
        "  }" +
        "})",
        new Traversal.AliasedNode[] {
            new Traversal.AliasedNode(
                findNode(compiler, "x", findNode(compiler, "foo", root)),
                findNode(compiler, "goog.x", root))
        });

    // Test with scoped alias using this.
    test(compiler,
        "goog.scope(function() {" +
        "  this.x = goog.x;" +
        "})",
        new Traversal.AliasedNode[] {
            new Traversal.AliasedNode(
                findNode(compiler, "x", findNode(compiler, "goog.scope", root)),
                findNode(compiler, "goog.x", root))
        });

    // Test with scoped alias using return.
    test(compiler,
        "goog.scope(function() {" +
        "  return goog.x;" +
        "})",
        new Traversal.AliasedNode[] {});

    // Test with scoped alias using throw.
    test(compiler,
        "goog.scope(function() {" +
        "  throw goog.x;" +
        "})",
        new Traversal.AliasedNode[] {});

    // Test with scoped alias using non-alias local.
    test(compiler,
        "goog.scope(function() {" +
        "  var x = 10;" +
        "  var goog_x = goog.x;" +
        "})",
        new Traversal.AliasedNode[] {});

    // Test with scoped alias with forbidden local.
    test(compiler,
        "goog.scope(function() {" +
        "  goog.module('goog.dom')" +
        "  var x = goog.x;" +
        "})",
        new Traversal.AliasedNode[] {});

    // Test with scoped alias using a qualified name.
    test(compiler,
        "goog.scope(function() {" +
        "  var goog_dom = goog.dom;" +
        "  goog_dom.createElement(goog_dom.TagName.DIV);" +
        "})",
        new Traversal.AliasedNode[] {
            new Traversal.AliasedNode(
                findNode(compiler, "goog_dom", root),
                findNode(compiler, "goog.dom", root)),
            new Traversal.AliasedNode(
                findNode(compiler, "TagName.DIV", findNode(compiler, "goog_dom", root)),
                findNode(compiler, "goog.dom.TagName.DIV", root))
        });
  }

  private static Node findNode(Compiler compiler, String name, Node root) {
    Node node = NodeUtil.findFirstDescendant(compiler, root, node ->
        node.isName() && node.getString().equals(name));
    assertNotNull(node);
    return node;
  }
}