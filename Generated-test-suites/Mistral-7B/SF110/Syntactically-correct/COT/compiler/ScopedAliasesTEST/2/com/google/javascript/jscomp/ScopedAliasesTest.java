import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.NEVER);
    compiler = new AbstractCompiler(options);
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testBasicFunctionAlias() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertFalse(traversal.hasErrors());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());
  }

  @Test
  public void testFunctionWithParametersAlias() {
    String input = "goog.scope(function(a, b) {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV, a, b); " +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertFalse(traversal.hasErrors());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV, goog.dom, undefined)",
        root.toString());
  }

  @Test
  public void testFunctionWithThisAlias() {
    String input = "goog.scope(function() {" +
        "  this.x = 1; " +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = traversal.getDiagnostics();
    Diagnostics firstDiagnostic = diagnostics.get(0);
    assertEquals("The body of a goog.scope function cannot reference 'this'",
        firstDiagnostic.getMessage());
    assertEquals("goog.scope", firstDiagnostic.getSourceName());
    assertEquals(SourcePosition.INVALID_POSITION, firstDiagnostic.getPosition());
  }

  @Test
  public void testFunctionWithReturnAlias() {
    String input = "goog.scope(function() {" +
        "  return 1; " +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = traversal.getDiagnostics();
    Diagnostics firstDiagnostic = diagnostics.get(0);
    assertEquals("The body of a goog.scope function cannot use 'return'",
        firstDiagnostic.getMessage());
    assertEquals("goog.scope", firstDiagnostic.getSourceName());
    assertEquals(SourcePosition.INVALID_POSITION, firstDiagnostic.getPosition());
  }

  @Test
  public void testFunctionWithThrowAlias() {
    String input = "goog.scope(function() {" +
        "  throw new Error('Test error'); " +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = traversal.getDiagnostics();
    Diagnostics firstDiagnostic = diagnostics.get(0);
    assertEquals("The body of a goog.scope function cannot use 'throw'",
        firstDiagnostic.getMessage());
    assertEquals("goog.scope", firstDiagnostic.getSourceName());
    assertEquals(SourcePosition.INVALID_POSITION, firstDiagnostic.getPosition());
  }

  @Test
  public void testAliasRedefined() {
    String input = "goog.scope(function() {" +
        "  var x = 1;" +
        "  goog.scope(function() {" +
        "    var x = 2;" +
        "  });" +
        "  goog.scope(function() {" +
        "    var x = 3;" +
        "  });" +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = traversal.getDiagnostics();
    Diagnostics firstDiagnostic = diagnostics.get(0);
    assertEquals("The alias x is assigned a value more than once",
        firstDiagnostic.getMessage());
    assertEquals("goog.scope", firstDiagnostic.getSourceName());
    assertEquals(SourcePosition.INVALID_POSITION, firstDiagnostic.getPosition());
  }

  @Test
  public void testAliasWithParameter() {
    String input = "goog.scope(function(x) {" +
        "  var googX = goog.x;" +
        "  googX = x;" +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertFalse(traversal.hasErrors());
    assertEquals("goog.x = x;", root.toString());
  }

  @Test
  public void testAliasWithNamespace() {
    String input = "goog.scope(function() {" +
        "  var goog = {name: 'goog'};" +
        "  goog.x = goog.name;" +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertFalse(traversal.hasErrors());
    assertEquals("goog.name = goog;", root.toString());
  }

  @Test
  public void testAliasWithLocalVariable() {
    String input = "goog.scope(function() {" +
        "  var x = 1;" +
        "  goog.x = x;" +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = traversal.getDiagnostics();
    Diagnostics firstDiagnostic = diagnostics.get(0);
    assertEquals("The local variable x is in a goog.scope and is not an alias",
        firstDiagnostic.getMessage());
    assertEquals("goog.scope", firstDiagnostic.getSourceName());
    assertEquals(SourcePosition.INVALID_POSITION, firstDiagnostic.getPosition());
  }

  @Test
  public void testAliasWithNamespaceShadow() {
    String input = "goog.scope(function() {" +
        "  var goog = {name: 'goog'};" +
        "  goog.x = goog.name;" +
        "  var x = 1;" +
        "});";
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new Traversal());
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = traversal.getDiagnostics();
    Diagnostics firstDiagnostic = diagnostics.get(0);
    assertEquals("The local variable x is in a goog.scope and shadows the alias name",
        firstDiagnostic.getMessage());
    assertEquals("goog.scope", firstDiagnostic.getSourceName());
    assertEquals(SourcePosition.INVALID_POSITION, firstDiagnostic.getPosition());
  }
}