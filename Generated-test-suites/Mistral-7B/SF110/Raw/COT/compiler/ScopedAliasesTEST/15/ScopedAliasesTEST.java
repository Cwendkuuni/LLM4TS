import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testProcess_EmptyScript() {
    Node root = Node.empty();
    scopedAliases.process(null, root);
    // No changes made, so no error reported.
  }

  @Test
  public void testProcess_SimpleScript() {
    String script = "goog.scope(function() {" +
        "var x = goog.x;" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The test does not check the specific values of the transformed code,
    // because it can vary based on the compiler options. Instead, we check
    // that the expected number of nodes are modified.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
  }

  @Test
  public void testProcess_FunctionWithArguments() {
    String script = "goog.scope(function(x, y) {" +
        "var z = goog.x;" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_MultipleScopeCalls() {
    String script = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "});" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithReturnStatement() {
    String script = "goog.scope(function() {" +
        "return goog.x;" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithThrowStatement() {
    String script = "goog.scope(function() {" +
        "throw new Error('Test');" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithThisKeyword() {
    String script = "goog.scope(function() {" +
        "this.x = goog.x;" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithParamNamedThis() {
    String script = "goog.scope(function(this) {" +
        "this.x = goog.x;" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);
  }

  @Test
  public void testProcess_FunctionWithAliases() {
    String script = "goog.scope(function() {" +
        "var x = goog.x;" +
        "var y = goog.y;" +
        "var z = goog.z;" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    when(compiler.report(any(DiagnosticType.class))).thenReturn(null);

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(3, aliasDefinitions.size());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(3, aliasUsages.size());

    // Check the aliases were applied in the correct order.
    Node xAliasDefinition = aliasDefinitions.get(0);
    assertEquals("goog.x", xAliasDefinition.getString());

    Node yAliasDefinition = aliasDefinitions.get(1);
    assertEquals("goog.y", yAliasDefinition.getString());

    Node zAliasDefinition = aliasDefinitions.get(2);
    assertEquals("goog.z", zAliasDefinition.getString());

    Node xUsage = aliasUsages.get(0).getNode();
    assertEquals("x", xUsage.getString());

    Node yUsage = aliasUsages.get(1).getNode();
    assertEquals("y", yUsage.getString());

    Node zUsage = aliasUsages.get(2).getNode();
    assertEquals("z", zUsage.getString());
  }

  @Test
  public void testProcess_FunctionWithBleedingFunction() {
    String script = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "goog.functions.addBleedingFunction('goog.x', function() {" +
        "return 42;" +
        "});" +
        "});" +
        "});";
    Node root = Parser.parseCompilationUnit(script, new SourceFile("Test.js", 1));

    when(compiler.report(any(DiagnosticType.class))).thenReturn(null);

    scopedAliases.process(null, root);

    NodeUtil.tryMergeBlock(root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
  }
}