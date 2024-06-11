import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGrouping;
import com.google.javascript.jscomp.DiagnosticMessageCategory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.Parser;
import com.google.javascript.rhino.SourceFile;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, getClass().getClassLoader());

    SourceFile source = new SourceFile("test.js", "test");
    String code =
        "function evil() { this.useful = undefined; } " +
        "var a = function() { this.useful = undefined; };" +
        "function a() {} " +
        "a.x = function() {}; " +
        "function MyConstructor() {} " +
        "MyConstructor.prototype.x = function() {}; " +
        "var MyPrototype = { x: function() {} }; ";

    Parser parser = new Parser(source, null, null, null, null, null);
    Node ast = parser.parse();

    NodeTraversal traverser = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traverser.traverse(ast, null);
    DiagnosticMessageCategory category = traverser.getDiagnostics().getErrorCategories().get(0);
    assertEquals("expected 1 warning", 1, traverser.getDiagnostics().getNumErrors());
    assertEquals("expected warning category to be 'JSC_USED_GLOBAL_THIS'", JSC_USED_GLOBAL_THIS, category.getId());

    // Check that only the expected nodes were flagged.
    Node expectedFirst = ast.getFirstChild();
    Node current = expectedFirst;
    int count = 0;
    while (current != null) {
      if (current.getType() == Token.THIS) {
        Node parent = current.getParent();
        if (parent.getType() != Token.ASSIGN && parent == null) {
          fail("unexpected 'this' found: " + current);
        }
        count++;
      }
      current = current.getNext();
    }
    assertEquals("expected 3 'this' nodes to be flagged", 3, count);

    // Check that no other nodes were flagged.
    Node[] flaggedNodes = traverser.getDiagnostics().getDiagnostics().toArray(new Diagnostic[0]);
    for (Diagnostic diagnostic : flaggedNodes) {
      Node node = diagnostic.getSourceNode();
      assertNotNull(node);
      Node parent = node.getParent();
      if (parent != null && (parent.getType() == Token.ASSIGN || parent.getType() == Token.GETPROP)) {
        Node grandparent = parent.getParent();
        if (grandparent != null && grandparent.getType() == Token.NAME) {
          String name = grandparent.getString();
          assertTrue("expected name to be 'evil' or 'a'", name.equals("evil") || name.equals("a"));
        }
      }
    }

    // Check that no other diagnostics were generated.
    DiagnosticGrouping grouping = traverser.getDiagnostics().getGrouping();
    assertEquals("expected exactly 1 diagnostic group", 1, grouping.getGroups().size());
    DiagnosticGroup group = grouping.getGroups().get(0);
    assertEquals("expected group to contain only 'JSC_USED_GLOBAL_THIS'", JSC_USED_GLOBAL_THIS, group.getCategory().getId());
  }
}