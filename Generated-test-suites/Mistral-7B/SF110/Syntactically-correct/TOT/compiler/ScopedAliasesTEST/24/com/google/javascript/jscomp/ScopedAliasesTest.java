import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Test
  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.x = 42;" +
        "  assertEquals(42, x);" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.x", traversal.getAliasUsages().get(0).aliasReference.getString());
  }

  @Test
  public void testShadowedNamespace() {
    String input = "goog.scope(function() {" +
        "  var goog = { x: 1 };" +
        "  var y = goog.x;" +
        "  goog.x = 42;" +
        "  assertEquals(42, y);" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.x", traversal.getAliasUsages().get(0).aliasReference.getString());

    // Check that the namespace shadow "goog" is renamed correctly
    assertEquals("goog_shadow_0", traversal.getScopeCalls().get(0).getFirstChild().getString());
  }

  @Test(expected = AssertionError.class)
  public void testImproperUsageOfGoogScope() {
    String input = "goog.scope(function() {" +
        "  var x = 1;" +
        "  goog.x = 2;" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // This test should fail because goog.scope should be alone in a statement
    assertEquals(2, traversal.getScopeCalls().size());
  }

  @Test(expected = AssertionError.class)
  public void testWrongNumberOfParameters() {
    String input = "goog.scope(function(x) {" +
        "  var y = goog.x;" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // This test should fail because goog.scope should take only a single parameter.
    // It must be an anonymous function that itself takes no parameters.
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(2, traversal.getScopeCalls().get(0).getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeRefersToThis() {
    String input = "goog.scope(function() {" +
        "  this.x = 1;" +
        "  var y = this.x;" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // This test should fail because the body of a goog.scope function cannot reference 'this'.
    assertEquals(1, traversal.getScopeCalls().size());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsesReturn() {
    String input = "goog.scope(function() {" +
        "  return 1;" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // This test should fail because the body of a goog.scope function cannot use 'return'.
    assertEquals(1, traversal.getScopeCalls().size());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeUsesThrow() {
    String input = "goog.scope(function() {" +
        "  throw new Error('Test');" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // This test should fail because the body of a goog.scope function cannot use 'throw'.
    assertEquals(1, traversal.getScopeCalls().size());
  }

  @Test
  public void testNoAliases() {
    String input = "goog.scope(function() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "});";

    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = compiler.parseTree(input);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
  }
}