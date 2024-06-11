import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

/**
 * Tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  private static String TEST_SOURCE =
      "goog.scope(function() {" +
      "  var goog = {dom: {createElement: function(tag) { return tag; }}};" +
      "  goog.x = goog.dom.createElement('DIV');" +
      "});";

  @ org.junit.Test
  public void testNoErrors() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    List<Compiler.CompilationUnit> units = compiler.compile(TEST_SOURCE, null, options);
    assertEquals(1, units.size());
    Node rootNode = units.get(0).getRootNode();

    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();
    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertEquals(1, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedTraversal.getAliasUsages().size());
  }

  @ org.junit.Test
  public void testImproperUsage() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String badSource =
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "});";

    List<Compiler.CompilationUnit> units = compiler.compile(badSource, null, options);

    Node rootNode = units.get(0).getRootNode();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();

    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertTrue(scopedTraversal.getScopeCalls().get(0).hasError());
    assertEquals(0, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedTraversal.getAliasUsages().size());

    SourcePosition position = scopedTraversal.getScopeCalls().get(0).getSourcePosition();
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_USED_IMPROPERLY");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.report(position, error, new Object[]{"line " + position.getLineNumber(), "col " + position.getColumnNumber()});
    });
  }

  @ org.junit.Test
  public void testMultipleDefinitions() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String multipleDefinitionsSource =
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var x = 5;" +
        "});";

    List<Compiler.CompilationUnit> units = compiler.compile(multipleDefinitionsSource, null, options);

    Node rootNode = units.get(0).getRootNode();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();

    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertTrue(scopedTraversal.getScopeCalls().get(0).hasError());
    assertEquals(1, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedTraversal.getAliasUsages().size());

    SourcePosition position = scopedTraversal.getScopeCalls().get(0).getSourcePosition();
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_ALIAS_REDEFINED");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.report(position, error, new Object[]{"line " + position.getLineNumber(), "col " + position.getColumnNumber(), "alias 'x' is assigned a value more than once"});
    });
  }

  @ org.junit.Test
  public void testNonAliasLocalVariable() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String nonAliasLocalVariableSource =
        "goog.scope(function() {" +
        "  var x = 5;" +
        "});";

    List<Compiler.CompilationUnit> units = compiler.compile(nonAliasLocalVariableSource, null, options);

    Node rootNode = units.get(0).getRootNode();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();

    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertTrue(scopedTraversal.getScopeCalls().get(0).hasError());
    assertEquals(0, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedTraversal.getAliasUsages().size());

    SourcePosition position = scopedTraversal.getScopeCalls().get(0).getSourcePosition();
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.report(position, error, new Object[]{"line " + position.getLineNumber(), "col " + position.getColumnNumber(), "local variable 'x' is in a goog.scope and is not an alias"});
    });
  }

  @ org.junit.Test
  public void testThisKeyword() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String thisKeywordSource =
        "goog.scope(function() {" +
        "  this.x = 5;" +
        "});";

    List<Compiler.CompilationUnit> units = compiler.compile(thisKeywordSource, null, options);

    Node rootNode = units.get(0).getRootNode();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();

    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertTrue(scopedTraversal.getScopeCalls().get(0).hasError());
    assertEquals(0, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedTraversal.getAliasUsages().size());

    SourcePosition position = scopedTraversal.getScopeCalls().get(0).getSourcePosition();
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.report(position, error, new Object[]{"line " + position.getLineNumber(), "col " + position.getColumnNumber()});
    });
  }

  @ org.junit.Test
  public void testReturnStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String returnStatementSource =
        "goog.scope(function() {" +
        "  return;" +
        "});";

    List<Compiler.CompilationUnit> units = compiler.compile(returnStatementSource, null, options);

    Node rootNode = units.get(0).getRootNode();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();

    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertTrue(scopedTraversal.getScopeCalls().get(0).hasError());
    assertEquals(0, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedTraversal.getAliasUsages().size());

    SourcePosition position = scopedTraversal.getScopeCalls().get(0).getSourcePosition();
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.report(position, error, new Object[]{"line " + position.getLineNumber(), "col " + position.getColumnNumber()});
    });
  }

  @ org.junit.Test
  public void testThrowStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String throwStatementSource =
        "goog.scope(function() {" +
        "  throw new Error('Test');" +
        "});";

    List<Compiler.CompilationUnit> units = compiler.compile(throwStatementSource, null, options);

    Node rootNode = units.get(0).getRootNode();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Traversal scopedTraversal = (Traversal) traversal.getUserData();

    assertFalse(scopedTraversal.hasErrors());
    assertEquals(1, scopedTraversal.getScopeCalls().size());
    assertTrue(scopedTraversal.getScopeCalls().get(0).hasError());
    assertEquals(0, scopedTraversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedTraversal.getAliasUsages().size());

    SourcePosition position = scopedTraversal.getScopeCalls().get(0).getSourcePosition();
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_USES_THROW");
    assertThrows(IllegalArgumentException.class, () -> {
      compiler.report(position, error, new Object[]{"line " + position.getLineNumber(), "col " + position.getColumnNumber()});
    });
  }
}