import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {

  @Test
  public void testBasicAliasTransformation() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.SIMPLE);

    AbstractCompiler compiler = new AbstractCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new TestAliasTransformationHandler());
    scopedAliases.process(null, Node.toTree(sourceCode));

    Node root = scopedAliases.getCompiledTree();
    Node expected = Node.toTree("goog.dom.createElement(goog.dom.TagName.DIV);");
    assertEquals(expected, root);
  }

  @Test
  public void testErrorScenarios() {
    String sourceCode = "goog.scope(function() {" +
        "  // This should cause an error because 'this' is used." +
        "  this.foo = 42;" +
        "  goog.scope(function() {});" + // This should cause an error because it has a parameter."
        "});";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

    try {
      scopedAliases.process(null, Node.toTree(sourceCode));
      fail("Expected error.");
    } catch (RuntimeException e) {
      // Check that the correct errors were thrown.
      String[] messages = e.getMessage().split("\n");
      for (String message : messages) {
        if (message.startsWith("JSC_GOOG_SCOPE_REFERENCES_THIS")) {
          assertTrue(message.contains("goog.scope(function() { this.foo = 42;"));
        } else if (message.startsWith("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS")) {
          assertTrue(message.contains("goog.scope(function(p) {"));
        }
      }
    }
  }

  @Test
  public void testAliasTransformationHandlesComplexScenarios() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var NAMESPACE = {'foo': function() {}};" +
        "  var x = goog.x;" +
        "  var y = goog.NAMESPACE.foo.x;" +
        "  goog.scope(function() {" +
        "    var x = 'inner-x';" +
        "  });" +
        "  goog.scope(function() {" +
        "    var NAMESPACE = {'bar': function() {}};" +
        "  });" +
        "  goog.scope(function() {" +
        "    var y = 'inner-y';" +
        "  });" +
        "  goog.scope(function() {" +
        "    var x = 'inner-x';" +
        "    var y = 'inner-y';" +
        "  });" +
        "  goog.scope(function() {" +
        "    var x = goog.x;" +
        "    var y = goog.NAMESPACE.foo.x;" +
        "  });" +
        "});";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

    Node root = scopedAliases.process(null, Node.toTree(sourceCode)).getCompiledTree();

    // Check that the alias transformations were performed correctly.
    Node xNode1 = root.getChildAtIndex(3);
    Node xNode2 = root.getChildAtIndex(11);
    Node xNode3 = root.getChildAtIndex(19);
    Node xNode4 = root.getChildAtIndex(25);
    Node yNode1 = root.getChildAtIndex(8);
    Node yNode2 = root.getChildAtIndex(21);
    Node yNode3 = root.getChildAtIndex(28);
    Node yNode4 = root.getChildAtIndex(32);

    assertEquals("goog.NAMESPACE.foo.x", xNode1.getString());
    assertEquals("goog.x", xNode2.getString());
    assertEquals("goog.NAMESPACE.foo.x", xNode3.getString());
    assertEquals("goog.x", xNode4.getString());

    assertEquals("goog.NAMESPACE.foo.x", yNode1.getString());
    assertEquals("goog.NAMESPACE.bar.x", yNode2.getString());
    assertEquals("inner-x", xNode4.getString());
    assertEquals("inner-y", yNode3.getString());
  }

  /**
   * A {@link AliasTransformationHandler} for testing purposes.
   */
  private static class TestAliasTransformationHandler
      implements AliasTransformationHandler {
    @Override
    public void logAliasTransformation(String filePath,
        SourcePosition position, String aliasName, String aliasValue) {
      System.out.printf(
          "Alias transformation: %s -> %s at %s:%d:%d%n", aliasName, aliasValue,
          filePath, position.getLineNumber(), position.getCharacterNumber());
    }
  }

  /**
   * Helper method to get the compiled tree from a {@link HotSwapCompilerPass}.
   */
  private Node getCompiledTree(HotSwapCompilerPass pass) {
    return pass.getCompiledTree();
  }
}