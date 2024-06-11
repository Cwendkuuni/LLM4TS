import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    options.aliasTransformation = AliasTransformation.GOOG_SCOPE;
    transformationHandler = new TestAliasTransformationHandler();
    compiler.setOptions(options);
    compiler.setAliasTransformationHandler(transformationHandler);
  }

  /**
   * Test the happy path case: goog.scope is used correctly with a single
   * parameter.
   */
  @Test
  public void testHappyPath() {
    String input =
        "goog.scope(function() {" +
            "goog.log('Hello world');" +
            "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // The code should have been transformed to "goog.log('Hello world')"
    // and the goog.scope call should have been removed.
    assertFalse(traversal.hasErrors());
    assertNull(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertNull(traversal.getScopeCalls().isEmpty());
  }

  /**
   * Test the error case: goog.scope is used improperly (with multiple
   * statements).
   */
  @Test
  public void testImproperUsage() {
    String input =
        "goog.scope(function() {" +
            "goog.log('Hello');" +
            "goog.log('World');" +
            "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_USED_IMPROPERLY error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, traversal.getDiagnostics().get(0).getType());
  }

  /**
   * Test the error case: goog.scope is used with bad parameters.
   */
  @Test
  public void testBadParameters() {
    String input =
        "goog.scope(function(x, y) {" +
            "goog.log('Hello world');" +
            "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_HAS_BAD_PARAMETERS error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, traversal.getDiagnostics().get(0).getType());
  }

  /**
   * Test the error case: goog.scope references 'this'.
   */
  @Test
  public void testRefersThis() {
    String input = "goog.scope(function() {" +
        "this.x = 1;" +
        "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_REFERENCES_THIS error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, traversal.getDiagnostics().get(0).getType());
  }

  /**
   * Test the error case: goog.scope uses 'return'.
   */
  @Test
  public void testUsesReturn() {
    String input = "goog.scope(function() {" +
        "return 'Hello world';" +
        "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_USES_RETURN error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USES_RETURN, traversal.getDiagnostics().get(0).getType());
  }

  /**
   * Test the error case: goog.scope uses 'throw'.
   */
  @Test
  public void testUsesThrow() {
    String input = "goog.scope(function() {" +
        "throw new Error('Hello world');" +
        "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_USES_THROW error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USES_THROW, traversal.getDiagnostics().get(0).getType());
  }

  /**
   * Test the alias redefinition error case.
   */
  @Test
  public void testAliasRedefinition() {
    String input =
        "goog.scope(function() {" +
            "var x = goog.x;" +
            "var x = 1;" +
            "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_ALIAS_REDEFINED error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getDiagnostics().get(0).getType());
  }

  /**
   * Test the case where a local variable is in a goog.scope but is not an alias.
   */
  @Test
  public void testNonAliasLocal() {
    String input =
        "goog.scope(function() {" +
            "var x = 1;" +
            "});";

    Node root = NodePrinter.parseJs(input);
    compiler.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be a GOOG_SCOPE_NON_ALIAS_LOCAL error for this code.
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, traversal.getDiagnostics().get(0).getType());
  }
}

class TestAliasTransformationHandler implements AliasTransformationHandler {
  @Override
  public void startProcessing() {}

  @Override
  public void endProcessing() {}

  @Override
  public void logAliasTransformation(String fileName, SourcePosition pos) {
    // Do nothing.
  }
}