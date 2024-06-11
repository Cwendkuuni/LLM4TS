import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test valid usage of ScopedAliases
    Node rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});");

    AbstractCompiler compiler = new AbstractCompiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);

    // Check that the alias has been applied correctly
    Node domNode = rootNode.getFirstChild().getFirstChild();
    assertEquals("goog.dom.createElement", domNode.getString());

    // Test error scenarios

    // Test improper usage of ScopedAliases
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "goog.scope(function() {});" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        scopedAliases.getDiagnostics().get(0).getType().getName());

    // Test ScopedAliases with bad parameters
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(1, function() {});" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        scopedAliases.getDiagnostics().get(0).getType().getName());

    // Test ScopedAliases referencing 'this'
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() { this.dom = goog.dom; });" +
        "console.log(this.dom);" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS",
        scopedAliases.getDiagnostics().get(0).getType().getName());

    // Test ScopedAliases using 'return'
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() { return dom; });" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN",
        scopedAliases.getDiagnostics().get(0).getType().getName());

    // Test ScopedAliases using 'throw'
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() { throw new Error(); });" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW",
        scopedAliases.getDiagnostics().get(0).getType().getName());

    // Test alias redefinition
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "});" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        scopedAliases.getDiagnostics().get(0).getType().getName());

    // Test non-alias local in goog.scope
    rootNode = createTestNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var x = 1;" +
        "});");
    scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, rootNode);
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL",
        scopedAliases.getDiagnostics().get(0).getType().getName());
  }

  private Node createTestNode(String code) {
    Node root = new Node_();
    new JavaScriptSource(new StringReader(code), code.length(), new SourcePosition(0, 0))
        .parse(root);
    return root;
  }
}