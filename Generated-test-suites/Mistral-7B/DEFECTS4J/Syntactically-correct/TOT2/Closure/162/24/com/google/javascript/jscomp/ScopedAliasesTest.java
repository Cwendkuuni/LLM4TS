import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.diagnostic.DiagnosticType;

import java.io.IOException;
import java.util.List;

public class ScopedAliasesTest {

  @org.junit.Test
  public void testValidScopedAliases() throws IOException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    String input =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = new SourceFile("test.js", input);
    Node root = sourceFile.getTree();

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(4, diagnostics.size());

    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(1));
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(2));
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(3));
  }

  @org.junit.Test
  public void testScopeWithoutError() throws IOException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    String input =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = new SourceFile("test.js", input);
    Node root = sourceFile.getTree();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @org.junit.Test
  public void testScopeWithError() throws IOException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    String input =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement('DIV');" +
            "  return 'foo';" +
            "});";

    SourceFile sourceFile = new SourceFile("test.js", input);
    Node root = sourceFile.getTree();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());

    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0));
  }

  @org.junit.Test
  public void testScopeWithMultipleErrors() throws IOException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    String input =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement('DIV');" +
            "  this.foo = 'bar';" +
            "  return 'foo';" +
            "});";

    SourceFile sourceFile = new SourceFile("test.js", input);
    Node root = sourceFile.getTree();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());
    assertTrue(traversal.hasErrors());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(2, diagnostics.size());

    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(1));
  }

  @org.junit.Test
  public void testScopeWithAliases() throws IOException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    String input =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  var domElement = goog.dom.createElement(DIV);" +
            "  goog.dom.appendChild(domElement, goog.dom.createTextNode('Hello World!'));" +
            "  goog.dom.document.body.appendChild(domElement);" +
            "  goog.scope('function() { var DIV = goog.dom.TagName.DIV; }')" +
            "});";

    SourceFile sourceFile = new SourceFile("test.js", input);
    Node root = sourceFile.getTree();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("DIV", traversal.getAliasDefinitionsInOrder().get(0).getString());

    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @org.junit.Test
  public void testScopeWithMultipleAliases() throws IOException {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    String input =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  var divElement = goog.dom.createElement(DIV);" +
            "  goog.dom.appendChild(divElement, goog.dom.createTextNode('Hello World!'));" +
            "  goog.scope('function() {' +" +
            "    var DIV = goog.dom.TagName.DIV;" +
            "    var DIV2 = goog.dom.TagName.DIV;" +
            "  }')" +
            "  goog.dom.document.body.appendChild(divElement);" +
            "});";

    SourceFile sourceFile = new SourceFile("test.js", input);
    Node root = sourceFile.getTree();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("DIV", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("DIV2", traversal.getAliasDefinitionsInOrder().get(1).getString());

    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }
}